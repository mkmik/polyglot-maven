; Define the project
(use 'org.sonatype.maven.polyglot.clojure.dsl.project)

(defproject project "a:b:c"
 :dependencies [["org.clojure:clojure:1.1.0-alpha-SNAPSHOT"]
                ["org.clojure:clojure-contrib:1.0-SNAPSHOT"]])

; Use the provided API to add a new dependency
(add-dependency! project ["org.testng:testng:5.10"])
