(ns school-module.schemas.types
  (:require [clojure.test.check.generators :as generators]
            [com.stuartsierra.component :as component]
            [parenthesin.components.db.jdbc-hikari :as components.db]
            [parenthesin.components.http.clj-http :as components.http]
            [schema.core :as s]))

(def PositiveNumber
  (s/constrained s/Num pos? 'PositiveNumber))

(def PositiveNumberGenerator
  (generators/fmap bigdec (generators/double* {:infinite? false :NaN? false :min 0.0001})))

(def NegativeNumber
  (s/constrained s/Num neg? 'NegativeNumber))

(def NegativeNumberGenerator
  (generators/fmap bigdec (generators/double* {:infinite? false :NaN? false :max -0.0001})))

(def NumberGenerator
  (generators/fmap bigdec (generators/double* {:infinite? false :NaN? false})))

(def TypesLeafGenerators
  {PositiveNumber PositiveNumberGenerator
   NegativeNumber NegativeNumberGenerator
   s/Num NumberGenerator})

(def HttpComponent (s/protocol components.http/HttpProvider))

(def DatabaseComponent (s/protocol components.db/DatabaseProvider))

(s/defschema Components
  {:config (s/protocol component/Lifecycle)
   :http HttpComponent
   :router (s/protocol component/Lifecycle)
   :database DatabaseComponent
   s/Keyword s/Any})
