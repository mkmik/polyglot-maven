;
; Copyright (C) 2010 the original author or authors.
;
; Licensed under the Apache License, Version 2.0 (the "License");
; you may not use this file except in compliance with the License.
; You may obtain a copy of the License at
;
; http://www.apache.org/licenses/LICENSE-2.0
;
; Unless required by applicable law or agreed to in writing, software
; distributed under the License is distributed on an "AS IS" BASIS,
; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
; See the License for the specific language governing permissions and
; limitations under the License.
;

(ns org.sonatype.maven.polyglot.clojure.dsl.plugin
  (:use org.sonatype.maven.polyglot.clojure.dsl.reference))

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

(defn contains-plugin?
  [model reference-source]
  (not (empty? (filter (filter-reference reference-source) (.getPlugins (.getBuild model))))))

(defn find-plugin
  ([model]
    (seq (.getPlugins (.getBuild model))))
  ([model reference-source]
    (filter (filter-reference reference-source) (.getPlugins (.getBuild model)))))

(defn find-plugin-executions
  [plugin]
  (seq (.getExecutions plugin)))

(defmulti add-plugin! #(class %2))

(defmethod add-plugin! org.apache.maven.model.Plugin
  [model plugin]
  (.addPlugin (.getBuild model) plugin))

(defmethod add-plugin! String
  [model reference-source]
  (.addPlugin (.getBuild model) (build-plugin [reference-source])))

(defmethod add-plugin! clojure.lang.PersistentVector
  [model plugin]
  (.addPlugin (.getBuild model) (build-plugin plugin)))
