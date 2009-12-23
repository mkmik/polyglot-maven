(ns org.sonatype.maven.polyglot.clojure.dsl.dependency
  (:use org.sonatype.maven.polyglot.clojure.dsl.reference))

(defmulti add-dependency! #(class %2))

(defmethod add-dependency! org.apache.maven.model.Dependency
  [model dependency]
  (.addDependency model dependency))

(defmethod add-dependency! String
  [model reference-source]
  (let [dependency (org.apache.maven.model.Dependency.)
        reference (parse-reference reference-source)]
    (apply-reference! reference dependency)
    (add-dependency! model dependency)))

(defmethod add-dependency! clojure.lang.PersistentVector
  [model [reference-source options]]
  (let [dependency (org.apache.maven.model.Dependency.)
        reference (parse-reference reference-source)]
    (apply-reference! reference dependency)
    (.setClassifier dependency (:classifier options))
    (.setScope dependency (:scope options))
    (add-dependency! model dependency)))

(defn contains-dependency?
  [model reference-source]
  (not (empty? (filter (filter-reference reference-source) (.getDependencies model)))))

(defn find-dependency
  ([model]
    (seq (.getDependencies model)))
  ([model reference-source]
    (filter (filter-reference reference-source) (.getDependencies model))))
