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
| [Apache Commons IO][16]                    | [Apache-2.0][3]                                                                                                                                                                                     |
| [dnsjava][17]                              | [BSD-3-Clause][18]                                                                                                                                                                                  |
| [JSch][19]                                 | [Revised BSD][20]; [Revised BSD][21]; [ISC][22]                                                                                                                                                     |
| Apache Hadoop Amazon Web Services support  | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| [Apache ZooKeeper - Server][23]            | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| Apache Hadoop Azure support                | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| Apache Hadoop Azure Data Lake support      | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| Apache Hadoop HDFS                         | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| Apache Hadoop HDFS Client                  | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| [Kotlin Stdlib][24]                        | [The Apache License, Version 2.0][6]                                                                                                                                                                |
| [Alluxio Core - Client - HDFS][25]         | [Apache License][26]                                                                                                                                                                                |
| [Metrics Core][27]                         | [Apache License 2.0][11]                                                                                                                                                                            |
| [Protocol Buffers [Core]][28]              | [BSD-3-Clause][18]                                                                                                                                                                                  |
| [gcs-connector-hadoop3][29]                | [Apache License, Version 2.0][6]                                                                                                                                                                    |
| [Google OAuth Client Library for Java][30] | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [ORC Core][31]                             | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| [aircompressor][32]                        | [Apache License 2.0][11]                                                                                                                                                                            |
| [Apache Avro][33]                          | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Commons Compress][34]              | [Apache-2.0][3]                                                                                                                                                                                     |
| [Nimbus JOSE+JWT][35]                      | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [delta-core][36]                           | [Apache-2.0][37]                                                                                                                                                                                    |
| [Spark Project SQL][38]                    | [Apache 2.0 License][39]                                                                                                                                                                            |
| [Apache Ivy][40]                           | [The Apache Software License, Version 2.0][6]                                                                                                                                                       |
| [janino][41]                               | [BSD-3-Clause][42]                                                                                                                                                                                  |
| [Parquet for Java][43]                     | [MIT License][44]                                                                                                                                                                                   |
| [JUL to SLF4J bridge][45]                  | [MIT License][46]                                                                                                                                                                                   |
| [Apache Log4j API][47]                     | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j 1.x Compatibility API][48]   | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j Core][49]                    | [Apache-2.0][3]                                                                                                                                                                                     |
| [scala-logging][50]                        | [Apache 2.0 License][39]                                                                                                                                                                            |
| [jersey-core-common][51]                   | [EPL 2.0][52]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][53]; [Apache License, 2.0][39]; [Public Domain][54]                                                      |
| [jersey-core-client][55]                   | [EPL 2.0][52]; [GPL2 w/ CPE][53]; [EDL 1.0][56]; [BSD 2-Clause][57]; [Apache License, 2.0][39]; [Public Domain][54]; [Modified BSD][58]; [jQuery license][59]; [MIT license][46]; [W3C license][60] |
| [jersey-core-server][61]                   | [EPL 2.0][52]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][53]; [Apache License, 2.0][39]; [Modified BSD][58]                                                       |
| [jersey-container-servlet][62]             | [EPL 2.0][52]; [GPL2 w/ CPE][53]; [EDL 1.0][56]; [BSD 2-Clause][57]; [Apache License, 2.0][39]; [Public Domain][54]; [Modified BSD][58]; [jQuery license][59]; [MIT license][46]; [W3C license][60] |
| [jersey-container-servlet-core][63]        | [EPL 2.0][52]; [GPL2 w/ CPE][53]; [EDL 1.0][56]; [BSD 2-Clause][57]; [Apache License, 2.0][39]; [Public Domain][54]; [Modified BSD][58]; [jQuery license][59]; [MIT license][46]; [W3C license][60] |
| [jersey-inject-hk2][64]                    | [EPL 2.0][52]; [GPL2 w/ CPE][53]; [EDL 1.0][56]; [BSD 2-Clause][57]; [Apache License, 2.0][39]; [Public Domain][54]; [Modified BSD][58]; [jQuery license][59]; [MIT license][46]; [W3C license][60] |

### Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [scalatest][65]                            | [the Apache License, ASL Version 2.0][37] |
| [scalatestplus-mockito][66]                | [Apache-2.0][37]                          |
| [mockito-core][67]                         | [MIT][68]                                 |
| [Hamcrest][69]                             | [BSD-3-Clause][70]                        |
| [testcontainers-scala-scalatest][71]       | [The MIT License (MIT)][68]               |
| [Testcontainers :: Localstack][72]         | [MIT][73]                                 |
| [Test containers for Exasol on Docker][74] | [MIT License][75]                         |
| [Test Database Builder for Java][76]       | [MIT License][77]                         |
| [Matcher for SQL Result Sets][78]          | [MIT License][79]                         |
| [EqualsVerifier \| release normal jar][80] | [Apache License, Version 2.0][3]          |
| [JUnit Jupiter API][81]                    | [Eclipse Public License v2.0][82]         |
| [Maven Project Version Getter][83]         | [MIT License][84]                         |
| [Extension integration tests library][85]  | [MIT License][86]                         |

### Runtime Dependencies

| Dependency                   | License                                                                       |
| ---------------------------- | ----------------------------------------------------------------------------- |
| [Logback Classic Module][87] | [Eclipse Public License - v 1.0][88]; [GNU Lesser General Public License][89] |
| [Logback Core Module][90]    | [Eclipse Public License - v 1.0][88]; [GNU Lesser General Public License][89] |

### Plugin Dependencies

| Dependency                                               | License                                       |
| -------------------------------------------------------- | --------------------------------------------- |
| [Project Keeper Maven plugin][91]                        | [The MIT License][92]                         |
| [OpenFastTrace Maven Plugin][93]                         | [GNU General Public License v3.0][94]         |
| [Scalastyle Maven Plugin][95]                            | [Apache 2.0][39]                              |
| [spotless-maven-plugin][96]                              | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][97]                              | [BSD-3-Clause][18]                            |
| [Exec Maven Plugin][98]                                  | [Apache License 2][3]                         |
| [Apache Maven Clean Plugin][99]                          | [Apache-2.0][3]                               |
| [Apache Maven Install Plugin][100]                       | [Apache-2.0][3]                               |
| [Apache Maven Resources Plugin][101]                     | [Apache-2.0][3]                               |
| [Apache Maven Site Plugin][102]                          | [Apache License, Version 2.0][3]              |
| [SonarQube Scanner for Maven][103]                       | [GNU LGPL 3][104]                             |
| [Apache Maven Toolchains Plugin][105]                    | [Apache-2.0][3]                               |
| [Apache Maven Compiler Plugin][106]                      | [Apache-2.0][3]                               |
| [Apache Maven Enforcer Plugin][107]                      | [Apache-2.0][3]                               |
| [Maven Flatten Plugin][108]                              | [Apache Software Licenese][3]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][109] | [ASL2][6]                                     |
| [scala-maven-plugin][110]                                | [Public domain (Unlicense)][111]              |
| [ScalaTest Maven Plugin][112]                            | [the Apache License, ASL Version 2.0][37]     |
| [Apache Maven Javadoc Plugin][113]                       | [Apache-2.0][3]                               |
| [Maven Surefire Plugin][114]                             | [Apache-2.0][3]                               |
| [Versions Maven Plugin][115]                             | [Apache License, Version 2.0][3]              |
| [duplicate-finder-maven-plugin Maven Mojo][116]          | [Apache License 2.0][39]                      |
| [Apache Maven Assembly Plugin][117]                      | [Apache-2.0][3]                               |
| [Apache Maven JAR Plugin][118]                           | [Apache-2.0][3]                               |
| [Artifact reference checker and unifier][119]            | [MIT License][120]                            |
| [Maven Failsafe Plugin][121]                             | [Apache-2.0][3]                               |
| [JaCoCo :: Maven Plugin][122]                            | [EPL-2.0][123]                                |
| [Quality Summarizer Maven Plugin][124]                   | [MIT License][125]                            |
| [error-code-crawler-maven-plugin][126]                   | [MIT License][127]                            |
| [Reproducible Build Maven Plugin][128]                   | [Apache 2.0][6]                               |

## Extension

### Compile Dependencies

| Dependency                                 | License |
| ------------------------------------------ | ------- |
| [@exasol/extension-manager-interface][129] | MIT     |

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
[16]: https://commons.apache.org/proper/commons-io/
[17]: https://github.com/dnsjava/dnsjava
[18]: https://opensource.org/licenses/BSD-3-Clause
[19]: https://github.com/mwiede/jsch
[20]: https://github.com/mwiede/jsch/blob/master/LICENSE.txt
[21]: https://github.com/mwiede/jsch/blob/master/LICENSE.JZlib.txt
[22]: https://github.com/mwiede/jsch/blob/master/LICENSE.jBCrypt.txt
[23]: http://zookeeper.apache.org/zookeeper
[24]: https://kotlinlang.org/
[25]: https://www.alluxio.io/alluxio-dora/alluxio-core/alluxio-core-client/alluxio-core-client-hdfs/
[26]: https://github.com/alluxio/alluxio/blob/master/LICENSE
[27]: https://metrics.dropwizard.io/metrics-core
[28]: https://developers.google.com/protocol-buffers/protobuf-java/
[29]: https://github.com/GoogleCloudPlatform/BigData-interop/gcs-connector/
[30]: https://github.com/googleapis/google-oauth-java-client/google-oauth-client
[31]: https://orc.apache.org/orc-core
[32]: https://github.com/airlift/aircompressor
[33]: https://avro.apache.org
[34]: https://commons.apache.org/proper/commons-compress/
[35]: https://bitbucket.org/connect2id/nimbus-jose-jwt
[36]: https://delta.io/
[37]: http://www.apache.org/licenses/LICENSE-2.0
[38]: https://spark.apache.org/
[39]: http://www.apache.org/licenses/LICENSE-2.0.html
[40]: http://ant.apache.org/ivy/
[41]: http://janino-compiler.github.io/janino/
[42]: https://spdx.org/licenses/BSD-3-Clause.html
[43]: https://github.com/exasol/parquet-io-java/
[44]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[45]: http://www.slf4j.org
[46]: http://www.opensource.org/licenses/mit-license.php
[47]: https://logging.apache.org/log4j/2.x/log4j/log4j-api/
[48]: https://logging.apache.org/log4j/2.x/log4j/log4j-1.2-api/
[49]: https://logging.apache.org/log4j/2.x/log4j/log4j-core/
[50]: https://github.com/lightbend/scala-logging
[51]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-common
[52]: http://www.eclipse.org/legal/epl-2.0
[53]: https://www.gnu.org/software/classpath/license.html
[54]: https://creativecommons.org/publicdomain/zero/1.0/
[55]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-client
[56]: http://www.eclipse.org/org/documents/edl-v10.php
[57]: https://opensource.org/licenses/BSD-2-Clause
[58]: https://asm.ow2.io/license.html
[59]: jquery.org/license
[60]: https://www.w3.org/Consortium/Legal/copyright-documents-19990405
[61]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-server
[62]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet
[63]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet-core
[64]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-hk2
[65]: http://www.scalatest.org
[66]: https://github.com/scalatest/scalatestplus-mockito
[67]: https://github.com/mockito/mockito
[68]: https://opensource.org/licenses/MIT
[69]: http://hamcrest.org/JavaHamcrest/
[70]: https://raw.githubusercontent.com/hamcrest/JavaHamcrest/master/LICENSE
[71]: https://github.com/testcontainers/testcontainers-scala
[72]: https://java.testcontainers.org
[73]: http://opensource.org/licenses/MIT
[74]: https://github.com/exasol/exasol-testcontainers/
[75]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[76]: https://github.com/exasol/test-db-builder-java/
[77]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[78]: https://github.com/exasol/hamcrest-resultset-matcher/
[79]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[80]: https://www.jqno.nl/equalsverifier
[81]: https://junit.org/junit5/
[82]: https://www.eclipse.org/legal/epl-v20.html
[83]: https://github.com/exasol/maven-project-version-getter/
[84]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[85]: https://github.com/exasol/extension-manager/
[86]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[87]: http://logback.qos.ch/logback-classic
[88]: http://www.eclipse.org/legal/epl-v10.html
[89]: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
[90]: http://logback.qos.ch/logback-core
[91]: https://github.com/exasol/project-keeper/
[92]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[93]: https://github.com/itsallcode/openfasttrace-maven-plugin
[94]: https://www.gnu.org/licenses/gpl-3.0.html
[95]: http://www.scalastyle.org
[96]: https://github.com/diffplug/spotless
[97]: https://github.com/evis/scalafix-maven-plugin
[98]: https://www.mojohaus.org/exec-maven-plugin
[99]: https://maven.apache.org/plugins/maven-clean-plugin/
[100]: https://maven.apache.org/plugins/maven-install-plugin/
[101]: https://maven.apache.org/plugins/maven-resources-plugin/
[102]: https://maven.apache.org/plugins/maven-site-plugin/
[103]: http://sonarsource.github.io/sonar-scanner-maven/
[104]: http://www.gnu.org/licenses/lgpl.txt
[105]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[106]: https://maven.apache.org/plugins/maven-compiler-plugin/
[107]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[108]: https://www.mojohaus.org/flatten-maven-plugin/
[109]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[110]: http://github.com/davidB/scala-maven-plugin
[111]: http://unlicense.org/
[112]: https://www.scalatest.org/user_guide/using_the_scalatest_maven_plugin
[113]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[114]: https://maven.apache.org/surefire/maven-surefire-plugin/
[115]: https://www.mojohaus.org/versions/versions-maven-plugin/
[116]: https://basepom.github.io/duplicate-finder-maven-plugin
[117]: https://maven.apache.org/plugins/maven-assembly-plugin/
[118]: https://maven.apache.org/plugins/maven-jar-plugin/
[119]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[120]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[121]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[122]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[123]: https://www.eclipse.org/legal/epl-2.0/
[124]: https://github.com/exasol/quality-summarizer-maven-plugin/
[125]: https://github.com/exasol/quality-summarizer-maven-plugin/blob/main/LICENSE
[126]: https://github.com/exasol/error-code-crawler-maven-plugin/
[127]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[128]: http://zlika.github.io/reproducible-build-maven-plugin
[129]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.4.3.tgz
