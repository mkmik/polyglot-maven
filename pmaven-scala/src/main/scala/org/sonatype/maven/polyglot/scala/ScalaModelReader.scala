package org.sonatype.maven.polyglot
package scala

import io.ModelReaderSupport
import java.io.Reader
import java.util.{Map => JavaMap}
import org.apache.maven.model.Model

class ScalaModelReader extends ModelReaderSupport {

  override def read(input: Reader, options: JavaMap[String, _]) = {
    require(input != null, "Illegal argument: input must not be null!")

    // TODO Remove temporarily hack!
    new Model
  }
}
