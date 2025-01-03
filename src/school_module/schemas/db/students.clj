(ns school-module.schemas.db.students
  (:require [schema.core :as s]))

(def students {:students/id s/Uuid
               :students/removed s/Bool
               :students/name s/Str
               :students/document s/Str
               :students/email s/Str
               :students/phone s/Str
               :students/created_at s/Inst})

(s/defschema StudentsTransaction
  (select-keys students [:students/id
                         :students/removed
                         :students/name
                         :students/document
                         :students/email
                         :students/phone]))

(s/defschema StudentsEntry
  (select-keys students [:students/id
                         :students/name
                         :students/document
                         :students/email
                         :students/phone
                         :students/created_at]))

