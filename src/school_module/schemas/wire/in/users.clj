(ns school-module.schemas.wire.in.users
  (:require [schema.core :as s]))

(s/defschema User
  {:username s/Str
   :password s/Str})
