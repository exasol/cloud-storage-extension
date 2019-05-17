# Cloud Storage ETL UDFs

Here we provide the instructions to setup or configure each of supported cloud
storage systems in order to transfer the data stored in different formats.

## Table of contents

- [Prerequisite](#prerequisite)
- [Amazon S3](#amazon-s3)
- [Google Cloud Storage](#google-cloud-storage)
- [Azure Blob Storage](#azure-blob-storage)
- [Azure Data Lake (Gen1) Storage](#azure-blob-storage)

## Prerequisite

- Deploy the latest jar into Exasol BucketFS bucket
- Setup the ETL user defined function scripts
- Make sure an example Exasol table is available

## Amazon S3

In order to access the Amazon S3 bucket you should provide the following
parameters to the cloud storage etl udfs scripts.

- `BUCKET_PATH`, that starts with ``s3a``
- `S3_ACCESS_KEY`
- `S3_SECRET_KEY`
- `S3_ENDPOINT`, for example, ``s3.eu-central-1.amazonaws.com``

Please follow the [Amazon credentials management best
practices](https://docs.aws.amazon.com/general/latest/gr/aws-sec-cred-types.html).

### Import from S3

```sql
IMPORT INTO SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH    = 's3a://bucket-path/parquet/retail/sales_positions/*'
  DATA_FORMAT    = 'PARQUET'
  S3_ACCESS_KEY  = 'MY_AWS_ACCESS_KEY'
  S3_SECRET_KEY  = 'MY_AWS_SECRET_KEY'
  S3_ENDPOINT    = 's3.MY_REGION.amazonaws.com'
  PARALLELISM    = 'nproc()';
```

### Export to S3

```sql
EXPORT SALES_POSITIONS
INTO SCRIPT ETL.EXPORT_PATH WITH
  BUCKET_PATH    = 's3a://bucket-path/parquet/retail/sales_positions/'
  S3_ACCESS_KEY  = 'MY_AWS_ACCESS_KEY'
  S3_SECRET_KEY  = 'MY_AWS_SECRET_KEY'
  S3_ENDPOINT    = 's3.MY_REGION.amazonaws.com'
  PARALLELISM    = 'iproc(), floor(random()*4)';
```

## Google Cloud Storage

In order to read data from Google Cloud Storage, you need to provide a service
account key file. This should be uploaded to a secure Exasol bucket in advance.

**Please make sure that the bucket is secure and only readable by user**.

For example,

```bash
curl \
  -X PUT \
  -T path/to/project-id-service-keyfile.json \
  http://w:MY-PASSWORD@EXA-NODE-ID:2580/bucket1/project-id-service-keyfile.json
```

**Summary of parameters:**

- `BUCKET_PATH`, that starts with ``gs``
- `GCS_PROJECT_ID`
- `GCS_KEYFILE_PATH`, path pointing to the project keyfile in Exasol bucketfs

### Import from GCS

```sql
IMPORT INTO SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH      = 'gs://google-storage-path/avro/retail/sales_positions/*'
  DATA_FORMAT      = 'AVRO'
  GCS_PROJECT_ID   = 'MY_GCS_PORJECT_ID'
  GCS_KEYFILE_PATH = 'MY_BUCKETFS_PATH/project-id-service-keyfile.json'
  PARALLELISM      = 'nproc()';
```

### Export to GCS

```sql
EXPORT SALES_POSITIONS
INTO SCRIPT ETL.EXPORT_PATH WITH
  BUCKET_PATH      = 'gs://google-storage-path/parquet/retail/sales_positions/'
  GCS_PROJECT_ID   = 'MY_GCS_PORJECT_ID'
  GCS_KEYFILE_PATH = 'MY_BUCKETFS_PATH/project-id-service-keyfile.json'
  PARALLELISM      = 'iproc()';
```

## Azure Blob Storage

The following parameter values are required to access the Azure Blob Storage.

- `BUCKET_PATH`, that starts with ``wasb`` or ``wasbs``
- `CONTAINER` the blob storage container name
- `AZURE_ACCOUNT_NAME` the Azure storage account name
- `AZURE_SECRET_KEY`

### Import from Blob Storage

```sql
IMPORT INTO SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH        = 'wasbs://CONTAINER@AZURE_ACCOUNT_NAME.blob.core.windows.net/orc/sales-positions/*'
  DATA_FORMAT        = 'ORC'
  AZURE_ACCOUNT_NAME = 'AZURE_ACCOUNT_NAME'
  AZURE_SECRET_KEY   = 'AZURE_SECRET_KEY'
  PARALLELISM        = 'nproc()';
```

### Export to Blob Storage

```sql
EXPORT SALES_POSITIONS
INTO SCRIPT ETL.EXPORT_PATH WITH
  BUCKET_PATH        = 'wasbs://CONTAINER@AZURE_ACCOUNT_NAME.blob.core.windows.net/parquet/sales-positions/'
  AZURE_ACCOUNT_NAME = 'AZURE_ACCOUNT_NAME'
  AZURE_SECRET_KEY   = 'AZURE_SECRET_KEY'
  PARALLELISM        = 'iproc()';
```

## Azure Data Lake (Gen1) Storage

Similarly the following parameters and values are required in order to access
the Azure Data Lake (Gen1) Storage system.

- `BUCKET_PATH`, that starts with ``adl``
- `CONTAINER` the blob storage container name
- `AZURE_CLIENT_ID`, this sometimes called also Azure app id
- `AZURE_CLIENT_SECRET`
- `AZURE_DIRECTORY_ID`

You can follow
[these](https://docs.microsoft.com/en-us/azure/data-lake-store/data-lake-store-service-to-service-authenticate-using-active-directory)
[instructions](https://docs.microsoft.com/en-us/azure/active-directory/develop/howto-create-service-principal-portal)
to obtain the above configuration settings.

**Please ensure that the above client id has an access permission to the Gen1
storage container and its sub folders**.

### Import from Data Lake (Gen1) Store

```sql
IMPORT INTO SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH         = 'adl://CONTAINER.azuredatalakestore.net/orc/sales_positions/*'
  DATA_FORMAT         = 'ORC'
  AZURE_CLIENT_ID     = 'AZURE_CLIENT_ID'
  AZURE_CLIENT_SECRET = 'AZURE_CLIENT_SECRET'
  AZURE_DIRECTORY_ID  = 'AZURE_DIRECTORY_ID'
  PARALLELISM         = 'nproc()';
```

### Export to Data Lake (Gen1) Store

```sql
EXPORT SALES_POSITIONS
INTO SCRIPT ETL.EXPORT_PATH WITH
  BUCKET_PATH         = 'adl://CONTAINER.azuredatalakestore.net/parquet/sales_positions/'
  AZURE_CLIENT_ID     = 'AZURE_CLIENT_ID'
  AZURE_CLIENT_SECRET = 'AZURE_CLIENT_SECRET'
  AZURE_DIRECTORY_ID  = 'AZURE_DIRECTORY_ID'
  PARALLELISM         = 'iproc()';
```
