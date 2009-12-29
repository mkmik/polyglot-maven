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

import org.apache.maven.model.{
    Model => ApacheModel,
    Contributor => ApacheContributor,
    Developer => ApacheDeveloper
}

import scala.collection.JavaConversions._
import scala.collection.mutable.{Buffer, Map}

object project {

  def apply(body: (Model) => Unit): Model = {
    val m = new Model
    body(m)
    m
  }

}

class Model extends ApacheModel {
  def modelVersion: String = getModelVersion
  def modelVersion_=(s: String) = setModelVersion(s)
  
  def artifactId: String = getArtifactId
  def artifactId_=(s: String) = setArtifactId(s)
  
  def groupId: String = getGroupId
  def groupId_=(s: String) = setGroupId(s)
  
  def version: String = getVersion
  def version_=(s: String) = setVersion(s)
  
  def description: String = getDescription
  def description_=(s: String) = setDescription(s)
  
  def inceptionYear: String = getInceptionYear
  def inceptionYear_=(s: String) = setInceptionYear(s)
  
  def modelEncoding: String = getModelEncoding
  def modelEncoding_=(s: String) = setModelEncoding(s)
  
  def name: String = getName
  def name_=(s: String) = setName(s)
  
  def packaging: String = getPackaging
  def packaging_=(s: String) = setPackaging(s)
  
  def url: String = getUrl
  def url_=(s: String) = setUrl(s)
  
  val contributors = (getContributors: Buffer[ApacheContributor])
  def contributor(body: (Contributor) => Unit): Contributor = {
    val c = new Contributor
    body(c)
    addContributor(c)
    c
  }
  def contributor(name: String): Contributor =
    contributor (_.name = name)

  val developers = (getDevelopers: Buffer[ApacheDeveloper])
  def developer(body: (Developer) => Unit): Developer = {
    val d = new Developer
    body(d)
    addDeveloper(d)
    d
  }
  def developer(id: String): Developer =
    developer (_.id = id)
}

class Contributor extends ApacheContributor {
  def email: String = getEmail
  def email_=(s: String) = setEmail(s)
  
  def name: String = getName
  def name_=(s: String) = setName(s)
  
  def organization: String = getOrganization
  def organization_=(s: String) = setOrganization(s)
  
  def organizationUrl: String = getOrganizationUrl
  def organizationUrl_=(s: String) = setOrganizationUrl(s)
  
  def timezone: String = getTimezone
  def timezone_=(s: String)= setTimezone(s)
  
  def roles = (getRoles: Buffer[String])
  def properties = (getProperties: Map[java.lang.Object, java.lang.Object])
}

class Developer extends ApacheDeveloper {
  def id: String = getId
  def id_=(s: String) = setId(s)
}
