(ns phonenumber-backend.server
  (:require [com.appsflyer.donkey.core :as donkey]
            [com.appsflyer.donkey.server :refer [start]]
            [com.appsflyer.donkey.result :refer [on-success]]
            [phonenumber-backend.routes :as routes]))

(defn create-donkey-server
  []
  (->
   (donkey/create-donkey)
   (donkey/create-server
    {:port 8080
     :routes [{:handler routes/routes
               :handler-mode :blocking}]})
   (start)
   (on-success
    (fn [_]
      (println "Server started listening on port 8080")))))