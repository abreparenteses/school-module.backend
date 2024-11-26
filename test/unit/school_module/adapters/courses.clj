(ns unit.school-module.adapters.courses
  (:require [clojure.test :refer [deftest is testing use-fixtures]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.properties :as properties]
            [schema-generators.generators :as g]
            [schema.core :as s]
            [schema.test :as schema.test]
            [school-module.adapters.courses :as adapters.courses]
            [school-module.schemas.db.courses :as schemas.db.courses]
            [school-module.schemas.types :as schemas.types]
            [school-module.schemas.wire.in.courses :as wire.in.courses]))

(use-fixtures :once schema.test/validate-schemas)

(deftest inst->utc-formated-string-test
  (testing "should adapt clojure/instant to formated string"
    (is (= "1987-02-10 09:38:43"
           (adapters.courses/inst->utc-formated-string #inst "1987-02-10T09:38:43.000Z"
                                                       "yyyy-MM-dd hh:mm:ss")))))

(deftest db->wire-in-test
  (testing "should adapt db/courses-entry to wire-in/courses-entry"
    (let [id (random-uuid)
          name "some-string"
          description "some-description"
          created_at #inst "2021-11-23T22:30:34"
          db-entry #:courses{:id id
                             :name name
                             :description description
                             :created_at created_at}
          wire-in-entry {:id id
                         :name name
                         :description description
                         :created-at created_at}]
      (is (= wire-in-entry
             (adapters.courses/db->wire-in db-entry))))))

(defspec db->wire-in-gen-test 50
  (properties/for-all [courses-db (g/generator schemas.db.courses/CoursesEntry schemas.types/TypesLeafGenerators)]
                      (s/validate wire.in.courses/CoursesEntry (adapters.courses/db->wire-in courses-db))))

(deftest ->courses-history-test
  (testing "should adapt db/courses-entry to wire-in/courses-history"
    (let [id (random-uuid)
          name "some-string"
          description "some-description"
          created_at #inst "2021-11-23T22:30:34"
          db-entry #:courses{:id id
                             :name name
                             :description description
                             :created_at created_at}
          wire-in-entry {:id id
                         :name name
                         :description description
                         :created-at created_at}
          db-entries [db-entry db-entry]
          wire-in-entries {:entries [wire-in-entry wire-in-entry]}]
      (is (= wire-in-entries
             (adapters.courses/->courses-history db-entries))))))

(defspec ->courses-history-gen-test 50
  (properties/for-all [courses-db (g/generator schemas.db.courses/CoursesEntry schemas.types/TypesLeafGenerators)]
                      (s/validate wire.in.courses/CoursesHistory (adapters.courses/->courses-history [courses-db courses-db]))))

(deftest ->courses-test
  (testing "should adapt db/courses-entry to wire-in/courses-entry"
    (let [id (random-uuid)
          name "some-string"
          description "some-description"
          created_at #inst "2021-11-23T22:30:34"
          db-entry #:courses{:id id
                             :name name
                             :description description
                             :created_at created_at}
          wire-in-entry {:id id
                         :name name
                         :description description
                         :created-at created_at}]
      (is (= wire-in-entry
             (adapters.courses/->courses [db-entry]))))))

(defspec ->courses-gen-test 50
  (properties/for-all [courses-db (g/generator schemas.db.courses/CoursesEntry schemas.types/TypesLeafGenerators)]
                      (s/validate wire.in.courses/CoursesEntry (adapters.courses/->courses [courses-db]))))
