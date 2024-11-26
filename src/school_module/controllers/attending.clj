(ns school-module.controllers.attending
  (:require [school-module.db.attending :as db.attending]
            [school-module.logics.attending :as logics.attending]
            [school-module.schemas.db.attending :as schemas.db.attending]
            [school-module.schemas.types :as schemas.types]
            [schema.core :as s]))

(defn- instant-now [] (java.util.Date/from (java.time.Instant/now)))

(s/defschema AttendingHistory
  {:entries [schemas.db.attending/AttendingEntry]})

(s/defn get-attending :- AttendingHistory
  [{:keys [database]} :- schemas.types/Components]
  {:entries (db.attending/get-attending-all-transactions database)})

(s/defn get-attending-by-id :- AttendingHistory
  [id :- s/Uuid
   {:keys [database]} :- schemas.types/Components]
  {:entries (db.attending/get-attending-by-id id database)})

(s/defn add-entry! :- schemas.db.attending/AttendingEntry
  [student-id :- s/Uuid
   subject-id :- s/Uuid
   {:keys [database]} :- schemas.types/Components]
  (let [now (instant-now)
        entry (logics.attending/->attending-transaction now student-id subject-id)]
    (db.attending/insert-attending-transaction entry database)))

(s/defn update-entry! :- schemas.db.attending/AttendingEntry
  [id :- s/Uuid
   student-id :- s/Uuid
   subject-id :- s/Uuid
   {:keys [database]} :- schemas.types/Components]
  (let [entry (logics.attending/->attending-update-transaction id student-id subject-id)
        result (db.attending/update-attending-transaction entry database)]
    (= 1 (:next.jdbc/update-count result))))

(s/defn remove-entry!
  [id :- s/Uuid
   {:keys [database]} :- schemas.types/Components]
  (db.attending/remove-attending id database))
