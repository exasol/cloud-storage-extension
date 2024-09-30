# Cloud Storage Extension 2.8.4, released 2024-??-??

Code name: Fixed vulnerability CVE-2024-23454 in org.apache.hadoop:hadoop-common:jar:3.3.6:compile

## Summary

This release fixes the following vulnerability:

### CVE-2024-23454 (CWE-269) in dependency `org.apache.hadoop:hadoop-common:jar:3.3.6:compile`
Apache Hadoopâs RunJar.run()Â does not set permissions for temporary directoryÂ by default. If sensitive data will be present in this file, all the other local users may be able to view the content.
This is because, on unix-like systems, the system temporary directory is
shared between all local users. As such, files written in this directory,
without setting the correct posix permissions explicitly, may be viewable
by all other local users.
#### References
* https://ossindex.sonatype.org/vulnerability/CVE-2024-23454?component-type=maven&component-name=org.apache.hadoop%2Fhadoop-common&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2024-23454
* https://github.com/advisories/GHSA-f5fw-25gw-5m92

## Security

* #327: Fixed vulnerability CVE-2024-23454 in dependency `org.apache.hadoop:hadoop-common:jar:3.3.6:compile`

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Updated `com.github.mwiede:jsch:0.2.18` to `0.2.20`
* Updated `com.google.cloud.bigdataoss:gcs-connector:1.9.4-hadoop3` to `3.0.2`
* Updated `com.google.guava:guava:33.2.1-jre` to `33.3.1-jre`
* Updated `com.google.protobuf:protobuf-java:3.25.5` to `4.28.2`
* Updated `com.nimbusds:nimbus-jose-jwt:9.40` to `9.41.1`
* Updated `dnsjava:dnsjava:3.6.1` to `3.6.2`
* Updated `io.airlift:aircompressor:0.27` to `2.0.2`
* Updated `io.dropwizard.metrics:metrics-core:4.2.26` to `4.2.27`
* Updated `io.grpc:grpc-netty:1.65.1` to `1.68.0`
* Updated `io.netty:netty-codec-http2:4.1.112.Final` to `4.1.113.Final`
* Updated `org.alluxio:alluxio-core-client-hdfs:300` to `313`
* Updated `org.apache.avro:avro:1.11.3` to `1.12.0`
* Updated `org.apache.commons:commons-compress:1.26.2` to `1.27.1`
* Updated `org.apache.commons:commons-lang3:3.15.0` to `3.17.0`
* Updated `org.apache.hadoop:hadoop-aws:3.3.6` to `3.4.0`
* Updated `org.apache.hadoop:hadoop-azure-datalake:3.3.6` to `3.4.0`
* Updated `org.apache.hadoop:hadoop-azure:3.3.6` to `3.4.0`
* Updated `org.apache.hadoop:hadoop-common:3.3.6` to `3.4.0`
* Updated `org.apache.hadoop:hadoop-hdfs-client:3.3.6` to `3.4.0`
* Updated `org.apache.hadoop:hadoop-hdfs:3.3.6` to `3.4.0`
* Updated `org.apache.logging.log4j:log4j-1.2-api:2.23.1` to `2.24.0`
* Updated `org.apache.logging.log4j:log4j-api:2.23.1` to `2.24.0`
* Updated `org.apache.logging.log4j:log4j-core:2.23.1` to `2.24.0`
* Updated `org.apache.orc:orc-core:1.9.4` to `2.0.2`
* Updated `org.apache.spark:spark-sql_2.13:3.4.1` to `4.0.0-preview2`
* Updated `org.glassfish.jersey.containers:jersey-container-servlet-core:2.43` to `3.1.8`
* Updated `org.glassfish.jersey.containers:jersey-container-servlet:2.43` to `3.1.8`
* Updated `org.glassfish.jersey.core:jersey-client:2.43` to `3.1.8`
* Updated `org.glassfish.jersey.core:jersey-common:2.43` to `3.1.8`
* Updated `org.glassfish.jersey.core:jersey-server:2.43` to `3.1.8`
* Updated `org.glassfish.jersey.inject:jersey-hk2:2.43` to `3.1.8`
* Updated `org.jetbrains.kotlin:kotlin-stdlib:1.9.25` to `2.0.20`
* Updated `org.scala-lang:scala-library:2.13.11` to `2.13.15`
* Updated `org.slf4j:jul-to-slf4j:2.0.13` to `2.0.16`
* Updated `org.xerial.snappy:snappy-java:1.1.10.5` to `1.1.10.7`

#### Runtime Dependency Updates

* Updated `ch.qos.logback:logback-classic:1.5.6` to `1.5.8`
* Updated `ch.qos.logback:logback-core:1.5.6` to `1.5.8`

#### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.16.1` to `3.17`
* Updated `org.hamcrest:hamcrest:2.2` to `3.0`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.10.3` to `5.11.1`
* Updated `org.mockito:mockito-core:5.12.0` to `5.14.0`
* Updated `org.testcontainers:localstack:1.20.0` to `1.20.1`
