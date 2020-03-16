# Exasol Public Cloud Storage ETL UDFs

<img alt="cloud-storage-etl-udfs logo" src="docs/images/cloud-storage-etl-udfs_128x128.png" style="float:left; padding:0px 10px 10px 10px;"/>

[![Build Status][travis-badge]][travis-link]
[![Codecov][codecov-badge]][codecov-link]
[![Coveralls][coveralls-badge]][coveralls-link]
[![GitHub Latest Release][gh-release-badge]][gh-release-link]

<p style="border: 1px solid black;padding: 10px; background-color: #FFFFCC;">
<span style="font-size:200%">&#128712;</span> Please note that this is an open
source project which is officially supported by Exasol. For any question, you
can contact our support team.
</p>

## Overview

This repository contains helper code to create [Exasol][exasol] user defined
functions (UDFs) in order to read from and write to public cloud storage
systems.

Additionally, it provides UDF scripts to import data from [Apache
Kafka][apache-kafka] clusters.

## Features

* Import formatted data from public cloud storage systems.
* Following data formats are supported as source file format when importing:
  [Apache Avro][avro], [Apache Orc][orc] and [Apache Parquet][parquet].
* Export Exasol table data to public cloud storage systems.
* Following data formats are supported as sink file format when exporting:
  [Apache Parquet][parquet].
* Following cloud storage systems are supported: [Amazon S3][s3], [Google Cloud
  Storage][gcs], [Azure Blob Storage][azure-blob], [Azure Data Lake (Gen1)
  Storage][azure-data-lake] and [Azure Data Lake (Gen2)
  Storage][azure-data-lake-gen2].
* Import Apache Avro formatted data from Apache Kafka clusters.

## Documentation

For more information please check out the following guides.

* [User Guide](docs/user_guide.md)
  - [Cloud Storage Systems](docs/storage/cloud_storages.md)
  - [Delta Format Import](docs/storage/delta_format.md)
  - [Apache Kafka Import](docs/kafka/import.md)
* [Deployment Guide](docs/deployment_guide.md)
* [Developer Guide](docs/developer_guide.md)

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for contribution guidelines.

For requesting a feature, providing a feedback or reporting an issue, please
open a [Github issue][gh-issues].

## Dependencies

The following sections list all the dependencies that are required for
compiling, testing and running the project.

We compile and build the `cloud-storage-etl-udfs` releases using Java 8;
however, it should be safe to run it on the newer JVM versions. This is also
[recommendend way][jdk-compatibility] to build the Scala code.

### Runtime Dependencies

| Dependency                                  | Purpose                                                         | License            |
|---------------------------------------------|-----------------------------------------------------------------|--------------------|
| [Exasol Script API][exasol-script-api-link] | Accessing Exasol IMPORT / EXPORT API                            | MIT License        |
| [Hadoop AWS][hadoop-aws-link]               | Access for Amazon S3 object store and compatible implementations| Apache License 2.0 |
| [Hadoop Azure][hadoop-azr-link]             | Access support for Azure Blob Storage                           | Apache License 2.0 |
| [Hadoop Azure Datalake][hadoop-azrlake-link]| Access support for Azure Data Lake Store                        | Apache License 2.0 |
| [Hadoop Client][hadoop-client-link]         | Apache Hadoop common dependencies as configuration or filesystem| Apache License 2.0 |
| [Google Cloud Storage][gcs-connectors-link] | Access support for Google Cloud Storage                         | Apache License 2.0 |
| [Apache Avro][apache-avro-link]             | Integration support for Avro format                             | Apache License 2.0 |
| [Apache Orc][apache-orc-link]               | Integration support for Orc format                              | Apache License 2.0 |
| [Apache Parquet][apache-parquet-link]       | Integration support for Parquet format                          | Apache License 2.0 |
| [Apache Kafka Clients][kafka-clients-link]  | An Apache Kafka client support for Java / Scala                 | Apache License 2.0 |
| [Kafka Avro Serializer][kafka-avro-link]    | Support for serializing / deserializing Avro formats with Kafka | Apache License 2.0 |
| [SLF4J API][slf4j-link]                     | A simple logging facade for Java (SLF4J)                        | MIT License        |
| [Scala Logging Library][scala-logging-link] | Scala logging library wrapping SLF4J                            | Apache License 2.0 |

### Test Dependencies

| Dependency                                  | Purpose                                                         | License            |
|---------------------------------------------|-----------------------------------------------------------------|--------------------|
| [Scalatest][scalatest-link]                 | A testing tool for Scala and Java developers                    | Apache License 2.0 |
| [Scalatest Plus][scalatestplus-link]        | An integration support between Scalatest and Mockito            | Apache License 2.0 |
| [Mockito Core][mockitocore-link]            | A mocking framework for unit tests                              | MIT License        |
| [Embedded Kafka Schema Registry][kafka-link]| An in-memory instances of Kafka and Schema registry for tests   | MIT License        |

### Compiler Plugin Dependencies

These plugins help with project development.

| Plugin Name                                 | Purpose                                                         | License              |
|---------------------------------------------|-----------------------------------------------------------------|----------------------|
| [SBT Coursier][sbt-coursier-link]           | Pure Scala artifact fetching                                    | Apache License 2.0   |
| [SBT Wartremover][sbt-wartremover-link]     | Flexible Scala code linting tool                                | Apache License 2.0   |
| [SBT Wartremover Contrib][sbt-wcontrib-link]| Community managed additional warts for wartremover              | Apache License 2.0   |
| [SBT Assembly][sbt-assembly-link]           | Create fat jars with all project dependencies                   | MIT License          |
| [SBT API Mappings][sbt-apimapping-link]     | A plugin that fetches API mappings for common Scala libraries   | Apache License 2.0   |
| [SBT Scoverage][sbt-scoverage-link]         | Integrates the scoverage code coverage library                  | Apache License 2.0   |
| [SBT Coveralls][sbt-coveralls-link]         | Uploads scala code coverage results to https://coveralls.io     | Apache License 2.0   |
| [SBT Updates][sbt-updates-link]             | Checks Maven and Ivy repositories for dependency updates        | BSD 3-Clause License |
| [SBT Scalafmt][sbt-scalafmt-link]           | A plugin for https://scalameta.org/scalafmt/ formatting         | Apache License 2.0   |
| [SBT Scalastyle][sbt-style-link]            | A plugin for http://www.scalastyle.org/ Scala style checker     | Apache License 2.0   |
| [SBT Dependency Graph][sbt-depgraph-link]   | A plugin for visualizing dependency graph of your project       | Apache License 2.0   |
| [SBT Explicit Dependencies][sbt-expdep-link]| Checks which direct libraries required to compile your code     | Apache License 2.0   |
| [SBT Git][sbt-git-link]                     | A plugin for Git integration, used to version the release jars  | BSD 2-Clause License |

[travis-badge]: https://img.shields.io/travis/exasol/cloud-storage-etl-udfs/master.svg?logo=travis
[travis-link]: https://travis-ci.org/exasol/cloud-storage-etl-udfs
[codecov-badge]: https://codecov.io/gh/exasol/cloud-storage-etl-udfs/branch/master/graph/badge.svg
[codecov-link]: https://codecov.io/gh/exasol/cloud-storage-etl-udfs
[coveralls-badge]: https://img.shields.io/coveralls/exasol/cloud-storage-etl-udfs.svg
[coveralls-link]: https://coveralls.io/github/exasol/cloud-storage-etl-udfs
[gh-release-badge]: https://img.shields.io/github/release/exasol/cloud-storage-etl-udfs.svg?logo=github
[gh-release-link]: https://github.com/exasol/cloud-storage-etl-udfs/releases/latest
[gh-issues]: https://github.com/exasol/cloud-storage-etl-udfs/issues
[exasol]: https://www.exasol.com/en/
[s3]: https://aws.amazon.com/s3/
[gcs]: https://cloud.google.com/storage/
[azure-blob]: https://azure.microsoft.com/en-us/services/storage/blobs/
[azure-data-lake]: https://azure.microsoft.com/en-us/solutions/data-lake/
[azure-data-lake-gen2]: https://azure.microsoft.com/en-us/services/storage/data-lake-storage/
[apache-kafka]: https://kafka.apache.org/
[avro]: https://avro.apache.org/
[orc]: https://orc.apache.org/
[parquet]: https://parquet.apache.org/
[hadoop-aws-link]: https://hadoop.apache.org/docs/current/hadoop-aws/tools/hadoop-aws/index.html
[hadoop-azr-link]: https://hadoop.apache.org/docs/current/hadoop-azure/index.html
[hadoop-azrlake-link]: https://hadoop.apache.org/docs/current/hadoop-azure-datalake/index.html
[hadoop-client-link]: https://github.com/apache/hadoop/tree/trunk/hadoop-client-modules
[gcs-connectors-link]: https://cloud.google.com/dataproc/docs/concepts/connectors/cloud-storage
[apache-avro-link]: https://avro.apache.org/
[apache-orc-link]: https://orc.apache.org/
[apache-parquet-link]: https://parquet.apache.org/
[kafka-clients-link]: https://github.com/apache/kafka/tree/trunk/clients
[kafka-avro-link]: https://github.com/confluentinc/schema-registry/tree/master/avro-serializer
[slf4j-link]: http://www.slf4j.org/
[scala-logging-link]: https://github.com/lightbend/scala-logging
[jdk-compatibility]: https://docs.scala-lang.org/overviews/jdk-compatibility/overview.html#running-versus-compiling
[exasol-script-api-link]: https://docs.exasol.com/database_concepts/udf_scripts.htm
[scalatest-link]: http://www.scalatest.org/
[scalatestplus-link]: https://github.com/scalatest/scalatestplus-mockito
[mockitocore-link]: https://site.mockito.org/
[kafka-link]: https://github.com/embeddedkafka/embedded-kafka-schema-registry
[sbt-coursier-link]: https://github.com/coursier/coursier
[sbt-wartremover-link]: http://github.com/puffnfresh/wartremover
[sbt-wcontrib-link]: http://github.com/wartremover/wartremover-contrib
[sbt-assembly-link]: https://github.com/sbt/sbt-assembly
[sbt-apimapping-link]: https://github.com/ThoughtWorksInc/sbt-api-mappings
[sbt-scoverage-link]: http://github.com/scoverage/sbt-scoverage
[sbt-coveralls-link]: https://github.com/scoverage/sbt-coveralls
[sbt-updates-link]: http://github.com/rtimush/sbt-updates
[sbt-scalafmt-link]: https://github.com/lucidsoftware/neo-sbt-scalafmt
[sbt-style-link]: https://github.com/scalastyle/scalastyle-sbt-plugin
[sbt-depgraph-link]: https://github.com/jrudolph/sbt-dependency-graph
[sbt-git-link]: https://github.com/sbt/sbt-git
[sbt-expdep-link]: https://github.com/cb372/sbt-explicit-dependencies
