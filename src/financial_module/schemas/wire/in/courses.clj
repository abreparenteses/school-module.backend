(ns financial-module.schemas.wire.in.courses
  (:require [schema.core :as s]))

(s/defschema CoursesId
  {:id s/Uuid})

(s/defschema CoursesUpsertEntry
  {:name s/Str
   :description s/Str})

(s/defschema CoursesEntry
  {:id s/Uuid
   :name s/Str
   :description s/Str
   :created-at s/Inst})

(s/defschema CoursesHistory
  {:entries [CoursesEntry]
   :total s/Num})
