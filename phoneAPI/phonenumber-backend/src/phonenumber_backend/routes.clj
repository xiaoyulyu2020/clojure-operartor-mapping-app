(ns phonenumber-backend.routes
  (:require [reitit.ring :as ring]
            [muuntaja.core :as m]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [phonenumber-backend.funtional :as f])
  (:gen-class))

(def routes
  (ring/ring-handler
    (ring/router
      ["/"
       [""
        f/home-handler]

       ["get/all-countries"
        {:get f/get-handler-all-countries}]
       
       ["get/unique-countries"
        {:get f/get-handler-unique-countries}]
       
       ["country/:id"
        {:get f/get-handler-country-by-iso3}]]
     
      {:data {:muuntaja m/instance
              :middleware [muuntaja/format-middleware]}})))