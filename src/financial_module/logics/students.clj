(ns financial-module.logics.students
  (:require [financial-module.adapters :as adapters]
            [financial-module.schemas.db.students :as db.students]
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
      (adapters/inst->utc-formated-string "yyyy-MM-dd hh:mm:ss")
      (str amount)
      uuid-from-string))

(s/defn ->students-transaction :- db.students/StudentsTransaction
  [date :- s/Int
   name :- s/Str
   document :- s/Str
   email :- s/Str
   phone :- s/Str
   birthdate :- s/Str]
  #:students{:id (uuid-from-date-amount date 0)
             :removed false
             :name name
             :document document
             :email email
             :phone phone
             :birthdate birthdate})

(s/defn ->students-update-transaction :- db.students/StudentsTransaction
  [id :- s/Uuid
   name :- s/Str
   document :- s/Str
   email :- s/Str
   phone :- s/Str
   birthdate :- s/Str]
  #:students{:id id
             :removed false
             :name name
             :document document
             :email email
             :phone phone
             :birthdate birthdate})
