(ns phonenumber-backend.server
  (:require [com.appsflyer.donkey.core :as donkey]
            [phonenumber-backend.routes :as routes]
            [com.appsflyer.donkey.server :refer [start]]
            [com.appsflyer.donkey.result :refer [on-success]]))

(defn create-donkey-server
  []
  (->
   (donkey/create-donkey)
   (donkey/create-server
    {:port 8080
     :routes [{:handler routes/app
               :handler-mode :blocking}]})
   (start)
   (on-success
    (fn [_]
      (println "Server started listening on port 8080")))))