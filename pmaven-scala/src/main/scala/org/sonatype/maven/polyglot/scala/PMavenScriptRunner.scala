package org.sonatype.maven.polyglot.scala

import scala.tools.nsc.{GenericRunnerSettings, CompileClient, Settings, CompileSocket, Global}
import scala.tools.nsc.reporters.ConsoleReporter
import scala.tools.nsc.io.{Directory, File, Path, PlainFile}
import scala.tools.nsc.util.{CompoundSourceFile, BatchSourceFile, SourceFile, SourceFileFragment}

import java.io.{InputStream, OutputStream, BufferedReader, FileInputStream, FileOutputStream,
  FileReader, InputStreamReader, PrintWriter, FileWriter, IOException, Reader, StringWriter,
  Writer, File => JFile}
import java.util.jar.{JarEntry, JarOutputStream}

import org.codehaus.plexus.logging.Logger

object PMavenScriptCompiler {

  /**
   * Utility method to make sure something happens, even if there's a global
   * failure of some sort.
   **/
  private def addShutdownHook(body: => Unit) =
    Runtime.getRuntime addShutdownHook new Thread { override def run { body } }

  /**
   * Statically-defined code inserted before PMaven script, which effectively
   * makesth e script code the bod yof the generateModel method of an object
   * named ModelGenerator.
   */
  def preambleCode: String = """
    | object ModelGenerator {
    |   def generateModel: Model = {
    |     import org.sonatype.mavem.polyglot.scala.model.Project._
  """.stripMargin
  
  def endCode: String = """
    |   }
    | }
  """.stripMargin
  
  def copyStreams(in: InputStream, out: OutputStream) = {
    val buf = new Array[Byte](10240)
    
    def loop: Unit = in.read(buf, 0, buf.length) match {
      case -1 => in.close()
      case n  => out.write(buf, 0, n) ; loop
    }
    
    loop
  }
  
  def copyReaders(in: Reader, out: Writer) = {
    val buf = new Array[Char](10240)
    
    def loop: Unit = in.read(buf, 0, buf.length) match {
      case -1 => in.close()
      case n  => out.write(buf, 0, n) ; loop
    }
    
    loop
  }  

  private def jarFileFor(scriptFile: String): File = {
    val name =
      if (scriptFile endsWith ".jar") scriptFile
      else scriptFile + ".jar"
    
    File(name)
  }
  
  private def tryMakeJar(jarFile: File, sourcePath: Directory) = {
    def addFromDir(jar: JarOutputStream, dir: Directory, prefix: String) {
      def addFileToJar(entry: File) = {
        jar putNextEntry new JarEntry(prefix + entry.name)
        copyStreams(entry.inputStream, jar)
        jar.closeEntry
      }

      dir.list foreach { entry =>
        if (entry.isFile) addFileToJar(entry.toFile)
        else addFromDir(jar, entry.toDirectory, prefix + entry.name + "/")
      }
    }

    try {
      val jar = new JarOutputStream(jarFile.outputStream())
      addFromDir(jar, sourcePath, "")
      jar.close
    } 
    catch {
      case _: Error => jarFile.delete() // XXX what errors to catch?
    }
  }

  /**
   * Compile a script using the fsc compilation deamon.
   */
  private def compileWithDaemon(
      settings: GenericRunnerSettings,
      scriptFileIn: String): Boolean =
  {
    val scriptFile = CompileClient absFileName scriptFileIn
    
    {
      import settings._
      for (setting <- List(classpath, sourcepath, bootclasspath, extdirs, outdir)) {
        setting.value = CompileClient absFileName setting.value
      }
    }
      
    val compSettingNames  = new Settings(error).allSettings map (_.name)
    val compSettings      = settings.allSettings filter (compSettingNames contains _.name)
    val coreCompArgs      = compSettings flatMap (_.unparse)
    val compArgs          = coreCompArgs ::: List("-Xscript", "ModelGenerator", scriptFile)
    var compok            = true
        
    def ManagedResource[T](x: => T) = Some(x)
    
    for {
      socket <- ManagedResource(CompileSocket getOrCreateSocket "")
      val _ = if (socket == null) return false
      out <- ManagedResource(new PrintWriter(socket.getOutputStream(), true))
      in <- ManagedResource(new BufferedReader(new InputStreamReader(socket.getInputStream())))
    } {
      out println (CompileSocket getPassword socket.getPort)
      out println (compArgs mkString "\0")
      
      for (fromServer <- (Iterator continually in.readLine()) takeWhile (_ != null)) {
        Console.err println fromServer
        if (CompileSocket.errorPattern matcher fromServer matches)
          compok = false
      }
      in.close() ; out.close() ; socket.close()
    }
    
    compok
  }

  /**
   * Wrap a script file into a model generator object named
   * <code>ModelGenertaor</code>.
   */
  def wrappedScript(
    filename: String, 
    getSourceFile: (PlainFile) => BatchSourceFile): SourceFile = 
  {
    val preamble = new BatchSourceFile("<script preamble>", preambleCode toCharArray)
    val middle = {
      val bsf = getSourceFile(PlainFile fromPath filename)
      new SourceFileFragment(bsf, 0, bsf.length)
    }
    val end = new BatchSourceFile("<script trailer>", endCode.toCharArray)

    new CompoundSourceFile(preamble, middle, end)
  }

  /**
   * Compile a script and return the compiled jar file location
   * @returns Location of the compiled script
   */
  private def compileScript(
    logger: Logger,
    settings: GenericRunnerSettings,
    scriptFile: String): Option[String] =
  {
    /** Compiles the script file, and returns the directory with the compiled
     *  class files, if the compilation succeeded.
     */
    def compile: Option[Directory] = {
      val compiledPath = Directory makeTemp "scalascript"

      // delete the directory after the user code has finished
      addShutdownHook(compiledPath.deleteRecursively())

      settings.outdir.value = compiledPath.path

      if (settings.nocompdaemon.value) {
        val reporter = new ConsoleReporter(settings)
        val compiler = new Global(settings, reporter)
        val cr = new compiler.Run
        val wrapped = wrappedScript(scriptFile, compiler getSourceFile _)
        
        cr compileSources List(wrapped)
        if (reporter.hasErrors) None else Some(compiledPath)
      }
      else if (compileWithDaemon(settings, scriptFile)) Some(compiledPath)
      else None  	      
    }

    if (settings.savecompiled.value) {
      val jarFile = jarFileFor(scriptFile)
      def jarOK   = jarFile.canRead && (jarFile isFresher File(scriptFile))
      
      def recompile() = {
        jarFile.delete()
        
        compile match {
          case Some(compiledPath) =>
            tryMakeJar(jarFile, compiledPath)
            if (jarOK) {
              compiledPath.deleteRecursively()
              Some(jarFile.toAbsolute.path)
            }            
            else None
          case _  => None
        }
      }

      if (jarOK) Some(jarFile.toAbsolute.path) // pre-compiled jar is current
      else recompile()                         // jar old - recompile the script.
    }
    // don't use a cache jar at all--just use the class files--only here for
    // debugging/experimentation purposes since in production jars will always
    // be used.
    else compile map (cp => Some(cp.path)) getOrElse None
  }

  /** 
   * Compiels a temporary script file named "ModelGenerator.scala". Compiles the script in
   * to ModelGenerator.jar. Make sure the "-savecompiled" generic runner
   * setting is turned "no", or else the resultant .jar file will silently be
   * deleted.
   * 
   * @returns the compile script location as a file path name.
   */
  def compileCommandScript(
    logger: Logger,
    settings: GenericRunnerSettings,
    reader: Reader) : Option[String] = {
    val buffWriter = new StringWriter
    copyReaders(reader, buffWriter)
    
    val scriptFile = new File( new JFile("ModelGenerator.scala"))
    
    // stream the reader to the file
    scriptFile writeAll List(buffWriter.toString)
    
    try compileScript(logger, settings, scriptFile.path)
    finally scriptFile.delete()  // in case there was a compilation error
  }
}
