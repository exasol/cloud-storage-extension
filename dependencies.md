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
| Apache Hadoop Common                       | [Apache-2.0][3]                                                                                                                                                                                     |
| [dnsjava][16]                              | [BSD-3-Clause][17]                                                                                                                                                                                  |
| [JSch][18]                                 | [Revised BSD][19]; [Revised BSD][20]; [ISC][21]                                                                                                                                                     |
| Apache Hadoop Amazon Web Services support  | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache ZooKeeper - Server][22]            | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| Apache Hadoop Azure support                | [Apache-2.0][3]                                                                                                                                                                                     |
| Apache Hadoop Azure Data Lake support      | [Apache-2.0][3]                                                                                                                                                                                     |
| Apache Hadoop HDFS                         | [Apache-2.0][3]                                                                                                                                                                                     |
| Apache Hadoop HDFS Client                  | [Apache-2.0][3]                                                                                                                                                                                     |
| [Kotlin Stdlib][23]                        | [The Apache License, Version 2.0][6]                                                                                                                                                                |
| [Alluxio Core - Client - HDFS][24]         | [Apache License][25]                                                                                                                                                                                |
| [Metrics Core][26]                         | [Apache License 2.0][11]                                                                                                                                                                            |
| [Protocol Buffers [Core]][27]              | [BSD-3-Clause][17]                                                                                                                                                                                  |
| [gcs-connector][28]                        | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| [Google OAuth Client Library for Java][29] | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [ORC Core][30]                             | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| [aircompressor][31]                        | [Apache License 2.0][11]                                                                                                                                                                            |
| [Apache Avro][32]                          | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Commons Compress][33]              | [Apache-2.0][3]                                                                                                                                                                                     |
| [Nimbus JOSE+JWT][34]                      | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [delta-core][35]                           | [Apache-2.0][36]                                                                                                                                                                                    |
| [Spark Project SQL][37]                    | [Apache-2.0][38]                                                                                                                                                                                    |
| [Apache Ivy][39]                           | [The Apache Software License, Version 2.0][6]                                                                                                                                                       |
| [Parquet for Java][40]                     | [MIT License][41]                                                                                                                                                                                   |
| [JUL to SLF4J bridge][42]                  | [MIT License][43]                                                                                                                                                                                   |
| [Apache Log4j API][44]                     | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j 1.x Compatibility API][45]   | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j Core][46]                    | [Apache-2.0][3]                                                                                                                                                                                     |
| [scala-logging][47]                        | [Apache 2.0 License][38]                                                                                                                                                                            |
| [jersey-core-common][48]                   | [EPL 2.0][49]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][50]; [Apache License, 2.0][38]; [Public Domain][51]                                                      |
| [jersey-core-client][52]                   | [EPL 2.0][49]; [GPL2 w/ CPE][50]; [EDL 1.0][53]; [BSD 2-Clause][54]; [Apache License, 2.0][38]; [Public Domain][51]; [Modified BSD][55]; [jQuery license][56]; [MIT license][43]; [W3C license][57] |
| [jersey-core-server][58]                   | [EPL 2.0][49]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][50]; [Apache License, 2.0][38]; [Modified BSD][55]                                                       |
| [jersey-container-servlet][59]             | [EPL 2.0][49]; [GPL2 w/ CPE][50]; [EDL 1.0][53]; [BSD 2-Clause][54]; [Apache License, 2.0][38]; [Public Domain][51]; [Modified BSD][55]; [jQuery license][56]; [MIT license][43]; [W3C license][57] |
| [jersey-container-servlet-core][60]        | [EPL 2.0][49]; [GPL2 w/ CPE][50]; [EDL 1.0][53]; [BSD 2-Clause][54]; [Apache License, 2.0][38]; [Public Domain][51]; [Modified BSD][55]; [jQuery license][56]; [MIT license][43]; [W3C license][57] |
| [jersey-inject-hk2][61]                    | [EPL 2.0][49]; [GPL2 w/ CPE][50]; [EDL 1.0][53]; [BSD 2-Clause][54]; [Apache License, 2.0][38]; [Public Domain][51]; [Modified BSD][55]; [jQuery license][56]; [MIT license][43]; [W3C license][57] |

### Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [scalatest][62]                            | [the Apache License, ASL Version 2.0][36] |
| [scalatestplus-mockito][63]                | [Apache-2.0][36]                          |
| [mockito-core][64]                         | [MIT][65]                                 |
| [Hamcrest][66]                             | [BSD-3-Clause][67]                        |
| [testcontainers-scala-scalatest][68]       | [The MIT License (MIT)][65]               |
| [Testcontainers :: Localstack][69]         | [MIT][70]                                 |
| [Test containers for Exasol on Docker][71] | [MIT License][72]                         |
| [Test Database Builder for Java][73]       | [MIT License][74]                         |
| [Matcher for SQL Result Sets][75]          | [MIT License][76]                         |
| [EqualsVerifier \| release normal jar][77] | [Apache License, Version 2.0][3]          |
| [JUnit Jupiter Engine][78]                 | [Eclipse Public License v2.0][79]         |
| [Maven Project Version Getter][80]         | [MIT License][81]                         |
| [Extension integration tests library][82]  | [MIT License][83]                         |

### Runtime Dependencies

| Dependency                   | License                                                                       |
| ---------------------------- | ----------------------------------------------------------------------------- |
| [Logback Classic Module][84] | [Eclipse Public License - v 1.0][85]; [GNU Lesser General Public License][86] |
| [Logback Core Module][87]    | [Eclipse Public License - v 1.0][85]; [GNU Lesser General Public License][86] |

### Plugin Dependencies

| Dependency                                              | License                                       |
| ------------------------------------------------------- | --------------------------------------------- |
| [SonarQube Scanner for Maven][88]                       | [GNU LGPL 3][89]                              |
| [Apache Maven Toolchains Plugin][90]                    | [Apache-2.0][3]                               |
| [Apache Maven Compiler Plugin][91]                      | [Apache-2.0][3]                               |
| [Apache Maven Enforcer Plugin][92]                      | [Apache-2.0][3]                               |
| [Maven Flatten Plugin][93]                              | [Apache Software Licenese][3]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][94] | [ASL2][6]                                     |
| [scala-maven-plugin][95]                                | [Public domain (Unlicense)][96]               |
| [ScalaTest Maven Plugin][97]                            | [the Apache License, ASL Version 2.0][36]     |
| [Apache Maven Javadoc Plugin][98]                       | [Apache-2.0][3]                               |
| [Maven Surefire Plugin][99]                             | [Apache-2.0][3]                               |
| [Versions Maven Plugin][100]                            | [Apache License, Version 2.0][3]              |
| [duplicate-finder-maven-plugin Maven Mojo][101]         | [Apache License 2.0][38]                      |
| [Apache Maven Assembly Plugin][102]                     | [Apache-2.0][3]                               |
| [Apache Maven JAR Plugin][103]                          | [Apache-2.0][3]                               |
| [Artifact reference checker and unifier][104]           | [MIT License][105]                            |
| [Maven Failsafe Plugin][106]                            | [Apache-2.0][3]                               |
| [JaCoCo :: Maven Plugin][107]                           | [EPL-2.0][108]                                |
| [error-code-crawler-maven-plugin][109]                  | [MIT License][110]                            |
| [Reproducible Build Maven Plugin][111]                  | [Apache 2.0][6]                               |
| [Project Keeper Maven plugin][112]                      | [The MIT License][113]                        |
| [OpenFastTrace Maven Plugin][114]                       | [GNU General Public License v3.0][115]        |
| [Scalastyle Maven Plugin][116]                          | [Apache 2.0][38]                              |
| [spotless-maven-plugin][117]                            | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][118]                            | [BSD-3-Clause][17]                            |
| [Exec Maven Plugin][119]                                | [Apache License 2][3]                         |
| [Apache Maven Clean Plugin][120]                        | [Apache-2.0][3]                               |

## Extension

### Compile Dependencies

| Dependency                                 | License |
| ------------------------------------------ | ------- |
| [@exasol/extension-manager-interface][121] | MIT     |

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
[16]: https://github.com/dnsjava/dnsjava
[17]: https://opensource.org/licenses/BSD-3-Clause
[18]: https://github.com/mwiede/jsch
[19]: https://github.com/mwiede/jsch/blob/master/LICENSE.txt
[20]: https://github.com/mwiede/jsch/blob/master/LICENSE.JZlib.txt
[21]: https://github.com/mwiede/jsch/blob/master/LICENSE.jBCrypt.txt
[22]: http://zookeeper.apache.org/zookeeper
[23]: https://kotlinlang.org/
[24]: https://www.alluxio.io/alluxio-dora/alluxio-core/alluxio-core-client/alluxio-core-client-hdfs/
[25]: https://github.com/alluxio/alluxio/blob/master/LICENSE
[26]: https://metrics.dropwizard.io/metrics-core
[27]: https://developers.google.com/protocol-buffers/protobuf-java/
[28]: https://github.com/GoogleCloudDataproc/hadoop-connectors/gcs-connector
[29]: https://github.com/googleapis/google-oauth-java-client/google-oauth-client
[30]: https://orc.apache.org/orc-core
[31]: https://github.com/airlift/aircompressor
[32]: https://avro.apache.org
[33]: https://commons.apache.org/proper/commons-compress/
[34]: https://bitbucket.org/connect2id/nimbus-jose-jwt
[35]: https://delta.io/
[36]: http://www.apache.org/licenses/LICENSE-2.0
[37]: https://spark.apache.org/
[38]: http://www.apache.org/licenses/LICENSE-2.0.html
[39]: http://ant.apache.org/ivy/
[40]: https://github.com/exasol/parquet-io-java/
[41]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[42]: http://www.slf4j.org
[43]: http://www.opensource.org/licenses/mit-license.php
[44]: https://logging.apache.org/log4j/2.x/log4j/log4j-api/
[45]: https://logging.apache.org/log4j/2.x/log4j/log4j-1.2-api/
[46]: https://logging.apache.org/log4j/2.x/log4j/log4j-core/
[47]: https://github.com/lightbend/scala-logging
[48]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-common
[49]: http://www.eclipse.org/legal/epl-2.0
[50]: https://www.gnu.org/software/classpath/license.html
[51]: https://creativecommons.org/publicdomain/zero/1.0/
[52]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-client
[53]: http://www.eclipse.org/org/documents/edl-v10.php
[54]: https://opensource.org/licenses/BSD-2-Clause
[55]: https://asm.ow2.io/license.html
[56]: jquery.org/license
[57]: https://www.w3.org/Consortium/Legal/copyright-documents-19990405
[58]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-server
[59]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet
[60]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet-core
[61]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-hk2
[62]: http://www.scalatest.org
[63]: https://github.com/scalatest/scalatestplus-mockito
[64]: https://github.com/mockito/mockito
[65]: https://opensource.org/licenses/MIT
[66]: http://hamcrest.org/JavaHamcrest/
[67]: https://raw.githubusercontent.com/hamcrest/JavaHamcrest/master/LICENSE
[68]: https://github.com/testcontainers/testcontainers-scala
[69]: https://java.testcontainers.org
[70]: http://opensource.org/licenses/MIT
[71]: https://github.com/exasol/exasol-testcontainers/
[72]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[73]: https://github.com/exasol/test-db-builder-java/
[74]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[75]: https://github.com/exasol/hamcrest-resultset-matcher/
[76]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[77]: https://www.jqno.nl/equalsverifier
[78]: https://junit.org/junit5/
[79]: https://www.eclipse.org/legal/epl-v20.html
[80]: https://github.com/exasol/maven-project-version-getter/
[81]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[82]: https://github.com/exasol/extension-manager/
[83]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[84]: http://logback.qos.ch/logback-classic
[85]: http://www.eclipse.org/legal/epl-v10.html
[86]: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
[87]: http://logback.qos.ch/logback-core
[88]: http://sonarsource.github.io/sonar-scanner-maven/
[89]: http://www.gnu.org/licenses/lgpl.txt
[90]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[91]: https://maven.apache.org/plugins/maven-compiler-plugin/
[92]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[93]: https://www.mojohaus.org/flatten-maven-plugin/
[94]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[95]: http://github.com/davidB/scala-maven-plugin
[96]: http://unlicense.org/
[97]: https://www.scalatest.org/user_guide/using_the_scalatest_maven_plugin
[98]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[99]: https://maven.apache.org/surefire/maven-surefire-plugin/
[100]: https://www.mojohaus.org/versions/versions-maven-plugin/
[101]: https://basepom.github.io/duplicate-finder-maven-plugin
[102]: https://maven.apache.org/plugins/maven-assembly-plugin/
[103]: https://maven.apache.org/plugins/maven-jar-plugin/
[104]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[105]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[106]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[107]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[108]: https://www.eclipse.org/legal/epl-2.0/
[109]: https://github.com/exasol/error-code-crawler-maven-plugin/
[110]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[111]: http://zlika.github.io/reproducible-build-maven-plugin
[112]: https://github.com/exasol/project-keeper/
[113]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[114]: https://github.com/itsallcode/openfasttrace-maven-plugin
[115]: https://www.gnu.org/licenses/gpl-3.0.html
[116]: http://www.scalastyle.org
[117]: https://github.com/diffplug/spotless
[118]: https://github.com/evis/scalafix-maven-plugin
[119]: https://www.mojohaus.org/exec-maven-plugin
[120]: https://maven.apache.org/plugins/maven-clean-plugin/
[121]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.4.2.tgz
