<!-- @formatter:off -->
# Dependencies

## Cloud Storage Extension

### Compile Dependencies

| Dependency                                 | License                                                                                                                                                                                             |
| ------------------------------------------ | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| [Scala Library][0]                         | [Apache-2.0][1]                                                                                                                                                                                     |
| [Apache Commons Lang][2]                   | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Commons Configuration][4]          | [Apache-2.0][3]                                                                                                                                                                                     |
| [Guava: Google Core Libraries for Java][5] | [Apache License, Version 2.0][6]                                                                                                                                                                    |
| [io.grpc:grpc-netty][7]                    | [Apache 2.0][8]                                                                                                                                                                                     |
| [Netty/Codec/HTTP2][9]                     | [Apache License, Version 2.0][1]                                                                                                                                                                    |
| [snappy-java][10]                          | [Apache-2.0][11]                                                                                                                                                                                    |
| [Import Export UDF Common Scala][12]       | [MIT License][13]                                                                                                                                                                                   |
| [error-reporting-java][14]                 | [MIT License][15]                                                                                                                                                                                   |
| Apache Hadoop Common                       | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| Apache Hadoop Amazon Web Services support  | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| [Apache ZooKeeper - Server][16]            | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| Apache Hadoop Azure support                | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| Apache Hadoop Azure Data Lake support      | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| Apache Hadoop HDFS                         | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| Apache Hadoop HDFS Client                  | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| [Kotlin Stdlib][17]                        | [The Apache License, Version 2.0][6]                                                                                                                                                                |
| [Alluxio Core - Client - HDFS][18]         | [Apache License][19]                                                                                                                                                                                |
| [Metrics Core][20]                         | [Apache License 2.0][11]                                                                                                                                                                            |
| [Protocol Buffers [Core]][21]              | [BSD-3-Clause][22]                                                                                                                                                                                  |
| [gcs-connector-hadoop3][23]                | [Apache License, Version 2.0][6]                                                                                                                                                                    |
| [Google OAuth Client Library for Java][24] | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [ORC Core][25]                             | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| [Apache Avro][26]                          | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Commons Compress][27]              | [Apache-2.0][3]                                                                                                                                                                                     |
| [Nimbus JOSE+JWT][28]                      | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [delta-core][29]                           | [Apache-2.0][30]                                                                                                                                                                                    |
| [Spark Project SQL][31]                    | [Apache 2.0 License][32]                                                                                                                                                                            |
| [Apache Ivy][33]                           | [The Apache Software License, Version 2.0][6]                                                                                                                                                       |
| [Parquet for Java][34]                     | [MIT License][35]                                                                                                                                                                                   |
| [JUL to SLF4J bridge][36]                  | [MIT License][37]                                                                                                                                                                                   |
| [Apache Log4j API][38]                     | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j 1.x Compatibility API][39]   | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j Core][40]                    | [Apache-2.0][3]                                                                                                                                                                                     |
| [scala-logging][41]                        | [Apache 2.0 License][32]                                                                                                                                                                            |
| [jersey-core-common][42]                   | [EPL 2.0][43]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][44]; [Apache License, 2.0][32]; [Public Domain][45]                                                      |
| [jersey-core-client][46]                   | [EPL 2.0][43]; [GPL2 w/ CPE][44]; [EDL 1.0][47]; [BSD 2-Clause][48]; [Apache License, 2.0][32]; [Public Domain][45]; [Modified BSD][49]; [jQuery license][50]; [MIT license][37]; [W3C license][51] |
| [jersey-core-server][52]                   | [EPL 2.0][43]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][44]; [Apache License, 2.0][32]; [Modified BSD][49]                                                       |
| [jersey-container-servlet][53]             | [EPL 2.0][43]; [GPL2 w/ CPE][44]; [EDL 1.0][47]; [BSD 2-Clause][48]; [Apache License, 2.0][32]; [Public Domain][45]; [Modified BSD][49]; [jQuery license][50]; [MIT license][37]; [W3C license][51] |
| [jersey-container-servlet-core][54]        | [EPL 2.0][43]; [GPL2 w/ CPE][44]; [EDL 1.0][47]; [BSD 2-Clause][48]; [Apache License, 2.0][32]; [Public Domain][45]; [Modified BSD][49]; [jQuery license][50]; [MIT license][37]; [W3C license][51] |
| [jersey-inject-hk2][55]                    | [EPL 2.0][43]; [GPL2 w/ CPE][44]; [EDL 1.0][47]; [BSD 2-Clause][48]; [Apache License, 2.0][32]; [Public Domain][45]; [Modified BSD][49]; [jQuery license][50]; [MIT license][37]; [W3C license][51] |

### Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [scalatest][56]                            | [the Apache License, ASL Version 2.0][30] |
| [scalatestplus-mockito][57]                | [Apache-2.0][30]                          |
| [mockito-core][58]                         | [MIT][59]                                 |
| [Hamcrest][60]                             | [BSD License 3][61]                       |
| [testcontainers-scala-scalatest][62]       | [The MIT License (MIT)][59]               |
| [Testcontainers :: Localstack][63]         | [MIT][64]                                 |
| [Test containers for Exasol on Docker][65] | [MIT License][66]                         |
| [Test Database Builder for Java][67]       | [MIT License][68]                         |
| [Matcher for SQL Result Sets][69]          | [MIT License][70]                         |
| [EqualsVerifier \| release normal jar][71] | [Apache License, Version 2.0][3]          |
| [JUnit Jupiter Engine][72]                 | [Eclipse Public License v2.0][73]         |
| [Maven Project Version Getter][74]         | [MIT License][75]                         |
| [Extension integration tests library][76]  | [MIT License][77]                         |

### Runtime Dependencies

| Dependency                   | License                                                                       |
| ---------------------------- | ----------------------------------------------------------------------------- |
| [Logback Classic Module][78] | [Eclipse Public License - v 1.0][79]; [GNU Lesser General Public License][80] |
| [Logback Core Module][81]    | [Eclipse Public License - v 1.0][79]; [GNU Lesser General Public License][80] |

### Plugin Dependencies

| Dependency                                              | License                                       |
| ------------------------------------------------------- | --------------------------------------------- |
| [SonarQube Scanner for Maven][82]                       | [GNU LGPL 3][83]                              |
| [Apache Maven Toolchains Plugin][84]                    | [Apache-2.0][3]                               |
| [Apache Maven Compiler Plugin][85]                      | [Apache-2.0][3]                               |
| [Apache Maven Enforcer Plugin][86]                      | [Apache-2.0][3]                               |
| [Maven Flatten Plugin][87]                              | [Apache Software Licenese][3]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][88] | [ASL2][6]                                     |
| [scala-maven-plugin][89]                                | [Public domain (Unlicense)][90]               |
| [ScalaTest Maven Plugin][91]                            | [the Apache License, ASL Version 2.0][30]     |
| [Apache Maven Javadoc Plugin][92]                       | [Apache-2.0][3]                               |
| [Maven Surefire Plugin][93]                             | [Apache-2.0][3]                               |
| [Versions Maven Plugin][94]                             | [Apache License, Version 2.0][3]              |
| [duplicate-finder-maven-plugin Maven Mojo][95]          | [Apache License 2.0][32]                      |
| [Apache Maven Assembly Plugin][96]                      | [Apache-2.0][3]                               |
| [Apache Maven JAR Plugin][97]                           | [Apache-2.0][3]                               |
| [Artifact reference checker and unifier][98]            | [MIT License][99]                             |
| [Maven Failsafe Plugin][100]                            | [Apache-2.0][3]                               |
| [JaCoCo :: Maven Plugin][101]                           | [EPL-2.0][102]                                |
| [error-code-crawler-maven-plugin][103]                  | [MIT License][104]                            |
| [Reproducible Build Maven Plugin][105]                  | [Apache 2.0][6]                               |
| [Project Keeper Maven plugin][106]                      | [The MIT License][107]                        |
| [OpenFastTrace Maven Plugin][108]                       | [GNU General Public License v3.0][109]        |
| [Scalastyle Maven Plugin][110]                          | [Apache 2.0][32]                              |
| [spotless-maven-plugin][111]                            | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][112]                            | [BSD-3-Clause][22]                            |
| [Exec Maven Plugin][113]                                | [Apache License 2][3]                         |
| [Apache Maven Clean Plugin][114]                        | [Apache-2.0][3]                               |

## Extension

### Compile Dependencies

| Dependency                                 | License |
| ------------------------------------------ | ------- |
| [@exasol/extension-manager-interface][115] | MIT     |

[0]: https://www.scala-lang.org/
[1]: https://www.apache.org/licenses/LICENSE-2.0
[2]: https://commons.apache.org/proper/commons-lang/
[3]: https://www.apache.org/licenses/LICENSE-2.0.txt
[4]: https://commons.apache.org/proper/commons-configuration/
[5]: https://github.com/google/guava
[6]: http://www.apache.org/licenses/LICENSE-2.0.txt
[7]: https://github.com/grpc/grpc-java
[8]: https://opensource.org/licenses/Apache-2.0
[9]: https://netty.io/netty-codec-http2/
[10]: https://github.com/xerial/snappy-java
[11]: https://www.apache.org/licenses/LICENSE-2.0.html
[12]: https://github.com/exasol/import-export-udf-common-scala/
[13]: https://github.com/exasol/import-export-udf-common-scala/blob/main/LICENSE
[14]: https://github.com/exasol/error-reporting-java/
[15]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[16]: http://zookeeper.apache.org/zookeeper
[17]: https://kotlinlang.org/
[18]: https://www.alluxio.io/alluxio-dora/alluxio-core/alluxio-core-client/alluxio-core-client-hdfs/
[19]: https://github.com/alluxio/alluxio/blob/master/LICENSE
[20]: https://metrics.dropwizard.io/metrics-core
[21]: https://developers.google.com/protocol-buffers/protobuf-java/
[22]: https://opensource.org/licenses/BSD-3-Clause
[23]: https://github.com/GoogleCloudPlatform/BigData-interop/gcs-connector/
[24]: https://github.com/googleapis/google-oauth-java-client/google-oauth-client
[25]: https://orc.apache.org/orc-core
[26]: https://avro.apache.org
[27]: https://commons.apache.org/proper/commons-compress/
[28]: https://bitbucket.org/connect2id/nimbus-jose-jwt
[29]: https://delta.io/
[30]: http://www.apache.org/licenses/LICENSE-2.0
[31]: https://spark.apache.org/
[32]: http://www.apache.org/licenses/LICENSE-2.0.html
[33]: http://ant.apache.org/ivy/
[34]: https://github.com/exasol/parquet-io-java/
[35]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[36]: http://www.slf4j.org
[37]: http://www.opensource.org/licenses/mit-license.php
[38]: https://logging.apache.org/log4j/2.x/log4j/log4j-api/
[39]: https://logging.apache.org/log4j/2.x/log4j/log4j-1.2-api/
[40]: https://logging.apache.org/log4j/2.x/log4j/log4j-core/
[41]: https://github.com/lightbend/scala-logging
[42]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-common
[43]: http://www.eclipse.org/legal/epl-2.0
[44]: https://www.gnu.org/software/classpath/license.html
[45]: https://creativecommons.org/publicdomain/zero/1.0/
[46]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-client
[47]: http://www.eclipse.org/org/documents/edl-v10.php
[48]: https://opensource.org/licenses/BSD-2-Clause
[49]: https://asm.ow2.io/license.html
[50]: jquery.org/license
[51]: https://www.w3.org/Consortium/Legal/copyright-documents-19990405
[52]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-server
[53]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet
[54]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet-core
[55]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-hk2
[56]: http://www.scalatest.org
[57]: https://github.com/scalatest/scalatestplus-mockito
[58]: https://github.com/mockito/mockito
[59]: https://opensource.org/licenses/MIT
[60]: http://hamcrest.org/JavaHamcrest/
[61]: http://opensource.org/licenses/BSD-3-Clause
[62]: https://github.com/testcontainers/testcontainers-scala
[63]: https://java.testcontainers.org
[64]: http://opensource.org/licenses/MIT
[65]: https://github.com/exasol/exasol-testcontainers/
[66]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[67]: https://github.com/exasol/test-db-builder-java/
[68]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[69]: https://github.com/exasol/hamcrest-resultset-matcher/
[70]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[71]: https://www.jqno.nl/equalsverifier
[72]: https://junit.org/junit5/
[73]: https://www.eclipse.org/legal/epl-v20.html
[74]: https://github.com/exasol/maven-project-version-getter/
[75]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[76]: https://github.com/exasol/extension-manager/
[77]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[78]: http://logback.qos.ch/logback-classic
[79]: http://www.eclipse.org/legal/epl-v10.html
[80]: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
[81]: http://logback.qos.ch/logback-core
[82]: http://sonarsource.github.io/sonar-scanner-maven/
[83]: http://www.gnu.org/licenses/lgpl.txt
[84]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[85]: https://maven.apache.org/plugins/maven-compiler-plugin/
[86]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[87]: https://www.mojohaus.org/flatten-maven-plugin/
[88]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[89]: http://github.com/davidB/scala-maven-plugin
[90]: http://unlicense.org/
[91]: https://www.scalatest.org/user_guide/using_the_scalatest_maven_plugin
[92]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[93]: https://maven.apache.org/surefire/maven-surefire-plugin/
[94]: https://www.mojohaus.org/versions/versions-maven-plugin/
[95]: https://basepom.github.io/duplicate-finder-maven-plugin
[96]: https://maven.apache.org/plugins/maven-assembly-plugin/
[97]: https://maven.apache.org/plugins/maven-jar-plugin/
[98]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[99]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[100]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[101]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[102]: https://www.eclipse.org/legal/epl-2.0/
[103]: https://github.com/exasol/error-code-crawler-maven-plugin/
[104]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[105]: http://zlika.github.io/reproducible-build-maven-plugin
[106]: https://github.com/exasol/project-keeper/
[107]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[108]: https://github.com/itsallcode/openfasttrace-maven-plugin
[109]: https://www.gnu.org/licenses/gpl-3.0.html
[110]: http://www.scalastyle.org
[111]: https://github.com/diffplug/spotless
[112]: https://github.com/evis/scalafix-maven-plugin
[113]: https://www.mojohaus.org/exec-maven-plugin
[114]: https://maven.apache.org/plugins/maven-clean-plugin/
[115]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.4.2.tgz
