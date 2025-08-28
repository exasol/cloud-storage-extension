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
| [Netty/Codec/HTTP2][12]                     | [Apache License, Version 2.0][1]                                                                                                                                                                    |
| [Apache Commons BeanUtils][13]              | [Apache-2.0][3]                                                                                                                                                                                     |
| [snappy-java][14]                           | [Apache-2.0][15]                                                                                                                                                                                    |
| [Import Export UDF Common Scala][16]        | [MIT License][17]                                                                                                                                                                                   |
| [error-reporting-java][18]                  | [MIT License][19]                                                                                                                                                                                   |
| Apache Hadoop Common                        | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Commons IO][20]                     | [Apache-2.0][3]                                                                                                                                                                                     |
| [dnsjava][21]                               | [BSD-3-Clause][22]                                                                                                                                                                                  |
| [JSch][23]                                  | [Revised BSD][24]; [Revised BSD][25]; [ISC][26]                                                                                                                                                     |
| Apache Hadoop Amazon Web Services support   | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache ZooKeeper - Server][27]             | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| Apache Hadoop Azure support                 | [Apache-2.0][3]                                                                                                                                                                                     |
| Apache Hadoop Azure Data Lake support       | [Apache-2.0][3]                                                                                                                                                                                     |
| Apache Hadoop HDFS                          | [Apache-2.0][3]                                                                                                                                                                                     |
| Apache Hadoop HDFS Client                   | [Apache-2.0][3]                                                                                                                                                                                     |
| [Kotlin Stdlib][28]                         | [The Apache License, Version 2.0][7]                                                                                                                                                                |
| [Alluxio Core - Client - HDFS][29]          | [Apache License][30]                                                                                                                                                                                |
| [Metrics Core][31]                          | [Apache License 2.0][15]                                                                                                                                                                            |
| [Protocol Buffers [Core]][32]               | [BSD-3-Clause][22]                                                                                                                                                                                  |
| [gcs-connector-hadoop3][33]                 | [Apache License, Version 2.0][7]                                                                                                                                                                    |
| [Google OAuth Client Library for Java][34]  | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [ORC Core][35]                              | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| [aircompressor][36]                         | [Apache License 2.0][15]                                                                                                                                                                            |
| [Apache Avro][37]                           | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Commons Compress][38]               | [Apache-2.0][3]                                                                                                                                                                                     |
| [Nimbus JOSE+JWT][39]                       | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [delta-core][40]                            | [Apache-2.0][41]                                                                                                                                                                                    |
| [Spark Project SQL][42]                     | [Apache 2.0 License][43]                                                                                                                                                                            |
| [Apache Ivy][44]                            | [The Apache Software License, Version 2.0][7]                                                                                                                                                       |
| [janino][45]                                | [BSD-3-Clause][46]                                                                                                                                                                                  |
| [Parquet for Java][47]                      | [MIT License][48]                                                                                                                                                                                   |
| [JUL to SLF4J bridge][49]                   | [MIT License][50]                                                                                                                                                                                   |
| [Apache Log4j API][51]                      | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j 1.x Compatibility API][52]    | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j Core][53]                     | [Apache-2.0][3]                                                                                                                                                                                     |
| [scala-logging][54]                         | [Apache 2.0 License][43]                                                                                                                                                                            |
| [jersey-core-common][55]                    | [EPL 2.0][56]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][57]; [Apache License, 2.0][43]; [Public Domain][58]                                                      |
| [jersey-core-client][59]                    | [EPL 2.0][56]; [GPL2 w/ CPE][57]; [EDL 1.0][60]; [BSD 2-Clause][61]; [Apache License, 2.0][43]; [Public Domain][58]; [Modified BSD][62]; [jQuery license][63]; [MIT license][50]; [W3C license][64] |
| [jersey-core-server][65]                    | [EPL 2.0][56]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][57]; [Apache License, 2.0][43]; [Modified BSD][62]                                                       |
| [jersey-container-servlet][66]              | [EPL 2.0][56]; [GPL2 w/ CPE][57]; [EDL 1.0][60]; [BSD 2-Clause][61]; [Apache License, 2.0][43]; [Public Domain][58]; [Modified BSD][62]; [jQuery license][63]; [MIT license][50]; [W3C license][64] |
| [jersey-container-servlet-core][67]         | [EPL 2.0][56]; [GPL2 w/ CPE][57]; [EDL 1.0][60]; [BSD 2-Clause][61]; [Apache License, 2.0][43]; [Public Domain][58]; [Modified BSD][62]; [jQuery license][63]; [MIT license][50]; [W3C license][64] |
| [jersey-inject-hk2][68]                     | [EPL 2.0][56]; [GPL2 w/ CPE][57]; [EDL 1.0][60]; [BSD 2-Clause][61]; [Apache License, 2.0][43]; [Public Domain][58]; [Modified BSD][62]; [jQuery license][63]; [MIT license][50]; [W3C license][64] |

### Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [scalatest][69]                            | [the Apache License, ASL Version 2.0][41] |
| [scalatestplus-mockito][70]                | [Apache-2.0][41]                          |
| [mockito-core][71]                         | [MIT][72]                                 |
| [Hamcrest][73]                             | [BSD-3-Clause][74]                        |
| [testcontainers-scala-scalatest][75]       | [The MIT License (MIT)][72]               |
| [Testcontainers :: Localstack][76]         | [MIT][77]                                 |
| [Test containers for Exasol on Docker][78] | [MIT License][79]                         |
| [Test Database Builder for Java][80]       | [MIT License][81]                         |
| [Matcher for SQL Result Sets][82]          | [MIT License][83]                         |
| [EqualsVerifier \| release normal jar][84] | [Apache License, Version 2.0][3]          |
| [JUnit Jupiter API][85]                    | [Eclipse Public License v2.0][86]         |
| [Maven Project Version Getter][87]         | [MIT License][88]                         |
| [Extension integration tests library][89]  | [MIT License][90]                         |

### Runtime Dependencies

| Dependency                   | License                                                                       |
| ---------------------------- | ----------------------------------------------------------------------------- |
| [Logback Classic Module][91] | [Eclipse Public License - v 1.0][92]; [GNU Lesser General Public License][93] |
| [Logback Core Module][94]    | [Eclipse Public License - v 1.0][92]; [GNU Lesser General Public License][93] |

### Plugin Dependencies

| Dependency                                               | License                                       |
| -------------------------------------------------------- | --------------------------------------------- |
| [Project Keeper Maven plugin][95]                        | [The MIT License][96]                         |
| [Scalastyle Maven Plugin][97]                            | [Apache 2.0][43]                              |
| [spotless-maven-plugin][98]                              | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][99]                              | [BSD-3-Clause][22]                            |
| [Exec Maven Plugin][100]                                 | [Apache License 2][3]                         |
| [Apache Maven Clean Plugin][101]                         | [Apache-2.0][3]                               |
| [Apache Maven Install Plugin][102]                       | [Apache-2.0][3]                               |
| [Apache Maven Resources Plugin][103]                     | [Apache-2.0][3]                               |
| [Apache Maven Site Plugin][104]                          | [Apache-2.0][3]                               |
| [SonarQube Scanner for Maven][105]                       | [GNU LGPL 3][106]                             |
| [Apache Maven Toolchains Plugin][107]                    | [Apache-2.0][3]                               |
| [Apache Maven Compiler Plugin][108]                      | [Apache-2.0][3]                               |
| [Apache Maven Enforcer Plugin][109]                      | [Apache-2.0][3]                               |
| [Maven Flatten Plugin][110]                              | [Apache Software Licenese][3]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][111] | [ASL2][7]                                     |
| [scala-maven-plugin][112]                                | [Public domain (Unlicense)][113]              |
| [ScalaTest Maven Plugin][114]                            | [the Apache License, ASL Version 2.0][41]     |
| [Apache Maven Javadoc Plugin][115]                       | [Apache-2.0][3]                               |
| [Maven Surefire Plugin][116]                             | [Apache-2.0][3]                               |
| [Versions Maven Plugin][117]                             | [Apache License, Version 2.0][3]              |
| [duplicate-finder-maven-plugin Maven Mojo][118]          | [Apache License 2.0][43]                      |
| [Apache Maven Artifact Plugin][119]                      | [Apache-2.0][3]                               |
| [Apache Maven Assembly Plugin][120]                      | [Apache-2.0][3]                               |
| [Apache Maven JAR Plugin][121]                           | [Apache-2.0][3]                               |
| [Artifact reference checker and unifier][122]            | [MIT License][123]                            |
| [Maven Failsafe Plugin][124]                             | [Apache-2.0][3]                               |
| [JaCoCo :: Maven Plugin][125]                            | [EPL-2.0][126]                                |
| [Quality Summarizer Maven Plugin][127]                   | [MIT License][128]                            |
| [error-code-crawler-maven-plugin][129]                   | [MIT License][130]                            |
| [Git Commit Id Maven Plugin][131]                        | [GNU Lesser General Public License 3.0][132]  |

## Extension

### Compile Dependencies

| Dependency                                 | License |
| ------------------------------------------ | ------- |
| [@exasol/extension-manager-interface][133] | MIT     |

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
[12]: https://netty.io/netty-codec-http2/
[13]: https://commons.apache.org/proper/commons-beanutils
[14]: https://github.com/xerial/snappy-java
[15]: https://www.apache.org/licenses/LICENSE-2.0.html
[16]: https://github.com/exasol/import-export-udf-common-scala/
[17]: https://github.com/exasol/import-export-udf-common-scala/blob/main/LICENSE
[18]: https://github.com/exasol/error-reporting-java/
[19]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[20]: https://commons.apache.org/proper/commons-io/
[21]: https://github.com/dnsjava/dnsjava
[22]: https://opensource.org/licenses/BSD-3-Clause
[23]: https://github.com/mwiede/jsch
[24]: https://github.com/mwiede/jsch/blob/master/LICENSE.txt
[25]: https://github.com/mwiede/jsch/blob/master/LICENSE.JZlib.txt
[26]: https://github.com/mwiede/jsch/blob/master/LICENSE.jBCrypt.txt
[27]: http://zookeeper.apache.org/zookeeper
[28]: https://kotlinlang.org/
[29]: https://www.alluxio.io/alluxio-dora/alluxio-core/alluxio-core-client/alluxio-core-client-hdfs/
[30]: https://github.com/alluxio/alluxio/blob/master/LICENSE
[31]: https://metrics.dropwizard.io/metrics-core
[32]: https://developers.google.com/protocol-buffers/protobuf-java/
[33]: https://github.com/GoogleCloudPlatform/BigData-interop/gcs-connector/
[34]: https://github.com/googleapis/google-oauth-java-client/google-oauth-client
[35]: https://orc.apache.org/orc-core
[36]: https://github.com/airlift/aircompressor
[37]: https://avro.apache.org
[38]: https://commons.apache.org/proper/commons-compress/
[39]: https://bitbucket.org/connect2id/nimbus-jose-jwt
[40]: https://delta.io/
[41]: http://www.apache.org/licenses/LICENSE-2.0
[42]: https://spark.apache.org/
[43]: http://www.apache.org/licenses/LICENSE-2.0.html
[44]: http://ant.apache.org/ivy/
[45]: http://janino-compiler.github.io/janino/
[46]: https://spdx.org/licenses/BSD-3-Clause.html
[47]: https://github.com/exasol/parquet-io-java/
[48]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[49]: http://www.slf4j.org
[50]: http://www.opensource.org/licenses/mit-license.php
[51]: https://logging.apache.org/log4j/2.x/log4j/log4j-api/
[52]: https://logging.apache.org/log4j/2.x/log4j/log4j-1.2-api/
[53]: https://logging.apache.org/log4j/2.x/log4j/log4j-core/
[54]: https://github.com/lightbend/scala-logging
[55]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-common
[56]: http://www.eclipse.org/legal/epl-2.0
[57]: https://www.gnu.org/software/classpath/license.html
[58]: https://creativecommons.org/publicdomain/zero/1.0/
[59]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-client
[60]: http://www.eclipse.org/org/documents/edl-v10.php
[61]: https://opensource.org/licenses/BSD-2-Clause
[62]: https://asm.ow2.io/license.html
[63]: jquery.org/license
[64]: https://www.w3.org/Consortium/Legal/copyright-documents-19990405
[65]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-server
[66]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet
[67]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet-core
[68]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-hk2
[69]: http://www.scalatest.org
[70]: https://github.com/scalatest/scalatestplus-mockito
[71]: https://github.com/mockito/mockito
[72]: https://opensource.org/licenses/MIT
[73]: http://hamcrest.org/JavaHamcrest/
[74]: https://raw.githubusercontent.com/hamcrest/JavaHamcrest/master/LICENSE
[75]: https://github.com/testcontainers/testcontainers-scala
[76]: https://java.testcontainers.org
[77]: http://opensource.org/licenses/MIT
[78]: https://github.com/exasol/exasol-testcontainers/
[79]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[80]: https://github.com/exasol/test-db-builder-java/
[81]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[82]: https://github.com/exasol/hamcrest-resultset-matcher/
[83]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[84]: https://www.jqno.nl/equalsverifier
[85]: https://junit.org/junit5/
[86]: https://www.eclipse.org/legal/epl-v20.html
[87]: https://github.com/exasol/maven-project-version-getter/
[88]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[89]: https://github.com/exasol/extension-manager/
[90]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[91]: http://logback.qos.ch/logback-classic
[92]: http://www.eclipse.org/legal/epl-v10.html
[93]: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
[94]: http://logback.qos.ch/logback-core
[95]: https://github.com/exasol/project-keeper/
[96]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[97]: http://www.scalastyle.org
[98]: https://github.com/diffplug/spotless
[99]: https://github.com/evis/scalafix-maven-plugin
[100]: https://www.mojohaus.org/exec-maven-plugin
[101]: https://maven.apache.org/plugins/maven-clean-plugin/
[102]: https://maven.apache.org/plugins/maven-install-plugin/
[103]: https://maven.apache.org/plugins/maven-resources-plugin/
[104]: https://maven.apache.org/plugins/maven-site-plugin/
[105]: http://docs.sonarqube.org/display/PLUG/Plugin+Library/sonar-scanner-maven/sonar-maven-plugin
[106]: http://www.gnu.org/licenses/lgpl.txt
[107]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[108]: https://maven.apache.org/plugins/maven-compiler-plugin/
[109]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[110]: https://www.mojohaus.org/flatten-maven-plugin/
[111]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[112]: http://github.com/davidB/scala-maven-plugin
[113]: http://unlicense.org/
[114]: https://www.scalatest.org/user_guide/using_the_scalatest_maven_plugin
[115]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[116]: https://maven.apache.org/surefire/maven-surefire-plugin/
[117]: https://www.mojohaus.org/versions/versions-maven-plugin/
[118]: https://basepom.github.io/duplicate-finder-maven-plugin
[119]: https://maven.apache.org/plugins/maven-artifact-plugin/
[120]: https://maven.apache.org/plugins/maven-assembly-plugin/
[121]: https://maven.apache.org/plugins/maven-jar-plugin/
[122]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[123]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[124]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[125]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[126]: https://www.eclipse.org/legal/epl-2.0/
[127]: https://github.com/exasol/quality-summarizer-maven-plugin/
[128]: https://github.com/exasol/quality-summarizer-maven-plugin/blob/main/LICENSE
[129]: https://github.com/exasol/error-code-crawler-maven-plugin/
[130]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[131]: https://github.com/git-commit-id/git-commit-id-maven-plugin
[132]: http://www.gnu.org/licenses/lgpl-3.0.txt
[133]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.4.3.tgz
