(ns financial-module.logics.attending
  (:require [financial-module.adapters.attending :as adapters.attending]
            [financial-module.schemas.db.attending :as db.attending]
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
      (adapters.attending/inst->utc-formated-string "yyyy-MM-dd hh:mm:ss")
      (str amount)
      uuid-from-string))

(s/defn ->attending-transaction :- db.attending/AttendingTransaction
  [date :- s/Inst
   students-id :- s/Uuid
   subjects-id :- s/Uuid]
  #:attending{:id (uuid-from-date-amount date 0)
              :removed false
              :students_id students-id
              :subjects_id subjects-id})

(s/defn ->attending-update-transaction :- db.attending/AttendingTransaction
  [id :- s/Uuid
   students-id :- s/Uuid
   subjects-id :- s/Uuid]
  #:attending{:id id
              :removed false
              :students_id students-id
              :subjects_id subjects-id})
