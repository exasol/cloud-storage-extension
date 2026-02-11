# Cloud Storage Extension 2.9.3, released 2026-??-??

Code name: Upgrade protobuf dependency

## Summary

This release upgrades protobuf-java dependency to fix the HDFS access issue.

## Features

* #367: java.lang.IllegalAccessError: ... tried to access method 'org.apache.hadoop.thirdparty.protobuf.LazyStringArrayList

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Added `com.google.protobuf:protobuf-java-util:3.25.8`
* Updated `com.google.protobuf:protobuf-java:3.25.5` to `3.25.8`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:5.4.4` to `5.4.5`
