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
| Apache Hadoop Common                       | [Apache-2.0][3]                               |
| Apache Hadoop Amazon Web Services support  | [Apache-2.0][3]                               |
| [Apache ZooKeeper - Server][15]            | [Apache License, Version 2.0][3]              |
| Apache Hadoop Azure support                | [Apache-2.0][3]                               |
| Apache Hadoop Azure Data Lake support      | [Apache-2.0][3]                               |
| Apache Hadoop HDFS                         | [Apache-2.0][3]                               |
| Apache Hadoop HDFS Client                  | [Apache-2.0][3]                               |
| [Kotlin Stdlib][16]                        | [The Apache License, Version 2.0][5]          |
| [Alluxio Core - Client - HDFS][17]         | [Apache License][18]                          |
| [Metrics Core][19]                         | [Apache License 2.0][10]                      |
| [Protocol Buffers [Core]][20]              | [BSD-3-Clause][21]                            |
| [gcs-connector][22]                        | [Apache License, Version 2.0][3]              |
| [Google OAuth Client Library for Java][23] | [The Apache Software License, Version 2.0][3] |
| [ORC Core][24]                             | [Apache License, Version 2.0][3]              |
| [Apache Avro][25]                          | [Apache-2.0][3]                               |
| [Apache Commons Compress][26]              | [Apache-2.0][3]                               |
| [Nimbus JOSE+JWT][27]                      | [The Apache Software License, Version 2.0][3] |
| [delta-core][28]                           | [Apache-2.0][29]                              |
| [Spark Project SQL][30]                    | [Apache-2.0][31]                              |
| [Apache Ivy][32]                           | [The Apache Software License, Version 2.0][5] |
| [Parquet for Java][33]                     | [MIT License][34]                             |
| [JUL to SLF4J bridge][35]                  | [MIT License][36]                             |
| [Apache Log4j API][37]                     | [Apache-2.0][3]                               |
| [Apache Log4j 1.x Compatibility API][38]   | [Apache-2.0][3]                               |
| [Apache Log4j Core][39]                    | [Apache-2.0][3]                               |
| [scala-logging][40]                        | [Apache 2.0 License][31]                      |

### Test Dependencies

| Dependency                                 | License                                                                                                                                        |
| ------------------------------------------ | ---------------------------------------------------------------------------------------------------------------------------------------------- |
| [scalatest][41]                            | [the Apache License, ASL Version 2.0][29]                                                                                                      |
| [scalatestplus-mockito][42]                | [Apache-2.0][29]                                                                                                                               |
| [mockito-core][43]                         | [MIT][44]                                                                                                                                      |
| [Hamcrest][45]                             | [BSD License 3][46]                                                                                                                            |
| [testcontainers-scala-scalatest][47]       | [The MIT License (MIT)][44]                                                                                                                    |
| [Testcontainers :: Localstack][48]         | [MIT][49]                                                                                                                                      |
| [Test containers for Exasol on Docker][50] | [MIT License][51]                                                                                                                              |
| [Test Database Builder for Java][52]       | [MIT License][53]                                                                                                                              |
| [Matcher for SQL Result Sets][54]          | [MIT License][55]                                                                                                                              |
| [EqualsVerifier \| release normal jar][56] | [Apache License, Version 2.0][3]                                                                                                               |
| [JUnit Jupiter Engine][57]                 | [Eclipse Public License v2.0][58]                                                                                                              |
| [Maven Project Version Getter][59]         | [MIT License][60]                                                                                                                              |
| [Extension integration tests library][61]  | [MIT License][62]                                                                                                                              |
| [jersey-core-common][63]                   | [EPL 2.0][64]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][65]; [Apache License, 2.0][31]; [Public Domain][66] |

### Runtime Dependencies

| Dependency                   | License                                                                       |
| ---------------------------- | ----------------------------------------------------------------------------- |
| [Logback Classic Module][67] | [Eclipse Public License - v 1.0][68]; [GNU Lesser General Public License][69] |
| [Logback Core Module][70]    | [Eclipse Public License - v 1.0][68]; [GNU Lesser General Public License][69] |

### Plugin Dependencies

| Dependency                                              | License                                       |
| ------------------------------------------------------- | --------------------------------------------- |
| [SonarQube Scanner for Maven][71]                       | [GNU LGPL 3][72]                              |
| [Apache Maven Toolchains Plugin][73]                    | [Apache License, Version 2.0][3]              |
| [Apache Maven Compiler Plugin][74]                      | [Apache-2.0][3]                               |
| [Apache Maven Enforcer Plugin][75]                      | [Apache-2.0][3]                               |
| [Maven Flatten Plugin][76]                              | [Apache Software Licenese][3]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][77] | [ASL2][5]                                     |
| [scala-maven-plugin][78]                                | [Public domain (Unlicense)][79]               |
| [ScalaTest Maven Plugin][80]                            | [the Apache License, ASL Version 2.0][29]     |
| [Apache Maven Javadoc Plugin][81]                       | [Apache-2.0][3]                               |
| [Maven Surefire Plugin][82]                             | [Apache-2.0][3]                               |
| [Versions Maven Plugin][83]                             | [Apache License, Version 2.0][3]              |
| [duplicate-finder-maven-plugin Maven Mojo][84]          | [Apache License 2.0][31]                      |
| [Apache Maven Assembly Plugin][85]                      | [Apache-2.0][3]                               |
| [Apache Maven JAR Plugin][86]                           | [Apache License, Version 2.0][3]              |
| [Artifact reference checker and unifier][87]            | [MIT License][88]                             |
| [Maven Failsafe Plugin][89]                             | [Apache-2.0][3]                               |
| [JaCoCo :: Maven Plugin][90]                            | [Eclipse Public License 2.0][91]              |
| [error-code-crawler-maven-plugin][92]                   | [MIT License][93]                             |
| [Reproducible Build Maven Plugin][94]                   | [Apache 2.0][5]                               |
| [Project Keeper Maven plugin][95]                       | [The MIT License][96]                         |
| [OpenFastTrace Maven Plugin][97]                        | [GNU General Public License v3.0][98]         |
| [Scalastyle Maven Plugin][99]                           | [Apache 2.0][31]                              |
| [spotless-maven-plugin][100]                            | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][101]                            | [BSD-3-Clause][21]                            |
| [Exec Maven Plugin][102]                                | [Apache License 2][3]                         |
| [Apache Maven Clean Plugin][103]                        | [Apache-2.0][3]                               |

## Extension

### Compile Dependencies

| Dependency                                 | License |
| ------------------------------------------ | ------- |
| [@exasol/extension-manager-interface][104] | MIT     |

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
[22]: https://github.com/GoogleCloudDataproc/hadoop-connectors/gcs-connector
[23]: https://github.com/googleapis/google-oauth-java-client/google-oauth-client
[24]: https://orc.apache.org/orc-core
[25]: https://avro.apache.org
[26]: https://commons.apache.org/proper/commons-compress/
[27]: https://bitbucket.org/connect2id/nimbus-jose-jwt
[28]: https://delta.io/
[29]: http://www.apache.org/licenses/LICENSE-2.0
[30]: https://spark.apache.org/
[31]: http://www.apache.org/licenses/LICENSE-2.0.html
[32]: http://ant.apache.org/ivy/
[33]: https://github.com/exasol/parquet-io-java/
[34]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[35]: http://www.slf4j.org
[36]: http://www.opensource.org/licenses/mit-license.php
[37]: https://logging.apache.org/log4j/2.x/log4j/log4j-api/
[38]: https://logging.apache.org/log4j/2.x/log4j/log4j-1.2-api/
[39]: https://logging.apache.org/log4j/2.x/log4j/log4j-core/
[40]: https://github.com/lightbend/scala-logging
[41]: http://www.scalatest.org
[42]: https://github.com/scalatest/scalatestplus-mockito
[43]: https://github.com/mockito/mockito
[44]: https://opensource.org/licenses/MIT
[45]: http://hamcrest.org/JavaHamcrest/
[46]: http://opensource.org/licenses/BSD-3-Clause
[47]: https://github.com/testcontainers/testcontainers-scala
[48]: https://java.testcontainers.org
[49]: http://opensource.org/licenses/MIT
[50]: https://github.com/exasol/exasol-testcontainers/
[51]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[52]: https://github.com/exasol/test-db-builder-java/
[53]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[54]: https://github.com/exasol/hamcrest-resultset-matcher/
[55]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[56]: https://www.jqno.nl/equalsverifier
[57]: https://junit.org/junit5/
[58]: https://www.eclipse.org/legal/epl-v20.html
[59]: https://github.com/exasol/maven-project-version-getter/
[60]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[61]: https://github.com/exasol/extension-manager/
[62]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[63]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-common
[64]: http://www.eclipse.org/legal/epl-2.0
[65]: https://www.gnu.org/software/classpath/license.html
[66]: https://creativecommons.org/publicdomain/zero/1.0/
[67]: http://logback.qos.ch/logback-classic
[68]: http://www.eclipse.org/legal/epl-v10.html
[69]: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
[70]: http://logback.qos.ch/logback-core
[71]: http://sonarsource.github.io/sonar-scanner-maven/
[72]: http://www.gnu.org/licenses/lgpl.txt
[73]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[74]: https://maven.apache.org/plugins/maven-compiler-plugin/
[75]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[76]: https://www.mojohaus.org/flatten-maven-plugin/
[77]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[78]: http://github.com/davidB/scala-maven-plugin
[79]: http://unlicense.org/
[80]: https://www.scalatest.org/user_guide/using_the_scalatest_maven_plugin
[81]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[82]: https://maven.apache.org/surefire/maven-surefire-plugin/
[83]: https://www.mojohaus.org/versions/versions-maven-plugin/
[84]: https://basepom.github.io/duplicate-finder-maven-plugin
[85]: https://maven.apache.org/plugins/maven-assembly-plugin/
[86]: https://maven.apache.org/plugins/maven-jar-plugin/
[87]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[88]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[89]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[90]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[91]: https://www.eclipse.org/legal/epl-2.0/
[92]: https://github.com/exasol/error-code-crawler-maven-plugin/
[93]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[94]: http://zlika.github.io/reproducible-build-maven-plugin
[95]: https://github.com/exasol/project-keeper/
[96]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[97]: https://github.com/itsallcode/openfasttrace-maven-plugin
[98]: https://www.gnu.org/licenses/gpl-3.0.html
[99]: http://www.scalastyle.org
[100]: https://github.com/diffplug/spotless
[101]: https://github.com/evis/scalafix-maven-plugin
[102]: https://www.mojohaus.org/exec-maven-plugin
[103]: https://maven.apache.org/plugins/maven-clean-plugin/
[104]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.4.1.tgz
