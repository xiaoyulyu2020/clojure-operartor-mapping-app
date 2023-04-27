(ns phonenumber-backend.core
  (:require [phonenumber-backend.server :refer [create-donkey-server]]
            [clojure.core :as c])
  (:gen-class))

(defn -main
  []
  (create-donkey-server))