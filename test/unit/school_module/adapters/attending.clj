(ns unit.school-module.adapters.attending
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.properties :as properties]
            [schema-generators.generators :as g]
            [schema.core :as s]
            [schema.test :as schema.test]
            [school-module.adapters.attending :as adapters.attending]
            [school-module.schemas.db.attending :as schemas.db.attending]
            [school-module.schemas.types :as schemas.types]
            [school-module.schemas.wire.in.attending :as wire.in.attending]))

(use-fixtures :once schema.test/validate-schemas)

(deftest inst->utc-formated-string-test
  (testing "should adapt clojure/instant to formated string"
    (is (= "1987-02-10 09:38:43"
           (adapters.attending/inst->utc-formated-string #inst "1987-02-10T09:38:43.000Z"
                                                         "yyyy-MM-dd hh:mm:ss")))))

(deftest db->wire-in-test
  (testing "should adapt db/attending-entry to wire-in/attending-entry"
    (let [id (random-uuid)
          students_id (random-uuid)
          subjects_id (random-uuid)
          created_at #inst "2021-11-23T22:30:34"
          db-entry #:attending{:id id
                               :students_id students_id
                               :subjects_id subjects_id
                               :created_at created_at}
          wire-in-entry {:id id
                         :students-id students_id
                         :subjects-id subjects_id
                         :created-at created_at}]
      (is (= wire-in-entry
             (adapters.attending/db->wire-in db-entry))))))

(defspec db->wire-in-gen-test 50
  (properties/for-all [attending-db (g/generator schemas.db.attending/AttendingEntry schemas.types/TypesLeafGenerators)]
                      (s/validate wire.in.attending/AttendingEntry (adapters.attending/db->wire-in attending-db))))

(deftest ->attending-history-test
  (testing "should adapt db/attending-entry to wire-in/attending-history"
    (let [id (random-uuid)
          students_id (random-uuid)
          subjects_id (random-uuid)
          created_at #inst "2021-11-23T22:30:34"
          db-entry #:attending{:id id
                               :students_id students_id
                               :subjects_id subjects_id
                               :created_at created_at}
          wire-in-entry {:id id
                         :students-id students_id
                         :subjects-id subjects_id
                         :created-at created_at}
          db-entries [db-entry db-entry]
          wire-in-entries {:entries [wire-in-entry wire-in-entry]}]
      (is (= wire-in-entries
             (adapters.attending/->attending-history db-entries))))))

(defspec ->attending-history-gen-test 50
  (properties/for-all [attending-db (g/generator schemas.db.attending/AttendingEntry schemas.types/TypesLeafGenerators)]
                      (s/validate wire.in.attending/AttendingHistory (adapters.attending/->attending-history [attending-db attending-db]))))

(deftest ->attending-test
  (testing "should adapt db/attending-entry to wire-in/attending-entry"
    (let [id (random-uuid)
          students_id (random-uuid)
          subjects_id (random-uuid)
          created_at #inst "2021-11-23T22:30:34"
          db-entry #:attending{:id id
                               :students_id students_id
                               :subjects_id subjects_id
                               :created_at created_at}
          wire-in-entry {:id id
                         :students-id students_id
                         :subjects-id subjects_id
                         :created-at created_at}]
      (is (= wire-in-entry
             (adapters.attending/->attending [db-entry]))))))

(defspec ->attending-gen-test 50
  (properties/for-all [attending-db (g/generator schemas.db.attending/AttendingEntry schemas.types/TypesLeafGenerators)]
                      (s/validate wire.in.attending/AttendingEntry (adapters.attending/->attending [attending-db]))))
