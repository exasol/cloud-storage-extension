# Cloud Storage Extension 2.9.4, released 2026-04-01

Code name: Update dependencies to fix CVEs

## Summary

This release updates the project dependencies to fix the following cves:
- CVE-2025-33042: org.apache.avro:avro:jar:1.12.0:compile
- CVE-2026-24308: org.apache.zookeeper:zookeeper:jar:3.9.4:compile
- CVE-2026-24281: org.apache.zookeeper:zookeeper:jar:3.9.4:compile
- CVE-2026-33871: io.netty:netty-codec-http2:jar:4.2.9.Final:compile
- CVE-2026-33870: io.netty:netty-codec-http:jar:4.2.9.Final:compile

## Security

* #369: CVE-2025-33042: org.apache.avro:avro:jar:1.12.0:compile
* #371: CVE-2026-24308: org.apache.zookeeper:zookeeper:jar:3.9.4:compile
* #373: CVE-2026-24281: org.apache.zookeeper:zookeeper:jar:3.9.4:compile
* #375: CVE-2026-33871: io.netty:netty-codec-http2:jar:4.2.9.Final:compile
* #376: CVE-2026-33870: io.netty:netty-codec-http:jar:4.2.9.Final:compile

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Updated `org.apache.avro:avro:1.12.0` to `1.12.1`
* Updated `org.apache.orc:orc-core:1.9.7` to `1.9.8`
* Updated `org.apache.zookeeper:zookeeper:3.9.4` to `3.9.5`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.5` to `2.0.6`
* Updated `com.exasol:project-keeper-maven-plugin:5.4.5` to `5.4.6`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.14.1` to `3.15.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.20.1` to `2.21.0`
