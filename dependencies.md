<!-- @formatter:off -->
# Dependencies

## Cloud Storage Extension

### Compile Dependencies

| Dependency                                 | License                                       |
| ------------------------------------------ | --------------------------------------------- |
| [Scala Library][0]                         | [Apache-2.0][1]                               |
| [Apache Commons Lang][2]                   | [Apache-2.0][3]                               |
| [Guava: Google Core Libraries for Java][4] | [Apache License, Version 2.0][5]              |
| [io.grpc:grpc-netty][6]                    | [Apache 2.0][7]                               |
| [Netty/Handler][8]                         | [Apache License, Version 2.0][1]              |
| [snappy-java][9]                           | [Apache-2.0][10]                              |
| [Import Export UDF Common Scala][11]       | [MIT License][12]                             |
| [error-reporting-java][13]                 | [MIT License][14]                             |
| Apache Hadoop Common                       | [Apache License, Version 2.0][3]              |
| Apache Hadoop Amazon Web Services support  | [Apache License, Version 2.0][3]              |
| [Apache ZooKeeper - Server][15]            | [Apache License, Version 2.0][3]              |
| Apache Hadoop Azure support                | [Apache License, Version 2.0][3]              |
| Apache Hadoop Azure Data Lake support      | [Apache License, Version 2.0][3]              |
| Apache Hadoop HDFS                         | [Apache License, Version 2.0][3]              |
| Apache Hadoop HDFS Client                  | [Apache License, Version 2.0][3]              |
| [Kotlin Stdlib][16]                        | [The Apache License, Version 2.0][5]          |
| [Alluxio Core - Client - HDFS][17]         | [Apache License][18]                          |
| [Metrics Core][19]                         | [Apache License 2.0][10]                      |
| [Protocol Buffers [Core]][20]              | [BSD-3-Clause][21]                            |
| [gcs-connector-hadoop3][22]                | [Apache License, Version 2.0][5]              |
| [Google OAuth Client Library for Java][23] | [The Apache Software License, Version 2.0][3] |
| [ORC Core][24]                             | [Apache License, Version 2.0][3]              |
| [Apache Avro][25]                          | [Apache-2.0][3]                               |
| [Apache Commons Compress][26]              | [Apache-2.0][3]                               |
| [delta-core][27]                           | [Apache-2.0][28]                              |
| [Spark Project SQL][29]                    | [Apache 2.0 License][30]                      |
| [Apache Ivy][31]                           | [The Apache Software License, Version 2.0][5] |
| [Parquet for Java][32]                     | [MIT License][33]                             |
| [JUL to SLF4J bridge][34]                  | [MIT License][35]                             |
| [Apache Log4j API][36]                     | [Apache-2.0][3]                               |
| [Apache Log4j 1.x Compatibility API][37]   | [Apache-2.0][3]                               |
| [Apache Log4j Core][38]                    | [Apache-2.0][3]                               |
| [scala-logging][39]                        | [Apache 2.0 License][30]                      |

### Test Dependencies

| Dependency                                 | License                                                                                                                                        |
| ------------------------------------------ | ---------------------------------------------------------------------------------------------------------------------------------------------- |
| [scalatest][40]                            | [the Apache License, ASL Version 2.0][28]                                                                                                      |
| [scalatestplus-mockito][41]                | [Apache-2.0][28]                                                                                                                               |
| [mockito-core][42]                         | [MIT][43]                                                                                                                                      |
| [Hamcrest][44]                             | [BSD License 3][45]                                                                                                                            |
| [testcontainers-scala-scalatest][46]       | [The MIT License (MIT)][43]                                                                                                                    |
| [Testcontainers :: Localstack][47]         | [MIT][48]                                                                                                                                      |
| [Test containers for Exasol on Docker][49] | [MIT License][50]                                                                                                                              |
| [Test Database Builder for Java][51]       | [MIT License][52]                                                                                                                              |
| [Matcher for SQL Result Sets][53]          | [MIT License][54]                                                                                                                              |
| [EqualsVerifier \| release normal jar][55] | [Apache License, Version 2.0][3]                                                                                                               |
| [JUnit Jupiter Engine][56]                 | [Eclipse Public License v2.0][57]                                                                                                              |
| [Maven Project Version Getter][58]         | [MIT License][59]                                                                                                                              |
| [Extension integration tests library][60]  | [MIT License][61]                                                                                                                              |
| [jersey-core-common][62]                   | [EPL 2.0][63]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][64]; [Apache License, 2.0][30]; [Public Domain][65] |

### Runtime Dependencies

| Dependency                   | License                                                                       |
| ---------------------------- | ----------------------------------------------------------------------------- |
| [Logback Classic Module][66] | [Eclipse Public License - v 1.0][67]; [GNU Lesser General Public License][68] |
| [Logback Core Module][69]    | [Eclipse Public License - v 1.0][67]; [GNU Lesser General Public License][68] |

### Plugin Dependencies

| Dependency                                              | License                                       |
| ------------------------------------------------------- | --------------------------------------------- |
| [SonarQube Scanner for Maven][70]                       | [GNU LGPL 3][71]                              |
| [Apache Maven Compiler Plugin][72]                      | [Apache-2.0][3]                               |
| [Apache Maven Enforcer Plugin][73]                      | [Apache-2.0][3]                               |
| [Maven Flatten Plugin][74]                              | [Apache Software Licenese][3]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][75] | [ASL2][5]                                     |
| [scala-maven-plugin][76]                                | [Public domain (Unlicense)][77]               |
| [ScalaTest Maven Plugin][78]                            | [the Apache License, ASL Version 2.0][28]     |
| [Apache Maven Javadoc Plugin][79]                       | [Apache-2.0][3]                               |
| [Maven Surefire Plugin][80]                             | [Apache-2.0][3]                               |
| [Versions Maven Plugin][81]                             | [Apache License, Version 2.0][3]              |
| [duplicate-finder-maven-plugin Maven Mojo][82]          | [Apache License 2.0][30]                      |
| [Apache Maven Assembly Plugin][83]                      | [Apache-2.0][3]                               |
| [Apache Maven JAR Plugin][84]                           | [Apache License, Version 2.0][3]              |
| [Artifact reference checker and unifier][85]            | [MIT License][86]                             |
| [Maven Failsafe Plugin][87]                             | [Apache-2.0][3]                               |
| [JaCoCo :: Maven Plugin][88]                            | [Eclipse Public License 2.0][89]              |
| [error-code-crawler-maven-plugin][90]                   | [MIT License][91]                             |
| [Reproducible Build Maven Plugin][92]                   | [Apache 2.0][5]                               |
| [Project Keeper Maven plugin][93]                       | [The MIT License][94]                         |
| [OpenFastTrace Maven Plugin][95]                        | [GNU General Public License v3.0][96]         |
| [Scalastyle Maven Plugin][97]                           | [Apache 2.0][30]                              |
| [spotless-maven-plugin][98]                             | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][99]                             | [BSD-3-Clause][21]                            |
| [Exec Maven Plugin][100]                                | [Apache License 2][3]                         |
| [Apache Maven Clean Plugin][101]                        | [Apache-2.0][3]                               |

## Extension

### Compile Dependencies

| Dependency                                 | License |
| ------------------------------------------ | ------- |
| [@exasol/extension-manager-interface][102] | MIT     |

[0]: https://www.scala-lang.org/
[1]: https://www.apache.org/licenses/LICENSE-2.0
[2]: https://commons.apache.org/proper/commons-lang/
[3]: https://www.apache.org/licenses/LICENSE-2.0.txt
[4]: https://github.com/google/guava
[5]: http://www.apache.org/licenses/LICENSE-2.0.txt
[6]: https://github.com/grpc/grpc-java
[7]: https://opensource.org/licenses/Apache-2.0
[8]: https://netty.io/netty-handler/
[9]: https://github.com/xerial/snappy-java
[10]: https://www.apache.org/licenses/LICENSE-2.0.html
[11]: https://github.com/exasol/import-export-udf-common-scala/
[12]: https://github.com/exasol/import-export-udf-common-scala/blob/main/LICENSE
[13]: https://github.com/exasol/error-reporting-java/
[14]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[15]: http://zookeeper.apache.org/zookeeper
[16]: https://kotlinlang.org/
[17]: https://www.alluxio.io/alluxio-dora/alluxio-core/alluxio-core-client/alluxio-core-client-hdfs/
[18]: https://github.com/alluxio/alluxio/blob/master/LICENSE
[19]: https://metrics.dropwizard.io/metrics-core
[20]: https://developers.google.com/protocol-buffers/protobuf-java/
[21]: https://opensource.org/licenses/BSD-3-Clause
[22]: https://github.com/GoogleCloudPlatform/BigData-interop/gcs-connector/
[23]: https://github.com/googleapis/google-oauth-java-client/google-oauth-client
[24]: https://orc.apache.org/orc-core
[25]: https://avro.apache.org
[26]: https://commons.apache.org/proper/commons-compress/
[27]: https://delta.io/
[28]: http://www.apache.org/licenses/LICENSE-2.0
[29]: https://spark.apache.org/
[30]: http://www.apache.org/licenses/LICENSE-2.0.html
[31]: http://ant.apache.org/ivy/
[32]: https://github.com/exasol/parquet-io-java/
[33]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[34]: http://www.slf4j.org
[35]: http://www.opensource.org/licenses/mit-license.php
[36]: https://logging.apache.org/log4j/2.x/log4j/log4j-api/
[37]: https://logging.apache.org/log4j/2.x/log4j/log4j-1.2-api/
[38]: https://logging.apache.org/log4j/2.x/log4j/log4j-core/
[39]: https://github.com/lightbend/scala-logging
[40]: http://www.scalatest.org
[41]: https://github.com/scalatest/scalatestplus-mockito
[42]: https://github.com/mockito/mockito
[43]: https://opensource.org/licenses/MIT
[44]: http://hamcrest.org/JavaHamcrest/
[45]: http://opensource.org/licenses/BSD-3-Clause
[46]: https://github.com/testcontainers/testcontainers-scala
[47]: https://java.testcontainers.org
[48]: http://opensource.org/licenses/MIT
[49]: https://github.com/exasol/exasol-testcontainers/
[50]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[51]: https://github.com/exasol/test-db-builder-java/
[52]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[53]: https://github.com/exasol/hamcrest-resultset-matcher/
[54]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[55]: https://www.jqno.nl/equalsverifier
[56]: https://junit.org/junit5/
[57]: https://www.eclipse.org/legal/epl-v20.html
[58]: https://github.com/exasol/maven-project-version-getter/
[59]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[60]: https://github.com/exasol/extension-manager/
[61]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[62]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-common
[63]: http://www.eclipse.org/legal/epl-2.0
[64]: https://www.gnu.org/software/classpath/license.html
[65]: https://creativecommons.org/publicdomain/zero/1.0/
[66]: http://logback.qos.ch/logback-classic
[67]: http://www.eclipse.org/legal/epl-v10.html
[68]: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
[69]: http://logback.qos.ch/logback-core
[70]: http://sonarsource.github.io/sonar-scanner-maven/
[71]: http://www.gnu.org/licenses/lgpl.txt
[72]: https://maven.apache.org/plugins/maven-compiler-plugin/
[73]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[74]: https://www.mojohaus.org/flatten-maven-plugin/
[75]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[76]: http://github.com/davidB/scala-maven-plugin
[77]: http://unlicense.org/
[78]: https://www.scalatest.org/user_guide/using_the_scalatest_maven_plugin
[79]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[80]: https://maven.apache.org/surefire/maven-surefire-plugin/
[81]: https://www.mojohaus.org/versions/versions-maven-plugin/
[82]: https://basepom.github.io/duplicate-finder-maven-plugin
[83]: https://maven.apache.org/plugins/maven-assembly-plugin/
[84]: https://maven.apache.org/plugins/maven-jar-plugin/
[85]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[86]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[87]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[88]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[89]: https://www.eclipse.org/legal/epl-2.0/
[90]: https://github.com/exasol/error-code-crawler-maven-plugin/
[91]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[92]: http://zlika.github.io/reproducible-build-maven-plugin
[93]: https://github.com/exasol/project-keeper/
[94]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[95]: https://github.com/itsallcode/openfasttrace-maven-plugin
[96]: https://www.gnu.org/licenses/gpl-3.0.html
[97]: http://www.scalastyle.org
[98]: https://github.com/diffplug/spotless
[99]: https://github.com/evis/scalafix-maven-plugin
[100]: https://www.mojohaus.org/exec-maven-plugin
[101]: https://maven.apache.org/plugins/maven-clean-plugin/
[102]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.4.0.tgz
