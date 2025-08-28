# Cloud Storage Extension 2.9.0, released 2025-??-??

Code name: Upgrade of hadoop libraries

## Summary
This version upgrades hadoop from 3.3.6 to the latest 3.4.1, which fixes several CVEs in transient dependencies and
leverages all the improvements the recent hadoop libs have.

## Features

* #310: Upgrade spark and hadoop versions

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Removed `io.grpc:grpc-netty:1.65.1`
* Updated `org.apache.hadoop:hadoop-aws:3.3.6` to `3.4.1`
* Updated `org.apache.hadoop:hadoop-azure-datalake:3.3.6` to `3.4.1`
* Updated `org.apache.hadoop:hadoop-azure:3.3.6` to `3.4.1`
* Updated `org.apache.hadoop:hadoop-common:3.3.6` to `3.4.1`
* Updated `org.apache.hadoop:hadoop-hdfs-client:3.3.6` to `3.4.1`
* Updated `org.apache.hadoop:hadoop-hdfs:3.3.6` to `3.4.1`
* Updated `org.apache.orc:orc-core:1.9.6` to `1.9.5`
* Added `software.amazon.awssdk:s3-transfer-manager:2.27.21`
* Added `software.amazon.awssdk:s3:2.27.21`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.3` to `2.0.4`
* Updated `com.exasol:project-keeper-maven-plugin:5.1.0` to `5.2.3`
