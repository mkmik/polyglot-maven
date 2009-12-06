;; Macros to build up a maven Model

(use 'clojure.contrib.str-utils)

(defn parse-reference
  [reference]
  (let [groups (re-find #"(.*):(.*):(.*)" reference)]
    {:group-id (groups 1) :artifact-id (groups 2) :version (groups 3)}))

(defn add-dependency!
  [model reference-source]
  (let [dependency (org.apache.maven.model.Dependency.)
        reference (parse-reference reference-source)]
     (.setGroupId dependency (:group-id reference))
     (.setArtifactId dependency (:artifact-id reference))
     (.setVersion dependency (:version reference))
     (.addDependency model dependency)))

(defn process-dependencies!
  [model options]
  (doseq [dependency (:dependencies options)]
          (add-dependency! model dependency)))

(defn plugin-execution
  [id phase & goals]
  (let [execution (org.apache.maven.model.PluginExecution.)]
    (.setId execution id)
    (.setPhase execution phase)
    (doseq [goal goals]
      (.addGoal execution goal))
    execution))

(defn add-plugin!
  [model reference-source & executions]
  (let [plugin (org.apache.maven.model.Plugin.)
        reference (parse-reference reference-source)]
    (.setGroupId plugin (:group-id reference))
    (.setArtifactId plugin (:artifact-id reference))
    (.setVersion plugin (:version reference))
    (doseq [execution executions]
      (.addExecution plugin execution))
    (.addPlugin (.getBuild model) plugin)))

(defn add-default-plugins!
  [model]
  (add-plugin! model "com.theoryinpractise:clojure-maven-plugin:1.1"
                     (plugin-execution "compile" "compile" "compile")
                     (plugin-execution "test" "test" "test")))

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
        (process-dependencies! model options)
        (add-default-plugins! model)
        model))
