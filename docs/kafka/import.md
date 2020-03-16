# Kafka Topic Import

In this guide you can find instructions to import Avro data from Apache Kafka
clusters.

## Table of contents

- [Prerequisites](#prerequisites)
- [Import from Kafka](#import-from-kafka-cluster)
  - [Usage example](#usage)
- [Secure connection to Kafka](#secure-connection-to-kafka-cluster)
  - [Usage example with SSL enabled](#usage-with-ssl-enabled)

## Prerequisites

Before start, you should deploy the latest jar file to the Exasol BucketFS
bucket and create UDF scripts for importing Avro data from Kafka cluster.
Please follow the instructions from [deployment guide](../deployment_guide.md)
if you have not done so.

Additionally, you can read the [user guide](../user_guide.md) in order to get
familiar with cloud-storage-etl-udfs in general.

In this guide, we will be using `RETAIL` schema and `SALES_POSITIONS` table to
import data into. However, we have to change previous `SALES_POSITIONS` table so
that it includes two additional columns that will help us to manage Kafka topic
partition offset in Exasol table.

Please change (or create) the Exasol table as below:

```sql
OPEN SCHEMA RETAIL;

CREATE OR REPLACE TABLE SALES_POSITIONS (
    -- Required for Kafka import UDF
    KAFKA_PARTITION DECIMAL(18, 0),
    KAFKA_OFFSET DECIMAL(36, 0),

    -- These columns match the Kafka topic schema
    SALES_ID    INTEGER,
    POSITION_ID SMALLINT,
    ARTICLE_ID  SMALLINT,
    AMOUNT      SMALLINT,
    PRICE       DECIMAL(9,2),
    VOUCHER_ID  SMALLINT,
    CANCELED    BOOLEAN
);
```

Please make sure that the Exasol table column names and types (except
`kafka_partition` and `kafka_offset`) match the Kafka topic schema.

## Import from Kafka cluster

There are several property values that are required in order to access the Kafka
cluster when importing data from Kafka topics using cloud-storage-etl-udfs.

- ``BOOTSTRAP_SERVERS``
- ``SCHEMA_REGISTRY_URL``
- ``TOPICS``
- ``TABLE_NAME``

The **BOOTSTRAP_SERVERS** is a comma separated list of host port pairs used to
establish initial connection to the Kafka cluster. The UDF consumer client will
contact all servers in Kafka cluster, irrespective of servers specified with
this parameter. This list only defines initial hosts used to discover full list
of Kafka servers.

The **SCHEMA_REGISTRY_URL** is an URL to the [Schema Registry][schema-registry]
server. It provides a serving layer for the storing, managing and retrieving
Avro schemas.

The **TOPICS** is the name of the Kafka topic we want to import Avro data from.
Please note that even though it is in plural form, curently only single topic
data imports are supported.

The **TABLE_NAME** is the Exasol table name that we are going to import Kafka
topic data.

For more information on Kafka import parameters, please refer to the [Kafka
import properties in the user guide](../user_guide.md#kafka-import-properties).

### Usage

```sql
IMPORT INTO RETAIL.SALES_POSITIONS
FROM SCRIPT ETL.KAFKA_PATH WITH
  BOOTSTRAP_SERVERS   = 'kafka01.internal:9092,kafka02.internal:9093,kafka03.internal:9094'
  SCHEMA_REGISTRY_URL = 'http://schema-registry.internal:8081'
  TOPICS              = 'SALES-POSITIONS'
  TABLE_NAME          = 'RETAIL.KAFKA_TOPIC_SALES_POSITIONS'
  GROUP_ID            = 'exasol-kafka-udf-consumers';
```

## Secure connection to Kafka cluster

Since the recent releases, Apache Kafka supports authentication of connections
to Kafka brokers from clients (producers and consumers) using either SSL or
SASL. Currently, cloud-storage-etl-udfs Kafka UDF only supports **SSL**.

In order to use the secure connections to Kafka cluster from the UDF, you need
to upload the consumer truststore and keystore JKS files to Exasol BucketFS
bucket so that we can access them when running the Kafka import UDF.

Upload the consumer JKS files:

```bash
# Upload consumer client truststore JKS file
curl -X PUT -T certs/kafka.consumer.truststore.jks \
  http://w:write-password@exasol.datanode.domain.com:2580/<BUCKET_NAME>/kafka.consumer.truststore.jks

# Upload consumer client keystore JKS file
curl -X PUT -T certs/kafka.consumer.keystore.jks \
  http://w:write-password@exasol.datanode.domain.com:2580/<BUCKET_NAME>/kafka.consumer.keystore.jks
```

Please check out the Apache Kafka documentation on [security][kafka-security]
and [Kafka client configurations][kafka-secure-clients] for more information.

Additionally, we have provide extra parameters to the UDF in order to enable
secure connection to Kafka cluster. Please check out the [Kafka import
properties in the user guide](../user_guide.md#kafka-import-properties) for
secure property descriptions.

### Usage with SSL enabled

```sql
IMPORT INTO RETAIL.SALES_POSITIONS
FROM SCRIPT ETL.KAFKA_PATH WITH
  BOOTSTRAP_SERVERS       = 'kafka01.internal:9092,kafka02.internal:9093,kafka03.internal:9094'
  SCHEMA_REGISTRY_URL     = 'http://schema-registry.internal:8081'
  TOPICS                  = 'SALES-POSITIONS'
  TABLE_NAME              = 'RETAIL.KAFKA_TOPIC_SALES_POSITIONS'
  GROUP_ID                = 'exasol-kafka-udf-consumers';
  -- Secure connection properties
  SSL_ENABLED             = 'true'
  SECURITY_PROTOCOL       = 'SSL'
  SSL_KEY_PASSWORD        = '<SSL_KEY_PASSWORD>'
  SSL_TRUSTSTORE_LOCATION = '/buckets/bfsdefault/<BUCKET_NAME>/kafka.consumer.truststore.jks'
  SSL_TRUSTSTORE_PASSWORD = '<CONSUMER_TRUSTSTORE_PASSWORD>'
  SSL_KEYSTORE_LOCATION   = '/buckets/bfsdefault/<BUCKET_NAME>/kafka.consumer.keystore.jks'
  SSL_KEYSTORE_PASSWORD   = '<CONSUMER_KEYSTORE_PASSWORD>';
```

[schema-registry]: https://docs.confluent.io/current/schema-registry/index.html
[kafka-security]: https://kafka.apache.org/documentation/#security
[kafka-secure-clients]: https://kafka.apache.org/documentation/#security_configclients
