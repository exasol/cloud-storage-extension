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
| [gcs-connector-hadoop3][28]                | [Apache License, Version 2.0][6]                                                                                                                                                                    |
| [Google OAuth Client Library for Java][29] | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [ORC Core][30]                             | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| [aircompressor][31]                        | [Apache License 2.0][11]                                                                                                                                                                            |
| [Apache Avro][32]                          | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Commons Compress][33]              | [Apache-2.0][3]                                                                                                                                                                                     |
| [Nimbus JOSE+JWT][34]                      | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [delta-core][35]                           | [Apache-2.0][36]                                                                                                                                                                                    |
| [Spark Project SQL][37]                    | [Apache 2.0 License][38]                                                                                                                                                                            |
| [Apache Ivy][39]                           | [The Apache Software License, Version 2.0][6]                                                                                                                                                       |
| [janino][40]                               | [BSD-3-Clause][41]                                                                                                                                                                                  |
| [Parquet for Java][42]                     | [MIT License][43]                                                                                                                                                                                   |
| [JUL to SLF4J bridge][44]                  | [MIT License][45]                                                                                                                                                                                   |
| [Apache Log4j API][46]                     | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j 1.x Compatibility API][47]   | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j Core][48]                    | [Apache-2.0][3]                                                                                                                                                                                     |
| [scala-logging][49]                        | [Apache 2.0 License][38]                                                                                                                                                                            |
| [jersey-core-common][50]                   | [EPL 2.0][51]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][52]; [Apache License, 2.0][38]; [Public Domain][53]                                                      |
| [jersey-core-client][54]                   | [EPL 2.0][51]; [GPL2 w/ CPE][52]; [EDL 1.0][55]; [BSD 2-Clause][56]; [Apache License, 2.0][38]; [Public Domain][53]; [Modified BSD][57]; [jQuery license][58]; [MIT license][45]; [W3C license][59] |
| [jersey-core-server][60]                   | [EPL 2.0][51]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][52]; [Apache License, 2.0][38]; [Modified BSD][57]                                                       |
| [jersey-container-servlet][61]             | [EPL 2.0][51]; [GPL2 w/ CPE][52]; [EDL 1.0][55]; [BSD 2-Clause][56]; [Apache License, 2.0][38]; [Public Domain][53]; [Modified BSD][57]; [jQuery license][58]; [MIT license][45]; [W3C license][59] |
| [jersey-container-servlet-core][62]        | [EPL 2.0][51]; [GPL2 w/ CPE][52]; [EDL 1.0][55]; [BSD 2-Clause][56]; [Apache License, 2.0][38]; [Public Domain][53]; [Modified BSD][57]; [jQuery license][58]; [MIT license][45]; [W3C license][59] |
| [jersey-inject-hk2][63]                    | [EPL 2.0][51]; [GPL2 w/ CPE][52]; [EDL 1.0][55]; [BSD 2-Clause][56]; [Apache License, 2.0][38]; [Public Domain][53]; [Modified BSD][57]; [jQuery license][58]; [MIT license][45]; [W3C license][59] |

### Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [scalatest][64]                            | [the Apache License, ASL Version 2.0][36] |
| [scalatestplus-mockito][65]                | [Apache-2.0][36]                          |
| [mockito-core][66]                         | [MIT][67]                                 |
| [Hamcrest][68]                             | [BSD License 3][69]                       |
| [testcontainers-scala-scalatest][70]       | [The MIT License (MIT)][67]               |
| [Testcontainers :: Localstack][71]         | [MIT][72]                                 |
| [Test containers for Exasol on Docker][73] | [MIT License][74]                         |
| [Test Database Builder for Java][75]       | [MIT License][76]                         |
| [Matcher for SQL Result Sets][77]          | [MIT License][78]                         |
| [EqualsVerifier \| release normal jar][79] | [Apache License, Version 2.0][3]          |
| [JUnit Jupiter Engine][80]                 | [Eclipse Public License v2.0][81]         |
| [Maven Project Version Getter][82]         | [MIT License][83]                         |
| [Extension integration tests library][84]  | [MIT License][85]                         |

### Runtime Dependencies

| Dependency                   | License                                                                       |
| ---------------------------- | ----------------------------------------------------------------------------- |
| [Logback Classic Module][86] | [Eclipse Public License - v 1.0][87]; [GNU Lesser General Public License][88] |
| [Logback Core Module][89]    | [Eclipse Public License - v 1.0][87]; [GNU Lesser General Public License][88] |

### Plugin Dependencies

| Dependency                                               | License                                       |
| -------------------------------------------------------- | --------------------------------------------- |
| [Project Keeper Maven plugin][90]                        | [The MIT License][91]                         |
| [OpenFastTrace Maven Plugin][92]                         | [GNU General Public License v3.0][93]         |
| [Scalastyle Maven Plugin][94]                            | [Apache 2.0][38]                              |
| [spotless-maven-plugin][95]                              | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][96]                              | [BSD-3-Clause][17]                            |
| [Exec Maven Plugin][97]                                  | [Apache License 2][3]                         |
| [Apache Maven Clean Plugin][98]                          | [Apache-2.0][3]                               |
| [Apache Maven Install Plugin][99]                        | [Apache-2.0][3]                               |
| [Apache Maven Resources Plugin][100]                     | [Apache-2.0][3]                               |
| [Apache Maven Site Plugin][101]                          | [Apache License, Version 2.0][3]              |
| [SonarQube Scanner for Maven][102]                       | [GNU LGPL 3][103]                             |
| [Apache Maven Toolchains Plugin][104]                    | [Apache-2.0][3]                               |
| [Apache Maven Compiler Plugin][105]                      | [Apache-2.0][3]                               |
| [Apache Maven Enforcer Plugin][106]                      | [Apache-2.0][3]                               |
| [Maven Flatten Plugin][107]                              | [Apache Software Licenese][3]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][108] | [ASL2][6]                                     |
| [scala-maven-plugin][109]                                | [Public domain (Unlicense)][110]              |
| [ScalaTest Maven Plugin][111]                            | [the Apache License, ASL Version 2.0][36]     |
| [Apache Maven Javadoc Plugin][112]                       | [Apache-2.0][3]                               |
| [Maven Surefire Plugin][113]                             | [Apache-2.0][3]                               |
| [Versions Maven Plugin][114]                             | [Apache License, Version 2.0][3]              |
| [duplicate-finder-maven-plugin Maven Mojo][115]          | [Apache License 2.0][38]                      |
| [Apache Maven Assembly Plugin][116]                      | [Apache-2.0][3]                               |
| [Apache Maven JAR Plugin][117]                           | [Apache-2.0][3]                               |
| [Artifact reference checker and unifier][118]            | [MIT License][119]                            |
| [Maven Failsafe Plugin][120]                             | [Apache-2.0][3]                               |
| [JaCoCo :: Maven Plugin][121]                            | [EPL-2.0][122]                                |
| [Quality Summarizer Maven Plugin][123]                   | [MIT License][124]                            |
| [error-code-crawler-maven-plugin][125]                   | [MIT License][126]                            |
| [Reproducible Build Maven Plugin][127]                   | [Apache 2.0][6]                               |

## Extension

### Compile Dependencies

| Dependency                                 | License |
| ------------------------------------------ | ------- |
| [@exasol/extension-manager-interface][128] | MIT     |

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
[28]: https://github.com/GoogleCloudPlatform/BigData-interop/gcs-connector/
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
[40]: http://janino-compiler.github.io/janino/
[41]: https://spdx.org/licenses/BSD-3-Clause.html
[42]: https://github.com/exasol/parquet-io-java/
[43]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[44]: http://www.slf4j.org
[45]: http://www.opensource.org/licenses/mit-license.php
[46]: https://logging.apache.org/log4j/2.x/log4j/log4j-api/
[47]: https://logging.apache.org/log4j/2.x/log4j/log4j-1.2-api/
[48]: https://logging.apache.org/log4j/2.x/log4j/log4j-core/
[49]: https://github.com/lightbend/scala-logging
[50]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-common
[51]: http://www.eclipse.org/legal/epl-2.0
[52]: https://www.gnu.org/software/classpath/license.html
[53]: https://creativecommons.org/publicdomain/zero/1.0/
[54]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-client
[55]: http://www.eclipse.org/org/documents/edl-v10.php
[56]: https://opensource.org/licenses/BSD-2-Clause
[57]: https://asm.ow2.io/license.html
[58]: jquery.org/license
[59]: https://www.w3.org/Consortium/Legal/copyright-documents-19990405
[60]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-server
[61]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet
[62]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet-core
[63]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-hk2
[64]: http://www.scalatest.org
[65]: https://github.com/scalatest/scalatestplus-mockito
[66]: https://github.com/mockito/mockito
[67]: https://opensource.org/licenses/MIT
[68]: http://hamcrest.org/JavaHamcrest/
[69]: http://opensource.org/licenses/BSD-3-Clause
[70]: https://github.com/testcontainers/testcontainers-scala
[71]: https://java.testcontainers.org
[72]: http://opensource.org/licenses/MIT
[73]: https://github.com/exasol/exasol-testcontainers/
[74]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[75]: https://github.com/exasol/test-db-builder-java/
[76]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[77]: https://github.com/exasol/hamcrest-resultset-matcher/
[78]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[79]: https://www.jqno.nl/equalsverifier
[80]: https://junit.org/junit5/
[81]: https://www.eclipse.org/legal/epl-v20.html
[82]: https://github.com/exasol/maven-project-version-getter/
[83]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[84]: https://github.com/exasol/extension-manager/
[85]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[86]: http://logback.qos.ch/logback-classic
[87]: http://www.eclipse.org/legal/epl-v10.html
[88]: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
[89]: http://logback.qos.ch/logback-core
[90]: https://github.com/exasol/project-keeper/
[91]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[92]: https://github.com/itsallcode/openfasttrace-maven-plugin
[93]: https://www.gnu.org/licenses/gpl-3.0.html
[94]: http://www.scalastyle.org
[95]: https://github.com/diffplug/spotless
[96]: https://github.com/evis/scalafix-maven-plugin
[97]: https://www.mojohaus.org/exec-maven-plugin
[98]: https://maven.apache.org/plugins/maven-clean-plugin/
[99]: https://maven.apache.org/plugins/maven-install-plugin/
[100]: https://maven.apache.org/plugins/maven-resources-plugin/
[101]: https://maven.apache.org/plugins/maven-site-plugin/
[102]: http://sonarsource.github.io/sonar-scanner-maven/
[103]: http://www.gnu.org/licenses/lgpl.txt
[104]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[105]: https://maven.apache.org/plugins/maven-compiler-plugin/
[106]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[107]: https://www.mojohaus.org/flatten-maven-plugin/
[108]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[109]: http://github.com/davidB/scala-maven-plugin
[110]: http://unlicense.org/
[111]: https://www.scalatest.org/user_guide/using_the_scalatest_maven_plugin
[112]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[113]: https://maven.apache.org/surefire/maven-surefire-plugin/
[114]: https://www.mojohaus.org/versions/versions-maven-plugin/
[115]: https://basepom.github.io/duplicate-finder-maven-plugin
[116]: https://maven.apache.org/plugins/maven-assembly-plugin/
[117]: https://maven.apache.org/plugins/maven-jar-plugin/
[118]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[119]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[120]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[121]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[122]: https://www.eclipse.org/legal/epl-2.0/
[123]: https://github.com/exasol/quality-summarizer-maven-plugin/
[124]: https://github.com/exasol/quality-summarizer-maven-plugin/blob/main/LICENSE
[125]: https://github.com/exasol/error-code-crawler-maven-plugin/
[126]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[127]: http://zlika.github.io/reproducible-build-maven-plugin
[128]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.4.2.tgz
