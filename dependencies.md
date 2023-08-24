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
| Apache Hadoop Azure support                | [Apache License, Version 2.0][3]              |
| Apache Hadoop Azure Data Lake support      | [Apache License, Version 2.0][3]              |
| Apache Hadoop HDFS                         | [Apache License, Version 2.0][3]              |
| Apache Hadoop HDFS Client                  | [Apache License, Version 2.0][3]              |
| [Kotlin Stdlib][15]                        | [The Apache License, Version 2.0][5]          |
| [Alluxio Core - Client - HDFS][16]         | [Apache License][17]                          |
| [Metrics Core][18]                         | [Apache License 2.0][10]                      |
| [Protocol Buffers [Core]][19]              | [BSD-3-Clause][20]                            |
| [gcs-connector-hadoop3][21]                | [Apache License, Version 2.0][5]              |
| [Google OAuth Client Library for Java][22] | [The Apache Software License, Version 2.0][3] |
| [ORC Core][23]                             | [Apache License, Version 2.0][3]              |
| [Apache Avro][24]                          | [Apache-2.0][3]                               |
| [delta-core][25]                           | [Apache-2.0][26]                              |
| [Spark Project SQL][27]                    | [Apache 2.0 License][28]                      |
| [Apache Ivy][29]                           | [The Apache Software License, Version 2.0][5] |
| [Parquet for Java][30]                     | [MIT License][31]                             |
| [JUL to SLF4J bridge][32]                  | [MIT License][33]                             |
| [SLF4J Reload4j Binding][34]               | [MIT License][33]                             |
| [Apache Log4j API][35]                     | [Apache License, Version 2.0][3]              |
| [Apache Log4j 1.x Compatibility API][36]   | [Apache License, Version 2.0][3]              |
| [Apache Log4j Core][37]                    | [Apache License, Version 2.0][3]              |
| [scala-logging][38]                        | [Apache 2.0 License][28]                      |

### Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [scalatest][39]                            | [the Apache License, ASL Version 2.0][26] |
| [scalatestplus-mockito][40]                | [Apache-2.0][26]                          |
| [mockito-core][41]                         | [The MIT License][42]                     |
| [Hamcrest][43]                             | [BSD License 3][44]                       |
| [testcontainers-scala-scalatest][45]       | [The MIT License (MIT)][46]               |
| [Testcontainers :: Localstack][47]         | [MIT][48]                                 |
| [Test containers for Exasol on Docker][49] | [MIT License][50]                         |
| [Test Database Builder for Java][51]       | [MIT License][52]                         |
| [Matcher for SQL Result Sets][53]          | [MIT License][54]                         |
| [EqualsVerifier \| release normal jar][55] | [Apache License, Version 2.0][3]          |
| [JUnit Jupiter Engine][56]                 | [Eclipse Public License v2.0][57]         |
| [Maven Project Version Getter][58]         | [MIT License][59]                         |
| [Extension integration tests library][60]  | [MIT License][61]                         |

### Plugin Dependencies

| Dependency                                              | License                                       |
| ------------------------------------------------------- | --------------------------------------------- |
| [SonarQube Scanner for Maven][62]                       | [GNU LGPL 3][63]                              |
| [Apache Maven Compiler Plugin][64]                      | [Apache-2.0][3]                               |
| [Apache Maven Enforcer Plugin][65]                      | [Apache-2.0][3]                               |
| [Maven Flatten Plugin][66]                              | [Apache Software Licenese][3]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][67] | [ASL2][5]                                     |
| [scala-maven-plugin][68]                                | [Public domain (Unlicense)][69]               |
| [ScalaTest Maven Plugin][70]                            | [the Apache License, ASL Version 2.0][26]     |
| [Apache Maven Javadoc Plugin][71]                       | [Apache-2.0][3]                               |
| [Maven Surefire Plugin][72]                             | [Apache-2.0][3]                               |
| [Versions Maven Plugin][73]                             | [Apache License, Version 2.0][3]              |
| [duplicate-finder-maven-plugin Maven Mojo][74]          | [Apache License 2.0][28]                      |
| [Apache Maven Assembly Plugin][75]                      | [Apache-2.0][3]                               |
| [Apache Maven JAR Plugin][76]                           | [Apache License, Version 2.0][3]              |
| [Artifact reference checker and unifier][77]            | [MIT License][78]                             |
| [Maven Failsafe Plugin][79]                             | [Apache-2.0][3]                               |
| [JaCoCo :: Maven Plugin][80]                            | [Eclipse Public License 2.0][81]              |
| [error-code-crawler-maven-plugin][82]                   | [MIT License][83]                             |
| [Reproducible Build Maven Plugin][84]                   | [Apache 2.0][5]                               |
| [Project keeper maven plugin][85]                       | [The MIT License][86]                         |
| [OpenFastTrace Maven Plugin][87]                        | [GNU General Public License v3.0][88]         |
| [Scalastyle Maven Plugin][89]                           | [Apache 2.0][28]                              |
| [spotless-maven-plugin][90]                             | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][91]                             | [BSD-3-Clause][20]                            |
| [Exec Maven Plugin][92]                                 | [Apache License 2][3]                         |
| [Apache Maven Clean Plugin][93]                         | [Apache-2.0][3]                               |
| [Maven Resources Plugin][94]                            | [The Apache Software License, Version 2.0][5] |
| [Maven Install Plugin][95]                              | [The Apache Software License, Version 2.0][5] |
| [Maven Deploy Plugin][96]                               | [The Apache Software License, Version 2.0][5] |
| [Maven Site Plugin 3][97]                               | [The Apache Software License, Version 2.0][5] |

## Extension

### Compile Dependencies

| Dependency                                | License |
| ----------------------------------------- | ------- |
| [@exasol/extension-manager-interface][98] | MIT     |

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
[15]: https://kotlinlang.org/
[16]: https://www.alluxio.io/alluxio-dora/alluxio-core/alluxio-core-client/alluxio-core-client-hdfs/
[17]: https://github.com/alluxio/alluxio/blob/master/LICENSE
[18]: https://metrics.dropwizard.io/metrics-core
[19]: https://github.com/protocolbuffers/protobuf/tree/main/java
[20]: https://opensource.org/licenses/BSD-3-Clause
[21]: https://github.com/GoogleCloudDataproc/hadoop-connectors/tree/master/gcs
[22]: https://github.com/googleapis/google-oauth-java-client
[23]: https://orc.apache.org/
[24]: https://avro.apache.org
[25]: https://delta.io/
[26]: http://www.apache.org/licenses/LICENSE-2.0
[27]: https://spark.apache.org/
[28]: http://www.apache.org/licenses/LICENSE-2.0.html
[29]: http://ant.apache.org/ivy/
[30]: https://github.com/exasol/parquet-io-java/
[31]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[32]: http://www.slf4j.org
[33]: http://www.opensource.org/licenses/mit-license.php
[34]: http://reload4j.qos.ch
[35]: https://logging.apache.org/log4j/2.x/log4j-api/
[36]: https://logging.apache.org/log4j/2.x/
[37]: https://logging.apache.org/log4j/2.x/log4j-core/
[38]: https://github.com/lightbend/scala-logging
[39]: http://www.scalatest.org
[40]: https://github.com/scalatest/scalatestplus-mockito
[41]: https://github.com/mockito/mockito
[42]: https://github.com/mockito/mockito/blob/main/LICENSE
[43]: http://hamcrest.org/JavaHamcrest/
[44]: http://opensource.org/licenses/BSD-3-Clause
[45]: https://github.com/testcontainers/testcontainers-scala
[46]: https://opensource.org/licenses/MIT
[47]: https://java.testcontainers.org
[48]: http://opensource.org/licenses/MIT
[49]: https://github.com/exasol/exasol-testcontainers/
[50]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[51]: https://github.com/exasol/test-db-builder-java/
[52]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[53]: https://github.com/exasol/hamcrest-resultset-matcher/
[54]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[55]: https://www.jqno.nl/equalsverifier
[56]: https://junit.org/junit5/
[57]: https://www.eclipse.org/legal/epl-v20.html
[58]: https://github.com/exasol/maven-project-version-getter/
[59]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[60]: https://github.com/exasol/extension-manager/
[61]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[62]: http://sonarsource.github.io/sonar-scanner-maven/
[63]: http://www.gnu.org/licenses/lgpl.txt
[64]: https://maven.apache.org/plugins/maven-compiler-plugin/
[65]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[66]: https://www.mojohaus.org/flatten-maven-plugin/
[67]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[68]: http://github.com/davidB/scala-maven-plugin
[69]: http://unlicense.org/
[70]: https://www.scalatest.org/user_guide/using_the_scalatest_maven_plugin
[71]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[72]: https://maven.apache.org/surefire/maven-surefire-plugin/
[73]: https://www.mojohaus.org/versions/versions-maven-plugin/
[74]: https://basepom.github.io/duplicate-finder-maven-plugin
[75]: https://maven.apache.org/plugins/maven-assembly-plugin/
[76]: https://maven.apache.org/plugins/maven-jar-plugin/
[77]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[78]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[79]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[80]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[81]: https://www.eclipse.org/legal/epl-2.0/
[82]: https://github.com/exasol/error-code-crawler-maven-plugin/
[83]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[84]: http://zlika.github.io/reproducible-build-maven-plugin
[85]: https://github.com/exasol/project-keeper/
[86]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[87]: https://github.com/itsallcode/openfasttrace-maven-plugin
[88]: https://www.gnu.org/licenses/gpl-3.0.html
[89]: http://www.scalastyle.org
[90]: https://github.com/diffplug/spotless
[91]: https://github.com/evis/scalafix-maven-plugin
[92]: https://www.mojohaus.org/exec-maven-plugin
[93]: https://maven.apache.org/plugins/maven-clean-plugin/
[94]: http://maven.apache.org/plugins/maven-resources-plugin/
[95]: http://maven.apache.org/plugins/maven-install-plugin/
[96]: http://maven.apache.org/plugins/maven-deploy-plugin/
[97]: http://maven.apache.org/plugins/maven-site-plugin/
[98]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.3.1.tgz
