<!-- @formatter:off -->
# Dependencies

## Compile Dependencies

| Dependency                                 | License                                       |
| ------------------------------------------ | --------------------------------------------- |
| [Scala Library][0]                         | [Apache-2.0][1]                               |
| [Apache Commons Lang][2]                   | [Apache License, Version 2.0][3]              |
| [Guava: Google Core Libraries for Java][4] | [Apache License, Version 2.0][5]              |
| [jackson-databind][6]                      | [The Apache Software License, Version 2.0][5] |
| [jackson-module-scala][8]                  | [The Apache Software License, Version 2.0][3] |
| [io.grpc:grpc-netty][10]                   | [Apache 2.0][11]                              |
| [Netty/All-in-One][12]                     | [Apache License, Version 2.0][1]              |
| [import-export-udf-common-scala][14]       | [MIT][15]                                     |
| [error-reporting-java][16]                 | [MIT][15]                                     |
| Apache Hadoop Common                       | [Apache License, Version 2.0][3]              |
| Apache Hadoop Amazon Web Services support  | [Apache License, Version 2.0][3]              |
| Apache Hadoop Azure support                | [Apache License, Version 2.0][3]              |
| Apache Hadoop Azure Data Lake support      | [Apache License, Version 2.0][3]              |
| Apache Hadoop HDFS                         | [Apache License, Version 2.0][3]              |
| [Alluxio Core - Client - HDFS][23]         | [Apache License][24]                          |
| [Protocol Buffers [Core]][25]              | [BSD-3-Clause][26]                            |
| [gcs-connector-hadoop3][27]                | [Apache License, Version 2.0][5]              |
| [Google OAuth Client Library for Java][29] | [The Apache Software License, Version 2.0][3] |
| [ORC Core][31]                             | [Apache License, Version 2.0][3]              |
| [Apache Avro][33]                          | [Apache License, Version 2.0][3]              |
| [delta-core][35]                           | [Apache-2.0][36]                              |
| [Data Mapper for Jackson][37]              | [The Apache Software License, Version 2.0][5] |
| [Spark Project SQL][39]                    | [Apache 2.0 License][40]                      |
| [Parquet for Java][41]                     | [MIT License][42]                             |
| [JUL to SLF4J bridge][43]                  | [MIT License][44]                             |
| [SLF4J LOG4J-12 Binding relocated][43]     | [MIT License][44]                             |
| [Apache Log4j API][47]                     | [Apache License, Version 2.0][3]              |
| [Apache Log4j 1.x Compatibility API][49]   | [Apache License, Version 2.0][3]              |
| [scala-logging][51]                        | [Apache 2.0 License][40]                      |

## Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [scalatest][53]                            | [the Apache License, ASL Version 2.0][36] |
| [scalatestplus-mockito][55]                | [Apache-2.0][36]                          |
| [mockito-core][57]                         | [The MIT License][58]                     |
| [Hamcrest][59]                             | [BSD License 3][60]                       |
| [testcontainers-scala-scalatest][61]       | [The MIT License (MIT)][15]               |
| [Testcontainers :: Localstack][63]         | [MIT][64]                                 |
| [Test containers for Exasol on Docker][65] | [MIT][15]                                 |
| [Test Database Builder for Java][67]       | [MIT License][68]                         |
| [Matcher for SQL Result Sets][69]          | [MIT][15]                                 |

## Plugin Dependencies

| Dependency                                              | License                                       |
| ------------------------------------------------------- | --------------------------------------------- |
| [SonarQube Scanner for Maven][71]                       | [GNU LGPL 3][72]                              |
| [Apache Maven Compiler Plugin][73]                      | [Apache License, Version 2.0][3]              |
| [Apache Maven Enforcer Plugin][75]                      | [Apache License, Version 2.0][3]              |
| [Maven Flatten Plugin][77]                              | [Apache Software Licenese][5]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][79] | [ASL2][5]                                     |
| [Reproducible Build Maven Plugin][81]                   | [Apache 2.0][5]                               |
| [scala-maven-plugin][83]                                | [Public domain (Unlicense)][84]               |
| [ScalaTest Maven Plugin][85]                            | [the Apache License, ASL Version 2.0][36]     |
| [Apache Maven Javadoc Plugin][87]                       | [Apache License, Version 2.0][3]              |
| [Maven Surefire Plugin][89]                             | [Apache License, Version 2.0][3]              |
| [Versions Maven Plugin][91]                             | [Apache License, Version 2.0][3]              |
| [Apache Maven Assembly Plugin][93]                      | [Apache License, Version 2.0][3]              |
| [Apache Maven JAR Plugin][95]                           | [Apache License, Version 2.0][3]              |
| [Artifact reference checker and unifier][97]            | [MIT][15]                                     |
| [Maven Failsafe Plugin][99]                             | [Apache License, Version 2.0][3]              |
| [JaCoCo :: Maven Plugin][101]                           | [Eclipse Public License 2.0][102]             |
| [error-code-crawler-maven-plugin][103]                  | [MIT][15]                                     |
| [Project keeper maven plugin][105]                      | [The MIT License][106]                        |
| [OpenFastTrace Maven Plugin][107]                       | [GNU General Public License v3.0][108]        |
| [Scalastyle Maven Plugin][109]                          | [Apache 2.0][40]                              |
| [spotless-maven-plugin][111]                            | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][113]                            | [BSD-3-Clause][26]                            |
| [Maven Clean Plugin][115]                               | [The Apache Software License, Version 2.0][5] |
| [Maven Resources Plugin][117]                           | [The Apache Software License, Version 2.0][5] |
| [Maven Install Plugin][119]                             | [The Apache Software License, Version 2.0][5] |
| [Maven Deploy Plugin][121]                              | [The Apache Software License, Version 2.0][5] |
| [Maven Site Plugin 3][123]                              | [The Apache Software License, Version 2.0][5] |

[55]: https://github.com/scalatest/scalatestplus-mockito
[16]: https://github.com/exasol/error-reporting-java
[5]: http://www.apache.org/licenses/LICENSE-2.0.txt
[109]: http://www.scalastyle.org
[89]: https://maven.apache.org/surefire/maven-surefire-plugin/
[111]: https://github.com/diffplug/spotless
[115]: http://maven.apache.org/plugins/maven-clean-plugin/
[39]: https://spark.apache.org/
[14]: https://github.com/exasol/import-export-udf-common-scala
[15]: https://opensource.org/licenses/MIT
[57]: https://github.com/mockito/mockito
[8]: https://github.com/FasterXML/jackson-module-scala
[11]: https://opensource.org/licenses/Apache-2.0
[42]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[77]: https://www.mojohaus.org/flatten-maven-plugin/
[35]: https://delta.io/
[91]: http://www.mojohaus.org/versions-maven-plugin/
[105]: https://github.com/exasol/project-keeper/
[37]: https://github.com/codehaus/jackson
[60]: http://opensource.org/licenses/BSD-3-Clause
[73]: https://maven.apache.org/plugins/maven-compiler-plugin/
[68]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[107]: https://github.com/itsallcode/openfasttrace-maven-plugin
[102]: https://www.eclipse.org/legal/epl-2.0/
[6]: http://github.com/FasterXML/jackson
[72]: http://www.gnu.org/licenses/lgpl.txt
[84]: http://unlicense.org/
[1]: https://www.apache.org/licenses/LICENSE-2.0
[101]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[58]: https://github.com/mockito/mockito/blob/main/LICENSE
[69]: https://github.com/exasol/hamcrest-resultset-matcher
[81]: http://zlika.github.io/reproducible-build-maven-plugin
[44]: http://www.opensource.org/licenses/mit-license.php
[26]: https://opensource.org/licenses/BSD-3-Clause
[71]: http://sonarsource.github.io/sonar-scanner-maven/
[12]: https://netty.io/
[24]: https://github.com/alluxio/alluxio/blob/master/LICENSE
[33]: https://avro.apache.org
[51]: https://github.com/lightbend/scala-logging
[10]: https://github.com/grpc/grpc-java
[59]: http://hamcrest.org/JavaHamcrest/
[43]: http://www.slf4j.org
[117]: http://maven.apache.org/plugins/maven-resources-plugin/
[27]: https://github.com/GoogleCloudDataproc/hadoop-connectors/tree/master/gcs
[97]: https://github.com/exasol/artifact-reference-checker-maven-plugin
[113]: https://github.com/evis/scalafix-maven-plugin
[95]: https://maven.apache.org/plugins/maven-jar-plugin/
[36]: http://www.apache.org/licenses/LICENSE-2.0
[29]: https://github.com/googleapis/google-oauth-java-client
[61]: https://github.com/testcontainers/testcontainers-scala
[67]: https://github.com/exasol/test-db-builder-java/
[4]: https://github.com/google/guava
[40]: http://www.apache.org/licenses/LICENSE-2.0.html
[53]: http://www.scalatest.org
[47]: https://logging.apache.org/log4j/2.x/log4j-api/
[99]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[49]: https://logging.apache.org/log4j/2.x/log4j-1.2-api/
[2]: https://commons.apache.org/proper/commons-lang/
[64]: http://opensource.org/licenses/MIT
[0]: https://www.scala-lang.org/
[65]: https://github.com/exasol/exasol-testcontainers
[23]: https://www.alluxio.io
[106]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[108]: https://www.gnu.org/licenses/gpl-3.0.html
[3]: https://www.apache.org/licenses/LICENSE-2.0.txt
[75]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[25]: https://github.com/protocolbuffers/protobuf/tree/main/java
[119]: http://maven.apache.org/plugins/maven-install-plugin/
[79]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[63]: https://testcontainers.org
[83]: http://github.com/davidB/scala-maven-plugin
[85]: https://github.com/scalatest/scalatest-maven-plugin
[41]: https://github.com/exasol/parquet-io-java/
[121]: http://maven.apache.org/plugins/maven-deploy-plugin/
[123]: http://maven.apache.org/plugins/maven-site-plugin/
[87]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[31]: https://orc.apache.org/
[103]: https://github.com/exasol/error-code-crawler-maven-plugin
[93]: https://maven.apache.org/plugins/maven-assembly-plugin/
