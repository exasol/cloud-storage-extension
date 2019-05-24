# Exasol Public Cloud Storage ETL UDFs

[![Build Status][travis-badge]][travis-link]
[![Codecov][codecov-badge]][codecov-link]
[![GitHub Latest Release][gh-release-badge]][gh-release-link]

<p style="border: 1px solid black;padding: 10px; background-color: #FFFFCC;">
<span style="font-size:200%">&#128712;</span> Please note that this is an open
source project which is officially supported by Exasol. For any question, you
can contact our support team.
</p>

## Table of contents

* [Overview](#overview)
* [A short example](#a-short-example)
* [Features](#features)
* [Configuration](#configuration-parameters)
* [Setup and deployment](#setup-and-deployment)
* [Building from source](#building-from-source)
* [Contributing](#contributing)

## Overview

This repository contains helper code to create [Exasol][exasol] ETL UDFs in
order to read from and write to public cloud storage services such as [AWS
S3][s3], [Google Cloud Storage][gcs] and [Azure Blob Storage][azure].

Please be aware that Exasol already supports natively [loading CSV format from
AWS S3][sol-594]; however, not from Google Cloud Storage and Azure Storage
systems. Additionally, transfering data between Exasol and [Apache
Hive][apache-hive] is supported via [Hadoop ETL UDFs][hadoop-etl-udfs].

## A short example

Here we show an excerpt from a simple example of importing and exporting Parquet
formatted data stored in Amazon S3.

Please see [the full list of all cloud storage providers and guidelines to
configure them](./docs/overview.md).

### Create an Exasol table

We are going to use a `SALES_POSITIONS` Exasol table to import data into or to
export its contents to Amazon S3.

```sql
CREATE SCHEMA RETAIL;
OPEN SCHEMA RETAIL;

DROP TABLE IF EXISTS SALES_POSITIONS;

CREATE TABLE SALES_POSITIONS (
  SALES_ID    INTEGER,
  POSITION_ID SMALLINT,
  ARTICLE_ID  SMALLINT,
  AMOUNT      SMALLINT,
  PRICE       DECIMAL(9,2),
  VOUCHER_ID  SMALLINT,
  CANCELED    BOOLEAN
);
```

### Import from S3

```sql
IMPORT INTO SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH    = 's3a://my-bucket/parquet/import/sales_positions/*'
  DATA_FORMAT    = 'PARQUET'
  S3_ACCESS_KEY  = 'MY_AWS_ACCESS_KEY'
  S3_SECRET_KEY  = 'MY_AWS_SECRET_KEY'
  S3_ENDPOINT    = 's3.MY_REGION.amazonaws.com'
  PARALLELISM    = 'nproc()';
```

### Export to S3

```sql
EXPORT SALES_POSITIONS
INTO SCRIPT ETL.EXPORT_PATH WITH
  BUCKET_PATH    = 's3a://my-bucket/parquet/export/sales_positions/'
  S3_ACCESS_KEY  = 'MY_AWS_ACCESS_KEY'
  S3_SECRET_KEY  = 'MY_AWS_SECRET_KEY'
  S3_ENDPOINT    = 's3.MY_REGION.amazonaws.com'
  PARALLELISM    = 'iproc(), floor(random()*4)';
```

Please change the paths and parameters accordingly.

## Features

The following table shows currently supported features with the latest realese.

<table>
  <tr>
    <th rowspan="2">Storage System / Data Format</th>
    <th colspan="2">Parquet</th>
    <th colspan="2">Avro</th>
    <th colspan="2">Orc</th>
  </tr>
  <tr>
    <th>IMPORT</th>
    <th>EXPORT</th>
    <th>IMPORT</th>
    <th>EXPORT</th>
    <th>IMPORT</th>
    <th>EXPORT</th>
  </tr>
  <tr>
    <td>Amazon S3</td>
    <td rowspan="4" align="center">&#10004;</td>
    <td rowspan="4" align="center">&#10004;</td>
    <td rowspan="4" align="center">&#10004;</td>
    <td rowspan="4" align="center">&#10005;</td>
    <td rowspan="4" align="center">&#10004;</td>
    <td rowspan="4" align="center">&#10005;</td>
  </tr>
  <tr>
    <td>Google Cloud Storage</td>
  </tr>
  <tr>
    <td>Azure Blob Storage</td>
  </tr>
  <tr>
    <td>Azure Data Lake (Gen1) Storage</td>
  </tr>
</table>

## Configuration Parameters

The following configuration parameters should be provided when using the
cloud-storage-etl-udfs.

| Parameter                      | Default        | Description
|:-------------------------------|:---------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------|
|``BUCKET_PATH``                 |*<none>*        |A path to the data bucket. It should start with cloud storage system specific schema, for example `s3a`.                                                  |
|``DATA_FORMAT``                 |``PARQUET``     |The data storage format in the provided path.                                                                                                             |
|``PARALLELISM IN IMPORT``       |``nproc()``     |The number of parallel instances to be started for importing data. *Please multiply this to increase the parallelism*.                                    |
|``PARALLELISM IN EXPORT``       |``iproc()``     |The parallel instances for exporting data. *Add another random number to increase the parallelism per node*. For example, ``iproc(), floor(random()*4)``. |
|``PARQUET_COMPRESSION_CODEC``   |``uncompressed``|The compression codec to use when exporting the data into parquet files. Other options are: `snappy`, `gzip` and `lzo`.                                   |
|``EXPORT_BATCH_SIZE``           |``100000``      |The number of records per file from each vm. For exampl, if a single vm gets `1M` records, it will export ten files with default 100000 records each.     |
|``storage specific parameters`` |*<none>*        |These are parameters for specific cloud storage for authentication purpose.                                                                               |

Please see [the parameters specific for each cloud storage and how to configure
them here](./docs/overview.md).

## Setup and deployment

Please follow the steps described below in order to setup the `IMPORT` and
`EXPORT` UDF scripts.

### Download the file

Download the latest jar file from [releases][jars].

Additionally, you can also build it from the source by following the [build from
source](#building-from-source) guide. This will allow you to use latest commits
that are not released yet.

### Create Exasol Bucket

In order to use the import or export functionality of `cloud-storage-etl-udfs`,
you have to upload the jar to a bucket in the Exasol bucket file system
(BucketFS).

For this overview we are using an example bucket named `bucket1`.

### Upload the JAR file to the bucket

This will allow using the jar in the ETL UDF scripts later on. Before uploading
the jar, please make sure that the BucketFS ports are open.

Here we use the port number `2580` for http.

```bash
curl \
  -X PUT \
  -T path/to/jar/cloud-storage-etl-udfs-{VERSION}.jar \
  http://w:MY-PASSWORD@EXA-NODE-ID:2580/bucket1/cloud-storage-etl-udfs-{VERSION}.jar
```

Please change other required parameters such as `VERSION`, `EXA-NODE-ID`.

### Create ETL UDFs scripts

Run the following SQL commands to create Exasol scripts.

```sql
CREATE SCHEMA ETL;
OPEN SCHEMA ETL;

-- Import related scripts

CREATE OR REPLACE JAVA SET SCRIPT IMPORT_PATH(...) EMITS (...) AS
%scriptclass com.exasol.cloudetl.scriptclasses.ImportPath;
%jar /buckets/bfsdefault/bucket1/cloud-storage-etl-udfs-{VERSION}.jar;
/

CREATE OR REPLACE JAVA SET SCRIPT IMPORT_FILES(...) EMITS (...) AS
%env LD_LIBRARY_PATH=/tmp/;
%scriptclass com.exasol.cloudetl.scriptclasses.ImportFiles;
%jar /buckets/bfsdefault/bucket1/cloud-storage-etl-udfs-{VERSION}.jar;
/

CREATE OR REPLACE JAVA SCALAR SCRIPT IMPORT_METADATA(...)
EMITS (filename VARCHAR(200), partition_index VARCHAR(100)) AS
%scriptclass com.exasol.cloudetl.scriptclasses.ImportMetadata;
%jar /buckets/bfsdefault/bucket1/cloud-storage-etl-udfs-{VERSION}.jar;
/

-- Export related scripts

CREATE OR REPLACE JAVA SET SCRIPT EXPORT_PATH(...) EMITS (...) AS
%scriptclass com.exasol.cloudetl.scriptclasses.ExportPath;
%jar /buckets/bfsdefault/bucket1/cloud-storage-etl-udfs-{VERSION}.jar;
/

CREATE OR REPLACE JAVA SET SCRIPT EXPORT_TABLE(...) EMITS (ROWS_AFFECTED INT) AS
%scriptclass com.exasol.cloudetl.scriptclasses.ExportTable;
%jar /buckets/bfsdefault/bucket1/cloud-storage-etl-udfs-{VERSION}.jar;
/
```

Please do not forget to change the bucket name or the latest jar version
according to your setup.

## Building from source

Clone the repository,

```bash
git clone https://github.com/exasol/cloud-storage-etl-udfs

cd cloud-storage-etl-udfs/
```

Create assembly jar,

```bash
./sbtx assembly
```

The packaged jar should be located at
`target/scala-2.12/cloud-storage-etl-udfs-{VERSION}.jar`.

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for contribution guidelines.

For requesting a feature, providing a feedback or reporting an issue, please
open a [Github issue][gh-issues].

[travis-badge]: https://travis-ci.org/exasol/cloud-storage-etl-udfs.svg?branch=master
[travis-link]: https://travis-ci.org/exasol/cloud-storage-etl-udfs
[codecov-badge]: https://codecov.io/gh/exasol/cloud-storage-etl-udfs/branch/master/graph/badge.svg
[codecov-link]: https://codecov.io/gh/exasol/cloud-storage-etl-udfs
[gh-release-badge]: https://img.shields.io/github/release/exasol/cloud-storage-etl-udfs.svg
[gh-release-link]: https://github.com/exasol/cloud-storage-etl-udfs/releases/latest
[gh-issues]: https://github.com/exasol/cloud-storage-etl-udfs/issues
[exasol]: https://www.exasol.com/en/
[sol-594]: https://www.exasol.com/support/browse/SOL-594
[apache-hive]: https://hive.apache.org/
[hadoop-etl-udfs]: https://github.com/exasol/hadoop-etl-udfs
[s3]: https://aws.amazon.com/s3/
[gcs]: https://cloud.google.com/storage/
[azure]: https://azure.microsoft.com/en-us/services/storage/blobs/
[parquet]: https://parquet.apache.org/
[avro]: https://avro.apache.org/
[jars]: https://github.com/exasol/cloud-storage-etl-udfs/releases
