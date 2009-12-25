package org.sonatype.maven.polyglot.scala

import java.io.Reader
import java.util.{Map => JavaMap}
import org.apache.maven.model.io.ModelReader
import org.sonatype.maven.polyglot.io.ModelReaderSupport
import org.sonatype.maven.polyglot.scala.model._
import org.codehaus.plexus.component.annotations.{Component, Requirement}
import org.codehaus.plexus.logging.Logger

@Component(role=classOf[ModelReader], hint="scala")
class ScalaModelReader extends ModelReaderSupport {

  @Requirement val logger: Logger = null
  
  override def read(input: Reader, options: JavaMap[String, _]) = {
    require(input != null, "Illegal argument: input must not be null!")

    // TODO Remove temporarily hack!
    new Model
  }
}
