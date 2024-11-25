(ns financial-module.ports.jwt
  (:require [buddy.core.hash :as hash]
            [buddy.sign.jwt :as jwt]))

(defn encrypt
  [data config]
  (let [secret (-> config :config :jwt :secret hash/sha256)]
    (jwt/encrypt data secret {:alg :dir :enc :a128cbc-hs256})))

(defn decrypt
  [token config]
  (let [secret (-> config :config :jwt :secret hash/sha256)]
    (-> token
        (jwt/decrypt secret))))
