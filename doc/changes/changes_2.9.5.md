# Cloud Storage Extension 2.9.5, released 2026-??-??

Code name: Fixed vulnerability CVE-2026-5588 in org.bouncycastle:bcprov-jdk18on:jar:1.78.1:compile

## Summary

This release fixes the following vulnerability:

### CVE-2026-5588 (CWE-327) in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.78.1:compile`
: Use of a Broken or Risky Cryptographic Algorithm vulnerability in Legion of the Bouncy Castle Inc. BC-JAVA bcpkix on all (pkix modules).

PKIX draft CompositeVerifier accepts empty signature sequence as valid.

This issue affects BC-JAVA: from 1.49 before 1.84.
#### References
* https://ossindex.sonatype.org/vulnerability/CVE-2026-5588?component-type=maven&component-name=org.bouncycastle%2Fbcprov-jdk18on&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-5588
* https://github.com/bcgit/bc-java/wiki/CVE%E2%80%902026%E2%80%905588
* https://www.sonatype.com/products/sonatype-guide/oss-index-users

## Security

* #386: Fixed vulnerability CVE-2026-5588 in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.78.1:compile`

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
* Added `io.vertx:vertx-core:4.5.26`
* Updated `org.alluxio:alluxio-core-client-hdfs:300` to `313`
* Updated `org.apache.commons:commons-compress:1.27.1` to `1.28.0`
* Updated `org.apache.commons:commons-configuration2:2.11.0` to `2.14.0`
* Updated `org.apache.commons:commons-lang3:3.18.0` to `3.20.0`
* Updated `org.apache.ivy:ivy:2.5.2` to `2.5.3`
* Updated `org.apache.logging.log4j:log4j-1.2-api:2.25.3` to `2.25.4`
* Updated `org.apache.logging.log4j:log4j-api:2.25.3` to `2.25.4`
* Updated `org.apache.logging.log4j:log4j-core:2.25.3` to `2.25.4`
* Updated `org.apache.orc:orc-core:1.9.8` to `2.3.0`
* Updated `org.apache.spark:spark-sql_2.13:3.5.7` to `4.2.0-preview4`
* Added `org.bouncycastle:bcprov-jdk18on:1.84`
* Updated `org.glassfish.jersey.containers:jersey-container-servlet-core:2.47` to `3.1.11`
* Updated `org.glassfish.jersey.containers:jersey-container-servlet:2.47` to `3.1.11`
* Updated `org.glassfish.jersey.core:jersey-client:2.47` to `3.1.11`
* Updated `org.glassfish.jersey.core:jersey-common:2.47` to `3.1.11`
* Updated `org.glassfish.jersey.core:jersey-server:2.47` to `3.1.11`
* Updated `org.glassfish.jersey.inject:jersey-hk2:2.47` to `3.1.11`
* Updated `org.jetbrains.kotlin:kotlin-stdlib:1.9.25` to `2.3.20`
* Updated `org.scala-lang:scala-library:2.13.11` to `2.13.18`
* Updated `org.slf4j:jul-to-slf4j:2.0.16` to `2.0.17`
* Updated `org.xerial.snappy:snappy-java:1.1.10.7` to `1.1.10.8`
* Updated `software.amazon.awssdk:s3-transfer-manager:2.34.0` to `2.42.36`
* Updated `software.amazon.awssdk:s3:2.34.0` to `2.42.36`

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
* Updated `nl.jqno.equalsverifier:equalsverifier:3.17.3` to `4.5`
* Updated `org.junit.jupiter:junit-jupiter-api:5.10.3` to `6.0.3`
* Updated `org.mockito:mockito-core:5.12.0` to `5.23.0`
* Updated `org.testcontainers:localstack:1.20.3` to `1.21.4`
