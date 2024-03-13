# Cloud Storage Extension 2.7.10, released 2024-??-??

Code name: Security fixes in transitive dependencies

## Summary
Fix CVEs in transitive dependencies, upgrade of PK to 4.1.0

## Features

* #294: CVE-2023-52428: com.nimbusds:nimbus-jose-jwt:jar:9.8.1:compile
* #295: CVE-2024-25710: org.apache.commons:commons-compress
* #296: CVE-2024-26308: org.apache.commons:commons-compress

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Added `com.nimbusds:nimbus-jose-jwt:9.37.3`
* Updated `org.apache.commons:commons-compress:1.25.0` to `1.26.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.3.1` to `2.0.0`
* Updated `com.exasol:project-keeper-maven-plugin:2.9.17` to `4.2.0`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.11.0` to `3.12.1`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.2.2` to `3.2.5`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.2.2` to `3.2.5`
* Added `org.apache.maven.plugins:maven-toolchains-plugin:3.1.0`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.5.0` to `1.6.0`
