# Delta Format

[Delta format][delta-io] is an open-source storage layer that brings ACID
transaction properties to Apache Spark and other blob storage systems.

Using the cloud-storage-etl-udfs, it is now possible to import data from the
Delta format.

## Import Delta formatted data

Like other cloud storage systems, you can run the Exasol IMPORT SQL statement to
import the data from the Delta format.

Here is an example to import delta formatted data from Amazon S3:

```sql
CREATE OR REPLACE CONNECTION S3_CONNECTION
TO ''
USER ''
IDENTIFIED BY 'S3_ACCESS_KEY=<AWS_ACCESS_KEY>;S3_SECRET_KEY=<AWS_SECRET_KEY>';

IMPORT INTO RETAIL.SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH     = 's3a://<BUCKET>/import/delta/sales_positions/*'
  DATA_FORMAT     = 'DELTA'
  S3_ENDPOINT     = 's3.<REGION>.amazonaws.com'
  CONNECTION_NAME = 'S3_CONNECTION'
  PARALLELISM     = 'nproc()*2';
```

## Supported Cloud Storage Systems

Currently, only Amazon S3, Azure Blob Storage and Azure Data Lake Storage Gen1
cloud storage systems are supported.

You can read more about the supported storage requirements and configuration on
the [delta.io/delta-storage.html][delta-storage] page.

## Delta Snapshot

When running the import SQL statement, we first query the [latest
snapshot][delta-history] of the Delta format, and only import the data from the
latest snapshot version. Thus, each import from the Delta format will import
data to the Exasol table.

## Schema Evolution

Delta format supports schema evolution and the import statement queries the
latest schema defined in the Delta format. Therefore, users should update the
Exasol table schema manually before the import if the schema in the delta format
changes.

[delta-io]: https://delta.io/
[delta-storage]: https://docs.delta.io/latest/delta-storage.html
[delta-history]: https://docs.delta.io/latest/delta-utility.html#history
