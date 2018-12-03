# Exasol Public Cloud Storage ETL UDFs

[![Build Status][travis-badge]][travis-link]
[![Codecov][codecov-badge]][codecov-link]

###### Please note that this is an open source project which is *not officially supported* by Exasol. We will try to help you as much as possible, but can't guarantee anything since this is not an official Exasol product.

## Table of Contents

* [Overview](#overview)
* [Usage](#usage)
* [Building from Source](#building-from-source)

## Overview

This repository contains helper code to create [Exasol][exasol] ETL UDFs in
order to transfer data to/from public cloud storage services such as [AWS
S3][s3], [Google Cloud Storage][gcs] and [Azure Blob Storage][azure].

**Currently only importing parquet (primitive types) files from AWS S3 into
Exasol is supported.**

**WARNING:** This project is under heavy development. Even though it is useful
currently, please expect many changes (api, additional storages and formats) in
the future.

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
```

### Import data from cloud storage

```sql
CREATE SCHEMA TEST;
OPEN SCHEMA TEST;

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
[exasol]: https://www.exasol.com/en/
[s3]: https://aws.amazon.com/s3/
[gcs]: https://cloud.google.com/storage/
[azure]: https://azure.microsoft.com/en-us/services/storage/blobs/
[jars]: https://github.com/exasol/cloud-storage-etl-udfs/releases
