(ns school-module.ports.http.in.attending
  (:require [school-module.adapters.attending :as adapters.attending]
            [school-module.controllers.attending :as controllers.attending]))

(defn get-history
  [{components :components}]
  (let [{:keys [entries]} (controllers.attending/get-attending components)]
    {:status 200
     :body (adapters.attending/->attending-history entries)}))

(defn get-entry-by-id
  [{{{:keys [id]} :path} :parameters
    components :components}]
  (let [{:keys [entries]} (controllers.attending/get-attending-by-id id components)]
    (if-not (empty? entries)
      {:status 200
       :body (adapters.attending/->attending entries)}
      {:status 400
       :body "Attending entry not found."})))

(defn add-entry!
  [{{{:keys [students-id subjects-id]} :body} :parameters
    components :components}]
  {:status 201
   :body (-> (controllers.attending/add-entry!
              students-id subjects-id components)
             adapters.attending/db->wire-in)})

(defn update-entry!
  [{{{:keys [id]} :path
     {:keys [students-id subjects-id]} :body} :parameters
    components :components}]
  (if (controllers.attending/update-entry! id students-id subjects-id components)
    {:status 202
     :body "Attending entry updated with success."}
    {:status 400
     :body "Attending entry not found."}))

(defn remove-entry!
  [{{{:keys [id]} :path} :parameters
    components :components}]
  (if (controllers.attending/remove-entry! id components)
    {:status 202
     :body "Attending entry removed with success."}
    {:status 400
     :body "Attending entry not found."}))
