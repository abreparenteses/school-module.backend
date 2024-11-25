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
  [{:attending/keys [id name description created_at]} :- db.courses/CoursesEntry]
  {:id id
   :name name
   :description description
   :created-at created_at})

(s/defn ->courses-history :- wire.in.courses/CoursesHistory
  [attending-entries :- [db.courses/CoursesEntry]]
  (let [total (reduce #(+ (:attending/amount %2) %1) 0M attending-entries)]
    {:entries (mapv db->wire-in attending-entries)
     :total (bigdec total)}))

(s/defn ->courses :- wire.in.courses/CoursesEntry
  [attending-entries :- [db.courses/CoursesEntry]]
  (let [attending-entry (first attending-entries)]
    (db->wire-in attending-entry)))
