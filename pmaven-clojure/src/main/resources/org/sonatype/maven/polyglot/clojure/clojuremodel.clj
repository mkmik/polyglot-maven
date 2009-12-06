;; Macros to build up a maven Model

(use 'clojure.contrib.str-utils)

(defn process-dependencies
  [model options]
  (doseq [dependency (:dependencies options)]
          (let [dep-seq (re-split #"/" (first dependency))
                dep-group (first dep-seq)
                dep-artifact (last dep-seq)
                dep-version (last dependency)
                dep (org.apache.maven.model.Dependency.)]
             (.setGroupId dep dep-group)
             (.setArtifactId dep dep-artifact)
             (.setVersion dep dep-version)
             (.addDependency model dep))))

(defn plugin-execution
  [id phase & goals]
  (let [execution (org.apache.maven.model.PluginExecution.)]
    (.setId execution id)
    (.setPhase execution phase)
    (doseq [goal goals]
      (.addGoal execution goal))
    execution))

(defn plugin
  [group-id artifact-id version & executions]
  (let [plugin (org.apache.maven.model.Plugin.)]
    (.setGroupId plugin group-id)
    (.setArtifactId plugin artifact-id)
    (.setVersion plugin version)
    (doseq [execution executions]
      (.addExecution plugin execution))
    plugin))

(defn add-default-plugins
  [build]
  (.addPlugin build (plugin "com.theoryinpractise"
                               "clojure-maven-plugin"
                               "1.1"
                               (plugin-execution "compile" "compile" "compile")
                               (plugin-execution "test" "test" "test"))))

(defn process-build
  [model options]
  (let [build (org.apache.maven.model.Build.)]
    (add-default-plugins build)
    (.setBuild model build)))

(defn defproject
  [group artifact version & rest]
  (let [model (org.apache.maven.model.Model.)
        options (apply hash-map rest)]
        (.setModelVersion model "4.0.0")
        (.setGroupId model group)
        (.setArtifactId model artifact)
        (.setVersion model version)
        (process-dependencies model options)
        (process-build model options)
        model))
