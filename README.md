# Exasol Cloud Storage Extension

<img alt="cloud-storage-extension logo" src="doc/images/cloud-storage-etl-udfs_128x128.png" style="float:left; padding:0px 10px 10px 10px;"/>

[![Build Status](https://github.com/exasol/cloud-storage-extension/actions/workflows/ci-build.yml/badge.svg)](https://github.com/exasol/cloud-storage-extension/actions/workflows/ci-build.yml)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Acloud-storage-extension&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.exasol%3Acloud-storage-extension)

[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Acloud-storage-extension&metric=security_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Acloud-storage-extension)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Acloud-storage-extension&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Acloud-storage-extension)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Acloud-storage-extension&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Acloud-storage-extension)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Acloud-storage-extension&metric=sqale_index)](https://sonarcloud.io/dashboard?id=com.exasol%3Acloud-storage-extension)

[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Acloud-storage-extension&metric=code_smells)](https://sonarcloud.io/dashboard?id=com.exasol%3Acloud-storage-extension)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Acloud-storage-extension&metric=coverage)](https://sonarcloud.io/dashboard?id=com.exasol%3Acloud-storage-extension)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Acloud-storage-extension&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=com.exasol%3Acloud-storage-extension)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Acloud-storage-extension&metric=ncloc)](https://sonarcloud.io/dashboard?id=com.exasol%3Acloud-storage-extension)

## Overview

Exasol Cloud Storage Extension provides [Exasol][exasol] user-defined functions (UDFs) for accessing formatted data stored in public cloud storage systems.

## Features

* Imports formatted data from public cloud storage systems.
* Supports the following data formats for importing: [Apache Avro][avro], [Apache Orc][orc] and [Apache Parquet][parquet].
* Allows data import from [Delta Lake](https://delta.io/).
* Supports table export as Apache Parquet format to public cloud storage systems.
* Supports the following cloud storage systems: [Amazon S3][s3], [Google Cloud Storage][gcs], [Azure Blob Storage][azure-blob], [Azure Data Lake (Gen1) Storage][azure-data-lake] and [Azure Data Lake (Gen2) Storage][azure-data-lake-gen2].
* Supports [Hadoop Distributed Filesystem (HDFS)][hdfs] and [Alluxio][alluxio-overview-link] filesystems.
* Allows configuration of parallel importer or exporter processes.

## Information for Users

For more information please check out the following guides.

* [User Guide](doc/user_guide/user_guide.md)
* [Changelog](doc/changes/changelog.md)

## Information for Contributors

* [General Developer Guide for Import-Export UDF][developer-guide]
* [Project Specific Developer Guide](doc/developers_guide/developers_guide.md)
* [Dependencies](dependencies.md)

[exasol]: https://www.exasol.com/en/
[avro]: https://avro.apache.org/
[orc]: https://orc.apache.org/
[parquet]: https://parquet.apache.org/
[s3]: https://aws.amazon.com/s3/
[gcs]: https://cloud.google.com/storage/
[azure-blob]: https://azure.microsoft.com/en-us/services/storage/blobs/
[azure-data-lake]: https://azure.microsoft.com/en-us/solutions/data-lake/
[azure-data-lake-gen2]: https://azure.microsoft.com/en-us/services/storage/data-lake-storage/
[hdfs]: https://hadoop.apache.org/docs/r1.2.1/hdfs_design.html
[alluxio-overview-link]: https://docs.alluxio.io/os/user/stable/en/Overview.html
[developer-guide]: https://github.com/exasol/import-export-udf-common-scala/blob/master/doc/development/developer_guide.md
