(ns financial-module.schemas.wire.in.subjects
  (:require [schema.core :as s]))

(s/defschema SubjectsId
  {:id s/Uuid})

(s/defschema SubjectsUpsertEntry
  {:name s/Str
   :description s/Str
   :courses-id s/Uuid})

(s/defschema SubjectsEntry
  {:id s/Uuid
   :name s/Str
   :description s/Str
   :courses-id s/Uuid
   :created-at s/Inst})

(s/defschema SubjectsHistory
  {:entries [SubjectsEntry]})
