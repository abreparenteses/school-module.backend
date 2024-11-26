(ns school-module.logics.subjects
  (:require [school-module.adapters.subjects :as adapters.subjects]
            [school-module.schemas.db.subjects :as db.subjects]
            [schema.core :as s])
  (:import [java.util UUID]))

(s/defn uuid-from-string :- s/Uuid
  [seed :- s/Str]
  (-> seed
      .getBytes
      UUID/nameUUIDFromBytes))

(s/defn uuid-from-date-amount :- s/Uuid
  [date :- s/Inst
   amount :- s/Num]
  (-> date
      (adapters.subjects/inst->utc-formated-string "yyyy-MM-dd hh:mm:ss")
      (str amount)
      uuid-from-string))

(s/defn ->subjects-transaction :- db.subjects/SubjectsTransaction
  [date :- s/Int
   name :- s/Str
   description :- s/Str
   courses-id :- s/Uuid]
  #:subjects{:id (uuid-from-date-amount date 0)
             :removed false
             :name name
             :description description
             :courses_id courses-id})

(s/defn ->subjects-update-transaction :- db.subjects/SubjectsTransaction
  [id :- s/Uuid
   name :- s/Str
   description :- s/Str
   courses-id :- s/Uuid]
  #:subjects{:id id
             :removed false
             :name name
             :description description
             :courses_id courses-id})
