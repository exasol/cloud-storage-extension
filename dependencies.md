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
| [ORC Core][19]                             | [Apache License, Version 2.0][3]              |
| [Apache Avro][20]                          | [Apache License, Version 2.0][3]              |
| [delta-core][21]                           | [Apache-2.0][22]                              |
| [Data Mapper for Jackson][23]              | [The Apache Software License, Version 2.0][5] |
| [Spark Project SQL][24]                    | [Apache 2.0 License][25]                      |
| [Parquet for Java][26]                     | [MIT License][27]                             |
| [JUL to SLF4J bridge][28]                  | [MIT License][29]                             |
| [SLF4J LOG4J-12 Binding relocated][28]     | [MIT License][29]                             |
| [Apache Log4j API][30]                     | [Apache License, Version 2.0][3]              |
| [Apache Log4j 1.x Compatibility API][31]   | [Apache License, Version 2.0][3]              |
| [scala-logging][32]                        | [Apache 2.0 License][25]                      |

## Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [scalatest][33]                            | [the Apache License, ASL Version 2.0][22] |
| [scalatestplus-mockito][34]                | [Apache-2.0][22]                          |
| [mockito-core][35]                         | [The MIT License][36]                     |
| [Hamcrest][37]                             | [BSD License 3][38]                       |
| [testcontainers-scala-scalatest][39]       | [The MIT License (MIT)][12]               |
| [Testcontainers :: Localstack][40]         | [MIT][41]                                 |
| [Test containers for Exasol on Docker][42] | [MIT][12]                                 |
| [Test Database Builder for Java][43]       | [MIT License][44]                         |
| [Matcher for SQL Result Sets][45]          | [MIT][12]                                 |

## Plugin Dependencies

| Dependency                                              | License                                       |
| ------------------------------------------------------- | --------------------------------------------- |
| [SonarQube Scanner for Maven][46]                       | [GNU LGPL 3][47]                              |
| [Apache Maven Compiler Plugin][48]                      | [Apache License, Version 2.0][3]              |
| [Apache Maven Enforcer Plugin][49]                      | [Apache License, Version 2.0][3]              |
| [Maven Flatten Plugin][50]                              | [Apache Software Licenese][5]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][51] | [ASL2][5]                                     |
| [Reproducible Build Maven Plugin][52]                   | [Apache 2.0][5]                               |
| [scala-maven-plugin][53]                                | [Public domain (Unlicense)][54]               |
| [ScalaTest Maven Plugin][55]                            | [the Apache License, ASL Version 2.0][22]     |
| [Apache Maven Javadoc Plugin][56]                       | [Apache License, Version 2.0][3]              |
| [Maven Surefire Plugin][57]                             | [Apache License, Version 2.0][3]              |
| [Versions Maven Plugin][58]                             | [Apache License, Version 2.0][3]              |
| [Apache Maven Assembly Plugin][59]                      | [Apache License, Version 2.0][3]              |
| [Apache Maven JAR Plugin][60]                           | [Apache License, Version 2.0][3]              |
| [Artifact reference checker and unifier][61]            | [MIT][12]                                     |
| [Maven Failsafe Plugin][62]                             | [Apache License, Version 2.0][3]              |
| [JaCoCo :: Maven Plugin][63]                            | [Eclipse Public License 2.0][64]              |
| [error-code-crawler-maven-plugin][65]                   | [MIT][12]                                     |
| [Project keeper maven plugin][66]                       | [The MIT License][67]                         |
| [OpenFastTrace Maven Plugin][68]                        | [GNU General Public License v3.0][69]         |
| [Scalastyle Maven Plugin][70]                           | [Apache 2.0][25]                              |
| [spotless-maven-plugin][71]                             | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][72]                             | [BSD-3-Clause][17]                            |
| [Maven Clean Plugin][73]                                | [The Apache Software License, Version 2.0][5] |
| [Maven Resources Plugin][74]                            | [The Apache Software License, Version 2.0][5] |
| [Maven Install Plugin][75]                              | [The Apache Software License, Version 2.0][5] |
| [Maven Deploy Plugin][76]                               | [The Apache Software License, Version 2.0][5] |
| [Maven Site Plugin 3][77]                               | [The Apache Software License, Version 2.0][5] |

[0]: https://www.scala-lang.org/
[1]: https://www.apache.org/licenses/LICENSE-2.0
[2]: https://commons.apache.org/proper/commons-lang/
[3]: https://www.apache.org/licenses/LICENSE-2.0.txt
[4]: https://github.com/google/guava
[5]: http://www.apache.org/licenses/LICENSE-2.0.txt
[6]: http://github.com/FasterXML/jackson
[7]: http://wiki.fasterxml.com/JacksonModuleScala
[8]: https://github.com/grpc/grpc-java
[9]: https://opensource.org/licenses/Apache-2.0
[10]: https://netty.io/netty-all/
[11]: https://github.com/exasol/import-export-udf-common-scala
[12]: https://opensource.org/licenses/MIT
[13]: https://github.com/exasol/error-reporting-java
[14]: https://www.alluxio.io/alluxio-core/alluxio-core-client/alluxio-core-client-hdfs/
[15]: https://github.com/alluxio/alluxio/blob/master/LICENSE
[16]: https://developers.google.com/protocol-buffers/protobuf-java/
[17]: https://opensource.org/licenses/BSD-3-Clause
[18]: https://github.com/GoogleCloudPlatform/BigData-interop/gcs-connector/
[19]: https://orc.apache.org/orc-core
[20]: https://avro.apache.org
[21]: https://delta.io/
[22]: http://www.apache.org/licenses/LICENSE-2.0
[23]: http://jackson.codehaus.org
[24]: https://spark.apache.org/
[25]: http://www.apache.org/licenses/LICENSE-2.0.html
[26]: https://github.com/exasol/parquet-io-java/
[27]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[28]: http://www.slf4j.org
[29]: http://www.opensource.org/licenses/mit-license.php
[30]: https://logging.apache.org/log4j/2.x/log4j-api/
[31]: https://logging.apache.org/log4j/2.x/log4j-1.2-api/
[32]: https://github.com/lightbend/scala-logging
[33]: http://www.scalatest.org
[34]: https://github.com/scalatest/scalatestplus-mockito
[35]: https://github.com/mockito/mockito
[36]: https://github.com/mockito/mockito/blob/main/LICENSE
[37]: http://hamcrest.org/JavaHamcrest/
[38]: http://opensource.org/licenses/BSD-3-Clause
[39]: https://github.com/testcontainers/testcontainers-scala
[40]: https://testcontainers.org
[41]: http://opensource.org/licenses/MIT
[42]: https://github.com/exasol/exasol-testcontainers
[43]: https://github.com/exasol/test-db-builder-java/
[44]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[45]: https://github.com/exasol/hamcrest-resultset-matcher
[46]: http://sonarsource.github.io/sonar-scanner-maven/
[47]: http://www.gnu.org/licenses/lgpl.txt
[48]: https://maven.apache.org/plugins/maven-compiler-plugin/
[49]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[50]: https://www.mojohaus.org/flatten-maven-plugin/
[51]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[52]: http://zlika.github.io/reproducible-build-maven-plugin
[53]: http://github.com/davidB/scala-maven-plugin
[54]: http://unlicense.org/
[55]: http://nexus.sonatype.org/oss-repository-hosting.html/scalatest-maven-plugin
[56]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[57]: https://maven.apache.org/surefire/maven-surefire-plugin/
[58]: http://www.mojohaus.org/versions-maven-plugin/
[59]: https://maven.apache.org/plugins/maven-assembly-plugin/
[60]: https://maven.apache.org/plugins/maven-jar-plugin/
[61]: https://github.com/exasol/artifact-reference-checker-maven-plugin
[62]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[63]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[64]: https://www.eclipse.org/legal/epl-2.0/
[65]: https://github.com/exasol/error-code-crawler-maven-plugin
[66]: https://github.com/exasol/project-keeper/
[67]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[68]: https://github.com/itsallcode/openfasttrace-maven-plugin
[69]: https://www.gnu.org/licenses/gpl-3.0.html
[70]: http://www.scalastyle.org
[71]: https://github.com/diffplug/spotless
[72]: https://github.com/evis/scalafix-maven-plugin
[73]: http://maven.apache.org/plugins/maven-clean-plugin/
[74]: http://maven.apache.org/plugins/maven-resources-plugin/
[75]: http://maven.apache.org/plugins/maven-install-plugin/
[76]: http://maven.apache.org/plugins/maven-deploy-plugin/
[77]: http://maven.apache.org/plugins/maven-site-plugin/
