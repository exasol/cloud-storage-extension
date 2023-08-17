# Cloud Storage Extension 2.7.3, released 2023-??-??

Code name: Upgrade Extension

## Summary

This release updates the extension so that it supports categories and upgrading an installed cloud storage extension to the latest version.

## Features

* #252: Implemented upgrading installed extension
* #251: Added category to extension

## Security

* #260: Updated dependencies to fix security vulnerabilities

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Updated `com.google.guava:guava:32.1.1-jre` to `32.1.2-jre`
* Updated `com.google.protobuf:protobuf-java:3.23.3` to `3.24.0`
* Updated `io.grpc:grpc-netty:1.56.1` to `1.57.2`
* Updated `io.netty:netty-handler:4.1.94.Final` to `4.1.96.Final`
* Updated `org.alluxio:alluxio-core-client-hdfs:300` to `302`
* Updated `org.apache.commons:commons-lang3:3.12.0` to `3.13.0`
* Updated `org.apache.orc:orc-core:1.9.0` to `1.9.1`
* Added `org.jetbrains.kotlin:kotlin-stdlib:1.9.0`
* Removed `org.slf4j:slf4j-log4j12:2.0.7`
* Added `org.slf4j:slf4j-reload4j:2.0.7`
* Updated `org.xerial.snappy:snappy-java:1.1.10.1` to `1.1.10.3`

#### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:6.6.0` to `6.6.1`
* Updated `com.exasol:extension-manager-integration-test-java:0.4.0` to `0.5.0`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.14.3` to `3.15.1`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.9.3` to `5.10.0`
* Updated `org.scalatestplus:scalatestplus-mockito_2.13:1.0.0-M2` to `1.0.0-SNAP5`
* Updated `org.scalatest:scalatest_2.13:3.2.10` to `3.3.0-SNAP4`

#### Plugin Dependency Updates

* Updated `com.diffplug.spotless:spotless-maven-plugin:2.37.0` to `2.38.0`
* Updated `com.exasol:error-code-crawler-maven-plugin:1.2.3` to `1.3.0`
* Updated `com.exasol:project-keeper-maven-plugin:2.9.7` to `2.9.10`
* Updated `org.apache.maven.plugins:maven-assembly-plugin:3.5.0` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0` to `3.1.2`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0` to `3.1.2`
* Updated `org.basepom.maven:duplicate-finder-maven-plugin:1.5.1` to `2.0.1`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.4.1` to `1.5.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.15.0` to `2.16.0`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.9` to `0.8.10`

### Extension

#### Compile Dependency Updates

* Updated `@exasol/extension-manager-interface:0.1.15` to `0.3.0`

#### Development Dependency Updates

* Updated `eslint:^8.20.0` to `^8.46.0`
* Added `@types/node:^20.4.9`
* Updated `@typescript-eslint/parser:^5.31.0` to `^6.3.0`
* Updated `ts-jest:^28.0.7` to `^29.1.1`
* Updated `@types/jest:^28.1.6` to `^29.5.3`
* Updated `typescript:^4.7.4` to `^5.1.6`
* Updated `@typescript-eslint/eslint-plugin:^5.31.0` to `^6.3.0`
* Updated `jest:28.1.3` to `29.6.2`
* Updated `esbuild:^0.14.50` to `^0.19.0`
