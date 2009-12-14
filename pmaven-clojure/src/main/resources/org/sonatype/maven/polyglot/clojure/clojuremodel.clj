;; Macros to build up a maven Model

(use 'clojure.contrib.str-utils)

(defn parse-reference
  [reference]
  (let [groups (re-find #"(.*):(.*):(.*)" reference)]
    {:group-id (groups 1) :artifact-id (groups 2) :version (groups 3)}))

(defn apply-reference!
  "Apply the reference's fields to the destination object, then return the destination"
  [reference dest]
  (.setGroupId dest (:group-id reference))
  (.setArtifactId dest (:artifact-id reference))
  (.setVersion dest (:version reference))
  dest)


(defn build-parent
  [reference-source]
  (let [reference (parse-reference reference-source)
        parent (org.apache.maven.model.Parent.)]
    (apply-reference! reference parent)))

(defmulti add-dependency! #(class %2))

(defmethod add-dependency! org.apache.maven.model.Dependency
  [model dependency]
  (.addDependency model dependency))

(defmethod add-dependency! String
  [model reference-source]
  (let [dependency (org.apache.maven.model.Dependency.)
        reference (parse-reference reference-source)]
     (apply-reference! reference dependency)
     (add-dependency! model dependency)))

(defmethod add-dependency! clojure.lang.PersistentVector
  [model [reference-source options]]
  (let [dependency (org.apache.maven.model.Dependency.)
        reference (parse-reference reference-source)]
     (apply-reference! reference dependency)
     (.setClassifier dependency (:classifier options))
     (.setScope dependency (:scope options))
     (add-dependency! model dependency)))

(defn get-reference
  [dependency]
  (str (.getGroupId dependency) ":" (.getArtifactId dependency) ":" (.getVersion dependency)))

(defn process-dependencies!
  [model options]
  (doseq [dependency (:dependencies options)]
          (add-dependency! model dependency)))

(defmulti build-plugin-configuration-node #(class %2))

(defmethod build-plugin-configuration-node String
  [name value]
  (let [node (org.codehaus.plexus.util.xml.Xpp3Dom. name)]
    (.setValue node value)
    node))

(defmethod build-plugin-configuration-node clojure.lang.PersistentVector
   [name values]
  (let [node (org.codehaus.plexus.util.xml.Xpp3Dom. (str name "s"))]
    (doseq [value values]
      (let [value-node (org.codehaus.plexus.util.xml.Xpp3Dom. name)]
        (.setValue value-node value)
        (.addChild node value-node)))
    node))

(defn build-plugin-configuration
  [options]
  (let [dom (org.codehaus.plexus.util.xml.Xpp3Dom. "configuration")]
    (doseq [key (keys options)]
       (.addChild dom (build-plugin-configuration-node key (get options key))))
    dom))

(defn build-plugin-execution
   [options]
   (let [execution (org.apache.maven.model.PluginExecution.)]
         (.setId execution (:id options))
         (.setPhase execution (:phase options))
         (.setConfiguration execution (build-plugin-configuration (:configuration options)))
         (doseq [goal (:goals options)]
           (.addGoal execution goal))
         execution))

(defn build-plugin
  [[reference-source options]]
  (let [plugin (org.apache.maven.model.Plugin.)
        reference (parse-reference reference-source)]
    (.setGroupId plugin (:group-id reference))
    (.setArtifactId plugin (:artifact-id reference))
    (.setVersion plugin (:version reference))
    (.setConfiguration plugin (build-plugin-configuration (:configuration options)))
    (doseq [execution (:executions options)]
      (.addExecution plugin (build-plugin-execution execution)))
    plugin))


(defn add-plugin!
  [model plugin]
  (.addPlugin (.getBuild model) (build-plugin plugin)))

(defn process-plugins!
  [model options]

  (doseq [plugin (:plugins options)]
          (add-plugin! model plugin)))

(defn contains-plugin?
  [model reference-source]
  (not (empty? (filter (fn [dependency]
                         (let [ref1 (parse-reference reference-source)
                               ref2 (parse-reference (get-reference dependency))]
                           (and
                             (= (:group-id ref1) (:group-id ref2))
                             (= (:artifact-id ref1) (:artifact-id ref2)))))
                       (.getPlugins (.getBuild model))))))

(defn add-default-plugins!
  [model]
  (if-not (contains-plugin? model "com.theoryinpractise:clojure-maven-plugin:1.1")
    (add-plugin! model ["com.theoryinpractise:clojure-maven-plugin:1.1"
                        {:configuration {"testScript" "src/test/clojure/test.clj"}
                         :executions [{:id "compile"
                                       :phase "compile"
                                       :goals ["compile"]}
                                      {:id "test"
                                        :phase "test"
                                        :goals ["test"]}]}])))


(defn defproject
  [reference-source & rest]
  (let [model (org.apache.maven.model.Model.)
        reference (parse-reference reference-source)
        options (apply hash-map rest)]
        (.setModelVersion model "4.0.0")
        (.setGroupId model (:group-id reference))
        (.setArtifactId model (:artifact-id reference))
        (.setVersion model (:version reference))
        (.setBuild model (org.apache.maven.model.Build.))
        (.setName model (:name options))
        (.setDescription model (:description options))
        (.setUrl model (:url options))
        (if (contains? options :parent)
          (.setParent model (build-parent (:parent options))))
        (process-dependencies! model options)
        (process-plugins! model options)
        (add-default-plugins! model)
        model))
