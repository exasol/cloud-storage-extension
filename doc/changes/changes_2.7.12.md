# Cloud Storage Extension 2.7.12, released 2024-??-??

Code name: Fixed vulnerabilities CVE-2024-29131, CVE-2024-29133

## Summary

This release fixes the following 2 vulnerabilities:

### CVE-2024-29131 (CWE-787) in dependency `org.apache.commons:commons-configuration2:jar:2.8.0:compile`
Out-of-bounds Write vulnerability in Apache Commons Configuration.This issue affects Apache Commons Configuration: from 2.0 before 2.10.1.

Users are recommended to upgrade to version 2.10.1, which fixes the issue.

#### References
* https://ossindex.sonatype.org/vulnerability/CVE-2024-29131?component-type=maven&component-name=org.apache.commons%2Fcommons-configuration2&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2024-29131
* https://lists.apache.org/thread/03nzzzjn4oknyw5y0871tw7ltj0t3r37
* https://github.com/advisories/GHSA-xjp4-hw94-mvp5

### CVE-2024-29133 (CWE-787) in dependency `org.apache.commons:commons-configuration2:jar:2.8.0:compile`
Out-of-bounds Write vulnerability in Apache Commons Configuration.This issue affects Apache Commons Configuration: from 2.0 before 2.10.1.

Users are recommended to upgrade to version 2.10.1, which fixes the issue.

#### References
* https://ossindex.sonatype.org/vulnerability/CVE-2024-29133?component-type=maven&component-name=org.apache.commons%2Fcommons-configuration2&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2024-29133
* https://lists.apache.org/thread/ccb9w15bscznh6tnp3wsvrrj9crbszh2

## Security

* #303: Fixed vulnerability CVE-2024-29131 in dependency `org.apache.commons:commons-configuration2:jar:2.8.0:compile`
* #304: Fixed vulnerability CVE-2024-29133 in dependency `org.apache.commons:commons-configuration2:jar:2.8.0:compile`

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Updated `com.exasol:parquet-io-java:2.0.6` to `2.0.7`
* Updated `com.google.cloud.bigdataoss:gcs-connector:1.9.4-hadoop3` to `3.0.0`
* Updated `com.google.guava:guava:32.1.3-jre` to `33.1.0-jre`
* Updated `com.google.oauth-client:google-oauth-client:1.34.1` to `1.35.0`
* Updated `com.google.protobuf:protobuf-java:3.25.1` to `4.26.0`
* Updated `io.dropwizard.metrics:metrics-core:4.2.23` to `4.2.25`
* Updated `io.grpc:grpc-netty:1.60.0` to `1.62.2`
* Updated `io.netty:netty-handler:4.1.101.Final` to `4.1.108.Final`
* Updated `org.alluxio:alluxio-core-client-hdfs:300` to `311`
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
* Updated `org.apache.orc:orc-core:1.9.2` to `2.0.0`
* Updated `org.apache.spark:spark-sql_2.13:3.4.1` to `3.5.1`
* Updated `org.jetbrains.kotlin:kotlin-stdlib:1.9.21` to `1.9.23`
* Updated `org.scala-lang:scala-library:2.13.11` to `2.13.13`
* Updated `org.slf4j:jul-to-slf4j:2.0.9` to `2.0.12`

#### Runtime Dependency Updates

* Updated `ch.qos.logback:logback-classic:1.2.13` to `1.5.3`
* Updated `ch.qos.logback:logback-core:1.2.13` to `1.5.3`

#### Test Dependency Updates

* Updated `com.dimafeng:testcontainers-scala-scalatest_2.13:0.41.0` to `0.41.3`
* Updated `com.exasol:exasol-testcontainers:7.0.0` to `7.0.1`
* Updated `com.exasol:extension-manager-integration-test-java:0.5.7` to `0.5.8`
* Updated `com.exasol:hamcrest-resultset-matcher:1.6.3` to `1.6.5`
* Updated `com.exasol:test-db-builder-java:3.5.3` to `3.5.4`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.15.4` to `3.16`
* Updated `org.glassfish.jersey.core:jersey-common:2.41` to `3.1.5`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.10.1` to `5.10.2`
* Updated `org.mockito:mockito-core:5.8.0` to `5.11.0`
* Updated `org.testcontainers:localstack:1.19.3` to `1.19.7`
