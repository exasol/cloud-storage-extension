<!-- @formatter:off -->
# Dependencies

## Compile Dependencies

| Dependency                                 | License                                       |
| ------------------------------------------ | --------------------------------------------- |
| [Scala Library][0]                         | [Apache-2.0][1]                               |
| [Apache Commons Lang][2]                   | [Apache License, Version 2.0][3]              |
| [Guava: Google Core Libraries for Java][4] | [Apache License, Version 2.0][5]              |
| [jackson-databind][6]                      | [The Apache Software License, Version 2.0][5] |
| [jackson-module-scala][7]                  | [The Apache Software License, Version 2.0][3] |
| [io.grpc:grpc-netty][8]                    | [Apache 2.0][9]                               |
| [Netty/All-in-One][10]                     | [Apache License, Version 2.0][1]              |
| [import-export-udf-common-scala][11]       | [MIT][12]                                     |
| [error-reporting-java][13]                 | [MIT License][14]                             |
| Apache Hadoop Common                       | [Apache License, Version 2.0][3]              |
| Apache Hadoop Amazon Web Services support  | [Apache License, Version 2.0][3]              |
| Apache Hadoop Azure support                | [Apache License, Version 2.0][3]              |
| Apache Hadoop Azure Data Lake support      | [Apache License, Version 2.0][3]              |
| Apache Hadoop HDFS                         | [Apache License, Version 2.0][3]              |
| Apache Hadoop HDFS Client                  | [Apache License, Version 2.0][3]              |
| [Alluxio Core - Client - HDFS][15]         | [Apache License][16]                          |
| [Protocol Buffers [Core]][17]              | [BSD-3-Clause][18]                            |
| [gcs-connector-hadoop3][19]                | [Apache License, Version 2.0][5]              |
| [Google OAuth Client Library for Java][20] | [The Apache Software License, Version 2.0][3] |
| [ORC Core][21]                             | [Apache License, Version 2.0][3]              |
| [Apache Avro][22]                          | [Apache License, Version 2.0][3]              |
| [delta-core][23]                           | [Apache-2.0][24]                              |
| [Spark Project SQL][25]                    | [Apache 2.0 License][26]                      |
| [Parquet for Java][27]                     | [MIT License][28]                             |
| [JUL to SLF4J bridge][29]                  | [MIT License][30]                             |
| [SLF4J LOG4J-12 Binding relocated][29]     | [MIT License][30]                             |
| [Apache Log4j API][31]                     | [Apache License, Version 2.0][3]              |
| [Apache Log4j 1.x Compatibility API][32]   | [Apache License, Version 2.0][3]              |
| [Apache Log4j Core][33]                    | [Apache License, Version 2.0][3]              |
| [scala-logging][34]                        | [Apache 2.0 License][26]                      |

## Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [scalatest][35]                            | [the Apache License, ASL Version 2.0][24] |
| [scalatestplus-mockito][36]                | [Apache-2.0][24]                          |
| [mockito-core][37]                         | [The MIT License][38]                     |
| [Hamcrest][39]                             | [BSD License 3][40]                       |
| [testcontainers-scala-scalatest][41]       | [The MIT License (MIT)][12]               |
| [Testcontainers :: Localstack][42]         | [MIT][43]                                 |
| [Test containers for Exasol on Docker][44] | [MIT License][45]                         |
| [Test Database Builder for Java][46]       | [MIT License][47]                         |
| [Matcher for SQL Result Sets][48]          | [MIT License][49]                         |
| [EqualsVerifier | release normal jar][50]  | [Apache License, Version 2.0][3]          |

## Plugin Dependencies

| Dependency                                              | License                                       |
| ------------------------------------------------------- | --------------------------------------------- |
| [SonarQube Scanner for Maven][51]                       | [GNU LGPL 3][52]                              |
| [Apache Maven Compiler Plugin][53]                      | [Apache License, Version 2.0][3]              |
| [Apache Maven Enforcer Plugin][54]                      | [Apache License, Version 2.0][3]              |
| [Maven Flatten Plugin][55]                              | [Apache Software Licenese][5]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][56] | [ASL2][5]                                     |
| [scala-maven-plugin][57]                                | [Public domain (Unlicense)][58]               |
| [ScalaTest Maven Plugin][59]                            | [the Apache License, ASL Version 2.0][24]     |
| [Apache Maven Javadoc Plugin][60]                       | [Apache License, Version 2.0][3]              |
| [Maven Surefire Plugin][61]                             | [Apache License, Version 2.0][3]              |
| [Versions Maven Plugin][62]                             | [Apache License, Version 2.0][3]              |
| [Apache Maven Assembly Plugin][63]                      | [Apache License, Version 2.0][3]              |
| [Apache Maven JAR Plugin][64]                           | [Apache License, Version 2.0][3]              |
| [Artifact reference checker and unifier][65]            | [MIT][12]                                     |
| [Maven Failsafe Plugin][66]                             | [Apache License, Version 2.0][3]              |
| [JaCoCo :: Maven Plugin][67]                            | [Eclipse Public License 2.0][68]              |
| [error-code-crawler-maven-plugin][69]                   | [MIT License][70]                             |
| [Reproducible Build Maven Plugin][71]                   | [Apache 2.0][5]                               |
| [Project keeper maven plugin][72]                       | [The MIT License][73]                         |
| [OpenFastTrace Maven Plugin][74]                        | [GNU General Public License v3.0][75]         |
| [Scalastyle Maven Plugin][76]                           | [Apache 2.0][26]                              |
| [spotless-maven-plugin][77]                             | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][78]                             | [BSD-3-Clause][18]                            |
| [Maven Clean Plugin][79]                                | [The Apache Software License, Version 2.0][5] |
| [Maven Resources Plugin][80]                            | [The Apache Software License, Version 2.0][5] |
| [Maven Install Plugin][81]                              | [The Apache Software License, Version 2.0][5] |
| [Maven Deploy Plugin][82]                               | [The Apache Software License, Version 2.0][5] |
| [Maven Site Plugin 3][83]                               | [The Apache Software License, Version 2.0][5] |

[0]: https://www.scala-lang.org/
[1]: https://www.apache.org/licenses/LICENSE-2.0
[2]: https://commons.apache.org/proper/commons-lang/
[3]: https://www.apache.org/licenses/LICENSE-2.0.txt
[4]: https://github.com/google/guava
[5]: http://www.apache.org/licenses/LICENSE-2.0.txt
[6]: http://github.com/FasterXML/jackson
[7]: https://github.com/FasterXML/jackson-module-scala
[8]: https://github.com/grpc/grpc-java
[9]: https://opensource.org/licenses/Apache-2.0
[10]: https://netty.io/
[11]: https://github.com/exasol/import-export-udf-common-scala
[12]: https://opensource.org/licenses/MIT
[13]: https://github.com/exasol/error-reporting-java/
[14]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[15]: https://www.alluxio.io
[16]: https://github.com/alluxio/alluxio/blob/master/LICENSE
[17]: https://github.com/protocolbuffers/protobuf/tree/main/java
[18]: https://opensource.org/licenses/BSD-3-Clause
[19]: https://github.com/GoogleCloudDataproc/hadoop-connectors/tree/master/gcs
[20]: https://github.com/googleapis/google-oauth-java-client
[21]: https://orc.apache.org/
[22]: https://avro.apache.org
[23]: https://delta.io/
[24]: http://www.apache.org/licenses/LICENSE-2.0
[25]: https://spark.apache.org/
[26]: http://www.apache.org/licenses/LICENSE-2.0.html
[27]: https://github.com/exasol/parquet-io-java/
[28]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[29]: http://www.slf4j.org
[30]: http://www.opensource.org/licenses/mit-license.php
[31]: https://logging.apache.org/log4j/2.x/log4j-api/
[32]: https://logging.apache.org/log4j/2.x/log4j-1.2-api/
[33]: https://logging.apache.org/log4j/2.x/log4j-core/
[34]: https://github.com/lightbend/scala-logging
[35]: http://www.scalatest.org
[36]: https://github.com/scalatest/scalatestplus-mockito
[37]: https://github.com/mockito/mockito
[38]: https://github.com/mockito/mockito/blob/main/LICENSE
[39]: http://hamcrest.org/JavaHamcrest/
[40]: http://opensource.org/licenses/BSD-3-Clause
[41]: https://github.com/testcontainers/testcontainers-scala
[42]: https://testcontainers.org
[43]: http://opensource.org/licenses/MIT
[44]: https://github.com/exasol/exasol-testcontainers/
[45]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[46]: https://github.com/exasol/test-db-builder-java/
[47]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[48]: https://github.com/exasol/hamcrest-resultset-matcher/
[49]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[50]: https://www.jqno.nl/equalsverifier
[51]: http://sonarsource.github.io/sonar-scanner-maven/
[52]: http://www.gnu.org/licenses/lgpl.txt
[53]: https://maven.apache.org/plugins/maven-compiler-plugin/
[54]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[55]: https://www.mojohaus.org/flatten-maven-plugin/
[56]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[57]: http://github.com/davidB/scala-maven-plugin
[58]: http://unlicense.org/
[59]: https://github.com/scalatest/scalatest-maven-plugin
[60]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[61]: https://maven.apache.org/surefire/maven-surefire-plugin/
[62]: http://www.mojohaus.org/versions-maven-plugin/
[63]: https://maven.apache.org/plugins/maven-assembly-plugin/
[64]: https://maven.apache.org/plugins/maven-jar-plugin/
[65]: https://github.com/exasol/artifact-reference-checker-maven-plugin
[66]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[67]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[68]: https://www.eclipse.org/legal/epl-2.0/
[69]: https://github.com/exasol/error-code-crawler-maven-plugin/
[70]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[71]: http://zlika.github.io/reproducible-build-maven-plugin
[72]: https://github.com/exasol/project-keeper/
[73]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[74]: https://github.com/itsallcode/openfasttrace-maven-plugin
[75]: https://www.gnu.org/licenses/gpl-3.0.html
[76]: http://www.scalastyle.org
[77]: https://github.com/diffplug/spotless
[78]: https://github.com/evis/scalafix-maven-plugin
[79]: http://maven.apache.org/plugins/maven-clean-plugin/
[80]: http://maven.apache.org/plugins/maven-resources-plugin/
[81]: http://maven.apache.org/plugins/maven-install-plugin/
[82]: http://maven.apache.org/plugins/maven-deploy-plugin/
[83]: http://maven.apache.org/plugins/maven-site-plugin/
