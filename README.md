# Exasol Cloud Storage Extension

<img alt="cloud-storage-extension logo" src="doc/images/cloud-storage-etl-udfs_128x128.png" style="float:left; padding:0px 10px 10px 10px;"/>

[![Build Status][travis-badge]][travis-link]
[![Coveralls][coveralls-badge]][coveralls-link]
[![GitHub Release][gh-release-badge]][gh-release-link]

## Overview

Exasol Cloud Storage Extension provides [Exasol][exasol] user-defined functions
(UDFs) for accessing formatted data stored in public cloud storage systems.

## Features

* Imports formatted data from public cloud storage systems.
* Supports the following data formats for importing: [Apache Avro][avro],
  [Apache Orc][orc] and [Apache Parquet][parquet].
* Allows data import from [Delta Lake](https://delta.io/).
* Supports table export as Apache Parquet format to public cloud storage
  systems.
* Supports the following cloud storage systems: [Amazon S3][s3], [Google Cloud
  Storage][gcs], [Azure Blob Storage][azure-blob], [Azure Data Lake (Gen1)
  Storage][azure-data-lake] and [Azure Data Lake (Gen2)
  Storage][azure-data-lake-gen2].
* Allows configuration of parallel importer or exporter processes.

## Information for Users

For more information please check out the following guides.

* [User Guide](doc/user_guide/user_guide.md)
* [Changelog](doc/changes/changelog.md)

## Information for Contributors

* [Developer Guide][developer-guide]

## Dependencies

The following sections list all the dependencies that are required for
compiling, testing and running the project.

We compile and build the `exasol-cloud-storage-extension` releases using Java 8;
however, it should be safe to run it on the newer JVM versions. This is also
[recommended way][jdk-compatibility] to build the Scala code.

### Runtime Dependencies

| Dependency                                  | Purpose                                                         | License            |
|---------------------------------------------|-----------------------------------------------------------------|--------------------|
| [Exasol Import Export UDF Common][ieudf]    | Common Import Export Library for Scala UDFs                     | MIT License        |
| [Apache Orc][orc]                           | Integration support for Orc format                              | Apache License 2.0 |
| [Apache Parquet][parquet]                   | Integration support for Parquet format                          | Apache License 2.0 |
| [Hadoop AWS][hadoop-aws-link]               | Access for Amazon S3 object store and compatible implementations| Apache License 2.0 |
| [Hadoop Azure][hadoop-azr-link]             | Access support for Azure Blob Storage                           | Apache License 2.0 |
| [Hadoop Azure Datalake][hadoop-azrlake-link]| Access support for Azure Data Lake Store                        | Apache License 2.0 |
| [Hadoop Client][hadoop-client-link]         | Apache Hadoop common dependencies as configuration or filesystem| Apache License 2.0 |
| [Google Cloud Storage][gcs-connectors-link] | Access support for Google Cloud Storage                         | Apache License 2.0 |
| [Delta Lake Core][delta-io]                 | Integration support for Delta Lake format                       | Apache License 2.0 |
| [Apache Spark SQL][spark-sql]               | Access support for Delta Lake formatted files                   | Apache License 2.0 |

### Test Dependencies

| Dependency                                  | Purpose                                                         | License            |
|---------------------------------------------|-----------------------------------------------------------------|--------------------|
| [Scalatest][scalatest-link]                 | A testing tool for Scala and Java developers                    | Apache License 2.0 |
| [Scalatest Plus][scalatestplus-link]        | An integration support between Scalatest and Mockito            | Apache License 2.0 |
| [Mockito Core][mockitocore-link]            | A mocking framework for unit tests                              | MIT License        |
| [Java Hamcrest][hamcrest-link]              | Matchers to create the flexible expressions of intent           | BSD License        |
| [Hamcrest Resultset Matcher][exars-link]    | Matchers for JDBC ResultSet objects                             | MIT License        |
| [Exasol Test Database Builder][tddb-link]   | Library to create and clean up database for integration tests   | MIT License        |
| [Exasol Testcontainers][exacontainers-link] | Test container for Exasol on Docker                             | MIT License        |
| [Localstack Testcontainers][localstack-link]| A local docker AWS cloud containers                             | MIT License        |

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

[travis-badge]: https://img.shields.io/travis/exasol/cloud-storage-extension/master.svg?logo=travis
[travis-link]: https://travis-ci.com/exasol/cloud-storage-extension
[coveralls-badge]: https://img.shields.io/coveralls/exasol/cloud-storage-extension.svg
[coveralls-link]: https://coveralls.io/github/exasol/cloud-storage-extension
[gh-release-badge]: https://img.shields.io/github/release/exasol/cloud-storage-extension.svg?logo=github
[gh-release-link]: https://github.com/exasol/cloud-storage-extension/releases/latest
[exasol]: https://www.exasol.com/en/
[ieudf]: https://github.com/exasol/import-export-udf-common-scala
[orc]: https://orc.apache.org/
[parquet]: https://parquet.apache.org/
[delta-io]: https://delta.io/
[spark-sql]: https://spark.apache.org/sql/
[s3]: https://aws.amazon.com/s3/
[gcs]: https://cloud.google.com/storage/
[azure-blob]: https://azure.microsoft.com/en-us/services/storage/blobs/
[azure-data-lake]: https://azure.microsoft.com/en-us/solutions/data-lake/
[azure-data-lake-gen2]: https://azure.microsoft.com/en-us/services/storage/data-lake-storage/
[hadoop-aws-link]: https://hadoop.apache.org/docs/current/hadoop-aws/tools/hadoop-aws/index.html
[hadoop-azr-link]: https://hadoop.apache.org/docs/current/hadoop-azure/index.html
[hadoop-azrlake-link]: https://hadoop.apache.org/docs/current/hadoop-azure-datalake/index.html
[hadoop-client-link]: https://github.com/apache/hadoop/tree/trunk/hadoop-client-modules
[gcs-connectors-link]: https://cloud.google.com/dataproc/docs/concepts/connectors/cloud-storage
[jdk-compatibility]: https://docs.scala-lang.org/overviews/jdk-compatibility/overview.html#running-versus-compiling
[scalatest-link]: http://www.scalatest.org/
[scalatestplus-link]: https://github.com/scalatest/scalatestplus-mockito
[mockitocore-link]: https://site.mockito.org/
[hamcrest-link]: http://hamcrest.org/JavaHamcrest/
[exars-link]: https://github.com/exasol/hamcrest-resultset-matcher
[tddb-link]: https://github.com/exasol/test-db-builder-java
[exacontainers-link]: https://github.com/exasol/exasol-testcontainers
[localstack-link]: https://www.testcontainers.org/modules/localstack
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
[developer-guide]: https://github.com/exasol/import-export-udf-common-scala/blob/master/doc/development/developer_guide.md
