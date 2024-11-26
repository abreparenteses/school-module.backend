(ns school-module.controllers.subjects
  (:require [school-module.db.subjects :as db.subjects]
            [school-module.logics.subjects :as logics.subjects]
            [school-module.schemas.db.subjects :as schemas.db.subjects]
            [school-module.schemas.types :as schemas.types]
            [schema.core :as s]))

(defn- instant-now [] (java.util.Date/from (java.time.Instant/now)))

(s/defschema SubjectsHistory
  {:entries [schemas.db.subjects/SubjectsEntry]})

(s/defn get-subjects :- SubjectsHistory
  [{:keys [database]} :- schemas.types/Components]
  {:entries (db.subjects/get-subjects-all-transactions database)})

(s/defn get-subjects-by-id :- SubjectsHistory
  [id :- s/Uuid
   {:keys [database]} :- schemas.types/Components]
  {:entries (db.subjects/get-subjects-by-id id database)})

(s/defn add-entry! :- schemas.db.subjects/SubjectsEntry
  [name :- s/Str
   description :- s/Str
   courses-id :- s/Uuid
   {:keys [database]} :- schemas.types/Components]
  (let [now (instant-now)
        entry (logics.subjects/->subjects-transaction now name description courses-id)]
    (db.subjects/insert-subjects-transaction entry database)))

(s/defn update-entry! :- schemas.db.subjects/SubjectsEntry
  [id :- s/Uuid
   name :- s/Str
   description :- s/Str
   courses-id :- s/Uuid
   {:keys [database]} :- schemas.types/Components]
  (let [entry (logics.subjects/->subjects-update-transaction id name description courses-id)
        result (db.subjects/update-subjects-transaction entry database)]
    (= 1 (:next.jdbc/update-count result))))

(s/defn remove-entry!
  [id :- s/Uuid
   {:keys [database]} :- schemas.types/Components]
  (db.subjects/remove-subjects id database))
