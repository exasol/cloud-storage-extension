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
| [testcontainers-scala-scalatest][47]       | [The MIT License (MIT)][48]               |
| [Testcontainers :: Localstack][49]         | [MIT][50]                                 |
| [Test containers for Exasol on Docker][51] | [MIT License][52]                         |
| [Test Database Builder for Java][53]       | [MIT License][54]                         |
| [Matcher for SQL Result Sets][55]          | [MIT License][56]                         |
| [EqualsVerifier \| release normal jar][57] | [Apache License, Version 2.0][3]          |
| [JUnit Jupiter Engine][58]                 | [Eclipse Public License v2.0][59]         |
| [Maven Project Version Getter][60]         | [MIT License][61]                         |
| [Extension integration tests library][62]  | [MIT License][63]                         |

### Plugin Dependencies

| Dependency                                              | License                                       |
| ------------------------------------------------------- | --------------------------------------------- |
| [SonarQube Scanner for Maven][64]                       | [GNU LGPL 3][65]                              |
| [Apache Maven Compiler Plugin][66]                      | [Apache-2.0][3]                               |
| [Apache Maven Enforcer Plugin][67]                      | [Apache-2.0][3]                               |
| [Maven Flatten Plugin][68]                              | [Apache Software Licenese][3]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][69] | [ASL2][5]                                     |
| [scala-maven-plugin][70]                                | [Public domain (Unlicense)][71]               |
| [ScalaTest Maven Plugin][72]                            | [the Apache License, ASL Version 2.0][28]     |
| [Apache Maven Javadoc Plugin][73]                       | [Apache-2.0][3]                               |
| [Maven Surefire Plugin][74]                             | [Apache-2.0][3]                               |
| [Versions Maven Plugin][75]                             | [Apache License, Version 2.0][3]              |
| [duplicate-finder-maven-plugin Maven Mojo][76]          | [Apache License 2.0][30]                      |
| [Apache Maven Assembly Plugin][77]                      | [Apache-2.0][3]                               |
| [Apache Maven JAR Plugin][78]                           | [Apache License, Version 2.0][3]              |
| [Artifact reference checker and unifier][79]            | [MIT License][80]                             |
| [Maven Failsafe Plugin][81]                             | [Apache-2.0][3]                               |
| [JaCoCo :: Maven Plugin][82]                            | [Eclipse Public License 2.0][83]              |
| [error-code-crawler-maven-plugin][84]                   | [MIT License][85]                             |
| [Reproducible Build Maven Plugin][86]                   | [Apache 2.0][5]                               |
| [Project keeper maven plugin][87]                       | [The MIT License][88]                         |
| [OpenFastTrace Maven Plugin][89]                        | [GNU General Public License v3.0][90]         |
| [Scalastyle Maven Plugin][91]                           | [Apache 2.0][30]                              |
| [spotless-maven-plugin][92]                             | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][93]                             | [BSD-3-Clause][21]                            |
| [Exec Maven Plugin][94]                                 | [Apache License 2][3]                         |
| [Apache Maven Clean Plugin][95]                         | [Apache-2.0][3]                               |

## Extension

### Compile Dependencies

| Dependency                                | License |
| ----------------------------------------- | ------- |
| [@exasol/extension-manager-interface][96] | MIT     |

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
[20]: https://github.com/protocolbuffers/protobuf/tree/main/java
[21]: https://opensource.org/licenses/BSD-3-Clause
[22]: https://github.com/GoogleCloudDataproc/hadoop-connectors/tree/master/gcs
[23]: https://github.com/googleapis/google-oauth-java-client
[24]: https://orc.apache.org/
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
[44]: https://github.com/mockito/mockito/blob/main/LICENSE
[45]: http://hamcrest.org/JavaHamcrest/
[46]: http://opensource.org/licenses/BSD-3-Clause
[47]: https://github.com/testcontainers/testcontainers-scala
[48]: https://opensource.org/licenses/MIT
[49]: https://java.testcontainers.org
[50]: http://opensource.org/licenses/MIT
[51]: https://github.com/exasol/exasol-testcontainers/
[52]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[53]: https://github.com/exasol/test-db-builder-java/
[54]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[55]: https://github.com/exasol/hamcrest-resultset-matcher/
[56]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[57]: https://www.jqno.nl/equalsverifier
[58]: https://junit.org/junit5/
[59]: https://www.eclipse.org/legal/epl-v20.html
[60]: https://github.com/exasol/maven-project-version-getter/
[61]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[62]: https://github.com/exasol/extension-manager/
[63]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[64]: http://sonarsource.github.io/sonar-scanner-maven/
[65]: http://www.gnu.org/licenses/lgpl.txt
[66]: https://maven.apache.org/plugins/maven-compiler-plugin/
[67]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[68]: https://www.mojohaus.org/flatten-maven-plugin/
[69]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[70]: http://github.com/davidB/scala-maven-plugin
[71]: http://unlicense.org/
[72]: https://www.scalatest.org/user_guide/using_the_scalatest_maven_plugin
[73]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[74]: https://maven.apache.org/surefire/maven-surefire-plugin/
[75]: https://www.mojohaus.org/versions/versions-maven-plugin/
[76]: https://basepom.github.io/duplicate-finder-maven-plugin
[77]: https://maven.apache.org/plugins/maven-assembly-plugin/
[78]: https://maven.apache.org/plugins/maven-jar-plugin/
[79]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[80]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[81]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[82]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[83]: https://www.eclipse.org/legal/epl-2.0/
[84]: https://github.com/exasol/error-code-crawler-maven-plugin/
[85]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[86]: http://zlika.github.io/reproducible-build-maven-plugin
[87]: https://github.com/exasol/project-keeper/
[88]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[89]: https://github.com/itsallcode/openfasttrace-maven-plugin
[90]: https://www.gnu.org/licenses/gpl-3.0.html
[91]: http://www.scalastyle.org
[92]: https://github.com/diffplug/spotless
[93]: https://github.com/evis/scalafix-maven-plugin
[94]: https://www.mojohaus.org/exec-maven-plugin
[95]: https://maven.apache.org/plugins/maven-clean-plugin/
[96]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.3.1.tgz
