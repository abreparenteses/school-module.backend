(ns integration.school-module.accounts-payable-test
  (:require [clojure.test :as clojure.test]
            [com.stuartsierra.component :as component]
            [school-module.routes :as routes]
            [integration.school-module.util :as util]
            [matcher-combinators.matchers :as matchers]
            [parenthesin.components.config.aero :as components.config]
            [parenthesin.components.db.jdbc-hikari :as components.database]
            [parenthesin.components.http.clj-http :as components.http]
            [parenthesin.components.router.reitit-schema :as components.router]
            [parenthesin.components.server.reitit-pedestal-jetty :as components.webserver]
            [parenthesin.helpers.state-flow.server.pedestal :as state-flow.server]
            [schema.test :as schema.test]
            [state-flow.api :refer [defflow]]
            [state-flow.assertions.matcher-combinators :refer [match?]]
            [state-flow.core :as state-flow :refer [flow]]))

(clojure.test/use-fixtures :once schema.test/validate-schemas)

(defn- create-and-start-components! []
  (component/start-system
   (component/system-map
    :config (components.config/new-config)
    :http (components.http/new-http-mock {})
    :router (components.router/new-router routes/routes)
    :database (component/using (components.database/new-database)
                               [:config])
    :webserver (component/using (components.webserver/new-webserver)
                                [:config :http :router :database]))))

(defflow
  flow-integration-accounts-payable-test
  {:init (util/start-system! create-and-start-components!)
   :cleanup util/stop-system!
   :fail-fast? true}
  (flow "should interact with system"
    (flow "should make login and return a token"
      (match? (matchers/embeds {:status 200
                                :body  {:token string?}})
              (state-flow.server/request! {:method :post
                                           :uri    "/login"
                                           :body   {:username "admin"
                                                    :password "admin"}})))

    (flow "should not insert accounts payable transaction"
      (match? (matchers/embeds {:status 401
                                :body "Invalid request signature"})
              (state-flow.server/request! {:method :post
                                           :uri    "/accounts-payable"
                                           :headers {"authorization:" "x"}
                                           :body   {:name "name"
                                                    :description "description"
                                                    :amount 100.00M}})))))
