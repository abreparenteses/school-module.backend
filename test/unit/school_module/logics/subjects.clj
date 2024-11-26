(ns unit.school-module.logics.subjects
  (:require [clojure.test :refer [use-fixtures]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.properties :as properties]
            [schema-generators.generators :as g]
            [schema.core :as s]
            [schema.test :as schema.test]
            [school-module.logics.subjects :as logics.subjects]
            [school-module.schemas.db.subjects :as schemas.db.subjects]))

(use-fixtures :once schema.test/validate-schemas)

(defspec subjects-transaction-test 50
  (properties/for-all [date (g/generator s/Inst)
                       name (g/generator s/Str)
                       description (g/generator s/Str)
                       courses-id (g/generator s/Uuid)]
                      (s/validate schemas.db.subjects/SubjectsTransaction
                                  (logics.subjects/->subjects-transaction date name description courses-id))))

(defspec subjects-update-transaction-test 50
  (properties/for-all [id (g/generator s/Uuid)
                       name (g/generator s/Str)
                       description (g/generator s/Str)
                       courses-id (g/generator s/Uuid)]
                      (s/validate schemas.db.subjects/SubjectsTransaction
                                  (logics.subjects/->subjects-update-transaction id name description courses-id))))
