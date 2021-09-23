# Cloud Storage Extension 2.0.0, released 2021-09-23

Code name: Improved Parquet Reader

## Summary

In this release we add optimized Parquet file importer. Previous versions read single Parquet file in a single importer process, in this version we improved it by virtually splitting files into fixed sized chunks that then can be imported in many parallel processes. For that, we please do not forget to update to the latest deployment scripts in the user guide.

In addition, we added support for connections behind proxies when accessing cloud storage systems and added unified error codes.

## Features

* #173: Added improved chunked Parquet reader
* Added support for connection behind proxy (PR #172)

## Refactoring

* #113: Added unified error codes

## Dependency Updates

### Compile Dependency Updates

* Added `com.exasol:error-reporting-java:0.4.0`
* Updated `com.exasol:parquet-io-java:1.0.3` to `1.1.0`
* Updated `org.apache.orc:orc-core:1.6.9` to `1.7.0`
* Updated `org.alluxio:alluxio-core-client-hdfs:2.6.1` to `2.6.2`
* Updated `io.grpc:grpc-netty:1.39.0` to `1.41.0`

### Test Dependency Updates

* Updated `com.dimafeng:testcontainers-scala-scalatest:0.39.5` to `0.39.8`
* Updated `com.exasol:exasol-testcontainers:4.0.0` to `5.0.0`
* Updated `org.mockito:mockito-core:3.11.2` to `3.12.4`
* Updated `org.scalatest:scalatest:3.2.9` to `3.2.10`

### Plugin Updates

* Updated `com.eed3si9n:sbt-assembly:1.0.0` to `1.1.0`
* Updated `net.bzzt:sbt-reproducible-builds:0.28` to `0.30`
* Updated `org.scoverage:sbt-scoverage:1.8.2` to `1.9.0`
