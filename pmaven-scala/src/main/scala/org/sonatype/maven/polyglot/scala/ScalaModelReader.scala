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
    
    val cmdLine = List("-savecompiled", "-nocompdaemon", "-d ./target/pmaven-scala")
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
