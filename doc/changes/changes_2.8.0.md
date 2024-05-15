# Cloud Storage Extension 2.8.0, released 2024-??-??

Code name:

## Summary

## Features

* #316: Allowed specifying Google Cloud credentials via `CONNECTION`

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Updated `com.google.cloud.bigdataoss:gcs-connector:1.9.4-hadoop3` to `3.0.0`
* Updated `com.google.guava:guava:32.1.3-jre` to `33.2.0-jre`
* Updated `com.google.oauth-client:google-oauth-client:1.34.1` to `1.36.0`
* Updated `com.google.protobuf:protobuf-java:3.25.1` to `4.26.1`
* Updated `com.nimbusds:nimbus-jose-jwt:9.37.3` to `9.39.1`
* Updated `io.dropwizard.metrics:metrics-core:4.2.23` to `4.2.25`
* Updated `io.grpc:grpc-netty:1.60.0` to `1.63.0`
* Updated `io.netty:netty-codec-http2:4.1.108.Final` to `4.1.109.Final`
* Updated `org.alluxio:alluxio-core-client-hdfs:300` to `312`
* Updated `org.apache.commons:commons-compress:1.26.0` to `1.26.1`
* Updated `org.apache.hadoop:hadoop-aws:3.3.6` to `3.4.0`
* Updated `org.apache.hadoop:hadoop-azure-datalake:3.3.6` to `3.4.0`
* Updated `org.apache.hadoop:hadoop-azure:3.3.6` to `3.4.0`
* Updated `org.apache.hadoop:hadoop-common:3.3.6` to `3.4.0`
* Updated `org.apache.hadoop:hadoop-hdfs-client:3.3.6` to `3.4.0`
* Updated `org.apache.hadoop:hadoop-hdfs:3.3.6` to `3.4.0`
* Updated `org.apache.logging.log4j:log4j-1.2-api:2.22.0` to `2.23.1`
* Updated `org.apache.logging.log4j:log4j-api:2.22.0` to `2.23.1`
* Updated `org.apache.logging.log4j:log4j-core:2.22.0` to `2.23.1`
* Updated `org.apache.orc:orc-core:1.9.2` to `2.0.1`
* Updated `org.apache.spark:spark-sql_2.13:3.4.1` to `3.5.1`
* Updated `org.glassfish.jersey.containers:jersey-container-servlet-core:2.41` to `3.1.6`
* Updated `org.glassfish.jersey.containers:jersey-container-servlet:2.41` to `3.1.6`
* Updated `org.glassfish.jersey.core:jersey-client:2.41` to `3.1.6`
* Updated `org.glassfish.jersey.core:jersey-common:2.41` to `3.1.6`
* Updated `org.glassfish.jersey.core:jersey-server:2.41` to `3.1.6`
* Updated `org.glassfish.jersey.inject:jersey-hk2:2.41` to `3.1.6`
* Updated `org.jetbrains.kotlin:kotlin-stdlib:1.9.21` to `1.9.24`
* Updated `org.scala-lang:scala-library:2.13.11` to `2.13.14`
* Updated `org.slf4j:jul-to-slf4j:2.0.9` to `2.0.13`

#### Runtime Dependency Updates

* Updated `ch.qos.logback:logback-classic:1.2.13` to `1.5.6`
* Updated `ch.qos.logback:logback-core:1.2.13` to `1.5.6`

#### Test Dependency Updates

* Updated `com.dimafeng:testcontainers-scala-scalatest_2.13:0.41.0` to `0.41.3`
* Updated `com.exasol:exasol-testcontainers:7.0.1` to `7.1.0`
* Updated `com.exasol:extension-manager-integration-test-java:0.5.7` to `0.5.11`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.15.4` to `3.16.1`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.10.1` to `5.10.2`
* Updated `org.mockito:mockito-core:5.8.0` to `5.12.0`
* Updated `org.testcontainers:localstack:1.19.3` to `1.19.8`

#### Plugin Dependency Updates

* Updated `com.diffplug.spotless:spotless-maven-plugin:2.41.0` to `2.43.0`
* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.2` to `2.0.3`
* Updated `com.exasol:project-keeper-maven-plugin:4.3.0` to `4.3.1`
* Updated `net.alchim31.maven:scala-maven-plugin:4.8.1` to `4.9.1`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.3.0` to `3.4.1`
* Updated `org.apache.maven.plugins:maven-toolchains-plugin:3.1.0` to `3.2.0`
* Updated `org.codehaus.mojo:exec-maven-plugin:3.1.1` to `3.2.1`

### Extension

#### Compile Dependency Updates

* Updated `@exasol/extension-manager-interface:0.4.1` to `0.4.2`

#### Development Dependency Updates

* Updated `eslint:^8.55.0` to `^8.56.0`
* Updated `@types/node:^20.10.4` to `^20.12.12`
* Updated `@typescript-eslint/parser:^6.13.2` to `^7.9.0`
* Updated `ts-jest:^29.1.1` to `^29.1.2`
* Updated `typescript:^5.3.3` to `^5.4.5`
* Updated `@typescript-eslint/eslint-plugin:^6.13.2` to `^7.9.0`
* Updated `ts-node:^10.9.1` to `^10.9.2`
* Updated `esbuild:^0.19.8` to `^0.21.2`
