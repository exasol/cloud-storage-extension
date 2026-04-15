# Cloud Storage Extension 2.9.5, released 2026-??-??

Code name: Fixed vulnerabilities CVE-2026-34477, CVE-2026-34479

## Summary

This release fixes the following 2 vulnerabilities:

### CVE-2026-34477 (CWE-297) in dependency `org.apache.logging.log4j:log4j-core:jar:2.25.3:compile`
The fix for  CVE-2025-68161 https://logging.apache.org/security.html#CVE-2025-68161  was incomplete: it addressed hostname verification only when enabled via the  log4j2.sslVerifyHostName https://logging.apache.org/log4j/2.x/manual/systemproperties.html#log4j2.sslVerifyHostName  system property, but not when configured through the  verifyHostName https://logging.apache.org/log4j/2.x/manual/appenders/network.html#SslConfiguration-attr-verifyHostName  attribute of the <Ssl> element.

Although the verifyHostName configuration attribute was introduced in Log4j Core 2.12.0, it was silently ignored in all versions through 2.25.3, leaving TLS connections vulnerable to interception regardless of the configured value.

A network-based attacker may be able to perform a man-in-the-middle attack when all of the following conditions are met:

  *  An SMTP, Socket, or Syslog appender is in use.
  *  TLS is configured via a nested <Ssl> element.
  *  The attacker can present a certificate issued by a CA trusted by the appender's configured trust store, or by the default Java trust store if none is configured.
This issue does not affect users of the HTTP appender, which uses a separate  verifyHostname https://logging.apache.org/log4j/2.x/manual/appenders/network.html#HttpAppender-attr-verifyHostName  attribute that was not subject to this bug and verifies host names by default.

Users are advised to upgrade to Apache Log4j Core 2.25.4, which corrects this issue.
#### References
* https://ossindex.sonatype.org/vulnerability/CVE-2026-34477?component-type=maven&component-name=org.apache.logging.log4j%2Flog4j-core&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-34477
* https://github.com/apache/logging-log4j2/pull/4075
* https://lists.apache.org/thread/lkx8cl46t2bvkcwfcb2pd43ygc097lq4
* https://logging.apache.org/cyclonedx/vdr.xml
* https://logging.apache.org/security.html#CVE-2026-34477
* https://www.sonatype.com/products/sonatype-guide/oss-index-users

### CVE-2026-34479 (CWE-116) in dependency `org.apache.logging.log4j:log4j-1.2-api:jar:2.25.3:compile`
The Log4j1XmlLayout from the Apache Log4j 1-to-Log4j 2 bridge fails to escape characters forbidden by the XML 1.0 standard, producing malformed XML output. Conforming XML parsers are required to reject documents containing such characters with a fatal error, which may cause downstream log processing systems to drop or fail to index affected records.

Two groups of users are affected:

  *  Those using Log4j1XmlLayout directly in a Log4j Core 2 configuration file.
  *  Those using the Log4j 1 configuration compatibility layer with org.apache.log4j.xml.XMLLayout specified as the layout class.

Users are advised to upgrade to Apache Log4j 1-to-Log4j 2 bridge version 2.25.4, which corrects this issue.

Note: The Apache Log4j 1-to-Log4j 2 bridge is deprecated and will not be present in Log4j 3. Users are encouraged to consult the  Log4j 1 to Log4j 2 migration guide https://logging.apache.org/log4j/2.x/migrate-from-log4j1.html , and specifically the section on eliminating reliance on the bridge.
#### References
* https://ossindex.sonatype.org/vulnerability/CVE-2026-34479?component-type=maven&component-name=org.apache.logging.log4j%2Flog4j-1.2-api&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-34479
* https://github.com/advisories/GHSA-h383-gmxw-35v2
* https://www.sonatype.com/products/sonatype-guide/oss-index-users

## Security

* #381: Fixed vulnerability CVE-2026-34477 in dependency `org.apache.logging.log4j:log4j-core:jar:2.25.3:compile`
* #382: Fixed vulnerability CVE-2026-34479 in dependency `org.apache.logging.log4j:log4j-1.2-api:jar:2.25.3:compile`

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Updated `at.yawk.lz4:lz4-java:1.10.2` to `1.11.0`
* Updated `com.exasol:error-reporting-java:1.0.1` to `1.0.2`
* Updated `com.exasol:parquet-io-java:2.0.12` to `2.0.13`
* Updated `com.github.mwiede:jsch:0.2.21` to `2.28.0`
* Updated `com.google.cloud.bigdataoss:gcs-connector:1.9.4-hadoop3` to `4.0.4`
* Updated `com.google.code.gson:gson:2.13.1` to `2.13.2`
* Updated `com.google.guava:guava:33.3.1-jre` to `33.6.0-jre`
* Updated `com.google.oauth-client:google-oauth-client:1.36.0` to `1.39.0`
* Updated `com.google.protobuf:protobuf-java-util:3.25.8` to `4.34.1`
* Updated `com.google.protobuf:protobuf-java:3.25.8` to `4.34.1`
* Updated `com.nimbusds:nimbus-jose-jwt:9.47` to `10.9`
* Updated `com.typesafe.scala-logging:scala-logging_2.13:3.9.5` to `3.9.6`
* Updated `commons-io:commons-io:2.18.0` to `2.21.0`
* Updated `dnsjava:dnsjava:3.6.2` to `3.6.4`
* Updated `io.delta:delta-spark_2.13:3.3.2` to `4.2.0`
* Updated `io.dropwizard.metrics:metrics-core:4.2.28` to `4.2.38`
* Updated `io.grpc:grpc-netty:1.65.1` to `1.80.0`
* Updated `org.alluxio:alluxio-core-client-hdfs:300` to `313`
* Updated `org.apache.commons:commons-compress:1.27.1` to `1.28.0`
* Updated `org.apache.commons:commons-configuration2:2.11.0` to `2.14.0`
* Updated `org.apache.commons:commons-lang3:3.18.0` to `3.20.0`
* Updated `org.apache.hadoop:hadoop-aws:3.4.1` to `3.5.0`
* Updated `org.apache.hadoop:hadoop-azure-datalake:3.4.1` to `3.5.0`
* Updated `org.apache.hadoop:hadoop-azure:3.4.1` to `3.5.0`
* Updated `org.apache.hadoop:hadoop-common:3.4.1` to `3.5.0`
* Updated `org.apache.hadoop:hadoop-hdfs-client:3.4.1` to `3.5.0`
* Updated `org.apache.hadoop:hadoop-hdfs:3.4.1` to `3.5.0`
* Updated `org.apache.ivy:ivy:2.5.2` to `2.5.3`
* Updated `org.apache.logging.log4j:log4j-1.2-api:2.25.3` to `2.25.4`
* Updated `org.apache.logging.log4j:log4j-api:2.25.3` to `2.25.4`
* Updated `org.apache.logging.log4j:log4j-core:2.25.3` to `2.25.4`
* Updated `org.apache.orc:orc-core:1.9.8` to `2.3.0`
* Updated `org.apache.spark:spark-sql_2.13:3.5.7` to `4.2.0-preview4`
* Updated `org.glassfish.jersey.containers:jersey-container-servlet-core:2.47` to `3.1.11`
* Updated `org.glassfish.jersey.containers:jersey-container-servlet:2.47` to `3.1.11`
* Updated `org.glassfish.jersey.core:jersey-client:2.47` to `3.1.11`
* Updated `org.glassfish.jersey.core:jersey-common:2.47` to `3.1.11`
* Updated `org.glassfish.jersey.core:jersey-server:2.47` to `3.1.11`
* Updated `org.glassfish.jersey.inject:jersey-hk2:2.47` to `3.1.11`
* Updated `org.jetbrains.kotlin:kotlin-stdlib:1.9.25` to `2.3.20`
* Updated `org.scala-lang:scala-library:2.13.11` to `3.8.3`
* Updated `org.slf4j:jul-to-slf4j:2.0.16` to `2.0.17`
* Updated `org.xerial.snappy:snappy-java:1.1.10.7` to `1.1.10.8`
* Updated `software.amazon.awssdk:s3-transfer-manager:2.34.0` to `2.42.34`
* Updated `software.amazon.awssdk:s3:2.34.0` to `2.42.34`

#### Runtime Dependency Updates

* Updated `ch.qos.logback:logback-classic:1.5.29` to `1.5.32`
* Updated `ch.qos.logback:logback-core:1.5.29` to `1.5.32`

#### Test Dependency Updates

* Updated `com.dimafeng:testcontainers-scala-scalatest_2.13:0.41.4` to `0.44.1`
* Updated `com.exasol:exasol-testcontainers:7.1.4` to `7.2.3`
* Updated `com.exasol:extension-manager-integration-test-java:0.5.13` to `0.5.19`
* Updated `com.exasol:hamcrest-resultset-matcher:1.7.0` to `1.7.2`
* Updated `com.exasol:maven-project-version-getter:1.2.0` to `1.2.2`
* Updated `com.exasol:test-db-builder-java:3.6.0` to `3.6.4`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.17.3` to `4.4.2`
* Updated `org.junit.jupiter:junit-jupiter-api:5.10.3` to `6.0.3`
* Updated `org.mockito:mockito-core:5.12.0` to `5.23.0`
* Updated `org.testcontainers:localstack:1.20.3` to `1.21.4`
