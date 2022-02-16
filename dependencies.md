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
| [gcs-connector-hadoop3][25]                | [Apache License, Version 2.0][5]              |
| [ORC Core][27]                             | [Apache License, Version 2.0][3]              |
| [Apache Avro][29]                          | [Apache License, Version 2.0][3]              |
| [delta-core][31]                           | [Apache-2.0][32]                              |
| [Spark Project SQL][33]                    | [Apache 2.0 License][34]                      |
| [Parquet for Java][35]                     | [MIT][15]                                     |
| [JUL to SLF4J bridge][37]                  | [MIT License][38]                             |
| [SLF4J LOG4J-12 Binding relocated][37]     | [MIT License][38]                             |
| [Apache Log4j API][41]                     | [Apache License, Version 2.0][3]              |
| [Apache Log4j 1.x Compatibility API][43]   | [Apache License, Version 2.0][3]              |
| [scala-logging][45]                        | [Apache 2.0 License][34]                      |

## Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [scalatest][47]                            | [the Apache License, ASL Version 2.0][32] |
| [scalatestplus-mockito][49]                | [Apache-2.0][32]                          |
| [mockito-core][51]                         | [The MIT License][52]                     |
| [Hamcrest][53]                             | [BSD License 3][54]                       |
| [testcontainers-scala-scalatest][55]       | [The MIT License (MIT)][15]               |
| [Testcontainers :: Localstack][57]         | [MIT][58]                                 |
| [Test containers for Exasol on Docker][59] | [MIT][15]                                 |
| [Test Database Builder for Java][61]       | [MIT][15]                                 |
| [Matcher for SQL Result Sets][63]          | [MIT][15]                                 |

## Plugin Dependencies

| Dependency                                              | License                                       |
| ------------------------------------------------------- | --------------------------------------------- |
| [scala-maven-plugin][65]                                | [Public domain (Unlicense)][66]               |
| [Apache Maven Compiler Plugin][67]                      | [Apache License, Version 2.0][3]              |
| [ScalaTest Maven Plugin][69]                            | [the Apache License, ASL Version 2.0][32]     |
| [Apache Maven Enforcer Plugin][71]                      | [Apache License, Version 2.0][3]              |
| [Apache Maven Deploy Plugin][73]                        | [Apache License, Version 2.0][3]              |
| [Apache Maven GPG Plugin][75]                           | [Apache License, Version 2.0][3]              |
| [Nexus Staging Maven Plugin][77]                        | [Eclipse Public License][78]                  |
| [Apache Maven Source Plugin][79]                        | [Apache License, Version 2.0][3]              |
| [Apache Maven Javadoc Plugin][81]                       | [Apache License, Version 2.0][3]              |
| [Apache Maven JAR Plugin][83]                           | [Apache License, Version 2.0][3]              |
| [Apache Maven Assembly Plugin][85]                      | [Apache License, Version 2.0][3]              |
| [Versions Maven Plugin][87]                             | [Apache License, Version 2.0][3]              |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][89] | [ASL2][5]                                     |
| [Reproducible Build Maven Plugin][91]                   | [Apache 2.0][5]                               |
| [Project keeper maven plugin][93]                       | [MIT][15]                                     |
| [JaCoCo :: Maven Plugin][95]                            | [Eclipse Public License 2.0][96]              |
| [error-code-crawler-maven-plugin][97]                   | [MIT][15]                                     |
| [Artifact reference checker and unifier][99]            | [MIT][15]                                     |
| [OpenFastTrace Maven Plugin][101]                       | [GNU General Public License v3.0][102]        |
| [Scalastyle Maven Plugin][103]                          | [Apache 2.0][34]                              |
| [spotless-maven-plugin][105]                            | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][107]                            | [BSD-3-Clause][108]                           |
| [Apache Maven Clean Plugin][109]                        | [Apache License, Version 2.0][3]              |
| [Apache Maven Resources Plugin][111]                    | [Apache License, Version 2.0][3]              |
| [Maven Surefire Plugin][113]                            | [The Apache Software License, Version 2.0][5] |
| [Apache Maven Install Plugin][115]                      | [Apache License, Version 2.0][5]              |
| [Apache Maven Site Plugin][117]                         | [Apache License, Version 2.0][3]              |

[93]: https://github.com/exasol/project-keeper-maven-plugin
[49]: https://github.com/scalatest/scalatestplus-mockito
[16]: https://github.com/exasol/error-reporting-java
[8]: http://wiki.fasterxml.com/JacksonModuleScala
[35]: https://github.com/exasol/parquet-io-java
[5]: http://www.apache.org/licenses/LICENSE-2.0.txt
[103]: http://www.scalastyle.org
[105]: https://github.com/diffplug/spotless
[12]: https://netty.io/netty-all/
[14]: https://github.com/exasol/import-export-udf-common-scala
[15]: https://opensource.org/licenses/MIT
[51]: https://github.com/mockito/mockito
[25]: https://github.com/GoogleCloudPlatform/BigData-interop/gcs-connector/
[11]: https://opensource.org/licenses/Apache-2.0
[31]: https://delta.io/
[87]: http://www.mojohaus.org/versions-maven-plugin/
[54]: http://opensource.org/licenses/BSD-3-Clause
[67]: https://maven.apache.org/plugins/maven-compiler-plugin/
[111]: https://maven.apache.org/plugins/maven-resources-plugin/
[101]: https://github.com/itsallcode/openfasttrace-maven-plugin
[109]: https://maven.apache.org/plugins/maven-clean-plugin/
[96]: https://www.eclipse.org/legal/epl-2.0/
[6]: http://github.com/FasterXML/jackson
[73]: https://maven.apache.org/plugins/maven-deploy-plugin/
[66]: http://unlicense.org/
[1]: https://www.apache.org/licenses/LICENSE-2.0
[95]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[52]: https://github.com/mockito/mockito/blob/main/LICENSE
[63]: https://github.com/exasol/hamcrest-resultset-matcher
[91]: http://zlika.github.io/reproducible-build-maven-plugin
[38]: http://www.opensource.org/licenses/mit-license.php
[113]: http://maven.apache.org/surefire/maven-surefire-plugin
[108]: https://opensource.org/licenses/BSD-3-Clause
[23]: https://www.alluxio.io/alluxio-core/alluxio-core-client/alluxio-core-client-hdfs/
[24]: https://github.com/alluxio/alluxio/blob/master/LICENSE
[29]: https://avro.apache.org
[45]: https://github.com/lightbend/scala-logging
[79]: https://maven.apache.org/plugins/maven-source-plugin/
[10]: https://github.com/grpc/grpc-java
[27]: https://orc.apache.org/orc-core
[53]: http://hamcrest.org/JavaHamcrest/
[37]: http://www.slf4j.org
[99]: https://github.com/exasol/artifact-reference-checker-maven-plugin
[107]: https://github.com/evis/scalafix-maven-plugin
[83]: https://maven.apache.org/plugins/maven-jar-plugin/
[32]: http://www.apache.org/licenses/LICENSE-2.0
[55]: https://github.com/testcontainers/testcontainers-scala
[4]: https://github.com/google/guava
[77]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[34]: http://www.apache.org/licenses/LICENSE-2.0.html
[47]: http://www.scalatest.org
[41]: https://logging.apache.org/log4j/2.x/log4j-api/
[43]: https://logging.apache.org/log4j/2.x/log4j-1.2-api/
[61]: https://github.com/exasol/test-db-builder-java
[2]: https://commons.apache.org/proper/commons-lang/
[58]: http://opensource.org/licenses/MIT
[0]: https://www.scala-lang.org/
[78]: http://www.eclipse.org/legal/epl-v10.html
[59]: https://github.com/exasol/exasol-testcontainers
[117]: https://maven.apache.org/plugins/maven-site-plugin/
[102]: https://www.gnu.org/licenses/gpl-3.0.html
[3]: https://www.apache.org/licenses/LICENSE-2.0.txt
[69]: http://nexus.sonatype.org/oss-repository-hosting.html/scalatest-maven-plugin
[71]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[115]: http://maven.apache.org/plugins/maven-install-plugin/
[89]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[75]: https://maven.apache.org/plugins/maven-gpg-plugin/
[57]: https://testcontainers.org
[65]: http://github.com/davidB/scala-maven-plugin
[33]: http://spark.apache.org/
[81]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[97]: https://github.com/exasol/error-code-crawler-maven-plugin
[85]: https://maven.apache.org/plugins/maven-assembly-plugin/
