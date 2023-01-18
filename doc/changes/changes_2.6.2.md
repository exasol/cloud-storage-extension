# Cloud Storage Extension 2.6.2, released 2023-01-18

Code name: Dependency Upgrade on top of 2.6.1

## Summary

Updated dependencies to remove references to discontinued repository maven.exasol.com.

## Bugfixes

* #232: Updated dependencies

## Dependency Updates

### Compile Dependency Updates

* Updated `com.fasterxml.jackson.core:jackson-databind:2.14.0-rc2` to `2.14.1`
* Updated `com.fasterxml.jackson.module:jackson-module-scala_2.13:2.14.0-rc2` to `2.14.1`
* Updated `com.google.protobuf:protobuf-java:3.21.7` to `3.21.12`
* Updated `io.delta:delta-core_2.13:2.1.0` to `2.2.0`
* Updated `io.grpc:grpc-netty:1.50.0` to `1.52.1`
* Updated `io.netty:netty-all:4.1.84.Final` to `4.1.87.Final`
* Updated `org.alluxio:alluxio-core-client-hdfs:2.8.1` to `2.9.0`
* Updated `org.apache.orc:orc-core:1.8.0` to `1.8.2`
* Updated `org.apache.spark:spark-sql_2.13:3.3.0` to `3.3.1`
* Updated `org.slf4j:jul-to-slf4j:2.0.3` to `2.0.6`
* Updated `org.slf4j:slf4j-log4j12:2.0.3` to `2.0.6`

### Test Dependency Updates

* Updated `com.dimafeng:testcontainers-scala-scalatest_2.13:0.40.11` to `0.40.12`
* Updated `com.exasol:exasol-testcontainers:6.2.0` to `6.5.0`
* Updated `com.exasol:extension-manager-integration-test-java:0.1.0` to `0.2.0`
* Updated `com.exasol:test-db-builder-java:3.4.0` to `3.4.1`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.10.1` to `3.12.3`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.9.1` to `5.9.2`
* Updated `org.mockito:mockito-core:4.8.0` to `5.0.0`
* Updated `org.testcontainers:localstack:1.17.5` to `1.17.6`
