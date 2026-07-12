# Cloud Storage Extension 2.9.7, released 2026-??-??

Code name: Fixed vulnerabilities CVE-2026-54399, CVE-2026-9563, CVE-2026-49844, CVE-2026-57914, CVE-2026-54515, CVE-2026-59889, CVE-2026-54428, CVE-2026-10532, CVE-2026-13006

## Summary

This release fixes the following 9 vulnerabilities:

### CVE-2026-54399 (CWE-400) in dependency `org.apache.httpcomponents.core5:httpcore5:jar:5.4.2:runtime`
Uncontrolled Resource Consumption vulnerability in the HTTP/1.1 message parserÂ in Apache HttpComponents Core (5.4.2 and earlier, 5.5-beta1 and earlier) allowsÂ an remote attacker to cause a denial of service through memory exhaustion by sending messages with excessive number of headers / excessive header length

Sonatype's research suggests that this CVE's details differ from those defined at NVD. See https://guide.sonatype.com/vulnerability/CVE-2026-54399 for details
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-54399?component-type=maven&component-name=org.apache.httpcomponents.core5%2Fhttpcore5&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-54399
* https://lists.apache.org/thread/zmxh1pl2zohov5ntdh4lt85gfrlchgpy
* http://www.openwall.com/lists/oss-security/2026/07/01/4

### CVE-2026-9563 (CWE-400) in dependency `org.eclipse.parsson:parsson:jar:1.1.7:test`
In Eclipse Parsson published Maven Central artifacts before version 1.1.8, the JSON parser did not enforce a default maximum on the number of characters consumed while parsing a single JSON document. Applications that parse attacker- controlled JSON can be forced to consume excessive CPU and memory by processing very large documents, including large arrays, objects, strings, numbers, whitespace, or nested structures, resulting in a denial of service. Eclipse Parsson 1.1.8 introduces a configurable maximum parsing limit with a default limit of 15 million parser-consumed characters.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-9563?component-type=maven&component-name=org.eclipse.parsson%2Fparsson&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-9563
* https://github.com/eclipse-ee4j/parsson/pull/169
* https://gitlab.eclipse.org/security/vulnerability-reports/-/work_items/444

### CVE-2026-49844 (CWE-116) in dependency `org.apache.logging.log4j:log4j-api:jar:2.26.0:compile`
Improper encoding of non-finite floating-point values during MapMessage JSON serialization in Apache Log4j API produces output that is not valid JSON. This issue affects Apache Log4j API versions 2.13.1 through 2.25.4 and version 2.26.0.

The fix for CVE-2026-34481 did not cover all code paths: when a MapMessage contains a non-finite IEEE 754 value (NaN, Infinity, or -Infinity), MapMessage.asJson() emits the corresponding bare token. RFC 8259 does not permit these tokens, so a conformant parser rejects the resulting document.

The defect is reachable only when both of the following conditions hold:

  *  The application uses the  message resolver https://logging.apache.org/log4j/2.x/manual/json-template-layout.html#event-template-resolver-message  of JsonTemplateLayout or any other layout that relies on MapMessage.asJson() or MapMessage.getFormattedMessage(new String[]{"JSON"}).
  *  The application logs a MapMessage that contains an attacker-controlled floating-point value.

An attacker who can supply a non-finite value can cause the affected layout to emit malformed JSON, which may corrupt the enclosing log record or disrupt downstream log ingestion and parsing.

Users are advised to upgrade to Apache Log4j API 2.25.5 or 2.26.1, both of which emit RFC 8259-compliant JSON for non-finite values.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-49844?component-type=maven&component-name=org.apache.logging.log4j%2Flog4j-api&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-49844
* https://logging.apache.org/security.html#CVE-2026-49844

### CVE-2026-57914 (CWE-400) in dependency `org.apache.kerby:kerby-asn1:jar:2.0.3:compile`
By sending a deeply nested ASN1 structure to a Apache Kerby client or service, it's possible to trigger a StackOverFlow Exception which can lead to denial of service issues. Users are recommended to upgrade to version 2.1.2, which fixes this issue.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-57914?component-type=maven&component-name=org.apache.kerby%2Fkerby-asn1&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-57914
* http://www.openwall.com/lists/oss-security/2026/06/26/7
* https://lists.apache.org/thread/w98h2q8wz0bq97vhz4vf55hqomcb2j1m

### CVE-2026-54515 (CWE-915) in dependency `com.fasterxml.jackson.core:jackson-databind:jar:2.22.0:compile`
jackson-databind contains the general-purpose data-binding functionality and tree-model for Jackson Data Processor. From 2.8.0 until 2.18.9, 2.21.5, and 3.1.4, in BeanDeserializerBase.createContextual(), per-property @JsonIgnoreProperties exclusions are applied by _handleByNameInclusion(), producing a contextual deserializer whose BeanPropertyMap has the ignored properties removed. The subsequent per-property case-insensitivity block (triggered by @JsonFormat(ACCEPT_CASE_INSENSITIVE_PROPERTIES)) rebuilds from this._beanProperties (the original, unfiltered map) instead of contextual._beanProperties, then overwrites the filtered map â restoring every property _handleByNameInclusion had just removed. The ignored property becomes writable again. This vulnerability is fixed in 2.18.9, 2.21.5, and 3.1.4.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-54515?component-type=maven&component-name=com.fasterxml.jackson.core%2Fjackson-databind&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-54515
* https://github.com/FasterXML/jackson-databind/security/advisories/GHSA-5jmj-h7xm-6q6v

### CVE-2026-59889 (CWE-863) in dependency `com.fasterxml.jackson.core:jackson-databind:jar:2.22.0:compile`
Jackson Databind -  Authorization bypass on JsonView Setter/Field
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-59889?component-type=maven&component-name=com.fasterxml.jackson.core%2Fjackson-databind&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-59889
* https://github.com/FasterXML/jackson-databind/issues/6060
* https://github.com/FasterXML/jackson-databind/pull/6056

### CVE-2026-54428 (CWE-400) in dependency `org.apache.httpcomponents.core5:httpcore5-h2:jar:5.4:runtime`
Allocation of resources without limits or throttling in the HTTP/2 HPACK decoder in Apache HttpComponents Core (5.4.2 and earlier, 5.5-beta1 and earlier) allows an remote attacker to cause a denial of service through memory exhaustion by sending oversized compressed header blocks before the HTTP/2 SETTINGS acknowledgement causes the configured header list size limit to be applied.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-54428?component-type=maven&component-name=org.apache.httpcomponents.core5%2Fhttpcore5-h2&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-54428
* https://lists.apache.org/thread/5zjp8vczvxq19pw2rvhs21q446bhl0sd

### CVE-2026-10532 (CWE-502) in dependency `ch.qos.logback:logback-core:jar:1.5.34:runtime`
Deserialization of untrusted data vulnerability in QOS.CH Sarl logback logback-core (HardenedObjectInputStream (logback-core) modules) allows Object Injection, albeit heavily restricted.

More precisely, an attacker able to influence serialized data sent to 
SimpleSocketServer or SimpleSSLSocketServer can instantiate Proxy objects.

Although deserialization is heavily restricted by HardenedObjectInputStream and no 
practical way to achieve remote code execution or significant privilege 
escalation has been identified, this issue constitutes a bypass of the 
intended security restrictions.

This issue affects logback: through 1.5.33 inclusive.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-10532?component-type=maven&component-name=ch.qos.logback%2Flogback-core&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-10532
* https://github.com/advisories/GHSA-jhq6-gfmj-v8fx
* https://logback.qos.ch/news.html#1.5.34

### CVE-2026-13006 (CWE-20) in dependency `ch.qos.logback:logback-core:jar:1.5.34:runtime`
ACE vulnerability in conditional configuration file processing  by QOS.CH logback-core up to and including version 1.5.36 in Java applications, allows an attacker to execute arbitrary code circumventing existing protections against CVE-2025-11226 byÂ compromising an existing logback configuration file or by injecting an environment variable before program execution.

A successful attack requires the presence of Janino library to be present on the user's class path. In addition, the attacker mustÂ  have write access to a 
configuration file. Alternatively, the attacker could inject a malicious 
environment variable pointing to a malicious configuration file. In both 
cases, the attack requires existing privilege.

Please note that in logack version 1.5.37 conditional processing using Janino was removed.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-13006?component-type=maven&component-name=ch.qos.logback%2Flogback-core&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-13006
* https://logback.qos.ch/news.html#1.5.35

## Security

* #428: Fixed vulnerability CVE-2026-54399 in dependency `org.apache.httpcomponents.core5:httpcore5:jar:5.4.2:runtime`
* #429: Fixed vulnerability CVE-2026-9563 in dependency `org.eclipse.parsson:parsson:jar:1.1.7:test`
* #430: Fixed vulnerability CVE-2026-49844 in dependency `org.apache.logging.log4j:log4j-api:jar:2.26.0:compile`
* #431: Fixed vulnerability CVE-2026-57914 in dependency `org.apache.kerby:kerby-asn1:jar:2.0.3:compile`
* #432: Fixed vulnerability CVE-2026-54515 in dependency `com.fasterxml.jackson.core:jackson-databind:jar:2.22.0:compile`
* #433: Fixed vulnerability CVE-2026-59889 in dependency `com.fasterxml.jackson.core:jackson-databind:jar:2.22.0:compile`
* #434: Fixed vulnerability CVE-2026-54428 in dependency `org.apache.httpcomponents.core5:httpcore5-h2:jar:5.4:runtime`
* #435: Fixed vulnerability CVE-2026-10532 in dependency `ch.qos.logback:logback-core:jar:1.5.34:runtime`
* #436: Fixed vulnerability CVE-2026-13006 in dependency `ch.qos.logback:logback-core:jar:1.5.34:runtime`

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Updated `at.yawk.lz4:lz4-java:1.11.0` to `1.11.1`
* Updated `com.github.mwiede:jsch:2.28.2` to `2.28.4`
* Updated `com.google.cloud.bigdataoss:gcs-connector:1.9.4-hadoop3` to `4.0.4`
* Updated `io.delta:delta-spark_2.13:3.3.2` to `4.3.1`
* Updated `io.grpc:grpc-netty:1.76.3` to `1.82.2`
* Updated `org.alluxio:alluxio-core-client-hdfs:300` to `313`
* Updated `org.apache.hadoop:hadoop-aws:3.4.3` to `3.5.0`
* Updated `org.apache.hadoop:hadoop-azure-datalake:3.4.3` to `3.5.0`
* Updated `org.apache.hadoop:hadoop-azure:3.4.3` to `3.5.0`
* Updated `org.apache.hadoop:hadoop-common:3.4.3` to `3.5.0`
* Updated `org.apache.hadoop:hadoop-hdfs-client:3.4.3` to `3.5.0`
* Updated `org.apache.hadoop:hadoop-hdfs:3.4.3` to `3.5.0`
* Updated `org.apache.logging.log4j:log4j-1.2-api:2.26.0` to `2.26.1`
* Updated `org.apache.logging.log4j:log4j-api:2.26.0` to `2.26.1`
* Updated `org.apache.logging.log4j:log4j-core:2.26.0` to `2.26.1`
* Updated `org.apache.orc:orc-core:1.9.8` to `2.3.0`
* Updated `org.apache.spark:spark-sql_2.13:3.5.8` to `4.2.0-preview5`
* Updated `org.scala-lang:scala-library:2.13.18` to `3.8.4`
* Updated `software.amazon.awssdk:s3-transfer-manager:2.46.7` to `2.47.5`
* Updated `software.amazon.awssdk:s3:2.46.7` to `2.47.5`

#### Runtime Dependency Updates

* Updated `ch.qos.logback:logback-classic:1.5.34` to `1.5.38`
* Updated `ch.qos.logback:logback-core:1.5.34` to `1.5.38`
* Updated `software.amazon.awssdk:apache-client:2.46.7` to `2.47.5`

#### Test Dependency Updates

* Updated `com.exasol:extension-manager-integration-test-java:0.5.19` to `0.5.20`
* Updated `com.exasol:test-db-builder-java:4.0.0` to `4.0.1`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.19.4` to `4.5`
* Updated `org.junit.jupiter:junit-jupiter:5.14.4` to `6.1.2`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:5.6.2` to `5.7.3`
