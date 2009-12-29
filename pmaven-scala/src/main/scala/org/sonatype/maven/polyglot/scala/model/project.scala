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
    ModelBase => ApacheModelBase,
    Model => ApacheModel,
    Contributor => ApacheContributor,
    Developer => ApacheDeveloper,
    License => ApacheLicense,
    MailingList => ApacheMailingList,
    Profile => ApacheProfile,
    Dependency => ApacheDependency,
    Exclusion => ApacheExclusion,
    Activation => ApacheActivation,
    ActivationOS => ApacheActivationOS,
    ActivationProperty => ApacheActivationProperty,
    ActivationFile => ApacheActivationFile,
    DistributionManagement => ApacheDistributionManagement,
    Relocation => ApacheRelocation,
    RepositoryBase => ApacheRepositoryBase,
    Repository => ApacheRepository,
    RepositoryPolicy => ApacheRepositoryPolicy,
    DeploymentRepository => ApacheDeploymentRepository,
    Resource => ApacheResource,
    IssueManagement => ApacheIssueManagement,
    Scm => ApacheScm,
    Site => ApacheSite,
    CiManagement => ApacheCiManagement,
    Notifier => ApacheNotifier,
    Prerequisites => ApachePrerequisites,
    BuildBase => ApacheBuildBase,
    Build => ApacheBuild,
    Extension => ApacheExtension,
    Reporting => ApacheReporting,
    PluginManagement => ApachePluginManagement,
    ConfigurationContainer => ApacheConfigurationContainer,
    PluginExecution => ApachePluginExecution,
    Plugin => ApachePlugin,
    PluginConfiguration => ApachePluginConfiguration,
    PluginContainer => ApachePluginContainer,
    ReportPlugin => ApacheReportPlugin,
    ReportSet => ApacheReportSet
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

trait ModelBaseProps {
  self: ApacheModelBase =>
  
  def distributionManagement: ApacheDistributionManagement = getDistributionManagement
  def distributionManagement(body: (DistributionManagement) => Unit): DistributionManagement = {
    val d = new DistributionManagement
    body(d)
    setDistributionManagement(d)
    d
  }
  
  def modules = (getModules: Buffer[String])
  
  def repositories = (getRepositories: Buffer[ApacheRepository])
  def repository(body: (Repository) => Unit): Repository = {
    val r = new Repository
    body(r)
    addRepository(r)
    r
  }
  
  def pluginRepositories = (getPluginRepositories: Buffer[ApacheRepository])
  def pluginRepository(body: (Repository) => Unit): Repository = {
    val r = new Repository
    body(r)
    addPluginRepository(r)
    r
  }
  
  def dependencies = (getDependencies: Buffer[ApacheDependency])
  def dependency(body: (Dependency) => Unit): Dependency = {
    val d = new Dependency
    body(d)
    addDependency(d)
    d
  }
  
  def reporting: ApacheReporting = getReporting
  def reporting(body: (Reporting) => Unit): Reporting = {
    val r = new Reporting
    body(r)
    setReporting(r)
    r
  }

  def properties = (getProperties: Map[java.lang.Object, java.lang.Object])  
}

class Model extends ApacheModel with ModelBaseProps {
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
  
  def contributors = (getContributors: Buffer[ApacheContributor])
  def contributor(body: (Contributor) => Unit): Contributor = {
    val c = new Contributor
    body(c)
    addContributor(c)
    c
  }
  def contributor(name: String): Contributor =
    contributor (_.name = name)

  def developers = (getDevelopers: Buffer[ApacheDeveloper])
  def developer(body: (Developer) => Unit): Developer = {
    val d = new Developer
    body(d)
    addDeveloper(d)
    d
  }
  def developer(id: String): Developer =
    developer (_.id = id)

  def licenses = (getLicenses: Buffer[ApacheLicense])
  def license(body: (License) => Unit): License = {
    val l = new License
    body(l)
    addLicense(l)
    l
  }
  def license(iname: String): License =
    license (_.name = name)

  def mailingLists = (getMailingLists: Buffer[ApacheMailingList])
  def mailingList(body: (MailingList) => Unit): MailingList = {
    val ml = new MailingList
    body(ml)
    addMailingList(ml)
    ml
  }
  def mailingList(name: String): MailingList =
    mailingList (_.name = name)


  def profiles = (getProfiles: Buffer[ApacheProfile])
  def profile(body: (Profile) => Unit): Profile = {
    val p = new Profile
    body(p)
    addProfile(p)
    p
  }
  
  def issueManagement: ApacheIssueManagement = getIssueManagement
  def issueManagement(body: (IssueManagement) => Unit): IssueManagement = {
    val i = new IssueManagement
    body(i)
    setIssueManagement(i)
    i
  }
  
  def scm: ApacheScm = getScm
  def scm(body: (Scm) => Unit): Scm = {
    val s = new Scm
    body(s)
    setScm(s)
    s
  }
  
  def ciManagement: ApacheCiManagement = getCiManagement
  def ciManagement(body: (CiManagement) => Unit): CiManagement = {
    val c = new CiManagement
    body(c)
    setCiManagement(c)
    c
  }
  
  def prerequisites: ApachePrerequisites = getPrerequisites
  def prerequisites(body: (Prerequisites) => Unit): Prerequisites = {
    val p = new Prerequisites
    body(p)
    setPrerequisites(p)
    p
  }
  
  def build: ApacheBuild = getBuild
  def build(body: (Build) => Unit): Build = {
    val b = new Build
    body(b)
    setBuild(b)
    b
  }
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

class License extends ApacheLicense {
  def name: String = getName
  def name_=(s: String) = setName(s)
  
  def url: String = getUrl
  def url_=(s: String) = setUrl(s)
  
  def distribution: String = getDistribution
  def distribution_=(s: String) = setDistribution(s)
  
  def comments: String = getComments
  def comments_=(s: String) = setComments(s)
}

class MailingList extends ApacheMailingList {
  def name: String = getName
  def name_=(s: String) = setName(s)
  
  def subscribe: String = getSubscribe
  def subscribe_=(s: String) = setSubscribe(s)
  
  def unsubscribe: String = getUnsubscribe
  def unsubscribe_=(s: String) = setUnsubscribe(s)
  
  def post: String = getPost
  def post_=(s: String) = setPost(s)
  
  def archive: String = getArchive
  def archive_=(s: String) = setArchive(s)
  
  val otherArchives = (getOtherArchives: Buffer[String])
}

class Dependency extends ApacheDependency {
  def groupId: String = getGroupId
  def groupId_=(s: String) = setGroupId(s)

  def artifactId: String = getArtifactId
  def artifactId_=(s: String) = setArtifactId(s)
  
  def version: String = getVersion
  def version_=(s: String) = setVersion(s)
  
  /**
   * Using symbol "_type" to avoid Scala keyword "type" collision
   **/
  def _type: String = getType
  def _type_=(s: String) = setType(s)
  
  def classifier: String = getClassifier
  def classifier_=(s: String) = setClassifier(s)
  
  def scope: String = getScope
  def scope_=(s: String) = setScope(s)
  
  def systemPath: String = getSystemPath
  def systemPath_=(s: String) = setSystemPath(s)
  
  def optional: String = getOptional
  def optional_=(s: String) = setOptional(s)
  
  def exclusions = (getExclusions: Buffer[ApacheExclusion])
  def exclusion(body: (Exclusion) => Unit): Exclusion = {
    val e = new Exclusion
    body(e)
    addExclusion(e)
    e
  }
}

class Exclusion extends ApacheExclusion {
  def groupId: String = getGroupId
  def groupId_=(s: String) = setGroupId(s)
  
  def artifactId: String = getArtifactId
  def artifactId_=(s: String) = setArtifactId(s)
}

class Activation extends ApacheActivation {
  def activeByDefault: Boolean = isActiveByDefault
  def activeByDefault_=(f: Boolean) = setActiveByDefault(f)
  
  def jdk: String = getJdk
  def jdk_=(s: String) = setJdk(s)
  
  def os: ApacheActivationOS = getOs
  def os(body: (ActivationOS) => Unit): ActivationOS = {
    val a = new ActivationOS
    body(a)
    setOs(a)
    a
  }
  
  def property: ApacheActivationProperty = getProperty
  def property(body: (ActivationProperty) => Unit): ActivationProperty = {
    val a = new ActivationProperty
    body(a)
    setProperty(a)
    a
  }
  
  def file: ApacheActivationFile = getFile
  def file(body: (ActivationFile) => Unit): ActivationFile = {
    val a = new ActivationFile
    body(a)
    setFile(a)
    a
  }
}

class ActivationOS extends ApacheActivationOS {
  def name: String = getName
  def name_=(s: String) = setName(s)
  
  def family: String = getFamily
  def family_=(s: String) = setFamily(s)
  
  def arch: String = getArch
  def arch_=(s: String) = setArch(s)
  
  def version: String = getVersion
  def version_=(s: String) = setVersion(s)
}

class ActivationProperty extends ApacheActivationProperty{
  def name: String = getName
  def name_=(s: String) = setName(s)
  
  def value: String = getValue
  def value_=(s: String) = setValue(s)
}

class ActivationFile extends ApacheActivationFile {
  def exists: String = getExists
  def exists_=(s: String) = setExists(s)
  
  def missing: String = getMissing
  def missing_=(s: String) = setMissing(s)
}

class DistributionManagement extends ApacheDistributionManagement {
  def repository: ApacheDeploymentRepository = getRepository
  def repository(body: (DeploymentRepository) => Unit): DeploymentRepository = {
    val d = new DeploymentRepository
    body(d)
    setRepository(d)
    d
  }
  
  def snapshotRepository: ApacheDeploymentRepository = getSnapshotRepository
  def snapshotRepository(body: (DeploymentRepository) => Unit): DeploymentRepository = {
    val d = new DeploymentRepository
    body(d)
    setSnapshotRepository(d)
    d
  }  
  
  def site: ApacheSite = getSite
  def site(body: (Site) => Unit) = {
    val s = new Site
    body(s)
    setSite(s)
    s
  }
  
  def downloadUrl: String = getDownloadUrl
  def downloadUrl_=(s: String) = setDownloadUrl(s)
  
  def status: String = getStatus
  def status_=(s: String) = setStatus(s)
  
  def relocation: ApacheRelocation = getRelocation
  def relocation(body: (Relocation) => Unit): Relocation = {
    val r = new Relocation
    body(r)
    setRelocation(r)
    r
  }
}

class DeploymentRepository extends ApacheDeploymentRepository with RepositoryBaseProps {
  def uniqueVersion: Boolean = isUniqueVersion
  def uniqueVersion_=(b: Boolean) = setUniqueVersion(b)
}

class Relocation extends ApacheRelocation {
  def groupId: String = getGroupId
  def groupId_=(s: String) = setGroupId(s)
  
  def artifactId: String = getArtifactId
  def artifactId_=(s: String) = setArtifactId(s)
  
  def version: String = getVersion
  def version_=(s: String) = setVersion(s)
  
  def message: String = getMessage
  def message_=(s: String) = setMessage(s)
}

trait RepositoryBaseProps {
  self: ApacheRepositoryBase =>
  
  def id: String = getId
  def id_=(s: String) = setId(s)
  
  def name: String = getName
  def name_=(s: String) = setName(s)
  
  def url: String = getUrl
  def url_=(s: String) = setUrl(s)
  
  def layout: String = getLayout
  def layout_=(s: String) = setLayout(s)
}

class Repository extends ApacheRepository with RepositoryBaseProps {
  def releases: ApacheRepositoryPolicy = getReleases
  def releases(body: (RepositoryPolicy) => Unit): RepositoryPolicy = {
    val p = new RepositoryPolicy
    body(p)
    setReleases(p)
    p
  }

  def snapshots: ApacheRepositoryPolicy = getReleases
  def snapshots(body: (RepositoryPolicy) => Unit): RepositoryPolicy = {
    val p = new RepositoryPolicy
    body(p)
    setSnapshots(p)
    p
  }
}

class RepositoryPolicy extends ApacheRepositoryPolicy {
 def enabled: String = getEnabled
 def enabled_=(s: String) = setEnabled(s)
 
 def updatePolicy: String = getUpdatePolicy
 def updatePolicy_=(s: String) = setUpdatePolicy(s)
 
 def checksumPolicy: String = getChecksumPolicy
 def checksumPolicy_=(s: String) = setChecksumPolicy(s)
}

class Profile extends ApacheProfile with ModelBaseProps {
  def id: String = getId
  def id_=(s: String) = setId(s)
  
  def activation: ApacheActivation = getActivation
  def activation(body: (Activation) => Unit): Activation = {
    val a = new Activation
    body(a)
    setActivation(a)
    a
  }
  
  def build: ApacheBuildBase = getBuild
  def build(body: (Build) => Unit): Build = {
    val b = new Build
    body(b)
    setBuild(b)
    b
  }
}

trait BuildBaseProps {
  self: ApacheBuildBase =>
  
  def defaultGoal: String = getDefaultGoal
  def defaultoal_=(s: String) = setDefaultGoal(s)
  
  def directory: String = getDirectory
  def directory_=(s: String) = setDirectory(s)
  
  def finalName: String = getFinalName
  def finalName_=(s: String) = setFinalName(s)
  
  def resources = (getResources: Buffer[ApacheResource])
  def resource(body: (Resource) => Unit): Resource = {
    val r = new Resource
    body(r)
    addResource(r)
    r
  }

  def testResources = (getTestResources: Buffer[ApacheResource])  
  def testResource(body: (Resource) => Unit): Resource = {
    val r = new Resource
    body(r)
    addTestResource(r)
    r
  }
  
  def filters = (getFilters: Buffer[String])
}

class Build extends ApacheBuild with BuildBaseProps {
  def sourceDirectory: String = getSourceDirectory
  def sourceDirectory_=(s: String) = setSourceDirectory(s)
  
  def scriptSourceDirectory: String = getScriptSourceDirectory
  def scriptSourceDirectory_=(s: String) = setScriptSourceDirectory(s)
  
  def testSourceDirectory: String = getTestSourceDirectory
  def testSourceDirectory_=(s: String) = setTestSourceDirectory(s)
  
  def testOutputDirectory: String = getTestOutputDirectory
  def testOutputDirectory_=(s: String) = setTestOutputDirectory(s)
  
  def extensions = (getExtensions: Buffer[ApacheExtension])
  def extension(body: (Extension) => Unit): Extension = {
    val e = new Extension
    body(e)
    addExtension(e)
    e
  }
}

class Resource extends ApacheResource {
  def targetPath: String = getTargetPath
  def targetPath_=(s: String) = setTargetPath(s)
  
  def filtering: String = getFiltering
  def filtering_=(s: String) = setFiltering(s)
}

class IssueManagement extends ApacheIssueManagement {
  def system: String = getSystem
  def system_=(s: String) = setSystem(s)
  
  def url: String = getUrl
  def url_=(s: String) = setUrl(s)
}

class Scm extends ApacheScm {
  def connection: String = getConnection
  def connection_=(s: String) = setConnection(s)
  
  def developerConnection: String = getDeveloperConnection
  def developerConnection_=(s: String) = setDeveloperConnection(s)
  
  def tag: String = getTag
  def tag_=(s: String) = setTag(s)
  
  def url: String = getUrl
  def url_=(s: String) = setUrl(s)
}

class CiManagement extends ApacheCiManagement {
  def system: String = getSystem
  def system_=:(s: String) = setSystem(s)
 
  def url: String = getUrl
  def url_=(s: String) = setUrl(s)
 
  def notifiers = (getNotifiers: Buffer[ApacheNotifier])
  def notifier(body: (Notifier) => Unit): Notifier = {
    val n = new Notifier
    body(n)
    addNotifier(n)
    n
  }
}

class Notifier extends ApacheNotifier {
  /**
   * Using symbol "_type" to avoid Scala keyword collision
   **/
  def _type: String = getType
  def _type_=(s: String) = setType(s)
  
  def sendOnError: Boolean = isSendOnError
  def sendOnError_=(b: Boolean) = setSendOnError(b)
  
  def sendOnFailure: Boolean = isSendOnFailure
  def sendOnFailure_=(b: Boolean) = setSendOnFailure(b)
  
  def sendOnSuccess: Boolean = isSendOnSuccess
  def sendOnSuccess_=(b: Boolean) = setSendOnSuccess(b)
  
  def sendOnWarning: Boolean = isSendOnWarning
  def sendOnWarning_=(b: Boolean) = setSendOnWarning(b)

  def address: String = getAddress
  def address_=(s: String) = setAddress(s)
  
  def configuration = (getConfiguration: Map[java.lang.Object, java.lang.Object])
}

class Prerequisites extends ApachePrerequisites {
  def maven: String = getMaven
  def maven_=(s: String) = setMaven(s)
}

class Extension extends ApacheExtension {
  def groupId: String = getGroupId
  def groupId_=(s: String) = setGroupId(s)
  
  def artifactId: String = getArtifactId
  def artifactId_=(s: String) = setArtifactId(s)
  
  def version: String = getVersion
  def version_=(s: String) = setVersion(s)
}

class Reporting extends ApacheReporting {
  def excludeDefaults: String = getExcludeDefaults
  def excludeDefaults_=(s: String) = setExcludeDefaults(s)
  
  def outputDirectory: String = getOutputDirectory
  def outputDirectory_=(s: String) = setOutputDirectory(s)
  
  def plugins = (getPlugins: Buffer[ApacheReportPlugin])
  def plugin(body: (ReportPlugin) => Unit): ReportPlugin = {
    val r = new ReportPlugin
    body(r)
    addPlugin(r)
    r
  }
}

trait PluginContainerProps {
  self: ApachePluginContainer =>
  
  def plugins = (getPlugins: Buffer[ApachePlugin])
  def plugin(body: (Plugin) => Unit): Plugin = {
    val p = new Plugin
    body(p)
    addPlugin(p)
    p
  }
}

trait PluginConfigurationProps extends PluginContainerProps {
  self: ApachePluginConfiguration =>

  def pluginManagement: ApachePluginManagement = getPluginManagement
  def pluginManagement(body: (PluginManagement) => Unit): PluginManagement = {
    val m = new PluginManagement
    body(m)
    setPluginManagement(m)
    m
  }
}

class Plugin extends ApachePlugin with ConfigurationContainerProps {
  def groupId: String = getGroupId
  def groupId_=(s: String) = setGroupId(s)
  
  def artifactId: String = getArtifactId
  def artifactId_=(s: String) = setArtifactId(s)
  
  def version: String = getVersion
  def version_=(s: String) = setVersion(s)
  
  def extensions: String = getExtensions
  def extensions_=(s: String) = setExtensions(s)
  
  def executions = (getExecutions: Buffer[ApachePluginExecution])
  def execution(body: (PluginExecution) => Unit): PluginExecution = {
    val e = new PluginExecution
    body(e)
    addExecution(e)
    e
  }
  
  def dependencies = (getDependencies: Buffer[ApacheDependency])
  def dependency(body: (Dependency) => Unit): Dependency = {
    val d = new Dependency
    body(d)
    addDependency(d)
    d
  }
}

class PluginExecution extends ApachePluginExecution with ConfigurationContainerProps {
  def id: String = getId
  def id_=(s: String) = setId(id)
  
  def phase: String = getPhase
  def phase_=(s: String) = setPhase(s)
  
  def priority: Int = getPriority
  def priority_=(i: Int) = setPriority(i)
  
  def goals = (getGoals: Buffer[String])
}


trait ConfigurationContainerProps {
  self: ApacheConfigurationContainer =>
  
  def inherited: String = getInherited
  def inherited_=(s: String)= setInherited(s)
  
  def configuration: java.lang.Object = getConfiguration
  /**
   * Statically, base type defines this is an Object, but semantically it
   * is supposed to be a W3C DOM Element node. So this mutator method
   * adds a little type safety by requiring use of a W3C DOM Element.
   **/
  def configuration_=(elem: org.w3c.dom.Element) = setConfiguration(elem)
}

class PluginManagement extends ApachePluginManagement with PluginContainerProps {
  //nothing additional added to the inherited members.
}

class Site extends ApacheSite {
  def id: String = getId
  def id_=(s: String) = setId(s)
  
  def name: String = getName
  def name_=(s: String) = setName(s)
  
  def url: String = getUrl
  def url_=(s: String) = setUrl(s)
}

// Funny this doesn't inherit from ConfigurationContainer. Seems like an
// oversight in the Apache Maven model codebase.
class ReportPlugin extends ApacheReportPlugin {
  def groupId: String = getGroupId
  def groupId_=(s: String) = setGroupId(s)
  
  def artifactId: String = getArtifactId
  def artifactId_=(s: String) = setArtifactId(s)
  
  def version: String = getVersion
  def verion_=(s: String) = setVersion(s)
  
  def inherited: Boolean = isInherited
  def inherited_=(b: Boolean) = setInherited(b)
  
  def reportSets = (getReportSets: Buffer[ApacheReportSet])
  def reportSet(body: (ReportSet) => Unit): ReportSet = {
    val s = new ReportSet
    body(s)
    addReportSet(s)
    s
  }

  def configuration: java.lang.Object = getConfiguration
  /**
   * Statically, base type defines this is an Object, but semantically it
   * is supposed to be a W3C DOM Element node. So this mutator method
   * adds a little type safety by requiring use of a W3C DOM Element.
   **/
  def configuration_=(elem: org.w3c.dom.Element) = setConfiguration(elem)
}

// Funny this doesn't inherit from ConfigurationContainer. Seems like an
// oversight in the Apache Maven model codebase.
class ReportSet extends ApacheReportSet {
  def id: String = getId
  def id_=(s: String) = setId(s)
  
  def reports = (getReports: Buffer[String])
  
  def configuration: java.lang.Object = getConfiguration
  /**
   * Statically, base type defines this is an Object, but semantically it
   * is supposed to be a W3C DOM Element node. So this mutator method
   * adds a little type safety by requiring use of a W3C DOM Element.
   **/
  def configuration_=(elem: org.w3c.dom.Element) = setConfiguration(elem)
}

