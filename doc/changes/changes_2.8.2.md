# Cloud Storage Extension 2.8.2, released 2024-07-30

Code name: Fix CVE-2024-25638 in `dnsjava:dnsjava:jar:3.4.0:compile`

## Summary

This release fixes vulnerability CVE-2024-25638 in `dnsjava:dnsjava:jar:3.4.0:compile`.

## Security

* #322: Fixed vulnerability CVE-2024-25638 in `dnsjava:dnsjava:jar:3.4.0:compile`

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Updated `com.exasol:parquet-io-java:2.0.8` to `2.0.10`
* Updated `com.github.mwiede:jsch:0.2.17` to `0.2.18`
* Updated `com.google.guava:guava:33.2.0-jre` to `33.2.1-jre`
* Updated `com.google.protobuf:protobuf-java:3.25.1` to `3.25.4`
* Updated `com.nimbusds:nimbus-jose-jwt:9.39.1` to `9.40`
* Added `dnsjava:dnsjava:3.6.1`
* Updated `io.dropwizard.metrics:metrics-core:4.2.25` to `4.2.26`
* Updated `io.grpc:grpc-netty:1.63.0` to `1.65.1`
* Updated `io.netty:netty-codec-http2:4.1.109.Final` to `4.1.112.Final`
* Updated `org.apache.commons:commons-compress:1.26.1` to `1.26.2`
* Updated `org.apache.commons:commons-configuration2:2.10.1` to `2.11.0`
* Updated `org.apache.commons:commons-lang3:3.14.0` to `3.15.0`
* Updated `org.apache.orc:orc-core:1.9.2` to `1.9.4`
* Updated `org.glassfish.jersey.containers:jersey-container-servlet-core:2.41` to `2.43`
* Updated `org.glassfish.jersey.containers:jersey-container-servlet:2.41` to `2.43`
* Updated `org.glassfish.jersey.core:jersey-client:2.41` to `2.43`
* Updated `org.glassfish.jersey.core:jersey-common:2.41` to `2.43`
* Updated `org.glassfish.jersey.core:jersey-server:2.41` to `2.43`
* Updated `org.glassfish.jersey.inject:jersey-hk2:2.41` to `2.43`
* Updated `org.jetbrains.kotlin:kotlin-stdlib:1.9.24` to `1.9.25`

#### Test Dependency Updates

* Updated `com.dimafeng:testcontainers-scala-scalatest_2.13:0.41.3` to `0.41.4`
* Updated `com.exasol:extension-manager-integration-test-java:0.5.11` to `0.5.12`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.10.2` to `5.10.3`
* Updated `org.testcontainers:localstack:1.19.8` to `1.20.0`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:4.3.2` to `4.3.3`
