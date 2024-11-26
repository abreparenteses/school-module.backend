(ns financial-module.schemas.wire.in.attending
  (:require [schema.core :as s]))

(s/defschema AttendingId
  {:id s/Uuid})

(s/defschema AttendingUpsertEntry
  {:students-id s/Uuid
   :subjects-id s/Uuid})

(s/defschema AttendingEntry
  {:id s/Uuid
   :students-id s/Uuid
   :subjects-id s/Uuid
   :created-at s/Inst})

(s/defschema AttendingHistory
  {:entries [AttendingEntry]})
