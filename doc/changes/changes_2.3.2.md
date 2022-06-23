# Cloud Storage Extension 2.3.2, released 2022-??-??

Code name:

## Refactorings

* #191: Enabled Java 17 javadoc CI checks

## Bugfixes

* #207: Upgrade dependencies to fix vulnerabilities

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:import-export-udf-common-scala_2.13:0.2.0` to `0.3.1`
* Updated `com.exasol:parquet-io-java:1.3.0` to `1.3.1`
* Updated `com.fasterxml.jackson.core:jackson-databind:2.13.2.2` to `2.13.3`
* Updated `com.fasterxml.jackson.module:jackson-module-scala_2.13:2.13.2` to `2.13.3`
* Added `com.google.oauth-client:google-oauth-client:1.34.1`
* Updated `com.google.protobuf:protobuf-java:3.20.0` to `3.21.1`
* Updated `com.typesafe.scala-logging:scala-logging_2.13:3.9.4` to `3.9.5`
* Updated `io.delta:delta-core_2.13:1.1.0` to `1.2.1`
* Updated `io.grpc:grpc-netty:1.45.1` to `1.47.0`
* Updated `io.netty:netty-all:4.1.75.Final` to `4.1.78.Final`
* Updated `org.alluxio:alluxio-core-client-hdfs:2.7.4` to `2.8.0-2`
* Updated `org.apache.hadoop:hadoop-aws:3.3.2` to `3.3.3`
* Updated `org.apache.hadoop:hadoop-azure-datalake:3.3.2` to `3.3.3`
* Updated `org.apache.hadoop:hadoop-azure:3.3.2` to `3.3.3`
* Updated `org.apache.hadoop:hadoop-common:3.3.2` to `3.3.3`
* Updated `org.apache.hadoop:hadoop-hdfs:3.3.2` to `3.3.3`
* Updated `org.apache.orc:orc-core:1.7.3` to `1.7.5`
* Updated `org.apache.spark:spark-sql_2.13:3.2.1` to `3.3.0`

### Test Dependency Updates

* Updated `com.dimafeng:testcontainers-scala-scalatest_2.13:0.40.5` to `0.40.8`
* Updated `com.exasol:exasol-testcontainers:6.1.1` to `6.1.2`
* Updated `com.exasol:test-db-builder-java:3.3.1` to `3.3.3`
* Updated `org.mockito:mockito-core:4.4.0` to `4.6.1`
* Updated `org.testcontainers:localstack:1.16.3` to `1.17.2`

### Plugin Dependency Updates

* Updated `com.diffplug.spotless:spotless-maven-plugin:2.22.0` to `2.22.8`
* Updated `com.exasol:artifact-reference-checker-maven-plugin:0.4.1` to `0.4.0`
* Updated `com.exasol:project-keeper-maven-plugin:1.3.4` to `2.5.0`
* Updated `net.alchim31.maven:scala-maven-plugin:4.6.1` to `4.6.3`
* Updated `org.apache.maven.plugins:maven-clean-plugin:3.2.0` to `2.5`
* Added `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M5`
* Removed `org.apache.maven.plugins:maven-gpg-plugin:3.0.1`
* Updated `org.apache.maven.plugins:maven-install-plugin:2.5.2` to `2.4`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.3.2` to `3.4.0`
* Updated `org.apache.maven.plugins:maven-resources-plugin:3.2.0` to `2.6`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.11.0` to `3.3`
* Removed `org.apache.maven.plugins:maven-source-plugin:3.2.1`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:2.12.4` to `3.0.0-M5`
* Added `org.codehaus.mojo:flatten-maven-plugin:1.2.7`
* Added `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184`
