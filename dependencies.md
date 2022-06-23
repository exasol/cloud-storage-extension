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
| [error-reporting-java][13]                 | [MIT][12]                                     |
| Apache Hadoop Common                       | [Apache License, Version 2.0][3]              |
| Apache Hadoop Amazon Web Services support  | [Apache License, Version 2.0][3]              |
| Apache Hadoop Azure support                | [Apache License, Version 2.0][3]              |
| Apache Hadoop Azure Data Lake support      | [Apache License, Version 2.0][3]              |
| Apache Hadoop HDFS                         | [Apache License, Version 2.0][3]              |
| [Alluxio Core - Client - HDFS][14]         | [Apache License][15]                          |
| [Protocol Buffers [Core]][16]              | [BSD-3-Clause][17]                            |
| [gcs-connector-hadoop3][18]                | [Apache License, Version 2.0][5]              |
| [Google OAuth Client Library for Java][19] | [The Apache Software License, Version 2.0][3] |
| [ORC Core][20]                             | [Apache License, Version 2.0][3]              |
| [Apache Avro][21]                          | [Apache License, Version 2.0][3]              |
| [delta-core][22]                           | [Apache-2.0][23]                              |
| [Data Mapper for Jackson][24]              | [The Apache Software License, Version 2.0][5] |
| [Spark Project SQL][25]                    | [Apache 2.0 License][26]                      |
| [Parquet for Java][27]                     | [MIT License][28]                             |
| [JUL to SLF4J bridge][29]                  | [MIT License][30]                             |
| [SLF4J LOG4J-12 Binding relocated][29]     | [MIT License][30]                             |
| [Apache Log4j API][31]                     | [Apache License, Version 2.0][3]              |
| [Apache Log4j 1.x Compatibility API][32]   | [Apache License, Version 2.0][3]              |
| [scala-logging][33]                        | [Apache 2.0 License][26]                      |

## Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [scalatest][34]                            | [the Apache License, ASL Version 2.0][23] |
| [scalatestplus-mockito][35]                | [Apache-2.0][23]                          |
| [mockito-core][36]                         | [The MIT License][37]                     |
| [Hamcrest][38]                             | [BSD License 3][39]                       |
| [testcontainers-scala-scalatest][40]       | [The MIT License (MIT)][12]               |
| [Testcontainers :: Localstack][41]         | [MIT][42]                                 |
| [Test containers for Exasol on Docker][43] | [MIT][12]                                 |
| [Test Database Builder for Java][44]       | [MIT License][45]                         |
| [Matcher for SQL Result Sets][46]          | [MIT][12]                                 |

## Plugin Dependencies

| Dependency                                              | License                                       |
| ------------------------------------------------------- | --------------------------------------------- |
| [SonarQube Scanner for Maven][47]                       | [GNU LGPL 3][48]                              |
| [Apache Maven Compiler Plugin][49]                      | [Apache License, Version 2.0][3]              |
| [Apache Maven Enforcer Plugin][50]                      | [Apache License, Version 2.0][3]              |
| [Maven Flatten Plugin][51]                              | [Apache Software Licenese][5]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][52] | [ASL2][5]                                     |
| [Reproducible Build Maven Plugin][53]                   | [Apache 2.0][5]                               |
| [scala-maven-plugin][54]                                | [Public domain (Unlicense)][55]               |
| [ScalaTest Maven Plugin][56]                            | [the Apache License, ASL Version 2.0][23]     |
| [Apache Maven Javadoc Plugin][57]                       | [Apache License, Version 2.0][3]              |
| [Maven Surefire Plugin][58]                             | [Apache License, Version 2.0][3]              |
| [Versions Maven Plugin][59]                             | [Apache License, Version 2.0][3]              |
| [Apache Maven Assembly Plugin][60]                      | [Apache License, Version 2.0][3]              |
| [Apache Maven JAR Plugin][61]                           | [Apache License, Version 2.0][3]              |
| [Artifact reference checker and unifier][62]            | [MIT][12]                                     |
| [Maven Failsafe Plugin][63]                             | [Apache License, Version 2.0][3]              |
| [JaCoCo :: Maven Plugin][64]                            | [Eclipse Public License 2.0][65]              |
| [error-code-crawler-maven-plugin][66]                   | [MIT][12]                                     |
| [Project keeper maven plugin][67]                       | [The MIT License][68]                         |
| [OpenFastTrace Maven Plugin][69]                        | [GNU General Public License v3.0][70]         |
| [Scalastyle Maven Plugin][71]                           | [Apache 2.0][26]                              |
| [spotless-maven-plugin][72]                             | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][73]                             | [BSD-3-Clause][17]                            |
| [Maven Clean Plugin][74]                                | [The Apache Software License, Version 2.0][5] |
| [Maven Resources Plugin][75]                            | [The Apache Software License, Version 2.0][5] |
| [Maven Install Plugin][76]                              | [The Apache Software License, Version 2.0][5] |
| [Maven Deploy Plugin][77]                               | [The Apache Software License, Version 2.0][5] |
| [Maven Site Plugin 3][78]                               | [The Apache Software License, Version 2.0][5] |

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
[13]: https://github.com/exasol/error-reporting-java
[14]: https://www.alluxio.io
[15]: https://github.com/alluxio/alluxio/blob/master/LICENSE
[16]: https://github.com/protocolbuffers/protobuf/tree/main/java
[17]: https://opensource.org/licenses/BSD-3-Clause
[18]: https://github.com/GoogleCloudDataproc/hadoop-connectors/tree/master/gcs
[19]: https://github.com/googleapis/google-oauth-java-client
[20]: https://orc.apache.org/
[21]: https://avro.apache.org
[22]: https://delta.io/
[23]: http://www.apache.org/licenses/LICENSE-2.0
[24]: https://github.com/codehaus/jackson
[25]: https://spark.apache.org/
[26]: http://www.apache.org/licenses/LICENSE-2.0.html
[27]: https://github.com/exasol/parquet-io-java/
[28]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[29]: http://www.slf4j.org
[30]: http://www.opensource.org/licenses/mit-license.php
[31]: https://logging.apache.org/log4j/2.x/log4j-api/
[32]: https://logging.apache.org/log4j/2.x/log4j-1.2-api/
[33]: https://github.com/lightbend/scala-logging
[34]: http://www.scalatest.org
[35]: https://github.com/scalatest/scalatestplus-mockito
[36]: https://github.com/mockito/mockito
[37]: https://github.com/mockito/mockito/blob/main/LICENSE
[38]: http://hamcrest.org/JavaHamcrest/
[39]: http://opensource.org/licenses/BSD-3-Clause
[40]: https://github.com/testcontainers/testcontainers-scala
[41]: https://testcontainers.org
[42]: http://opensource.org/licenses/MIT
[43]: https://github.com/exasol/exasol-testcontainers
[44]: https://github.com/exasol/test-db-builder-java/
[45]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[46]: https://github.com/exasol/hamcrest-resultset-matcher
[47]: http://sonarsource.github.io/sonar-scanner-maven/
[48]: http://www.gnu.org/licenses/lgpl.txt
[49]: https://maven.apache.org/plugins/maven-compiler-plugin/
[50]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[51]: https://www.mojohaus.org/flatten-maven-plugin/
[52]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[53]: http://zlika.github.io/reproducible-build-maven-plugin
[54]: http://github.com/davidB/scala-maven-plugin
[55]: http://unlicense.org/
[56]: https://github.com/scalatest/scalatest-maven-plugin
[57]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[58]: https://maven.apache.org/surefire/maven-surefire-plugin/
[59]: http://www.mojohaus.org/versions-maven-plugin/
[60]: https://maven.apache.org/plugins/maven-assembly-plugin/
[61]: https://maven.apache.org/plugins/maven-jar-plugin/
[62]: https://github.com/exasol/artifact-reference-checker-maven-plugin
[63]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[64]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[65]: https://www.eclipse.org/legal/epl-2.0/
[66]: https://github.com/exasol/error-code-crawler-maven-plugin
[67]: https://github.com/exasol/project-keeper/
[68]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[69]: https://github.com/itsallcode/openfasttrace-maven-plugin
[70]: https://www.gnu.org/licenses/gpl-3.0.html
[71]: http://www.scalastyle.org
[72]: https://github.com/diffplug/spotless
[73]: https://github.com/evis/scalafix-maven-plugin
[74]: http://maven.apache.org/plugins/maven-clean-plugin/
[75]: http://maven.apache.org/plugins/maven-resources-plugin/
[76]: http://maven.apache.org/plugins/maven-install-plugin/
[77]: http://maven.apache.org/plugins/maven-deploy-plugin/
[78]: http://maven.apache.org/plugins/maven-site-plugin/
