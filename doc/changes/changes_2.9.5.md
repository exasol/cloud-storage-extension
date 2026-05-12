# Cloud Storage Extension 2.9.5, released 2026-??-??

Code name: Migrate from Scala to Java

## Summary

This release migrates the project from Scala to Java to simplify maintainence and reduce dependencies.

## Refactoring

* #393: Migrate production code from Scala to Java

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Updated `com.exasol:parquet-io-java:2.0.12` to `2.0.16`
* Added `com.fasterxml.jackson.core:jackson-databind:2.12.7.1`

#### Test Dependency Updates

* Updated `com.exasol:extension-manager-integration-test-java:0.5.13` to `0.5.19`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.6` to `2.0.7`
* Updated `com.exasol:project-keeper-maven-plugin:5.4.6` to `5.6.2`
* Removed `io.github.evis:scalafix-maven-plugin_2.13:0.1.7_0.10.3`
* Updated `io.github.git-commit-id:git-commit-id-maven-plugin:9.0.2` to `10.0.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.4` to `3.5.5`
* Updated `org.apache.maven.plugins:maven-resources-plugin:3.4.0` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.4` to `3.5.5`
* Removed `org.scalastyle:scalastyle-maven-plugin:1.0.0`
