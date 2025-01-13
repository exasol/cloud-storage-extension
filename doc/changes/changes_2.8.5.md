# Cloud Storage Extension 2.8.5, released 2025-??-??

Code name: Security fixes

## Summary
Fixes two CVEs in transitive dependencies: CVE-2024-12801 and CVE-2024-12798

## Security
* #338: CVE-2024-12801: ch.qos.logback:logback-core:jar:1.5.12:runtime
* #337: CVE-2024-12798: ch.qos.logback:logback-core:jar:1.5.12:runtime

## Dependency Updates

### Cloud Storage Extension

#### Runtime Dependency Updates

* Updated `ch.qos.logback:logback-classic:1.5.12` to `1.5.16`
* Updated `ch.qos.logback:logback-core:1.5.12` to `1.5.16`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:4.4.0` to `4.5.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.1` to `3.5.2`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.9.1` to `3.21.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.1` to `3.5.2`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.17.1` to `2.18.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121` to `5.0.0.4389`
