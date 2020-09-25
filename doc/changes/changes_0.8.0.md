# Cloud Storage ETL UDFs v0.8.0, released 2020-09-09

## Summary

This is **major** release step in separating the different modules into separate
repositories. After this release,
[`kafka-connector-extension`](https://github.com/exasol/kafka-connector-extension)
and
[`kinesis-connector-extension`](https://github.com/exasol/kinesis-connector-extension)
will have their own separate repositories. And this repository will be renamed
to `cloud-storage-extension`.

This allows quick release cycle and better visibility of integration connectors
for our users and customers.

In this release we have added support for nested data structures in Kinesis
connector, added support for connection object in Kafka connector.

## Features

* [#84](https://github.com/exasol/cloud-storage-extension/issues/84): Changed to multi module setup repository (PR [#85](https://github.com/exasol/cloud-storage-extension/pull/85)).
* [#87](https://github.com/exasol/cloud-storage-extension/issues/87): Added nested data support for Kinesis connector (PR [#88](https://github.com/exasol/cloud-storage-extension/pull/88)).
* Added support for connection object in Kafka connector (PR [#90](https://github.com/exasol/cloud-storage-extension/pull/90)).
* Added checks for keystore and truststore files in Kafka connector (PR [#92](https://github.com/exasol/cloud-storage-extension/pull/92)).

## Dependency Updates

#### Runtime Dependency Updates

* Updated ``org.apache.hadoop:hadoop-aws`` from `3.2.1` to `3.3.0`.
* Updated ``org.apache.hadoop:hadoop-azure`` from `3.2.1` to `3.3.0`.
* Updated ``org.apache.hadoop:hadoop-azure-datalake`` from `3.2.1` to `3.3.0`.
* Updated ``org.apache.hadoop:hadoop-client`` from `3.2.1` to `3.3.0`.
* Updated ``org.apache.parquet:parquet-hadoop`` from `1.10.1` to `1.11.1`.
* Updated ``org.scala-lang:scala-library`` from `2.12.10` to `2.12.12`.
* Updated ``org.apache.avro:avro`` from `1.9.2` to `1.10.0`.
* Updated ``org.apache.orc:orc-core`` from `1.6.2` to `1.6.3`.
* Updated ``io.delta:delta-core`` from `0.5.0` to `0.7.0`.
* Updated ``org.apache.spark:spark-sql`` from `2.4.5` to `3.0.0`.
* Updated ``org.apache.kafka:kafka-clients`` from `2.4.0` to `2.5.0`.
* Updated ``io.confluent:kafka-avro-serializer`` from `5.4.0` to `5.5.1`.

#### Test Dependency Updates

* Updated ``com.exasol:exasol-testcontainers`` from `2.0.0` to `2.1.0`.
* Updated ``org.testcontainers:localstack`` from `1.13.0` to `1.14.3`.
* Updated ``org.scalatest:scalatest`` from `3.1.0` to `3.2.2`.
* Updated ``org.mockito:mockito-core`` from `3.2.4` to `3.5.10`.
* Updated ``com.fasterxml.jackson.core:jackson-core`` from `2.6.7` to `2.11.2`.
* Updated ``com.fasterxml.jackson.core:jackson-databind`` from `2.6.7.3` to `2.11.2`.
* Updated ``com.fasterxml.jackson.core:jackson-annotations`` from `2.6.7` to `2.11.2`.
* Updated ``com.fasterxml.jackson.module:"jackson-module-scala`` from `2.6.7.1` to `2.11.2`.

#### Plugin Updates

* Updated ``sbt.version`` from `1.3.7` to `1.3.13`.
* Updated ``org.wartremover:sbt-wartremover`` from `2.4.3` to `2.4.10`.
* Updated ``org.wartremover:sbt-wartremover-contrib`` from `1.3.2` to `1.3.8`.
* Updated ``com.eed3si9n:sbt-assembly`` from `0.14.10` to `0.15.0`.
* Updated ``com.timushev.sbt:sbt-updates`` from `0.5.0` to `0.5.1`.
* Updated ``com.github.cb372:sbt-explicit-dependencies`` from `0.2.12` to `0.2.13`.
