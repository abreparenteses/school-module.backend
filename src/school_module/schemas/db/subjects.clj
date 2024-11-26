(ns school-module.schemas.db.subjects
  (:require [schema.core :as s]))

(def subjects {:subjects/id s/Uuid
               :subjects/removed s/Bool
               :subjects/name s/Str
               :subjects/description s/Str
               :subjects/courses_id s/Uuid
               :subjects/created_at s/Inst})

(s/defschema SubjectsTransaction
  (select-keys subjects [:subjects/id
                         :subjects/removed
                         :subjects/name
                         :subjects/description
                         :subjects/courses_id]))

(s/defschema SubjectsEntry
  (select-keys subjects [:subjects/id
                         :subjects/name
                         :subjects/description
                         :subjects/courses_id
                         :subjects/created_at]))
