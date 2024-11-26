(ns unit.school-module.adapters.students
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.properties :as properties]
            [schema-generators.generators :as g]
            [schema.core :as s]
            [schema.test :as schema.test]
            [school-module.adapters.students :as adapters.students]
            [school-module.schemas.db.students :as schemas.db.students]
            [school-module.schemas.types :as schemas.types]
            [school-module.schemas.wire.in.students :as wire.in.students]))

(use-fixtures :once schema.test/validate-schemas)

(deftest inst->utc-formated-string-test
  (testing "should adapt clojure/instant to formated string"
    (is (= "1987-02-10 09:38:43"
           (adapters.students/inst->utc-formated-string #inst "1987-02-10T09:38:43.000Z"
                                                        "yyyy-MM-dd hh:mm:ss")))))

(deftest db->wire-in-test
  (testing "should adapt db/students-entry to wire-in/students-entry"
    (let [id (random-uuid)
          name "some-string"
          document "some-document"
          email "some-email"
          phone "some-phone"
          created_at #inst "2021-11-23T22:30:34"
          db-entry #:students{:id id
                              :name name
                              :document document
                              :email email
                              :phone phone
                              :created_at created_at}
          wire-in-entry {:id id
                         :name name
                         :document document
                         :email email
                         :phone phone
                         :created-at created_at}]
      (is (= wire-in-entry
             (adapters.students/db->wire-in db-entry))))))

(defspec db->wire-in-gen-test 50
  (properties/for-all [students-db (g/generator schemas.db.students/StudentsEntry schemas.types/TypesLeafGenerators)]
                      (s/validate wire.in.students/StudentsEntry (adapters.students/db->wire-in students-db))))

(deftest ->students-history-test
  (testing "should adapt db/students-entry to wire-in/students-history"
    (let [id (random-uuid)
          name "some-string"
          document "some-document"
          email "some-email"
          phone "some-phone"
          created_at #inst "2021-11-23T22:30:34"
          db-entry #:students{:id id
                              :name name
                              :document document
                              :email email
                              :phone phone
                              :created_at created_at}
          wire-in-entry {:id id
                         :name name
                         :document document
                         :email email
                         :phone phone
                         :created-at created_at}
          db-entries [db-entry db-entry]
          wire-in-entries {:entries [wire-in-entry wire-in-entry]}]
      (is (= wire-in-entries
             (adapters.students/->students-history db-entries))))))

(defspec ->students-history-gen-test 50
  (properties/for-all [students-db (g/generator schemas.db.students/StudentsEntry schemas.types/TypesLeafGenerators)]
                      (s/validate wire.in.students/StudentsHistory (adapters.students/->students-history [students-db students-db]))))

(deftest ->students-test
  (testing "should adapt db/students-entry to wire-in/students-entry"
    (let [id (random-uuid)
          name "some-string"
          document "some-document"
          email "some-email"
          phone "some-phone"
          created_at #inst "2021-11-23T22:30:34"
          db-entry #:students{:id id
                              :name name
                              :document document
                              :email email
                              :phone phone
                              :created_at created_at}
          wire-in-entry {:id id
                         :name name
                         :document document
                         :email email
                         :phone phone
                         :created-at created_at}]
      (is (= wire-in-entry
             (adapters.students/db->wire-in db-entry))))))

(defspec ->students-gen-test 50
  (properties/for-all [students-db (g/generator schemas.db.students/StudentsEntry schemas.types/TypesLeafGenerators)]
                      (s/validate wire.in.students/StudentsEntry (adapters.students/db->wire-in students-db))))
