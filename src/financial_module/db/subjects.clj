(ns financial-module.db.subjects
  (:require [financial-module.schemas.db.subjects :as db.subjects]
            [financial-module.schemas.types :as schemas.types]
            [honey.sql :as sql]
            [honey.sql.helpers :as sql.helpers]
            [parenthesin.components.db.jdbc-hikari :as components.database]
            [schema.core :as s]))

(s/defn insert-subjects-transaction
  [transaction :- db.subjects/SubjectsTransaction
   db :- schemas.types/DatabaseComponent]
  (->> (-> (sql.helpers/insert-into :subjects)
           (sql.helpers/values [transaction])
           (sql.helpers/returning :*)
           sql/format)
       (components.database/execute db)
       first))

(s/defn update-subjects-transaction
  [{:subjects/keys [id name description courses_id]} :- db.subjects/SubjectsTransaction
   db :- schemas.types/DatabaseComponent]
  (->> (-> (sql.helpers/update :subjects)
           (sql.helpers/set {:name name
                             :description description
                             :courses_id courses_id})
           (sql.helpers/where [:= :id id])
           sql/format)
       (components.database/execute db)
       first))

(s/defn remove-subjects
  [id :- s/Uuid
   db :- schemas.types/DatabaseComponent]
  (->> (-> (sql.helpers/update :subjects)
           (sql.helpers/set {:removed true})
           (sql.helpers/where [:= :id id])
           sql/format)
       (components.database/execute db)))

(s/defn get-subjects-all-transactions :- [db.subjects/SubjectsEntry]
  [db :- schemas.types/DatabaseComponent]
  (components.database/execute
   db
   (-> (sql.helpers/select :id :name :description :courses_id :created_at)
       (sql.helpers/from :subjects)
       (sql.helpers/where [:= :removed false])
       (sql.helpers/order-by [:created_at :desc])
       sql/format)))

(s/defn get-subjects-by-id :- [db.subjects/SubjectsEntry]
  [id :- s/Uuid
   db :- schemas.types/DatabaseComponent]
  (components.database/execute
   db
   (-> (sql.helpers/select :id :name :description :courses_id :created_at)
       (sql.helpers/from :subjects)
       (sql.helpers/where [:= :removed false]
                          [:= :id id])
       (sql.helpers/order-by [:created_at :desc])
       sql/format)))
