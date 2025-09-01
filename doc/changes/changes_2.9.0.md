# Cloud Storage Extension 2.9.0, released 2025-09-01

Code name: Upgrade of hadoop libraries

## Summary
This version upgrades hadoop from 3.3.6 to the latest 3.4.1, which fixes several CVEs in transient dependencies and
leverages all the improvements the recent hadoop libs have.

Security fixes which were fixed:

### CVE-2025-48924: org.apache.commons:commons-lang3:jar:3.17.0:compile

Uncontrolled Recursion vulnerability in Apache Commons Lang.

This issue affects Apache Commons Lang: Starting withÂ commons-lang:commons-langÂ 2.0 to 2.6, and, from org.apache.commons:commons-lang3 3.0 beforeÂ 3.18.0.

The methods ClassUtils.getClass(...) can throwÂ StackOverflowError on very long inputs. Because an Error is usually not handled by applications and libraries, a
StackOverflowError couldÂ cause an application to stop.

#### References
* https://ossindex.sonatype.org/vulnerability/CVE-2025-48924?component-type=maven&component-name=org.apache.commons%2Fcommons-lang3&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2025-48924
* GHSA-j288-q9x7-2f5v

### CVE-2025-53864: com.nimbusds:nimbus-jose-jwt:jar:9.47:compile

Connect2id Nimbus JOSE + JWT before 10.0.2 allows a remote attacker to cause a denial of service via a deeply nested JSON object supplied in a JWT claim set, because of uncontrolled recursion. NOTE: this is independent of the Gson 2.11.0 issue because the Connect2id product could have checked the JSON object nesting depth, regardless of what limits (if any) were imposed by Gson.

#### References
* https://ossindex.sonatype.org/vulnerability/CVE-2025-53864?component-type=maven&component-name=com.nimbusds%2Fnimbus-jose-jwt&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2025-53864
* https://bitbucket.org/connect2id/nimbus-jose-jwt/issues/583/stackoverflowerror-due-to-deeply-nested

### CVE-2025-55163: io.netty:netty-codec-http2:jar:4.1.119.Final:compile

Netty is an asynchronous, event-driven network application framework. Prior to versions 4.1.124.Final and 4.2.4.Final, Netty is vulnerable to MadeYouReset DDoS. This is a logical vulnerability in the HTTP/2 protocol, that uses malformed HTTP/2 control frames in order to break the max concurrent streams limit - which results in resource exhaustion and distributed denial of service. This issue has been patched in versions 4.1.124.Final and 4.2.4.Final.

#### References
* https://ossindex.sonatype.org/vulnerability/CVE-2025-55163?component-type=maven&component-name=io.netty%2Fnetty-codec-http2&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2025-55163
* GHSA-prj3-ccx8-p6x4

## Features

* #310: Upgrade spark and hadoop versions

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Added `com.google.code.gson:gson:2.13.1`
* Updated `io.netty:netty-codec-http2:4.1.119.Final` to `4.1.124.Final`
* Updated `org.apache.commons:commons-lang3:3.17.0` to `3.18.0`
* Updated `org.apache.hadoop:hadoop-aws:3.3.6` to `3.4.1`
* Updated `org.apache.hadoop:hadoop-azure-datalake:3.3.6` to `3.4.1`
* Updated `org.apache.hadoop:hadoop-azure:3.3.6` to `3.4.1`
* Updated `org.apache.hadoop:hadoop-common:3.3.6` to `3.4.1`
* Updated `org.apache.hadoop:hadoop-hdfs-client:3.3.6` to `3.4.1`
* Updated `org.apache.hadoop:hadoop-hdfs:3.3.6` to `3.4.1`
* Updated `org.apache.orc:orc-core:1.9.6` to `1.9.5`
* Added `software.amazon.awssdk:s3-transfer-manager:2.32.31`
* Added `software.amazon.awssdk:s3:2.32.31`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.3` to `2.0.4`
* Updated `com.exasol:project-keeper-maven-plugin:5.1.0` to `5.2.3`
