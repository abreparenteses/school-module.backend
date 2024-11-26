(ns integration.school-module.db-test
  (:require [clojure.test :as clojure.test]
            [com.stuartsierra.component :as component]
            [school-module.db :as db]
            [integration.school-module.util :as util]
            [parenthesin.components.config.aero :as components.config]
            [parenthesin.components.db.jdbc-hikari :as components.database]
            [schema.test :as schema.test]
            [state-flow.api :refer [defflow]]
            [state-flow.assertions.matcher-combinators :refer [match?]]
            [state-flow.core :as state-flow :refer [flow]]
            [state-flow.state :as state]))

(clojure.test/use-fixtures :once schema.test/validate-schemas)

(defn- create-and-start-components! []
  (component/start-system
   (component/system-map
    :config (components.config/new-config)
    :database (component/using (components.database/new-database)
                               [:config]))))

(defflow
  flow-integration-db-test
  {:init (util/start-system! create-and-start-components!)
   :cleanup util/stop-system!
   :fail-fast? true}
  (flow "creates a table, insert data and checks return in the database"
    [database (state-flow.api/get-state :database)]

    (state/invoke
     #(db/insert-accounts-payable-transaction {:accounts_payable/id #uuid "cd989358-af38-4a2f-a1a1-88096aa425a7"
                                               :accounts_payable/removed false
                                               :accounts_payable/name "name"
                                               :accounts_payable/description "description"
                                               :accounts_payable/amount 100.00M}
                                              database))

    (flow "check transaction was inserted in db"
      (match? [#:accounts_payable{:id #uuid "cd989358-af38-4a2f-a1a1-88096aa425a7"
                                  :name "name"
                                  :description "description"
                                  :amount 100.00M
                                  :created_at inst?}]
              (db/get-accounts-payable-all-transactions database)))))
