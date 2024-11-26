(ns school-module.ports.http.in.students
  (:require [school-module.adapters.students :as adapters.students]
            [school-module.controllers.students :as controllers.students]))

(defn get-history
  [{components :components}]
  (let [{:keys [entries]} (controllers.students/get-students components)]
    {:status 200
     :body (adapters.students/->students-history entries)}))

(defn get-entry-by-id
  [{{{:keys [id]} :path} :parameters
    components :components}]
  (let [{:keys [entries]} (controllers.students/get-students-by-id id components)]
    {:status 200
     :body (adapters.students/->students entries)}))

(defn add-entry!
  [{{{:keys [name document email phone]} :body} :parameters
    components :components}]
  {:status 201
   :body (-> (controllers.students/add-entry!
              name document email phone components)
             adapters.students/db->wire-in)})

(defn update-entry!
  [{{{:keys [id]} :path
     {:keys [name document email phone]} :body} :parameters
    components :components}]
  (if (controllers.students/update-entry! id name document email phone components)
    {:status 202
     :body "Students entry updated with success."}
    {:status 400
     :body "Students entry not found."}))

(defn remove-entry!
  [{{{:keys [id]} :path} :parameters
    components :components}]
  (if (controllers.students/remove-entry! id components)
    {:status 202
     :body "Students entry removed with success."}
    {:status 400
     :body "Students entry not found."}))
