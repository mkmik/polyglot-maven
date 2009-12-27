package org.sonatype.maven.polyglot.scala

import java.io.Reader
import java.util.{Map => JavaMap}
import org.apache.maven.model.{Model => MavenModelClass}
import org.apache.maven.model.io.ModelReader
import org.sonatype.maven.polyglot.io.ModelReaderSupport
import org.sonatype.maven.polyglot.scala.model.Model
import org.codehaus.plexus.component.annotations.{Component, Requirement}
import org.codehaus.plexus.logging.Logger

import scala.tools.nsc.GenericRunnerSettings

@Component(role=classOf[ModelReader], hint="scala")
class ScalaModelReader extends ModelReaderSupport {

  @Requirement val logger: Logger = null
  
  override def read(input: Reader, options: JavaMap[String, _]): MavenModelClass = {
    require(input != null, "Illegal argument: input must not be null!")

    def errorFn(err: String) {
    }
    
    val cmdLine = List("-savecompiled", "-d", "target/pmaven-scala")
    val settings = new GenericRunnerSettings(errorFn _)
    settings.parseParams(cmdLine)
        
    PMavenScriptCompiler.compileCommandScript(logger, settings, input) match {
      case Some(location) => 
        //...execute the script...
        new Model
      case None =>
        //...raise error...
        null
    }
  }
}
