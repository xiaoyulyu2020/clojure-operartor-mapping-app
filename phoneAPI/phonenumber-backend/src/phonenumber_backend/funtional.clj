(ns phonenumber-backend.funtional
  (:require [phonenumber-backend.db :as db])
  (:gen-class))


(defn get-handler-all-countries
  "GET all-countries-collection"
  [_]
  (let [body (db/all-countries db/operator-mappings)]
    (if (empty? body)
      {:status 404}
      {:status 200
       :body body})))

(defn get-handler-unique-countries
  "GET unique-countries-collection"
  [_]
  (let [body (db/unique-countries db/operator-mappings)]
    (if (empty? body)
      {:status 404}
      {:status 200
       :body body})))

(defn get-handler-country-by-iso3
  "GET a country by parameter --- iso3"
  [{{:keys [:id]} :path-params}]
  (let [body (db/get-country-by-iso3 (str id))]
    (if (empty? body)
      {:status 404}
      {:status 200
       :body body})))

(defn home-handler
  "home dir"
  [_]
  (let [body (db/operator-mappings)]
    (if (empty? body)
      {:status 404}
      {:status 200
       :body (db/operator-mappings)})))
