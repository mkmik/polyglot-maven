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
    
    val inputEmpty = getClass.getResourceAsStream("/pomEmpty.scala")
    val inputMinimal = getClass.getResourceAsStream("/pomMinimal.scala")
    
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
        val model = reader.read(inputEmpty, emptyOptions)
        model should not be(null)
      }
    }
    
    "called with minimal pom and empty options" should {
    
      "parse project coordinates" in {
        val model = reader.read(inputMinimal, emptyOptions)
        
        model.getModelVersion should equal ("4.0.0")
        model.getGroupId should equal ("org.blepharospasm")
        model.getArtifactId should equal ("squankdiliumtious")
        model.getVersion should equal ("0.0.0-SNAPSHOT")
      }
    }
  }
}
