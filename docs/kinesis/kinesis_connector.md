# Exasol Kinesis Connector 

Exasol Kinesis Connector provides UDF scripts which allow users import data
from a Kinesis Stream to an Exasol table.

## Prerequisites

* A running Exasol cluster with a version 6.0 or later.
* An AWS account with all necessary permissions to read from a Kinesis Stream.
* An AWS Kinesis producer which sends the data to a stream in the valid JSON
 format. 
 An example of a valid JSON string: 
 `{"sensorId": 17,"currentTemperature": 147,"status": "WARN"}`.
 An example of an invalid JSON format:
 `{"sensorId": 17,"currentTemperature": 147,"status": "WARN",}`. The Exasol
 Kinesis Connector will not parse this string correctly.

## Deployment

Please refer to the [deployment guide](../deployment_guide.md) till the section
`Upload the JAR file to the bucket` (inclusive) to prepare a bucket and a JAR.

## Preparing a table for data

To store the data from a stream we need a table. 
The columns in the Exasol table have to imitate the data types of the data
 stored in the stream and also be in the exact same order.
 
The table also requires two additional columns to store the Kinesis metadata:  
    `KINESIS_SHARD_ID VARCHAR(2000), 
     SHARD_SEQUENCE_NUMBER VARCHAR(2000)`
These two columns must be in the end of the table.
 
For example, given the following stream data, 
`{"sensorId": 17,"currentTemperature": 147,"status": "WARN"}`, 
the Exasol table should be as below:

```sql
OPEN SCHEMA <schema_name>;

CREATE OR REPLACE TABLE <schema_name>.<table_name> 
    (SENSORID DECIMAL(18,0), 
    CURRENTTEMPERATURE DECIMAL(18,0), 
    STATUS VARCHAR(100), 
-- Required for importing data from Kinesis
    KINESIS_SHARD_ID VARCHAR(2000), 
    SHARD_SEQUENCE_NUMBER VARCHAR(2000));

```

## Create ETL UDFs Scripts

Create the following UDF scripts. Please do not change the names of the scripts.

```sql
--/
CREATE OR REPLACE JAVA SET SCRIPT KINESIS_METADATA (...) 
EMITS (KINESIS_SHARD_ID VARCHAR(130), SHARD_SEQUENCE_NUMBER VARCHAR(2000)) AS
  %scriptclass com.exasol.cloudetl.kinesis.KinesisShardsMetadataReader;
  %jar /buckets/bfsdefault/kinesis/cloud-storage-etl-udfs-<VERSION>.jar;
/
;
  
--/
CREATE OR REPLACE JAVA SET SCRIPT KINESIS_IMPORT (...) EMITS (...) AS
  %scriptclass com.exasol.cloudetl.kinesis.KinesisShardDataImporter;
  %jar /buckets/bfsdefault/kinesis/cloud-storage-etl-udfs-<VERSION>.jar;
/
;
    
--/
CREATE OR REPLACE JAVA SET SCRIPT KINESIS_PATH (...) EMITS (...) AS
  %scriptclass com.exasol.cloudetl.kinesis.KinesisImportQueryGenerator;
  %jar /buckets/bfsdefault/kinesis/cloud-storage-etl-udfs-<VERSION>.jar;
/
; 
```

## Import data

Run an import query. All properties values are mandatory.
 
```sql
IMPORT INTO <table_name>
FROM SCRIPT KINESIS_PATH WITH
  TABLE_NAME     = '<table_name>'
  AWS_ACCESS_KEY  = '<access_key>'
  AWS_SECRET_KEY  = '<secret_key>'
  AWS_SESSION_TOKEN  = '<session_token>'
  STREAM_NAME    = '<stream_name>'
  REGION    = '<region>'
;
``` 

### Properties

Property name      | Required    | Description
-------------------|-------------|--------------------------------------------
TABLE_NAME         |  Mandatory  | A name of an Exasol table to store the data.                   
AWS_ACCESS_KEY     |  Mandatory  | An AWS access key id                    
AWS_SECRET_KEY     |  Mandatory  | An AWS secret key                   
AWS_SESSION_TOKEN  |  Mandatory  | An AWS session token                   
STREAM_NAME        |  Mandatory  | A name of the stream to consume the data from                   
REGION             |  Mandatory  | An AWS region where a stream is localed                   
