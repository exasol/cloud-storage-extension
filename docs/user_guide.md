# User Guide

This section provides short instructions and examples so that you can get
started with using the cloud-storage-etl-udfs.

## Table of contents

- [Deployment Guide](#deployment-guide)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Properties](#properties)
  - [Storage Properties](#storage-properties)
  - [Kafka Import Properties](#kafka-import-properties)

## Deployment guide

In order to use the cloud-storage-etl-udfs, you should deploy the jar file and
create UDF scripts.

Please follow the steps from [deployment guide](./deployment_guide.md).

## Getting started

The cloud-storage-etl-udfs allows you to import data from external cloud storage
filesystems or Apache Kafka cluster into Exasol table. Similarly, it allows you
to export data from Exasol table to cloud storage filesystems.

Therefore, the next step is to create a table inside the Exasol.

In this guide, we are going to create a `RETAIL` schema with `SALES_POSITIONS`
table. Then we will use this table to demonstrate the import and export
features of the cloud-storage-etl-udfs.

Create a retail schema:

```sql
CREATE SCHEMA RETAIL;
```

Then create a sales positions table:

```sql
OPEN SCHEMA RETAIL;

CREATE OR REPLACE TABLE SALES_POSITIONS (
    SALES_ID    INTEGER,
    POSITION_ID SMALLINT,
    ARTICLE_ID  SMALLINT,
    AMOUNT      SMALLINT,
    PRICE       DECIMAL(9,2),
    VOUCHER_ID  SMALLINT,
    CANCELED    BOOLEAN
);
```

## Usage

Here we are going to show an excerpt from a simple example that will import Avro
formatted data from Azure Blob Storage into `SALES_POSITIONS` table and export
from it into Amazon S3 bucket as Parquet formatted data.

Import Avro data from Azure Blob store container:

```sql
IMPORT INTO RETAIL.SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH        = 'wasbs://<CONTAINER>@<AZURE_ACCOUNT_NAME>.blob.core.windows.net/data/avro/sales-positions/*'
  DATA_FORMAT        = 'AVRO'
  AZURE_ACCOUNT_NAME = '<AZURE_ACCOUNT_NAME>'
  AZURE_SECRET_KEY   = '<AZURE_SECRET_KEY>'
  PARALLELISM        = 'nproc()';
```

The same can be achieved by using the secret key connection object. Please check the [supported cloud storage systems](storage/cloud_storages.md) for more details.

```sql
IMPORT INTO RETAIL.SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH        = 'wasbs://<CONTAINER>@<AZURE_ACCOUNT_NAME>.blob.core.windows.net/data/avro/sales-positions/*'
  DATA_FORMAT        = 'AVRO'
  CONNECTION_NAME  = '<PREDEFINED_CONNECTION>'
  PARALLELISM      = 'nproc()';
```

When running the import, it will first execute the `IMPORT_METADATA` udf in
order to calculate the number of files in the user provided path. Then it will
distribute these files in round-robin fashion into number of `PARALLELISM`
virtual machines that then will be importing data from their list of files.

In the example above, `PARALLELISM` property value is set to `nproc()` which
defines the number of physical datanodes in the Exasol cluster. Therefore, each
datanode will be running single virtual machine (VM) to import data from files.

However, you can increase the parallelism by setting it to different value. For
example, in order to start four VMs in each datanode, you can set the
`PARALLELISM` to `nproc()*4`. Or similarly, to higher static number as
`PARALLELISM = '16'` that will use 16 virtual machines in total.

Once the process is finished, make sure that data is imported:

```sql
SELECT * FROM retail.sales_positions LIMIT 10;

SELECT count(*) FROM retail.sales_positions;
```

Similarly, to export table rows into Amazon S3 bucket as Parquet data:

```sql
EXPORT RETAIL.SALES_POSITIONS
INTO SCRIPT ETL.EXPORT_PATH WITH
  BUCKET_PATH    = 's3a://<S3_BUCKET>/data/parquet/sales_positions/'
  DATA_FORMAT    = 'PARQUET'
  S3_ACCESS_KEY  = '<AWS_ACCESS_KEY>'
  S3_SECRET_KEY  = '<AWS_SECRET_KEY>'
  S3_ENDPOINT    = 's3.<REGION>.amazonaws.com'
  PARALLELISM    = 'iproc(), floor(random()*4)';
```

Under the hoods, the `EXPORT_PATH` entry point UDF will call the `EXPORT_TABLE`
user defined function we created in the deployment guide.

You might have noticed the different value for `PARALLELISM` in the export SQL
statement. This is due to fact that when exporting an Exasol table, the
`PARALLELISM` property value will be used internally in a `GROUP BY` clause to
distribute table rows into many virtual machines.

In the example above, `iproc()` will return the sequence numbers of Exasol
datanodes. Thus, setting parallelism to `iproc()` only will create a single VM
per each physical datanode. Therefore, we can add additional number to `iproc()`
to increase parallel running VMs for exporting data.

Please be aware that setting the `PARALLELISM` to a static number, for instance
`16` or `nproc()`, when exporting will only start a single virtual machine in
total. 

Finally, please change the placeholder parameters accordingly for your use case.

## Properties

Several properties should be provided by users in order `cloud-storage-etl-udfs`
to work properly. As you have seen from examples they are required parameters
such as path to storage filesystem, access control credentials or optional
parameters such as parallelism.

Please note that properties are provided as strings, `S3_ENDPOINT =
's3.eu-central-1.amazonaws.com'`.

### Storage Properties

These property names related to accessing various cloud storage filesystems for
importing or exporting data.

#### Required Properties

* ``BUCKET_PATH`` - It specifies a path to the cloud storage filesystem.
  Additionally, it should start with storage specific schema, such as `s3a`. You
  can check out the currently [supported list of cloud storage
  systems](storage/cloud_storages.md).

* ``DATA_FORMAT`` - It defines the data file format in the user provided path.
  Currently, we support importing data from **Avro**, **Orc** and **Parquet**
  file formats and exporting to only **Parquet** format.

* Additional storage related properties that enable accessing storage
  filesystems. Please refer to the [supported cloud storage
  systems](storage/cloud_storages.md) documentation for more information.

#### Optional Properties

These are optional parameters that usually have default values.

* ``PARALLELISM`` - It defines the number of parallel virtual-machine instances
  that will be started to import or export data. Default value in import SQL
  statement: `nproc()`. Default value in export SQL statement: `iproc()`. As
  mentioned before, you can changes these values to increase the parallelism.
  For example, multiply with a number when importing `PARALLELISM = 'nproc()*4'`
  or append additional numbers when exporting `PARALLELISM = 'iproc(),
  floor(random()*4)'`. Please check out the [supported cloud storage
  systems](storage/cloud_storages.md) for more examples.

* ``PARQUET_COMPRESSION_CODEC`` - This property is only used in export SQL
  statement. It defines the compression codec to use when exporting data into
  Parquet formatted files. Default value is **uncompressed**. Other compression
  options are **snappy**, **gzip** and **lzo**.

* ``EXPORT_BATCH_SIZE`` - This property is only used in export SQL statement. It
  defines the number of records per file from each virtual machine. Default
  value is **100000**. That is, if a single VM gets `1M` rows to export, it will
  create 10 files with default value 100000 records in each file.

### Kafka Import Properties

The following properties are related to UDFs when importing data from Kafka
clusters. Most of these properties are exactly same as [Kafka consumer
configurations][kafka-consumer-configs].

#### Required Properties

* ``BOOTSTRAP_SERVERS`` - It is a comma separated host-port pairs of Kafka
  brokers. These addresses will be used to establish the initial connection to
  the Kafka cluster.

* ``SCHEMA_REGISTRY_URL`` - It specifies an URL to the Confluent [Schema
  Registry][schema-registry] which stores Avro schemas as metadata. Schema
  Registry will be used to parse the Kafka topic Avro data schemas.

* ``TOPICS`` - It defines Kafka topic name that we want to import data from.
  Currently, we only support single topic data imports. Therefore, it should not
  contain comma separated list of more than one topic names.

* ``TABLE_NAME`` - It defines the Exasol table name the data will be imported.
  This is required as user provided parameter since unfortunately we cannot
  obtain table name from inside UDF even though we are importing data into it.

Please note that we do not have `PARALLELISM` when importing from Kafka cluster.
The number of parallel running virtual machine instances are defined by the
Kafka topic partitions. That is, when importing data from Kafka topic, we will
be importing from each topic partition in parallel. Therefore, it is important
to configure Kafka topics with several partitions.

Please check out the [Kafka import examples](kafka/import.md) for more
information.

#### Optional Properties

These are optional parameters with their default values.

* ``GROUP_ID`` - It defines the id for this type of consumers. Default value is
  **EXASOL_KAFKA_UDFS_CONSUMERS**. It is a unique string that identifies the
  consumer group this consumer belongs to.

* ``POLL_TIMEOUT_MS`` - It defines the timeout value that is the number of
  milliseconds to wait for the consumer poll function to return any data.
  Default value is **30000** milliseconds.

* ``MIN_RECORDS_PER_RUN`` - It is an upper bound on the minimum number of
  records to consumer per UDF run. Default value is **100**. That is, if the
  pull function returns fewer records than this number, we consume returned
  records and finish the UDF process. Otherwise, we continue polling more data
  until total number of records reaches certain threshold, for example,
  `MAX_RECORD_PER_RUN`.

* ``MAX_RECORD_PER_RUN`` - It is a lower bound on the maximum number of records
  to consumer per UDF run. Default value is **1000000**. When the returned
  number of records from poll is more than `MIN_RECORDS_PER_RUN`, we continue
  polling for more records until total number reaches this number.

* ``MAX_POLL_RECORDS`` - It is the maximum number of records returned in a
  single call from the consumer poll method. Default value is **500**.

* ``FETCH_MIN_BYTES`` - It is the minimum amount of data the server should
  return for a fetch request. If insufficient data is available the request will
  wait for that much data to accumulate before answering the request. Default
  value is **1**.

* ``FETCH_MAX_BYTES`` - It is the maximum amount of data the server should
  return for a fetch request. Default value is **52428800**.

* ``MAX_PARTITION_FETCH_BYTES`` - It is the maximum amount of data per
  partition the server will return. Default value is **1048576**.

The following properties should be provided to enable secure connection to the
Kafka clusters.

* ``SSL_ENABLED`` - It is a boolean property that should be set to `true` in
  order to use the secure connections to the Kafka cluster. Default value is
  **'false'**.

* ``SECURITY_PROTOCOL`` - It is the protocol used to communicate with Kafka
  servers. Default value is **PLAINTEXT**.

* ``SSL_KEY_PASSWORD`` - It represents the password of the private key inside
  the keystore file.

* ``SSL_KEYSTORE_PASSWORD`` - It the store password for the keystore file.

* ``SSL_KEYSTORE_LOCATION`` - It represents the location of the keystore file.
  This location value should point to the keystore file that is available via
  Exasol bucket in BucketFS.

* ``SSL_TRUSTSTORE_PASSWORD`` - It is the password for the truststore file.

* ``SSL_TRUSTSTORE_LOCATION`` - It is the location of the truststore file, and
  it should refer to the truststore file stored inside bucket in Exasol
  BucketFS.

* ``SSL_ENDPOINT_IDENTIFICATION_ALGORITHM`` - It is the endpoint identification
  algorithm to validate server hostname using server certificate. Default value
  is **https**.

[schema-registry]: https://docs.confluent.io/current/schema-registry/index.html
[kafka-consumer-configs]: https://kafka.apache.org/documentation/#consumerconfigs
