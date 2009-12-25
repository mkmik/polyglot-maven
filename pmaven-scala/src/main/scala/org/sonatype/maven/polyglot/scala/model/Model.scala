package org.sonatype.maven.polyglot.scala.model

import org.apache.maven._

/**
 * <p>
 * Convenience subclass, adds _= mutators for scalar-typed
 * properties of the base class, and (X)=>Unit mutators as
 * well as _= for defining non-scalar-typed properties.
 * </p>
 *
 * <p>
 * These extra mutator methods are vital for being able to
 * made a DSL for defining builds as a hierarchical structure
 * of Scala-langauge strutures, such as:
 * </p>
 *
 * <code><blockindent><pre>
 * project {
 *   _.artifactId = "some.artifact.id"
 *
 *   parent {
 *     _.groupId = "some.group.id"
 *     _.version = "1.0-SNAPSHOT"
 *   }
 *   ...
 * }
 * </pre></blockindent></code>
 **/
class Model extends model.Model {
  def artifactId_=(s: String) = setArtifactId(s)  
  def groupId_=(s: String) = setGroupId(s)  
  def version_= (s: String) = setVersion(s)
}
