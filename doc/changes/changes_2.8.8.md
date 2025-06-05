# Cloud Storage Extension 2.8.8, released 2025-06-05

Code name: CVE fixes in transitive dependencies.

## Summary

Fixed two CVEs in transitive dependencies: CVE-2025-48734 and CVE-2025-47436.

## Features

* #351: CVE-2025-47436: org.apache.orc:orc-core:jar:1.9.5:compile
* #353: CVE-2025-48734: commons-beanutils:commons-beanutils:jar:1.9.4:compile

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Added `commons-beanutils:commons-beanutils:1.11.0`
* Updated `org.apache.orc:orc-core:1.9.5` to `1.9.6`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:5.0.0` to `5.1.0`
* Added `io.github.git-commit-id:git-commit-id-maven-plugin:9.0.1`
* Removed `io.github.zlika:reproducible-build-maven-plugin:0.17`
* Added `org.apache.maven.plugins:maven-artifact-plugin:3.6.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.2` to `3.5.3`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.2` to `3.5.3`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.12` to `0.8.13`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.0.0.4389` to `5.1.0.4751`
