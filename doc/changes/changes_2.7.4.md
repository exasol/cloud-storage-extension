# Cloud Storage Extension 2.7.4, released 2023-??-??

Code name: Upgrade Dependencies

## Summary

This release fixes vulnerability CVE-2022-46751 in transitive dependency `org.apache.ivy:ivy` by upgrading it to the latest version.

The release also updates the extension to use common code from `@exasol/extension-manager-interface`.

## Security

* #269: Fixed CVE-2022-46751 in `org.apache.ivy:ivy`
* #272: Fixed CVE-CVE-2023-42503 in `org.apache.commons:commons-compress`

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Updated `com.exasol:parquet-io-java:2.0.4` to `2.0.5`
* Updated `com.google.protobuf:protobuf-java:3.24.0` to `3.24.3`
* Updated `io.dropwizard.metrics:metrics-core:4.2.19` to `4.2.20`
* Updated `io.netty:netty-handler:4.1.96.Final` to `4.1.99.Final`
* Updated `org.apache.avro:avro:1.11.2` to `1.11.3`
* Added `org.apache.commons:commons-compress:1.24.0`
* Added `org.apache.ivy:ivy:2.5.2`
* Updated `org.jetbrains.kotlin:kotlin-stdlib:1.9.0` to `1.9.10`
* Updated `org.slf4j:jul-to-slf4j:2.0.7` to `2.0.9`
* Updated `org.slf4j:slf4j-reload4j:2.0.7` to `2.0.9`
* Updated `org.xerial.snappy:snappy-java:1.1.10.3` to `1.1.10.5`

#### Test Dependency Updates

* Updated `com.dimafeng:testcontainers-scala-scalatest_2.13:0.40.17` to `0.41.0`
* Updated `com.exasol:exasol-testcontainers:6.6.1` to `6.6.2`
* Updated `com.exasol:extension-manager-integration-test-java:0.5.0` to `0.5.1`
* Updated `com.exasol:hamcrest-resultset-matcher:1.6.0` to `1.6.1`
* Updated `com.exasol:test-db-builder-java:3.4.2` to `3.5.1`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.15.1` to `3.15.2`
* Updated `org.mockito:mockito-core:5.4.0` to `5.5.0`
* Updated `org.testcontainers:localstack:1.18.3` to `1.19.0`

#### Plugin Dependency Updates

* Updated `com.diffplug.spotless:spotless-maven-plugin:2.38.0` to `2.39.0`
* Updated `com.exasol:project-keeper-maven-plugin:2.9.10` to `2.9.12`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.3.0` to `3.4.0`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.5.0` to `3.6.0`

### Extension

#### Compile Dependency Updates

* Updated `@exasol/extension-manager-interface:0.3.0` to `0.3.1`

#### Development Dependency Updates

* Updated `eslint:^8.46.0` to `^8.47.0`
* Added `@jest/globals:^29.6.3`
* Updated `@types/node:^20.4.9` to `^20.5.4`
* Updated `@typescript-eslint/parser:^6.3.0` to `^6.4.1`
* Updated `@typescript-eslint/eslint-plugin:^6.3.0` to `^6.4.1`
* Updated `jest:29.6.2` to `29.6.3`
* Updated `esbuild:^0.19.0` to `^0.19.2`
* Removed `@types/jest:^29.5.3`
