# Cloud Storage Extension 2.7.1, released 2023-03-16

Code name: Various Refactorings

## Summary

In this release, we removed obsolete setup instructions that are only relevant to docker based setups, updated dependencies and fixed vulnerabilities that are coming transitively from dependencies. We also update information on `PARALLELISM` parameter in the user guide.

## Documentation

* #238: Updated `parallelism` information in user guide

## Refactoring

* #228: Removed setup instructions that are only related to dockerized setup
* #237: Fixed vulnerabilities in transitive dependencies

## Dependency Updates

### Compile Dependency Updates

* Updated `com.google.protobuf:protobuf-java:3.22.0` to `3.22.2`
* Updated `io.netty:netty-all:4.1.89.Final` to `4.1.90.Final`
* Updated `org.apache.orc:orc-core:1.8.2` to `1.8.3`

### Test Dependency Updates

* Updated `org.mockito:mockito-core:5.1.1` to `5.2.0`

### Plugin Dependency Updates

* Updated `com.diffplug.spotless:spotless-maven-plugin:2.34.0` to `2.35.0`
* Updated `com.exasol:project-keeper-maven-plugin:2.9.3` to `2.9.4`
* Updated `org.apache.maven.plugins:maven-assembly-plugin:3.4.2` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.1.0` to `3.2.1`
