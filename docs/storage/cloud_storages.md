# Cloud Storage Systems

This guide provides the instructions to setup or configure supported cloud
storage systems, usage examples to transfer data stored in supported file
formats.

## Table of contents

- [Prerequisites](#prerequisites)
- [Exasol Named Connection Object](#exasol-named-connection-object)
- [Amazon S3](#amazon-s3)
- [Google Cloud Storage](#google-cloud-storage)
- [Azure Blob Storage](#azure-blob-storage)
- [Azure Data Lake (Gen1) Storage](#azure-blob-storage)

## Prerequisites

Before start, you should deploy the latest jar file to the Exasol BucketFS
bucket and create UDF scripts for importing or exporing data from cloud storage
filesystems. Please follow the instructions from [deployment
guide](../deployment_guide.md) if you have not done so.

Additionally, you can read the [user guide](../user_guide.md) in order to get
familiar with cloud-storage-etl-udfs in general.

## Exasol Named Connection Object

In this guide, we will be providing examples using `RETAIL` schema and
`SALES_POSITIONS` table.

Moreover, we are going to show how to provide secure access credentials using
UDF parameters and [Exasol named connection object][exa-connection]. We highly
**recommend** to use connection objects to provide credentials to UDFs so that
secrets are not displayed in audit logs.

## Amazon S3

Amazon Web Services uses security credentials, for example Access Key and Secret
Key, in order to verify and check whether you have permission to acces a
resource such S3.

Thus, if you want to import data files from an Amazon Simple Storage Service
(Amazon S3) bucket, your credentials must allow that access.

Thereofore, in order to access the Amazon S3 bucket you should provide the
following property keys to the UDF scripts with values from AWS credentials
access key and secret key.

- `S3_ACCESS_KEY`
- `S3_SECRET_KEY`

However, when using Multi Factor Authentication (MFA), you can also provide
additional session token using `S3_SESSION_TOKEN` parameter.

Please follow the [Amazon credentials management best practices][aws-creds] when
creating credentials.

Additionally, you should provide S3 endpoint:

- `S3_ENDPOINT`, for example, s3.eu-central-1.amazonaws.com.

An endpoint is the URL of the entry point for an AWS resource. For example,
`https://dynamodb.us-west-2.amazonaws.com` is the endpoint for the Amazon
DynamoDB service in the US West (Oregon) Region.

### Using connection object

First of all create a named connection object and encode credentials a key-value
pairs separated by semicolon (`;`).

Using AWS access and secret keys:

```sql
CREATE OR REPLACE CONNECTION S3_CONNECTION
TO ''
USER ''
IDENTIFIED BY 'S3_ACCESS_KEY=<AWS_ACCESS_KEY>;S3_SECRET_KEY=<AWS_SECRET_KEY>';
```

Or together with session token:

```sql
CREATE OR REPLACE CONNECTION S3_CONNECTION
TO ''
USER ''
IDENTIFIED BY 'S3_ACCESS_KEY=<AWS_ACCESS_KEY>;S3_SECRET_KEY=<AWS_SECRET_KEY>;S3_SESSION_TOKEN=<AWS_SESSION_TOKEN>';
```

#### Import

```sql
IMPORT INTO RETAIL.SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH     = 's3a://<BUCKET>/import/orc/sales_positions/*'
  DATA_FORMAT     = 'ORC'
  S3_ENDPOINT     = 's3.<REGION>.amazonaws.com'
  CONNECTION_NAME = 'S3_CONNECTION'
  PARALLELISM     = 'nproc()*2';
```

#### Export

```sql
EXPORT RETAIL.SALES_POSITIONS
INTO SCRIPT ETL.EXPORT_PATH WITH
  BUCKET_PATH     = 's3a://<BUCKET>/export/parquet/sales_positions/'
  DATA_FORMAT     = 'PARQUET'
  S3_ENDPOINT     = 's3.<REGION>.amazonaws.com'
  CONNECTION_NAME = 'S3_CONNECTION'
  PARALLELISM     = 'iproc(), floor(random()*2)';
```

### Using UDF Parameters

In this case, you should provide each access credentials as key-value pairs.

#### Import

```sql
IMPORT INTO RETAIL.SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH      = 's3a://<BUCKET>/import/orc/sales_positions/*'
  DATA_FORMAT      = 'ORC'
  S3_ACCESS_KEY    = '<AWS_ACCESS_KEY>'
  S3_SECRET_KEY    = '<AWS_SECRET_KEY>'
  S3_SESSION_TOKEN = '<AWS_SESSION_TOKEN>'
  S3_ENDPOINT      = 's3.<REGION>.amazonaws.com'
  PARALLELISM      = 'nproc()*2';
```

#### Export

```sql
EXPORT RETAIL.SALES_POSITIONS
INTO SCRIPT ETL.EXPORT_PATH WITH
  BUCKET_PATH    = 's3a://<BUCKET>/export/parquet/sales_positions/'
  DATA_FORMAT    = 'PARQUET'
  S3_ACCESS_KEY  = '<AWS_ACCESS_KEY>'
  S3_SECRET_KEY  = '<AWS_SECRET_KEY>'
  S3_ENDPOINT    = 's3.<REGION>.amazonaws.com'
  PARALLELISM    = 'iproc(), floor(random()*2)';
```

Currently, we only support `s3a` URI scheme; therefore, `BUCKET_PATH` should
start with it.

## Google Cloud Storage

Similar to Amazon S3, you need to provide security credentials to the UDF in
order to access Google Cloud Storage.

For this, you need to set two property keys when running UDF:

- ``GCS_PROJECT_ID``
- ``GCS_KEYFILE_PATH``

The **GCS_PROJECT_ID** is a Google Cloud Platform (GCP) project identifier. It
is unique string for you project which is composed of the project name and a
randomly assigned number. Please check out the GCP [creating and managing
projects][gcp-projects] page for more information.

The **GCS_KEYFILE_PATH** is path to the location of GCP service account private
key file, usually stored in JSON format.

A Google Cloud Platform service account is an identity that an instance or an
application can use to authenticate its identity and perform authorized tasks on
Google cloud resources. It is special type of Google account intended to
represent a non-human user that needs to authenticate and be authorized to
access Google APIs. Please check out the GCP [introduction to service
accounts][gcp-auth-intro], [understanding service accounts][gcp-auth-under] and
generating [service account private key][gcp-auth-keys] documentation pages.

Once the service account is generated, give enough permissions to it to access
the Google Cloud Storage objects and download its private key as JSON file.

To be able use the service account private key in the UDF, it should be uploaded
to the bucket in Exasol BucketFS.

Upload GCP service account keyfile:

```bash
curl -X PUT -T gcp-<PROJECT_ID>-service-keyfile.json \
  http://w:write-password@exasol.datanode.domain.com:2580/<BUCKET_NAME>/gcp-<PROJECT_ID>-service-keyfile.json
```

Please make sure that the bucket is **secure** and only **readable by users**
who run the UDFs.

### Using UDF Parameters

#### Import from GCS

```sql
IMPORT INTO RETAIL.SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH      = 'gs://<GCS-STORAGE>/import/avro/sales_positions/*'
  DATA_FORMAT      = 'AVRO'
  GCS_PROJECT_ID   = '<GCP_PORJECT_ID>'
  GCS_KEYFILE_PATH = '/buckets/bfsdefault/<BUCKET_NAME>/gcp-<PROJECT_ID>-service-keyfile.json'
  PARALLELISM      = 'nproc()*4';
```

#### Export to GCS

```sql
EXPORT RETAIL.SALES_POSITIONS
INTO SCRIPT ETL.EXPORT_PATH WITH
  BUCKET_PATH      = 'gs://<GCS-STORAGE>/export/parquet/sales_positions/'
  DATA_FORMAT      = 'PARQUET'
  GCS_PROJECT_ID   = '<GCP_PORJECT_ID>'
  GCS_KEYFILE_PATH = '/buckets/bfsdefault/<BUCKET_NAME>/gcp-<PROJECT_ID>-service-keyfile.json'
  PARALLELISM      = 'iproc(), floor(random()*4)';
```

You should notice that UDF access GCS object using `gs` URI scheme.

The Exasol user defined functions have access to buckets from predefined file
path, `/buckets/bfsdefault/`. As a result, you can refer to your buckets when
running the cloud-storage-etl-udfs by providing a path to it such as
`/buckets/bfsdefault/<BUCKET_NAME>/`. Please check out the Exasol manual to
learn more about Exasol synchronous cluster filesystem BucketFS.

## Azure Blob Storage

Azure Blob Storage containers can be accessed using two possible authorization
mechanisms.

- ``AZURE_SECRET_KEY``
- ``AZURE_SAS_TOKEN``

The **AZURE_SECRET_KEY** is 512-bit storage access keys that can be generated
after creating a storage account. It is used to authorize access to the storage
accounts.

The **AZURE_SAS_TOKEN** is a Shared Access Signature (SAS) that provides secure
access to storage account with granular control over how the clients can access
the data.

You should provider either one of these parameters when using
cloud-storage-elt-udfs to access the Azure Blob Storage containers.

Please refer to Azure documentation on [creating storage
account][azure-blob-account], managing [storage access keys][azure-blob-keys]
and using [shared access signatures (SAS)][azure-blob-sas].

### Using connection object

Create a named connection for using with Azure secret key:

```sql
CREATE OR REPLACE CONNECTION AZURE_BLOB_SECRET_CONNECTION
TO ''
USER ''
IDENTIFIED BY 'AZURE_SECRET_KEY=<AZURE_SECRET_KEY>';
```

Or for using with Azure SAS token:

```sql
CREATE OR REPLACE CONNECTION AZURE_BLOB_SAS_CONNECTION
TO ''
USER ''
IDENTIFIED BY 'AZURE_SAS_TOKEN=<AZURE_SAS_TOKEN>';
```

#### Import using secret key connection object

```sql
IMPORT INTO RETAIL.SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH      = 'wasbs://<AZURE_CONTAINER_NAME>@<AZURE_ACCOUNT_NAME>.blob.core.windows.net/import/orc/*'
  DATA_FORMAT      = 'ORC'
  CONNECTION_NAME  = 'AZURE_BLOB_SECRET_CONNECTION'
  PARALLELISM      = 'nproc()';
```

#### Import using SAS token connection object

```sql
IMPORT INTO RETAIL.SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH     = 'wasbs://<AZURE_CONTAINER_NAME>@<AZURE_ACCOUNT_NAME>.blob.core.windows.net/import/orc/*'
  DATA_FORMAT     = 'ORC'
  CONNECTION_NAME = 'AZURE_BLOB_SAS_CONNECTION'
  PARALLELISM     = 'nproc()';
```

#### Export using secret key connection object

```sql
EXPORT RETAIL.SALES_POSITIONS
INTO SCRIPT ETL.EXPORT_PATH WITH
  BUCKET_PATH      = 'wasbs://<AZURE_CONTAINER_NAME>@<AZURE_ACCOUNT_NAME>.blob.core.windows.net/export/parquet/'
  DATA_FORMAT      = 'PARQUET'
  CONNECTION_NAME  = 'AZURE_BLOB_SECRET_CONNECTION'
  PARALLELISM      = 'iproc()';
```

#### Export using SAS token connection object

```sql
EXPORT RETAIL.SALES_POSITIONS
INTO SCRIPT ETL.EXPORT_PATH WITH
  BUCKET_PATH     = 'wasbs://<AZURE_CONTAINER_NAME>@<AZURE_ACCOUNT_NAME>.blob.core.windows.net/export/parquet/'
  DATA_FORMAT     = 'PARQUET'
  CONNECTION_NAME = 'AZURE_BLOB_SAS_CONNECTION'
  PARALLELISM     = 'iproc()';
```

### Using UDF Parameters

#### Import using secret key

```sql
IMPORT INTO RETAIL.SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH      = 'wasbs://<AZURE_CONTAINER_NAME>@<AZURE_ACCOUNT_NAME>.blob.core.windows.net/import/orc/*'
  DATA_FORMAT      = 'ORC'
  AZURE_SECRET_KEY = '<AZURE_SECRET_KEY>'
  PARALLELISM      = 'nproc()';
```

#### Import using SAS token

```sql
IMPORT INTO RETAIL.SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH     = 'wasbs://<AZURE_CONTAINER_NAME>@<AZURE_ACCOUNT_NAME>.blob.core.windows.net/import/orc/*'
  DATA_FORMAT     = 'ORC'
  AZURE_SAS_TOKEN = '<AZURE_SAS_TOKEN>'
  PARALLELISM     = 'nproc()';
```

#### Export using secret key

```sql
EXPORT RETAIL.SALES_POSITIONS
INTO SCRIPT ETL.EXPORT_PATH WITH
  BUCKET_PATH      = 'wasbs://<AZURE_CONTAINER_NAME>@<AZURE_ACCOUNT_NAME>.blob.core.windows.net/export/parquet/'
  DATA_FORMAT      = 'PARQUET'
  AZURE_SECRET_KEY = '<AZURE_SECRET_KEY>'
  PARALLELISM      = 'iproc()';
```

#### Export using SAS token

```sql
EXPORT RETAIL.SALES_POSITIONS
INTO SCRIPT ETL.EXPORT_PATH WITH
  BUCKET_PATH     = 'wasbs://<AZURE_CONTAINER_NAME>@<AZURE_ACCOUNT_NAME>.blob.core.windows.net/export/parquet/'
  DATA_FORMAT     = 'PARQUET'
  AZURE_SAS_TOKEN = '<AZURE_SAS_TOKEN>'
  PARALLELISM     = 'iproc()';
```

The Azure Blob Storage container path URI scheme can be `wasbs` or `wasb`.

## Azure Data Lake (Gen1) Storage

Currently only Azure Data Lake Storage Gen1 version is supported.

The following properties should be provided to the UDF in order to access the
Azure Data Lake (Gen1) Storage.

- `AZURE_CLIENT_ID`
- `AZURE_CLIENT_SECRET`
- `AZURE_DIRECTORY_ID`

The **AZURE_CLIENT_ID** is the Azure Active Directory (AD) App registation
Application ID.

The **AZURE_CLIENT_SECRET** is the secret key generated for the Application ID.

The **AZURE_DIRECTORY_ID** is ht Active Directory (AD) Directory (Tenant) ID.

Please check out the Azure documentation on how to create [service to service
authentication using Active Directory][azure-adl-s2s-auth] and [Azure AD
application and service principal][azure-adl-srv-prin]. These Azure
documentation pages should show how obtain required configuration settings.

Finally, make sure that the client id has an access permissions to the Gen1
storage container or its child directories.

### Using connection object

Create a named connection object that includes secure credentials for Azure ADLS
Storage in the identification field:

```sql
CREATE OR REPLACE CONNECTION AZURE_ADLS_CONNECTION
TO ''
USER ''
IDENTIFIED BY 'AZURE_CLIENT_ID=<AZURE_CLIENT_ID>;AZURE_CLIENT_SECRET=<AZURE_CLIENT_SECRET>;AZURE_DIRECTORY_ID=<AZURE_DIRECTORY_ID>';
```

#### Import

```sql
IMPORT INTO RETAIL.SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH     = 'adl://<AZURE_CONTAINER_NAME>.azuredatalakestore.net/import/avro/*'
  DATA_FORMAT     = 'AVRO'
  CONNECTION_NAME = 'AZURE_ADLS_CONNECTION'
  PARALLELISM     = 'nproc()';
```

#### Export

```sql
EXPORT RETAIL.SALES_POSITIONS
INTO SCRIPT ETL.EXPORT_PATH WITH
  BUCKET_PATH     = 'adl://<AZURE_CONTAINER_NAME>.azuredatalakestore.net/export/parquet/'
  DATA_FORMAT     = 'PARQUET'
  CONNECTION_NAME = 'AZURE_ADLS_CONNECTION'
  PARALLELISM     = 'iproc()';
```

### Using UDF Parameters

#### Import

```sql
IMPORT INTO RETAIL.SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH         = 'adl://<AZURE_CONTAINER_NAME>.azuredatalakestore.net/import/avro/*'
  DATA_FORMAT         = 'AVRO'
  AZURE_CLIENT_ID     = '<AZURE_CLIENT_ID>'
  AZURE_CLIENT_SECRET = '<AZURE_CLIENT_SECRET>'
  AZURE_DIRECTORY_ID  = '<AZURE_DIRECTORY_ID>'
  PARALLELISM         = 'nproc()';
```

#### Export

```sql
EXPORT RETAIL.SALES_POSITIONS
INTO SCRIPT ETL.EXPORT_PATH WITH
  BUCKET_PATH         = 'adl://<AZURE_CONTAINER_NAME>.azuredatalakestore.net/export/parquet/'
  DATA_FORMAT         = 'PARQUET'
  AZURE_CLIENT_ID     = '<AZURE_CLIENT_ID>'
  AZURE_CLIENT_SECRET = '<AZURE_CLIENT_SECRET>'
  AZURE_DIRECTORY_ID  = '<AZURE_DIRECTORY_ID>'
  PARALLELISM         = 'iproc()';
```

The container path should start with `adl` URI scheme.

[exa-connection]: https://docs.exasol.com/sql/create_connection.htm
[aws-creds]: https://docs.aws.amazon.com/general/latest/gr/aws-sec-cred-types.html
[gcp-projects]: https://cloud.google.com/resource-manager/docs/creating-managing-projects
[gcp-auth-intro]: https://cloud.google.com/compute/docs/access/service-accounts
[gcp-auth-under]: https://cloud.google.com/iam/docs/understanding-service-accounts
[gcp-auth-keys]: https://cloud.google.com/video-intelligence/docs/common/auth
[azure-blob-account]: https://docs.microsoft.com/en-us/azure/storage/common/storage-quickstart-create-account
[azure-blob-keys]: https://docs.microsoft.com/en-us/azure/storage/common/storage-account-manage#access-keys
[azure-blob-sas]: https://docs.microsoft.com/en-us/azure/storage/common/storage-sas-overview
[azure-adl-s2s-auth]: https://docs.microsoft.com/en-us/azure/data-lake-store/data-lake-store-service-to-service-authenticate-using-active-directory
[azure-adl-src-prin]: https://docs.microsoft.com/en-us/azure/active-directory/develop/howto-create-service-principal-portal
