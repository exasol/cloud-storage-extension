# Cloud Storage Extension 2.7.9, released 2023-??-??

Code name: Fix CVE-2023-6378

## Summary

This release fixes vulnerability CVE-2023-6378 (CWE-502: Deserialization of Untrusted Data (7.1)) in the following dependencies:
* `ch.qos.logback:logback-classic:jar:1.2.10:compile`
* `ch.qos.logback:logback-core:jar:1.2.10:compile`

## Security

* #288: Fixed CVE-2023-6378 in `ch.qos.logback:logback-core:jar:1.2.10:compile`
* #289: Fixed CVE-2023-6378 in `ch.qos.logback:logback-classic:jar:1.2.10:compile`

## Dependency Updates

### Cloud Storage Extension

#### Runtime Dependency Updates

* Added `ch.qos.logback:logback-classic:1.2.13`
* Added `ch.qos.logback:logback-core:1.2.13`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:2.9.15` to `2.9.17`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.1.2` to `3.2.2`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.1.2` to `3.2.2`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.1` to `2.16.2`
