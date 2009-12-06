; Define the project and store in a variable
(def project (defproject "a:b:c"
    :dependencies ["org.clojure:clojure:1.1.0-alpha-SNAPSHOT"
                   "org.clojure:clojure-contrib:1.0-SNAPSHOT"]))

; Use the provided API to easily add a new dependency programatically
(add-dependency! project "org.testng:testng:5.10")

; Return the project
project
