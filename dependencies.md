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
| [Netty/Transport][9]                       | [Apache License, Version 2.0][1]              |
| [snappy-java][10]                          | [Apache-2.0][11]                              |
| [Import Export UDF Common Scala][12]       | [MIT License][13]                             |
| [error-reporting-java][14]                 | [MIT License][15]                             |
| Apache Hadoop Common                       | [Apache License, Version 2.0][3]              |
| Apache Hadoop Amazon Web Services support  | [Apache License, Version 2.0][3]              |
| Apache Hadoop Azure support                | [Apache License, Version 2.0][3]              |
| Apache Hadoop Azure Data Lake support      | [Apache License, Version 2.0][3]              |
| Apache Hadoop HDFS                         | [Apache License, Version 2.0][3]              |
| Apache Hadoop HDFS Client                  | [Apache License, Version 2.0][3]              |
| [Kotlin Stdlib][16]                        | [The Apache License, Version 2.0][5]          |
| [Alluxio Core - Client - HDFS][17]         | [Apache License][18]                          |
| [Metrics Core][19]                         | [Apache License 2.0][11]                      |
| [Protocol Buffers [Core]][20]              | [BSD-3-Clause][21]                            |
| [gcs-connector-hadoop3][22]                | [Apache License, Version 2.0][5]              |
| [Google OAuth Client Library for Java][23] | [The Apache Software License, Version 2.0][3] |
| [ORC Core][24]                             | [Apache License, Version 2.0][3]              |
| [Apache Avro][25]                          | [Apache-2.0][3]                               |
| [delta-core][26]                           | [Apache-2.0][27]                              |
| [Spark Project SQL][28]                    | [Apache 2.0 License][29]                      |
| [Parquet for Java][30]                     | [MIT License][31]                             |
| [JUL to SLF4J bridge][32]                  | [MIT License][33]                             |
| [SLF4J LOG4J-12 Binding relocated][32]     | [MIT License][33]                             |
| [Apache Log4j API][34]                     | [Apache License, Version 2.0][3]              |
| [Apache Log4j 1.x Compatibility API][35]   | [Apache License, Version 2.0][3]              |
| [Apache Log4j Core][36]                    | [Apache License, Version 2.0][3]              |
| [scala-logging][37]                        | [Apache 2.0 License][29]                      |

### Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [scalatest][38]                            | [the Apache License, ASL Version 2.0][27] |
| [scalatestplus-mockito][39]                | [Apache-2.0][27]                          |
| [mockito-core][40]                         | [The MIT License][41]                     |
| [Hamcrest][42]                             | [BSD License 3][43]                       |
| [testcontainers-scala-scalatest][44]       | [The MIT License (MIT)][45]               |
| [Testcontainers :: Localstack][46]         | [MIT][47]                                 |
| [Test containers for Exasol on Docker][48] | [MIT License][49]                         |
| [Test Database Builder for Java][50]       | [MIT License][51]                         |
| [Matcher for SQL Result Sets][52]          | [MIT License][53]                         |
| [EqualsVerifier \| release normal jar][54] | [Apache License, Version 2.0][3]          |
| [JUnit Jupiter Engine][55]                 | [Eclipse Public License v2.0][56]         |
| [Maven Project Version Getter][57]         | [MIT License][58]                         |
| [Extension integration tests library][59]  | [MIT License][60]                         |

### Plugin Dependencies

| Dependency                                              | License                                       |
| ------------------------------------------------------- | --------------------------------------------- |
| [SonarQube Scanner for Maven][61]                       | [GNU LGPL 3][62]                              |
| [Apache Maven Compiler Plugin][63]                      | [Apache-2.0][3]                               |
| [Apache Maven Enforcer Plugin][64]                      | [Apache-2.0][3]                               |
| [Maven Flatten Plugin][65]                              | [Apache Software Licenese][3]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][66] | [ASL2][5]                                     |
| [scala-maven-plugin][67]                                | [Public domain (Unlicense)][68]               |
| [ScalaTest Maven Plugin][69]                            | [the Apache License, ASL Version 2.0][27]     |
| [Apache Maven Javadoc Plugin][70]                       | [Apache-2.0][3]                               |
| [Maven Surefire Plugin][71]                             | [Apache-2.0][3]                               |
| [Versions Maven Plugin][72]                             | [Apache License, Version 2.0][3]              |
| [duplicate-finder-maven-plugin Maven Mojo][73]          | [Apache License 2.0][29]                      |
| [Apache Maven Assembly Plugin][74]                      | [Apache-2.0][3]                               |
| [Apache Maven JAR Plugin][75]                           | [Apache License, Version 2.0][3]              |
| [Artifact reference checker and unifier][76]            | [MIT License][77]                             |
| [Maven Failsafe Plugin][78]                             | [Apache-2.0][3]                               |
| [JaCoCo :: Maven Plugin][79]                            | [Eclipse Public License 2.0][80]              |
| [error-code-crawler-maven-plugin][81]                   | [MIT License][82]                             |
| [Reproducible Build Maven Plugin][83]                   | [Apache 2.0][5]                               |
| [Project keeper maven plugin][84]                       | [The MIT License][85]                         |
| [OpenFastTrace Maven Plugin][86]                        | [GNU General Public License v3.0][87]         |
| [Scalastyle Maven Plugin][88]                           | [Apache 2.0][29]                              |
| [spotless-maven-plugin][89]                             | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][90]                             | [BSD-3-Clause][21]                            |
| [Exec Maven Plugin][91]                                 | [Apache License 2][3]                         |
| [Apache Maven Clean Plugin][92]                         | [Apache-2.0][3]                               |
| [Maven Resources Plugin][93]                            | [The Apache Software License, Version 2.0][5] |
| [Maven Install Plugin][94]                              | [The Apache Software License, Version 2.0][5] |
| [Maven Deploy Plugin][95]                               | [The Apache Software License, Version 2.0][5] |
| [Maven Site Plugin 3][96]                               | [The Apache Software License, Version 2.0][5] |

## Extension

### Compile Dependencies

| Dependency                                | License |
| ----------------------------------------- | ------- |
| [@exasol/extension-manager-interface][97] | MIT     |

[0]: https://www.scala-lang.org/
[1]: https://www.apache.org/licenses/LICENSE-2.0
[2]: https://commons.apache.org/proper/commons-lang/
[3]: https://www.apache.org/licenses/LICENSE-2.0.txt
[4]: https://github.com/google/guava
[5]: http://www.apache.org/licenses/LICENSE-2.0.txt
[6]: https://github.com/grpc/grpc-java
[7]: https://opensource.org/licenses/Apache-2.0
[8]: https://netty.io/netty-handler/
[9]: https://netty.io/netty-transport/
[10]: https://github.com/xerial/snappy-java
[11]: https://www.apache.org/licenses/LICENSE-2.0.html
[12]: https://github.com/exasol/import-export-udf-common-scala/
[13]: https://github.com/exasol/import-export-udf-common-scala/blob/main/LICENSE
[14]: https://github.com/exasol/error-reporting-java/
[15]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[16]: https://kotlinlang.org/
[17]: https://www.alluxio.io/alluxio-dora/alluxio-core/alluxio-core-client/alluxio-core-client-hdfs/
[18]: https://github.com/alluxio/alluxio/blob/master/LICENSE
[19]: https://metrics.dropwizard.io/metrics-core
[20]: https://github.com/protocolbuffers/protobuf/tree/main/java
[21]: https://opensource.org/licenses/BSD-3-Clause
[22]: https://github.com/GoogleCloudDataproc/hadoop-connectors/tree/master/gcs
[23]: https://github.com/googleapis/google-oauth-java-client
[24]: https://orc.apache.org/
[25]: https://avro.apache.org
[26]: https://delta.io/
[27]: http://www.apache.org/licenses/LICENSE-2.0
[28]: https://spark.apache.org/
[29]: http://www.apache.org/licenses/LICENSE-2.0.html
[30]: https://github.com/exasol/parquet-io-java/
[31]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[32]: http://www.slf4j.org
[33]: http://www.opensource.org/licenses/mit-license.php
[34]: https://logging.apache.org/log4j/2.x/log4j-api/
[35]: https://logging.apache.org/log4j/2.x/
[36]: https://logging.apache.org/log4j/2.x/log4j-core/
[37]: https://github.com/lightbend/scala-logging
[38]: http://www.scalatest.org
[39]: https://github.com/scalatest/scalatestplus-mockito
[40]: https://github.com/mockito/mockito
[41]: https://github.com/mockito/mockito/blob/main/LICENSE
[42]: http://hamcrest.org/JavaHamcrest/
[43]: http://opensource.org/licenses/BSD-3-Clause
[44]: https://github.com/testcontainers/testcontainers-scala
[45]: https://opensource.org/licenses/MIT
[46]: https://testcontainers.org
[47]: http://opensource.org/licenses/MIT
[48]: https://github.com/exasol/exasol-testcontainers/
[49]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[50]: https://github.com/exasol/test-db-builder-java/
[51]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[52]: https://github.com/exasol/hamcrest-resultset-matcher/
[53]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[54]: https://www.jqno.nl/equalsverifier
[55]: https://junit.org/junit5/
[56]: https://www.eclipse.org/legal/epl-v20.html
[57]: https://github.com/exasol/maven-project-version-getter/
[58]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[59]: https://github.com/exasol/extension-manager/
[60]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[61]: http://sonarsource.github.io/sonar-scanner-maven/
[62]: http://www.gnu.org/licenses/lgpl.txt
[63]: https://maven.apache.org/plugins/maven-compiler-plugin/
[64]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[65]: https://www.mojohaus.org/flatten-maven-plugin/
[66]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[67]: http://github.com/davidB/scala-maven-plugin
[68]: http://unlicense.org/
[69]: https://www.scalatest.org/user_guide/using_the_scalatest_maven_plugin
[70]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[71]: https://maven.apache.org/surefire/maven-surefire-plugin/
[72]: https://www.mojohaus.org/versions/versions-maven-plugin/
[73]: https://basepom.github.io/duplicate-finder-maven-plugin
[74]: https://maven.apache.org/plugins/maven-assembly-plugin/
[75]: https://maven.apache.org/plugins/maven-jar-plugin/
[76]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[77]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[78]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[79]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[80]: https://www.eclipse.org/legal/epl-2.0/
[81]: https://github.com/exasol/error-code-crawler-maven-plugin/
[82]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[83]: http://zlika.github.io/reproducible-build-maven-plugin
[84]: https://github.com/exasol/project-keeper/
[85]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[86]: https://github.com/itsallcode/openfasttrace-maven-plugin
[87]: https://www.gnu.org/licenses/gpl-3.0.html
[88]: http://www.scalastyle.org
[89]: https://github.com/diffplug/spotless
[90]: https://github.com/evis/scalafix-maven-plugin
[91]: https://www.mojohaus.org/exec-maven-plugin
[92]: https://maven.apache.org/plugins/maven-clean-plugin/
[93]: http://maven.apache.org/plugins/maven-resources-plugin/
[94]: http://maven.apache.org/plugins/maven-install-plugin/
[95]: http://maven.apache.org/plugins/maven-deploy-plugin/
[96]: http://maven.apache.org/plugins/maven-site-plugin/
[97]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.3.0.tgz
