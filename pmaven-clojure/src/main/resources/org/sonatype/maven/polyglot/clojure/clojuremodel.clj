;; Macros to build up a maven Model

(use 'clojure.contrib.str-utils)

(defn defproject
  [group artifact version & rest]
  (let [model (org.apache.maven.model.Model.)
        options (apply hash-map rest)]
        (.setModelVersion model "4.0.0")
        (.setGroupId model group)
        (.setArtifactId model artifact)
        (.setVersion model version)
        (doseq [dependency (:dependencies options)]
          (let [dep-seq (re-split #"/" (first dependency))
                dep-group (first dep-seq)
                dep-artifact (last dep-seq)
                dep-version (last dependency)
                dep (org.apache.maven.model.Dependency.)]
             (.setGroupId dep dep-group)
             (.setArtifactId dep dep-artifact)
             (.setVersion dep dep-version)
             (.addDependency model dep)))
        model))
