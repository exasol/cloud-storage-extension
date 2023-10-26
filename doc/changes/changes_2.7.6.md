# Cloud Storage Extension 2.7.6, released 2023-10-26

Code name: Fix Vulnerabilities CVE-2023-44981 and CVE-2023-46120

## Summary

This release fixes vulnerabilities
* CVE-2023-42503 by overriding version `3.6.3` of transitive dependency `org.apache.zookeeper:zookeeper` via `org.apache.hadoop:hadoop-common`
* CVE-2023-46120 by excluding transitive dependency `com.rabbitmq:amqp-client` via `org.alluxio:alluxio-core-client-hdfs`

## Security

* #281: Fixed vulnerabilities CVE-2023-44981 and CVE-2023-46120

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Updated `com.exasol:parquet-io-java:2.0.5` to `2.0.6`
* Updated `com.google.guava:guava:32.1.2-jre` to `32.1.3-jre`
* Added `com.rabbitmq:amqp-client:5.19.0`
* Updated `io.dropwizard.metrics:metrics-core:4.2.20` to `4.2.21`
* Updated `io.grpc:grpc-netty:1.56.1` to `1.59.0`
* Updated `io.netty:netty-handler:4.1.99.Final` to `4.1.100.Final`
* Updated `org.alluxio:alluxio-core-client-hdfs:300` to `304`
* Updated `org.apache.logging.log4j:log4j-1.2-api:2.20.0` to `2.21.1`
* Updated `org.apache.logging.log4j:log4j-api:2.20.0` to `2.21.1`
* Updated `org.apache.logging.log4j:log4j-core:2.20.0` to `2.21.1`
* Added `org.apache.zookeeper:zookeeper:3.9.1`

#### Test Dependency Updates

* Updated `com.exasol:extension-manager-integration-test-java:0.5.1` to `0.5.4`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.3.0` to `1.3.1`
* Updated `com.exasol:project-keeper-maven-plugin:2.9.12` to `2.9.14`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.4.0` to `3.4.1`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.0` to `2.16.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.10` to `0.8.11`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184` to `3.10.0.2594`
