# Cloud Storage Extension 2.7.2, released 2023-07-05

Code name: Documentation and dependency updates

## Summary

With this release we updated the user guide documentation and upgraded dependency versions. We also fixed vulnerability findings in the transitive dependencies and updated their versions.

## Documentation

* #243: Removed target file name from `curl` commands in user guide
* #250: Added `S3_ENDPOINT_REGION` parameter description to user guide

## Security

* #245: Fixed dependency check vulnerability findings
* #247: Fixed dependency check vulnerability findings
* #253: Fixed dependency check vulnerability findings

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:import-export-udf-common-scala_2.13:1.1.0` to `1.1.1`
* Updated `com.exasol:parquet-io-java:2.0.1` to `2.0.4`
* Removed `com.fasterxml.jackson.core:jackson-databind:2.14.2`
* Removed `com.fasterxml.jackson.module:jackson-module-scala_2.13:2.14.2`
* Removed `com.fasterxml.woodstox:woodstox-core:6.5.0`
* Updated `com.google.guava:guava:31.1-jre` to `32.1.1-jre`
* Updated `com.google.protobuf:protobuf-java:3.22.2` to `3.23.3`
* Updated `io.delta:delta-core_2.13:2.2.0` to `2.4.0`
* Added `io.dropwizard.metrics:metrics-core:4.2.19`
* Updated `io.grpc:grpc-netty:1.53.0` to `1.56.1`
* Removed `io.netty:netty-all:4.1.90.Final`
* Added `io.netty:netty-handler:4.1.94.Final`
* Updated `org.alluxio:alluxio-core-client-hdfs:2.9.2` to `300`
* Updated `org.apache.avro:avro:1.11.1` to `1.11.2`
* Removed `org.apache.commons:commons-text:1.10.0`
* Updated `org.apache.hadoop:hadoop-aws:3.3.4` to `3.3.6`
* Updated `org.apache.hadoop:hadoop-azure-datalake:3.3.4` to `3.3.6`
* Updated `org.apache.hadoop:hadoop-azure:3.3.4` to `3.3.6`
* Updated `org.apache.hadoop:hadoop-common:3.3.4` to `3.3.6`
* Updated `org.apache.hadoop:hadoop-hdfs-client:3.3.4` to `3.3.6`
* Updated `org.apache.hadoop:hadoop-hdfs:3.3.4` to `3.3.6`
* Removed `org.apache.ivy:ivy:2.5.1`
* Updated `org.apache.orc:orc-core:1.8.3` to `1.9.0`
* Updated `org.apache.spark:spark-sql_2.13:3.3.2` to `3.4.1`
* Removed `org.codehaus.jackson:jackson-mapper-asl:1.9.13`
* Updated `org.scala-lang:scala-library:2.13.10` to `2.13.11`
* Updated `org.slf4j:jul-to-slf4j:2.0.6` to `2.0.7`
* Updated `org.slf4j:slf4j-log4j12:2.0.6` to `2.0.7`
* Added `org.xerial.snappy:snappy-java:1.1.10.1`

### Test Dependency Updates

* Updated `com.dimafeng:testcontainers-scala-scalatest_2.13:0.40.12` to `0.40.17`
* Updated `com.exasol:exasol-testcontainers:6.5.1` to `6.6.0`
* Updated `com.exasol:extension-manager-integration-test-java:0.2.2` to `0.4.0`
* Updated `com.exasol:hamcrest-resultset-matcher:1.5.2` to `1.6.0`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.14.1` to `3.14.3`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.9.2` to `5.10.0`
* Updated `org.mockito:mockito-core:5.2.0` to `5.4.0`
* Updated `org.scalatestplus:scalatestplus-mockito_2.13:1.0.0-M2` to `1.0.0-SNAP5`
* Updated `org.scalatest:scalatest_2.13:3.2.10` to `3.3.0-SNAP4`
* Updated `org.testcontainers:localstack:1.17.6` to `1.18.3`

### Plugin Dependency Updates

* Updated `com.diffplug.spotless:spotless-maven-plugin:2.35.0` to `2.38.0`
* Updated `com.exasol:error-code-crawler-maven-plugin:1.2.2` to `1.3.0`
* Updated `com.exasol:project-keeper-maven-plugin:2.9.4` to `2.9.10`
* Updated `org.apache.maven.plugins:maven-assembly-plugin:3.5.0` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-clean-plugin:3.2.0` to `3.3.1`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.10.1` to `3.11.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.2.1` to `3.3.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M8` to `3.1.2`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M8` to `3.1.2`
* Added `org.basepom.maven:duplicate-finder-maven-plugin:2.0.1`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.3.0` to `1.5.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.14.2` to `2.16.0`
* Updated `org.itsallcode:openfasttrace-maven-plugin:1.6.1` to `1.6.2`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.8` to `0.8.10`
