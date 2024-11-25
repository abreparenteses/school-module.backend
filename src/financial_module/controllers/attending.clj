(ns financial-module.controllers.attending
  (:require [financial-module.db :as db]
            [financial-module.logics :as logics]
            [financial-module.ports.http-out :as http-out]
            [financial-module.schemas.db :as schemas.db]
            [financial-module.schemas.types :as schemas.types]
            [schema.core :as s]))

(defn- instant-now [] (java.util.Date/from (java.time.Instant/now)))

(s/defschema AccountsPayableHistory
  {:entries [schemas.db/AccountsPayableEntry]})

(s/defn get-accounts-payable :- AccountsPayableHistory
  [{:keys [database]} :- schemas.types/Components]
  {:entries (db/get-accounts-payable-all-transactions database)})

(s/defn get-accounts-payable-by-id :- AccountsPayableHistory
  [id :- s/Uuid
   {:keys [database]} :- schemas.types/Components]
  {:entries (db/get-accounts-payable-by-id id database)})

(s/defn add-entry! :- schemas.db/AccountsPayableEntry
  [name :- s/Str
   description :- s/Str
   amount :- schemas.types/PositiveNumber
   {:keys [database]} :- schemas.types/Components]
  (let [now (instant-now)
        entry (logics/->accounts-payable-transaction now name description amount)]
    (db/insert-accounts-payable-transaction entry database)))

(s/defn update-entry! :- schemas.db/AccountsPayableEntry
  [id :- s/Uuid
   name :- s/Str
   description :- s/Str
   amount :- schemas.types/PositiveNumber
   {:keys [database]} :- schemas.types/Components]
  (let [entry (logics/->accounts-payable-update-transaction id name description amount)
        result (db/update-accounts-payable-entry-transaction entry database)]
    (= 1 (:next.jdbc/update-count result))))

(s/defn remove-entry!
  [id :- s/Uuid
   {:keys [database]} :- schemas.types/Components]
  (db/remove-accounts-payable-entry id database))
