(ns phonenumber-backend.handler
  (:require [clojure.string :as str]
            [phonenumber-backend.db :as db]
            [phonenumber-backend.functions :as fn])
  (:gen-class))

(defn exception-response
  [body]
  (if (empty? body)
    {:status 404
     :body {:data "No result. Please check the correct detail was provided."}}
    {:status 200
     :body {:data body}}))

(defn unique-country-names-handler
  [_]
  (let [body (fn/unique-country-names db/operator-mappings-db)]
    (exception-response body)))

(defn read-tadig-network-mappings
  [_]
  (let [body (fn/read-tadig-network-mappings db/operator-mappings-db)]
    (exception-response body)))

(defn get-handler
  [request]
  (let [{:keys [path-params]} request
        {:keys [key id]} path-params
        body (fn/read-mapping (keyword (str key)) id)]
    (exception-response body)))

(defn create-operator-mapping
   [{operator-mapping :body-params}]
  (let [incoming-tadigs (:tadig operator-mapping)]
    (if (fn/tadig-exists? incoming-tadigs)
      {:status 403
       :body (str incoming-tadigs " already exists, try another one please.")}
      (let [body (fn/create-operator-mapping operator-mapping)]
        (exception-response body)))))

(defn delete-handler-by-tadig
  [{{:keys [:id]} :path-params}]
  (let [tadig (str/upper-case (str id))]
    (if (fn/tadig-exists? [tadig])
      (do
        (fn/delete-mapping-by-tadig tadig)
        {:status 200
         :body "Delete successfully."})
      {:status 404
       :body {:error (str "Tadig: " tadig " Does not exists in the Operator Mappings. Delete failed, Please check the correct detail was provided.")}})))

(defn update-mapping-handler
  [{incoming-operator-mapping :body-params {:keys [:id]} :path-params}]
  (let [tadig (str/upper-case (str id))
        existing-mapping (atom (fn/read-mapping :tadig tadig))
        body (when-not (nil? @existing-mapping)
                (fn/delete-mapping-by-tadig tadig)
                (fn/update-mapping  @existing-mapping incoming-operator-mapping))]
    (exception-response body)))