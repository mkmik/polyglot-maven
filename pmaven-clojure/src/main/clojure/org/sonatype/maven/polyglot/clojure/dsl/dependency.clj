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

(ns org.sonatype.maven.polyglot.clojure.dsl.dependency
  (:use org.sonatype.maven.polyglot.clojure.dsl.reference))

(defmulti make-dependency class)

(defmethod make-dependency org.apache.maven.model.Dependency
  [dependency]
  dependency)

(defmethod make-dependency String
  [reference-source]
  (let [dependency (org.apache.maven.model.Dependency.)
        reference (parse-reference reference-source)]
    (apply-reference! reference dependency)
    dependency))

(defmethod make-dependency clojure.lang.PersistentVector
  [[reference-source options]]
  (let [dependency (make-dependency reference-source)]
    (.setClassifier dependency (:classifier options))
    (.setType dependency (if (contains? options :type)
      (:type options)
      "jar"))
    dependency))

(defn add-dependency!
  ([model reference-source]
    (.addDependency model (make-dependency reference-source)))
  ([model reference-source scope]
    (let [dependency (make-dependency reference-source)]
      (.setScope dependency scope)
      (.addDependency model dependency))))

(defn contains-dependency?
  [model reference-source]
  (not (empty? (filter (filter-reference reference-source) (.getDependencies model)))))

(defn find-dependency
  ([model]
    (seq (.getDependencies model)))
  ([model reference-source]
    (filter (filter-reference reference-source) (.getDependencies model))))
