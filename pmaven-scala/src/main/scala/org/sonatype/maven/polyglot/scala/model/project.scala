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

package org.sonatype.maven.polyglot.scala.model

import org.apache.maven._

object project {

  def apply(body: (Model) => Unit): Model = {
    val m = new Model
    body(m)
    m
  }

}

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
  def modelVersion: String = getModelVersion
  def modelVersion_=(s: String) = setModelVersion(s)
  
  def artifactId: String = getArtifactId
  def artifactId_=(s: String) = setArtifactId(s)
  
  def groupId: String = getGroupId
  def groupId_=(s: String) = setGroupId(s)
  
  def version: String = getVersion
  def version_=(s: String) = setVersion(s)
}

