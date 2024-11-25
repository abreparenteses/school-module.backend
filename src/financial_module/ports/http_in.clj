(ns financial-module.ports.http-in
  (:require [financial-module.adapters :as adapters]
            [financial-module.controllers :as controllers]
            [financial-module.ports.jwt :as jwt]))

(defn login
  [{{{:keys [username]} :body} :parameters
    components :components}]
  (let [token (jwt/encrypt {:username username} (:config components))]
    {:status 200
     :body {:token token}}))

(defn get-history
  [{components :components}]
  (let [{:keys [entries]} (controllers/get-accounts-payable components)]
    {:status 200
     :body (adapters/->accounts-payable-history entries)}))

(defn get-entry-by-id
  [{{{:keys [id]} :path} :parameters
    components :components}]
  (let [{:keys [entries]} (controllers/get-accounts-payable-by-id id components)]
    {:status 200
     :body (adapters/->accounts-payable entries)}))

(defn add-entry!
  [{{{:keys [name description amount]} :body} :parameters
    components :components}]
  (if (pos? amount)
    {:status 201
     :body (-> (controllers/add-entry!
                name description amount components)
               adapters/db->wire-in)}
    {:status 400
     :body "amount can't be negative."}))

(defn update-entry!
  [{{{:keys [id]} :path
     {:keys [name description amount]} :body} :parameters
    components :components}]
  (if (pos? amount)
    (if (controllers/update-entry! id name description amount components)
      {:status 202
       :body "Entry updated with success."}
      {:status 400
       :body "Entry not found."})
    {:status 400
     :body "amount can't be negative."}))

(defn remove-entry!
  [{{{:keys [id]} :path} :parameters
    components :components}]
  (if (controllers/remove-entry! id components)
    {:status 202
     :body "Entry removed with success."}
    {:status 400
     :body "Entry not found."}))
