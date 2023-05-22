(ns phonenumber-backend.functions
  (:require  [clojure.string :as str]
             [phonenumber-backend.db :refer [operator-mappings-db]]))

(defn read-tadig
  [operator-mappings-db]
  (set (mapcat :tadig  operator-mappings-db)))

(defn tadig-exists?
  [tadig]
  (let [tadigs (read-tadig @operator-mappings-db)]
    (boolean (some #(contains? tadigs %) tadig))))

(defn read-tadig-network-mappings
  "Create a map of tadigs to their network names, for every single tadig"
  [operator-mappings-db]
  (reduce
   (fn [tadig-network operator-mapping]
     (let [tadigs (:tadig operator-mapping)
           network-name (:name operator-mapping)]
       (into tadig-network
             (apply hash-map (interleave tadigs (repeat (count tadigs) network-name))))))
   {}
   @operator-mappings-db))

(defn read-all-countries
  [operator-mappings-db]
  (mapv :country @operator-mappings-db))

(defn unique-country-names
  [operator-mappings-db]
  (-> operator-mappings-db
      read-all-countries
      distinct))

(defn read-string-field
  [mapping-field-key field-value]
  (let [field-value (str/upper-case field-value)]
   (->> @operator-mappings-db
       (filter #(= (str/upper-case (mapping-field-key %)) field-value)))))

(defn read-nested-field
  [mapping-field-key field-value]
  (let [field-value (str/upper-case field-value)]
    (->> @operator-mappings-db
       (filter (fn [operator-mapping]
                 (some #(= (str/upper-case %) field-value)
                       (mapping-field-key operator-mapping)))))))

(defn read-mapping
  [mapping-key mapping-value]
  (if (some #(= mapping-key %) [:iso3 :iso2 :name :country])
    (read-string-field mapping-key mapping-value)
    (read-nested-field mapping-key mapping-value)))

(defn create-operator-mapping
  [new-mapping-operater-mapping]
  (swap! operator-mappings-db conj new-mapping-operater-mapping))

(defn delete-mapping-by-tadig
  [tadig]
  (swap! operator-mappings-db
        (fn [operator-mappings]
         (remove (fn [operator-mapping]
                   (some #(= tadig %)
                         (:tadig operator-mapping)))
                 operator-mappings))))

(defn update-mapping
  [existing-operator-mapping incoming-operator-mapping]
  (let [updated-operator-mapping (merge existing-operator-mapping incoming-operator-mapping)]
    (swap! operator-mappings-db conj (first updated-operator-mapping))))