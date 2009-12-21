(ns org.sonatype.maven.polyglot.clojure.dsl.reference)

(defmulti parse-reference #(+ 1 (count (re-seq #":" %))))

(defmethod parse-reference 2
  [reference]
  (let [groups (re-find #"(.*):(.*)" reference)]
    {:group-id (groups 1) :artifact-id (groups 2)}))

(defmethod parse-reference 3
  [reference]
  (let [groups (re-find #"(.*):(.*):(.*)" reference)]
    {:group-id (groups 1) :artifact-id (groups 2) :version (groups 3)}))

(defn apply-reference!
  "Apply the reference's fields to the destination object, then return the destination"
  [reference dest]
  (.setGroupId dest (:group-id reference))
  (.setArtifactId dest (:artifact-id reference))
  (.setVersion dest (:version reference))
  dest)


(defn get-reference
  [dependency]
  (str (.getGroupId dependency) ":" (.getArtifactId dependency) ":" (.getVersion dependency)))
