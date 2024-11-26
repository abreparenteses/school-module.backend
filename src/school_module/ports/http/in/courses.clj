(ns school-module.ports.http.in.courses
  (:require [school-module.adapters.courses :as adapters.courses]
            [school-module.controllers.courses :as controllers.courses]))

(defn get-history
  [{components :components}]
  (let [{:keys [entries]} (controllers.courses/get-courses components)]
    {:status 200
     :body (adapters.courses/->courses-history entries)}))

(defn get-entry-by-id
  [{{{:keys [id]} :path} :parameters
    components :components}]
  (let [{:keys [entries]} (controllers.courses/get-courses-by-id id components)]
    (if-not (empty? entries)
      {:status 200
       :body (adapters.courses/->courses entries)}
      {:status 400
       :body "Courses entry not found."})))

(defn add-entry!
  [{{{:keys [name description]} :body} :parameters
    components :components}]
  {:status 201
   :body (-> (controllers.courses/add-entry!
              name description components)
             adapters.courses/db->wire-in)})

(defn update-entry!
  [{{{:keys [id]} :path
     {:keys [name description]} :body} :parameters
    components :components}]
  (if (controllers.courses/update-entry! id name description components)
    {:status 202
     :body "Courses entry updated with success."}
    {:status 400
     :body "Courses entry not found."}))

(defn remove-entry!
  [{{{:keys [id]} :path} :parameters
    components :components}]
  (if (controllers.courses/remove-entry! id components)
    {:status 202
     :body "Courses entry removed with success."}
    {:status 400
     :body "Courses entry not found."}))
