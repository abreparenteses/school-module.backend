(ns school-module.db.courses
  (:require [school-module.schemas.db.courses :as db.courses]
            [school-module.schemas.types :as schemas.types]
            [honey.sql :as sql]
            [honey.sql.helpers :as sql.helpers]
            [parenthesin.components.db.jdbc-hikari :as components.database]
            [schema.core :as s]))

(s/defn insert-courses-transaction
  [transaction :- db.courses/CoursesTransaction
   db :- schemas.types/DatabaseComponent]
  (->> (-> (sql.helpers/insert-into :courses)
           (sql.helpers/values [transaction])
           (sql.helpers/returning :*)
           sql/format)
       (components.database/execute db)
       first))

(s/defn update-courses-transaction
  [{:courses/keys [id name description]} :- db.courses/CoursesTransaction
   db :- schemas.types/DatabaseComponent]
  (->> (-> (sql.helpers/update :courses)
           (sql.helpers/set {:name name
                             :description description})
           (sql.helpers/where [:= :id id])
           sql/format)
       (components.database/execute db)
       first))

(s/defn remove-courses
  [id :- s/Uuid
   db :- schemas.types/DatabaseComponent]
  (->> (-> (sql.helpers/update :courses)
           (sql.helpers/set {:removed true})
           (sql.helpers/where [:= :id id])
           sql/format)
       (components.database/execute db)))

(s/defn get-courses-all-transactions :- [db.courses/CoursesEntry]
  [db :- schemas.types/DatabaseComponent]
  (components.database/execute
   db
   (-> (sql.helpers/select :id :name :description :created_at)
       (sql.helpers/from :courses)
       (sql.helpers/where [:= :removed false])
       (sql.helpers/order-by [:created_at :desc])
       sql/format)))

(s/defn get-courses-by-id :- [db.courses/CoursesEntry]
  [id :- s/Uuid
   db :- schemas.types/DatabaseComponent]
  (components.database/execute
   db
   (-> (sql.helpers/select :id :name :description :created_at)
       (sql.helpers/from :courses)
       (sql.helpers/where [:= :removed false]
                          [:= :id id])
       (sql.helpers/order-by [:created_at :desc])
       sql/format)))
