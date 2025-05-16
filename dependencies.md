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
| [Apache Commons IO][16]                    | [Apache-2.0][3]                                                                                                                                                                                     |
| [dnsjava][17]                              | [BSD-3-Clause][18]                                                                                                                                                                                  |
| [JSch][19]                                 | [Revised BSD][20]; [Revised BSD][21]; [ISC][22]                                                                                                                                                     |
| Apache Hadoop Amazon Web Services support  | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache ZooKeeper - Server][23]            | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| Apache Hadoop Azure support                | [Apache-2.0][3]                                                                                                                                                                                     |
| Apache Hadoop Azure Data Lake support      | [Apache-2.0][3]                                                                                                                                                                                     |
| Apache Hadoop HDFS                         | [Apache-2.0][3]                                                                                                                                                                                     |
| Apache Hadoop HDFS Client                  | [Apache-2.0][3]                                                                                                                                                                                     |
| [Kotlin Stdlib][24]                        | [The Apache License, Version 2.0][6]                                                                                                                                                                |
| [Alluxio Core - Client - HDFS][25]         | [Apache License][26]                                                                                                                                                                                |
| [Metrics Core][27]                         | [Apache License 2.0][11]                                                                                                                                                                            |
| [Protocol Buffers [Core]][28]              | [BSD-3-Clause][18]                                                                                                                                                                                  |
| [gcs-connector][29]                        | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| [Google OAuth Client Library for Java][30] | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [ORC Core][31]                             | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| [aircompressor][32]                        | [Apache License 2.0][11]                                                                                                                                                                            |
| [Apache Avro][33]                          | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Commons Compress][34]              | [Apache-2.0][3]                                                                                                                                                                                     |
| [Nimbus JOSE+JWT][35]                      | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [delta-core][36]                           | [Apache-2.0][37]                                                                                                                                                                                    |
| [Spark Project SQL][38]                    | [Apache-2.0][39]                                                                                                                                                                                    |
| [Apache Ivy][40]                           | [The Apache Software License, Version 2.0][6]                                                                                                                                                       |
| [janino][41]                               | [BSD-3-Clause][42]                                                                                                                                                                                  |
| [Parquet for Java][43]                     | [MIT License][44]                                                                                                                                                                                   |
| [JUL to SLF4J bridge][45]                  | [MIT][46]                                                                                                                                                                                           |
| [Apache Log4j API][47]                     | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j 1.x Compatibility API][48]   | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j Core][49]                    | [Apache-2.0][3]                                                                                                                                                                                     |
| [scala-logging][50]                        | [Apache 2.0 License][39]                                                                                                                                                                            |
| [jersey-core-common][51]                   | [EPL 2.0][52]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][53]; [Apache License, 2.0][39]; [Public Domain][54]                                                      |
| [jersey-core-client][55]                   | [EPL 2.0][52]; [GPL2 w/ CPE][53]; [EDL 1.0][56]; [BSD 2-Clause][57]; [Apache License, 2.0][39]; [Public Domain][54]; [Modified BSD][58]; [jQuery license][59]; [MIT license][60]; [W3C license][61] |
| [jersey-core-server][62]                   | [EPL 2.0][52]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][53]; [Apache License, 2.0][39]; [Modified BSD][58]                                                       |
| [jersey-container-servlet][63]             | [EPL 2.0][52]; [GPL2 w/ CPE][53]; [EDL 1.0][56]; [BSD 2-Clause][57]; [Apache License, 2.0][39]; [Public Domain][54]; [Modified BSD][58]; [jQuery license][59]; [MIT license][60]; [W3C license][61] |
| [jersey-container-servlet-core][64]        | [EPL 2.0][52]; [GPL2 w/ CPE][53]; [EDL 1.0][56]; [BSD 2-Clause][57]; [Apache License, 2.0][39]; [Public Domain][54]; [Modified BSD][58]; [jQuery license][59]; [MIT license][60]; [W3C license][61] |
| [jersey-inject-hk2][65]                    | [EPL 2.0][52]; [GPL2 w/ CPE][53]; [EDL 1.0][56]; [BSD 2-Clause][57]; [Apache License, 2.0][39]; [Public Domain][54]; [Modified BSD][58]; [jQuery license][59]; [MIT license][60]; [W3C license][61] |

### Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [scalatest][66]                            | [the Apache License, ASL Version 2.0][37] |
| [scalatestplus-mockito][67]                | [Apache-2.0][37]                          |
| [mockito-core][68]                         | [MIT][69]                                 |
| [Hamcrest][70]                             | [BSD-3-Clause][71]                        |
| [testcontainers-scala-scalatest][72]       | [The MIT License (MIT)][69]               |
| [Testcontainers :: Localstack][73]         | [MIT][74]                                 |
| [Test containers for Exasol on Docker][75] | [MIT License][76]                         |
| [Test Database Builder for Java][77]       | [MIT License][78]                         |
| [Matcher for SQL Result Sets][79]          | [MIT License][80]                         |
| [EqualsVerifier \| release normal jar][81] | [Apache License, Version 2.0][3]          |
| [JUnit Jupiter API][82]                    | [Eclipse Public License v2.0][83]         |
| [Maven Project Version Getter][84]         | [MIT License][85]                         |
| [Extension integration tests library][86]  | [MIT License][87]                         |

### Runtime Dependencies

| Dependency                   | License                                                                       |
| ---------------------------- | ----------------------------------------------------------------------------- |
| [Logback Classic Module][88] | [Eclipse Public License - v 1.0][89]; [GNU Lesser General Public License][90] |
| [Logback Core Module][91]    | [Eclipse Public License - v 1.0][89]; [GNU Lesser General Public License][90] |

### Plugin Dependencies

| Dependency                                               | License                                       |
| -------------------------------------------------------- | --------------------------------------------- |
| [Project Keeper Maven plugin][92]                        | [The MIT License][93]                         |
| [Scalastyle Maven Plugin][94]                            | [Apache 2.0][39]                              |
| [spotless-maven-plugin][95]                              | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][96]                              | [BSD-3-Clause][18]                            |
| [Exec Maven Plugin][97]                                  | [Apache License 2][3]                         |
| [Apache Maven Clean Plugin][98]                          | [Apache-2.0][3]                               |
| [Apache Maven Install Plugin][99]                        | [Apache-2.0][3]                               |
| [Apache Maven Resources Plugin][100]                     | [Apache-2.0][3]                               |
| [Apache Maven Site Plugin][101]                          | [Apache-2.0][3]                               |
| [SonarQube Scanner for Maven][102]                       | [GNU LGPL 3][103]                             |
| [Apache Maven Toolchains Plugin][104]                    | [Apache-2.0][3]                               |
| [Apache Maven Compiler Plugin][105]                      | [Apache-2.0][3]                               |
| [Apache Maven Enforcer Plugin][106]                      | [Apache-2.0][3]                               |
| [Maven Flatten Plugin][107]                              | [Apache Software Licenese][3]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][108] | [ASL2][6]                                     |
| [scala-maven-plugin][109]                                | [Public domain (Unlicense)][110]              |
| [ScalaTest Maven Plugin][111]                            | [the Apache License, ASL Version 2.0][37]     |
| [Apache Maven Javadoc Plugin][112]                       | [Apache-2.0][3]                               |
| [Maven Surefire Plugin][113]                             | [Apache-2.0][3]                               |
| [Versions Maven Plugin][114]                             | [Apache License, Version 2.0][3]              |
| [duplicate-finder-maven-plugin Maven Mojo][115]          | [Apache License 2.0][39]                      |
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
[29]: https://github.com/GoogleCloudDataproc/hadoop-connectors/gcs-connector
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
[46]: https://opensource.org/license/mit
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
[60]: http://www.opensource.org/licenses/mit-license.php
[61]: https://www.w3.org/Consortium/Legal/copyright-documents-19990405
[62]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-server
[63]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet
[64]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet-core
[65]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-hk2
[66]: http://www.scalatest.org
[67]: https://github.com/scalatest/scalatestplus-mockito
[68]: https://github.com/mockito/mockito
[69]: https://opensource.org/licenses/MIT
[70]: http://hamcrest.org/JavaHamcrest/
[71]: https://raw.githubusercontent.com/hamcrest/JavaHamcrest/master/LICENSE
[72]: https://github.com/testcontainers/testcontainers-scala
[73]: https://java.testcontainers.org
[74]: http://opensource.org/licenses/MIT
[75]: https://github.com/exasol/exasol-testcontainers/
[76]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[77]: https://github.com/exasol/test-db-builder-java/
[78]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[79]: https://github.com/exasol/hamcrest-resultset-matcher/
[80]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[81]: https://www.jqno.nl/equalsverifier
[82]: https://junit.org/junit5/
[83]: https://www.eclipse.org/legal/epl-v20.html
[84]: https://github.com/exasol/maven-project-version-getter/
[85]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[86]: https://github.com/exasol/extension-manager/
[87]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[88]: http://logback.qos.ch/logback-classic
[89]: http://www.eclipse.org/legal/epl-v10.html
[90]: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
[91]: http://logback.qos.ch/logback-core
[92]: https://github.com/exasol/project-keeper/
[93]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[94]: http://www.scalastyle.org
[95]: https://github.com/diffplug/spotless
[96]: https://github.com/evis/scalafix-maven-plugin
[97]: https://www.mojohaus.org/exec-maven-plugin
[98]: https://maven.apache.org/plugins/maven-clean-plugin/
[99]: https://maven.apache.org/plugins/maven-install-plugin/
[100]: https://maven.apache.org/plugins/maven-resources-plugin/
[101]: https://maven.apache.org/plugins/maven-site-plugin/
[102]: http://docs.sonarqube.org/display/PLUG/Plugin+Library/sonar-maven-plugin
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
[128]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.4.3.tgz
