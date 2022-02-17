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
| [Protocol Buffers [Core]][25]              | [3-Clause BSD License][26]                    |
| [gcs-connector-hadoop3][27]                | [Apache License, Version 2.0][5]              |
| [ORC Core][29]                             | [Apache License, Version 2.0][3]              |
| [Apache Avro][31]                          | [Apache License, Version 2.0][3]              |
| [delta-core][33]                           | [Apache-2.0][34]                              |
| [Spark Project SQL][35]                    | [Apache 2.0 License][36]                      |
| [Parquet for Java][37]                     | [MIT][15]                                     |
| [JUL to SLF4J bridge][39]                  | [MIT License][40]                             |
| [SLF4J LOG4J-12 Binding relocated][39]     | [MIT License][40]                             |
| [Apache Log4j API][43]                     | [Apache License, Version 2.0][3]              |
| [Apache Log4j 1.x Compatibility API][45]   | [Apache License, Version 2.0][3]              |
| [scala-logging][47]                        | [Apache 2.0 License][36]                      |

## Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [scalatest][49]                            | [the Apache License, ASL Version 2.0][34] |
| [scalatestplus-mockito][51]                | [Apache-2.0][34]                          |
| [mockito-core][53]                         | [The MIT License][54]                     |
| [Hamcrest][55]                             | [BSD License 3][56]                       |
| [testcontainers-scala-scalatest][57]       | [The MIT License (MIT)][15]               |
| [Testcontainers :: Localstack][59]         | [MIT][60]                                 |
| [Test containers for Exasol on Docker][61] | [MIT][15]                                 |
| [Test Database Builder for Java][63]       | [MIT][15]                                 |
| [Matcher for SQL Result Sets][65]          | [MIT][15]                                 |

## Plugin Dependencies

| Dependency                                              | License                                       |
| ------------------------------------------------------- | --------------------------------------------- |
| [scala-maven-plugin][67]                                | [Public domain (Unlicense)][68]               |
| [Apache Maven Compiler Plugin][69]                      | [Apache License, Version 2.0][3]              |
| [ScalaTest Maven Plugin][71]                            | [the Apache License, ASL Version 2.0][34]     |
| [Apache Maven Enforcer Plugin][73]                      | [Apache License, Version 2.0][3]              |
| [Apache Maven GPG Plugin][75]                           | [Apache License, Version 2.0][3]              |
| [Apache Maven Source Plugin][77]                        | [Apache License, Version 2.0][3]              |
| [Apache Maven Javadoc Plugin][79]                       | [Apache License, Version 2.0][3]              |
| [Apache Maven JAR Plugin][81]                           | [Apache License, Version 2.0][3]              |
| [Apache Maven Assembly Plugin][83]                      | [Apache License, Version 2.0][3]              |
| [Versions Maven Plugin][85]                             | [Apache License, Version 2.0][3]              |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][87] | [ASL2][5]                                     |
| [Reproducible Build Maven Plugin][89]                   | [Apache 2.0][5]                               |
| [Project keeper maven plugin][91]                       | [MIT][15]                                     |
| [JaCoCo :: Maven Plugin][93]                            | [Eclipse Public License 2.0][94]              |
| [error-code-crawler-maven-plugin][95]                   | [MIT][15]                                     |
| [Artifact reference checker and unifier][97]            | [MIT][15]                                     |
| [OpenFastTrace Maven Plugin][99]                        | [GNU General Public License v3.0][100]        |
| [Scalastyle Maven Plugin][101]                          | [Apache 2.0][36]                              |
| [spotless-maven-plugin][103]                            | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][105]                            | [BSD-3-Clause][26]                            |
| [Apache Maven Clean Plugin][107]                        | [Apache License, Version 2.0][3]              |
| [Apache Maven Resources Plugin][109]                    | [Apache License, Version 2.0][3]              |
| [Maven Surefire Plugin][111]                            | [The Apache Software License, Version 2.0][5] |
| [Apache Maven Install Plugin][113]                      | [Apache License, Version 2.0][5]              |
| [Maven Deploy Plugin][115]                              | [The Apache Software License, Version 2.0][5] |
| [Apache Maven Site Plugin][117]                         | [Apache License, Version 2.0][3]              |

[91]: https://github.com/exasol/project-keeper-maven-plugin
[51]: https://github.com/scalatest/scalatestplus-mockito
[16]: https://github.com/exasol/error-reporting-java
[8]: http://wiki.fasterxml.com/JacksonModuleScala
[37]: https://github.com/exasol/parquet-io-java
[5]: http://www.apache.org/licenses/LICENSE-2.0.txt
[101]: http://www.scalastyle.org
[103]: https://github.com/diffplug/spotless
[12]: https://netty.io/netty-all/
[14]: https://github.com/exasol/import-export-udf-common-scala
[15]: https://opensource.org/licenses/MIT
[53]: https://github.com/mockito/mockito
[27]: https://github.com/GoogleCloudPlatform/BigData-interop/gcs-connector/
[11]: https://opensource.org/licenses/Apache-2.0
[33]: https://delta.io/
[85]: http://www.mojohaus.org/versions-maven-plugin/
[56]: http://opensource.org/licenses/BSD-3-Clause
[69]: https://maven.apache.org/plugins/maven-compiler-plugin/
[109]: https://maven.apache.org/plugins/maven-resources-plugin/
[99]: https://github.com/itsallcode/openfasttrace-maven-plugin
[107]: https://maven.apache.org/plugins/maven-clean-plugin/
[94]: https://www.eclipse.org/legal/epl-2.0/
[6]: http://github.com/FasterXML/jackson
[68]: http://unlicense.org/
[1]: https://www.apache.org/licenses/LICENSE-2.0
[93]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[54]: https://github.com/mockito/mockito/blob/main/LICENSE
[65]: https://github.com/exasol/hamcrest-resultset-matcher
[89]: http://zlika.github.io/reproducible-build-maven-plugin
[40]: http://www.opensource.org/licenses/mit-license.php
[111]: http://maven.apache.org/surefire/maven-surefire-plugin
[26]: https://opensource.org/licenses/BSD-3-Clause
[23]: https://www.alluxio.io/alluxio-core/alluxio-core-client/alluxio-core-client-hdfs/
[24]: https://github.com/alluxio/alluxio/blob/master/LICENSE
[31]: https://avro.apache.org
[47]: https://github.com/lightbend/scala-logging
[77]: https://maven.apache.org/plugins/maven-source-plugin/
[10]: https://github.com/grpc/grpc-java
[29]: https://orc.apache.org/orc-core
[55]: http://hamcrest.org/JavaHamcrest/
[39]: http://www.slf4j.org
[97]: https://github.com/exasol/artifact-reference-checker-maven-plugin
[105]: https://github.com/evis/scalafix-maven-plugin
[81]: https://maven.apache.org/plugins/maven-jar-plugin/
[34]: http://www.apache.org/licenses/LICENSE-2.0
[57]: https://github.com/testcontainers/testcontainers-scala
[4]: https://github.com/google/guava
[36]: http://www.apache.org/licenses/LICENSE-2.0.html
[49]: http://www.scalatest.org
[43]: https://logging.apache.org/log4j/2.x/log4j-api/
[45]: https://logging.apache.org/log4j/2.x/log4j-1.2-api/
[63]: https://github.com/exasol/test-db-builder-java
[2]: https://commons.apache.org/proper/commons-lang/
[60]: http://opensource.org/licenses/MIT
[0]: https://www.scala-lang.org/
[61]: https://github.com/exasol/exasol-testcontainers
[117]: https://maven.apache.org/plugins/maven-site-plugin/
[100]: https://www.gnu.org/licenses/gpl-3.0.html
[3]: https://www.apache.org/licenses/LICENSE-2.0.txt
[71]: http://nexus.sonatype.org/oss-repository-hosting.html/scalatest-maven-plugin
[73]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[113]: http://maven.apache.org/plugins/maven-install-plugin/
[87]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[75]: https://maven.apache.org/plugins/maven-gpg-plugin/
[59]: https://testcontainers.org
[67]: http://github.com/davidB/scala-maven-plugin
[35]: http://spark.apache.org/
[115]: http://maven.apache.org/plugins/maven-deploy-plugin/
[25]: https://developers.google.com/protocol-buffers/protobuf-java/
[79]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[95]: https://github.com/exasol/error-code-crawler-maven-plugin
[83]: https://maven.apache.org/plugins/maven-assembly-plugin/
