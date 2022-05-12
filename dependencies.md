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
| [ORC Core][29]                             | [Apache License, Version 2.0][3]              |
| [Apache Avro][31]                          | [Apache License, Version 2.0][3]              |
| [delta-core][33]                           | [Apache-2.0][34]                              |
| [Data Mapper for Jackson][35]              | [The Apache Software License, Version 2.0][5] |
| [Spark Project SQL][37]                    | [Apache 2.0 License][38]                      |
| [Parquet for Java][39]                     | [MIT][15]                                     |
| [JUL to SLF4J bridge][41]                  | [MIT License][42]                             |
| [SLF4J LOG4J-12 Binding relocated][41]     | [MIT License][42]                             |
| [Apache Log4j API][45]                     | [Apache License, Version 2.0][3]              |
| [Apache Log4j 1.x Compatibility API][47]   | [Apache License, Version 2.0][3]              |
| [scala-logging][49]                        | [Apache 2.0 License][38]                      |

## Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [scalatest][51]                            | [the Apache License, ASL Version 2.0][34] |
| [scalatestplus-mockito][53]                | [Apache-2.0][34]                          |
| [mockito-core][55]                         | [The MIT License][56]                     |
| [Hamcrest][57]                             | [BSD License 3][58]                       |
| [testcontainers-scala-scalatest][59]       | [The MIT License (MIT)][15]               |
| [Testcontainers :: Localstack][61]         | [MIT][62]                                 |
| [Test containers for Exasol on Docker][63] | [MIT][15]                                 |
| [Test Database Builder for Java][65]       | [MIT License][66]                         |
| [Matcher for SQL Result Sets][67]          | [MIT][15]                                 |

## Plugin Dependencies

| Dependency                                              | License                                       |
| ------------------------------------------------------- | --------------------------------------------- |
| [scala-maven-plugin][69]                                | [Public domain (Unlicense)][70]               |
| [Apache Maven Compiler Plugin][71]                      | [Apache License, Version 2.0][3]              |
| [ScalaTest Maven Plugin][73]                            | [the Apache License, ASL Version 2.0][34]     |
| [Apache Maven Enforcer Plugin][75]                      | [Apache License, Version 2.0][3]              |
| [Apache Maven GPG Plugin][77]                           | [Apache License, Version 2.0][3]              |
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
| [Scalastyle Maven Plugin][103]                          | [Apache 2.0][38]                              |
| [spotless-maven-plugin][105]                            | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][107]                            | [BSD-3-Clause][26]                            |
| [Apache Maven Clean Plugin][109]                        | [Apache License, Version 2.0][3]              |
| [Apache Maven Resources Plugin][111]                    | [Apache License, Version 2.0][3]              |
| [Maven Surefire Plugin][113]                            | [The Apache Software License, Version 2.0][5] |
| [Apache Maven Install Plugin][115]                      | [Apache License, Version 2.0][5]              |
| [Maven Deploy Plugin][117]                              | [The Apache Software License, Version 2.0][5] |
| [Apache Maven Site Plugin][119]                         | [Apache License, Version 2.0][3]              |

[93]: https://github.com/exasol/project-keeper-maven-plugin
[53]: https://github.com/scalatest/scalatestplus-mockito
[16]: https://github.com/exasol/error-reporting-java
[8]: http://wiki.fasterxml.com/JacksonModuleScala
[39]: https://github.com/exasol/parquet-io-java
[5]: http://www.apache.org/licenses/LICENSE-2.0.txt
[103]: http://www.scalastyle.org
[105]: https://github.com/diffplug/spotless
[12]: https://netty.io/netty-all/
[14]: https://github.com/exasol/import-export-udf-common-scala
[15]: https://opensource.org/licenses/MIT
[55]: https://github.com/mockito/mockito
[27]: https://github.com/GoogleCloudPlatform/BigData-interop/gcs-connector/
[11]: https://opensource.org/licenses/Apache-2.0
[33]: https://delta.io/
[87]: http://www.mojohaus.org/versions-maven-plugin/
[58]: http://opensource.org/licenses/BSD-3-Clause
[71]: https://maven.apache.org/plugins/maven-compiler-plugin/
[111]: https://maven.apache.org/plugins/maven-resources-plugin/
[66]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[101]: https://github.com/itsallcode/openfasttrace-maven-plugin
[109]: https://maven.apache.org/plugins/maven-clean-plugin/
[96]: https://www.eclipse.org/legal/epl-2.0/
[6]: http://github.com/FasterXML/jackson
[70]: http://unlicense.org/
[1]: https://www.apache.org/licenses/LICENSE-2.0
[95]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[56]: https://github.com/mockito/mockito/blob/main/LICENSE
[67]: https://github.com/exasol/hamcrest-resultset-matcher
[91]: http://zlika.github.io/reproducible-build-maven-plugin
[42]: http://www.opensource.org/licenses/mit-license.php
[113]: http://maven.apache.org/surefire/maven-surefire-plugin
[26]: https://opensource.org/licenses/BSD-3-Clause
[23]: https://www.alluxio.io/alluxio-core/alluxio-core-client/alluxio-core-client-hdfs/
[24]: https://github.com/alluxio/alluxio/blob/master/LICENSE
[31]: https://avro.apache.org
[35]: http://jackson.codehaus.org
[49]: https://github.com/lightbend/scala-logging
[79]: https://maven.apache.org/plugins/maven-source-plugin/
[10]: https://github.com/grpc/grpc-java
[29]: https://orc.apache.org/orc-core
[57]: http://hamcrest.org/JavaHamcrest/
[41]: http://www.slf4j.org
[99]: https://github.com/exasol/artifact-reference-checker-maven-plugin
[107]: https://github.com/evis/scalafix-maven-plugin
[83]: https://maven.apache.org/plugins/maven-jar-plugin/
[34]: http://www.apache.org/licenses/LICENSE-2.0
[59]: https://github.com/testcontainers/testcontainers-scala
[65]: https://github.com/exasol/test-db-builder-java/
[4]: https://github.com/google/guava
[38]: http://www.apache.org/licenses/LICENSE-2.0.html
[51]: http://www.scalatest.org
[45]: https://logging.apache.org/log4j/2.x/log4j-api/
[47]: https://logging.apache.org/log4j/2.x/log4j-1.2-api/
[2]: https://commons.apache.org/proper/commons-lang/
[62]: http://opensource.org/licenses/MIT
[0]: https://www.scala-lang.org/
[63]: https://github.com/exasol/exasol-testcontainers
[119]: https://maven.apache.org/plugins/maven-site-plugin/
[102]: https://www.gnu.org/licenses/gpl-3.0.html
[3]: https://www.apache.org/licenses/LICENSE-2.0.txt
[73]: http://nexus.sonatype.org/oss-repository-hosting.html/scalatest-maven-plugin
[75]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[115]: http://maven.apache.org/plugins/maven-install-plugin/
[89]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[77]: https://maven.apache.org/plugins/maven-gpg-plugin/
[61]: https://testcontainers.org
[69]: http://github.com/davidB/scala-maven-plugin
[37]: http://spark.apache.org/
[117]: http://maven.apache.org/plugins/maven-deploy-plugin/
[25]: https://developers.google.com/protocol-buffers/protobuf-java/
[81]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[97]: https://github.com/exasol/error-code-crawler-maven-plugin
[85]: https://maven.apache.org/plugins/maven-assembly-plugin/
