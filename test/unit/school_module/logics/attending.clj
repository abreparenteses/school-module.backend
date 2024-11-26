(ns unit.school-module.logics.attending
  (:require [clojure.test :refer [use-fixtures]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.properties :as properties]
            [schema-generators.generators :as g]
            [schema.core :as s]
            [schema.test :as schema.test]
            [school-module.logics.attending :as logics.attending]
            [school-module.schemas.db.attending :as schemas.db.attending]))

(use-fixtures :once schema.test/validate-schemas)

(defspec attending-transaction-test 50
  (properties/for-all [date (g/generator s/Inst)
                       students-id (g/generator s/Uuid)
                       subjects-id (g/generator s/Uuid)]
                      (s/validate schemas.db.attending/AttendingTransaction
                                  (logics.attending/->attending-transaction date students-id subjects-id))))

(defspec attending-update-transaction-test 50
  (properties/for-all [id (g/generator s/Uuid)
                       students-id (g/generator s/Uuid)
                       subjects-id (g/generator s/Uuid)]
                      (s/validate schemas.db.attending/AttendingTransaction
                                  (logics.attending/->attending-update-transaction id students-id subjects-id))))
