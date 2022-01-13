# User Guide

Exasol Cloud Storage Extension allows you to access public cloud storage
systems.

Using the extension you can import and export data in structured formats such as
Parquet, Avro or Orc.

## Table of Contents

- [Getting Started](#getting-started)
- [Deployment](#deployment)
- [Prepare Exasol Table](#prepare-an-exasol-table-for-import)
- [UDF Parameters](#parameters)
- [Parallelism](#parallelism)
- [Data Mapping](#data-mapping)
- [Amazon S3](#amazon-s3)
- [Google Cloud Storage](#google-cloud-storage)
- [Azure Blob Storage](#azure-blob-storage)
- [Azure DataLake Gen1 Storage](#azure-data-lake-gen1-storage)
- [Azure DataLake Gen2 Storage](#azure-data-lake-gen2-storage)
- [Delta Format](#delta-format)
- [Hadoop Distributed Filesystem (HDFS)](#hadoop-distributed-filesystem-hdfs)
- [Alluxio Filesystem](#alluxio-filesystem)

## Overview

Here an overview of the supported features.

<table>
  <tr>
    <th rowspan="2">Import</th>
    <th rowspan="2"></th>
    <th colspan="4"></th>
  </tr>
  <tr>
    <th>Parquet</th>
    <th>Avro</th>
    <th>Orc</th>
    <th>Delta</th>
  </tr>
  <tr>
    <td>AWS</td>
    <td>S3</td>
    <td rowspan="1" align="center">&#10003;</td>
    <td rowspan="1" align="center">&#10003;</td>
    <td rowspan="1" align="center">&#10003;</td>
    <td rowspan="1" align="center">&#10003;</td>
  </tr>
  <tr>
    <td>GCP</td>
    <td>Google Cloud Storage</td>
    <td rowspan="1" align="center">&#10003;</td>
    <td rowspan="1" align="center">&#10003;</td>
    <td rowspan="1" align="center">&#10003;</td>
    <td rowspan="1" align="center"></td>
  </tr>
  <tr>
    <td rowspan="3" align="center">Azure</td>
    <td>Blob Storage</td>
    <td rowspan="3" align="center">&#10003;</td>
    <td rowspan="3" align="center">&#10003;</td>
    <td rowspan="3" align="center">&#10003;</td>
    <td rowspan="3" align="center">&#10003;</td>
  </tr>
  <tr>
    <td>Data Lake (Gen1) Storage</td>
  </tr>
  <tr>
    <td>Data Lake (Gen2) Storage</td>
  </tr>
  <tr></tr>
  <tr>
    <th rowspan="2">Export</th>
    <th rowspan="2"></th>
    <th colspan="4"></th>
  </tr>
  <tr>
    <th>Parquet</th>
    <th>Avro</th>
    <th>Orc</th>
    <th>Delta</th>
  </tr>
  <tr>
    <td>AWS</td>
    <td>S3</td>
    <td rowspan="6" align="center">&#10003;</td>
    <td rowspan="6" align="center"></td>
    <td rowspan="6" align="center"></td>
    <td rowspan="6" align="center"></td>
  </tr>
  <tr>
    <td>GCP</td>
    <td>Google Cloud Storage</td>
  </tr>
  <tr>
  	<td rowspan="4">Azure</td>
  </tr>
  <tr>
    <td>Blob Storage</td>
  </tr>
  <tr>
    <td>Data Lake (Gen1) Storage</td>
  </tr>
  <tr>
    <td>Data Lake (Gen2) Storage</td>
  </tr>
</table>

## Getting Started

The `cloud-storage-extension` works for all the supported Exasol versions.

### Supported Data Formats

We support the Parquet, Avro and Orc formats when importing data from cloud
storages into an Exasol table. However, we export Exasol tables only as Parquet
data to storage systems.

### Supported Cloud Storage Systems

Exasol Cloud Storage Extension can access Amazon S3, Google Cloud Storage (GCP),
Azure Blob Storage, Azure Data Lake Gen1 Storage and Azure Data Lake Gen2
Storage.

## Deployment

To use the exasol-cloud-storage-extension, you should first deploy the jar file
to an Exasol BucketFS bucket and create user-defined function (UDF) scripts.

### Download the JAR File

Download the latest assembled (with all dependencies included) JAR file
from [Github releases][jars].

[jars]: https://github.com/exasol/cloud-storage-extension/releases

Once it is saved to your local system, make sure that the SHA256 sum of the
downloaded jar file is the same as the checksum provided in the releases.

To check the SHA256 result of the local jar, run the command:

```sh
sha256sum exasol-cloud-storage-extension-<VERSION>.jar
```

### Building From Source

Additionally, you can build a jar from the source. This allows you to use
the latest commits that may not be released yet.

Clone the repository:

```sh
git clone https://github.com/exasol/cloud-storage-extension

cd cloud-storage-extension/
```

To create an assembled jar file, [install SBT build
tool](https://www.scala-sbt.org/download.html) and run the command:

```sh
sbt assembly
```

The assembled jar file should be located at
`target/scala-2.12/cloud-storage-extension-<VERSION>.jar`.

### Create an Exasol Bucket

Next, you need to upload the jar file to a bucket in the Exasol bucket file
system: BucketFS. This allows you to reference the extension jar file in UDF
scripts.

For more information on how to create a bucket in BucketFS, please check out the
Exasol [Database Concepts: BucketFS][bucketfs-docs] documentation.

[bucketfs-docs]: https://docs.exasol.com/database_concepts/bucketfs/bucketfs.htm

### Upload the JAR File to the Bucket

Now you upload the jar file to the bucket. However, before uploading the
jar, please make sure the BucketFS ports are open. It uses port number `2580`
for the HTTP protocol.

Upload the jar file using curl command:

```sh
curl -X PUT -T exasol-cloud-storage-extension-<VERSION>.jar \
  http://w:<WRITE_PASSWORD>@exasol.datanode.domain.com:2580/<BUCKET>/exasol-cloud-storage-extension-<VERSION>.jar
```

You can also check out the Exasol [BucketFS Explorer][bucketfs-explorer] as an
alternative option to upload jar files to buckets in BucketFS.

[bucketfs-explorer]: https://github.com/exasol/bucketfs-explorer

Please ensure that the file is successfully uploaded. Check the bucket contents:

```sh
curl -X GET http://r:<READ_PASSWORD>@exasol.datanode.domain.com:2580/<BUCKET>/
```

### Create UDF Scripts

Run the following SQL commands to setup and configure UDF scripts.

First, create a schema that will contain UDF scripts:

```sql
CREATE SCHEMA CLOUD_STORAGE_EXTENSION;
```

#### Setup Import UDF Scripts

Run the following SQL statements to create importer UDF scripts.

```sql
OPEN SCHEMA CLOUD_STORAGE_EXTENSION;

CREATE OR REPLACE JAVA SET SCRIPT IMPORT_PATH(...) EMITS (...) AS
  %scriptclass com.exasol.cloudetl.scriptclasses.FilesImportQueryGenerator;
  %jar /buckets/bfsdefault/<BUCKET>/exasol-cloud-storage-extension-<VERSION>.jar;
/

CREATE OR REPLACE JAVA SCALAR SCRIPT IMPORT_METADATA(...) EMITS (
  filename VARCHAR(2000),
  partition_index VARCHAR(100),
  start_index DECIMAL(36, 0),
  end_index DECIMAL(36, 0)
) AS
  %scriptclass com.exasol.cloudetl.scriptclasses.FilesMetadataReader;
  %jar /buckets/bfsdefault/<BUCKET>/exasol-cloud-storage-extension-<VERSION>.jar;
/

CREATE OR REPLACE JAVA SET SCRIPT IMPORT_FILES(...) EMITS (...) AS
  %scriptclass com.exasol.cloudetl.scriptclasses.FilesDataImporter;
  %jar /buckets/bfsdefault/<BUCKET>/exasol-cloud-storage-extension-<VERSION>.jar;
/
```

Please do not change the UDF script names. The first script, `IMPORT_PATH` will
be used as an entry point when running the import UDF. It will execute the
`IMPORT_METADATA` script to calculate the number of files in the user-provided
cloud storage path. Then each file will be imported by `IMPORT_FILES` UDF
script.

#### Setup Import UDF Scripts in Docker

If you are using Exasol Docker installation, the UDF scripts require slightly
different deployment.

```sql
OPEN SCHEMA CLOUD_STORAGE_EXTENSION;

CREATE OR REPLACE JAVA SET SCRIPT IMPORT_PATH(...) EMITS (...) AS
  %scriptclass com.exasol.cloudetl.scriptclasses.DockerFilesImportQueryGenerator;
  %jar /buckets/bfsdefault/<BUCKET>/exasol-cloud-storage-extension-<VERSION>.jar;
/

CREATE OR REPLACE JAVA SCALAR SCRIPT IMPORT_METADATA(...) EMITS (
  filename VARCHAR(2000),
  partition_index VARCHAR(100),
  start_index DECIMAL(36, 0),
  end_index DECIMAL(36, 0)
) AS
  %scriptclass com.exasol.cloudetl.scriptclasses.DockerFilesMetadataReader;
  %jar /buckets/bfsdefault/<BUCKET>/exasol-cloud-storage-extension-<VERSION>.jar;
/

CREATE OR REPLACE JAVA SET SCRIPT IMPORT_FILES(...) EMITS (...) AS
  %scriptclass com.exasol.cloudetl.scriptclasses.DockerFilesDataImporter;
  %jar /buckets/bfsdefault/<BUCKET>/exasol-cloud-storage-extension-<VERSION>.jar;
/
```

Please notice that we use different class names for the `%scriptclasses`
parameter.

#### Setup Export UDF Scripts

Run these statements to create export UDF scripts:

```sql
OPEN SCHEMA CLOUD_STORAGE_EXTENSION;

CREATE OR REPLACE JAVA SET SCRIPT EXPORT_PATH(...) EMITS (...) AS
  %scriptclass com.exasol.cloudetl.scriptclasses.TableExportQueryGenerator;
  %jar /buckets/bfsdefault/<BUCKET>/exasol-cloud-storage-extension-<VERSION>.jar;
/

CREATE OR REPLACE JAVA SET SCRIPT EXPORT_TABLE(...) EMITS (ROWS_AFFECTED INT) AS
  %scriptclass com.exasol.cloudetl.scriptclasses.TableDataExporter;
  %jar /buckets/bfsdefault/<BUCKET>/exasol-cloud-storage-extension-<VERSION>.jar;
/
```

Please do not change the UDF script names. The `EXPORT_PATH` is an entry point
UDF and it will call the `EXPORT_TABLE` script internally.

Make sure you change the `<BUCKET>` name and jar version `<VERSION>`
accordingly.

#### Setup Export UDF Scripts in Docker

Similar to import, the UDF scripts require slightly different deployment for
Exasol Docker installations.

```sql
OPEN SCHEMA CLOUD_STORAGE_EXTENSION;

CREATE OR REPLACE JAVA SET SCRIPT EXPORT_PATH(...) EMITS (...) AS
  %scriptclass com.exasol.cloudetl.scriptclasses.DockerTableExportQueryGenerator;
  %jar /buckets/bfsdefault/<BUCKET>/exasol-cloud-storage-extension-<VERSION>.jar;
/

CREATE OR REPLACE JAVA SET SCRIPT EXPORT_TABLE(...) EMITS (ROWS_AFFECTED INT) AS
  %scriptclass com.exasol.cloudetl.scriptclasses.DockerTableDataExporter;
  %jar /buckets/bfsdefault/<BUCKET>/exasol-cloud-storage-extension-<VERSION>.jar;
/
```

Please notice that we use different class names for the `%scriptclasses`
parameter.

## Prepare an Exasol Table for Import

To store the imported data, you need to create a table inside the Exasol
database.

Let's create an example table:

```sql
CREATE OR REPLACE TABLE <schema_name>.<table_name> (
    SALES_ID    INTEGER,
    POSITION_ID SMALLINT,
    ARTICLE_ID  SMALLINT,
    AMOUNT      SMALLINT,
    PRICE       DECIMAL(9,2),
    VOUCHER_ID  SMALLINT,
    CANCELED    BOOLEAN
);
```

The table column names and data types should match the file data format schema.

## Parameters

You can provide several parameters to the Exasol Cloud Storage Extension.

Some of the parameters are required such as connection object name with access
control credentials. And others are optional, for example, parallelism
configuration to set the number of parallel importers.

Please note that the parameter values are provided as string literals,
`S3_ENDPOINT = 's3.eu-central-1.amazonaws.com'`.

### Required Parameters

* ``BUCKET_PATH`` - It specifies a path to the cloud storage filesystem. It
  should start with a storage-specific schema, such as `s3a` or `adl`.

* ``DATA_FORMAT`` - It defines the data file format in the provided path. We
  support importing data from **Avro**, **Orc** and **Parquet** file formats and
  exporting to **Parquet** format.

* Additional storage-specific properties that enable accessing storage
  filesystems.

### Optional Parameters

These are optional parameters that have default values.

* ``PARALLELISM`` - It defines the number of parallel virtual machine instances
  that will be started to import or export data. The default value is `nproc()`
  in the Import SQL statement. Likewise, the default value is `iproc()` in the
  Export SQL statement.

#### Import Optional Parameters

The following are optional parameters for import statements.

* ``CHUNK_SIZE`` - It specifies a file chunk size in bytes. The importer then
  will try to virtually splits a file into chunks with specified size, and
  imports each chunk in parallel. By default it is `67108864` (64MB).

#### Export Optional Parameters

These optional parameters only apply to the data export statements.

* ``OVERWRITE`` - If it is set to `true`, the UDF deletes all the files in the
  export path. By default it is set to `false`. Please keep in mind the delete
  operation is blocking, it can take time to finish at least proportional to the
  number of files. If the delete operation is interrupted, the filesystem is
  left in an intermediate state.

* ``PARQUET_COMPRESSION_CODEC`` - It defines the compression codec to use when
  exporting data into Parquet formatted files. The default value is
  **uncompressed**. Other compression options are **snappy**, **gzip** and
  **lzo**.

* ``EXPORT_BATCH_SIZE`` - It defines the number of records per file from each
  virtual machine (VM). The default value is **100000**. That is, if a single VM
  gets `1M` rows to export, it will create 10 files with default 100000 records
  in each file.

#### Optional proxy configuration

If access to the cloud storage is restricted by a proxy, the IMPORT and EXPORT
statements can be configured with:

* ``PROXY_HOST`` - Configures a proxy host that is used to access the bucket if
  this is required.

* ``PROXY_PORT`` - Configures the port to use for the proxy defined with
  `PROXY_HOST`.

* ``PROXY_USERNAME`` - Configures the username to use for the proxy defined with
  `PROXY_HOST`.

* ``PROXY_PASSWORD`` - Configures the port to use for the proxy defined with
  `PROXY_HOST`.

These parameters are only available for S3 and GCP buckets.

For Azure Buckets, the proxy can be configured via JVM properties using the
`%jvmoption` when creating metadata reader and files data importer deployment
scripts.

For example:

```sql
CREATE OR REPLACE JAVA SCALAR SCRIPT IMPORT_METADATA(...) EMITS (
  filename VARCHAR(2000),
  partition_index VARCHAR(100),
  start_index DECIMAL(36, 0),
  end_index DECIMAL(36, 0)
) AS
  %jvmoption -DHTTPS_PROXY=http://username:password@10.10.1.10:1180
  %scriptclass com.exasol.cloudetl.scriptclasses.FilesMetadataReader;
  %jar /buckets/bfsdefault/<BUCKET>/exasol-cloud-storage-extension-<VERSION>.jar;
/

CREATE OR REPLACE JAVA SET SCRIPT IMPORT_FILES(...) EMITS (...) AS
  %jvmoption -DHTTPS_PROXY=http://username:password@10.10.1.10:1180
  %scriptclass com.exasol.cloudetl.scriptclasses.FilesDataImporter;
  %jar /buckets/bfsdefault/<BUCKET>/exasol-cloud-storage-extension-<VERSION>.jar;
/
```

The variable names may be different, please check out the Azure storage
documentation.

## Parallelism

The setting for parallelism is **different** for import and export statements.

### Import Parallelism Parameter

In the import, the number of files in the storage path is distributed to the
parallel running importer processes. These parallel processes can be controlled
by setting the `PARALLELISM` parameter.

By default, this parameter is set to the `nproc()`. The `nproc()` is an Exasol
special SQL command that returns the total number of data nodes in your cluster.

```sql
IMPORT INTO <schema>.<table>
FROM SCRIPT CLOUD_STORAGE_EXTENSION.IMPORT_PATH WITH
  BUCKET_PATH     = 's3a://<S3_PATH>/*'
  DATA_FORMAT     = 'ORC'
  S3_ENDPOINT     = 's3.<REGION>.amazonaws.com'
  CONNECTION_NAME = 'S3_CONNECTION'
  PARALLELISM     = 'nproc()';
```

In the example above, `PARALLELISM` property value is set to `nproc()` which
returns the number of physical data nodes in the cluster. Thus, the storage
extension starts `nproc()` many parallel importer processes. The total number of
files is distributed among these processes in a round-robin fashion and each
process imports data from their own set of files.

However, you can increase the parallelism by multiplying it with a number. For
example, in order to start four times more processes, set it:

```sql
PARALLELISM = 'nproc()*4'
```

Or to a higher static number as `PARALLELISM = '16'` that will use 16 importer
processes in total.

We **recommend** to set the parallelism properly depending on the cluster
resources (number of cores, memory per node), the number of files and the size
of each file.

### Export Parallelism Parameter

In the export, the parallelism works differently compared to the import SQL
statement.

In the import statement, we are importing data from many files. Using the user
provider parallelism number, we distribute these files into that many importer
processes. For example, simply by taking modulo of file hash by parallelism
number.

In export, we have a table with many records. When exporting an Exasol table,
the `PARALLELISM` parameter value is internally used in a `GROUP BY` clause to
distribute the table records into many exporter processes. The parallelism
should be something dynamic that Exasol database can understand and use in the
group by clause.

The default value for parallelism for export is `iproc()` (notice that it is
different from `nproc()`). It returns the data node id numbers. Therefore,
by default, it creates as many exporter processes as the number of datanodes.

```sql
EXPORT <schema>.<table>
INTO SCRIPT CLOUD_STORAGE_EXTENSION.EXPORT_PATH WITH
  BUCKET_PATH     = 's3a://<S3_PATH>/'
  DATA_FORMAT     = 'PARQUET'
  S3_ENDPOINT     = 's3.<REGION>.amazonaws.com'
  CONNECTION_NAME = 'S3_CONNECTION'
  PARALLELISM     = 'iproc()';
```

Like in import, you can increase the number of exporter processes. Since we need
a dynamic number that the Exasol database can understand, you can combine the
`iproc()` statement with `random()` and `floor()` operations.

For example, to increase the exporter processes four times, set it as below:

```sql
PARALLELISM = 'iproc(), floor(random()*4)'
```

Please change this parameter according to your setup.

Each exporter process creates a single file. This can be a problem if the table
has many records. You can change this behavior by adapting the
`EXPORT_BATCH_SIZE` parameter. This value is used to further split the number of
records per process and create several files instead of a single file.

## Data Mapping

This section shows how data types from each format is mapped to the
[Exasol types][exasol-types]. We suggest to use the recommended Exasol
column types when preparing the table.

[exasol-types]: https://docs.exasol.com/sql_references/data_types/datatypesoverview.htm

### Avro Data Mapping

| Avro Data Type | Avro Logical Attribute | Recommended Exasol Column Types |
|:---------------|:-----------------------|:--------------------------------|
| boolean        |                        | BOOLEAN                         |
| int            |                        | INT, INTEGER, DECIMAL(18, 0)    |
| int            | date                   | DATE                            |
| long           |                        | BIGINT, DECIMAL(36, 0)          |
| long           | timestamp-millis       | TIMESTAMP                       |
| long           | timestamp-micros       | TIMESTAMP                       |
| float          |                        | FLOAT                           |
| double         |                        | DOUBLE, DOUBLE PRECISION        |
| bytes          |                        | VARCHAR(n), CHAR(n)             |
| bytes          | decimal(p, s)          | DECIMAL(p, s)                   |
| fixed          |                        | VARCHAR(n), CHAR(n)             |
| fixed          | decimal(p, s)          | DECIMAL(p, s)                   |
| string         |                        | VARCHAR(n), CHAR(n)             |
| enum           |                        | VARCHAR(n), CHAR(n)             |
| union          |                        | Corresponding Non Null Type     |
| array          |                        | VARCHAR(n), CHAR(n)             |
| map            |                        | VARCHAR(n), CHAR(n)             |
| record         |                        | VARCHAR(n), CHAR(n)             |

### Orc Data Mapping

| Orc Data Type        | Orc Logical Type  | Recommended Exasol Column Types |
|:---------------------|:------------------|:--------------------------------|
| boolean              |                   | BOOLEAN                         |
| short                |                   | INT, INTEGER, DECIMAL(18, 0)    |
| int                  |                   | INT, INTEGER, DECIMAL(18, 0)    |
| byte                 |                   | BIGINT, DECIMAL(36, 0)          |
| long                 |                   | BIGINT, DECIMAL(36, 0)          |
| float                |                   | FLOAT                           |
| double               |                   | DOUBLE, DOUBLE PRECISION        |
| binary               |                   | VARCHAR(n), CHAR(n)             |
| char                 |                   | VARCHAR(n), CHAR(n)             |
| string               |                   | VARCHAR(n), CHAR(n)             |
| varchar              |                   | VARCHAR(n), CHAR(n)             |
| decimal              |                   | DECIMAL(p, s)                   |
| date                 |                   | DATE                            |
| timestamp            |                   | TIMESTAMP                       |
| list                 |                   | VARCHAR(n), CHAR(n)             |
| map                  |                   | VARCHAR(n), CHAR(n)             |
| struct               |                   | VARCHAR(n), CHAR(n)             |
| union                |                   | VARCHAR(n), CHAR(n)             |

#### Orc Union Type

Unlike `struct` type, `union` type holds a tagged field values. Therefore, there
are no fields names in the union type.

```java
unionSchema = TypeDescription.fromString("struct<column:uniontype<i:int,s:string>>");
unionValue = new OrcUnion(unionSchema);
unionValue.set(0, new IntWritable(13));
unionValue.set(1, new Text("abc"));
```

In the above example union type, we can only read the values using the
positional tag index. For example, to read the integer `unionValue.0`.

When converting this type into an Exasol `VARCHAR` we decided to use the
category name of inner types as field names.

The above union value will be converted to a JSON string as following:

```
{"INT":13,"STRING":"abc"}
```

If either of the tags are not set, then it is read as a `null` value.

### Parquet Data Mapping

| Parquet Data Type    | Parquet Logical Type  | Recommended Exasol Column Types |
|:---------------------|:----------------------|:--------------------------------|
| boolean              |                       | BOOLEAN                         |
| int32                |                       | INT, INTEGER, DECIMAL(18, 0)    |
| int32                | date                  | DATE                            |
| int32                | decimal(p, s)         | DECIMAL(p, s)                   |
| int64                |                       | BIGINT, DECIMAL(36, 0)          |
| int64                | timestamp_millis      | TIMESTAMP                       |
| int64                | timestamp_micros      | TIMESTAMP                       |
| int64                | decimal(p, s)         | DECIMAL(p, s)                   |
| float                |                       | FLOAT                           |
| double               |                       | DOUBLE, DOUBLE PRECISION        |
| binary               |                       | VARCHAR(n), CHAR(n)             |
| binary               | utf8                  | VARCHAR(n), CHAR(n)             |
| binary               | decimal(p, s)         | DECIMAL(p, s)                   |
| fixed_len_byte_array |                       | VARCHAR(n), CHAR(n)             |
| fixed_len_byte_array | decimal(p, s)         | DECIMAL(p, s)                   |
| int96                |                       | TIMESTAMP                       |
| group                |                       | VARCHAR(n)                      |
| group                | LIST                  | VARCHAR(n)                      |
| group                | MAP                   | VARCHAR(n)                      |
| group                | REPEATED              | VARCHAR(n)                      |

#### Parquet Repeated Types

Parquet data type can repeat a single field or the group of fields.  The
cloud-storage-extension imports repeated fields and repeated groups with a
single field as a JSON array string.

For example, the Parquet importer imports the following two schemas as JSON
array strings.

```
message parquet_schema {
  repeated binary name (UTF8);
}
```

```
message parquet_schema {
  repeated group person {
    required binary name (UTF8);
  }
}
```

The importer imports both of these Parquet types as a JSON array
`["John","Jane"]`.

On the other hand, you import a Parquet repeated group with multiple fields as
JSON array of structs.

```
message parquet_schema {
  repeated group person {
    required binary name (UTF8);
    optional int32 age;
  }
}
```

The Parquet importer imports it as JSON array of person structs:

```
[
  {"name": "John", "age": 24},
  {"name": "Jane", "age": 22}
]
```

## Amazon S3

To access the Amazon S3 bucket data, you need to provide AWS access credentials:
access key and secret key. However, if you are using the Multi Factor
Authentication (MFA), you can also provide additional session token together
with access credentials.

```
S3_ACCESS_KEY
S3_SECRET_KEY
S3_SESSION_TOKEN
```

Please follow the [Amazon credentials management best practices][aws-creds] when
creating credentials.

[aws-creds]: https://docs.aws.amazon.com/general/latest/gr/aws-sec-cred-types.html

Another required parameter is the S3 endpoint, `S3_ENDPOINT`. An endpoint is the
URL of the entry point for an AWS resource. For example,
`s3.eu-central-1.amazonaws.com` is an endpoint for the S3 resource in the
Frankfurt region.

### Create Exasol Connection Object

Create a named connection object and encode credentials key-value pairs
separated by a semicolon (`;`).

Using AWS access and secret keys:

```sql
CREATE OR REPLACE CONNECTION S3_CONNECTION
TO ''
USER ''
IDENTIFIED BY 'S3_ACCESS_KEY=<AWS_ACCESS_KEY>;S3_SECRET_KEY=<AWS_SECRET_KEY>';
```

Or together with a session token:

```sql
CREATE OR REPLACE CONNECTION S3_CONNECTION
TO ''
USER ''
IDENTIFIED BY 'S3_ACCESS_KEY=<AWS_ACCESS_KEY>;S3_SECRET_KEY=<AWS_SECRET_KEY>;S3_SESSION_TOKEN=<AWS_SESSION_TOKEN>';
```
A user that will run IMPORT or EXPORT UDF will need ACCESS privilige on this connection directly or via role. See [Privileges][exa-docs-privileges] and [Details on Rights Management][exa-docs-privileges-det] sections in the Exasol documentation for more details.

[exa-docs-privileges]: https://docs.exasol.com/database_concepts/privileges.htm
[exa-docs-privileges-det]: https://docs.exasol.com/database_concepts/privileges/details_rights_management.htm

### Run Import Statement

```sql
IMPORT INTO <schema>.<table>
FROM SCRIPT CLOUD_STORAGE_EXTENSION.IMPORT_PATH WITH
  BUCKET_PATH     = 's3a://<S3_PATH>/import/orc/data/*'
  DATA_FORMAT     = 'ORC'
  S3_ENDPOINT     = 's3.<REGION>.amazonaws.com'
  CONNECTION_NAME = 'S3_CONNECTION'
  PARALLELISM     = 'nproc()*<MULTIPLIER>';
```

### Run Export Statement

```sql
EXPORT <schema>.<table>
INTO SCRIPT CLOUD_STORAGE_EXTENSION.EXPORT_PATH WITH
  BUCKET_PATH     = 's3a://<S3_PATH>/export/parquet/data/'
  DATA_FORMAT     = 'PARQUET'
  S3_ENDPOINT     = 's3.<REGION>.amazonaws.com'
  CONNECTION_NAME = 'S3_CONNECTION'
  PARALLELISM     = 'iproc(), floor(random()*<MULTIPLIER>)';
```

### S3 Path Style Access

Amazon S3 [deprecated the path][s3-path-style-deprecation1] [style
access][s3-path-style-deprecation2] to the buckets at the end of the 2020. This
breaks the access to the bucket that contain dot (`.`) in their names.

[s3-path-style-deprecation1]: https://aws.amazon.com/blogs/aws/amazon-s3-path-deprecation-plan-the-rest-of-the-story/
[s3-path-style-deprecation2]: https://docs.aws.amazon.com/AmazonS3/latest/userguide/VirtualHosting.html#path-style-access

To enable the path style access to the bucket, you can set the
`S3_PATH_STYLE_ACCESS` parameter to `true`.

For example:

```
IMPORT INTO <schema>.<table>
FROM SCRIPT CLOUD_STORAGE_EXTENSION.IMPORT_PATH WITH
  BUCKET_PATH          = 's3a://<S3_PATH>.data.domain/import/data/*.parquet'
  DATA_FORMAT          = 'PARQUET'
  S3_PATH_STYLE_ACCESS = 'true'
  S3_ENDPOINT          = 's3.<REGION>.amazonaws.com'
  CONNECTION_NAME      = 'S3_CONNECTION'
  PARALLELISM          = 'nproc()';
```

## Google Cloud Storage

Similar to Amazon S3, you need to have security credentials to access the Google
Cloud Storage (GCP).

For this, you need to set two properties when running the UDF:

```
GCS_PROJECT_ID
GCS_KEYFILE_PATH
```

The **GCS_PROJECT_ID** is a Google Cloud Platform (GCP) project identifier. It
is a unique string for your project which is composed of the project name and a
randomly assigned number. Please check out the GCP [creating and managing
projects][gcp-projects] page for more information.

The **GCS_KEYFILE_PATH** is a BucketFS path to the GCP private key file
location. It is usually stored in the JSON format.

A Google Cloud Platform service account is an identity that an application can
use to authenticate and perform authorized tasks on Google cloud resources. It
is a special type of Google account intended to represent a non-human user that
needs to access Google APIs. Please check out the GCP [introduction to service
accounts][gcp-auth-intro], [understanding service accounts][gcp-auth-under] and
generating [service account private key][gcp-auth-keys] documentation pages.

[gcp-projects]: https://cloud.google.com/resource-manager/docs/creating-managing-projects
[gcp-auth-intro]: https://cloud.google.com/compute/docs/access/service-accounts
[gcp-auth-under]: https://cloud.google.com/iam/docs/understanding-service-accounts
[gcp-auth-keys]: https://cloud.google.com/video-intelligence/docs/common/auth

Once the service account is generated, give enough permissions to it to access
the Google Cloud Storage objects and download its private key as a JSON file.

Upload a GCP service account key file to a BucketFS bucket:

```bash
curl -X PUT -T gcp-<PROJECT_ID>-service-keyfile.json \
  http://w:<PASSWORD>@exasol.datanode.domain.com:2580/<BUCKET>/gcp-<PROJECT_ID>-service-keyfile.json
```

Make sure that the bucket is **secure** and only **readable by users** who run
the Exasol Cloud Storage Extension scripts. Please check out the [BucketFS
Access
Control](https://docs.exasol.com/database_concepts/bucketfs/access_control.htm)
documentation for more information.

### Run Import Statement

```sql
IMPORT INTO <schema>.<table>
FROM SCRIPT CLOUD_STORAGE_EXTENSION.IMPORT_PATH WITH
  BUCKET_PATH      = 'gs://<GCS_STORAGE_PATH>/import/avro/data/*'
  DATA_FORMAT      = 'AVRO'
  GCS_PROJECT_ID   = '<GCP_PORJECT_ID>'
  GCS_KEYFILE_PATH = '/buckets/bfsdefault/<BUCKET>/gcp-<PROJECT_ID>-service-keyfile.json'
  PARALLELISM      = 'nproc()*<MULTIPLIER>';
```

### Run Export Statement

```sql
EXPORT <schema>.<table>
INTO SCRIPT CLOUD_STORAGE_EXTENSION.EXPORT_PATH WITH
  BUCKET_PATH      = 'gs://<GCS_STORAGE_PATH>/export/parquet/data/'
  DATA_FORMAT      = 'PARQUET'
  GCS_PROJECT_ID   = '<GCP_PORJECT_ID>'
  GCS_KEYFILE_PATH = '/buckets/bfsdefault/<BUCKET>/gcp-<PROJECT_ID>-service-keyfile.json'
  PARALLELISM      = 'iproc(), floor(random()*<MULTIPLIER>)';
```

## Azure Blob Storage

You can access Azure Blob Storage containers using two possible authorization
mechanisms:

```
AZURE_SECRET_KEY
AZURE_SAS_TOKEN
```

The **AZURE_SECRET_KEY** is 512-bit storage access keys that can be generated
after creating a storage account. It is used to authorize access to the storage
accounts.

The **AZURE_SAS_TOKEN** is a Shared Access Signature (SAS) that provides secure
access to storage accounts with granular control over how the clients can access
the data.

Please refer to Azure documentation on [creating storage
account][azure-blob-account], managing [storage access keys][azure-blob-keys]
and using [shared access signatures (SAS)][azure-blob-sas].

[azure-blob-account]: https://docs.microsoft.com/en-us/azure/storage/common/storage-quickstart-create-account
[azure-blob-keys]: https://docs.microsoft.com/en-us/azure/storage/common/storage-account-manage#access-keys
[azure-blob-sas]: https://docs.microsoft.com/en-us/azure/storage/common/storage-sas-overview

You should use either one of these options when using
exasol-cloud-storage-extension to access the Azure Blob Storage containers.


### Using Azure Secret Key Authentication

Create a named connection containing an Azure secret key:

```sql
CREATE OR REPLACE CONNECTION AZURE_BLOB_SECRET_CONNECTION
TO ''
USER ''
IDENTIFIED BY 'AZURE_SECRET_KEY=<AZURE_SECRET_KEY>';
```

Run an import statement:

```sql
IMPORT INTO <schema>.<table>
FROM SCRIPT CLOUD_STORAGE_EXTENSION.IMPORT_PATH WITH
  BUCKET_PATH      = 'wasbs://<AZURE_CONTAINER_NAME>@<AZURE_ACCOUNT_NAME>.blob.core.windows.net/import/orc/data/*'
  DATA_FORMAT      = 'ORC'
  CONNECTION_NAME  = 'AZURE_BLOB_SECRET_CONNECTION'
  PARALLELISM      = 'nproc()*<MULTIPLIER>';
```

Run an export statement:

```sql
EXPORT <schema>.<table>
INTO SCRIPT CLOUD_STORAGE_EXTENSION.EXPORT_PATH WITH
  BUCKET_PATH      = 'wasbs://<AZURE_CONTAINER_NAME>@<AZURE_ACCOUNT_NAME>.blob.core.windows.net/export/parquet/'
  DATA_FORMAT      = 'PARQUET'
  CONNECTION_NAME  = 'AZURE_BLOB_SECRET_CONNECTION'
  PARALLELISM      = 'iproc(), floor(random()*<MULTIPLIER>)';
```

### Using Azure SAS Token Authentication

Create a named connection containing an Azure SAS token:

```sql
CREATE OR REPLACE CONNECTION AZURE_BLOB_SAS_CONNECTION
TO ''
USER ''
IDENTIFIED BY 'AZURE_SAS_TOKEN=<AZURE_SAS_TOKEN>';
```

Run an import statement:

```sql
IMPORT INTO <schema>.<table>
FROM SCRIPT CLOUD_STORAGE_EXTENSION.IMPORT_PATH WITH
  BUCKET_PATH     = 'wasbs://<AZURE_CONTAINER_NAME>@<AZURE_ACCOUNT_NAME>.blob.core.windows.net/import/avro/data/*'
  DATA_FORMAT     = 'AVRO'
  CONNECTION_NAME = 'AZURE_BLOB_SAS_CONNECTION'
  PARALLELISM     = 'nproc()*<MULTIPLIER>';
```

Run an export statement:

```sql
EXPORT <schema>.<table>
INTO SCRIPT CLOUD_STORAGE_EXTENSION.EXPORT_PATH WITH
  BUCKET_PATH     = 'wasbs://<AZURE_CONTAINER_NAME>@<AZURE_ACCOUNT_NAME>.blob.core.windows.net/export/parquet/'
  DATA_FORMAT     = 'PARQUET'
  CONNECTION_NAME = 'AZURE_BLOB_SAS_CONNECTION'
  PARALLELISM     = 'iproc(), floor(random()*<MULTIPLIER>)';
```

The Azure Blob Storage container path URI scheme can be `wasbs` or `wasb`.

## Azure Data Lake Gen1 Storage

The following properties are required to access the Azure Data Lake (Gen1)
Storage.

```
AZURE_CLIENT_ID
AZURE_CLIENT_SECRET
AZURE_DIRECTORY_ID
```

The **AZURE_CLIENT_ID** is the Azure Active Directory (AD) App registration
Application ID.

The **AZURE_CLIENT_SECRET** is the secret key generated for the Application ID.

The **AZURE_DIRECTORY_ID** is the Active Directory (AD) Directory (Tenant) ID.

Please check out the Azure documentation on how to create [service to service
authentication using Active Directory][azure-adl-s2s-auth] and [Azure AD
application and service principal][azure-adl-srv-prin]. These Azure
documentation pages should show how to obtain required configuration settings.

[azure-adl-s2s-auth]: https://docs.microsoft.com/en-us/azure/data-lake-store/data-lake-store-service-to-service-authenticate-using-active-directory
[azure-adl-srv-prin]: https://docs.microsoft.com/en-us/azure/active-directory/develop/howto-create-service-principal-portal

Finally, make sure that the client id has access permissions to the Gen1
storage container or its child directories.

### Create Exasol Connection Object

Create a named connection object that includes secure credentials for Azure ADLS
Storage in the identification field:

```sql
CREATE OR REPLACE CONNECTION AZURE_ADLS_CONNECTION
TO ''
USER ''
IDENTIFIED BY 'AZURE_CLIENT_ID=<AZURE_CLIENT_ID>;AZURE_CLIENT_SECRET=<AZURE_CLIENT_SECRET>;AZURE_DIRECTORY_ID=<AZURE_DIRECTORY_ID>';
```
A user that will run IMPORT or EXPORT UDF will need ACCESS privilige on this connection directly or via role. See [Privileges][exa-docs-privileges] and [Details on Rights Management][exa-docs-privileges-det] sections in the Exasol documentation for more details.

### Run Import Statement

```sql
IMPORT INTO <schema>.<table>
FROM SCRIPT CLOUD_STORAGE_EXTENSION.IMPORT_PATH WITH
  BUCKET_PATH     = 'adl://<AZURE_CONTAINER_NAME>.azuredatalakestore.net/import/avro/data/*'
  DATA_FORMAT     = 'AVRO'
  CONNECTION_NAME = 'AZURE_ADLS_CONNECTION'
  PARALLELISM     = 'nproc()*<MULTIPLIER>';
```

### Run Export Statement

```sql
EXPORT <schema>.<table>
INTO SCRIPT CLOUD_STORAGE_EXTENSION.EXPORT_PATH WITH
  BUCKET_PATH     = 'adl://<AZURE_CONTAINER_NAME>.azuredatalakestore.net/export/parquet/data/'
  DATA_FORMAT     = 'PARQUET'
  CONNECTION_NAME = 'AZURE_ADLS_CONNECTION'
  PARALLELISM     = 'iproc(), floor(random()*<MULTIPLIER>)';
```

## Azure Data Lake Gen2 Storage

Likewise, the Azure Data Lake Gen2 Storage requires a secret key of the storage
account for authentication.

```
AZURE_SECRET_KEY
```

Please refer to the Azure documentation on [creating storage
account][azure-blob-account] and managing [storage access
keys][azure-blob-keys].

### Create Exasol Connection Object

Create a named connection object that includes a secret key for the Azure Data
Lake Gen2 Storage in the identification field:

```sql
CREATE OR REPLACE CONNECTION AZURE_ABFS_CONNECTION
TO ''
USER ''
IDENTIFIED BY 'AZURE_SECRET_KEY=<AZURE_SECRET_KEY>';
```
A user that will run IMPORT or EXPORT UDF will need ACCESS privilige on this connection directly or via role. See [Privileges][exa-docs-privileges] and [Details on Rights Management][exa-docs-privileges-det] sections in the Exasol documentation for more details.

### Run Import Statement

```sql
IMPORT INTO <schema>.<table>
FROM SCRIPT CLOUD_STORAGE_EXTENSION.IMPORT_PATH WITH
  BUCKET_PATH     = 'abfs://<AZURE_CONTAINER_NAME>@<AZURE_ACCOUNT_NAME>.dfs.core.windows.net/import/orc/data/*'
  DATA_FORMAT     = 'ORC'
  CONNECTION_NAME = 'AZURE_ABFS_CONNECTION'
  PARALLELISM     = 'nproc()*<MULTIPLIER>';
```

### Run Export Statement

```sql
EXPORT <schema>.<table>
INTO SCRIPT CLOUD_STORAGE_EXTENSION.EXPORT_PATH WITH
  BUCKET_PATH     = 'abfss://<AZURE_CONTAINER_NAME>@<AZURE_ACCOUNT_NAME>.dfs.core.windows.net/export/parquet/data/'
  DATA_FORMAT     = 'PARQUET'
  CONNECTION_NAME = 'AZURE_ABFS_CONNECTION'
  PARALLELISM     = 'iproc(), floor(random()*<MULTIPLIER>)';
```

The bucket path should start with an `abfs` or `abfss` URI scheme.

## Delta Format

[Delta format][delta-io] is an open-source storage layer that brings ACID
transaction properties to Apache Spark and other blob storage systems.

Using the Exasol Cloud Storage Extension, it is now possible to import data from
the Delta format.

### Import Delta Formatted Data

Like other cloud storage systems, you can run the Exasol IMPORT SQL statement to
import the data from the Delta format.

Here is an example of import delta formatted data from Amazon S3:

```sql
CREATE OR REPLACE CONNECTION S3_CONNECTION
TO ''
USER ''
IDENTIFIED BY 'S3_ACCESS_KEY=<AWS_ACCESS_KEY>;S3_SECRET_KEY=<AWS_SECRET_KEY>';

IMPORT INTO <schema>.<table>
FROM SCRIPT CLOUD_STORAGE_EXTENSION.IMPORT_PATH WITH
  BUCKET_PATH     = 's3a://<S3_PATH>/import/delta/data/*'
  DATA_FORMAT     = 'DELTA'
  S3_ENDPOINT     = 's3.<REGION>.amazonaws.com'
  CONNECTION_NAME = 'S3_CONNECTION'
  PARALLELISM     = 'nproc()*<MULTIPLIER>';
```

### Supported Cloud Storage Systems

Currently, cloud-storage-extension supports importing delta formatted data from
Amazon S3, Azure Blob Storage and Azure Data Lake Storage Gen1 and Gen2 storage
systems.

You can read more about the supported storage requirements and configuration on
the [delta.io/delta-storage.html][delta-storage] page.

### Delta Snapshot

When running the import SQL statement, we first query the [latest
snapshot][delta-history] of the Delta format and only import the data from the
latest snapshot version. Thus, each import from the Delta format will import the
snapshot data to the Exasol table.

### Schema Evolution

Delta format supports schema evolution and the import statement queries the
latest schema defined in the Delta format. Therefore, users should update the
Exasol table schema manually before the import if the schema in the delta format
changes.

[delta-io]: https://delta.io/
[delta-storage]: https://docs.delta.io/latest/delta-storage.html
[delta-history]: https://docs.delta.io/latest/delta-utility.html#history

## Hadoop Distributed Filesystem (HDFS)

The [Hadoop distributed file system (HDFS)][hdfs-link] is a distributed,
scalable, and portable file system written in Java for the Hadoop framework

[hdfs-link]: https://hadoop.apache.org/docs/r1.2.1/hdfs_design.html

When the Hadoop datanodes and Exasol cluster are installed in the same (virtual)
network, you can access the HDFS using `cloud-storage-extension`.

For import:

```sql
IMPORT INTO <schema>.<table>
FROM SCRIPT CLOUD_STORAGE_EXTENSION.IMPORT_PATH WITH
  BUCKET_PATH     = 'hdfs://<HDFS_PATH>/import/orc/data/*.orc'
  DATA_FORMAT     = 'ORC'
  PARALLELISM     = 'nproc()*<MULTIPLIER>';
```

For export:

```sql
EXPORT <schema>.<table>
INTO SCRIPT CLOUD_STORAGE_EXTENSION.EXPORT_PATH WITH
  BUCKET_PATH     = 'hdfs://<HDFS_PATH>/export/parquet/data/'
  DATA_FORMAT     = 'PARQUET'
  PARALLELISM     = 'iproc(), floor(random()*<MULTIPLIER>)';
```

Because we assume that they are in the same private network, you do not have to
create a connection object.

At the moment, it is not possible to access HDFS using `cloud-storage-extension`
if the clusters are not located in the same private network.

## Alluxio Filesystem

[Alluxio](https://docs.alluxio.io/os/user/stable/en/Overview.html) is an open
source data orchestration technology for analytics and AI for the cloud. It
provides filesystem API similar to the HDFS.

You can import formatted data from Alluxio using the cloud-storage-extension.

```sql
IMPORT INTO <schema>.<table>
FROM SCRIPT CLOUD_STORAGE_EXTENSION.IMPORT_PATH WITH
  BUCKET_PATH     = 'alluxio://<ALLUXIO_PATH>/import/parquet/data/*'
  DATA_FORMAT     = 'PARQUET'
  PARALLELISM     = 'nproc()*<MULTIPLIER>';
```

For this to work, the Alluxio and Exasol clusters should be located in a same
(virtual) network. It is not possible to import if they are not within the same
private network.
