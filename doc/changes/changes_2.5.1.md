# Cloud Storage Extension 2.5.1, released 2022-??-??

Code name: Extension Manager support

## Summary

In this release we added an extension for the extension manager.

## Features

* #215: Added extension for extension manager

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:parquet-io-java:1.3.3` to `2.0.0`
* Updated `com.google.protobuf:protobuf-java:3.21.6` to `3.21.7`
* Updated `io.grpc:grpc-netty:1.49.1` to `1.49.2`
* Updated `org.scala-lang:scala-library:2.13.9` to `2.13.10`

### Test Dependency Updates

* Updated `com.dimafeng:testcontainers-scala-scalatest_2.13:0.40.10` to `0.40.11`
* Added `com.exasol:extension-manager-integration-test-java:0.1.0`
* Added `com.exasol:maven-project-version-getter:1.2.0`
* Updated `com.exasol:test-db-builder-java:3.3.4` to `3.4.0`
* Added `org.junit.jupiter:junit-jupiter-engine:5.9.1`
* Updated `org.testcontainers:localstack:1.17.3` to `1.17.5`

### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-clean-plugin:2.5` to `3.2.0`
* Added `org.codehaus.mojo:exec-maven-plugin:3.0.0`
