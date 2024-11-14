# Cloud Storage Extension 2.8.4, released 2024-??-??

Code name: Fixed vulnerability CVE-2024-47535 in io.netty:netty-common:jar:4.1.112.Final:compile

## Summary

This release fixes the following vulnerability:

### CVE-2024-47535 (CWE-400) in dependency `io.netty:netty-common:jar:4.1.112.Final:compile`
Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers & clients. An unsafe reading of environment file could potentially cause a denial of service in Netty. When loaded on an Windows application, Netty attempts to load a file that does not exist. If an attacker creates such a large file, the Netty application crashes. This vulnerability is fixed in 4.1.115.
#### References
* https://ossindex.sonatype.org/vulnerability/CVE-2024-47535?component-type=maven&component-name=io.netty%2Fnetty-common&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2024-47535
* https://github.com/advisories/GHSA-xq3w-v528-46rv

## Security

* #334: Fixed vulnerability CVE-2024-47535 in dependency `io.netty:netty-common:jar:4.1.112.Final:compile`

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Updated `com.exasol:parquet-io-java:2.0.10` to `2.0.11`
* Updated `com.github.mwiede:jsch:0.2.18` to `0.2.21`
* Updated `com.google.cloud.bigdataoss:gcs-connector:1.9.4-hadoop3` to `3.0.4`
* Updated `com.google.guava:guava:33.2.1-jre` to `33.3.1-jre`
* Updated `com.google.protobuf:protobuf-java:3.25.5` to `4.28.3`
* Updated `com.nimbusds:nimbus-jose-jwt:9.40` to `9.46`
* Updated `dnsjava:dnsjava:3.6.1` to `3.6.2`
* Updated `io.airlift:aircompressor:0.27` to `2.0.2`
* Updated `io.dropwizard.metrics:metrics-core:4.2.26` to `4.2.28`
* Updated `io.grpc:grpc-netty:1.65.1` to `1.68.1`
* Updated `io.netty:netty-codec-http2:4.1.112.Final` to `4.1.115.Final`
* Updated `org.alluxio:alluxio-core-client-hdfs:300` to `313`
* Updated `org.apache.avro:avro:1.11.3` to `1.12.0`
* Updated `org.apache.commons:commons-compress:1.26.2` to `1.27.1`
* Updated `org.apache.commons:commons-lang3:3.15.0` to `3.17.0`
* Updated `org.apache.hadoop:hadoop-aws:3.3.6` to `3.4.1`
* Updated `org.apache.hadoop:hadoop-azure-datalake:3.3.6` to `3.4.1`
* Updated `org.apache.hadoop:hadoop-azure:3.3.6` to `3.4.1`
* Updated `org.apache.hadoop:hadoop-common:3.3.6` to `3.4.1`
* Updated `org.apache.hadoop:hadoop-hdfs-client:3.3.6` to `3.4.1`
* Updated `org.apache.hadoop:hadoop-hdfs:3.3.6` to `3.4.1`
* Updated `org.apache.logging.log4j:log4j-1.2-api:2.23.1` to `2.24.1`
* Updated `org.apache.logging.log4j:log4j-api:2.23.1` to `2.24.1`
* Updated `org.apache.logging.log4j:log4j-core:2.23.1` to `2.24.1`
* Updated `org.apache.orc:orc-core:1.9.4` to `2.0.2`
* Updated `org.apache.spark:spark-sql_2.13:3.4.1` to `4.0.0-preview2`
* Updated `org.apache.zookeeper:zookeeper:3.9.2` to `3.9.3`
* Updated `org.glassfish.jersey.containers:jersey-container-servlet-core:2.43` to `3.1.9`
* Updated `org.glassfish.jersey.containers:jersey-container-servlet:2.43` to `3.1.9`
* Updated `org.glassfish.jersey.core:jersey-client:2.43` to `3.1.9`
* Updated `org.glassfish.jersey.core:jersey-common:2.43` to `3.1.9`
* Updated `org.glassfish.jersey.core:jersey-server:2.43` to `3.1.9`
* Updated `org.glassfish.jersey.inject:jersey-hk2:2.43` to `3.1.9`
* Updated `org.jetbrains.kotlin:kotlin-stdlib:1.9.25` to `2.0.21`
* Updated `org.scala-lang:scala-library:2.13.11` to `2.13.15`
* Updated `org.slf4j:jul-to-slf4j:2.0.13` to `2.0.16`
* Updated `org.xerial.snappy:snappy-java:1.1.10.5` to `1.1.10.7`

#### Runtime Dependency Updates

* Updated `ch.qos.logback:logback-classic:1.5.6` to `1.5.12`
* Updated `ch.qos.logback:logback-core:1.5.6` to `1.5.12`

#### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.16.1` to `3.17.3`
* Updated `org.hamcrest:hamcrest:2.2` to `3.0`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.10.3` to `5.11.3`
* Updated `org.mockito:mockito-core:5.12.0` to `5.14.2`
* Updated `org.testcontainers:localstack:1.20.0` to `1.20.3`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:4.3.3` to `4.4.0`
