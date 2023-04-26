(ns phonenumber-backend.core
  (:require [phonenumber-backend.server :refer [create-donkey-server]])
  (:gen-class))

(defn -main
  []
  (create-donkey-server))