# Cloud Storage Extension 0.9.1, released 2020-MM-DD

## Features / Enhancements

* #106: Updated parquet-hadoop version that includes api changes (PR #107).
* #108: Added dictionary aware Parquet decoders (PR #109).

## Documentation

* #89: Increased the default number of characters for file path (PR #105).

## Dependency Updates

### Runtime Dependency Updates

* Updated `org.apache.orc:orc-core` from `1.6.4` to `1.6.5`.
* Updated `org.apache.parquet:parquet-hadoop` from `1.10.1` to `1.11.1`.
* Updated `com.exasol:import-export-udf-common-scala` from `0.1.0` to `0.2.0`.
* Removed `com.exasol:exasol-script-api`
* Removed `com.typesafe.scala-logging:scala-logging`
* Removed `com.fasterxml.jackson.core:jackson-core`
* Removed `com.fasterxml.jackson.core:jackson-databind`
* Removed `com.fasterxml.jackson.core:jackson-annotations`
* Removed `com.fasterxml.jackson.module:"jackson-module-scala`
* Removed libraries are included in `import-export-udf-common-scala` dependency.

### Test Dependency Updates

* Updates `org.scalatest:scalatest` from `3.2.2` to `3.2.3`.
* Updated `org.mockito:mockito-core` from `3.5.13` to `3.6.0`.

### Plugin Updates

* Updated `org.wartremover:sbt-wartremover` from `2.4.10` to `2.4.13`.
* Updated `org.wartremover:sbt-wartremover-contrib` from `1.3.8` to `1.3.11`.
* Updated `com.github.cb372:sbt-explicit-dependencies` from `0.2.13` to `0.2.15`.
