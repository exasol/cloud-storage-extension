# Cloud Storage Extension 0.9.1, released 2020-MM-DD

## Features / Enhancements

* #106: Updated parquet-hadoop version that includes api changes (PR #107).
* #108: Added dictionary aware Parquet decoders (PR #109).
* #11: Added support for importing Parquet complex (LIST, MAP) types (PR #111).
* #115: Added support for importing Orc complex (LIST, STRUCT) types (PR #116).

## Documentation

* #89: Increased the default number of characters for file path (PR #105).

## Dependency Updates

### Runtime Dependency Updates

* Updated to `org.apache.orc:orc-core:1.6.6` (was `1.6.4`)
* Updated to `org.apache.parquet:parquet-hadoop:1.11.1` (was `1.10.1`)
* Updated to `com.exasol:import-export-udf-common-scala:0.2.0` (was `0.1.0`)
* Updated to `org.apache.spark:spark-sql:3.0.1` (was `3.0.0`)
* Removed `com.exasol:exasol-script-api`
* Removed `com.typesafe.scala-logging:scala-logging`
* Removed `com.fasterxml.jackson.core:jackson-core`
* Removed `com.fasterxml.jackson.core:jackson-databind`
* Removed `com.fasterxml.jackson.core:jackson-annotations`
* Removed `com.fasterxml.jackson.module:"jackson-module-scala`
* Removed libraries are included in `import-export-udf-common-scala` dependency

### Test Dependency Updates

* Updates to `org.scalatest:scalatest:3.2.3` (was `3.2.2`)
* Updated to `org.mockito:mockito-core:3.6.28` (was `3.5.13`)

### Plugin Updates

* Updated to `org.wartremover:sbt-wartremover:2.4.13` (was `2.4.10`)
* Updated to `org.wartremover:sbt-wartremover-contrib:1.3.11` (was `1.3.8`)
* Updated to `com.github.cb372:sbt-explicit-dependencies:0.2.16` (was `0.2.13`)
