# Cloud Storage Extension 2.7.9, released 2023-12-07

Code name: Fix CVE-2023-6378

## Summary

This release fixes vulnerability CVE-2023-6378 (CWE-502: Deserialization of Untrusted Data (7.1)) in the following dependencies:
* `ch.qos.logback:logback-classic:jar:1.2.10:compile`
* `ch.qos.logback:logback-core:jar:1.2.10:compile`

## Security

* #288: Fixed CVE-2023-6378 in `ch.qos.logback:logback-core:jar:1.2.10:compile`
* #289: Fixed CVE-2023-6378 in `ch.qos.logback:logback-classic:jar:1.2.10:compile`

## Refactoring

* #290: Added tests to verify importing many files works

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Updated `com.exasol:import-export-udf-common-scala_2.13:1.1.1` to `2.0.0`
* Updated `com.google.protobuf:protobuf-java:3.25.0` to `3.25.1`
* Updated `io.dropwizard.metrics:metrics-core:4.2.22` to `4.2.23`
* Updated `io.grpc:grpc-netty:1.59.0` to `1.60.0`
* Updated `io.netty:netty-handler:4.1.100.Final` to `4.1.101.Final`
* Updated `org.apache.commons:commons-compress:1.24.0` to `1.25.0`
* Updated `org.apache.commons:commons-lang3:3.13.0` to `3.14.0`
* Updated `org.apache.logging.log4j:log4j-1.2-api:2.21.1` to `2.22.0`
* Updated `org.apache.logging.log4j:log4j-api:2.21.1` to `2.22.0`
* Updated `org.apache.logging.log4j:log4j-core:2.21.1` to `2.22.0`
* Updated `org.apache.orc:orc-core:1.9.1` to `1.9.2`
* Updated `org.jetbrains.kotlin:kotlin-stdlib:1.9.20` to `1.9.21`
* Removed `org.slf4j:slf4j-reload4j:2.0.9`

#### Runtime Dependency Updates

* Added `ch.qos.logback:logback-classic:1.2.13`
* Added `ch.qos.logback:logback-core:1.2.13`

#### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:6.6.3` to `7.0.0`
* Updated `com.exasol:extension-manager-integration-test-java:0.5.5` to `0.5.7`
* Updated `com.exasol:hamcrest-resultset-matcher:1.6.2` to `1.6.3`
* Updated `com.exasol:test-db-builder-java:3.5.1` to `3.5.3`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.15.3` to `3.15.4`
* Updated `org.mockito:mockito-core:5.7.0` to `5.8.0`
* Updated `org.testcontainers:localstack:1.19.1` to `1.19.3`

#### Plugin Dependency Updates

* Updated `com.diffplug.spotless:spotless-maven-plugin:2.40.0` to `2.41.0`
* Updated `com.exasol:project-keeper-maven-plugin:2.9.15` to `2.9.17`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.1.2` to `3.2.2`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.6.2` to `3.6.3`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.1.2` to `3.2.2`
* Updated `org.codehaus.mojo:exec-maven-plugin:3.1.0` to `3.1.1`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.1` to `2.16.2`
