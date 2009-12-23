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

(defn contains-version?
  [source]
  (and (not= (.getVersion source) nil)))

(defn get-reference
  [source]
  (str (.getGroupId source) ":" (.getArtifactId source)
    (if (contains-version? source) (str ":" (.getVersion source)) "")))

(defn set-version!
  [dest version]
  (apply-reference! (assoc (parse-reference (get-reference dest)) :version version) dest))

(defn get-version
  [dest]
  (:version (parse-reference (get-reference dest))))

(defn remove-version!
  [dest]
  (apply-reference! (dissoc (parse-reference (get-reference dest)) :version) dest))

(defn filter-reference
  [reference-source]
  (fn [reference]
    (let [ref1 (parse-reference reference-source)
          ref2 (parse-reference (get-reference reference))]
      (and
        (= (:group-id ref1) (:group-id ref2))
        (= (:artifact-id ref1) (:artifact-id ref2))
        (= (if (contains? :version ref1) (= (:version ref1) (:version ref2)) true))))))
