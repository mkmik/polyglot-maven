/*
 * Copyright (C) 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sonatype.maven.polyglot.scala

import scala.tools.nsc.{GenericRunnerSettings, ScriptRunner}

/**
 * <p>
 * Compiles a PMaven Scala script. Generates a JAR in the "target/pmaven-scala"
 * directory. Will avoid recompilation when the original PMaven Scala file is
 * older than the generated JAR file.
 * </p>
 *
 * <p>
 * Always generates a object with the name "ModelGenerator" in the default
 * package. This object will conform to the following structural type:
 * </p>
 *
 * <p><blockquote><pre><b>
 * type modelGen = {
 *   def genModel: Model
 * }
 * </b></pre></blockquote></p>
 *
 * <p>
 * The ScalaModelReader loads the ModelGenerator class and invokes the
 * genModel method in order to create a Model object for the project.
 * </p>
 **/
object PMavenScriptRunner extends ScriptRunner {
  override protected def preambleCode(objectName: String): String = {
    val (maybePack, objName)  = splitObjectName(objectName)
    val packageDecl           = maybePack map ("package %s\n" format _) getOrElse ("")

    return """|
    |  // ignorable main class with main method. Executed automatically
    |  // as part of the script compilation process. 
    |  object %s {
    |    def main(argv: Array[String]): Unit = { }
    |  }
    |
    |  object ModelGenerator {
    |    def genModel: Model = {
    |      import org.sonatype.maven.polyglot.scala.model.Project._
    |
    |""".stripMargin.format(objName)
  }

  override val endCode = """
    |    }
    |  }
    |""".stripMargin
}
