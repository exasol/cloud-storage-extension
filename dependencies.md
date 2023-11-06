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
| [SLF4J Reload4j Provider][36]              | [MIT License][35]                             |
| [Apache Log4j API][37]                     | [Apache-2.0][3]                               |
| [Apache Log4j 1.x Compatibility API][38]   | [Apache-2.0][3]                               |
| [Apache Log4j Core][39]                    | [Apache-2.0][3]                               |
| [scala-logging][40]                        | [Apache 2.0 License][30]                      |

### Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [scalatest][41]                            | [the Apache License, ASL Version 2.0][28] |
| [scalatestplus-mockito][42]                | [Apache-2.0][28]                          |
| [mockito-core][43]                         | [MIT][44]                                 |
| [Hamcrest][45]                             | [BSD License 3][46]                       |
| [testcontainers-scala-scalatest][47]       | [The MIT License (MIT)][44]               |
| [Testcontainers :: Localstack][48]         | [MIT][49]                                 |
| [Test containers for Exasol on Docker][50] | [MIT License][51]                         |
| [Test Database Builder for Java][52]       | [MIT License][53]                         |
| [Matcher for SQL Result Sets][54]          | [MIT License][55]                         |
| [EqualsVerifier \| release normal jar][56] | [Apache License, Version 2.0][3]          |
| [JUnit Jupiter Engine][57]                 | [Eclipse Public License v2.0][58]         |
| [Maven Project Version Getter][59]         | [MIT License][60]                         |
| [Extension integration tests library][61]  | [MIT License][62]                         |

### Plugin Dependencies

| Dependency                                              | License                                       |
| ------------------------------------------------------- | --------------------------------------------- |
| [SonarQube Scanner for Maven][63]                       | [GNU LGPL 3][64]                              |
| [Apache Maven Compiler Plugin][65]                      | [Apache-2.0][3]                               |
| [Apache Maven Enforcer Plugin][66]                      | [Apache-2.0][3]                               |
| [Maven Flatten Plugin][67]                              | [Apache Software Licenese][3]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][68] | [ASL2][5]                                     |
| [scala-maven-plugin][69]                                | [Public domain (Unlicense)][70]               |
| [ScalaTest Maven Plugin][71]                            | [the Apache License, ASL Version 2.0][28]     |
| [Apache Maven Javadoc Plugin][72]                       | [Apache-2.0][3]                               |
| [Maven Surefire Plugin][73]                             | [Apache-2.0][3]                               |
| [Versions Maven Plugin][74]                             | [Apache License, Version 2.0][3]              |
| [duplicate-finder-maven-plugin Maven Mojo][75]          | [Apache License 2.0][30]                      |
| [Apache Maven Assembly Plugin][76]                      | [Apache-2.0][3]                               |
| [Apache Maven JAR Plugin][77]                           | [Apache License, Version 2.0][3]              |
| [Artifact reference checker and unifier][78]            | [MIT License][79]                             |
| [Maven Failsafe Plugin][80]                             | [Apache-2.0][3]                               |
| [JaCoCo :: Maven Plugin][81]                            | [Eclipse Public License 2.0][82]              |
| [error-code-crawler-maven-plugin][83]                   | [MIT License][84]                             |
| [Reproducible Build Maven Plugin][85]                   | [Apache 2.0][5]                               |
| [Project keeper maven plugin][86]                       | [The MIT License][87]                         |
| [OpenFastTrace Maven Plugin][88]                        | [GNU General Public License v3.0][89]         |
| [Scalastyle Maven Plugin][90]                           | [Apache 2.0][30]                              |
| [spotless-maven-plugin][91]                             | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][92]                             | [BSD-3-Clause][21]                            |
| [Exec Maven Plugin][93]                                 | [Apache License 2][3]                         |
| [Apache Maven Clean Plugin][94]                         | [Apache-2.0][3]                               |

## Extension

### Compile Dependencies

| Dependency                                | License |
| ----------------------------------------- | ------- |
| [@exasol/extension-manager-interface][95] | MIT     |

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
[36]: http://reload4j.qos.ch
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
[63]: http://sonarsource.github.io/sonar-scanner-maven/
[64]: http://www.gnu.org/licenses/lgpl.txt
[65]: https://maven.apache.org/plugins/maven-compiler-plugin/
[66]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[67]: https://www.mojohaus.org/flatten-maven-plugin/
[68]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[69]: http://github.com/davidB/scala-maven-plugin
[70]: http://unlicense.org/
[71]: https://www.scalatest.org/user_guide/using_the_scalatest_maven_plugin
[72]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[73]: https://maven.apache.org/surefire/maven-surefire-plugin/
[74]: https://www.mojohaus.org/versions/versions-maven-plugin/
[75]: https://basepom.github.io/duplicate-finder-maven-plugin
[76]: https://maven.apache.org/plugins/maven-assembly-plugin/
[77]: https://maven.apache.org/plugins/maven-jar-plugin/
[78]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[79]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[80]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[81]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[82]: https://www.eclipse.org/legal/epl-2.0/
[83]: https://github.com/exasol/error-code-crawler-maven-plugin/
[84]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[85]: http://zlika.github.io/reproducible-build-maven-plugin
[86]: https://github.com/exasol/project-keeper/
[87]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[88]: https://github.com/itsallcode/openfasttrace-maven-plugin
[89]: https://www.gnu.org/licenses/gpl-3.0.html
[90]: http://www.scalastyle.org
[91]: https://github.com/diffplug/spotless
[92]: https://github.com/evis/scalafix-maven-plugin
[93]: https://www.mojohaus.org/exec-maven-plugin
[94]: https://maven.apache.org/plugins/maven-clean-plugin/
[95]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.4.0.tgz
