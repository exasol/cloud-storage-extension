# Cloud Storage Extension 2.8.1, released 2024-06-04

Code name: Security update - fix for CVE-2024-36114

## Summary

Fixed CVE-2024-36114  https://github.com/advisories/GHSA-973x-65j7-xcf4 via transitive version update.
Updated dependencies.

## Security

* #318: CVE-2024-36114: io.airlift:aircompressor:jar:0.21:compile

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Added `io.airlift:aircompressor:0.27`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:4.3.1` to `4.3.2`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.4.1` to `3.5.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:3.11.0.3922` to `4.0.0.4121`
