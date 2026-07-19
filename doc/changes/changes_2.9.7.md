# Cloud Storage Extension 2.9.7, released 2026-??-??

Code name: Fixed vulnerabilities CVE-2026-59901, CVE-2026-56819, CVE-2026-59921

## Summary

This release fixes the following 3 vulnerabilities:

### CVE-2026-59901 (CWE-835) in dependency `io.netty:netty-codec-compression:jar:4.2.15.Final:compile`
netty-codec - Bzip2Decoder infinite loop DoS via malformed stream
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-59901?component-type=maven&component-name=io.netty%2Fnetty-codec-compression&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-59901
* https://github.com/netty/netty/security/advisories/GHSA-558v-64gr-wgg4

### CVE-2026-56819 (CWE-401) in dependency `io.netty:netty-codec-http2:jar:4.2.15.Final:compile`
netty-codec-http2 - Memory leak in HTTP/2 decompressor
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-56819?component-type=maven&component-name=io.netty%2Fnetty-codec-http2&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-56819
* https://github.com/netty/netty/security/advisories/GHSA-93wv-jw9v-4972

### CVE-2026-59921 (CWE-93) in dependency `io.netty:netty-codec-http:jar:4.2.15.Final:compile`
netty-codec-http - CRLF injection via multipart filename
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-59921?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-59921
* https://github.com/netty/netty/security/advisories/GHSA-gcjf-9mgh-3p7g

## Security

* #438: Fixed vulnerability CVE-2026-59901 in dependency `io.netty:netty-codec-compression:jar:4.2.15.Final:compile`
* #439: Fixed vulnerability CVE-2026-56819 in dependency `io.netty:netty-codec-http2:jar:4.2.15.Final:compile`
* #440: Fixed vulnerability CVE-2026-59921 in dependency `io.netty:netty-codec-http:jar:4.2.15.Final:compile`

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Updated `at.yawk.lz4:lz4-java:1.11.0` to `1.11.1`
* Updated `com.github.mwiede:jsch:2.28.2` to `2.28.4`
* Updated `com.google.cloud.bigdataoss:gcs-connector:1.9.4-hadoop3` to `4.0.4`
* Updated `io.delta:delta-spark_2.13:3.3.2` to `4.3.1`
* Updated `io.grpc:grpc-netty:1.76.3` to `1.82.2`
* Updated `org.alluxio:alluxio-core-client-hdfs:300` to `313`
* Updated `org.apache.hadoop:hadoop-aws:3.4.3` to `3.5.0`
* Updated `org.apache.hadoop:hadoop-azure-datalake:3.4.3` to `3.5.0`
* Updated `org.apache.hadoop:hadoop-azure:3.4.3` to `3.5.0`
* Updated `org.apache.hadoop:hadoop-common:3.4.3` to `3.5.0`
* Updated `org.apache.hadoop:hadoop-hdfs-client:3.4.3` to `3.5.0`
* Updated `org.apache.hadoop:hadoop-hdfs:3.4.3` to `3.5.0`
* Updated `org.apache.ivy:ivy:2.5.3` to `2.6.0`
* Updated `org.apache.logging.log4j:log4j-1.2-api:2.26.0` to `2.26.1`
* Updated `org.apache.logging.log4j:log4j-api:2.26.0` to `2.26.1`
* Updated `org.apache.logging.log4j:log4j-core:2.26.0` to `2.26.1`
* Updated `org.apache.orc:orc-core:1.9.8` to `2.3.1`
* Updated `org.apache.spark:spark-sql_2.13:3.5.8` to `4.2.0-preview5`
* Updated `org.scala-lang:scala-library:2.13.18` to `3.8.4`
* Updated `software.amazon.awssdk:s3-transfer-manager:2.46.7` to `2.48.3`
* Updated `software.amazon.awssdk:s3:2.46.7` to `2.48.3`

#### Runtime Dependency Updates

* Updated `ch.qos.logback:logback-classic:1.5.34` to `1.5.38`
* Updated `ch.qos.logback:logback-core:1.5.34` to `1.5.38`
* Updated `software.amazon.awssdk:apache-client:2.46.7` to `2.48.3`

#### Test Dependency Updates

* Updated `com.exasol:extension-manager-integration-test-java:0.5.19` to `0.5.20`
* Updated `com.exasol:test-db-builder-java:4.0.0` to `4.0.1`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.19.4` to `4.5`
* Updated `org.junit.jupiter:junit-jupiter:5.14.4` to `6.1.2`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:5.6.2` to `5.7.4`
