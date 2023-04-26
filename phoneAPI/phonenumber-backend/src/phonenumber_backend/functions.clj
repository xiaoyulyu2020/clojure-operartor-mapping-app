(ns phonenumber-backend.functions
  (:require  [clojure.string :as str]
             [phonenumber-backend.db :refer [operator-mappings-db]]))

(defn read-tadig
  [operator-mappings-db]
  (set (mapcat :tadig  operator-mappings-db)))

(defn tadig-exists?
  [tadig]
  (let [tadigs (read-tadig @operator-mappings-db)]
    (some #(contains? tadigs %) tadig)))

;; READ functions
(defn read-all-countries
  "list all countries"
  [operator-mappings-db]
  (mapv :country @operator-mappings-db))

(defn read-unique-countries
  "List all unique Countries sorted alphabetically"
  [operator-mappings-db]
  (-> operator-mappings-db
      read-all-countries
      distinct))

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

(defn read-country-by-iso3
  [iso3]
  (->> @operator-mappings-db
       (filter #(= (:iso3 %) (str/upper-case iso3)))))

(defn read-country-by-tadig
  [tadig]
  (->> @operator-mappings-db
       (filter (fn [operator-mapping]
                 (some #(= (str/upper-case tadig )%) (:tadig operator-mapping))))))

;;CREATE functions
(defn new-country-operator
  [new-country-operater-mapping]
  (swap! operator-mappings-db conj new-country-operater-mapping))

;DELETE functions
(defn delete-country-by-tadig
  [tadig]
  (swap! operator-mappings-db
         (fn [atom]
           (remove (some #(= tadig %) (:tadig atom)) atom))))

; UPDATE functions
(defn update-country
  [incoming-operator-mapping existing-operator-mapping]
  (let [updated-operator-mapping (merge existing-operator-mapping incoming-operator-mapping)]
    (swap! operator-mappings-db conj updated-operator-mapping)))