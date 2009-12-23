package org.sonatype.maven.polyglot.scala

import java.io.{StringReader, Reader}
import org.junit.runner.RunWith
import org.scalatest._
import matchers.ShouldMatchers
import junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ScalaModelReaderSpec extends WordSpec with ShouldMatchers {

  "ScalaModelReader.read" when {
    val reader = new ScalaModelReader
    val input = getClass.getResourceAsStream("/pom.scala")
    val emptyOptions = new java.util.HashMap[String, String]()

    "called with a null input and empty options" should {

      "throw an IllegalArgumentException" in {
        intercept[IllegalArgumentException] {
          reader.read(null, emptyOptions)
        }
      }
    }

    "called with a valid input and empty options" should {

      "return a valid model" in {
        val model = reader.read(input, emptyOptions)
        model should not be(null)
      }
    }
  }
}
