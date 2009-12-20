(ns org.sonatype.maven.polyglot.clojure.dsl.project
  (:use org.sonatype.maven.polyglot.clojure.dsl.reference)
  (:use org.sonatype.maven.polyglot.clojure.dsl.dependency)
  (:use org.sonatype.maven.polyglot.clojure.dsl.plugin)
  (:use org.sonatype.maven.polyglot.clojure.dsl.defaults))

(def *PROJECT* (atom nil))

(defn add-property!
  [project key value]
  (.put (.getProperties project) key value))

(defn- build-parent
  [reference-source]
  (let [reference (parse-reference reference-source)
        parent (org.apache.maven.model.Parent.)]
    (apply-reference! reference parent)))

(defn- process-dependencies!
  [project options]
  (doseq [dependency (:dependencies options)]
          (add-dependency! project dependency)))

(defn- process-properties!
  [project options]
  (let [properties (:properties options)]
    (doseq [key (keys properties)]
            (add-property! project key (get properties key)))))

(defn- process-plugins!
  [project options]
  (doseq [plugin (:plugins options)]
          (add-plugin! project plugin)))

(defn build-project
  [reference-source & options]
  (let [project (org.apache.maven.model.Model.)
        reference (parse-reference reference-source)
        options (apply hash-map options)]
        (.setModelVersion project "4.0.0")
        (.setGroupId project (:group-id reference))
        (.setArtifactId project (:artifact-id reference))
        (.setVersion project (:version reference))
        (.setBuild project (org.apache.maven.model.Build.))
        (.setName project (:name options))
        (.setDescription project (:description options))
        (.setUrl project (:url options))
        (if (contains? options :parent)
          (.setParent project (build-parent (:parent options))))
        (process-properties! project options)
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

