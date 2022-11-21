<!-- @formatter:off -->
# Dependencies

## Compile Dependencies

| Dependency                                 | License                                       |
| ------------------------------------------ | --------------------------------------------- |
| [Scala Library][0]                         | [Apache-2.0][1]                               |
| [Apache Commons Lang][2]                   | [Apache License, Version 2.0][3]              |
| [Guava: Google Core Libraries for Java][4] | [Apache License, Version 2.0][5]              |
| [jackson-databind][6]                      | [The Apache Software License, Version 2.0][3] |
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
| [Data Mapper for Jackson][25]              | [The Apache Software License, Version 2.0][5] |
| [Spark Project SQL][26]                    | [Apache 2.0 License][27]                      |
| [Apache Ivy][28]                           | [The Apache Software License, Version 2.0][5] |
| [Apache Commons Text][29]                  | [Apache License, Version 2.0][3]              |
| [Parquet for Java][30]                     | [MIT License][31]                             |
| [JUL to SLF4J bridge][32]                  | [MIT License][33]                             |
| [SLF4J LOG4J-12 Binding relocated][32]     | [MIT License][33]                             |
| [Apache Log4j API][34]                     | [Apache License, Version 2.0][3]              |
| [Apache Log4j 1.x Compatibility API][35]   | [Apache License, Version 2.0][3]              |
| [Apache Log4j Core][36]                    | [Apache License, Version 2.0][3]              |
| [scala-logging][37]                        | [Apache 2.0 License][27]                      |

## Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [scalatest][38]                            | [the Apache License, ASL Version 2.0][24] |
| [scalatestplus-mockito][39]                | [Apache-2.0][24]                          |
| [mockito-core][40]                         | [The MIT License][41]                     |
| [Hamcrest][42]                             | [BSD License 3][43]                       |
| [testcontainers-scala-scalatest][44]       | [The MIT License (MIT)][12]               |
| [Testcontainers :: Localstack][45]         | [MIT][46]                                 |
| [Test containers for Exasol on Docker][47] | [MIT License][48]                         |
| [Test Database Builder for Java][49]       | [MIT License][50]                         |
| [Matcher for SQL Result Sets][51]          | [MIT License][52]                         |
| [EqualsVerifier | release normal jar][53]  | [Apache License, Version 2.0][3]          |
| [JUnit Jupiter Engine][54]                 | [Eclipse Public License v2.0][55]         |
| [Maven Project Version Getter][56]         | [MIT License][57]                         |
| [Extension integration tests library][58]  | [MIT License][59]                         |

## Plugin Dependencies

| Dependency                                              | License                                       |
| ------------------------------------------------------- | --------------------------------------------- |
| [SonarQube Scanner for Maven][60]                       | [GNU LGPL 3][61]                              |
| [Apache Maven Compiler Plugin][62]                      | [Apache License, Version 2.0][3]              |
| [Apache Maven Enforcer Plugin][63]                      | [Apache License, Version 2.0][3]              |
| [Maven Flatten Plugin][64]                              | [Apache Software Licenese][3]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][65] | [ASL2][5]                                     |
| [scala-maven-plugin][66]                                | [Public domain (Unlicense)][67]               |
| [ScalaTest Maven Plugin][68]                            | [the Apache License, ASL Version 2.0][24]     |
| [Apache Maven Javadoc Plugin][69]                       | [Apache License, Version 2.0][3]              |
| [Maven Surefire Plugin][70]                             | [Apache License, Version 2.0][3]              |
| [Versions Maven Plugin][71]                             | [Apache License, Version 2.0][3]              |
| [Apache Maven Assembly Plugin][72]                      | [Apache License, Version 2.0][3]              |
| [Apache Maven JAR Plugin][73]                           | [Apache License, Version 2.0][3]              |
| [Artifact reference checker and unifier][74]            | [MIT License][75]                             |
| [Maven Failsafe Plugin][76]                             | [Apache License, Version 2.0][3]              |
| [JaCoCo :: Maven Plugin][77]                            | [Eclipse Public License 2.0][78]              |
| [error-code-crawler-maven-plugin][79]                   | [MIT License][80]                             |
| [Reproducible Build Maven Plugin][81]                   | [Apache 2.0][5]                               |
| [Project keeper maven plugin][82]                       | [The MIT License][83]                         |
| [OpenFastTrace Maven Plugin][84]                        | [GNU General Public License v3.0][85]         |
| [Scalastyle Maven Plugin][86]                           | [Apache 2.0][27]                              |
| [spotless-maven-plugin][87]                             | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][88]                             | [BSD-3-Clause][18]                            |
| [Exec Maven Plugin][89]                                 | [Apache License 2][5]                         |
| [Apache Maven Clean Plugin][90]                         | [Apache License, Version 2.0][3]              |
| [Maven Resources Plugin][91]                            | [The Apache Software License, Version 2.0][5] |
| [Maven Install Plugin][92]                              | [The Apache Software License, Version 2.0][5] |
| [Maven Deploy Plugin][93]                               | [The Apache Software License, Version 2.0][5] |
| [Maven Site Plugin 3][94]                               | [The Apache Software License, Version 2.0][5] |

[0]: https://www.scala-lang.org/
[1]: https://www.apache.org/licenses/LICENSE-2.0
[2]: https://commons.apache.org/proper/commons-lang/
[3]: https://www.apache.org/licenses/LICENSE-2.0.txt
[4]: https://github.com/google/guava
[5]: http://www.apache.org/licenses/LICENSE-2.0.txt
[6]: https://github.com/FasterXML/jackson
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
[28]: http://ant.apache.org/ivy/
[29]: https://commons.apache.org/proper/commons-text
[30]: https://github.com/exasol/parquet-io-java/
[31]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[32]: http://www.slf4j.org
[33]: http://www.opensource.org/licenses/mit-license.php
[34]: https://logging.apache.org/log4j/2.x/log4j-api/
[35]: https://logging.apache.org/log4j/2.x/log4j-1.2-api/
[36]: https://logging.apache.org/log4j/2.x/log4j-core/
[37]: https://github.com/lightbend/scala-logging
[38]: http://www.scalatest.org
[39]: https://github.com/scalatest/scalatestplus-mockito
[40]: https://github.com/mockito/mockito
[41]: https://github.com/mockito/mockito/blob/main/LICENSE
[42]: http://hamcrest.org/JavaHamcrest/
[43]: http://opensource.org/licenses/BSD-3-Clause
[44]: https://github.com/testcontainers/testcontainers-scala
[45]: https://testcontainers.org
[46]: http://opensource.org/licenses/MIT
[47]: https://github.com/exasol/exasol-testcontainers/
[48]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[49]: https://github.com/exasol/test-db-builder-java/
[50]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[51]: https://github.com/exasol/hamcrest-resultset-matcher/
[52]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[53]: https://www.jqno.nl/equalsverifier
[54]: https://junit.org/junit5/
[55]: https://www.eclipse.org/legal/epl-v20.html
[56]: https://github.com/exasol/maven-project-version-getter/
[57]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[58]: https://github.com/exasol/extension-manager/
[59]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[60]: http://sonarsource.github.io/sonar-scanner-maven/
[61]: http://www.gnu.org/licenses/lgpl.txt
[62]: https://maven.apache.org/plugins/maven-compiler-plugin/
[63]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[64]: https://www.mojohaus.org/flatten-maven-plugin/
[65]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[66]: http://github.com/davidB/scala-maven-plugin
[67]: http://unlicense.org/
[68]: https://github.com/scalatest/scalatest-maven-plugin
[69]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[70]: https://maven.apache.org/surefire/maven-surefire-plugin/
[71]: https://www.mojohaus.org/versions-maven-plugin/
[72]: https://maven.apache.org/plugins/maven-assembly-plugin/
[73]: https://maven.apache.org/plugins/maven-jar-plugin/
[74]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[75]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[76]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[77]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[78]: https://www.eclipse.org/legal/epl-2.0/
[79]: https://github.com/exasol/error-code-crawler-maven-plugin/
[80]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[81]: http://zlika.github.io/reproducible-build-maven-plugin
[82]: https://github.com/exasol/project-keeper/
[83]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[84]: https://github.com/itsallcode/openfasttrace-maven-plugin
[85]: https://www.gnu.org/licenses/gpl-3.0.html
[86]: http://www.scalastyle.org
[87]: https://github.com/diffplug/spotless
[88]: https://github.com/evis/scalafix-maven-plugin
[89]: http://www.mojohaus.org/exec-maven-plugin
[90]: https://maven.apache.org/plugins/maven-clean-plugin/
[91]: http://maven.apache.org/plugins/maven-resources-plugin/
[92]: http://maven.apache.org/plugins/maven-install-plugin/
[93]: http://maven.apache.org/plugins/maven-deploy-plugin/
[94]: http://maven.apache.org/plugins/maven-site-plugin/
