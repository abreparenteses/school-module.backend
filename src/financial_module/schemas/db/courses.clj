(ns financial-module.schemas.db.courses
  (:require [schema.core :as s]))

(def courses {:courses/id s/Uuid
              :courses/removed s/Bool
              :courses/name s/Str
              :courses/description s/Str
              :courses/created_at s/Inst})

(s/defschema CoursesTransaction
  (select-keys courses [:courses/id
                        :courses/removed
                        :courses/name
                        :courses/description]))

(s/defschema CoursesEntry
  (select-keys courses [:courses/id
                        :courses/name
                        :courses/description
                        :courses/created_at]))
