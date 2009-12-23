(ns org.sonatype.maven.polyglot.clojure.dsl.defaults
  (:use org.sonatype.maven.polyglot.clojure.dsl.plugin))

(defn add-default-plugins!
  [model]
  (if-not (contains-plugin? model "com.theoryinpractise:clojure-maven-plugin")
    (add-plugin! model ["com.theoryinpractise:clojure-maven-plugin:1.3" {:configuration {"testScript" "src/test/clojure/test.clj"}
                                                                         :executions [{:id "compile"
                                                                                       :phase "compile"
                                                                                       :goals ["compile"]}
                                                                                      {:id "test-compile"
                                                                                       :phase "test-compile"
                                                                                       :goals ["testCompile"]}
                                                                                      {:id "test"
                                                                                       :phase "test"
                                                                                       :goals ["test"]}]}])))
