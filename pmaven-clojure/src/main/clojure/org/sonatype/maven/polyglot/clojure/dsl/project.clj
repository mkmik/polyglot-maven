(ns org.sonatype.maven.polyglot.clojure.dsl.project
  (:use org.sonatype.maven.polyglot.clojure.dsl.reference)
  (:use org.sonatype.maven.polyglot.clojure.dsl.dependency)
  (:use org.sonatype.maven.polyglot.clojure.dsl.plugin)
  (:use org.sonatype.maven.polyglot.clojure.dsl.defaults))

(def *PROJECT* (atom nil))

(defn add-property!
  [project key value]
  (.addProperty project key value))

(defn- build-parent
  [reference-source]
  (let [reference (parse-reference reference-source)
        parent (org.apache.maven.model.Parent.)]
    (apply-reference! reference parent)))


(defn- keyword-for-scope
  "Return the keyword to use for a given scope"
  [scope]
  (if (= "compile" scope)
    :dependencies
    (keyword (str scope "-dependencies"))))

(defn- process-dependencies!
  [project options]
  (doseq [scope ["compile" "provided" "runtime" "test" "system" "import"]]
    (doseq [dependency ((keyword-for-scope scope) options)]
      (add-dependency! project dependency scope))))


(defn- process-dependency-management!
  [project options]
  (if (contains? options :dependency-management)
    (let [dependency-management (org.apache.maven.model.DependencyManagement.)]
      (process-dependencies! dependency-management (:dependency-management options))
      (.setDependencyManagement project dependency-management))))

(defn- process-properties!
  [project options]
  (let [properties (:properties options)]
    (doseq [key (keys properties)]
      (add-property! project key (get properties key)))))

(defn- process-plugins!
  [project options]
  (doseq [plugin (:plugins options)]
    (add-plugin! project plugin)))

(defn process-scm!
  [project options]
  (if (contains? options :scm)
    (let [src (:scm options)
          dest (org.apache.maven.model.Scm.)]
      (.setConnection dest (:connection src))
      (.setDeveloperConnection dest (:developer-connection src))
      (.setTag dest (:tag src))
      (.setUrl dest (:url src))
      (.setScm project dest))))

(defn process-ci!
  [project options]
  (if (contains? options :ci-management)
    (let [src (:ci-management options)
          dest (org.apache.maven.model.CiManagement.)]
      (.setSystem dest (:system src))
      (.setUrl dest (:url src))
      (.setCiManagement project dest))))

(defn process-issue-management!
  [project options]
  (if (contains? options :issue-management)
    (let [src (:issue-management options)
          dest (org.apache.maven.model.IssueManagement.)]
      (.setSystem dest (:system src))
      (.setUrl dest (:url src))
      (.setIssueManagement project dest))))

(defn process-modules!
  [project options]
  (doseq [module (:modules options)]
    (.addModule project module)))

(defn process-build!
  [project options]
  (let [build (org.apache.maven.model.Build.)]
    (.setDefaultGoal build (:default-goal options))
    (.setFinalName build (:final-name options))
    (.setDirectory build (:directory options))
    (.setScriptSourceDirectory build (:script-source-directory options))
    (.setTestSourceDirectory build (:test-source-directory options))
    (.setTestOutputDirectory build (:test-output-directory options))
    (.setSourceDirectory build (:source-directory options))
    (.setBuild project build)))

(defn process-profiles!
  [project options]
  (doseq [profile-options (:profiles options)]
    (let [profile (org.apache.maven.model.Profile.)]
      (.setId profile (:id profile-options))
      (process-modules! profile profile-options)
      (process-build! profile profile-options)
      (process-dependencies! profile profile-options)
      (process-plugins! profile profile-options)
      (.addProfile project profile))))

(defn build-project
  [reference-source & options]
  (let [project (org.apache.maven.model.Model.)
        reference (parse-reference reference-source)
        options (apply hash-map options)]
    (.setModelVersion project "4.0.0")
    (.setGroupId project (:group-id reference))
    (.setArtifactId project (:artifact-id reference))
    (.setVersion project (:version reference))
    (.setName project (:name options))
    (.setDescription project (:description options))
    (.setUrl project (:url options))
    (if (contains? options :parent)
      (.setParent project (build-parent (:parent options))))
    (process-properties! project options)
    (process-modules! project options)
    (process-scm! project options)
    (process-ci! project options)
    (process-profiles! project options)
    (process-build! project options)
    (process-dependency-management! project options)
    (process-dependencies! project options)
    (process-plugins! project options)
    project))

(defn use-project [project]
  (reset! *PROJECT* project))

(defmacro defproject [name reference-source & options]
  `(do
    (def ~name (build-project ~reference-source ~@options))
    (add-default-plugins! ~name)
    (use-project ~name)))

