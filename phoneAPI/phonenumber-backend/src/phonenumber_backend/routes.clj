(ns phonenumber-backend.routes
  (:require [muuntaja.core :as m]
            [reitit.ring :as ring]
            [reitit.ring.coercion :as rrc]
            [reitit.coercion.schema :as rcs]
            [phonenumber-backend.handler :as h]
            [ring.middleware.cors :refer [wrap-cors]]
            [reitit.ring.middleware.muuntaja :as muuntaja])
  (:gen-class))

(def routes
  (ring/ring-handler
   (ring/router
    ["/"
     ["all-country-names"
      {:get h/unique-country-names-handler}]
     ["all-tadigs-networks"
      {:get h/read-tadig-network-mappings}]
     ["create-mapping"
      {:post h/create-operator-mapping}]
     ["delete/:id"
      {:delete h/delete-handler-by-tadig}]
     ["update/:id"
      {:put h/update-mapping-handler}]
     ["search/"
      [":key/:id"
       {:get h/get-handler}]]]
    {:data {:muuntaja m/instance
            :coercion rcs/coercion
            :middleware [muuntaja/format-negotiate-middleware
                         muuntaja/format-response-middleware
                         muuntaja/format-request-middleware
                         rrc/coerce-request-middleware
                         rrc/coerce-response-middleware]}})))

(def app
  (-> routes
      (wrap-cors :access-control-allow-origin #".*"
                 :access-control-allow-methods [:get :post :put :delete])))