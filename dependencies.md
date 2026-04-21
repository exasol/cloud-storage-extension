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
| [Bouncy Castle Provider][15]                | [Bouncy Castle Licence][16]                                                                                                                                                                         |
| [Vert.x Core][17]                           | [The Apache Software License, Version 2.0][7]; [Eclipse Public License - v 2.0][18]                                                                                                                 |
| [Import Export UDF Common Scala][19]        | [MIT License][20]                                                                                                                                                                                   |
| [error-reporting-java][21]                  | [MIT License][22]                                                                                                                                                                                   |
| Apache Hadoop Common                        | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Commons IO][23]                     | [Apache-2.0][3]                                                                                                                                                                                     |
| [dnsjava][24]                               | [BSD-3-Clause][25]                                                                                                                                                                                  |
| [JSch][26]                                  | [Revised BSD][27]; [Revised BSD][28]; [ISC][29]                                                                                                                                                     |
| Apache Hadoop Amazon Web Services support   | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache ZooKeeper - Server][30]             | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| Apache Hadoop Azure support                 | [Apache-2.0][3]                                                                                                                                                                                     |
| Apache Hadoop Azure Data Lake support       | [Apache-2.0][3]                                                                                                                                                                                     |
| Apache Hadoop HDFS                          | [Apache-2.0][3]                                                                                                                                                                                     |
| Apache Hadoop HDFS Client                   | [Apache-2.0][3]                                                                                                                                                                                     |
| [Kotlin Stdlib][31]                         | [Apache-2.0][7]                                                                                                                                                                                     |
| [Alluxio Core - Client - HDFS][32]          | [Apache License][33]                                                                                                                                                                                |
| [Metrics Core][34]                          | [Apache License 2.0][14]                                                                                                                                                                            |
| [Protocol Buffers [Core]][35]               | [BSD-3-Clause][25]                                                                                                                                                                                  |
| [Protocol Buffers [Util]][36]               | [BSD-3-Clause][25]                                                                                                                                                                                  |
| [gcs-connector][37]                         | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| [Google OAuth Client Library for Java][38]  | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [ORC Core][39]                              | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Avro][40]                           | [Apache-2.0][3]                                                                                                                                                                                     |
| lz4-java                                    |                                                                                                                                                                                                     |
| [LZ4 Java Compression][41]                  | [Apache License, Version 2.0][1]                                                                                                                                                                    |
| [Apache Commons Compress][42]               | [Apache-2.0][3]                                                                                                                                                                                     |
| [Nimbus JOSE+JWT][43]                       | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [delta-spark][44]                           | [Apache-2.0][45]                                                                                                                                                                                    |
| [Spark Project SQL][46]                     | [Apache-2.0][14]                                                                                                                                                                                    |
| [Apache Ivy][47]                            | [The Apache Software License, Version 2.0][7]                                                                                                                                                       |
| [janino][48]                                | [BSD-3-Clause][49]                                                                                                                                                                                  |
| [Parquet for Java][50]                      | [MIT License][51]                                                                                                                                                                                   |
| [JUL to SLF4J bridge][52]                   | [MIT][53]                                                                                                                                                                                           |
| [Apache Log4j API][54]                      | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j 1.x Compatibility API][54]    | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j Core][54]                     | [Apache-2.0][3]                                                                                                                                                                                     |
| [scala-logging][55]                         | [Apache 2.0 License][56]                                                                                                                                                                            |
| [jersey-core-common][57]                    | [EPL 2.0][58]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][59]; [Apache License, 2.0][56]; [Public Domain][60]                                                      |
| [jersey-core-client][61]                    | [EPL 2.0][58]; [GPL2 w/ CPE][59]; [EDL 1.0][62]; [BSD 2-Clause][63]; [Apache License, 2.0][56]; [Public Domain][60]; [Modified BSD][64]; [jQuery license][65]; [MIT license][66]; [W3C license][67] |
| [jersey-core-server][68]                    | [EPL 2.0][58]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][59]; [Apache License, 2.0][56]; [Modified BSD][64]                                                       |
| [jersey-container-servlet][69]              | [EPL 2.0][58]; [GPL2 w/ CPE][59]; [EDL 1.0][62]; [BSD 2-Clause][63]; [Apache License, 2.0][56]; [Public Domain][60]; [Modified BSD][64]; [jQuery license][65]; [MIT license][66]; [W3C license][67] |
| [jersey-container-servlet-core][70]         | [EPL 2.0][58]; [GPL2 w/ CPE][59]; [EDL 1.0][62]; [BSD 2-Clause][63]; [Apache License, 2.0][56]; [Public Domain][60]; [Modified BSD][64]; [jQuery license][65]; [MIT license][66]; [W3C license][67] |
| [jersey-inject-hk2][71]                     | [EPL 2.0][58]; [GPL2 w/ CPE][59]; [EDL 1.0][62]; [BSD 2-Clause][63]; [Apache License, 2.0][56]; [Public Domain][60]; [Modified BSD][64]; [jQuery license][65]; [MIT license][66]; [W3C license][67] |

### Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [Alluxio Under File System - Local FS][72] | [Apache License][33]                      |
| [scalatest][73]                            | [the Apache License, ASL Version 2.0][45] |
| [scalatestplus-mockito][74]                | [Apache-2.0][45]                          |
| [mockito-core][75]                         | [MIT][76]                                 |
| [Hamcrest][77]                             | [BSD-3-Clause][78]                        |
| [testcontainers-scala-scalatest][79]       | [The MIT License (MIT)][76]               |
| [Testcontainers :: Localstack][80]         | [MIT][81]                                 |
| [Test containers for Exasol on Docker][82] | [MIT License][83]                         |
| [Test Database Builder for Java][84]       | [MIT License][85]                         |
| [Matcher for SQL Result Sets][86]          | [MIT License][87]                         |
| [EqualsVerifier \| release normal jar][88] | [Apache License, Version 2.0][3]          |
| [JUnit Jupiter API][89]                    | [Eclipse Public License v2.0][90]         |
| [Maven Project Version Getter][91]         | [MIT License][92]                         |
| [Extension integration tests library][93]  | [MIT License][94]                         |

### Runtime Dependencies

| Dependency                   | License                                                                       |
| ---------------------------- | ----------------------------------------------------------------------------- |
| [Logback Classic Module][95] | [Eclipse Public License - v 2.0][90]; [GNU Lesser General Public License][96] |
| [Logback Core Module][97]    | [Eclipse Public License - v 2.0][90]; [GNU Lesser General Public License][96] |

### Plugin Dependencies

| Dependency                                               | License                                       |
| -------------------------------------------------------- | --------------------------------------------- |
| [Project Keeper Maven plugin][98]                        | [The MIT License][99]                         |
| [Scalastyle Maven Plugin][100]                           | [Apache 2.0][56]                              |
| [spotless-maven-plugin][101]                             | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][102]                             | [BSD-3-Clause][25]                            |
| [Exec Maven Plugin][103]                                 | [Apache License 2][3]                         |
| [Apache Maven Clean Plugin][104]                         | [Apache-2.0][3]                               |
| [Apache Maven Install Plugin][105]                       | [Apache-2.0][3]                               |
| [Apache Maven Resources Plugin][106]                     | [Apache-2.0][3]                               |
| [Apache Maven Site Plugin][107]                          | [Apache-2.0][3]                               |
| [SonarQube Scanner for Maven][108]                       | [GNU LGPL 3][109]                             |
| [Apache Maven Toolchains Plugin][110]                    | [Apache-2.0][3]                               |
| [Apache Maven Compiler Plugin][111]                      | [Apache-2.0][3]                               |
| [Apache Maven Enforcer Plugin][112]                      | [Apache-2.0][3]                               |
| [Maven Flatten Plugin][113]                              | [Apache Software License][3]                  |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][114] | [ASL2][7]                                     |
| [scala-maven-plugin][115]                                | [Public domain (Unlicense)][116]              |
| [ScalaTest Maven Plugin][117]                            | [the Apache License, ASL Version 2.0][45]     |
| [Apache Maven Javadoc Plugin][118]                       | [Apache-2.0][3]                               |
| [Maven Surefire Plugin][119]                             | [Apache-2.0][3]                               |
| [Versions Maven Plugin][120]                             | [Apache License, Version 2.0][3]              |
| [duplicate-finder-maven-plugin Maven Mojo][121]          | [Apache License 2.0][56]                      |
| [Apache Maven Artifact Plugin][122]                      | [Apache-2.0][3]                               |
| [Apache Maven Assembly Plugin][123]                      | [Apache-2.0][3]                               |
| [Apache Maven JAR Plugin][124]                           | [Apache-2.0][3]                               |
| [Artifact reference checker and unifier][125]            | [MIT License][126]                            |
| [Maven Failsafe Plugin][127]                             | [Apache-2.0][3]                               |
| [JaCoCo :: Maven Plugin][128]                            | [EPL-2.0][129]                                |
| [Quality Summarizer Maven Plugin][130]                   | [MIT License][131]                            |
| [error-code-crawler-maven-plugin][132]                   | [MIT License][133]                            |
| [Git Commit Id Maven Plugin][134]                        | [GNU Lesser General Public License 3.0][135]  |

## Extension

### Compile Dependencies

| Dependency                                 | License |
| ------------------------------------------ | ------- |
| [@exasol/extension-manager-interface][136] | MIT     |

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
[15]: https://www.bouncycastle.org/download/bouncy-castle-java/
[16]: https://www.bouncycastle.org/licence.html
[17]: https://github.com/vert-x3/vertx-parent/vertx-core
[18]: http://www.eclipse.org/legal/epl-v20.html
[19]: https://github.com/exasol/import-export-udf-common-scala/
[20]: https://github.com/exasol/import-export-udf-common-scala/blob/main/LICENSE
[21]: https://github.com/exasol/error-reporting-java/
[22]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[23]: https://commons.apache.org/proper/commons-io/
[24]: https://github.com/dnsjava/dnsjava
[25]: https://opensource.org/licenses/BSD-3-Clause
[26]: https://github.com/mwiede/jsch
[27]: https://github.com/mwiede/jsch/blob/master/LICENSE.txt
[28]: https://github.com/mwiede/jsch/blob/master/LICENSE.JZlib.txt
[29]: https://github.com/mwiede/jsch/blob/master/LICENSE.jBCrypt.txt
[30]: http://zookeeper.apache.org/zookeeper
[31]: https://kotlinlang.org/
[32]: https://www.alluxio.io/alluxio-dora/alluxio-core/alluxio-core-client/alluxio-core-client-hdfs/
[33]: https://github.com/alluxio/alluxio/blob/master/LICENSE
[34]: https://metrics.dropwizard.io/metrics-core
[35]: https://developers.google.com/protocol-buffers/protobuf-java/
[36]: https://developers.google.com/protocol-buffers/protobuf-java-util/
[37]: https://github.com/GoogleCloudDataproc/hadoop-connectors/gcs-connector
[38]: https://github.com/googleapis/google-oauth-java-client/google-oauth-client
[39]: https://orc.apache.org/orc-core
[40]: https://avro.apache.org
[41]: https://github.com/yawkat/lz4-java
[42]: https://commons.apache.org/proper/commons-compress/
[43]: https://bitbucket.org/connect2id/nimbus-jose-jwt
[44]: https://delta.io/
[45]: http://www.apache.org/licenses/LICENSE-2.0
[46]: https://spark.apache.org/
[47]: http://ant.apache.org/ivy/
[48]: http://janino-compiler.github.io/janino/
[49]: https://spdx.org/licenses/BSD-3-Clause.html
[50]: https://github.com/exasol/parquet-io-java/
[51]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[52]: http://www.slf4j.org
[53]: https://opensource.org/license/mit
[54]: https://logging.apache.org/log4j/2.x/
[55]: https://github.com/lightbend/scala-logging
[56]: http://www.apache.org/licenses/LICENSE-2.0.html
[57]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-common
[58]: http://www.eclipse.org/legal/epl-2.0
[59]: https://www.gnu.org/software/classpath/license.html
[60]: https://creativecommons.org/publicdomain/zero/1.0/
[61]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-client
[62]: http://www.eclipse.org/org/documents/edl-v10.php
[63]: https://opensource.org/licenses/BSD-2-Clause
[64]: https://asm.ow2.io/license.html
[65]: jquery.org/license
[66]: http://www.opensource.org/licenses/mit-license.php
[67]: https://www.w3.org/Consortium/Legal/copyright-documents-19990405
[68]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-server
[69]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet
[70]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet-core
[71]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-hk2
[72]: https://www.alluxio.io/alluxio-dora/alluxio-underfs/alluxio-underfs-local/
[73]: http://www.scalatest.org
[74]: https://github.com/scalatest/scalatestplus-mockito
[75]: https://github.com/mockito/mockito
[76]: https://opensource.org/licenses/MIT
[77]: http://hamcrest.org/JavaHamcrest/
[78]: https://raw.githubusercontent.com/hamcrest/JavaHamcrest/master/LICENSE
[79]: https://github.com/testcontainers/testcontainers-scala
[80]: https://java.testcontainers.org
[81]: http://opensource.org/licenses/MIT
[82]: https://github.com/exasol/exasol-testcontainers/
[83]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[84]: https://github.com/exasol/test-db-builder-java/
[85]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[86]: https://github.com/exasol/hamcrest-resultset-matcher/
[87]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[88]: https://www.jqno.nl/equalsverifier
[89]: https://junit.org/
[90]: https://www.eclipse.org/legal/epl-v20.html
[91]: https://github.com/exasol/maven-project-version-getter/
[92]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[93]: https://github.com/exasol/extension-manager/
[94]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[95]: http://logback.qos.ch/logback-classic
[96]: https://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
[97]: http://logback.qos.ch/logback-core
[98]: https://github.com/exasol/project-keeper/
[99]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[100]: http://www.scalastyle.org
[101]: https://github.com/diffplug/spotless
[102]: https://github.com/evis/scalafix-maven-plugin
[103]: https://www.mojohaus.org/exec-maven-plugin
[104]: https://maven.apache.org/plugins/maven-clean-plugin/
[105]: https://maven.apache.org/plugins/maven-install-plugin/
[106]: https://maven.apache.org/plugins/maven-resources-plugin/
[107]: https://maven.apache.org/plugins/maven-site-plugin/
[108]: https://docs.sonarsource.com/sonarqube-server/latest/extension-guide/developing-a-plugin/plugin-basics/sonar-scanner-maven/sonar-maven-plugin/
[109]: http://www.gnu.org/licenses/lgpl.txt
[110]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[111]: https://maven.apache.org/plugins/maven-compiler-plugin/
[112]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[113]: https://www.mojohaus.org/flatten-maven-plugin/
[114]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[115]: http://github.com/davidB/scala-maven-plugin
[116]: http://unlicense.org/
[117]: https://www.scalatest.org/user_guide/using_the_scalatest_maven_plugin
[118]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[119]: https://maven.apache.org/surefire/maven-surefire-plugin/
[120]: https://www.mojohaus.org/versions/versions-maven-plugin/
[121]: https://basepom.github.io/duplicate-finder-maven-plugin
[122]: https://maven.apache.org/plugins/maven-artifact-plugin/
[123]: https://maven.apache.org/plugins/maven-assembly-plugin/
[124]: https://maven.apache.org/plugins/maven-jar-plugin/
[125]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[126]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[127]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[128]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[129]: https://www.eclipse.org/legal/epl-2.0/
[130]: https://github.com/exasol/quality-summarizer-maven-plugin/
[131]: https://github.com/exasol/quality-summarizer-maven-plugin/blob/main/LICENSE
[132]: https://github.com/exasol/error-code-crawler-maven-plugin/
[133]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[134]: https://github.com/git-commit-id/git-commit-id-maven-plugin
[135]: http://www.gnu.org/licenses/lgpl-3.0.txt
[136]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.4.3.tgz
