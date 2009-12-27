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
