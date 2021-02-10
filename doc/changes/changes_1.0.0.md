# Cloud Storage Extension 1.0.0, released 2021-MM-DD

## Features / Enhancements

* #106: Updated parquet-hadoop version that includes api changes (PR #107).
* #108: Added dictionary aware Parquet decoders (PR #109).
* #11: Added support for importing Parquet complex (LIST, MAP) types (PR #111).
* #115: Added support for importing Orc complex (LIST, STRUCT) types (PR #116).
* #118: Added support for docker based Exasol installations (PR #119).

## Bug Fixes

* #112: Fixed Parquet repeated converter bugs (PR #121)
* #35: Fixed the hidden and metadata files filter bug (PR #122).

## Refactoring

* #117: Added Exasol docker containers for integration tests (PR #119).

## Documentation

* #89: Increased the default number of characters for file path (PR #105).
* #110: Added overview table with supported features (PR #124).

## Dependency Updates

### Runtime Dependency Updates

* Updated `org.apache.orc:orc-core:1.6.4` to `1.6.7`
* Updated `org.apache.parquet:parquet-hadoop:1.10.1` to `1.11.1`
* Updated `com.exasol:import-export-udf-common-scala:0.1.0` to `0.2.0`
* Updated `org.apache.spark:spark-sql:3.0.0` to `3.0.1`
* Removed `com.exasol:exasol-script-api`
* Removed `com.typesafe.scala-logging:scala-logging`
* Removed `com.fasterxml.jackson.core:jackson-core`
* Removed `com.fasterxml.jackson.core:jackson-databind`
* Removed `com.fasterxml.jackson.core:jackson-annotations`
* Removed `com.fasterxml.jackson.module:"jackson-module-scala`
* Removed libraries are included in `import-export-udf-common-scala` dependency

### Test Dependency Updates

* Added `org.hamcrest:hamcrest:2.2`
* Added `com.exasol:hamcrest-resultset-matcher:1.3.0`
* Added `com.exasol:test-db-builder-java:3.0.0`
* Added `com.exasol:exasol-testcontainers:3.4.1`
* Added `org.testcontainers:localstack:1.15.1`
* Updates `org.scalatest:scalatest:3.2.2` to `3.2.3`
* Updated `org.mockito:mockito-core:3.5.13` to `3.7.7`

### Plugin Updates

* Updated SBT `sbtx` wrapper to latest
* Updated `org.wartremover:sbt-wartremover:2.4.10` to `2.4.13`
* Updated `org.wartremover:sbt-wartremover-contrib:1.3.8` to `1.3.11`
* Updated `com.github.cb372:sbt-explicit-dependencies:0.2.13` to `0.2.16`
