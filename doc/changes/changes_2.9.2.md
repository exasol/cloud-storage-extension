# Cloud Storage Extension 2.9.2, released 2025-??-??

Code name:

## Summary

## Features

* #365: Class import error on delta files

## Security

* Upgrade of transitive dependency to fix [CVE-2025-12183] CWE-125: Out-of-bounds Read (8.8); https://ossindex.sonatype.org/vulnerability/CVE-2025-12183?component-type=maven&component-name=org.lz4%2Flz4-java&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* Upgrade zookeeper dependency to fix [CVE-2025-58457] CWE-280: Improper Handling of Insufficient Permissions or Privileges  (5.3); https://ossindex.sonatype.org/vulnerability/CVE-2025-58457?component-type=maven&component-name=org.apache.zookeeper%2Fzookeeper&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Removed `io.delta:delta-core_2.13:2.4.0`
* Added `io.delta:delta-spark_2.13:3.3.2`
* Updated `org.apache.spark:spark-sql_2.13:3.4.1` to `3.5.7`
* Updated `org.apache.zookeeper:zookeeper:3.9.3` to `3.9.4`
* Added `org.lz4:lz4-java:1.8.1`

#### Plugin Dependency Updates

* Updated `com.exasol:artifact-reference-checker-maven-plugin:0.4.3` to `0.4.4`
* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.4` to `2.0.5`
* Updated `com.exasol:project-keeper-maven-plugin:5.2.3` to `5.4.4`
* Updated `com.exasol:quality-summarizer-maven-plugin:0.2.0` to `0.2.1`
* Updated `io.github.git-commit-id:git-commit-id-maven-plugin:9.0.1` to `9.0.2`
* Updated `org.apache.maven.plugins:maven-artifact-plugin:3.6.0` to `3.6.1`
* Updated `org.apache.maven.plugins:maven-assembly-plugin:3.7.1` to `3.8.0`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.14.0` to `3.14.1`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.5.0` to `3.6.2`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.3` to `3.5.4`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.4.2` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-resources-plugin:3.3.1` to `3.4.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.3` to `3.5.4`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.7.0` to `1.7.3`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.18.0` to `2.20.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.13` to `0.8.14`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.1.0.4751` to `5.5.0.6356`
