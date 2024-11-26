(ns school-module.schemas.db.attending
  (:require [schema.core :as s]))

(def attending {:attending/id s/Uuid
                :attending/removed s/Bool
                :attending/students_id s/Uuid
                :attending/subjects_id s/Uuid
                :attending/created_at s/Inst})

(s/defschema AttendingTransaction
  (select-keys attending [:attending/id
                          :attending/removed
                          :attending/students_id
                          :attending/subjects_id]))

(s/defschema AttendingEntry
  (select-keys attending [:attending/id
                          :attending/students_id
                          :attending/subjects_id
                          :attending/created_at]))
