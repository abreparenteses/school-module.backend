(ns financial-module.adapters.attending
  (:require [financial-module.schemas.db.attending :as db.attending]
            [financial-module.schemas.wire.in.attending :as wire.in.attending]
            [schema.core :as s])
  (:import [java.time LocalDateTime ZoneId]
           [java.time.format DateTimeFormatter]))

(s/defn ^:private date->localdatetime :- LocalDateTime
  [value :- s/Inst
   zone-id :- ZoneId]
  (-> value
      (.toInstant)
      (.atZone zone-id)
      (.toLocalDateTime)))

(s/defn inst->utc-formated-string :- s/Str
  [inst :- s/Inst
   str-format :- s/Str]
  (-> inst
      (date->localdatetime (ZoneId/of "UTC"))
      (.format (DateTimeFormatter/ofPattern str-format))))

(s/defn db->wire-in :- wire.in.attending/AttendingEntry
  [{:attending/keys [id students_id subjects_id created_at]} :- db.attending/AttendingEntry]
  {:id id
   :students-id students_id
   :subjects-id subjects_id
   :created-at created_at})

(s/defn ->attending-history :- wire.in.attending/AttendingHistory
  [attending-entries :- [db.attending/AttendingEntry]]
  {:entries (mapv db->wire-in attending-entries)})

(s/defn ->attending :- wire.in.attending/AttendingEntry
  [attending-entries :- [db.attending/AttendingEntry]]
  (let [attending-entry (first attending-entries)]
    (db->wire-in attending-entry)))
