(ns financial-module.schemas.wire.in.users
  (:require [schema.core :as s]))

(s/defschema Users
  {:username s/Str
   :password s/Str})
