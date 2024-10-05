# Cloud Storage Extension 2.8.4, released 2024-??-??

Code name: Fixed vulnerabilities CVE-2024-47561, CVE-2024-47554

## Summary

This release fixes the following 2 vulnerabilities:

### CVE-2024-47561 (CWE-502) in dependency `org.apache.avro:avro:jar:1.11.3:compile`
Schema parsing in the Java SDK of Apache Avro 1.11.3 and previous versions allows bad actors to execute arbitrary code.
Users are recommended to upgrade to version 1.11.4Â  or 1.12.0, which fix this issue.
#### References
* https://ossindex.sonatype.org/vulnerability/CVE-2024-47561?component-type=maven&component-name=org.apache.avro%2Favro&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2024-47561
* https://lists.apache.org/thread/c2v7mhqnmq0jmbwxqq3r5jbj1xg43h5x

### CVE-2024-47554 (CWE-400) in dependency `commons-io:commons-io:jar:2.8.0:compile`
Uncontrolled Resource Consumption vulnerability in Apache Commons IO.

The org.apache.commons.io.input.XmlStreamReader class may excessively consume CPU resources when processing maliciously crafted input.

This issue affects Apache Commons IO: from 2.0 before 2.14.0.

Users are recommended to upgrade to version 2.14.0 or later, which fixes the issue.
#### References
* https://ossindex.sonatype.org/vulnerability/CVE-2024-47554?component-type=maven&component-name=commons-io%2Fcommons-io&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2024-47554
* https://lists.apache.org/thread/6ozr91rr9cj5lm0zyhv30bsp317hk5z1

## Security

* #329: Fixed vulnerability CVE-2024-47561 in dependency `org.apache.avro:avro:jar:1.11.3:compile`
* #330: Fixed vulnerability CVE-2024-47554 in dependency `commons-io:commons-io:jar:2.8.0:compile`

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Updated `com.github.mwiede:jsch:0.2.18` to `0.2.20`
* Updated `com.google.cloud.bigdataoss:gcs-connector:1.9.4-hadoop3` to `3.0.2`
* Updated `com.google.guava:guava:33.2.1-jre` to `33.3.1-jre`
* Updated `com.google.protobuf:protobuf-java:3.25.5` to `4.28.2`
* Updated `com.nimbusds:nimbus-jose-jwt:9.40` to `9.41.2`
* Updated `dnsjava:dnsjava:3.6.1` to `3.6.2`
* Updated `io.airlift:aircompressor:0.27` to `2.0.2`
* Updated `io.dropwizard.metrics:metrics-core:4.2.26` to `4.2.28`
* Updated `io.grpc:grpc-netty:1.65.1` to `1.68.0`
* Updated `io.netty:netty-codec-http2:4.1.112.Final` to `4.1.114.Final`
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
* Updated `org.apache.logging.log4j:log4j-1.2-api:2.23.1` to `2.24.1`
* Updated `org.apache.logging.log4j:log4j-api:2.23.1` to `2.24.1`
* Updated `org.apache.logging.log4j:log4j-core:2.23.1` to `2.24.1`
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

* Updated `nl.jqno.equalsverifier:equalsverifier:3.16.1` to `3.17.1`
* Updated `org.hamcrest:hamcrest:2.2` to `3.0`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.10.3` to `5.11.2`
* Updated `org.mockito:mockito-core:5.12.0` to `5.14.1`
* Updated `org.testcontainers:localstack:1.20.0` to `1.20.2`
