# Cloud Storage Extension 2.7.7, released 2023-11-07

Code name: Refactoring of Extension

## Summary

This release refactors the extension to use shared integration tests to simplify the source code.

## Refactoring

* #284: Used shared extension integration tests

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Updated `com.google.protobuf:protobuf-java:3.24.4` to `3.25.0`
* Updated `io.dropwizard.metrics:metrics-core:4.2.21` to `4.2.22`
* Updated `org.jetbrains.kotlin:kotlin-stdlib:1.9.10` to `1.9.20`

#### Test Dependency Updates

* Updated `com.exasol:extension-manager-integration-test-java:0.5.4` to `0.5.5`
* Updated `com.exasol:hamcrest-resultset-matcher:1.6.1` to `1.6.2`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.15.2` to `3.15.3`
* Added `org.glassfish.jersey.core:jersey-common:2.41`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.10.0` to `5.10.1`
* Updated `org.mockito:mockito-core:5.6.0` to `5.7.0`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:2.9.14` to `2.9.15`
* Updated `org.apache.maven.plugins:maven-clean-plugin:3.3.1` to `3.3.2`

### Extension

#### Compile Dependency Updates

* Updated `@exasol/extension-manager-interface:0.3.1` to `0.4.0`

#### Development Dependency Updates

* Updated `eslint:^8.47.0` to `^8.53.0`
* Updated `@jest/globals:^29.6.3` to `^29.7.0`
* Updated `@types/node:^20.5.4` to `^20.8.10`
* Updated `@typescript-eslint/parser:^6.4.1` to `^6.9.1`
* Updated `typescript:^5.1.6` to `^5.2.2`
* Updated `@typescript-eslint/eslint-plugin:^6.4.1` to `^6.9.1`
* Updated `jest:29.6.3` to `29.7.0`
* Updated `esbuild:^0.19.2` to `^0.19.5`
