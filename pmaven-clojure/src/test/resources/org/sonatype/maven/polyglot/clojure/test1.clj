(defproject "a:b:c"
    :name "Polyglot Test"
    :description "Maven 3 Clojure based Polyglot test"
    :dependencies ["org.clojure:clojure:1.1.0-alpha-SNAPSHOT"
                   "org.clojure:clojure-contrib:1.0-SNAPSHOT"]
    :plugins [["com.theoryinpractise:clojure-maven-plugin:1.2-SNAPSHOT"
               (plugin-execution "compile" "compile" "compile")
               (plugin-execution "test" "test" "test")]])