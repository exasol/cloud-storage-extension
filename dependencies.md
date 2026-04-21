<!-- @formatter:off -->
# Dependencies

## Cloud Storage Extension

### Compile Dependencies

| Dependency                                  | License                                                                                                                                                                                             |
| ------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| [Scala Library][0]                          | [Apache-2.0][1]                                                                                                                                                                                     |
| [Apache Commons Lang][2]                    | [Apache-2.0][3]                                                                                                                                                                                     |
| [Gson][4]                                   | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Commons Configuration][5]           | [Apache-2.0][3]                                                                                                                                                                                     |
| [Guava: Google Core Libraries for Java][6]  | [Apache License, Version 2.0][7]                                                                                                                                                                    |
| [AWS Java SDK :: Services :: Amazon S3][8]  | [Apache License, Version 2.0][9]                                                                                                                                                                    |
| [AWS Java SDK :: S3 :: Transfer Manager][8] | [Apache License, Version 2.0][9]                                                                                                                                                                    |
| [io.grpc:grpc-netty][10]                    | [Apache 2.0][11]                                                                                                                                                                                    |
| [Apache Commons BeanUtils][12]              | [Apache-2.0][3]                                                                                                                                                                                     |
| [snappy-java][13]                           | [Apache-2.0][14]                                                                                                                                                                                    |
| bcprov-jdk18on                              |                                                                                                                                                                                                     |
| [Vert.x Core][15]                           | [The Apache Software License, Version 2.0][7]; [Eclipse Public License - v 2.0][16]                                                                                                                 |
| [Import Export UDF Common Scala][17]        | [MIT License][18]                                                                                                                                                                                   |
| [error-reporting-java][19]                  | [MIT License][20]                                                                                                                                                                                   |
| Apache Hadoop Common                        | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Commons IO][21]                     | [Apache-2.0][3]                                                                                                                                                                                     |
| [dnsjava][22]                               | [BSD-3-Clause][23]                                                                                                                                                                                  |
| [JSch][24]                                  | [Revised BSD][25]; [Revised BSD][26]; [ISC][27]                                                                                                                                                     |
| Apache Hadoop Amazon Web Services support   | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache ZooKeeper - Server][28]             | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| Apache Hadoop Azure support                 | [Apache-2.0][3]                                                                                                                                                                                     |
| Apache Hadoop Azure Data Lake support       | [Apache-2.0][3]                                                                                                                                                                                     |
| Apache Hadoop HDFS                          | [Apache-2.0][3]                                                                                                                                                                                     |
| Apache Hadoop HDFS Client                   | [Apache-2.0][3]                                                                                                                                                                                     |
| [Kotlin Stdlib][29]                         | [Apache-2.0][7]                                                                                                                                                                                     |
| [Alluxio Core - Client - HDFS][30]          | [Apache License][31]                                                                                                                                                                                |
| [Metrics Core][32]                          | [Apache License 2.0][14]                                                                                                                                                                            |
| [Protocol Buffers [Core]][33]               | [BSD-3-Clause][23]                                                                                                                                                                                  |
| [Protocol Buffers [Util]][34]               | [BSD-3-Clause][23]                                                                                                                                                                                  |
| [gcs-connector][35]                         | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| [Google OAuth Client Library for Java][36]  | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [ORC Core][37]                              | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Avro][38]                           | [Apache-2.0][3]                                                                                                                                                                                     |
| lz4-java                                    |                                                                                                                                                                                                     |
| [LZ4 Java Compression][39]                  | [Apache License, Version 2.0][1]                                                                                                                                                                    |
| [Apache Commons Compress][40]               | [Apache-2.0][3]                                                                                                                                                                                     |
| [Nimbus JOSE+JWT][41]                       | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [delta-spark][42]                           | [Apache-2.0][43]                                                                                                                                                                                    |
| [Spark Project SQL][44]                     | [Apache-2.0][14]                                                                                                                                                                                    |
| [Apache Ivy][45]                            | [The Apache Software License, Version 2.0][7]                                                                                                                                                       |
| [janino][46]                                | [BSD-3-Clause][47]                                                                                                                                                                                  |
| [Parquet for Java][48]                      | [MIT License][49]                                                                                                                                                                                   |
| [JUL to SLF4J bridge][50]                   | [MIT][51]                                                                                                                                                                                           |
| [Apache Log4j API][52]                      | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j 1.x Compatibility API][52]    | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j Core][52]                     | [Apache-2.0][3]                                                                                                                                                                                     |
| [scala-logging][53]                         | [Apache 2.0 License][54]                                                                                                                                                                            |
| [jersey-core-common][55]                    | [EPL 2.0][56]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][57]; [Apache License, 2.0][54]; [Public Domain][58]                                                      |
| [jersey-core-client][59]                    | [EPL 2.0][56]; [GPL2 w/ CPE][57]; [EDL 1.0][60]; [BSD 2-Clause][61]; [Apache License, 2.0][54]; [Public Domain][58]; [Modified BSD][62]; [jQuery license][63]; [MIT license][64]; [W3C license][65] |
| [jersey-core-server][66]                    | [EPL 2.0][56]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][57]; [Apache License, 2.0][54]; [Modified BSD][62]                                                       |
| [jersey-container-servlet][67]              | [EPL 2.0][56]; [GPL2 w/ CPE][57]; [EDL 1.0][60]; [BSD 2-Clause][61]; [Apache License, 2.0][54]; [Public Domain][58]; [Modified BSD][62]; [jQuery license][63]; [MIT license][64]; [W3C license][65] |
| [jersey-container-servlet-core][68]         | [EPL 2.0][56]; [GPL2 w/ CPE][57]; [EDL 1.0][60]; [BSD 2-Clause][61]; [Apache License, 2.0][54]; [Public Domain][58]; [Modified BSD][62]; [jQuery license][63]; [MIT license][64]; [W3C license][65] |
| [jersey-inject-hk2][69]                     | [EPL 2.0][56]; [GPL2 w/ CPE][57]; [EDL 1.0][60]; [BSD 2-Clause][61]; [Apache License, 2.0][54]; [Public Domain][58]; [Modified BSD][62]; [jQuery license][63]; [MIT license][64]; [W3C license][65] |

### Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [Alluxio Under File System - Local FS][70] | [Apache License][31]                      |
| [scalatest][71]                            | [the Apache License, ASL Version 2.0][43] |
| [scalatestplus-mockito][72]                | [Apache-2.0][43]                          |
| [mockito-core][73]                         | [MIT][74]                                 |
| [Hamcrest][75]                             | [BSD-3-Clause][76]                        |
| [testcontainers-scala-scalatest][77]       | [The MIT License (MIT)][74]               |
| [Testcontainers :: Localstack][78]         | [MIT][79]                                 |
| [Test containers for Exasol on Docker][80] | [MIT License][81]                         |
| [Test Database Builder for Java][82]       | [MIT License][83]                         |
| [Matcher for SQL Result Sets][84]          | [MIT License][85]                         |
| [EqualsVerifier \| release normal jar][86] | [Apache License, Version 2.0][3]          |
| [JUnit Jupiter API][87]                    | [Eclipse Public License v2.0][88]         |
| [Maven Project Version Getter][89]         | [MIT License][90]                         |
| [Extension integration tests library][91]  | [MIT License][92]                         |

### Runtime Dependencies

| Dependency                   | License                                                                       |
| ---------------------------- | ----------------------------------------------------------------------------- |
| [Logback Classic Module][93] | [Eclipse Public License - v 2.0][88]; [GNU Lesser General Public License][94] |
| [Logback Core Module][95]    | [Eclipse Public License - v 2.0][88]; [GNU Lesser General Public License][94] |

### Plugin Dependencies

| Dependency                                               | License                                       |
| -------------------------------------------------------- | --------------------------------------------- |
| [Project Keeper Maven plugin][96]                        | [The MIT License][97]                         |
| [Scalastyle Maven Plugin][98]                            | [Apache 2.0][54]                              |
| [spotless-maven-plugin][99]                              | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][100]                             | [BSD-3-Clause][23]                            |
| [Exec Maven Plugin][101]                                 | [Apache License 2][3]                         |
| [Apache Maven Clean Plugin][102]                         | [Apache-2.0][3]                               |
| [Apache Maven Install Plugin][103]                       | [Apache-2.0][3]                               |
| [Apache Maven Resources Plugin][104]                     | [Apache-2.0][3]                               |
| [Apache Maven Site Plugin][105]                          | [Apache-2.0][3]                               |
| [SonarQube Scanner for Maven][106]                       | [GNU LGPL 3][107]                             |
| [Apache Maven Toolchains Plugin][108]                    | [Apache-2.0][3]                               |
| [Apache Maven Compiler Plugin][109]                      | [Apache-2.0][3]                               |
| [Apache Maven Enforcer Plugin][110]                      | [Apache-2.0][3]                               |
| [Maven Flatten Plugin][111]                              | [Apache Software License][3]                  |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][112] | [ASL2][7]                                     |
| [scala-maven-plugin][113]                                | [Public domain (Unlicense)][114]              |
| [ScalaTest Maven Plugin][115]                            | [the Apache License, ASL Version 2.0][43]     |
| [Apache Maven Javadoc Plugin][116]                       | [Apache-2.0][3]                               |
| [Maven Surefire Plugin][117]                             | [Apache-2.0][3]                               |
| [Versions Maven Plugin][118]                             | [Apache License, Version 2.0][3]              |
| [duplicate-finder-maven-plugin Maven Mojo][119]          | [Apache License 2.0][54]                      |
| [Apache Maven Artifact Plugin][120]                      | [Apache-2.0][3]                               |
| [Apache Maven Assembly Plugin][121]                      | [Apache-2.0][3]                               |
| [Apache Maven JAR Plugin][122]                           | [Apache-2.0][3]                               |
| [Artifact reference checker and unifier][123]            | [MIT License][124]                            |
| [Maven Failsafe Plugin][125]                             | [Apache-2.0][3]                               |
| [JaCoCo :: Maven Plugin][126]                            | [EPL-2.0][127]                                |
| [Quality Summarizer Maven Plugin][128]                   | [MIT License][129]                            |
| [error-code-crawler-maven-plugin][130]                   | [MIT License][131]                            |
| [Git Commit Id Maven Plugin][132]                        | [GNU Lesser General Public License 3.0][133]  |

## Extension

### Compile Dependencies

| Dependency                                 | License |
| ------------------------------------------ | ------- |
| [@exasol/extension-manager-interface][134] | MIT     |

[0]: https://www.scala-lang.org/
[1]: https://www.apache.org/licenses/LICENSE-2.0
[2]: https://commons.apache.org/proper/commons-lang/
[3]: https://www.apache.org/licenses/LICENSE-2.0.txt
[4]: https://github.com/google/gson
[5]: https://commons.apache.org/proper/commons-configuration/
[6]: https://github.com/google/guava
[7]: http://www.apache.org/licenses/LICENSE-2.0.txt
[8]: https://aws.amazon.com/sdkforjava
[9]: https://aws.amazon.com/apache2.0
[10]: https://github.com/grpc/grpc-java
[11]: https://opensource.org/licenses/Apache-2.0
[12]: https://commons.apache.org/proper/commons-beanutils
[13]: https://github.com/xerial/snappy-java
[14]: https://www.apache.org/licenses/LICENSE-2.0.html
[15]: https://github.com/vert-x3/vertx-parent/vertx-core
[16]: http://www.eclipse.org/legal/epl-v20.html
[17]: https://github.com/exasol/import-export-udf-common-scala/
[18]: https://github.com/exasol/import-export-udf-common-scala/blob/main/LICENSE
[19]: https://github.com/exasol/error-reporting-java/
[20]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[21]: https://commons.apache.org/proper/commons-io/
[22]: https://github.com/dnsjava/dnsjava
[23]: https://opensource.org/licenses/BSD-3-Clause
[24]: https://github.com/mwiede/jsch
[25]: https://github.com/mwiede/jsch/blob/master/LICENSE.txt
[26]: https://github.com/mwiede/jsch/blob/master/LICENSE.JZlib.txt
[27]: https://github.com/mwiede/jsch/blob/master/LICENSE.jBCrypt.txt
[28]: http://zookeeper.apache.org/zookeeper
[29]: https://kotlinlang.org/
[30]: https://www.alluxio.io/alluxio-dora/alluxio-core/alluxio-core-client/alluxio-core-client-hdfs/
[31]: https://github.com/alluxio/alluxio/blob/master/LICENSE
[32]: https://metrics.dropwizard.io/metrics-core
[33]: https://developers.google.com/protocol-buffers/protobuf-java/
[34]: https://developers.google.com/protocol-buffers/protobuf-java-util/
[35]: https://github.com/GoogleCloudDataproc/hadoop-connectors/gcs-connector
[36]: https://github.com/googleapis/google-oauth-java-client/google-oauth-client
[37]: https://orc.apache.org/orc-core
[38]: https://avro.apache.org
[39]: https://github.com/yawkat/lz4-java
[40]: https://commons.apache.org/proper/commons-compress/
[41]: https://bitbucket.org/connect2id/nimbus-jose-jwt
[42]: https://delta.io/
[43]: http://www.apache.org/licenses/LICENSE-2.0
[44]: https://spark.apache.org/
[45]: http://ant.apache.org/ivy/
[46]: http://janino-compiler.github.io/janino/
[47]: https://spdx.org/licenses/BSD-3-Clause.html
[48]: https://github.com/exasol/parquet-io-java/
[49]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[50]: http://www.slf4j.org
[51]: https://opensource.org/license/mit
[52]: https://logging.apache.org/log4j/2.x/
[53]: https://github.com/lightbend/scala-logging
[54]: http://www.apache.org/licenses/LICENSE-2.0.html
[55]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-common
[56]: http://www.eclipse.org/legal/epl-2.0
[57]: https://www.gnu.org/software/classpath/license.html
[58]: https://creativecommons.org/publicdomain/zero/1.0/
[59]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-client
[60]: http://www.eclipse.org/org/documents/edl-v10.php
[61]: https://opensource.org/licenses/BSD-2-Clause
[62]: https://asm.ow2.io/license.html
[63]: jquery.org/license
[64]: http://www.opensource.org/licenses/mit-license.php
[65]: https://www.w3.org/Consortium/Legal/copyright-documents-19990405
[66]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-server
[67]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet
[68]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet-core
[69]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-hk2
[70]: https://www.alluxio.io/alluxio-dora/alluxio-underfs/alluxio-underfs-local/
[71]: http://www.scalatest.org
[72]: https://github.com/scalatest/scalatestplus-mockito
[73]: https://github.com/mockito/mockito
[74]: https://opensource.org/licenses/MIT
[75]: http://hamcrest.org/JavaHamcrest/
[76]: https://raw.githubusercontent.com/hamcrest/JavaHamcrest/master/LICENSE
[77]: https://github.com/testcontainers/testcontainers-scala
[78]: https://java.testcontainers.org
[79]: http://opensource.org/licenses/MIT
[80]: https://github.com/exasol/exasol-testcontainers/
[81]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[82]: https://github.com/exasol/test-db-builder-java/
[83]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[84]: https://github.com/exasol/hamcrest-resultset-matcher/
[85]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[86]: https://www.jqno.nl/equalsverifier
[87]: https://junit.org/
[88]: https://www.eclipse.org/legal/epl-v20.html
[89]: https://github.com/exasol/maven-project-version-getter/
[90]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[91]: https://github.com/exasol/extension-manager/
[92]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[93]: http://logback.qos.ch/logback-classic
[94]: https://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
[95]: http://logback.qos.ch/logback-core
[96]: https://github.com/exasol/project-keeper/
[97]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[98]: http://www.scalastyle.org
[99]: https://github.com/diffplug/spotless
[100]: https://github.com/evis/scalafix-maven-plugin
[101]: https://www.mojohaus.org/exec-maven-plugin
[102]: https://maven.apache.org/plugins/maven-clean-plugin/
[103]: https://maven.apache.org/plugins/maven-install-plugin/
[104]: https://maven.apache.org/plugins/maven-resources-plugin/
[105]: https://maven.apache.org/plugins/maven-site-plugin/
[106]: https://docs.sonarsource.com/sonarqube-server/latest/extension-guide/developing-a-plugin/plugin-basics/sonar-scanner-maven/sonar-maven-plugin/
[107]: http://www.gnu.org/licenses/lgpl.txt
[108]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[109]: https://maven.apache.org/plugins/maven-compiler-plugin/
[110]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[111]: https://www.mojohaus.org/flatten-maven-plugin/
[112]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[113]: http://github.com/davidB/scala-maven-plugin
[114]: http://unlicense.org/
[115]: https://www.scalatest.org/user_guide/using_the_scalatest_maven_plugin
[116]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[117]: https://maven.apache.org/surefire/maven-surefire-plugin/
[118]: https://www.mojohaus.org/versions/versions-maven-plugin/
[119]: https://basepom.github.io/duplicate-finder-maven-plugin
[120]: https://maven.apache.org/plugins/maven-artifact-plugin/
[121]: https://maven.apache.org/plugins/maven-assembly-plugin/
[122]: https://maven.apache.org/plugins/maven-jar-plugin/
[123]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[124]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[125]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[126]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[127]: https://www.eclipse.org/legal/epl-2.0/
[128]: https://github.com/exasol/quality-summarizer-maven-plugin/
[129]: https://github.com/exasol/quality-summarizer-maven-plugin/blob/main/LICENSE
[130]: https://github.com/exasol/error-code-crawler-maven-plugin/
[131]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[132]: https://github.com/git-commit-id/git-commit-id-maven-plugin
[133]: http://www.gnu.org/licenses/lgpl-3.0.txt
[134]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.4.3.tgz
