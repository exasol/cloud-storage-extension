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
  Storage][gcs], [Azure Blob Storage][azure-blob] and [Azure Data Lake (Gen1)
  Storage][azure-data-lake].
* Import Apache Avro formatted data from Apache Kafka clusters.

## Documentation

For more information please check out the following guides.

* [User Guide](docs/user_guide.md)
  - [Cloud Storage Systems](docs/storage/cloud_storages.md)
  - [Apache Kafka Import](docs/kafka/import.md)
* [Deployment Guide](docs/deployment_guide.md)
* [Developer Guide](docs/developer_guide.md)

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for contribution guidelines.

For requesting a feature, providing a feedback or reporting an issue, please
open a [Github issue][gh-issues].

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
[apache-kafka]: https://kafka.apache.org/
[avro]: https://avro.apache.org/
[orc]: https://orc.apache.org/
[parquet]: https://parquet.apache.org/
