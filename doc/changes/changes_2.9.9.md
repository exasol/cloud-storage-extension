# Cloud Storage Extension 2.9.9, released 2026-??-??

Code name: Fixed vulnerabilities CVE-2026-122096, CVE-2026-103611, CVE-2026-103612, CVE-2026-103613, CVE-2026-103614, CVE-2026-103616, CVE-2026-103617

## Summary

This release fixes the following 7 vulnerabilities:

### CVE-2026-122096 (CWE-400) in dependency `org.apache.datasketches:datasketches-java:jar:3.3.0:compile`
Uncontrolled Resource Consumption vulnerability in the sketch deserialization of Apache DataSketches Java.

Several deserializers trusted sizes and counts declared in the serialized input. A small crafted input could make an application allocate an excessive amount of memory, leading to an OutOfMemoryError. A crafted string passed to FrequentLongsSketch.getInstance(String) could also make deserialization, or a later update, loop indefinitely. Only applications that deserialize sketches from untrusted sources are affected.

This issue affects Apache DataSketches Java: through 9.0.0.

Users are recommended to upgrade to version 9.1.0, which fixes the issue.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-122096?component-type=maven&component-name=org.apache.datasketches%2Fdatasketches-java&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-122096
* https://github.com/apache/datasketches-java/pull/777
* https://github.com/apache/datasketches-java/pull/783
* https://lists.apache.org/thread/9wd7ngmho7hm8x74sq1p2fp1xgjd8kt6
* http://www.openwall.com/lists/oss-security/2026/11/10/29

### CVE-2026-103611 (CWE-407) in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.86:compile`
org.bouncycastle:bcprov - Inefficient Algorithmic Complexity
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-103611?component-type=maven&component-name=org.bouncycastle%2Fbcprov-jdk18on&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-103611
* https://github.com/bcgit/bc-java/blob/r1rv87/docs/releasenotes.md#215-security-advisories

### CVE-2026-103612 (CWE-347) in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.86:compile`
org.bouncycastle:bcprov - Improper Verification of Cryptographic Signature
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-103612?component-type=maven&component-name=org.bouncycastle%2Fbcprov-jdk18on&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-103612
* https://github.com/bcgit/bc-java/blob/r1rv87/docs/releasenotes.md#215-security-advisories

### CVE-2026-103613 (CWE-459) in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.86:compile`
org.bouncycastle:bcprov - Incomplete Cleanup
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-103613?component-type=maven&component-name=org.bouncycastle%2Fbcprov-jdk18on&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-103613
* https://github.com/bcgit/bc-java/blob/r1rv87/docs/releasenotes.md#215-security-advisories

### CVE-2026-103614 (CWE-203) in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.86:compile`
org.bouncycastle:bcprov - Observable Discrepancy
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-103614?component-type=maven&component-name=org.bouncycastle%2Fbcprov-jdk18on&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-103614
* https://github.com/bcgit/bc-java/blob/r1rv87/docs/releasenotes.md#215-security-advisories

### CVE-2026-103616 (CWE-203) in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.86:compile`
org.bouncycastle:bcprov - Observable Discrepancy
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-103616?component-type=maven&component-name=org.bouncycastle%2Fbcprov-jdk18on&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-103616
* https://github.com/bcgit/bc-java/blob/r1rv87/docs/releasenotes.md#215-security-advisories

### CVE-2026-103617 (CWE-299) in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.86:compile`
Bouncy Castle - Improper Check for Certificate Revocation
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-103617?component-type=maven&component-name=org.bouncycastle%2Fbcprov-jdk18on&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-103617
* https://github.com/bcgit/bc-java/blob/r1rv87/docs/releasenotes.md#215-security-advisories

## Security

* #547: Fixed vulnerability CVE-2026-122096 in dependency `org.apache.datasketches:datasketches-java:jar:3.3.0:compile`
* #548: Fixed vulnerability CVE-2026-103611 in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.86:compile`
* #549: Fixed vulnerability CVE-2026-103612 in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.86:compile`
* #550: Fixed vulnerability CVE-2026-103613 in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.86:compile`
* #551: Fixed vulnerability CVE-2026-103614 in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.86:compile`
* #552: Fixed vulnerability CVE-2026-103616 in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.86:compile`
* #553: Fixed vulnerability CVE-2026-103617 in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.86:compile`

## Dependency Updates

### Compile Dependency Updates

* Updated `at.yawk.lz4:lz4-java:1.12.1` to `1.14.0`
* Updated `com.exasol:parquet-io-java:2.0.16` to `2.1.2`
* Updated `com.google.cloud.bigdataoss:gcs-connector:1.9.4-hadoop3` to `4.0.10`
* Updated `com.google.code.gson:gson:2.14.0` to `2.15.0`
* Updated `com.google.guava:guava:33.7.2-jre` to `33.8.0-jre`
* Updated `dnsjava:dnsjava:3.6.5` to `3.7.1`
* Updated `io.delta:delta-spark_2.13:3.3.2` to `4.4.1`
* Updated `io.grpc:grpc-netty:1.76.3` to `1.85.1`
* Updated `org.alluxio:alluxio-core-client-hdfs:300` to `313`
* Updated `org.apache.commons:commons-lang3:3.21.0` to `3.22.0`
* Updated `org.apache.hadoop:hadoop-aws:3.4.3` to `3.5.0`
* Updated `org.apache.hadoop:hadoop-azure-datalake:3.4.3` to `3.5.0`
* Updated `org.apache.hadoop:hadoop-azure:3.4.3` to `3.5.0`
* Updated `org.apache.hadoop:hadoop-common:3.4.3` to `3.5.0`
* Updated `org.apache.hadoop:hadoop-hdfs-client:3.4.3` to `3.5.0`
* Updated `org.apache.hadoop:hadoop-hdfs:3.4.3` to `3.5.0`
* Updated `org.apache.orc:orc-core:1.9.9` to `2.3.1`
* Updated `org.apache.parquet:parquet-column:1.18.1` to `1.18.2`
* Updated `org.apache.parquet:parquet-common:1.18.1` to `1.18.2`
* Updated `org.apache.spark:spark-sql_2.13:3.5.9` to `4.3.0`
* Updated `org.apache.zookeeper:zookeeper:3.9.6` to `3.9.7`
* Updated `org.scala-lang:scala-library:2.13.18` to `3.10.1`
* Updated `software.amazon.awssdk:s3-transfer-manager:2.57.2` to `2.59.1`
* Updated `software.amazon.awssdk:s3:2.57.2` to `2.59.1`

### Runtime Dependency Updates

* Updated `ch.qos.logback:logback-classic:1.6.5` to `1.6.7`
* Updated `ch.qos.logback:logback-core:1.6.5` to `1.6.7`
* Updated `software.amazon.awssdk:apache-client:2.57.2` to `2.59.1`

### Test Dependency Updates

* Updated `com.exasol:test-db-builder-java:4.0.3` to `5.0.0`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.19.4` to `4.5.2`
* Updated `org.junit.jupiter:junit-jupiter:5.14.4` to `6.1.3`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:5.8.1` to `5.8.2`
