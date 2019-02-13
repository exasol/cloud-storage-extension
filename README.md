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
* [Usage](#usage)
* [Building from Source](#building-from-source)

## Overview

This repository contains helper code to create [Exasol][exasol] ETL UDFs in
order to transfer data to/from public cloud storage services such as [AWS
S3][s3], [Google Cloud Storage][gcs] and [Azure Blob Storage][azure].

**Currently only [Apache Parquet][parquet] format and export to AWS S3 is
supported.**

Please be aware that Exasol already supports natively [loading CSV format from
AWS S3][sol-594] (but not GCS or Azure) and similarly transfering data to/from
[Apache Hive][hadoop-etl-udfs].

## Usage

Please follow the steps described below in order to setup the UDFs.

### Download the JAR file

Download the latest jar file from [releases][jars].

Additionally, you can also build it from the source by following the [build from
source](#building-from-source) steps.

### Upload the JAR file to Exasol BucketFS

```bash
curl \
  -X PUT \
  -T path/to/jar/cloud-storage-etl-udfs-{VERSION}.jar \
  http://w:MY-PASSWORD@EXA-NODE-ID:2580/bucket1/cloud-storage-etl-udfs-{VERSION}.jar
```

Please change required parameters.

### Create UDFs scripts

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

### Import data from cloud storages

Please follow steps below in order to import from cloud strorages.

#### Create an Exasol schema and table

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

#### Import from AWS S3

```sql
-- ALTER SESSION SET SCRIPT_OUTPUT_ADDRESS='10.0.2.162:3000';

IMPORT INTO SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH    = 's3a://exa-mo-frankfurt/test/retail/sales_positions/*'
  S3_ACCESS_KEY  = 'MY_AWS_ACCESS_KEY'
  S3_SECRET_KEY  = 'MY_AWS_SECRET_KEY'
  S3_ENDPOINT    = 's3.MY_REGION.amazonaws.com'
  PARALLELISM    = 'nproc()*10';

-- MY_REGION is one of AWS regions, for example, eu-central-1

SELECT * FROM SALES_POSITIONS LIMIT 10;
```

#### Import from Google GCS

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
  BUCKET_PATH      = 'gs://exa-test-bucket/data/parquet/sales_positions/*'
  GCS_PROJECT_ID   = 'MY_GCS_PORJECT_ID'
  GCS_KEYFILE_PATH = 'MY_BUCKETFS_PATH/project-id-service-keyfile.json'
  PARALLELISM      = 'nproc()*10';

SELECT * FROM SALES_POSITIONS LIMIT 10;
```

#### Import from Azure Blob Store

```sql
IMPORT INTO SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH        = 'wasbs://<MY-CONTAINER>@<MY-ACCOUNT-NAME>.blob.core.windows.net/sales-positions/*'
  AZURE_ACCOUNT_NAME = 'MY_AZURE_STORAGE_ACCOUNT_NAME'
  AZURE_SECRET_KEY   = 'MY_AZURE_STORAGE_SECRET_KEY'
  PARALLELISM        = 'nproc()*10';

SELECT * FROM SALES_POSITIONS LIMIT 10;
```

#### Export to AWS S3

```sql
EXPORT SALES_POSITIONS
INTO SCRIPT ETL.EXPORT_PATH WITH
  BUCKET_PATH    = 's3a://exa-mo-frankfurt/export/retail/sales_positions/'
  S3_ACCESS_KEY  = 'MY_AWS_ACCESS_KEY'
  S3_SECRET_KEY  = 'MY_AWS_SECRET_KEY'
  S3_ENDPOINT    = 's3.MY_REGION.amazonaws.com'
  PARALLELISM    = 'nproc()';
```

## Building from Source

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
[jars]: https://github.com/exasol/cloud-storage-etl-udfs/releases
