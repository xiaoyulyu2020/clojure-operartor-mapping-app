(ns phonenumber-backend.db
  (:require [clojure.java.io :as io]
            [clojure.data.json :as json])
  (:gen-class))

(def operator-mappings
  (with-open [read (io/reader "resources/operator-mappings.json")]
    (json/read read :key-fn keyword)))

(def operator-mappings-db (atom operator-mappings))