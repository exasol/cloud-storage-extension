# Exasol Public Cloud Storage ETL UDFs

[![Build Status][travis-badge]][travis-link]
[![Codecov][codecov-badge]][codecov-link]
[![GitHub Latest Release][gh-release-badge]][gh-release-link]

<p style="border: 1px solid black;padding: 10px; background-color: #FFFFCC;">
<span style="font-size:200%">&#128712;</span> Please note that this is an open
source project which is officially supported by Exasol. For any question, you
can contact our support team.
</p>

## Table of Contents

* [Overview](#overview)
* [Getting started](#getting-started)
* [Storage Formats](#data-storage-formats)
* [Import](#import)
* [Export](#export)
* [Building from source](#building-from-source)

## Overview

This repository contains helper code to create [Exasol][exasol] ETL UDFs in
order to transfer data to/from public cloud storage services such as [AWS
S3][s3], [Google Cloud Storage][gcs] and [Azure Blob Storage][azure].

**Please check out the currently [supported storage formats
below](#data-storage-formats).**

Please be aware that Exasol already supports natively [loading CSV format from
AWS S3][sol-594] (but not GCS or Azure) and similarly transfering data to/from
[Apache Hive][hadoop-etl-udfs].

## Getting started

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

Please do not forget to change the bucket name or latest jar version according
to your setup.

## Data Storage Formats

When performing import or export, the default data format is set as [Apache
Parquet][parquet] format. However, you can specify the format using
`DATA_FORMAT` configuration property.

For example in order to import [Apache Avro][avro] format:

```sql
IMPORT INTO MY_SALES_POSITIONS_TABLE
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH    = 's3a://exa-bucket/data/avro/retail/sales_positions/*'
  DATA_FORMAT    = 'AVRO'
  ...
  PARALLELISM    = 'nproc()';
```

### Supported storage formats

* [Apache Parquet][parquet]
* [Apache Avro][avro]: currently only import is supported

## IMPORT

Please follow instructions below in order to import from different cloud
storages.

### Make sure an Exasol table is available

In this walkthrough we use the `SALES_POSITIONS` table to import data into or to
export its contents to cloud storages.

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

Similarly, please do not forget to change the paths accordingly in the import
commands below.

### Import from AWS S3

```sql
IMPORT INTO SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH    = 's3a://exa-bucket/data/parquet/retail/sales_positions/*'
  S3_ACCESS_KEY  = 'MY_AWS_ACCESS_KEY'
  S3_SECRET_KEY  = 'MY_AWS_SECRET_KEY'
  S3_ENDPOINT    = 's3.MY_REGION.amazonaws.com'
  PARALLELISM    = 'nproc()';

-- MY_REGION is one of AWS regions, for example, eu-central-1

SELECT * FROM SALES_POSITIONS LIMIT 10;
```

### Import from Google GCS

In order to read data from [Google GCS][gcs], you need to provide a service
account key file. This should be uploaded to a secure Exasol bucket in advance.

For example,

```bash
curl \
  -X PUT \
  -T path/to/project-id-service-keyfile.json \
  http://w:MY-PASSWORD@EXA-NODE-ID:2580/bucket1/project-id-service-keyfile.json
```

And then run import,

```sql
IMPORT INTO SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH      = 'gs://exa-bucket/data/parquet/retail/sales_positions/*'
  GCS_PROJECT_ID   = 'MY_GCS_PORJECT_ID'
  GCS_KEYFILE_PATH = 'MY_BUCKETFS_PATH/project-id-service-keyfile.json'
  PARALLELISM      = 'nproc()';

SELECT * FROM SALES_POSITIONS LIMIT 10;
```

### Import from Azure Blob Store

```sql
IMPORT INTO SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH        = 'wasbs://<MY-CONTAINER>@<MY-ACCOUNT-NAME>.blob.core.windows.net/data/parquet/sales-positions/*'
  AZURE_ACCOUNT_NAME = 'MY_AZURE_STORAGE_ACCOUNT_NAME'
  AZURE_SECRET_KEY   = 'MY_AZURE_STORAGE_SECRET_KEY'
  PARALLELISM        = 'nproc()';

SELECT * FROM SALES_POSITIONS LIMIT 10;
```

## EXPORT

Please follow steps below in order to export to various cloud storages.

Like import, we will export the `SALES_POSITIONS` table.

### Export to AWS S3

```sql
EXPORT SALES_POSITIONS
INTO SCRIPT ETL.EXPORT_PATH WITH
  BUCKET_PATH    = 's3a://exa-bucket/data/parquet/retail/sales_positions/'
  S3_ACCESS_KEY  = 'MY_AWS_ACCESS_KEY'
  S3_SECRET_KEY  = 'MY_AWS_SECRET_KEY'
  S3_ENDPOINT    = 's3.MY_REGION.amazonaws.com'
  PARALLELISM    = 'nproc()';
```

### Export to GCS

```sql
EXPORT SALES_POSITIONS
INTO SCRIPT ETL.EXPORT_PATH WITH
  BUCKET_PATH      = 'gs://exa-bucket/data/parquet/retail/sales_positions/'
  GCS_PROJECT_ID   = 'MY_GCS_PORJECT_ID'
  GCS_KEYFILE_PATH = 'MY_BUCKETFS_PATH/project-id-service-keyfile.json'
  PARALLELISM      = 'nproc()';
```

### Export to Azure Blob Store

```sql
EXPORT SALES_POSITIONS
INTO SCRIPT ETL.EXPORT_PATH WITH
  BUCKET_PATH        = 'wasbs://<MY-CONTAINER>@<MY-ACCOUNT-NAME>.blob.core.windows.net/data/parquet/sales-positions/'
  AZURE_ACCOUNT_NAME = 'MY_AZURE_STORAGE_ACCOUNT_NAME'
  AZURE_SECRET_KEY   = 'MY_AZURE_STORAGE_SECRET_KEY'
  PARALLELISM        = 'nproc()';
```

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

[travis-badge]: https://travis-ci.org/exasol/cloud-storage-etl-udfs.svg?branch=master
[travis-link]: https://travis-ci.org/exasol/cloud-storage-etl-udfs
[codecov-badge]: https://codecov.io/gh/exasol/cloud-storage-etl-udfs/branch/master/graph/badge.svg
[codecov-link]: https://codecov.io/gh/exasol/cloud-storage-etl-udfs
[gh-release-badge]: https://img.shields.io/github/release/exasol/cloud-storage-etl-udfs.svg
[gh-release-link]: https://github.com/exasol/cloud-storage-etl-udfs/releases/latest
[exasol]: https://www.exasol.com/en/
[sol-594]: https://www.exasol.com/support/browse/SOL-594
[hadoop-etl-udfs]: https://github.com/exasol/hadoop-etl-udfs
[s3]: https://aws.amazon.com/s3/
[gcs]: https://cloud.google.com/storage/
[azure]: https://azure.microsoft.com/en-us/services/storage/blobs/
[parquet]: https://parquet.apache.org/
[avro]: https://avro.apache.org/
[jars]: https://github.com/exasol/cloud-storage-etl-udfs/releases
