# Cloud Storage Extension 2.9.4, released 2026-??-??

Code name: Fixed vulnerability CVE-2025-33042 in org.apache.avro:avro:jar:1.12.0:compile

## Summary

This release fixes the following vulnerability:

### CVE-2025-33042 (CWE-94) in dependency `org.apache.avro:avro:jar:1.12.0:compile`
Improper Control of Generation of Code ('Code Injection') vulnerability in Apache Avro Java SDK when generating specific records from untrusted Avro schemas.

This issue affects Apache Avro Java SDK: all versions through 1.11.4 and versionÂ 1.12.0.

Users are recommended to upgrade to version 1.12.1 or 1.11.5, which fix the issue.
#### References
* https://ossindex.sonatype.org/vulnerability/CVE-2025-33042?component-type=maven&component-name=org.apache.avro%2Favro&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2025-33042
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2025/33xxx/CVE-2025-33042.json
* https://github.com/advisories/GHSA-rp46-r563-jrc7
* https://gitlab.com/gitlab-org/advisories-community/-/blob/main/maven/org.apache.avro/avro/CVE-2025-33042.yml
* https://nvd.nist.gov/vuln/detail/CVE-2025-33042
* https://osv-vulnerabilities.storage.googleapis.com/Maven/GHSA-rp46-r563-jrc7.json

## Security

* #369: Fixed vulnerability CVE-2025-33042 in dependency `org.apache.avro:avro:jar:1.12.0:compile`

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Updated `at.yawk.lz4:lz4-java:1.10.2` to `1.10.3`
* Updated `com.exasol:error-reporting-java:1.0.1` to `1.0.2`
* Updated `com.exasol:parquet-io-java:2.0.12` to `2.0.13`
* Updated `com.github.mwiede:jsch:0.2.21` to `2.27.7`
* Updated `com.google.cloud.bigdataoss:gcs-connector:1.9.4-hadoop3` to `4.0.2`
* Updated `com.google.code.gson:gson:2.13.1` to `2.13.2`
* Updated `com.google.guava:guava:33.3.1-jre` to `33.5.0-jre`
* Updated `com.google.oauth-client:google-oauth-client:1.36.0` to `1.39.0`
* Updated `com.google.protobuf:protobuf-java-util:3.25.8` to `4.33.5`
* Updated `com.google.protobuf:protobuf-java:3.25.8` to `4.33.5`
* Updated `com.nimbusds:nimbus-jose-jwt:9.47` to `10.7`
* Updated `com.typesafe.scala-logging:scala-logging_2.13:3.9.5` to `3.9.6`
* Updated `commons-io:commons-io:2.18.0` to `2.21.0`
* Updated `dnsjava:dnsjava:3.6.2` to `3.6.4`
* Updated `io.delta:delta-spark_2.13:3.3.2` to `4.0.1`
* Updated `io.dropwizard.metrics:metrics-core:4.2.28` to `4.2.38`
* Updated `io.grpc:grpc-netty:1.65.1` to `1.79.0`
* Updated `org.alluxio:alluxio-core-client-hdfs:300` to `313`
* Updated `org.apache.avro:avro:1.12.0` to `1.12.1`
* Updated `org.apache.commons:commons-compress:1.27.1` to `1.28.0`
* Updated `org.apache.commons:commons-configuration2:2.11.0` to `2.13.0`
* Updated `org.apache.commons:commons-lang3:3.18.0` to `3.20.0`
* Updated `org.apache.hadoop:hadoop-aws:3.4.1` to `3.4.2`
* Updated `org.apache.hadoop:hadoop-azure-datalake:3.4.1` to `3.4.2`
* Updated `org.apache.hadoop:hadoop-azure:3.4.1` to `3.4.2`
* Updated `org.apache.hadoop:hadoop-common:3.4.1` to `3.4.2`
* Updated `org.apache.hadoop:hadoop-hdfs-client:3.4.1` to `3.4.2`
* Updated `org.apache.hadoop:hadoop-hdfs:3.4.1` to `3.4.2`
* Updated `org.apache.ivy:ivy:2.5.2` to `2.5.3`
* Updated `org.apache.orc:orc-core:1.9.7` to `2.2.2`
* Updated `org.apache.spark:spark-sql_2.13:3.5.7` to `4.2.0-preview2`
* Updated `org.glassfish.jersey.containers:jersey-container-servlet-core:2.47` to `3.1.11`
* Updated `org.glassfish.jersey.containers:jersey-container-servlet:2.47` to `3.1.11`
* Updated `org.glassfish.jersey.core:jersey-client:2.47` to `3.1.11`
* Updated `org.glassfish.jersey.core:jersey-common:2.47` to `3.1.11`
* Updated `org.glassfish.jersey.core:jersey-server:2.47` to `3.1.11`
* Updated `org.glassfish.jersey.inject:jersey-hk2:2.47` to `3.1.11`
* Updated `org.jetbrains.kotlin:kotlin-stdlib:1.9.25` to `2.3.10`
* Updated `org.scala-lang:scala-library:2.13.11` to `3.8.1`
* Updated `org.slf4j:jul-to-slf4j:2.0.16` to `2.0.17`
* Updated `org.xerial.snappy:snappy-java:1.1.10.7` to `1.1.10.8`
* Updated `software.amazon.awssdk:s3-transfer-manager:2.34.0` to `2.41.29`
* Updated `software.amazon.awssdk:s3:2.34.0` to `2.41.29`

#### Runtime Dependency Updates

* Updated `ch.qos.logback:logback-classic:1.5.29` to `1.5.31`
* Updated `ch.qos.logback:logback-core:1.5.29` to `1.5.31`

#### Test Dependency Updates

* Updated `com.dimafeng:testcontainers-scala-scalatest_2.13:0.41.4` to `0.44.1`
* Updated `com.exasol:exasol-testcontainers:7.1.4` to `7.2.2`
* Updated `com.exasol:extension-manager-integration-test-java:0.5.13` to `0.5.18`
* Updated `com.exasol:hamcrest-resultset-matcher:1.7.0` to `1.7.2`
* Updated `com.exasol:maven-project-version-getter:1.2.0` to `1.2.2`
* Updated `com.exasol:test-db-builder-java:3.6.0` to `3.6.4`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.17.3` to `4.3.1`
* Updated `org.junit.jupiter:junit-jupiter-api:5.10.3` to `6.0.3`
* Updated `org.mockito:mockito-core:5.12.0` to `5.21.0`
* Updated `org.testcontainers:localstack:1.20.3` to `1.21.4`
