## v0.4.2

> 2019 MAY 28

* Adds batch size parameter for export that helps exporting large tables
  [#28](https://github.com/exasol/cloud-storage-etl-udfs/issues/28)
  [#32](https://github.com/exasol/cloud-storage-etl-udfs/pull/32)

## v0.4.1

> 2019 APR 15

* Adds support for Azure Data Lake (Gen1) Storage
  [#22](https://github.com/exasol/cloud-storage-etl-udfs/issues/22)
  [#25](https://github.com/exasol/cloud-storage-etl-udfs/pull/25)
* Support ORC formatted data import
  [#23](https://github.com/exasol/cloud-storage-etl-udfs/issues/23)
  [#26](https://github.com/exasol/cloud-storage-etl-udfs/pull/26)

## v0.4.0

> 2019 MAR 21

* Add Avro format import support
  [#18](https://github.com/exasol/cloud-storage-etl-udfs/issues/18)
  [#21](https://github.com/exasol/cloud-storage-etl-udfs/pull/21)

## v0.3.1

> 2019 MAR 08

* Adds Google Cloud Storage and Azure Blob Storage export as Parquet format
  [#17](https://github.com/exasol/cloud-storage-etl-udfs/issues/17)
  [#20](https://github.com/exasol/cloud-storage-etl-udfs/pull/20)

## v0.3.0

> 2019 FEB 12

* Adds feature to export Exasol tables into AWS S3 as Parquet format
  [#14](https://github.com/exasol/cloud-storage-etl-udfs/issues/14)
  [#16](https://github.com/exasol/cloud-storage-etl-udfs/pull/16)
* Imports date and timestamp values correctly into Exasol
  [#14](https://github.com/exasol/cloud-storage-etl-udfs/issues/14)
  [#16](https://github.com/exasol/cloud-storage-etl-udfs/pull/16)

## v0.2.2

> 2018 DEC 13

* Update Hadoop, Exasol Jdbc and Scala versions
  [#13](https://github.com/exasol/cloud-storage-etl-udfs/pull/13)
* Fixes issue [#12](https://github.com/exasol/cloud-storage-etl-udfs/issues/12)

## v0.2.1

> 2018 DEC 06

* Add initial Azure Blob Store import support
  [#10](https://github.com/exasol/cloud-storage-etl-udfs/pull/10)

## v0.2.0

> 2018 DEC 05

* Add initial Google Compute Storage (GCS) import functionality
  [#9](https://github.com/exasol/cloud-storage-etl-udfs/pull/9)

## v0.1.1

> 2018 NOV 28

* Added AWS S3 endpoint as a parameter
  [#8](https://github.com/exasol/cloud-storage-etl-udfs/pull/8)

## v0.1.0

> 2018 NOV 27

* Add Travis based automated build
  [#3](https://github.com/exasol/cloud-storage-etl-udfs/pull/3)
* Renamed packages and external files to more generic cloudetl from s3etl
  [#4](https://github.com/exasol/cloud-storage-etl-udfs/pull/4)
* Added automated Github release when a tag is pushed
  [#5](https://github.com/exasol/cloud-storage-etl-udfs/pull/5)

## v0.0.1

> 2018 NOV 23

* Initial release with only AWS S3 and Parquet format with only primitive types
  support
