# Introduction

## Acknowledgements

This document's section structure is derived from the 
"[arc42](https://arc42.org/)" architectural template by Dr. Gernot Starke, 
Dr. Peter Hruschka.

## Terms and Abbreviations

<dl>
    <dt>EKC</dt><dd>Exasol Kinesis Connector</dd>
    <dt>UDF</dt><dd>Used-defined function</dd>
</dl>

# Solution Strategy

AWS Kinesis Data Streams provides an [API](https://docs.aws.amazon.com
/kinesis/latest/APIReference/Welcome.html) for consuming data from streams
. EKC is an IMPORT UDF script that consume the data from Kinesis Streams and
import it to an Exasol table. 

## Requirement Overview

Please refer to the [System Requirement Specification](system_requirements.md) 
for user-level requirements.

# Building Blocks

This section introduces the building blocks of the software.
Together those building blocks make up the big picture of the software structure.

## UDF functions

Kinesis Connector uses a set of UDF to import the data. 

### `KINESIS_METADATA`

Emits a list of available shards from a stream.

### `KINESIS_IMPORT`

Emits data from a single shard.

### `KINESIS_PATH`

Uses `KINESIS_METADATA` and `KINESIS_PATH` internally to generate a single
import statement.

## Scriptclasses implementation

The scriptclasses contain logic that UDF function use.

### `KinesisShardsMetadataReader`

The `KinesisShardsMetadataReader` is responsible for reading shards' metadata
from a Stream.
 
### `KinesisShardDataImporter`

The `KinesisShardDataImporter` consumes the data from a single shard.

### `KinesisImportQueryGenerator`

The `KinesisImportQueryGenerator` generates an import query that we use for
an IMPORT UDF. 

# Runtime

This section describes the runtime behavior of the software.

## `KinesisShardsMetadataReader` reads shards' metadata
`dsn~kinesisshardsmetadatareader-reads-shards-metadata~1`

The `KinesisShardsMetadataReader` implements a `run` function and emits a
list of enabled shards.

Covers:

* `req~connect-to-kinesis-streams~1`

Needs: impl, itest

## `KinesisShardDataImporter` imports data from shard
`dsn~kinesissharddataimporter-imports-data-from-shard~1`

The `KinesisShardDataImporter` reads and emits data from a single shard. It
also implements a `run` function.

Covers:

* `req~connect-to-kinesis-streams~1`

Needs: impl, itest

## `KinesisImportQueryGenerator` generates an import query
`dsn~kinesisimportquerygenerator-generates-an-import-query~1`

The `KinesisImportQueryGenerator` implements a `generateSqlForImportSpec` 
function and returns a SELECT statement which a UDF script uses in INSERT
command.

Covers:

* `req~transfer-data-to-an-exasol-table~1`

Needs: impl, itest

## `KinesisImportQueryGenerator` runs as single transaction
`dsn~kinesisimportquerygenerator-runs-as-single-transaction~1`

The `KinesisImportQueryGenerator` generates a single statement for an IMPORT.
That means the script commits the data only when a transaction is successful.
Otherwise we read the data from the same place with the next run.

Covers:

* `req~transfer-without-loosing-data~1`

Needs: impl, itest

## `KinesisShardDataImporter` add metadata to each row
`dsn~KinesisShardDataImporter-add-metadata-to-each-row~1`

The `KinesisShardDataImporter` emit two additional columns for each
 row of importing data: `KINESIS_SHARD_ID` and `SHARD_SEQUENCE_NUMBER`.

Covers:

* `req~import-without-duplicates~1`

Needs: impl, itest

## `KinesisImportQueryGenerator` starts consuming from new data records
`dsn~KinesisImportQueryGenerator-starts-consuming-from-new-data-records~1`

The `KinesisImportQueryGenerator` looks for the last read record in  
`SHARD_SEQUENCE_NUMBER` column for each shard in `KINESIS_SHARD_ID`. 

If the table is empty or mentioned columns do not contain any data, it starts
 consuming from the oldest available record.

Covers:

* `req~import-without-duplicates~1`

Needs: impl, itest