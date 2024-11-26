(ns school-module.db.attending
  (:require [school-module.schemas.db.attending :as db.attending]
            [school-module.schemas.types :as schemas.types]
            [honey.sql :as sql]
            [honey.sql.helpers :as sql.helpers]
            [parenthesin.components.db.jdbc-hikari :as components.database]
            [schema.core :as s]))

(s/defn insert-attending-transaction
  [transaction :- db.attending/AttendingTransaction
   db :- schemas.types/DatabaseComponent]
  (->> (-> (sql.helpers/insert-into :attending)
           (sql.helpers/values [transaction])
           (sql.helpers/returning :*)
           sql/format)
       (components.database/execute db)
       first))

(s/defn update-attending-transaction
  [{:attending/keys [id students_id subjects_id]} :- db.attending/AttendingTransaction
   db :- schemas.types/DatabaseComponent]
  (->> (-> (sql.helpers/update :attending)
           (sql.helpers/set {:students_id students_id
                             :subjects_id subjects_id})
           (sql.helpers/where [:= :id id])
           sql/format)
       (components.database/execute db)
       first))

(s/defn remove-attending
  [id :- s/Uuid
   db :- schemas.types/DatabaseComponent]
  (->> (-> (sql.helpers/update :attending)
           (sql.helpers/set {:removed true})
           (sql.helpers/where [:= :id id])
           sql/format)
       (components.database/execute db)))

(s/defn get-attending-all-transactions :- [db.attending/AttendingEntry]
  [db :- schemas.types/DatabaseComponent]
  (components.database/execute
   db
   (-> (sql.helpers/select :id :students_id :subjects_id :created_at)
       (sql.helpers/from :attending)
       (sql.helpers/where [:= :removed false])
       (sql.helpers/order-by [:created_at :desc])
       sql/format)))

(s/defn get-attending-by-id :- [db.attending/AttendingEntry]
  [id :- s/Uuid
   db :- schemas.types/DatabaseComponent]
  (components.database/execute
   db
   (-> (sql.helpers/select :id :students_id :subjects_id :created_at)
       (sql.helpers/from :attending)
       (sql.helpers/where [:= :removed false]
                          [:= :id id])
       (sql.helpers/order-by [:created_at :desc])
       sql/format)))
