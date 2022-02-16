# Cloud Storage Extension 0.6.2, released 2020-03-16

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
