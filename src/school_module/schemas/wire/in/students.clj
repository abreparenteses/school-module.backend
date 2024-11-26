(ns school-module.schemas.wire.in.students
  (:require [schema.core :as s]))

(s/defschema StudentsId
  {:id s/Uuid})

(s/defschema StudentsUpsertEntry
  {:name s/Str
   :email s/Str
   :document s/Str
   :phone s/Str})

(s/defschema StudentsEntry
  {:id s/Uuid
   :name s/Str
   :email s/Str
   :document s/Str
   :phone s/Str
   :created-at s/Inst})

(s/defschema StudentsHistory
  {:entries [StudentsEntry]})
