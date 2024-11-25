# financial-module

Clojure microservice implementing: Components, Reitit, Pedestal, Schema, PostgreSQL and Tests

## About this project
 - **financial-module**: With ports and adapters architecture, this project is a simple financial module that allows you to register and manipulate accounts payable (and receivable in the future - if possible).

- [parenthesin/components](https://github.com/parenthesin/components): Helpers and component wrappers to give a foundation to create new services in clojure,
you can find components for database, http, webserver and tools for db migrations.

Verb   | URL                    | Description
------ | ---------------------- | ------------------------------------------------
POST   | /login                 | login with your username and password  
GET    | /accounts-payable      | get all accounts payable
POST   | /accounts-payable      | create a new account payable
GET    | /accounts-payable/{id} | get an account payable by id
PUT    | /accounts-payable/{id} | update an account payable by id
DELETE | /accounts-payable/{id} | delete an account payable by id 

## Repl
To open a nrepl
```bash
clj -M:nrepl
```
To open a nrepl with all test extra-deps on it
```bash
clj -M:test:nrepl
```

## Run Tests
To run unit tests inside `./test/unit`
```bash
clj -M:test :unit
```
To run integration tests inside `./test/integration`
```bash
clj -M:test :integration
```
To run all tests inside `./test`
```bash
clj -M:test
```
To generate a coverage report 
```bash
clj -M:test --plugin kaocha.plugin/cloverage
```

## Lint fix and format

```bash
clj -M:clojure-lsp format
clj -M:clojure-lsp clean-ns
clj -M:clojure-lsp diagnostics
```

## Migrations
To create a new migration with a name
```bash
clj -M:migratus create migration-name
```
To execute all pending migrations
```bash
clj -M:migratus migrate
```
To rollback the latest migration
```bash
clj -M:migratus rollback
```
See [Migratus Usage](https://github.com/yogthos/migratus#usage) for documentation on each command.


## Docker
Start containers with postgres `user: postgres, password: postgres, hostname: db, port: 5432`  
and [pg-admin](http://localhost:5433) `email: pg@pg.cc, password: pg, port: 5433`
```bash
docker-compose -f docker/docker-compose.yml up -d
```
Stop containers
```bash
docker-compose -f docker/docker-compose.yml stop
```

## Running the server
First you need to have the database running, for this you can use the docker command in the step above.

### Repl
You can start a repl open and evaluate the file `src/financial_module/server.clj` and execute following code:
```clojure
(start-system! (build-system-map))
```

### Uberjar
You can generate an uberjar and execute it via java in the terminal:
```bash
# genarate a target/service.jar
clj -T:build uberjar
# execute it via java
java -jar target/service.jar
```

## Features

### System
- [schema](https://github.com/plumatic/schema) Types and Schemas
- [component](https://github.com/stuartsierra/component) System Lifecycle and Dependencies
- [pedestal](https://github.com/pedestal/pedestal) Http Server
- [reitit](https://github.com/metosin/reitit) Http Routes System 
- [clj-http](https://github.com/dakrone/clj-http) Http Client
- [cheshire](https://github.com/dakrone/cheshire) JSON encoding
- [aero](https://github.com/juxt/aero) Configuration file and enviroment variables manager
- [timbre](https://github.com/ptaoussanis/timbre) Logging library
- [next-jdbc](https://github.com/seancorfield/next-jdbc) JDBC-based layer to access databases
- [hikaricp](https://github.com/brettwooldridge/HikariCP) A solid, high-performance, JDBC connection pool at last
- [tools.build](https://github.com/clojure/tools.build) Clojure builds as Clojure programs

### Tests & Checks
- [kaocha](https://github.com/lambdaisland/kaocha) Test runner
- [kaocha-cloverage](https://github.com/lambdaisland/kaocha-cloverage) Kaocha plugin for code coverage reports
- [schema-generators](https://github.com/plumatic/schema-generators) Data generation and generative testing
- [state-flow](https://github.com/nubank/state-flow) Testing framework for integration tests
- [matcher-combinators](https://github.com/nubank/matcher-combinators) Assertions in data structures
- [pg-embedded-clj](https://github.com/Bigsy/pg-embedded-clj) Embedded PostgreSQL for integration tests
- [clojure-lsp](https://github.com/clojure-lsp/clojure-lsp/) Code Format, Namespace Check and Diagnosis

## Directory Structure
```
./
├── .clj-kondo -- clj-kondo configuration and classes
├── .lsp -- clojure-lsp configuration
├── .github
│   └── workflows -- Github workflows folder.
├── docker -- docker and docker-compose files for the database
├── resources -- Application resources assets folder and configuration files.
│   └── migrations -- Current database schemas, synced on service startup.
├── src -- Library source code and headers.
│   └── financial_module -- Source for the service example.
└── test -- Test source code.
    ├── integration -- Integration tests source (uses state-flow).
    │   └── financial_module -- Tests for service example.
    └── unit -- Unity tests source (uses clojure.test).
        └── financial_module -- Tests for service example.
```

## Related

### Official Template
- [parenthesin/microservice-boilerplate](https://github.com/rafaeldelboni/super-dice-roll-clj)

## License
This is free and unencumbered software released into the public domain.  
For more information, please refer to <http://unlicense.org>
