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
     ["" h/home-handler]
     ["get-all-countries"
      {:get h/get-handler-all-countries}]
     ["create-country"
      {:post h/new-operator-mappings}]
     ["get-unique-countries"
      {:get h/get-handler-unique-countries}]
     ["get-tadig-network-mappings"
      {:get h/get-handler-tadig-network-mappings}]
     ["get-country-iso3/:id"
      {:get h/get-handler-country-by-iso3}]
     ["get-country-tadig/:id"
      {:get h/get-handler-country-by-tadig}]
     ["delete-country-tadig/:id"
      {:delete h/delete-handler-by-tadig}]
     ["update-country/:id"
      {:put h/update-country-handler}]]
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