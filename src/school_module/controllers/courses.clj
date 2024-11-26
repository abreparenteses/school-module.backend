(ns school-module.controllers.courses
  (:require [school-module.db.courses :as db.courses]
            [school-module.logics.courses :as logics.courses]
            [school-module.schemas.db.courses :as schemas.db.courses]
            [school-module.schemas.types :as schemas.types]
            [schema.core :as s]))

(defn- instant-now [] (java.util.Date/from (java.time.Instant/now)))

(s/defschema CoursesHistory
  {:entries [schemas.db.courses/CoursesEntry]})

(s/defn get-courses :- CoursesHistory
  [{:keys [database]} :- schemas.types/Components]
  {:entries (db.courses/get-courses-all-transactions database)})

(s/defn get-courses-by-id :- CoursesHistory
  [id :- s/Uuid
   {:keys [database]} :- schemas.types/Components]
  {:entries (db.courses/get-courses-by-id id database)})

(s/defn add-entry! :- schemas.db.courses/CoursesEntry
  [name :- s/Str
   description :- s/Str
   {:keys [database]} :- schemas.types/Components]
  (let [now (instant-now)
        entry (logics.courses/->courses-transaction now name description)]
    (db.courses/insert-courses-transaction entry database)))

(s/defn update-entry! :- schemas.db.courses/CoursesEntry
  [id :- s/Uuid
   name :- s/Str
   description :- s/Str
   {:keys [database]} :- schemas.types/Components]
  (let [entry (logics.courses/->courses-update-transaction id name description)
        result (db.courses/update-courses-transaction entry database)]
    (= 1 (:next.jdbc/update-count result))))

(s/defn remove-entry!
  [id :- s/Uuid
   {:keys [database]} :- schemas.types/Components]
  (db.courses/remove-courses id database))
