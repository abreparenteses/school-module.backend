(ns unit.school-module.adapters.subjects
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.properties :as properties]
            [schema-generators.generators :as g]
            [schema.core :as s]
            [schema.test :as schema.test]
            [school-module.adapters.subjects :as adapters.subjects]
            [school-module.schemas.db.subjects :as schemas.db.subjects]
            [school-module.schemas.types :as schemas.types]
            [school-module.schemas.wire.in.subjects :as wire.in.subjects]))

(use-fixtures :once schema.test/validate-schemas)

(deftest inst->utc-formated-string-test
  (testing "should adapt clojure/instant to formated string"
    (is (= "1987-02-10 09:38:43"
           (adapters.subjects/inst->utc-formated-string #inst "1987-02-10T09:38:43.000Z"
                                                        "yyyy-MM-dd hh:mm:ss")))))

(deftest db->wire-in-test
  (testing "should adapt db/subjects-entry to wire-in/subjects-entry"
    (let [id (random-uuid)
          name "some-string"
          description "some-description"
          courses-id (random-uuid)
          created_at #inst "2021-11-23T22:30:34"
          db-entry #:subjects{:id id
                              :name name
                              :description description
                              :courses_id courses-id
                              :created_at created_at}
          wire-in-entry {:id id
                         :name name
                         :description description
                         :courses-id courses-id
                         :created-at created_at}]
      (is (= wire-in-entry
             (adapters.subjects/db->wire-in db-entry))))))

(defspec db->wire-in-gen-test 50
  (properties/for-all [subjects-db (g/generator schemas.db.subjects/SubjectsEntry schemas.types/TypesLeafGenerators)]
                      (s/validate wire.in.subjects/SubjectsEntry (adapters.subjects/db->wire-in subjects-db))))

(deftest ->subjects-history-test
  (testing "should adapt db/subjects-entry to wire-in/subjects-history"
    (let [id (random-uuid)
          name "some-string"
          description "some-description"
          courses-id (random-uuid)
          created_at #inst "2021-11-23T22:30:34"
          db-entry #:subjects{:id id
                              :name name
                              :description description
                              :courses_id courses-id
                              :created_at created_at}
          wire-in-entry {:id id
                         :name name
                         :description description
                         :courses-id courses-id
                         :created-at created_at}
          db-entries [db-entry db-entry]
          wire-in-entries {:entries [wire-in-entry wire-in-entry]}]
      (is (= wire-in-entries
             (adapters.subjects/->subjects-history db-entries))))))

(defspec ->subjects-history-gen-test 50
  (properties/for-all [subjects-db (g/generator schemas.db.subjects/SubjectsEntry schemas.types/TypesLeafGenerators)]
                      (s/validate wire.in.subjects/SubjectsHistory (adapters.subjects/->subjects-history [subjects-db subjects-db]))))

(deftest ->subjects-test
  (testing "should adapt db/subjects-entry to wire-in/subjects-entry"
    (let [id (random-uuid)
          name "some-string"
          description "some-description"
          courses-id (random-uuid)
          created_at #inst "2021-11-23T22:30:34"
          db-entry #:subjects{:id id
                              :name name
                              :description description
                              :courses_id courses-id
                              :created_at created_at}
          wire-in-entry {:id id
                         :name name
                         :description description
                         :courses-id courses-id
                         :created-at created_at}]
      (is (= wire-in-entry
             (adapters.subjects/db->wire-in db-entry))))))

(defspec ->subjects-gen-test 50
  (properties/for-all [subjects-db (g/generator schemas.db.subjects/SubjectsEntry schemas.types/TypesLeafGenerators)]
                      (s/validate wire.in.subjects/SubjectsEntry (adapters.subjects/->subjects [subjects-db]))))
