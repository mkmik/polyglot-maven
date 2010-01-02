package org.sonatype.maven.polyglot.scala

package object dsl {

  implicit def stringToDependency(s: String) = {
    // TODO Replace dummy implementation through real thing
    Dependency("a", "b", "1.0")
  }
}
