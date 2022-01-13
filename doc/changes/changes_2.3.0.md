# Cloud Storage Extension 2.3.0, released 2022-01-13

Code name: Added support for Parquet `TIMESTAMP_MICROS` and `UUID` logical types

## Summary

In this release, we added support for reading Parquet `INT64 (TIMESTAMP_MICROS)` and `FIXED_LEN_BYTE_ARRAY (UUID)` types. We also introduced an S3 SSL enabled parameter that can be used to disable secure connection to S3 bucket.

## Features

* #181: Added support for reading Parquet timestamp micros values
* #183: Added support for reading Parquet UUID values
* #184: Added S3 SSL enabled parameter

## Dependency Updates

### Compile Dependency Updates

* Updated `io.grpc:grpc-netty:1.43.1` to `1.43.2`
* Updated `io.netty:netty-all:4.1.72.Final` to `4.1.73.Final`
* Updated `org.alluxio:alluxio-core-client-hdfs:2.7.1` to `2.7.2`
* Updated `org.apache.logging.log4j:log4j-api:2.17.0` to `2.17.1`
* Updated `org.apache.logging.log4j:log4j-1.2-api:2.17.0` to `2.17.1`

### Test Dependency Updates

### Plugin Updates

* Updated `org.scalameta:sbt-scalafmt:2.4.5` to `2.4.6`
* Updated `org.scoverage:sbt-scoverage:1.9.2` to `1.9.3`
* Updated `com.timushev.sbt:sbt-updates:0.6.0` to `0.6.1`
