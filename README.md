# Exasol S3 ETL User Defined Functions

S3 ETL user defined functions are main way to transfer data between AWS S3 and
Exasol.

**Supported Formats**:

- Parquet (only primitive types)

## Usage

- Create assembly jar

```bash
./sbtx assembly
```

The package jar is located at `target/scala-2.11/s3etl-{VERSION}.jar`.

- Upload the jar to a bucket in Exasol BucketFS

```bash
curl \
  -X PUT \
  -T target/scala-2.11/s3etl-{VERSION}.jar \
  http://w:MY-PASSWORD@DATA-NODE-ID:2580/bucket1/s3et-{VERSION}.jar
```

Please change required parameters.

- Create ETL Scripts

```sql
CREATE SCHEMA ETL;
OPEN SCHEMA ETL;

--- S3 ETL SCRIPTS

CREATE OR REPLACE JAVA SET SCRIPT IMPORT_S3_PATH(...) EMITS (...) AS
%scriptclass com.exasol.s3etl.scriptclasses.ImportS3Path;
%jar /buckets/bfsdefault/bucket1/s3etl-{VERSION}.jar;
/

CREATE OR REPLACE JAVA SET SCRIPT IMPORT_S3_FILES(...) EMITS (...) AS
%env LD_LIBRARY_PATH=/tmp/;
%scriptclass com.exasol.s3etl.scriptclasses.ImportS3Files;
%jar /buckets/bfsdefault/bucket1/s3etl-{VERSION}.jar;
/

CREATE OR REPLACE JAVA SCALAR SCRIPT IMPORT_S3_METADATA(...)
EMITS (s3_filename VARCHAR(200), partition_index VARCHAR(100)) AS
%scriptclass com.exasol.s3etl.scriptclasses.ImportS3Metadata;
%jar /buckets/bfsdefault/bucket1/s3etl-{VERSION}.jar;
/
```

- Import data

```bash
CREATE SCHEMA TEST;
OPEN SCHEMA TEST;

DROP TABLE IF EXISTS SALES_POSITIONS;

CREATE TABLE SALES_POSITIONS (
  SALES_ID    DECIMAL(18,0),
  POSITION_ID DECIMAL(9,0),
  ARTICLE_ID  DECIMAL(9,0),
  AMOUNT      DECIMAL(9,0),
  PRICE       DECIMAL(9,2),
  VOUCHER_ID  DECIMAL(9,0),
  CANCELED    BOOLEAN
);

-- ALTER SESSION SET SCRIPT_OUTPUT_ADDRESS='10.0.2.162:3000';

IMPORT INTO SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_S3_PATH WITH
 S3_BUCKET_PATH = 's3a://exa-mo-frankfurt/test/retail/sales_positions/*'
 S3_ACCESS_KEY  = 'MY_AWS_ACCESS_KEY'
 S3_SECRET_KEY  = 'MY_AWS_SECRET_KEY'
 PARALLELISM    = 'nproc()*10';


SELECT * FROM SALES_POSITIONS LIMIT 10;
```
