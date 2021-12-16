# Cloud Storage Extension 2.1.0, released 2021-12-16

Code name: Fixed Log4J Vulnerability

## Summary

In this release we updated dependencies and fixed Log4J `CVE-2021-44228` vulnerability.

## Bug Fixes

* #175: Updated dependencies and fixed log4j vulnerability

## Dependency Updates

### Compile Dependency Updates

* Added `io.netty:netty-all:4.1.72.Final`
* Added `org.apache.avro:avro:1.11.0`
* Added `com.fasterxml.jackson.core:jackson-databind:2.13.0`
* Added `com.fasterxml.jackson.module:jackson-module-scala_2.13:2.13.0`
* Added `org.slf4j:jul-to-slf4j:1.7.32`
* Added `org.apache.logging.log4j:log4j-api:2.16.0`
* Added `org.apache.logging.log4j:log4j-1.2-api:2.16.0`
* Updated `io.grpc:grpc-netty:1.41.0` to `1.43.0`
* Updated `com.google.guava:guava:30.0.1-jre` to `31.0.1-jre`
* Updated `com.exasol:error-reporting-java:0.4.1`
* Updated `com.exasol:parquet-io-java:1.1.0` to `1.2.1`
* Updated `org.apache.orc:orc-core:1.7.0` to `1.7.1`
* Updated `org.alluxio:alluxio-core-client-hdfs:2.6.2` to `2.7.1`
* Updated `io.delta:delta-core_2.13:0.7.0` to `1.1.0`
* Updated `org.apache.spark:spark-sql_2.13:3.0.1` to `3.2.0`

### Test Dependency Updates

* Updated `org.mockito:mockito-core:3.12.4` to `4.1.0`
* Updated `com.exasol:hamcrest-resultset-matcher:1.4.1` to `1.5.1`
* Updated `com.exasol:exasol-testcontainers:5.0.0` to `5.1.1`
* Updated `com.dimafeng:testcontainers-scala-scalatest:0.39.8` to `0.39.12`
* Updated `org.testcontainers:localstack:0.16.0` to `0.16.2`

### Plugin Updates

* Updated `org.scalameta:sbt-scalafmt:2.4.3` to `2.4.5`
* Updated `org.scoverage:sbt-scoverage:1.9.0` to `1.9.2`
* Updated `org.wartremover:sbt-wartremover-contrib:1.3.12` to `1.3.13`
