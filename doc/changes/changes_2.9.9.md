# Cloud Storage Extension 2.9.9, released 2026-??-??

Code name: Fixed vulnerabilities CVE-2026-118042, CVE-2026-100235, CVE-2026-100252, CVE-2026-100300, CVE-2026-100302, CVE-2026-115448

## Summary

This release fixes the following 6 vulnerabilities:

### CVE-2026-118042 (CWE-407) in dependency `com.google.guava:guava:jar:33.7.2-jre:compile`
Google Guava - Hash flooding vulnerability in HashBiMap and LinkedHashMultimap
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-118042?component-type=maven&component-name=com.google.guava%2Fguava&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-118042
* https://github.com/google/guava/security/advisories/GHSA-v5x9-67m3-7m56

### CVE-2026-100235 (CWE-367) in dependency `org.apache.zookeeper:zookeeper:jar:3.9.6:compile`
Incorrect authorization due to a time-of-check to time-of-use (TOCTOU) race condition in the setACL request handling of Apache ZooKeeper.

When a node's ACL is changed via setACL, the pending change is not published to the server's outstanding change record for that node. Any request that is prepared (and authorized) after the setACL is submitted but before it commits is therefore checked against the previous, pre-revocation ACL, yet is applied to the data tree after the ACL change has taken effect. A client that currently holds a permission can use this window to perform operations requiring that permission through and after its revocation â for example, committing writes or deletes after losing write access, or re-granting itself full permissions, which persistently defeats the revocation. In deployments transitioning from the default OPEN_ACL_UNSAFE ACL to a restrictive ACL, any unauthenticated client can exploit this during the transition.

This issue affects Apache ZooKeeper up to and including 3.9.6. Users are recommended to upgrade to version 3.8.8 or 3.9.7, which fixes the issue.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-100235?component-type=maven&component-name=org.apache.zookeeper%2Fzookeeper&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-100235
* https://lists.apache.org/thread.html/r40gp594o64mycwybc3v9dtvrm0vos0l

### CVE-2026-100252 (CWE-863) in dependency `org.apache.zookeeper:zookeeper:jar:3.9.6:compile`
Improper Authorization vulnerability in Apache ZooKeeper.

When SASL authentication is enabled on the client port, the server's SASL callback handler grants every authorization identity (authzid) that a client requests, without checking it against the principal the client authenticated as. The server then tries to replace the requested identity with a canonical form of the authenticated principal, derived from the zookeeper.security.auth_to_local rules. If no rule applies to the principal, canonicalization fails and the error is only logged. The client-requested authzid then becomes the session's SASL identity for all ACL checks.

An authenticated client whose principal is not covered by the auth_to_local rules can therefore claim any SASL identity. With the default rules, this includes any Kerberos principal from a realm other than the server's default realm, such as a principal from a cross-realm trusted realm. The client then gets the permissions granted to that identity in sasl ACLs. If the requested identity matches a configured zookeeper.superUser, it gets superuser access to the whole data tree.

Users are recommended to upgrade to version 3.8.8 or 3.9.7, which fixes the issue.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-100252?component-type=maven&component-name=org.apache.zookeeper%2Fzookeeper&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-100252
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/100xxx/CVE-2026-100252.json

### CVE-2026-100300 (CWE-755) in dependency `org.apache.zookeeper:zookeeper:jar:3.9.6:compile`
Improper handling of exceptional conditions in the quota subsystem of Apache ZooKeeper allows an attacker able to write under the /zookeeper/quota subtree â unauthenticated on default deployments, where that subtree is world-writable â to crash the server's critical request-processing thread and render the on-disk data directory permanently unbootable.

This issue affects Apache ZooKeeper up to and including 3.9.6. Users are recommended to upgrade to version 3.8.8 or 3.9.7, which fixes the issue.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-100300?component-type=maven&component-name=org.apache.zookeeper%2Fzookeeper&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-100300
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/100xxx/CVE-2026-100300.json

### CVE-2026-100302 (CWE-770) in dependency `org.apache.zookeeper:zookeeper:jar:3.9.6:compile`
Allocation of Resources Without Limits or Throttling in the multiRead request handler of Apache ZooKeeper allows a remote attacker to crash the server (denial of service) with a single small request.

The multiRead handler places no limit on the number of bundled read operations or on the cumulative size of the response it builds in memory. Because each operation's result is fully materialized and retained before serialization, a request that is small on the wire â within the jute.maxbuffer limit â but that references a large znode many times can force the server to allocate far more heap than the request size implies, exhausting the JVM heap and terminating the server with an OutOfMemoryError. Neither jute.maxbuffer nor largeRequestMaxBytes prevents this, as both bound only the inbound request, not the response.

This issue affects Apache ZooKeeper from 3.7.0 through 3.9.6. Users are recommended to upgrade to version 3.9.7, which enforces configurable limits on the multiRead operation count and cumulative response size.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-100302?component-type=maven&component-name=org.apache.zookeeper%2Fzookeeper&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-100302
* https://lists.apache.org/thread.html/gj2vxwx8myq31js6tvb7bgls4nnxohj7
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/100xxx/CVE-2026-100302.json

### CVE-2026-115448 (CWE-770) in dependency `org.apache.zookeeper:zookeeper:jar:3.9.6:compile`
Uncontrolled recursion in Apache ZooKeeper. The server serializes its data tree to a snapshot by recursing one stack frame per znode level, with no depth bound. A client permitted to create znodes (the default root ACL is open) can build a chain of nested znodes deep enough that taking a snapshot exhausts the JVM stack and kills the snapshot thread; sizes stay within jute.maxbuffer, so existing limits do not prevent it. This is a denial of service that becomes persistent: on restart the server rebuilds the deep tree from its transaction log and fails again while taking the startup snapshot, so it does not return to service, and recovery can entail data loss. The same log affects a leader and its followers, so it can spread across an ensemble.

This issue affects Apache ZooKeeper 3.8.0 through 3.8.7 and 3.9.0 through 3.9.6. Users are recommended to upgrade to 3.8.8 or 3.9.7, which fix the issue. As a workaround, use ACLs and authentication to restrict znode creation rather than relying on the default open root ACL.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-115448?component-type=maven&component-name=org.apache.zookeeper%2Fzookeeper&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-115448
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/115xxx/CVE-2026-115448.json

## Security

* #540: Fixed vulnerability CVE-2026-118042 in dependency `com.google.guava:guava:jar:33.7.2-jre:compile`
* #541: Fixed vulnerability CVE-2026-100235 in dependency `org.apache.zookeeper:zookeeper:jar:3.9.6:compile`
* #542: Fixed vulnerability CVE-2026-100252 in dependency `org.apache.zookeeper:zookeeper:jar:3.9.6:compile`
* #543: Fixed vulnerability CVE-2026-100300 in dependency `org.apache.zookeeper:zookeeper:jar:3.9.6:compile`
* #544: Fixed vulnerability CVE-2026-100302 in dependency `org.apache.zookeeper:zookeeper:jar:3.9.6:compile`
* #545: Fixed vulnerability CVE-2026-115448 in dependency `org.apache.zookeeper:zookeeper:jar:3.9.6:compile`

## Dependency Updates

### Compile Dependency Updates

* Updated `at.yawk.lz4:lz4-java:1.12.1` to `1.14.0`
* Updated `com.exasol:parquet-io-java:2.0.16` to `2.1.2`
* Updated `com.google.cloud.bigdataoss:gcs-connector:1.9.4-hadoop3` to `4.0.9`
* Updated `com.google.code.gson:gson:2.14.0` to `2.15.0`
* Updated `com.google.guava:guava:33.7.2-jre` to `33.8.0-jre`
* Updated `dnsjava:dnsjava:3.6.5` to `3.7.1`
* Updated `io.delta:delta-spark_2.13:3.3.2` to `4.4.1`
* Updated `io.grpc:grpc-netty:1.76.3` to `1.85.1`
* Updated `org.alluxio:alluxio-core-client-hdfs:300` to `313`
* Updated `org.apache.hadoop:hadoop-aws:3.4.3` to `3.5.0`
* Updated `org.apache.hadoop:hadoop-azure-datalake:3.4.3` to `3.5.0`
* Updated `org.apache.hadoop:hadoop-azure:3.4.3` to `3.5.0`
* Updated `org.apache.hadoop:hadoop-common:3.4.3` to `3.5.0`
* Updated `org.apache.hadoop:hadoop-hdfs-client:3.4.3` to `3.5.0`
* Updated `org.apache.hadoop:hadoop-hdfs:3.4.3` to `3.5.0`
* Updated `org.apache.orc:orc-core:1.9.9` to `2.3.1`
* Updated `org.apache.spark:spark-sql_2.13:3.5.9` to `4.3.0`
* Updated `org.apache.zookeeper:zookeeper:3.9.6` to `3.9.7`
* Updated `org.scala-lang:scala-library:2.13.18` to `3.10.0`
* Updated `software.amazon.awssdk:s3-transfer-manager:2.57.2` to `2.57.11`
* Updated `software.amazon.awssdk:s3:2.57.2` to `2.57.11`

### Runtime Dependency Updates

* Updated `ch.qos.logback:logback-classic:1.6.5` to `1.6.6`
* Updated `ch.qos.logback:logback-core:1.6.5` to `1.6.6`
* Updated `software.amazon.awssdk:apache-client:2.57.2` to `2.57.11`

### Test Dependency Updates

* Updated `com.exasol:test-db-builder-java:4.0.3` to `5.0.0`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.19.4` to `4.5.2`
* Updated `org.junit.jupiter:junit-jupiter:5.14.4` to `6.1.3`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:5.8.1` to `5.8.2`
