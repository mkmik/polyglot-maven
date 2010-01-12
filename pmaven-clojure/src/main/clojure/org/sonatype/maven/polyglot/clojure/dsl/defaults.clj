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

(ns org.sonatype.maven.polyglot.clojure.dsl.defaults
  (:use org.sonatype.maven.polyglot.clojure.dsl.plugin))

(defn add-default-plugins?
  [options]
  (if (contains? options :add-default-plugins)
    (= true (options :add-default-plugins))
    true))

(defn add-default-plugins!
  [model]
  (if (nil? (.getModelVersion model))
    (.setModelVersion model "4.0.0"))
  (if-not (contains-plugin? model "com.theoryinpractise:clojure-maven-plugin")
    (add-plugin! model ["com.theoryinpractise:clojure-maven-plugin:1.3" {:configuration {:testScript "src/test/clojure/test.clj"}
                                                                         :executions [{:id "compile"
                                                                                       :phase "compile"
                                                                                       :goals ["compile"]}
                                                                                      {:id "test-compile"
                                                                                       :phase "test-compile"
                                                                                       :goals ["testCompile"]}
                                                                                      {:id "test"
                                                                                       :phase "test"
                                                                                       :goals ["test"]}]}])))
