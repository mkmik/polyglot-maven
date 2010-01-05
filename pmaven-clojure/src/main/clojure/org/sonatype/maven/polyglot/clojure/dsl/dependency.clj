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
