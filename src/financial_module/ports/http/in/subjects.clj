(ns financial-module.ports.http.in.subjects
  (:require [financial-module.adapters.subjects :as adapters.subjects]
            [financial-module.controllers.subjects :as controllers.subjects]))

(defn get-history
  [{components :components}]
  (let [{:keys [entries]} (controllers.subjects/get-subjects components)]
    {:status 200
     :body (adapters.subjects/->subjects-history entries)}))

(defn get-entry-by-id
  [{{{:keys [id]} :path} :parameters
    components :components}]
  (let [{:keys [entries]} (controllers.subjects/get-subjects-by-id id components)]
    {:status 200
     :body (adapters.subjects/->subjects entries)}))

(defn add-entry!
  [{{{:keys [name description courses-id]} :body} :parameters
    components :components}]
  {:status 201
   :body (-> (controllers.subjects/add-entry!
              name description courses-id components)
             adapters.subjects/db->wire-in)})

(defn update-entry!
  [{{{:keys [id]} :path
     {:keys [name description courses-id]} :body} :parameters
    components :components}]
  (if (controllers.subjects/update-entry! id name description courses-id components)
    {:status 202
     :body "Subjects entry updated with success."}
    {:status 400
     :body "Subjects entry not found."}))

(defn remove-entry!
  [{{{:keys [id]} :path} :parameters
    components :components}]
  (if (controllers.subjects/remove-entry! id components)
    {:status 202
     :body "Subjects entry removed with success."}
    {:status 400
     :body "Subjects entry not found."}))
