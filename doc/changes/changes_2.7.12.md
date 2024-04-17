# Cloud Storage Extension 2.7.12, released 2024-04-18

Code name: Dependency upgrades

## Summary
Dependencies upgraded to fix CVE-2024-29131, CVE-2024-29133 and CVE-2024-29025

## Features

* #303: CVE-2024-29131: org.apache.commons:commons-configuration2:jar:2.8.0:compile
* #304: CVE-2024-29133: org.apache.commons:commons-configuration2:jar:2.8.0:compile
* #306: CVE-2024-29025: io.netty:netty-codec-http:jar:4.1.100.Final:compile

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Updated `com.exasol:parquet-io-java:2.0.6` to `2.0.8`
* Added `io.netty:netty-codec-http2:4.1.108.Final`
* Removed `io.netty:netty-handler:4.1.101.Final`
* Added `org.apache.commons:commons-configuration2:2.10.1`
* Added `org.glassfish.jersey.containers:jersey-container-servlet-core:2.41`
* Added `org.glassfish.jersey.containers:jersey-container-servlet:2.41`
* Added `org.glassfish.jersey.core:jersey-client:2.41`
* Added `org.glassfish.jersey.core:jersey-common:2.41`
* Added `org.glassfish.jersey.core:jersey-server:2.41`
* Added `org.glassfish.jersey.inject:jersey-hk2:2.41`

#### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:7.0.0` to `7.0.1`
* Updated `com.exasol:hamcrest-resultset-matcher:1.6.3` to `1.6.5`
* Updated `com.exasol:test-db-builder-java:3.5.3` to `3.5.4`
* Removed `org.glassfish.jersey.core:jersey-common:2.41`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.0` to `2.0.2`
* Updated `com.exasol:project-keeper-maven-plugin:4.1.0` to `4.3.0`
* Updated `org.apache.maven.plugins:maven-assembly-plugin:3.6.0` to `3.7.1`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.12.1` to `3.13.0`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.11` to `0.8.12`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:3.10.0.2594` to `3.11.0.3922`
