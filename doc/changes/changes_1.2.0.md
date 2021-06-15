# Cloud Storage Extension 1.2.0, released 2021-06-15

Code name: Fixed bug listing files on a path using asterisk

## Summary

In this release we fixed a bug related to listing files using asterisk on a path. We also updated user guide and added link checker to detect broken links in the project documentation.  

## Bugfixes

* #146: Fixed issue listing files when asterisk is missing on a path

## Documentation

* #141: Updated readme to reflect recent changes
* #143: Added broken links checker Github actions workflow
* #151: Update user guide overview with additional sources

## Dependency Updates

### Runtime Dependency Updates

* Updated Scala version `2.12.12` to `2.12.14`
* Updated `org.apache.orc:orc-core:1.6.7` to `1.6.8`
* Updated `org.apache.hadoop:hadoop-aws:3.3.0` to `3.3.1`
* Updated `org.apache.hadoop:hadoop-azure:3.3.0` to `3.3.1`
* Updated `org.apache.hadoop:hadoop-azure-datalake:3.3.0` to `3.3.1`
* Updated `org.apache.hadoop:hadoop-client:3.3.0` to `3.3.1`
* Updated `org.apache.hadoop:hadoop-hdfs:3.3.0` to `3.3.1`

### Test Dependency Updates

* Updated `com.exasol:test-db-builder-java:3.1.1` to `3.2.0`
* Updated `com.exasol:exasol-testcontainers:3.5.1` to `3.5.3`
* Updated `org.scalatest:scalatest:3.2.6` to `3.2.9`
* Updated `org.mockito:mockito-core:3.8.0` to `3.11.1`
* Updated `org.testcontainers:localstack:1.15.2` to `1.15.3`
* Updated `com.dimafeng:testcontainers-scala-scalatest:0.39.3` to `0.39.5`

### Plugin Updates

* Updated `com.eed3si9n:sbt-assembly:0.15.0` to `1.0.0`
* Updated `com.timushev.sbt:sbt-updates:0.5.2` to `0.5.3`
* Updated `com.typesafe.sbt:sbt-git:1.0.0` to `1.0.1`
* Updated `org.scoverage:sbt-coveralls:1.2.7` to `1.3.0`
* Updated `org.scoverage:sbt-scoverage:1.6.1` to `1.8.2`
