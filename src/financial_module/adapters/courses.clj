(ns financial-module.adapters.courses
  (:require [financial-module.schemas.db.courses :as db.courses]
            [financial-module.schemas.wire.in.courses :as wire.in.courses]
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

(s/defn db->wire-in :- wire.in.courses/CoursesEntry
  [{:courses/keys [id name description created_at]} :- db.courses/CoursesEntry]
  {:id id
   :name name
   :description description
   :created-at created_at})

(s/defn ->courses-history :- wire.in.courses/CoursesHistory
  [courses-entries :- [db.courses/CoursesEntry]]
  (println "adapters: " courses-entries)
  {:entries (mapv db->wire-in courses-entries)})

(s/defn ->courses :- wire.in.courses/CoursesEntry
  [courses-entries :- [db.courses/CoursesEntry]]
  (let [courses-entry (first courses-entries)]
    (db->wire-in courses-entry)))
