# Cloud Storage Extension 2.3.1, released 2022-02-??

Code name:

## Summary

## Bug Fixes

* #190: Added S3 bucket validation for bucket path that end with number
* #193: Fixed bug reading delta formatted data

## Refactoring

* #177: Migrated to maven based build

## Dependency Updates

### Compile Dependency Updates

* Added `com.exasol:error-reporting-java:0.4.1`
* Added `com.exasol:import-export-udf-common-scala_2.13:0.2.0`
* Added `com.exasol:parquet-io-java:1.3.0`
* Added `com.fasterxml.jackson.core:jackson-databind:2.13.2.2`
* Added `com.fasterxml.jackson.module:jackson-module-scala_2.13:2.13.2`
* Added `com.google.cloud.bigdataoss:gcs-connector:1.9.4-hadoop3`
* Added `com.google.guava:guava:31.1-jre`
* Added `com.google.protobuf:protobuf-java:3.19.4`
* Added `com.typesafe.scala-logging:scala-logging_2.13:3.9.4`
* Added `io.delta:delta-core_2.13:1.1.0`
* Added `io.grpc:grpc-netty:1.45.1`
* Added `io.netty:netty-all:4.1.75.Final`
* Added `org.alluxio:alluxio-core-client-hdfs:2.7.3`
* Added `org.apache.avro:avro:1.11.0`
* Added `org.apache.commons:commons-lang3:3.12.0`
* Added `org.apache.hadoop:hadoop-aws:3.3.2`
* Added `org.apache.hadoop:hadoop-azure-datalake:3.3.2`
* Added `org.apache.hadoop:hadoop-azure:3.3.2`
* Added `org.apache.hadoop:hadoop-common:3.3.2`
* Added `org.apache.hadoop:hadoop-hdfs:3.3.2`
* Added `org.apache.logging.log4j:log4j-1.2-api:2.17.2`
* Added `org.apache.logging.log4j:log4j-api:2.17.2`
* Added `org.apache.orc:orc-core:1.7.3`
* Added `org.apache.spark:spark-sql_2.13:3.2.1`
* Added `org.scala-lang:scala-library:2.13.8`
* Added `org.slf4j:jul-to-slf4j:1.7.36`
* Added `org.slf4j:slf4j-log4j12:1.7.36`

### Test Dependency Updates

* Added `com.dimafeng:testcontainers-scala-scalatest_2.13:0.40.4`
* Added `com.exasol:exasol-testcontainers:6.1.1`
* Added `com.exasol:hamcrest-resultset-matcher:1.5.1`
* Added `com.exasol:test-db-builder-java:3.3.1`
* Added `org.hamcrest:hamcrest:2.2`
* Added `org.mockito:mockito-core:4.4.0`
* Added `org.scalatestplus:scalatestplus-mockito_2.13:1.0.0-M2`
* Added `org.scalatest:scalatest_2.13:3.2.10`
* Added `org.testcontainers:localstack:1.16.3`

### Plugin Dependency Updates

* Added `com.diffplug.spotless:spotless-maven-plugin:2.22.0`
* Added `com.exasol:artifact-reference-checker-maven-plugin:0.4.1`
* Added `com.exasol:error-code-crawler-maven-plugin:1.1.0`
* Added `com.exasol:project-keeper-maven-plugin:1.3.4`
* Added `io.github.evis:scalafix-maven-plugin_2.13:0.1.4_0.9.33`
* Added `io.github.zlika:reproducible-build-maven-plugin:0.15`
* Added `net.alchim31.maven:scala-maven-plugin:4.6.1`
* Added `org.apache.maven.plugins:maven-assembly-plugin:3.3.0`
* Added `org.apache.maven.plugins:maven-clean-plugin:3.1.0`
* Added `org.apache.maven.plugins:maven-compiler-plugin:3.10.1`
* Added `org.apache.maven.plugins:maven-deploy-plugin:2.7`
* Added `org.apache.maven.plugins:maven-enforcer-plugin:3.0.0`
* Added `org.apache.maven.plugins:maven-gpg-plugin:3.0.1`
* Added `org.apache.maven.plugins:maven-install-plugin:2.5.2`
* Added `org.apache.maven.plugins:maven-jar-plugin:3.2.2`
* Added `org.apache.maven.plugins:maven-javadoc-plugin:3.3.2`
* Added `org.apache.maven.plugins:maven-resources-plugin:3.2.0`
* Added `org.apache.maven.plugins:maven-site-plugin:3.11.0`
* Added `org.apache.maven.plugins:maven-source-plugin:3.2.1`
* Added `org.apache.maven.plugins:maven-surefire-plugin:2.12.4`
* Added `org.codehaus.mojo:versions-maven-plugin:2.10.0`
* Added `org.itsallcode:openfasttrace-maven-plugin:1.5.0`
* Added `org.jacoco:jacoco-maven-plugin:0.8.7`
* Added `org.scalastyle:scalastyle-maven-plugin:1.0.0`
* Added `org.scalatest:scalatest-maven-plugin:2.0.2`
* Added `org.sonatype.ossindex.maven:ossindex-maven-plugin:3.2.0`
