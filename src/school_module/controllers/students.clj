(ns school-module.controllers.students
  (:require [school-module.db.students :as db.students]
            [school-module.logics.students :as logics.students]
            [school-module.schemas.db.students :as schemas.db.students]
            [school-module.schemas.types :as schemas.types]
            [schema.core :as s]))

(defn- instant-now [] (java.util.Date/from (java.time.Instant/now)))

(s/defschema StudentsHistory
  {:entries [schemas.db.students/StudentsEntry]})

(s/defn get-students :- StudentsHistory
  [{:keys [database]} :- schemas.types/Components]
  {:entries (db.students/get-students-all-transactions database)})

(s/defn get-students-by-id :- StudentsHistory
  [id :- s/Uuid
   {:keys [database]} :- schemas.types/Components]
  {:entries (db.students/get-students-by-id id database)})

(s/defn add-entry! :- schemas.db.students/StudentsEntry
  [name :- s/Str
   document :- s/Str
   email :- s/Str
   phone :- s/Str
   {:keys [database]} :- schemas.types/Components]
  (let [now (instant-now)
        entry (logics.students/->students-transaction now name document email phone)]
    (db.students/insert-students-transaction entry database)))

(s/defn update-entry! :- schemas.db.students/StudentsEntry
  [id :- s/Uuid
   name :- s/Str
   document :- s/Str
   email :- s/Str
   phone :- s/Str
   {:keys [database]} :- schemas.types/Components]
  (let [entry (logics.students/->students-update-transaction id name document email phone)
        result (db.students/update-students-transaction entry database)]
    (= 1 (:next.jdbc/update-count result))))

(s/defn remove-entry!
  [id :- s/Uuid
   {:keys [database]} :- schemas.types/Components]
  (db.students/remove-students id database))
