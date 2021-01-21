# Releases

* [1.0.0](changes_1.0.0.md)
* [0.9.0](changes_0.9.0.md)
* [0.8.0](changes_0.8.0.md)

## v0.7.1

> 2020 MAY 25

* Adds `OVERWRITE` user parameter to be used with Export UDF.
  [#36](https://github.com/exasol/cloud-storage-extension/issues/36)
  [#81](https://github.com/exasol/cloud-storage-extension/pull/81)

* Adds compression codec as an additional extension to the exported file.
  [#82](https://github.com/exasol/cloud-storage-extension/issues/82)
  [#83](https://github.com/exasol/cloud-storage-extension/pull/83)

## v0.7.0

> 2020 MAR 27

* Adds import JSON formatted data from AWS Kinesis Streams.
   [#74](https://github.com/exasol/cloud-storage-extension/issues/74)
   [#77](https://github.com/exasol/cloud-storage-extension/pull/77)

## v0.6.2

> 2020 MAR 16

* Adds Delta Lake format import integration.
  [#31](https://github.com/exasol/cloud-storage-extension/issues/31)
  [#78](https://github.com/exasol/cloud-storage-extension/pull/78)

* Adds initial system requirements document for the upcoming Amazon Kinesis
  Connector.
  [#72](https://github.com/exasol/cloud-storage-extension/issues/72)
  [#73](https://github.com/exasol/cloud-storage-extension/pull/73)

* Improve the user guide document with an additional Exasol connection object
  usage example.
  [#66](https://github.com/exasol/cloud-storage-extension/issues/66)
  [#76](https://github.com/exasol/cloud-storage-extension/pull/76)

* Updated dependencies to make the project run with IntelliJ IDEA.
  [#70](https://github.com/exasol/cloud-storage-extension/issues/70)
  [#71](https://github.com/exasol/cloud-storage-extension/pull/71)

## v0.6.1

> 2020 FEB 17

* Add Azure Data Lake Gen2 Storage support.
  [#58](https://github.com/exasol/cloud-storage-extension/issues/58)
  [#69](https://github.com/exasol/cloud-storage-extension/pull/69)

* Fixes a bug with regex pattern matching on the paths.
  [#65](https://github.com/exasol/cloud-storage-extension/issues/65)
  [#67](https://github.com/exasol/cloud-storage-extension/pull/67)

* Adds list of dependencies with their licenses to the README.md.
  [#63](https://github.com/exasol/cloud-storage-extension/issues/63)
  [#64](https://github.com/exasol/cloud-storage-extension/pull/64)

* Refactors code linting and styling checks.
  [#51](https://github.com/exasol/cloud-storage-extension/issues/51)
  [#62](https://github.com/exasol/cloud-storage-extension/pull/62)

* Adds a logo to the project.
  [#60](https://github.com/exasol/cloud-storage-extension/pull/60)

* Fixes bug related to BigDecimal.
  [#56](https://github.com/exasol/cloud-storage-extension/issues/56)
  [#57](https://github.com/exasol/cloud-storage-extension/pull/57)

## v0.6.0

> 2019 DEC 06

* Improves Azure Blob Storage parameters, do not enforce requirement of
  `AZURE_ACCOUNT_NAME` and `AZURE_CONTAINER_NAME` properties since they are
  available in the path.
  [#50](https://github.com/exasol/cloud-storage-extension/issues/50)
  [#55](https://github.com/exasol/cloud-storage-extension/pull/55)

* Adds support for Exasol named connection object
  [#24](https://github.com/exasol/cloud-storage-extension/issues/24)
  [#54](https://github.com/exasol/cloud-storage-extension/pull/54)

* Fixes bug when importing Parquet `INT64 (TIMESTAMP_MILLIS)` type values.
  [#52](https://github.com/exasol/cloud-storage-extension/issues/52)
  [#53](https://github.com/exasol/cloud-storage-extension/pull/53)

## v0.5.0

> 2019 OCT 31 :jack_o_lantern:

* Adds Apache Kafka consumer UDF to import Avro formatted data from Kafka
  clusters. [#40](https://github.com/exasol/cloud-storage-extension/issues/40)
  [#39](https://github.com/exasol/cloud-storage-extension/pull/39)
  [#48](https://github.com/exasol/cloud-storage-extension/pull/48)

* Adds several new Kafka consumer settings as user provided UDF parameters.
  [#41](https://github.com/exasol/cloud-storage-extension/issues/41)
  [#43](https://github.com/exasol/cloud-storage-extension/pull/43)

* Refactors the UDF user properties handling in order to make more robust and
  maintainable. [#46](https://github.com/exasol/cloud-storage-extension/pull/46)

* Reworks the documentation to incorporate the new Kafka import changes.
  [#45](https://github.com/exasol/cloud-storage-extension/issues/45)
  [#47](https://github.com/exasol/cloud-storage-extension/pull/47)

## v0.4.4

> 2019 OCT 27

* Adds Shared Access Signature (SAS) token authentication when using Azure Blob
  Storage. [#42](https://github.com/exasol/cloud-storage-extension/issues/42)
  [#44](https://github.com/exasol/cloud-storage-extension/pull/44)

## v0.4.3

> 2019 JUL 01

* Deserialize Orc BYTE format as a Long value.
  [#33](https://github.com/exasol/cloud-storage-extension/issues/33)
  [#38](https://github.com/exasol/cloud-storage-extension/pull/38)

* Improve import process.
  [#34](https://github.com/exasol/cloud-storage-extension/issues/34)
  [#38](https://github.com/exasol/cloud-storage-extension/pull/38)

## v0.4.2

> 2019 MAY 28

* Adds batch size parameter for export that helps exporting large tables.
  [#28](https://github.com/exasol/cloud-storage-extension/issues/28)
  [#32](https://github.com/exasol/cloud-storage-extension/pull/32)

* Applied some refactoring.
  [#29](https://github.com/exasol/cloud-storage-extension/pull/29)

## v0.4.1

> 2019 APR 15

* Adds support for Azure Data Lake (Gen1) Storage.
  [#22](https://github.com/exasol/cloud-storage-extension/issues/22)
  [#25](https://github.com/exasol/cloud-storage-extension/pull/25)

* Support ORC formatted data import.
  [#23](https://github.com/exasol/cloud-storage-extension/issues/23)
  [#26](https://github.com/exasol/cloud-storage-extension/pull/26)

## v0.4.0

> 2019 MAR 21

* Add Avro format import support.
  [#18](https://github.com/exasol/cloud-storage-extension/issues/18)
  [#21](https://github.com/exasol/cloud-storage-extension/pull/21)

## v0.3.1

> 2019 MAR 08

* Adds Google Cloud Storage and Azure Blob Storage export as Parquet format.
  [#17](https://github.com/exasol/cloud-storage-extension/issues/17)
  [#20](https://github.com/exasol/cloud-storage-extension/pull/20)

## v0.3.0

> 2019 FEB 12

* Adds feature to export Exasol tables into AWS S3 as Parquet format.
  [#14](https://github.com/exasol/cloud-storage-extension/issues/14)
  [#16](https://github.com/exasol/cloud-storage-extension/pull/16)

* Imports date and timestamp values correctly into Exasol.
  [#14](https://github.com/exasol/cloud-storage-extension/issues/14)
  [#16](https://github.com/exasol/cloud-storage-extension/pull/16)

## v0.2.2

> 2018 DEC 13

* Update Hadoop, Exasol Jdbc and Scala versions.
  [#13](https://github.com/exasol/cloud-storage-extension/pull/13)

* Fixes issue.
  [#12](https://github.com/exasol/cloud-storage-extension/issues/12)

## v0.2.1

> 2018 DEC 06

* Add initial Azure Blob Store import support.
  [#10](https://github.com/exasol/cloud-storage-extension/pull/10)

## v0.2.0

> 2018 DEC 05

* Add initial Google Compute Storage (GCS) import functionality.
  [#9](https://github.com/exasol/cloud-storage-extension/pull/9)

## v0.1.1

> 2018 NOV 28

* Added AWS S3 endpoint as a parameter.
  [#8](https://github.com/exasol/cloud-storage-extension/pull/8)

## v0.1.0

> 2018 NOV 27

* Add Travis based automated build.
  [#3](https://github.com/exasol/cloud-storage-extension/pull/3)

* Renamed packages and external files to more generic cloudetl from s3etl.
  [#4](https://github.com/exasol/cloud-storage-extension/pull/4)

* Added automated Github release when a tag is pushed.
  [#5](https://github.com/exasol/cloud-storage-extension/pull/5)

## v0.0.1

> 2018 NOV 23

* Initial release with only AWS S3 and Parquet format with only primitive types
  support
