# Deployment Guide

The steps in this section describe how to deploy the user defined functions
(UDFs) jar file and configure the UDF scripts.

## Table of contents

* [Prerequisites](#prerequisites)
* [Download the JAR file](#download-the-jar-file)
  * [Building from source](#building-from-source)
* [Create Exasol Bucket](#create-exasol-bucket)
* [Upload the JAR file to the bucket](#upload-the-jar-file-to-the-bucket)
* [Create ETL UDFs Scrtips](#create-etl-udfs-scripts)

## Prerequisites

* Running Exasol cluster with version 6.0 or later

## Download the JAR file

You can download the latest assembled (with all dependencies included) JAR file
from [Github releases][jars].

Additionally, you can build it from the source by following the [build from
source](#building-from-source) guide below. This will allow you to use latest
commits that may not be released yet.

### Building from source

Clone the repository,

```bash
git clone https://github.com/exasol/cloud-storage-etl-udfs

cd cloud-storage-etl-udfs/
```

Create assembled jar file,

```bash
./sbtx assembly
```

The packaged jar file should be located at
`target/scala-2.12/cloud-storage-etl-udfs-<VERSION>.jar`.

## Create Exasol Bucket

Next, you need to upload the jar file to a bucket in the Exasol bucket file
system (BucketFS). This will allow us to reference the jar in UDF scripts.

> Please see the section "The synchronous cluster file system BucketFS"
> in the EXASolution User Manual for more details about BucketFS.

For this guide we are going to use an example bucket named `bucket1`.

## Upload the JAR file to the bucket

Then, upload the jar file to the bucket `bucket1`. However, before uploading the
jar, please make sure that the BucketFS ports are open. In this example, we use
the port number `2580` for http.

Upload the jar file using curl:

```bash
curl -X PUT -T cloud-storage-etl-udfs-<VERSION>.jar \
  http://w:write-password@exasol.datanode.domain.com:2580/bucket1/cloud-storage-etl-udfs-<VERSION>.jar
```

> Please also check out Exasol [BucketFS Explorer][bucketfs-explorer] as an
> alternative option to upload jar file to buckets in BucketFS.

## Create ETL UDFs Scripts

Run the following SQL commands to configure UDF scripts.

First, create a schema that will contain UDF scrtips.

```sql
CREATE SCHEMA ETL;
```

The following SQL statements create scripts that allow importing data from cloud
storage system.

The first script, `IMPORT_PATH` will be used as entry point when running the
import UDF.

```sql
OPEN SCHEMA ETL;

CREATE OR REPLACE JAVA SET SCRIPT IMPORT_PATH(...) EMITS (...) AS
  %scriptclass com.exasol.cloudetl.scriptclasses.ImportPath;
  %jar /buckets/bfsdefault/bucket1/cloud-storage-etl-udfs-<VERSION>.jar;
/

CREATE OR REPLACE JAVA SET SCRIPT IMPORT_FILES(...) EMITS (...) AS
  %scriptclass com.exasol.cloudetl.scriptclasses.ImportFiles;
  %jar /buckets/bfsdefault/bucket1/cloud-storage-etl-udfs-<VERSION>.jar;
/

CREATE OR REPLACE JAVA SCALAR SCRIPT IMPORT_METADATA(...)
EMITS (filename VARCHAR(200), partition_index VARCHAR(100)) AS
  %scriptclass com.exasol.cloudetl.scriptclasses.ImportMetadata;
  %jar /buckets/bfsdefault/bucket1/cloud-storage-etl-udfs-<VERSION>.jar;
/
```

For exporting data from Exasol tables to cloud storage filesystems, run these
statements to create export UDF scrips.

```sql
OPEN SCHEMA ETL;

CREATE OR REPLACE JAVA SET SCRIPT EXPORT_PATH(...) EMITS (...) AS
  %scriptclass com.exasol.cloudetl.scriptclasses.ExportPath;
  %jar /buckets/bfsdefault/bucket1/cloud-storage-etl-udfs-<VERSION>.jar;
/

CREATE OR REPLACE JAVA SET SCRIPT EXPORT_TABLE(...) EMITS (ROWS_AFFECTED INT) AS
  %scriptclass com.exasol.cloudetl.scriptclasses.ExportTable;
  %jar /buckets/bfsdefault/bucket1/cloud-storage-etl-udfs-<VERSION>.jar;
/
```

Similarly, run the following SQL statements in order to create Exasol UDF
scripts to import data from Kafka cluster.

```sql
OPEN SCHEMA ETL;

CREATE OR REPLACE JAVA SET SCRIPT KAFKA_PATH(...) EMITS (...) AS
  %scriptclass com.exasol.cloudetl.scriptclasses.KafkaPath;
  %jar /buckets/bfsdefault/bucket1/cloud-storage-etl-udfs-<VERSION>.jar;
/

CREATE OR REPLACE JAVA SET SCRIPT KAFKA_IMPORT(...) EMITS (...) AS
  %scriptclass com.exasol.cloudetl.scriptclasses.KafkaImport;
  %jar /buckets/bfsdefault/bucket1/cloud-storage-etl-udfs-<VERSION>.jar;
/

CREATE OR REPLACE JAVA SET SCRIPT KAFKA_METADATA(
  params VARCHAR(2000), 
  kafka_partition DECIMAL(18, 0), 
  kafka_offset DECIMAL(36, 0)
)
EMITS (partition_index DECIMAL(18, 0), max_offset DECIMAL(36,0)) AS
  %scriptclass com.exasol.cloudetl.scriptclasses.KafkaMetadata;
  %jar /buckets/bfsdefault/bucket1/cloud-storage-etl-udfs-<VERSION>.jar;
```

Please do not forget to change the bucket name or the latest jar version
according to your deployment setup.

[jars]: https://github.com/exasol/cloud-storage-etl-udfs/releases
[bucketfs-explorer]: https://github.com/exasol/bucketfs-explorer
