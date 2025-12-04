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
| [Import Export UDF Common Scala][15]        | [MIT License][16]                                                                                                                                                                                   |
| [error-reporting-java][17]                  | [MIT License][18]                                                                                                                                                                                   |
| Apache Hadoop Common                        | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Commons IO][19]                     | [Apache-2.0][3]                                                                                                                                                                                     |
| [dnsjava][20]                               | [BSD-3-Clause][21]                                                                                                                                                                                  |
| [JSch][22]                                  | [Revised BSD][23]; [Revised BSD][24]; [ISC][25]                                                                                                                                                     |
| Apache Hadoop Amazon Web Services support   | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache ZooKeeper - Server][26]             | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| Apache Hadoop Azure support                 | [Apache-2.0][3]                                                                                                                                                                                     |
| Apache Hadoop Azure Data Lake support       | [Apache-2.0][3]                                                                                                                                                                                     |
| Apache Hadoop HDFS                          | [Apache-2.0][3]                                                                                                                                                                                     |
| Apache Hadoop HDFS Client                   | [Apache-2.0][3]                                                                                                                                                                                     |
| [Kotlin Stdlib][27]                         | [The Apache License, Version 2.0][7]                                                                                                                                                                |
| [Alluxio Core - Client - HDFS][28]          | [Apache License][29]                                                                                                                                                                                |
| [Metrics Core][30]                          | [Apache License 2.0][14]                                                                                                                                                                            |
| [Protocol Buffers [Core]][31]               | [BSD-3-Clause][21]                                                                                                                                                                                  |
| [gcs-connector-hadoop3][32]                 | [Apache License, Version 2.0][7]                                                                                                                                                                    |
| [Google OAuth Client Library for Java][33]  | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [ORC Core][34]                              | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| [aircompressor][35]                         | [Apache License 2.0][14]                                                                                                                                                                            |
| [Apache Avro][36]                           | [Apache-2.0][3]                                                                                                                                                                                     |
| lz4-java                                    |                                                                                                                                                                                                     |
| [Apache Commons Compress][37]               | [Apache-2.0][3]                                                                                                                                                                                     |
| [Nimbus JOSE+JWT][38]                       | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [delta-spark][39]                           | [Apache-2.0][40]                                                                                                                                                                                    |
| [Spark Project SQL][41]                     | [Apache-2.0][42]                                                                                                                                                                                    |
| [Apache Ivy][43]                            | [The Apache Software License, Version 2.0][7]                                                                                                                                                       |
| [janino][44]                                | [BSD-3-Clause][45]                                                                                                                                                                                  |
| [Parquet for Java][46]                      | [MIT License][47]                                                                                                                                                                                   |
| [JUL to SLF4J bridge][48]                   | [MIT License][49]                                                                                                                                                                                   |
| [Apache Log4j API][50]                      | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j 1.x Compatibility API][51]    | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j Core][52]                     | [Apache-2.0][3]                                                                                                                                                                                     |
| [scala-logging][53]                         | [Apache 2.0 License][42]                                                                                                                                                                            |
| [jersey-core-common][54]                    | [EPL 2.0][55]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][56]; [Apache License, 2.0][42]; [Public Domain][57]                                                      |
| [jersey-core-client][58]                    | [EPL 2.0][55]; [GPL2 w/ CPE][56]; [EDL 1.0][59]; [BSD 2-Clause][60]; [Apache License, 2.0][42]; [Public Domain][57]; [Modified BSD][61]; [jQuery license][62]; [MIT license][49]; [W3C license][63] |
| [jersey-core-server][64]                    | [EPL 2.0][55]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][56]; [Apache License, 2.0][42]; [Modified BSD][61]                                                       |
| [jersey-container-servlet][65]              | [EPL 2.0][55]; [GPL2 w/ CPE][56]; [EDL 1.0][59]; [BSD 2-Clause][60]; [Apache License, 2.0][42]; [Public Domain][57]; [Modified BSD][61]; [jQuery license][62]; [MIT license][49]; [W3C license][63] |
| [jersey-container-servlet-core][66]         | [EPL 2.0][55]; [GPL2 w/ CPE][56]; [EDL 1.0][59]; [BSD 2-Clause][60]; [Apache License, 2.0][42]; [Public Domain][57]; [Modified BSD][61]; [jQuery license][62]; [MIT license][49]; [W3C license][63] |
| [jersey-inject-hk2][67]                     | [EPL 2.0][55]; [GPL2 w/ CPE][56]; [EDL 1.0][59]; [BSD 2-Clause][60]; [Apache License, 2.0][42]; [Public Domain][57]; [Modified BSD][61]; [jQuery license][62]; [MIT license][49]; [W3C license][63] |

### Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [scalatest][68]                            | [the Apache License, ASL Version 2.0][40] |
| [scalatestplus-mockito][69]                | [Apache-2.0][40]                          |
| [mockito-core][70]                         | [MIT][71]                                 |
| [Hamcrest][72]                             | [BSD-3-Clause][73]                        |
| [testcontainers-scala-scalatest][74]       | [The MIT License (MIT)][71]               |
| [Testcontainers :: Localstack][75]         | [MIT][76]                                 |
| [Test containers for Exasol on Docker][77] | [MIT License][78]                         |
| [Test Database Builder for Java][79]       | [MIT License][80]                         |
| [Matcher for SQL Result Sets][81]          | [MIT License][82]                         |
| [EqualsVerifier \| release normal jar][83] | [Apache License, Version 2.0][3]          |
| [JUnit Jupiter API][84]                    | [Eclipse Public License v2.0][85]         |
| [Maven Project Version Getter][86]         | [MIT License][87]                         |
| [Extension integration tests library][88]  | [MIT License][89]                         |

### Runtime Dependencies

| Dependency                   | License                                                                       |
| ---------------------------- | ----------------------------------------------------------------------------- |
| [Logback Classic Module][90] | [Eclipse Public License - v 1.0][91]; [GNU Lesser General Public License][92] |
| [Logback Core Module][93]    | [Eclipse Public License - v 1.0][91]; [GNU Lesser General Public License][92] |

### Plugin Dependencies

| Dependency                                               | License                                       |
| -------------------------------------------------------- | --------------------------------------------- |
| [Project Keeper Maven plugin][94]                        | [The MIT License][95]                         |
| [Scalastyle Maven Plugin][96]                            | [Apache 2.0][42]                              |
| [spotless-maven-plugin][97]                              | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][98]                              | [BSD-3-Clause][21]                            |
| [Exec Maven Plugin][99]                                  | [Apache License 2][3]                         |
| [Apache Maven Clean Plugin][100]                         | [Apache-2.0][3]                               |
| [Apache Maven Install Plugin][101]                       | [Apache-2.0][3]                               |
| [Apache Maven Resources Plugin][102]                     | [Apache-2.0][3]                               |
| [Apache Maven Site Plugin][103]                          | [Apache-2.0][3]                               |
| [SonarQube Scanner for Maven][104]                       | [GNU LGPL 3][105]                             |
| [Apache Maven Toolchains Plugin][106]                    | [Apache-2.0][3]                               |
| [Apache Maven Compiler Plugin][107]                      | [Apache-2.0][3]                               |
| [Apache Maven Enforcer Plugin][108]                      | [Apache-2.0][3]                               |
| [Maven Flatten Plugin][109]                              | [Apache Software License][3]                  |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][110] | [ASL2][7]                                     |
| [scala-maven-plugin][111]                                | [Public domain (Unlicense)][112]              |
| [ScalaTest Maven Plugin][113]                            | [the Apache License, ASL Version 2.0][40]     |
| [Apache Maven Javadoc Plugin][114]                       | [Apache-2.0][3]                               |
| [Maven Surefire Plugin][115]                             | [Apache-2.0][3]                               |
| [Versions Maven Plugin][116]                             | [Apache License, Version 2.0][3]              |
| [duplicate-finder-maven-plugin Maven Mojo][117]          | [Apache License 2.0][42]                      |
| [Apache Maven Artifact Plugin][118]                      | [Apache-2.0][3]                               |
| [Apache Maven Assembly Plugin][119]                      | [Apache-2.0][3]                               |
| [Apache Maven JAR Plugin][120]                           | [Apache-2.0][3]                               |
| [Artifact reference checker and unifier][121]            | [MIT License][122]                            |
| [Maven Failsafe Plugin][123]                             | [Apache-2.0][3]                               |
| [JaCoCo :: Maven Plugin][124]                            | [EPL-2.0][125]                                |
| [Quality Summarizer Maven Plugin][126]                   | [MIT License][127]                            |
| [error-code-crawler-maven-plugin][128]                   | [MIT License][129]                            |
| [Git Commit Id Maven Plugin][130]                        | [GNU Lesser General Public License 3.0][131]  |

## Extension

### Compile Dependencies

| Dependency                                 | License |
| ------------------------------------------ | ------- |
| [@exasol/extension-manager-interface][132] | MIT     |

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
[15]: https://github.com/exasol/import-export-udf-common-scala/
[16]: https://github.com/exasol/import-export-udf-common-scala/blob/main/LICENSE
[17]: https://github.com/exasol/error-reporting-java/
[18]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[19]: https://commons.apache.org/proper/commons-io/
[20]: https://github.com/dnsjava/dnsjava
[21]: https://opensource.org/licenses/BSD-3-Clause
[22]: https://github.com/mwiede/jsch
[23]: https://github.com/mwiede/jsch/blob/master/LICENSE.txt
[24]: https://github.com/mwiede/jsch/blob/master/LICENSE.JZlib.txt
[25]: https://github.com/mwiede/jsch/blob/master/LICENSE.jBCrypt.txt
[26]: http://zookeeper.apache.org/zookeeper
[27]: https://kotlinlang.org/
[28]: https://www.alluxio.io/alluxio-dora/alluxio-core/alluxio-core-client/alluxio-core-client-hdfs/
[29]: https://github.com/alluxio/alluxio/blob/master/LICENSE
[30]: https://metrics.dropwizard.io/metrics-core
[31]: https://developers.google.com/protocol-buffers/protobuf-java/
[32]: https://github.com/GoogleCloudPlatform/BigData-interop/gcs-connector/
[33]: https://github.com/googleapis/google-oauth-java-client/google-oauth-client
[34]: https://orc.apache.org/orc-core
[35]: https://github.com/airlift/aircompressor
[36]: https://avro.apache.org
[37]: https://commons.apache.org/proper/commons-compress/
[38]: https://bitbucket.org/connect2id/nimbus-jose-jwt
[39]: https://delta.io/
[40]: http://www.apache.org/licenses/LICENSE-2.0
[41]: https://spark.apache.org/
[42]: http://www.apache.org/licenses/LICENSE-2.0.html
[43]: http://ant.apache.org/ivy/
[44]: http://janino-compiler.github.io/janino/
[45]: https://spdx.org/licenses/BSD-3-Clause.html
[46]: https://github.com/exasol/parquet-io-java/
[47]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[48]: http://www.slf4j.org
[49]: http://www.opensource.org/licenses/mit-license.php
[50]: https://logging.apache.org/log4j/2.x/log4j/log4j-api/
[51]: https://logging.apache.org/log4j/2.x/log4j/log4j-1.2-api/
[52]: https://logging.apache.org/log4j/2.x/log4j/log4j-core/
[53]: https://github.com/lightbend/scala-logging
[54]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-common
[55]: http://www.eclipse.org/legal/epl-2.0
[56]: https://www.gnu.org/software/classpath/license.html
[57]: https://creativecommons.org/publicdomain/zero/1.0/
[58]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-client
[59]: http://www.eclipse.org/org/documents/edl-v10.php
[60]: https://opensource.org/licenses/BSD-2-Clause
[61]: https://asm.ow2.io/license.html
[62]: jquery.org/license
[63]: https://www.w3.org/Consortium/Legal/copyright-documents-19990405
[64]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-server
[65]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet
[66]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet-core
[67]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-hk2
[68]: http://www.scalatest.org
[69]: https://github.com/scalatest/scalatestplus-mockito
[70]: https://github.com/mockito/mockito
[71]: https://opensource.org/licenses/MIT
[72]: http://hamcrest.org/JavaHamcrest/
[73]: https://raw.githubusercontent.com/hamcrest/JavaHamcrest/master/LICENSE
[74]: https://github.com/testcontainers/testcontainers-scala
[75]: https://java.testcontainers.org
[76]: http://opensource.org/licenses/MIT
[77]: https://github.com/exasol/exasol-testcontainers/
[78]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[79]: https://github.com/exasol/test-db-builder-java/
[80]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[81]: https://github.com/exasol/hamcrest-resultset-matcher/
[82]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[83]: https://www.jqno.nl/equalsverifier
[84]: https://junit.org/junit5/
[85]: https://www.eclipse.org/legal/epl-v20.html
[86]: https://github.com/exasol/maven-project-version-getter/
[87]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[88]: https://github.com/exasol/extension-manager/
[89]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[90]: http://logback.qos.ch/logback-classic
[91]: http://www.eclipse.org/legal/epl-v10.html
[92]: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
[93]: http://logback.qos.ch/logback-core
[94]: https://github.com/exasol/project-keeper/
[95]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[96]: http://www.scalastyle.org
[97]: https://github.com/diffplug/spotless
[98]: https://github.com/evis/scalafix-maven-plugin
[99]: https://www.mojohaus.org/exec-maven-plugin
[100]: https://maven.apache.org/plugins/maven-clean-plugin/
[101]: https://maven.apache.org/plugins/maven-install-plugin/
[102]: https://maven.apache.org/plugins/maven-resources-plugin/
[103]: https://maven.apache.org/plugins/maven-site-plugin/
[104]: http://docs.sonarqube.org/display/PLUG/Plugin+Library/sonar-scanner-maven/sonar-maven-plugin
[105]: http://www.gnu.org/licenses/lgpl.txt
[106]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[107]: https://maven.apache.org/plugins/maven-compiler-plugin/
[108]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[109]: https://www.mojohaus.org/flatten-maven-plugin/
[110]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[111]: http://github.com/davidB/scala-maven-plugin
[112]: http://unlicense.org/
[113]: https://www.scalatest.org/user_guide/using_the_scalatest_maven_plugin
[114]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[115]: https://maven.apache.org/surefire/maven-surefire-plugin/
[116]: https://www.mojohaus.org/versions/versions-maven-plugin/
[117]: https://basepom.github.io/duplicate-finder-maven-plugin
[118]: https://maven.apache.org/plugins/maven-artifact-plugin/
[119]: https://maven.apache.org/plugins/maven-assembly-plugin/
[120]: https://maven.apache.org/plugins/maven-jar-plugin/
[121]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[122]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[123]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[124]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[125]: https://www.eclipse.org/legal/epl-2.0/
[126]: https://github.com/exasol/quality-summarizer-maven-plugin/
[127]: https://github.com/exasol/quality-summarizer-maven-plugin/blob/main/LICENSE
[128]: https://github.com/exasol/error-code-crawler-maven-plugin/
[129]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[130]: https://github.com/git-commit-id/git-commit-id-maven-plugin
[131]: http://www.gnu.org/licenses/lgpl-3.0.txt
[132]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.4.3.tgz
