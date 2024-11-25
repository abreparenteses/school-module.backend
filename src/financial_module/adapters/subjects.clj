(ns financial-module.adapters.subjects
  (:require [financial-module.schemas.db.subjects :as db.subjects]
            [financial-module.schemas.wire.in.subjects :as wire.in.subjects]
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

(s/defn db->wire-in :- wire.in.subjects/SubjectsEntry
  [{:attending/keys [id name description courses_id created_at]} :- db.subjects/SubjectsEntry]
  {:id id
   :name name
   :description description
   :courses-id courses_id
   :created-at created_at})

(s/defn ->courses-history :- wire.in.subjects/SubjectsHistory
  [attending-entries :- [db.subjects/SubjectsEntry]]
  (let [total (reduce #(+ (:attending/amount %2) %1) 0M attending-entries)]
    {:entries (mapv db->wire-in attending-entries)
     :total (bigdec total)}))

(s/defn ->courses :- wire.in.subjects/SubjectsEntry
  [attending-entries :- [db.subjects/SubjectsEntry]]
  (let [attending-entry (first attending-entries)]
    (db->wire-in attending-entry)))
