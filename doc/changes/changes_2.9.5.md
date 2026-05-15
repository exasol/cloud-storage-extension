# Cloud Storage Extension 2.9.5, released 2026-05-15

Code name: Migrate from Scala to Java

## Summary

This release migrates the project from Scala to Java to simplify maintenance and reduce dependencies.

As part of this endeavor, we updated the `parquet-io-java` dependency to 2.0.16, which is now also pure Java.

This should not be a breaking change, so we updated only the patch number of the release. It is a major refactoring though. We ran all tests, before and after we migrated them.
If you read this release letter, we would love to get feedback from you. Does the migrated code run as fast and reliable as before in your production environment?

You can use GitHub issues in this repository or a mail to tell us your experiences!

## Refactoring

* #393: Migrate production code from Scala to Java

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Added `org.slf4j:slf4j-api:2.0.18`

#### Test Dependency Updates

* Removed `com.dimafeng:testcontainers-scala-scalatest_2.13:0.41.4`
* Updated `com.exasol:extension-manager-integration-test-java:0.5.13` to `0.5.19`
* Removed `org.junit.jupiter:junit-jupiter-api:5.10.3`
* Added `org.junit.jupiter:junit-jupiter:5.10.3`
* Removed `org.scalatestplus:scalatestplus-mockito_2.13:1.0.0-SNAP5`
* Removed `org.scalatest:scalatest_2.13:3.3.0-SNAP4`
* Updated `org.testcontainers:localstack:1.20.3` to `1.21.4`

#### Plugin Dependency Updates

* Removed `com.diffplug.spotless:spotless-maven-plugin:2.43.0`
* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.6` to `2.0.7`
* Updated `com.exasol:project-keeper-maven-plugin:5.4.6` to `5.6.2`
* Removed `io.github.evis:scalafix-maven-plugin_2.13:0.1.7_0.10.3`
* Updated `io.github.git-commit-id:git-commit-id-maven-plugin:9.0.2` to `10.0.0`
* Removed `net.alchim31.maven:scala-maven-plugin:4.9.1`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.4` to `3.5.5`
* Updated `org.apache.maven.plugins:maven-resources-plugin:3.4.0` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.4` to `3.5.5`
* Removed `org.scalastyle:scalastyle-maven-plugin:1.0.0`
* Removed `org.scalatest:scalatest-maven-plugin:2.2.0`
