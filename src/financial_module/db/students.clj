(ns financial-module.db.students
  (:require [financial-module.schemas.db.students :as db.students]
            [financial-module.schemas.types :as schemas.types]
            [honey.sql :as sql]
            [honey.sql.helpers :as sql.helpers]
            [parenthesin.components.db.jdbc-hikari :as components.database]
            [schema.core :as s]))

(s/defn insert-students-transaction
  [transaction :- db.students/StudentsTransaction
   db :- schemas.types/DatabaseComponent]
  (->> (-> (sql.helpers/insert-into :students)
           (sql.helpers/values [transaction])
           (sql.helpers/returning :*)
           sql/format)
       (components.database/execute db)
       first))

(s/defn update-students-transaction
  [{:students/keys [id name document email phone]} :- db.students/StudentsTransaction
   db :- schemas.types/DatabaseComponent]
  (->> (-> (sql.helpers/update :students)
           (sql.helpers/set {:name name
                             :document document
                             :email email
                             :phone phone})
           (sql.helpers/where [:= :id id])
           sql/format)
       (components.database/execute db)
       first))

(s/defn remove-students
  [id :- s/Uuid
   db :- schemas.types/DatabaseComponent]
  (->> (-> (sql.helpers/update :students)
           (sql.helpers/set {:removed true})
           (sql.helpers/where [:= :id id])
           sql/format)
       (components.database/execute db)))

(s/defn get-students-all-transactions :- [db.students/StudentsEntry]
  [db :- schemas.types/DatabaseComponent]
  (components.database/execute
   db
   (-> (sql.helpers/select :id :name :document :email :phone :created_at)
       (sql.helpers/from :students)
       (sql.helpers/where [:= :removed false])
       (sql.helpers/order-by [:created_at :desc])
       sql/format)))

(s/defn get-students-by-id :- [db.students/StudentsEntry]
  [id :- s/Uuid
   db :- schemas.types/DatabaseComponent]
  (components.database/execute
   db
   (-> (sql.helpers/select :id :name :document :email :phone :created_at)
       (sql.helpers/from :students)
       (sql.helpers/where [:= :removed false]
                          [:= :id id])
       (sql.helpers/order-by [:created_at :desc])
       sql/format)))
