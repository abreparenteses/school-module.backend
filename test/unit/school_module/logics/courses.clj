(ns unit.school-module.logics.courses
  (:require [clojure.test :refer [use-fixtures]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.properties :as properties]
            [schema-generators.generators :as g]
            [schema.core :as s]
            [schema.test :as schema.test]
            [school-module.logics.courses :as logics.courses]
            [school-module.schemas.db.courses :as schemas.db.courses]))

(use-fixtures :once schema.test/validate-schemas)

(defspec courses-transaction-test 50
  (properties/for-all [date (g/generator s/Inst)
                       name (g/generator s/Str)
                       description (g/generator s/Str)]
                      (s/validate schemas.db.courses/CoursesTransaction
                                  (logics.courses/->courses-transaction date name description))))

(defspec courses-update-transaction-test 50
  (properties/for-all [id (g/generator s/Uuid)
                       name (g/generator s/Str)
                       description (g/generator s/Str)]
                      (s/validate schemas.db.courses/CoursesTransaction
                                  (logics.courses/->courses-update-transaction id name description))))
