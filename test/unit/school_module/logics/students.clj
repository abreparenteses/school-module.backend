(ns unit.school-module.logics.students
  (:require [clojure.test :refer [use-fixtures]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.properties :as properties]
            [schema-generators.generators :as g]
            [schema.core :as s]
            [schema.test :as schema.test]
            [school-module.logics.students :as logics.students]
            [school-module.schemas.db.students :as schemas.db.students]))

(use-fixtures :once schema.test/validate-schemas)

(defspec students-transaction-test 50
  (properties/for-all [date (g/generator s/Inst)
                       name (g/generator s/Str)
                       document (g/generator s/Str)
                       email (g/generator s/Str)
                       phone (g/generator s/Str)]
                      (s/validate schemas.db.students/StudentsTransaction
                                  (logics.students/->students-transaction date name document email phone))))

(defspec students-update-transaction-test 50
  (properties/for-all [id (g/generator s/Uuid)
                       name (g/generator s/Str)
                       document (g/generator s/Str)
                       email (g/generator s/Str)
                       phone (g/generator s/Str)]
                      (s/validate schemas.db.students/StudentsTransaction
                                  (logics.students/->students-update-transaction id name document email phone))))
