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
| [AWS Java SDK :: Services :: Amazon S3][7] | [Apache License, Version 2.0][8]                                                                                                                                                                    |
| [Netty/Codec/HTTP2][9]                     | [Apache License, Version 2.0][1]                                                                                                                                                                    |
| [Apache Commons BeanUtils][10]             | [Apache-2.0][3]                                                                                                                                                                                     |
| [snappy-java][11]                          | [Apache-2.0][12]                                                                                                                                                                                    |
| [Import Export UDF Common Scala][13]       | [MIT License][14]                                                                                                                                                                                   |
| [error-reporting-java][15]                 | [MIT License][16]                                                                                                                                                                                   |
| Apache Hadoop Common                       | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Commons IO][17]                    | [Apache-2.0][3]                                                                                                                                                                                     |
| [dnsjava][18]                              | [BSD-3-Clause][19]                                                                                                                                                                                  |
| [JSch][20]                                 | [Revised BSD][21]; [Revised BSD][22]; [ISC][23]                                                                                                                                                     |
| Apache Hadoop Amazon Web Services support  | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache ZooKeeper - Server][24]            | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| Apache Hadoop Azure support                | [Apache-2.0][3]                                                                                                                                                                                     |
| Apache Hadoop Azure Data Lake support      | [Apache-2.0][3]                                                                                                                                                                                     |
| Apache Hadoop HDFS                         | [Apache-2.0][3]                                                                                                                                                                                     |
| Apache Hadoop HDFS Client                  | [Apache-2.0][3]                                                                                                                                                                                     |
| [Kotlin Stdlib][25]                        | [The Apache License, Version 2.0][6]                                                                                                                                                                |
| [Alluxio Core - Client - HDFS][26]         | [Apache License][27]                                                                                                                                                                                |
| [Metrics Core][28]                         | [Apache License 2.0][12]                                                                                                                                                                            |
| [Protocol Buffers [Core]][29]              | [BSD-3-Clause][19]                                                                                                                                                                                  |
| [gcs-connector-hadoop3][30]                | [Apache License, Version 2.0][6]                                                                                                                                                                    |
| [Google OAuth Client Library for Java][31] | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [ORC Core][32]                             | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| [aircompressor][33]                        | [Apache License 2.0][12]                                                                                                                                                                            |
| [Apache Avro][34]                          | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Commons Compress][35]              | [Apache-2.0][3]                                                                                                                                                                                     |
| [Nimbus JOSE+JWT][36]                      | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [delta-core][37]                           | [Apache-2.0][38]                                                                                                                                                                                    |
| [Spark Project SQL][39]                    | [Apache 2.0 License][40]                                                                                                                                                                            |
| [Apache Ivy][41]                           | [The Apache Software License, Version 2.0][6]                                                                                                                                                       |
| [janino][42]                               | [BSD-3-Clause][43]                                                                                                                                                                                  |
| [Parquet for Java][44]                     | [MIT License][45]                                                                                                                                                                                   |
| [JUL to SLF4J bridge][46]                  | [MIT License][47]                                                                                                                                                                                   |
| [Apache Log4j API][48]                     | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j 1.x Compatibility API][49]   | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j Core][50]                    | [Apache-2.0][3]                                                                                                                                                                                     |
| [scala-logging][51]                        | [Apache 2.0 License][40]                                                                                                                                                                            |
| [jersey-core-common][52]                   | [EPL 2.0][53]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][54]; [Apache License, 2.0][40]; [Public Domain][55]                                                      |
| [jersey-core-client][56]                   | [EPL 2.0][53]; [GPL2 w/ CPE][54]; [EDL 1.0][57]; [BSD 2-Clause][58]; [Apache License, 2.0][40]; [Public Domain][55]; [Modified BSD][59]; [jQuery license][60]; [MIT license][47]; [W3C license][61] |
| [jersey-core-server][62]                   | [EPL 2.0][53]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][54]; [Apache License, 2.0][40]; [Modified BSD][59]                                                       |
| [jersey-container-servlet][63]             | [EPL 2.0][53]; [GPL2 w/ CPE][54]; [EDL 1.0][57]; [BSD 2-Clause][58]; [Apache License, 2.0][40]; [Public Domain][55]; [Modified BSD][59]; [jQuery license][60]; [MIT license][47]; [W3C license][61] |
| [jersey-container-servlet-core][64]        | [EPL 2.0][53]; [GPL2 w/ CPE][54]; [EDL 1.0][57]; [BSD 2-Clause][58]; [Apache License, 2.0][40]; [Public Domain][55]; [Modified BSD][59]; [jQuery license][60]; [MIT license][47]; [W3C license][61] |
| [jersey-inject-hk2][65]                    | [EPL 2.0][53]; [GPL2 w/ CPE][54]; [EDL 1.0][57]; [BSD 2-Clause][58]; [Apache License, 2.0][40]; [Public Domain][55]; [Modified BSD][59]; [jQuery license][60]; [MIT license][47]; [W3C license][61] |

### Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [scalatest][66]                            | [the Apache License, ASL Version 2.0][38] |
| [scalatestplus-mockito][67]                | [Apache-2.0][38]                          |
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
| [Scalastyle Maven Plugin][94]                            | [Apache 2.0][40]                              |
| [spotless-maven-plugin][95]                              | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][96]                              | [BSD-3-Clause][19]                            |
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
| [ScalaTest Maven Plugin][111]                            | [the Apache License, ASL Version 2.0][38]     |
| [Apache Maven Javadoc Plugin][112]                       | [Apache-2.0][3]                               |
| [Maven Surefire Plugin][113]                             | [Apache-2.0][3]                               |
| [Versions Maven Plugin][114]                             | [Apache License, Version 2.0][3]              |
| [duplicate-finder-maven-plugin Maven Mojo][115]          | [Apache License 2.0][40]                      |
| [Apache Maven Artifact Plugin][116]                      | [Apache-2.0][3]                               |
| [Apache Maven Assembly Plugin][117]                      | [Apache-2.0][3]                               |
| [Apache Maven JAR Plugin][118]                           | [Apache-2.0][3]                               |
| [Artifact reference checker and unifier][119]            | [MIT License][120]                            |
| [Maven Failsafe Plugin][121]                             | [Apache-2.0][3]                               |
| [JaCoCo :: Maven Plugin][122]                            | [EPL-2.0][123]                                |
| [Quality Summarizer Maven Plugin][124]                   | [MIT License][125]                            |
| [error-code-crawler-maven-plugin][126]                   | [MIT License][127]                            |
| [Git Commit Id Maven Plugin][128]                        | [GNU Lesser General Public License 3.0][129]  |

## Extension

### Compile Dependencies

| Dependency                                 | License |
| ------------------------------------------ | ------- |
| [@exasol/extension-manager-interface][130] | MIT     |

[0]: https://www.scala-lang.org/
[1]: https://www.apache.org/licenses/LICENSE-2.0
[2]: https://commons.apache.org/proper/commons-lang/
[3]: https://www.apache.org/licenses/LICENSE-2.0.txt
[4]: https://commons.apache.org/proper/commons-configuration/
[5]: https://github.com/google/guava
[6]: http://www.apache.org/licenses/LICENSE-2.0.txt
[7]: https://aws.amazon.com/sdkforjava
[8]: https://aws.amazon.com/apache2.0
[9]: https://netty.io/netty-codec-http2/
[10]: https://commons.apache.org/proper/commons-beanutils
[11]: https://github.com/xerial/snappy-java
[12]: https://www.apache.org/licenses/LICENSE-2.0.html
[13]: https://github.com/exasol/import-export-udf-common-scala/
[14]: https://github.com/exasol/import-export-udf-common-scala/blob/main/LICENSE
[15]: https://github.com/exasol/error-reporting-java/
[16]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[17]: https://commons.apache.org/proper/commons-io/
[18]: https://github.com/dnsjava/dnsjava
[19]: https://opensource.org/licenses/BSD-3-Clause
[20]: https://github.com/mwiede/jsch
[21]: https://github.com/mwiede/jsch/blob/master/LICENSE.txt
[22]: https://github.com/mwiede/jsch/blob/master/LICENSE.JZlib.txt
[23]: https://github.com/mwiede/jsch/blob/master/LICENSE.jBCrypt.txt
[24]: http://zookeeper.apache.org/zookeeper
[25]: https://kotlinlang.org/
[26]: https://www.alluxio.io/alluxio-dora/alluxio-core/alluxio-core-client/alluxio-core-client-hdfs/
[27]: https://github.com/alluxio/alluxio/blob/master/LICENSE
[28]: https://metrics.dropwizard.io/metrics-core
[29]: https://developers.google.com/protocol-buffers/protobuf-java/
[30]: https://github.com/GoogleCloudPlatform/BigData-interop/gcs-connector/
[31]: https://github.com/googleapis/google-oauth-java-client/google-oauth-client
[32]: https://orc.apache.org/orc-core
[33]: https://github.com/airlift/aircompressor
[34]: https://avro.apache.org
[35]: https://commons.apache.org/proper/commons-compress/
[36]: https://bitbucket.org/connect2id/nimbus-jose-jwt
[37]: https://delta.io/
[38]: http://www.apache.org/licenses/LICENSE-2.0
[39]: https://spark.apache.org/
[40]: http://www.apache.org/licenses/LICENSE-2.0.html
[41]: http://ant.apache.org/ivy/
[42]: http://janino-compiler.github.io/janino/
[43]: https://spdx.org/licenses/BSD-3-Clause.html
[44]: https://github.com/exasol/parquet-io-java/
[45]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[46]: http://www.slf4j.org
[47]: http://www.opensource.org/licenses/mit-license.php
[48]: https://logging.apache.org/log4j/2.x/log4j/log4j-api/
[49]: https://logging.apache.org/log4j/2.x/log4j/log4j-1.2-api/
[50]: https://logging.apache.org/log4j/2.x/log4j/log4j-core/
[51]: https://github.com/lightbend/scala-logging
[52]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-common
[53]: http://www.eclipse.org/legal/epl-2.0
[54]: https://www.gnu.org/software/classpath/license.html
[55]: https://creativecommons.org/publicdomain/zero/1.0/
[56]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-client
[57]: http://www.eclipse.org/org/documents/edl-v10.php
[58]: https://opensource.org/licenses/BSD-2-Clause
[59]: https://asm.ow2.io/license.html
[60]: jquery.org/license
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
[102]: http://docs.sonarqube.org/display/PLUG/Plugin+Library/sonar-scanner-maven/sonar-maven-plugin
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
[116]: https://maven.apache.org/plugins/maven-artifact-plugin/
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
[128]: https://github.com/git-commit-id/git-commit-id-maven-plugin
[129]: http://www.gnu.org/licenses/lgpl-3.0.txt
[130]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.4.3.tgz
