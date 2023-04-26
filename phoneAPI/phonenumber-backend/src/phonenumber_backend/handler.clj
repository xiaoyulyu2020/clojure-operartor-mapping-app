(ns phonenumber-backend.handler
  (:require [clojure.string :as str]
            [phonenumber-backend.db :as db]
            [phonenumber-backend.functions :as fn])
  (:gen-class))

;GET handler
(defn home-handler
  "home dir"
  [_]
  (let [body (db/operator-mappings)]
      {:status 200
       :body body}))

(defn get-handler-all-countries
  "GET: all-countries-collection"
  [_]
  (let [body (fn/read-all-countries db/operator-mappings-db)]
    {:status 200
     :body body}))

(defn get-handler-unique-countries
  "GET: unique-countries-collection"
  [_]
  (let [body (fn/read-unique-countries db/operator-mappings-db)]
    {:status 200
     :body body}))

(defn get-handler-tadig-network-mappings
  "GET: Create a map of tadigs to their network names, for every single tadig"
  [_]
  (let [body (fn/read-tadig-network-mappings db/operator-mappings-db)]
    {:status 200
     :body body}))

(defn get-handler-country-by-iso3
  "GET: a country by parameter --- iso3"
  [{{:keys [:id]} :path-params}]
  (let [body (fn/read-country-by-iso3 (str id))]
    {:status 200
     :body body}))

(defn get-handler-country-by-tadig
  "GET: a country by parameter --- tadig"
  [{{:keys [:id]} :path-params}]
  (let [body (fn/read-country-by-tadig (str id))]
    {:status 200
     :body body}))

;PUT handler
(defn new-operator-mappings
  "Create a new operator mappings"
  [{operator-mapping :body-params}]
  (let [incoming-tadigs (:tadig operator-mapping)]
    (if (fn/tadig-exists? incoming-tadigs)
      {:status 404
       :body "Tadigs are existing"}
      (let [body (fn/new-country-operator operator-mapping)]
        {:status 200
         :body body}))))

;DELETE handler
(defn delete-handler-by-tadig
  "Delete a mapping by tadig"
  [{{:keys [:id]} :path-params}]
  (let [tadig (str/upper-case (str id))
        body (fn/delete-country-by-tadig tadig)]
    {:status 200
     :body body}))

;UPDATE handler
(defn update-country-handler
  [{incoming-operator-mapping :body-params {:keys [:id]} :path-params}]
  (let [tadig (str id)
        existing-operator-mapping (fn/read-country-by-tadig tadig)
        body (if-not (nil? existing-operator-mapping)
                  (do
                    (fn/delete-country-by-tadig tadig)
                    (fn/update-country incoming-operator-mapping existing-operator-mapping)))]
    {:status 200
     :body body}))