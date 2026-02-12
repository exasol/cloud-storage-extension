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
| [Protocol Buffers [Util]][32]               | [BSD-3-Clause][21]                                                                                                                                                                                  |
| [gcs-connector-hadoop3][33]                 | [Apache License, Version 2.0][7]                                                                                                                                                                    |
| [Google OAuth Client Library for Java][34]  | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [ORC Core][35]                              | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| [Apache Avro][36]                           | [Apache-2.0][3]                                                                                                                                                                                     |
| lz4-java                                    |                                                                                                                                                                                                     |
| [LZ4 Java Compression][37]                  | [Apache License, Version 2.0][1]                                                                                                                                                                    |
| [Apache Commons Compress][38]               | [Apache-2.0][3]                                                                                                                                                                                     |
| [Nimbus JOSE+JWT][39]                       | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [delta-spark][40]                           | [Apache-2.0][41]                                                                                                                                                                                    |
| [Spark Project SQL][42]                     | [Apache-2.0][43]                                                                                                                                                                                    |
| [Apache Ivy][44]                            | [The Apache Software License, Version 2.0][7]                                                                                                                                                       |
| [janino][45]                                | [BSD-3-Clause][46]                                                                                                                                                                                  |
| [Parquet for Java][47]                      | [MIT License][48]                                                                                                                                                                                   |
| [JUL to SLF4J bridge][49]                   | [MIT License][50]                                                                                                                                                                                   |
| [Apache Log4j API][51]                      | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j 1.x Compatibility API][51]    | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j Core][51]                     | [Apache-2.0][3]                                                                                                                                                                                     |
| [scala-logging][52]                         | [Apache 2.0 License][43]                                                                                                                                                                            |
| [jersey-core-common][53]                    | [EPL 2.0][54]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][55]; [Apache License, 2.0][43]; [Public Domain][56]                                                      |
| [jersey-core-client][57]                    | [EPL 2.0][54]; [GPL2 w/ CPE][55]; [EDL 1.0][58]; [BSD 2-Clause][59]; [Apache License, 2.0][43]; [Public Domain][56]; [Modified BSD][60]; [jQuery license][61]; [MIT license][50]; [W3C license][62] |
| [jersey-core-server][63]                    | [EPL 2.0][54]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][55]; [Apache License, 2.0][43]; [Modified BSD][60]                                                       |
| [jersey-container-servlet][64]              | [EPL 2.0][54]; [GPL2 w/ CPE][55]; [EDL 1.0][58]; [BSD 2-Clause][59]; [Apache License, 2.0][43]; [Public Domain][56]; [Modified BSD][60]; [jQuery license][61]; [MIT license][50]; [W3C license][62] |
| [jersey-container-servlet-core][65]         | [EPL 2.0][54]; [GPL2 w/ CPE][55]; [EDL 1.0][58]; [BSD 2-Clause][59]; [Apache License, 2.0][43]; [Public Domain][56]; [Modified BSD][60]; [jQuery license][61]; [MIT license][50]; [W3C license][62] |
| [jersey-inject-hk2][66]                     | [EPL 2.0][54]; [GPL2 w/ CPE][55]; [EDL 1.0][58]; [BSD 2-Clause][59]; [Apache License, 2.0][43]; [Public Domain][56]; [Modified BSD][60]; [jQuery license][61]; [MIT license][50]; [W3C license][62] |

### Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [scalatest][67]                            | [the Apache License, ASL Version 2.0][41] |
| [scalatestplus-mockito][68]                | [Apache-2.0][41]                          |
| [mockito-core][69]                         | [MIT][70]                                 |
| [Hamcrest][71]                             | [BSD-3-Clause][72]                        |
| [testcontainers-scala-scalatest][73]       | [The MIT License (MIT)][70]               |
| [Testcontainers :: Localstack][74]         | [MIT][75]                                 |
| [Test containers for Exasol on Docker][76] | [MIT License][77]                         |
| [Test Database Builder for Java][78]       | [MIT License][79]                         |
| [Matcher for SQL Result Sets][80]          | [MIT License][81]                         |
| [EqualsVerifier \| release normal jar][82] | [Apache License, Version 2.0][3]          |
| [JUnit Jupiter API][83]                    | [Eclipse Public License v2.0][84]         |
| [Maven Project Version Getter][85]         | [MIT License][86]                         |
| [Extension integration tests library][87]  | [MIT License][88]                         |

### Runtime Dependencies

| Dependency                   | License                                                                       |
| ---------------------------- | ----------------------------------------------------------------------------- |
| [Logback Classic Module][89] | [Eclipse Public License - v 2.0][84]; [GNU Lesser General Public License][90] |
| [Logback Core Module][91]    | [Eclipse Public License - v 2.0][84]; [GNU Lesser General Public License][90] |

### Plugin Dependencies

| Dependency                                               | License                                       |
| -------------------------------------------------------- | --------------------------------------------- |
| [Project Keeper Maven plugin][92]                        | [The MIT License][93]                         |
| [Scalastyle Maven Plugin][94]                            | [Apache 2.0][43]                              |
| [spotless-maven-plugin][95]                              | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][96]                              | [BSD-3-Clause][21]                            |
| [Exec Maven Plugin][97]                                  | [Apache License 2][3]                         |
| [Apache Maven Clean Plugin][98]                          | [Apache-2.0][3]                               |
| [Apache Maven Install Plugin][99]                        | [Apache-2.0][3]                               |
| [Apache Maven Resources Plugin][100]                     | [Apache-2.0][3]                               |
| [Apache Maven Site Plugin][101]                          | [Apache-2.0][3]                               |
| [SonarQube Scanner for Maven][102]                       | [GNU LGPL 3][103]                             |
| [Apache Maven Toolchains Plugin][104]                    | [Apache-2.0][3]                               |
| [Apache Maven Compiler Plugin][105]                      | [Apache-2.0][3]                               |
| [Apache Maven Enforcer Plugin][106]                      | [Apache-2.0][3]                               |
| [Maven Flatten Plugin][107]                              | [Apache Software License][3]                  |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][108] | [ASL2][7]                                     |
| [scala-maven-plugin][109]                                | [Public domain (Unlicense)][110]              |
| [ScalaTest Maven Plugin][111]                            | [the Apache License, ASL Version 2.0][41]     |
| [Apache Maven Javadoc Plugin][112]                       | [Apache-2.0][3]                               |
| [Maven Surefire Plugin][113]                             | [Apache-2.0][3]                               |
| [Versions Maven Plugin][114]                             | [Apache License, Version 2.0][3]              |
| [duplicate-finder-maven-plugin Maven Mojo][115]          | [Apache License 2.0][43]                      |
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
[32]: https://developers.google.com/protocol-buffers/protobuf-java-util/
[33]: https://github.com/GoogleCloudPlatform/BigData-interop/gcs-connector/
[34]: https://github.com/googleapis/google-oauth-java-client/google-oauth-client
[35]: https://orc.apache.org/orc-core
[36]: https://avro.apache.org
[37]: https://github.com/yawkat/lz4-java
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
[51]: https://logging.apache.org/log4j/2.x/
[52]: https://github.com/lightbend/scala-logging
[53]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-common
[54]: http://www.eclipse.org/legal/epl-2.0
[55]: https://www.gnu.org/software/classpath/license.html
[56]: https://creativecommons.org/publicdomain/zero/1.0/
[57]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-client
[58]: http://www.eclipse.org/org/documents/edl-v10.php
[59]: https://opensource.org/licenses/BSD-2-Clause
[60]: https://asm.ow2.io/license.html
[61]: jquery.org/license
[62]: https://www.w3.org/Consortium/Legal/copyright-documents-19990405
[63]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-server
[64]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet
[65]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet-core
[66]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-hk2
[67]: http://www.scalatest.org
[68]: https://github.com/scalatest/scalatestplus-mockito
[69]: https://github.com/mockito/mockito
[70]: https://opensource.org/licenses/MIT
[71]: http://hamcrest.org/JavaHamcrest/
[72]: https://raw.githubusercontent.com/hamcrest/JavaHamcrest/master/LICENSE
[73]: https://github.com/testcontainers/testcontainers-scala
[74]: https://java.testcontainers.org
[75]: http://opensource.org/licenses/MIT
[76]: https://github.com/exasol/exasol-testcontainers/
[77]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[78]: https://github.com/exasol/test-db-builder-java/
[79]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[80]: https://github.com/exasol/hamcrest-resultset-matcher/
[81]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[82]: https://www.jqno.nl/equalsverifier
[83]: https://junit.org/junit5/
[84]: https://www.eclipse.org/legal/epl-v20.html
[85]: https://github.com/exasol/maven-project-version-getter/
[86]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[87]: https://github.com/exasol/extension-manager/
[88]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[89]: http://logback.qos.ch/logback-classic
[90]: https://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
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
[102]: https://docs.sonarsource.com/sonarqube-server/latest/extension-guide/developing-a-plugin/plugin-basics/sonar-scanner-maven/sonar-maven-plugin/
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
