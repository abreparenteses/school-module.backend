(ns school-module.routes
  (:require [school-module.interceptors :as interceptors]
            [school-module.ports.http.in.attending :as http.in.attending]
            [school-module.ports.http.in.courses :as http.in.courses]
            [school-module.ports.http.in.login :as http.in.login]
            [school-module.ports.http.in.students :as http.in.students]
            [school-module.ports.http.in.subjects :as http.in.subjects]
            [school-module.schemas.wire.in.attending :as wire.in.attending]
            [school-module.schemas.wire.in.courses :as wire.in.courses]
            [school-module.schemas.wire.in.students :as wire.in.students]
            [school-module.schemas.wire.in.subjects :as wire.in.subjects]
            [school-module.schemas.wire.in.users :as wire.in.users]
            [reitit.swagger :as swagger]
            [schema.core :as s]))

(def routes
  [["/swagger.json"
    {:get {:no-doc true
           :swagger {:info {:title "school module api"
                            :description "small sample using the school-module"}}
           :handler (swagger/create-swagger-handler)}}]

   ["/login"
    {:swagger {:tags ["auth"]}

     :post {:summary "login"
            :parameters {:body wire.in.users/User}
            :responses {200 {:body {:token s/Str}}
                        400 {:body s/Str}
                        500 {:body s/Str}}
            :handler http.in.login/login}}]

   ["/courses"
    {:swagger {:tags ["courses"]}

     :get {:summary "get all courses transactions"
           :interceptors [(interceptors/auth-validate-jwt-interceptor)]
           :responses {200 {:body wire.in.courses/CoursesHistory}
                       500 {:body s/Str}}
           :handler http.in.courses/get-history}

     :post {:summary "add an entry in the courses"
            :interceptors [(interceptors/auth-validate-jwt-interceptor)]
            :parameters {:body wire.in.courses/CoursesUpsertEntry}
            :responses {201 {:body wire.in.courses/CoursesEntry}
                        400 {:body s/Str}
                        500 {:body s/Str}}
            :handler http.in.courses/add-entry!}}]

   ["/courses/:id"
    {:swagger {:tags ["courses"]}

     :get {:summary "get all courses transactions by id"
           :interceptors [(interceptors/auth-validate-jwt-interceptor)]
           :parameters {:path wire.in.courses/CoursesId}
           :responses {200 {:body wire.in.courses/CoursesEntry}
                       500 {:body s/Str}}
           :handler http.in.courses/get-entry-by-id}

     :put {:summary "update an entry in the courses"
           :interceptors [(interceptors/auth-validate-jwt-interceptor)]
           :parameters {:path wire.in.courses/CoursesId
                        :body wire.in.courses/CoursesUpsertEntry}
           :responses {202 {:body s/Str}
                       400 {:body s/Str}
                       500 {:body s/Str}}
           :handler http.in.courses/update-entry!}

     :delete {:summary "remove an entry in the courses"
              :interceptors [(interceptors/auth-validate-jwt-interceptor)]
              :parameters {:path wire.in.courses/CoursesId}
              :responses {202 {:body s/Str}
                          400 {:body s/Str}
                          500 {:body s/Str}}
              :handler http.in.courses/remove-entry!}}]

   ["/students"
    {:swagger {:tags ["students"]}

     :get {:summary "get all students transactions"
           :interceptors [(interceptors/auth-validate-jwt-interceptor)]
           :responses {200 {:body wire.in.students/StudentsHistory}
                       500 {:body s/Str}}
           :handler http.in.students/get-history}

     :post {:summary "add an entry in the students"
            :interceptors [(interceptors/auth-validate-jwt-interceptor)]
            :parameters {:body wire.in.students/StudentsUpsertEntry}
            :responses {201 {:body wire.in.students/StudentsEntry}
                        400 {:body s/Str}
                        500 {:body s/Str}}
            :handler http.in.students/add-entry!}}]

   ["/students/:id"
    {:swagger {:tags ["students"]}

     :get {:summary "get all students transactions by id"
           :interceptors [(interceptors/auth-validate-jwt-interceptor)]
           :parameters {:path wire.in.students/StudentsId}
           :responses {200 {:body wire.in.students/StudentsEntry}
                       500 {:body s/Str}}
           :handler http.in.students/get-entry-by-id}

     :put {:summary "update an entry in the students"
           :interceptors [(interceptors/auth-validate-jwt-interceptor)]
           :parameters {:path wire.in.students/StudentsId
                        :body wire.in.students/StudentsUpsertEntry}
           :responses {202 {:body s/Str}
                       400 {:body s/Str}
                       500 {:body s/Str}}
           :handler http.in.students/update-entry!}

     :delete {:summary "remove an entry in the students"
              :interceptors [(interceptors/auth-validate-jwt-interceptor)]
              :parameters {:path wire.in.students/StudentsId}
              :responses {202 {:body s/Str}
                          400 {:body s/Str}
                          500 {:body s/Str}}
              :handler http.in.students/remove-entry!}}]

   ["/subjects"
    {:swagger {:tags ["subjects"]}

     :get {:summary "get all subjects transactions"
           :interceptors [(interceptors/auth-validate-jwt-interceptor)]
           :responses {200 {:body wire.in.subjects/SubjectsHistory}
                       500 {:body s/Str}}
           :handler http.in.subjects/get-history}

     :post {:summary "add an entry in the subjects"
            :interceptors [(interceptors/auth-validate-jwt-interceptor)]
            :parameters {:body wire.in.subjects/SubjectsUpsertEntry}
            :responses {201 {:body wire.in.subjects/SubjectsEntry}
                        400 {:body s/Str}
                        500 {:body s/Str}}
            :handler http.in.subjects/add-entry!}}]

   ["/subjects/:id"
    {:swagger {:tags ["subjects"]}

     :get {:summary "get all subjects transactions by id"
           :interceptors [(interceptors/auth-validate-jwt-interceptor)]
           :parameters {:path wire.in.subjects/SubjectsId}
           :responses {200 {:body wire.in.subjects/SubjectsEntry}
                       500 {:body s/Str}}
           :handler http.in.subjects/get-entry-by-id}

     :put {:summary "update an entry in the subjects"
           :interceptors [(interceptors/auth-validate-jwt-interceptor)]
           :parameters {:path wire.in.subjects/SubjectsId
                        :body wire.in.subjects/SubjectsUpsertEntry}
           :responses {202 {:body s/Str}
                       400 {:body s/Str}
                       500 {:body s/Str}}
           :handler http.in.subjects/update-entry!}

     :delete {:summary "remove an entry in the subjects"
              :interceptors [(interceptors/auth-validate-jwt-interceptor)]
              :parameters {:path wire.in.subjects/SubjectsId}
              :responses {202 {:body s/Str}
                          400 {:body s/Str}
                          500 {:body s/Str}}
              :handler http.in.subjects/remove-entry!}}]

   ["/attending"
    {:swagger {:tags ["attending"]}

     :get {:summary "get all attending transactions"
           :interceptors [(interceptors/auth-validate-jwt-interceptor)]
           :responses {200 {:body wire.in.attending/AttendingHistory}
                       500 {:body s/Str}}
           :handler http.in.attending/get-history}

     :post {:summary "add an entry in the attending"
            :interceptors [(interceptors/auth-validate-jwt-interceptor)]
            :parameters {:body wire.in.attending/AttendingUpsertEntry}
            :responses {201 {:body wire.in.attending/AttendingEntry}
                        400 {:body s/Str}
                        500 {:body s/Str}}
            :handler http.in.attending/add-entry!}}]

   ["/attending/:id"
    {:swagger {:tags ["attending"]}

     :get {:summary "get all attending transactions by id"
           :interceptors [(interceptors/auth-validate-jwt-interceptor)]
           :parameters {:path wire.in.attending/AttendingId}
           :responses {200 {:body wire.in.attending/AttendingEntry}
                       500 {:body s/Str}}
           :handler http.in.attending/get-entry-by-id}

     :put {:summary "update an entry in the attending"
           :interceptors [(interceptors/auth-validate-jwt-interceptor)]
           :parameters {:path wire.in.attending/AttendingId
                        :body wire.in.attending/AttendingUpsertEntry}
           :responses {202 {:body s/Str}
                       400 {:body s/Str}
                       500 {:body s/Str}}
           :handler http.in.attending/update-entry!}

     :delete {:summary "remove an entry in the attending"
              :interceptors [(interceptors/auth-validate-jwt-interceptor)]
              :parameters {:path wire.in.attending/AttendingId}
              :responses {202 {:body s/Str}
                          400 {:body s/Str}
                          500 {:body s/Str}}
              :handler http.in.attending/remove-entry!}}]])
