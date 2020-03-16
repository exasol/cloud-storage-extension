# Introduction

## Acknowledgements

This document's section structure is derived from the 
"[arc42](https://arc42.org/)" architectural template by Dr. Gernot Starke, 
Dr. Peter Hruschka.

## Terms and Abbreviations

<dl>
    <dt>EKC</dt><dd>Exasol Kinesis Connector</dd>
    <dt>Sequence number</dt><dd>An identifier
     associated with every record put into the stream.</dd>
    <dt>Shard</dt><dd>A uniquely identified 
    sequence of data records in a stream.</dd>
    <dt>Stream</dt><dd>A Kinesis data stream is a set of shards.</dd>
    <dt>Record</dt><dd>The unit of data of the Kinesis data stream.</dd>
    <dt>UDF</dt><dd>Used-defined function. UDF scripts provides you the ability 
    to program your own analysis, processing, or generation functions, 
    and execute them in parallel inside Exasol</dd>
</dl>

# Solution Strategy

AWS Kinesis Data Streams provide an 
[API][kinesis-api] for consuming data from streams. 
EKC is an IMPORT UDF script that consumes 
data coming from a Kinesis Stream and imports it to an Exasol table. 

## Requirement Overview

Please refer to the [System Requirement Specification](system_requirements.md) 
for user-level requirements.

# Building Blocks

This section introduces the building blocks of the software.
Together those building blocks make up the big picture of the software structure.

## UDF Functions

Kinesis Connector uses a set of UDFs to import the data. 

### `KINESIS_METADATA`

Emits a list of available shards from a stream.

### `KINESIS_IMPORT`

Emits data from a single shard.

### `KINESIS_PATH`

Uses `KINESIS_METADATA` and `KINESIS_PATH` internally to generate a single
import statement.

## Scriptclasses Implementation

The scriptclasses contain logic that the UDF functions use.

### `KinesisShardsMetadataReader`

The `KinesisShardsMetadataReader` is responsible for reading shards' metadata
from a Stream.
 
### `KinesisShardDataImporter`

The `KinesisShardDataImporter` consumes the data from a single shard.

### `KinesisImportQueryGenerator`

The `KinesisImportQueryGenerator` generates an import query that we use for
an IMPORT UDF. 

## Table for Importing

We request a manually created table for importing data for each stream.
Users create a table imitating the data types of the data
stored in the stream and also place columns in the exact order. 

Two last columns of the table have names `KINESIS_SHARD_ID` and `SHARD_SEQUENCE_NUMBER`. 
We use them for storing metadata. `KINESIS_SHARD_ID` is a string with a shard's name where 
the data came from. `SHARD_SEQUENCE_NUMBER` is a string with a unique number that identifies 
a record in a stream.

# Runtime

This section describes the runtime behavior of the software.

## `KinesisShardsMetadataReader` Reads Shards' Metadata
`dsn~kinesisshardsmetadatareader-reads-shards-metadata~1`

The `KinesisShardsMetadataReader` gets all available shards from a stream
and also check if the user-defined table contains any metadata.

It emits a list of available shards accompanied by sequence numbers of rows 
to start consuming from (if the numbers are available in the import table). 

Covers:

* `req~connect-to-kinesis-streams~1`

Needs: impl, itest

## `KinesisShardDataImporter` Imports Data from Shard
`dsn~kinesissharddataimporter-imports-data-from-shard~1`

The `KinesisShardDataImporter` reads and emits data from a single shard.
If shard's metadata has a sequence number, it consumes from the next available record.
Otherwise, it consumes from the start of the stream.

Covers:

* `req~connect-to-kinesis-streams~1`

Needs: impl, itest

## `KinesisImportQueryGenerator` Generates an Import Query
`dsn~kinesisimportquerygenerator-generates-an-import-query~1`

The `KinesisImportQueryGenerator` implements a `generateSqlForImportSpec` 
function and returns a SELECT statement which a UDF script uses in INSERT
command.

Covers:

* `req~transfer-data-to-an-exasol-table~1`

Needs: impl, itest

## `KinesisImportQueryGenerator` Runs as a Single Transaction
`dsn~kinesisimportquerygenerator-runs-as-single-transaction~1`

The `KinesisImportQueryGenerator` generates a single statement for an IMPORT.
That means the script commits the data only when a transaction is successful.
In case of an error during the transaction, with the next run we start consuming 
from the same sequence number.  

Covers:

* `req~transfer-without-loosing-data~1`

Needs: impl, itest

## `KinesisShardDataImporter` Add Metadata to Each Row
`dsn~KinesisShardDataImporter-add-metadata-to-each-row~1`

The `KinesisShardDataImporter` emits two additional columns for each
 row of importing data: `KINESIS_SHARD_ID` and `SHARD_SEQUENCE_NUMBER`.

Covers:

* `req~import-without-duplicates~1`

Needs: impl, itest

## `KinesisImportQueryGenerator` Starts Consuming From New Data Records
`dsn~KinesisImportQueryGenerator-starts-consuming-from-new-data-records~1`

The `KinesisImportQueryGenerator` looks for the last read record in  
`SHARD_SEQUENCE_NUMBER` column for each shard in `KINESIS_SHARD_ID`. 

If the table is empty or mentioned columns do not contain any data, it starts
 consuming from the oldest available record.

Covers:

* `req~import-without-duplicates~1`

Needs: impl, itest

[kinesis-api]: https://docs.aws.amazon.com/kinesis/latest/APIReference/Welcome.html