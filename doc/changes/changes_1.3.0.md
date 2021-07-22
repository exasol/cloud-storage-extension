# Cloud Storage Extension 1.3.0, released 2021-??-??

Code name: 

## Summary

## Features

* #37: Added support for Hive compatible Parquet schema when exporting Exasol tables

## Bug Fixes

* #145: Fixed issue exporting tables with delimited identifiers
* #156: Fixed bug related to logging backend

## Refactoring

* #150: Added extracted parquet-io-java library
* #157: Refactored build setup
* #162: Fixed code smells reported by Sonar cloud

## Dependency Updates

### Runtime Dependency Updates

* Added `com.exasol:parquet-io-java:1.0.2`
* Added `com.google.guava:guava:30.1.1-jre`
* Added `com.typesafe.scala-logging:scala-logging:3.9.4`
* Added `org.apache.commons:commons-lang3:3.12.0`
* Added `org.slf4j:slf4j-log4j12:1.7.32`
* Added `org.apache.hadoop:hadoop-common:3.3.1`
* Removed `org.apache.hadoop:hadoop-client:3.3.1`
* Removed `org.apache.parquet:parquet-hadoop:1.12.0`
* Updated `org.apache.orc:orc-core:1.6.8` to `1.6.9`
* Updated `org.alluxio:alluxio-core-client-hdfs:2.5.0` to `2.6.0`

### Test Dependency Updates

* Updated `org.mockito:mockito-core:3.11.1` to `3.11.2`
* Updates `org.testcontainers:localstack:1.15.3` to `1.16.0`

### Plugin Updates

* Updated `org.wartremover:sbt-wartremover:2.4.13` to `2.4.16`
* Updated `org.wartremover:sbt-wartremover-contrib:1.3.11` to `1.3.12`
* Updated `org.scoverage:sbt-coveralls:1.3.0` to `1.3.1`
* Updated `net.bzzt:sbt-reproducible-builds:0.25` to `0.28`
* Removed `com.lucidchart:sbt-scalafmt-coursier:1.16`
* Removed `com.typesafe.sbt:sbt-git:1.0.1`
