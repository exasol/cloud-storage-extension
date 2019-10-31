# Cloud Storage Systems

This guide provides the instructions to setup or configure supported cloud
storage systems, usage examples to transfer data stored in supported file
formats.

## Table of contents

- [Prerequisites](#prerequisites)
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

In this guide, we will be using `RETAIL` schema and `SALES_POSITIONS` table in
examples.

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

Please follow the [Amazon credentials management best practices][aws-creds] when
creating credentials.

Additionally, you should provide S3 endpoint:

- `S3_ENDPOINT`, for example, s3.eu-central-1.amazonaws.com.

An endpoint is the URL of the entry point for an AWS resource. For example,
`https://dynamodb.us-west-2.amazonaws.com` is the endpoint for the Amazon
DynamoDB service in the US West (Oregon) Region.

### Import from S3

```sql
IMPORT INTO RETAIL.SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH    = 's3a://<BUCKET>/data/orc/sales_positions/*'
  DATA_FORMAT    = 'ORC'
  S3_ACCESS_KEY  = '<AWS_ACCESS_KEY>'
  S3_SECRET_KEY  = '<AWS_SECRET_KEY>'
  S3_ENDPOINT    = 's3.<REGION>.amazonaws.com'
  PARALLELISM    = 'nproc()*2';
```

### Export to S3

```sql
EXPORT RETAIL.SALES_POSITIONS
INTO SCRIPT ETL.EXPORT_PATH WITH
  BUCKET_PATH    = 's3a://<BUCKET>/data/parquet/sales_positions/'
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

### Import from GCS

```sql
IMPORT INTO RETAIL.SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH      = 'gs://<GCS-STORAGE>/data/avro/sales_positions/*'
  DATA_FORMAT      = 'AVRO'
  GCS_PROJECT_ID   = '<GCP_PORJECT_ID>'
  GCS_KEYFILE_PATH = '/buckets/bfsdefault/<BUCKET_NAME>/gcp-<PROJECT_ID>-service-keyfile.json'
  PARALLELISM      = 'nproc()*4';
```

### Export to GCS

```sql
EXPORT RETAIL.SALES_POSITIONS
INTO SCRIPT ETL.EXPORT_PATH WITH
  BUCKET_PATH      = 'gs://<GCS-STORAGE>/data/parquet/sales_positions/'
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

Azure Blob Storage containers can be accessed using two possible authotization
mechanism.

- ``AZURE_SECRET_KEY``
- ``AZURE_SAS_TOKEN``

The **AZURE_SECRET_KEY** is 512-bit storage access keys that can be generated
after creating a storage account. It is used to authorize access to the storage
accounts.

THE **AZURE_SAS_TOKEN** is Shared Access Signature (SAS) that provides secure
access to storage account with granular control over how the clients can access
the data.

You should provider either one of these parameters when using
cloud-storage-elt-udfs to access the Azure Blob Storage containers.

Additionally, you need to obtain the Azure Blob store account name and container
name and provide them as UDF parameters.

- ``AZURE_ACCOUNT_NAME``
- ``AZURE_CONTAINER_NAME``

The **AZURE_CONTAINER_NAME** parameter is optional if you are using storage
account access keys. However, it should still be available in the
``BUCKET_PATH`` property value string.

Please refer to Azure documentation on [creating storage
account][azure-blob-account], managing [storage access keys][azure-blob-keys]
and using [shared access signatures (SAS)][azure-blob-sas].

### Import from Blob Storage using access key

```sql
IMPORT INTO RETAIL.SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH        = 'wasbs://<AZURE_CONTAINER_NAME>@<AZURE_ACCOUNT_NAME>.blob.core.windows.net/data/orc/sales-positions/*'
  DATA_FORMAT        = 'ORC'
  AZURE_ACCOUNT_NAME = '<AZURE_ACCOUNT_NAME>'
  AZURE_SECRET_KEY   = '<AZURE_SECRET_KEY>'
  PARALLELISM        = 'nproc()';
```

### Import from Blob Storage using SAS token

```sql
IMPORT INTO RETAIL.SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH          = 'wasbs://<AZURE_CONTAINER_NAME>@<AZURE_ACCOUNT_NAME>.blob.core.windows.net/data/orc/sales-positions/*'
  DATA_FORMAT          = 'ORC'
  AZURE_ACCOUNT_NAME   = '<AZURE_ACCOUNT_NAME>'
  AZURE_CONTAINER_NAME = '<AZURE_CONTAINER_NAME>'
  AZURE_SAS_TOKEN      = '<AZURE_SAS_TOKEN>'
  PARALLELISM          = 'nproc()';
```

### Export to Blob Storage

```sql
EXPORT RETAIL.SALES_POSITIONS
INTO SCRIPT ETL.EXPORT_PATH WITH
  BUCKET_PATH        = 'wasbs://<AZURE_CONTAINER_NAME>@<AZURE_ACCOUNT_NAME>.blob.core.windows.net/data/parquet/sales-positions/'
  DATA_FORMAT        = 'PARQUET'
  AZURE_ACCOUNT_NAME = '<AZURE_ACCOUNT_NAME>'
  AZURE_SECRET_KEY   = '<AZURE_SECRET_KEY>'
  PARALLELISM        = 'iproc()';
```

Similar to import, you can also use the SAS token when exporting.

The Azure Blob Storage container path URI scheme can be `wasbs` or `wasb`.

## Azure Data Lake Storage

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

### Import from Data Lake (Gen1) Storage

```sql
IMPORT INTO RETAIL.SALES_POSITIONS
FROM SCRIPT ETL.IMPORT_PATH WITH
  BUCKET_PATH         = 'adl://<AZURE_CONTAINER_NAME>.azuredatalakestore.net/data/avro/sales_positions/*'
  DATA_FORMAT         = 'AVRO'
  AZURE_CLIENT_ID     = '<AZURE_CLIENT_ID>'
  AZURE_CLIENT_SECRET = '<AZURE_CLIENT_SECRET>'
  AZURE_DIRECTORY_ID  = '<AZURE_DIRECTORY_ID>'
  PARALLELISM         = 'nproc()';
```

### Export to Data Lake (Gen1) Storage

```sql
EXPORT RETAIL.SALES_POSITIONS
INTO SCRIPT ETL.EXPORT_PATH WITH
  BUCKET_PATH         = 'adl://<AZURE_CONTAINER_NAME>.azuredatalakestore.net/data/parquet/sales_positions/'
  DATA_FORMAT         = 'PARQUET'
  AZURE_CLIENT_ID     = '<AZURE_CLIENT_ID>'
  AZURE_CLIENT_SECRET = '<AZURE_CLIENT_SECRET>'
  AZURE_DIRECTORY_ID  = '<AZURE_DIRECTORY_ID>'
  PARALLELISM         = 'iproc()';
```

The container path should start with `adl` URI scheme.

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
