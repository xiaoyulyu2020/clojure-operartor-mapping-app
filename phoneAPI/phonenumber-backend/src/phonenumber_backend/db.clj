(ns phonenumber-backend.db
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io])
  (:gen-class))

(def operator-mappings
  (with-open [rdr (io/reader "resources/operator-mappings.json")]
    (json/read rdr :key-fn keyword)))

(defn all-countries
  "list all countries"
  [operator-mappings]
  (mapv :country operator-mappings))

(defn unique-countries
  "List all unique Countries sorted alphabetically"
  [operator-mappings]
  (-> operator-mappings
      all-countries
      distinct
      sort))

(defn get-country-by-iso3 [iso3]
  (->> operator-mappings
       (filter #(= (:iso3 %) iso3))))