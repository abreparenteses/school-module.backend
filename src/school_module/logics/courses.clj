(ns school-module.logics.courses
  (:require [school-module.adapters.courses :as adapters.courses]
            [school-module.schemas.db.courses :as db.courses]
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
      (adapters.courses/inst->utc-formated-string "yyyy-MM-dd hh:mm:ss")
      (str amount)
      uuid-from-string))

(s/defn ->courses-transaction :- db.courses/CoursesTransaction
  [date :- s/Inst
   name :- s/Str
   description :- s/Str]
  #:courses{:id (uuid-from-date-amount date 0)
            :removed false
            :name name
            :description description})

(s/defn ->courses-update-transaction :- db.courses/CoursesTransaction
  [id :- s/Uuid
   name :- s/Str
   description :- s/Str]
  #:courses{:id id
            :removed false
            :name name
            :description description})
