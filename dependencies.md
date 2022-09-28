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
| [Alluxio Core - Client - HDFS][15]         | [Apache License][16]                          |
| [Protocol Buffers [Core]][17]              | [BSD-3-Clause][18]                            |
| [gcs-connector-hadoop3][19]                | [Apache License, Version 2.0][5]              |
| [Google OAuth Client Library for Java][20] | [The Apache Software License, Version 2.0][3] |
| [ORC Core][21]                             | [Apache License, Version 2.0][3]              |
| [Apache Avro][22]                          | [Apache License, Version 2.0][3]              |
| [delta-core][23]                           | [Apache-2.0][24]                              |
| [Data Mapper for Jackson][25]              | [The Apache Software License, Version 2.0][5] |
| [Spark Project SQL][26]                    | [Apache 2.0 License][27]                      |
| [Parquet for Java][28]                     | [MIT License][29]                             |
| [JUL to SLF4J bridge][30]                  | [MIT License][31]                             |
| [SLF4J LOG4J-12 Binding relocated][30]     | [MIT License][31]                             |
| [Apache Log4j API][32]                     | [Apache License, Version 2.0][3]              |
| [Apache Log4j 1.x Compatibility API][33]   | [Apache License, Version 2.0][3]              |
| [Apache Log4j Core][34]                    | [Apache License, Version 2.0][3]              |
| [scala-logging][35]                        | [Apache 2.0 License][27]                      |

## Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [scalatest][36]                            | [the Apache License, ASL Version 2.0][24] |
| [scalatestplus-mockito][37]                | [Apache-2.0][24]                          |
| [mockito-core][38]                         | [The MIT License][39]                     |
| [Hamcrest][40]                             | [BSD License 3][41]                       |
| [testcontainers-scala-scalatest][42]       | [The MIT License (MIT)][12]               |
| [Testcontainers :: Localstack][43]         | [MIT][44]                                 |
| [Test containers for Exasol on Docker][45] | [MIT License][46]                         |
| [Test Database Builder for Java][47]       | [MIT License][48]                         |
| [Matcher for SQL Result Sets][49]          | [MIT License][50]                         |
| [EqualsVerifier | release normal jar][51]  | [Apache License, Version 2.0][3]          |

## Plugin Dependencies

| Dependency                                              | License                                       |
| ------------------------------------------------------- | --------------------------------------------- |
| [SonarQube Scanner for Maven][52]                       | [GNU LGPL 3][53]                              |
| [Apache Maven Compiler Plugin][54]                      | [Apache License, Version 2.0][3]              |
| [Apache Maven Enforcer Plugin][55]                      | [Apache License, Version 2.0][3]              |
| [Maven Flatten Plugin][56]                              | [Apache Software Licenese][5]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][57] | [ASL2][5]                                     |
| [scala-maven-plugin][58]                                | [Public domain (Unlicense)][59]               |
| [ScalaTest Maven Plugin][60]                            | [the Apache License, ASL Version 2.0][24]     |
| [Apache Maven Javadoc Plugin][61]                       | [Apache License, Version 2.0][3]              |
| [Maven Surefire Plugin][62]                             | [Apache License, Version 2.0][3]              |
| [Versions Maven Plugin][63]                             | [Apache License, Version 2.0][3]              |
| [Apache Maven Assembly Plugin][64]                      | [Apache License, Version 2.0][3]              |
| [Apache Maven JAR Plugin][65]                           | [Apache License, Version 2.0][3]              |
| [Artifact reference checker and unifier][66]            | [MIT][12]                                     |
| [Maven Failsafe Plugin][67]                             | [Apache License, Version 2.0][3]              |
| [JaCoCo :: Maven Plugin][68]                            | [Eclipse Public License 2.0][69]              |
| [error-code-crawler-maven-plugin][70]                   | [MIT License][71]                             |
| [Reproducible Build Maven Plugin][72]                   | [Apache 2.0][5]                               |
| [Project keeper maven plugin][73]                       | [The MIT License][74]                         |
| [OpenFastTrace Maven Plugin][75]                        | [GNU General Public License v3.0][76]         |
| [Scalastyle Maven Plugin][77]                           | [Apache 2.0][27]                              |
| [spotless-maven-plugin][78]                             | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][79]                             | [BSD-3-Clause][18]                            |
| [Maven Clean Plugin][80]                                | [The Apache Software License, Version 2.0][5] |
| [Maven Resources Plugin][81]                            | [The Apache Software License, Version 2.0][5] |
| [Maven Install Plugin][82]                              | [The Apache Software License, Version 2.0][5] |
| [Maven Deploy Plugin][83]                               | [The Apache Software License, Version 2.0][5] |
| [Maven Site Plugin 3][84]                               | [The Apache Software License, Version 2.0][5] |

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
[25]: https://github.com/codehaus/jackson
[26]: https://spark.apache.org/
[27]: http://www.apache.org/licenses/LICENSE-2.0.html
[28]: https://github.com/exasol/parquet-io-java/
[29]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[30]: http://www.slf4j.org
[31]: http://www.opensource.org/licenses/mit-license.php
[32]: https://logging.apache.org/log4j/2.x/log4j-api/
[33]: https://logging.apache.org/log4j/2.x/log4j-1.2-api/
[34]: https://logging.apache.org/log4j/2.x/log4j-core/
[35]: https://github.com/lightbend/scala-logging
[36]: http://www.scalatest.org
[37]: https://github.com/scalatest/scalatestplus-mockito
[38]: https://github.com/mockito/mockito
[39]: https://github.com/mockito/mockito/blob/main/LICENSE
[40]: http://hamcrest.org/JavaHamcrest/
[41]: http://opensource.org/licenses/BSD-3-Clause
[42]: https://github.com/testcontainers/testcontainers-scala
[43]: https://testcontainers.org
[44]: http://opensource.org/licenses/MIT
[45]: https://github.com/exasol/exasol-testcontainers/
[46]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[47]: https://github.com/exasol/test-db-builder-java/
[48]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[49]: https://github.com/exasol/hamcrest-resultset-matcher/
[50]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[51]: https://www.jqno.nl/equalsverifier
[52]: http://sonarsource.github.io/sonar-scanner-maven/
[53]: http://www.gnu.org/licenses/lgpl.txt
[54]: https://maven.apache.org/plugins/maven-compiler-plugin/
[55]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[56]: https://www.mojohaus.org/flatten-maven-plugin/
[57]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[58]: http://github.com/davidB/scala-maven-plugin
[59]: http://unlicense.org/
[60]: https://github.com/scalatest/scalatest-maven-plugin
[61]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[62]: https://maven.apache.org/surefire/maven-surefire-plugin/
[63]: http://www.mojohaus.org/versions-maven-plugin/
[64]: https://maven.apache.org/plugins/maven-assembly-plugin/
[65]: https://maven.apache.org/plugins/maven-jar-plugin/
[66]: https://github.com/exasol/artifact-reference-checker-maven-plugin
[67]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[68]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[69]: https://www.eclipse.org/legal/epl-2.0/
[70]: https://github.com/exasol/error-code-crawler-maven-plugin/
[71]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[72]: http://zlika.github.io/reproducible-build-maven-plugin
[73]: https://github.com/exasol/project-keeper/
[74]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[75]: https://github.com/itsallcode/openfasttrace-maven-plugin
[76]: https://www.gnu.org/licenses/gpl-3.0.html
[77]: http://www.scalastyle.org
[78]: https://github.com/diffplug/spotless
[79]: https://github.com/evis/scalafix-maven-plugin
[80]: http://maven.apache.org/plugins/maven-clean-plugin/
[81]: http://maven.apache.org/plugins/maven-resources-plugin/
[82]: http://maven.apache.org/plugins/maven-install-plugin/
[83]: http://maven.apache.org/plugins/maven-deploy-plugin/
[84]: http://maven.apache.org/plugins/maven-site-plugin/
