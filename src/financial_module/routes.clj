(ns financial-module.routes
  (:require [financial-module.interceptors :as interceptors]
            [financial-module.ports.http-in :as ports.http-in]
            [financial-module.schemas.wire-in :as schemas.wire-in]
            [reitit.swagger :as swagger]
            [schema.core :as s]))

(def routes
  [["/swagger.json"
    {:get {:no-doc true
           :swagger {:info {:title "financial module api"
                            :description "small sample using the financial-module"}}
           :handler (swagger/create-swagger-handler)}}]

   ["/login"
    {:swagger {:tags ["auth-module"]}

     :post {:summary "login"
            :parameters {:body schemas.wire-in/User}
            :responses {200 {:body {:token s/Str}}
                        400 {:body s/Str}
                        500 {:body s/Str}}
            :handler ports.http-in/login}}]

   ["/accounts-payable"
    {:swagger {:tags ["financial-module"]}

     :get {:summary "get all accounts payable transactions"
           :interceptors [(interceptors/auth-validate-jwt-interceptor)]
           :responses {200 {:body schemas.wire-in/AccountsPayableHistory}
                       500 {:body s/Str}}
           :handler ports.http-in/get-history}

     :post {:summary "add an entry in the accounts payable"
            :interceptors [(interceptors/auth-validate-jwt-interceptor)]
            :parameters {:body schemas.wire-in/AccountsPayableUpsertEntry}
            :responses {201 {:body schemas.wire-in/AccountsPayableEntry}
                        400 {:body s/Str}
                        500 {:body s/Str}}
            :handler ports.http-in/add-entry!}}]

   ["/accounts-payable/:id"
    {:swagger {:tags ["financial-module"]}

     :get {:summary "get all accounts payable transactions by id"
           :interceptors [(interceptors/auth-validate-jwt-interceptor)]
           :parameters {:path schemas.wire-in/AccountsPayableId}
           :responses {200 {:body schemas.wire-in/AccountsPayableEntry}
                       500 {:body s/Str}}
           :handler ports.http-in/get-entry-by-id}

     :put {:summary "update an entry in the accounts payable"
           :interceptors [(interceptors/auth-validate-jwt-interceptor)]
           :parameters {:path schemas.wire-in/AccountsPayableId
                        :body schemas.wire-in/AccountsPayableUpsertEntry}
           :responses {202 {:body s/Str}
                       400 {:body s/Str}
                       500 {:body s/Str}}
           :handler ports.http-in/update-entry!}

     :delete {:summary "remove an entry in the accounts payable"
              :interceptors [(interceptors/auth-validate-jwt-interceptor)]
              :parameters {:path schemas.wire-in/AccountsPayableId}
              :responses {202 {:body s/Str}
                          400 {:body s/Str}
                          500 {:body s/Str}}
              :handler ports.http-in/remove-entry!}}]])
