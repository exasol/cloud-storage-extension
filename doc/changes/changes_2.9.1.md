# Cloud Storage Extension 2.9.1, released 2025-??-??

Code name: Fixed vulnerability CVE-2025-58056 in io.netty:netty-codec-http:jar:4.1.124.Final:compile

## Summary

This release fixes the following vulnerability:

### CVE-2025-58056 (CWE-444) in dependency `io.netty:netty-codec-http:jar:4.1.124.Final:compile`
Netty is an asynchronous event-driven network application framework for development of maintainable high performance protocol servers and clients. In versions 4.1.124.Final, and 4.2.0.Alpha3 through 4.2.4.Final, Netty incorrectly accepts standalone newline characters (LF) as a chunk-size line terminator, regardless of a preceding carriage return (CR), instead of requiring CRLF per HTTP/1.1 standards. When combined with reverse proxies that parse LF differently (treating it as part of the chunk extension), attackers can craft requests that the proxy sees as one request but Netty processes as two, enabling request smuggling attacks. This is fixed in versions 4.1.125.Final and 4.2.5.Final.
#### References
* https://ossindex.sonatype.org/vulnerability/CVE-2025-58056?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2025-58056
* https://github.com/netty/netty/security/advisories/GHSA-fghv-69vj-qj49

## Security

* #362: Fixed vulnerability CVE-2025-58056 in dependency `io.netty:netty-codec-http:jar:4.1.124.Final:compile`

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Updated `com.exasol:parquet-io-java:2.0.12` to `2.0.13`
* Updated `com.github.mwiede:jsch:0.2.21` to `2.27.3`
* Updated `com.google.cloud.bigdataoss:gcs-connector:1.9.4-hadoop3` to `3.1.5`
* Updated `com.google.guava:guava:33.3.1-jre` to `33.4.8-jre`
* Updated `com.google.oauth-client:google-oauth-client:1.36.0` to `1.39.0`
* Updated `com.google.protobuf:protobuf-java:3.25.5` to `4.32.0`
* Updated `com.nimbusds:nimbus-jose-jwt:9.47` to `10.5`
* Updated `commons-io:commons-io:2.18.0` to `2.20.0`
* Updated `dnsjava:dnsjava:3.6.2` to `3.6.3`
* Updated `io.airlift:aircompressor:0.27` to `2.0.2`
* Updated `io.dropwizard.metrics:metrics-core:4.2.28` to `4.2.36`
* Updated `io.grpc:grpc-netty:1.65.1` to `1.75.0`
* Updated `io.netty:netty-codec-http2:4.1.124.Final` to `4.2.5.Final`
* Updated `org.alluxio:alluxio-core-client-hdfs:300` to `313`
* Updated `org.apache.commons:commons-compress:1.27.1` to `1.28.0`
* Updated `org.apache.commons:commons-configuration2:2.11.0` to `2.12.0`
* Updated `org.apache.hadoop:hadoop-aws:3.4.1` to `3.4.2`
* Updated `org.apache.hadoop:hadoop-azure-datalake:3.4.1` to `3.4.2`
* Updated `org.apache.hadoop:hadoop-azure:3.4.1` to `3.4.2`
* Updated `org.apache.hadoop:hadoop-common:3.4.1` to `3.4.2`
* Updated `org.apache.hadoop:hadoop-hdfs-client:3.4.1` to `3.4.2`
* Updated `org.apache.hadoop:hadoop-hdfs:3.4.1` to `3.4.2`
* Updated `org.apache.ivy:ivy:2.5.2` to `2.5.3`
* Updated `org.apache.logging.log4j:log4j-1.2-api:2.24.1` to `2.25.1`
* Updated `org.apache.logging.log4j:log4j-api:2.24.1` to `2.25.1`
* Updated `org.apache.logging.log4j:log4j-core:2.24.1` to `2.25.1`
* Updated `org.apache.orc:orc-core:1.9.5` to `2.2.0`
* Updated `org.apache.spark:spark-sql_2.13:3.4.1` to `4.1.0-preview1`
* Updated `org.apache.zookeeper:zookeeper:3.9.3` to `3.9.4`
* Updated `org.glassfish.jersey.containers:jersey-container-servlet-core:2.45` to `3.1.11`
* Updated `org.glassfish.jersey.containers:jersey-container-servlet:2.45` to `3.1.11`
* Updated `org.glassfish.jersey.core:jersey-client:2.45` to `3.1.11`
* Updated `org.glassfish.jersey.core:jersey-common:2.45` to `3.1.11`
* Updated `org.glassfish.jersey.core:jersey-server:2.45` to `3.1.11`
* Updated `org.glassfish.jersey.inject:jersey-hk2:2.45` to `3.1.11`
* Updated `org.jetbrains.kotlin:kotlin-stdlib:1.9.25` to `2.2.10`
* Updated `org.scala-lang:scala-library:2.13.11` to `2.13.16`
* Updated `org.slf4j:jul-to-slf4j:2.0.16` to `2.0.17`
* Updated `org.xerial.snappy:snappy-java:1.1.10.7` to `1.1.10.8`
* Updated `software.amazon.awssdk:s3-transfer-manager:2.32.31` to `2.33.4`
* Updated `software.amazon.awssdk:s3:2.32.31` to `2.33.4`

#### Runtime Dependency Updates

* Updated `ch.qos.logback:logback-classic:1.5.16` to `1.5.18`
* Updated `ch.qos.logback:logback-core:1.5.16` to `1.5.18`

#### Test Dependency Updates

* Updated `com.dimafeng:testcontainers-scala-scalatest_2.13:0.41.4` to `0.43.0`
* Updated `com.exasol:exasol-testcontainers:7.1.4` to `7.1.7`
* Updated `com.exasol:extension-manager-integration-test-java:0.5.13` to `0.5.16`
* Updated `com.exasol:hamcrest-resultset-matcher:1.7.0` to `1.7.2`
* Updated `com.exasol:maven-project-version-getter:1.2.0` to `1.2.1`
* Updated `com.exasol:test-db-builder-java:3.6.0` to `3.6.3`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.17.3` to `4.1`
* Updated `org.junit.jupiter:junit-jupiter-api:5.10.3` to `5.13.4`
* Updated `org.mockito:mockito-core:5.12.0` to `5.19.0`
* Updated `org.testcontainers:localstack:1.20.3` to `1.21.3`
