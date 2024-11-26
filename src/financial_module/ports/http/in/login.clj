(ns financial-module.ports.http.in.login
  (:require [financial-module.ports.jwt :as jwt]))

(defn login
  [{{{:keys [username]} :body} :parameters
    components :components}]
  (let [token (jwt/encrypt {:username username} (:config components))]
    {:status 200
     :body {:token token}}))
