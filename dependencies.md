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
| [RabbitMQ Java Client][19]                 | [AL 2.0][10]; [GPL v2][20]; [MPL 2.0][21]     |
| [Metrics Core][22]                         | [Apache License 2.0][10]                      |
| [Protocol Buffers [Core]][23]              | [BSD-3-Clause][24]                            |
| [gcs-connector-hadoop3][25]                | [Apache License, Version 2.0][5]              |
| [Google OAuth Client Library for Java][26] | [The Apache Software License, Version 2.0][3] |
| [ORC Core][27]                             | [Apache License, Version 2.0][3]              |
| [Apache Avro][28]                          | [Apache-2.0][3]                               |
| [Apache Commons Compress][29]              | [Apache-2.0][3]                               |
| [delta-core][30]                           | [Apache-2.0][31]                              |
| [Spark Project SQL][32]                    | [Apache 2.0 License][33]                      |
| [Apache Ivy][34]                           | [The Apache Software License, Version 2.0][5] |
| [Parquet for Java][35]                     | [MIT License][36]                             |
| [JUL to SLF4J bridge][37]                  | [MIT License][38]                             |
| [SLF4J Reload4j Provider][39]              | [MIT License][38]                             |
| [Apache Log4j API][40]                     | [Apache-2.0][3]                               |
| [Apache Log4j 1.x Compatibility API][41]   | [Apache-2.0][3]                               |
| [Apache Log4j Core][42]                    | [Apache-2.0][3]                               |
| [scala-logging][43]                        | [Apache 2.0 License][33]                      |

### Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [scalatest][44]                            | [the Apache License, ASL Version 2.0][31] |
| [scalatestplus-mockito][45]                | [Apache-2.0][31]                          |
| [mockito-core][46]                         | [MIT][47]                                 |
| [Hamcrest][48]                             | [BSD License 3][49]                       |
| [testcontainers-scala-scalatest][50]       | [The MIT License (MIT)][51]               |
| [Testcontainers :: Localstack][52]         | [MIT][53]                                 |
| [Test containers for Exasol on Docker][54] | [MIT License][55]                         |
| [Test Database Builder for Java][56]       | [MIT License][57]                         |
| [Matcher for SQL Result Sets][58]          | [MIT License][59]                         |
| [EqualsVerifier \| release normal jar][60] | [Apache License, Version 2.0][3]          |
| [JUnit Jupiter Engine][61]                 | [Eclipse Public License v2.0][62]         |
| [Maven Project Version Getter][63]         | [MIT License][64]                         |
| [Extension integration tests library][65]  | [MIT License][66]                         |

### Plugin Dependencies

| Dependency                                              | License                                       |
| ------------------------------------------------------- | --------------------------------------------- |
| [SonarQube Scanner for Maven][67]                       | [GNU LGPL 3][68]                              |
| [Apache Maven Compiler Plugin][69]                      | [Apache-2.0][3]                               |
| [Apache Maven Enforcer Plugin][70]                      | [Apache-2.0][3]                               |
| [Maven Flatten Plugin][71]                              | [Apache Software Licenese][3]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][72] | [ASL2][5]                                     |
| [scala-maven-plugin][73]                                | [Public domain (Unlicense)][74]               |
| [ScalaTest Maven Plugin][75]                            | [the Apache License, ASL Version 2.0][31]     |
| [Apache Maven Javadoc Plugin][76]                       | [Apache-2.0][3]                               |
| [Maven Surefire Plugin][77]                             | [Apache-2.0][3]                               |
| [Versions Maven Plugin][78]                             | [Apache License, Version 2.0][3]              |
| [duplicate-finder-maven-plugin Maven Mojo][79]          | [Apache License 2.0][33]                      |
| [Apache Maven Assembly Plugin][80]                      | [Apache-2.0][3]                               |
| [Apache Maven JAR Plugin][81]                           | [Apache License, Version 2.0][3]              |
| [Artifact reference checker and unifier][82]            | [MIT License][83]                             |
| [Maven Failsafe Plugin][84]                             | [Apache-2.0][3]                               |
| [JaCoCo :: Maven Plugin][85]                            | [Eclipse Public License 2.0][86]              |
| [error-code-crawler-maven-plugin][87]                   | [MIT License][88]                             |
| [Reproducible Build Maven Plugin][89]                   | [Apache 2.0][5]                               |
| [Project keeper maven plugin][90]                       | [The MIT License][91]                         |
| [OpenFastTrace Maven Plugin][92]                        | [GNU General Public License v3.0][93]         |
| [Scalastyle Maven Plugin][94]                           | [Apache 2.0][33]                              |
| [spotless-maven-plugin][95]                             | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][96]                             | [BSD-3-Clause][24]                            |
| [Exec Maven Plugin][97]                                 | [Apache License 2][3]                         |
| [Apache Maven Clean Plugin][98]                         | [Apache-2.0][3]                               |

## Extension

### Compile Dependencies

| Dependency                                | License |
| ----------------------------------------- | ------- |
| [@exasol/extension-manager-interface][99] | MIT     |

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
[19]: https://www.rabbitmq.com
[20]: https://www.gnu.org/licenses/gpl-2.0.txt
[21]: https://www.mozilla.org/en-US/MPL/2.0/
[22]: https://metrics.dropwizard.io/metrics-core
[23]: https://developers.google.com/protocol-buffers/protobuf-java/
[24]: https://opensource.org/licenses/BSD-3-Clause
[25]: https://github.com/GoogleCloudPlatform/BigData-interop/gcs-connector/
[26]: https://github.com/googleapis/google-oauth-java-client/google-oauth-client
[27]: https://orc.apache.org/orc-core
[28]: https://avro.apache.org
[29]: https://commons.apache.org/proper/commons-compress/
[30]: https://delta.io/
[31]: http://www.apache.org/licenses/LICENSE-2.0
[32]: https://spark.apache.org/
[33]: http://www.apache.org/licenses/LICENSE-2.0.html
[34]: http://ant.apache.org/ivy/
[35]: https://github.com/exasol/parquet-io-java/
[36]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[37]: http://www.slf4j.org
[38]: http://www.opensource.org/licenses/mit-license.php
[39]: http://reload4j.qos.ch
[40]: https://logging.apache.org/log4j/2.x/log4j/log4j-api/
[41]: https://logging.apache.org/log4j/2.x/log4j/log4j-1.2-api/
[42]: https://logging.apache.org/log4j/2.x/log4j/log4j-core/
[43]: https://github.com/lightbend/scala-logging
[44]: http://www.scalatest.org
[45]: https://github.com/scalatest/scalatestplus-mockito
[46]: https://github.com/mockito/mockito
[47]: https://github.com/mockito/mockito/blob/main/LICENSE
[48]: http://hamcrest.org/JavaHamcrest/
[49]: http://opensource.org/licenses/BSD-3-Clause
[50]: https://github.com/testcontainers/testcontainers-scala
[51]: https://opensource.org/licenses/MIT
[52]: https://java.testcontainers.org
[53]: http://opensource.org/licenses/MIT
[54]: https://github.com/exasol/exasol-testcontainers/
[55]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[56]: https://github.com/exasol/test-db-builder-java/
[57]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[58]: https://github.com/exasol/hamcrest-resultset-matcher/
[59]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[60]: https://www.jqno.nl/equalsverifier
[61]: https://junit.org/junit5/
[62]: https://www.eclipse.org/legal/epl-v20.html
[63]: https://github.com/exasol/maven-project-version-getter/
[64]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[65]: https://github.com/exasol/extension-manager/
[66]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[67]: http://sonarsource.github.io/sonar-scanner-maven/
[68]: http://www.gnu.org/licenses/lgpl.txt
[69]: https://maven.apache.org/plugins/maven-compiler-plugin/
[70]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[71]: https://www.mojohaus.org/flatten-maven-plugin/
[72]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[73]: http://github.com/davidB/scala-maven-plugin
[74]: http://unlicense.org/
[75]: https://www.scalatest.org/user_guide/using_the_scalatest_maven_plugin
[76]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[77]: https://maven.apache.org/surefire/maven-surefire-plugin/
[78]: https://www.mojohaus.org/versions/versions-maven-plugin/
[79]: https://basepom.github.io/duplicate-finder-maven-plugin
[80]: https://maven.apache.org/plugins/maven-assembly-plugin/
[81]: https://maven.apache.org/plugins/maven-jar-plugin/
[82]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[83]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[84]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[85]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[86]: https://www.eclipse.org/legal/epl-2.0/
[87]: https://github.com/exasol/error-code-crawler-maven-plugin/
[88]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[89]: http://zlika.github.io/reproducible-build-maven-plugin
[90]: https://github.com/exasol/project-keeper/
[91]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[92]: https://github.com/itsallcode/openfasttrace-maven-plugin
[93]: https://www.gnu.org/licenses/gpl-3.0.html
[94]: http://www.scalastyle.org
[95]: https://github.com/diffplug/spotless
[96]: https://github.com/evis/scalafix-maven-plugin
[97]: https://www.mojohaus.org/exec-maven-plugin
[98]: https://maven.apache.org/plugins/maven-clean-plugin/
[99]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.3.1.tgz
