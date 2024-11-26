(ns school-module.adapters.students
  (:require [school-module.schemas.db.students :as db.students]
            [school-module.schemas.wire.in.students :as wire.in.students]
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

(s/defn db->wire-in :- wire.in.students/StudentsEntry
  [{:students/keys [id name document email phone created_at]} :- db.students/StudentsEntry]
  {:id id
   :name name
   :document document
   :email email
   :phone phone
   :created-at created_at})

(s/defn ->students-history :- wire.in.students/StudentsHistory
  [students-entries :- [db.students/StudentsEntry]]
  {:entries (mapv db->wire-in students-entries)})

(s/defn ->students :- wire.in.students/StudentsEntry
  [students-entries :- [db.students/StudentsEntry]]
  (let [students-entry (first students-entries)]
    (db->wire-in students-entry)))
