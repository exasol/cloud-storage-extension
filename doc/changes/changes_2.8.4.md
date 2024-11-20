# Cloud Storage Extension 2.8.4, released 2024-11-20

Code name: Fix vulnerabilities CVE-2024-23454 & CVE-2024-47561 & CVE-2024-47554 & CVE-2024-51504 & CVE-2024-47535

## Summary

This release fixes the following vulnerabilities in dependencies:
* CVE-2024-23454 in `org.apache.hadoop:hadoop-common:jar:3.3.6:compile`
* CVE-2024-47561 in `org.apache.avro:avro:jar:1.11.3:compile`
* CVE-2024-47554 in `commons-io:commons-io:jar:2.8.0:compile`
* CVE-2024-51504 in `org.apache.zookeeper:zookeeper:jar:3.9.2:compile`
* CVE-2024-47535 in `io.netty:netty-common:jar:4.1.112.Final:compile`

## Security

* #327: Fixed CVE-2024-23454 in `org.apache.hadoop:hadoop-common:jar:3.3.6:compile`
* #329: Fixed CVE-2024-47561 in `org.apache.avro:avro:jar:1.11.3:compile`
* #330: Fixed CVE-2024-47554 in `commons-io:commons-io:jar:2.8.0:compile`
* #333: Fixed CVE-2024-51504 in `org.apache.zookeeper:zookeeper:jar:3.9.2:compile`
* #334: Fixed CVE-2024-47535 in `io.netty:netty-common:jar:4.1.112.Final:compile`

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Updated `com.exasol:parquet-io-java:2.0.10` to `2.0.12`
* Updated `com.github.mwiede:jsch:0.2.18` to `0.2.21`
* Updated `com.google.guava:guava:33.2.1-jre` to `33.3.1-jre`
* Updated `com.nimbusds:nimbus-jose-jwt:9.40` to `9.47`
* Added `commons-io:commons-io:2.18.0`
* Updated `dnsjava:dnsjava:3.6.1` to `3.6.2`
* Updated `io.dropwizard.metrics:metrics-core:4.2.26` to `4.2.28`
* Updated `io.netty:netty-codec-http2:4.1.112.Final` to `4.1.115.Final`
* Updated `org.apache.avro:avro:1.11.3` to `1.12.0`
* Updated `org.apache.commons:commons-compress:1.26.2` to `1.27.1`
* Updated `org.apache.commons:commons-lang3:3.15.0` to `3.17.0`
* Updated `org.apache.logging.log4j:log4j-1.2-api:2.23.1` to `2.24.1`
* Updated `org.apache.logging.log4j:log4j-api:2.23.1` to `2.24.1`
* Updated `org.apache.logging.log4j:log4j-core:2.23.1` to `2.24.1`
* Updated `org.apache.orc:orc-core:1.9.4` to `1.9.5`
* Updated `org.apache.zookeeper:zookeeper:3.9.2` to `3.9.3`
* Added `org.codehaus.janino:janino:3.1.12`
* Updated `org.glassfish.jersey.containers:jersey-container-servlet-core:2.43` to `2.45`
* Updated `org.glassfish.jersey.containers:jersey-container-servlet:2.43` to `2.45`
* Updated `org.glassfish.jersey.core:jersey-client:2.43` to `2.45`
* Updated `org.glassfish.jersey.core:jersey-common:2.43` to `2.45`
* Updated `org.glassfish.jersey.core:jersey-server:2.43` to `2.45`
* Updated `org.glassfish.jersey.inject:jersey-hk2:2.43` to `2.45`
* Updated `org.slf4j:jul-to-slf4j:2.0.13` to `2.0.16`
* Updated `org.xerial.snappy:snappy-java:1.1.10.5` to `1.1.10.7`

#### Runtime Dependency Updates

* Updated `ch.qos.logback:logback-classic:1.5.6` to `1.5.12`
* Updated `ch.qos.logback:logback-core:1.5.6` to `1.5.12`

#### Test Dependency Updates

* Updated `com.exasol:extension-manager-integration-test-java:0.5.12` to `0.5.13`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.16.1` to `3.17.3`
* Updated `org.hamcrest:hamcrest:2.2` to `3.0`
* Added `org.junit.jupiter:junit-jupiter-api:5.10.3`
* Removed `org.junit.jupiter:junit-jupiter-engine:5.10.3`
* Updated `org.testcontainers:localstack:1.20.0` to `1.20.3`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:4.3.3` to `4.4.0`
* Added `com.exasol:quality-summarizer-maven-plugin:0.2.0`
* Updated `io.github.zlika:reproducible-build-maven-plugin:0.16` to `0.17`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.2.5` to `3.5.1`
* Updated `org.apache.maven.plugins:maven-install-plugin:2.4` to `3.1.3`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.4.1` to `3.4.2`
* Updated `org.apache.maven.plugins:maven-resources-plugin:2.6` to `3.3.1`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.3` to `3.9.1`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.2.5` to `3.5.1`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.2` to `2.17.1`

### Extension

#### Compile Dependency Updates

* Updated `@exasol/extension-manager-interface:0.4.2` to `0.4.3`

#### Development Dependency Updates

* Updated `eslint:^8.56.0` to `9.14.0`
* Updated `@types/node:^20.12.12` to `^22.9.1`
* Updated `ts-jest:^29.1.2` to `^29.2.5`
* Added `typescript-eslint:^8.14.0`
* Updated `typescript:^5.4.5` to `^5.6.3`
* Updated `esbuild:^0.21.2` to `^0.24.0`
* Removed `@typescript-eslint/parser:^7.9.0`
* Removed `@typescript-eslint/eslint-plugin:^7.9.0`
