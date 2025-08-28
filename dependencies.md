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
| [Netty/Codec/HTTP2][10]                     | [Apache License, Version 2.0][1]                                                                                                                                                                    |
| [Apache Commons BeanUtils][11]              | [Apache-2.0][3]                                                                                                                                                                                     |
| [snappy-java][12]                           | [Apache-2.0][13]                                                                                                                                                                                    |
| [Import Export UDF Common Scala][14]        | [MIT License][15]                                                                                                                                                                                   |
| [error-reporting-java][16]                  | [MIT License][17]                                                                                                                                                                                   |
| Apache Hadoop Common                        | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Commons IO][18]                     | [Apache-2.0][3]                                                                                                                                                                                     |
| [dnsjava][19]                               | [BSD-3-Clause][20]                                                                                                                                                                                  |
| [JSch][21]                                  | [Revised BSD][22]; [Revised BSD][23]; [ISC][24]                                                                                                                                                     |
| Apache Hadoop Amazon Web Services support   | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache ZooKeeper - Server][25]             | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| Apache Hadoop Azure support                 | [Apache-2.0][3]                                                                                                                                                                                     |
| Apache Hadoop Azure Data Lake support       | [Apache-2.0][3]                                                                                                                                                                                     |
| Apache Hadoop HDFS                          | [Apache-2.0][3]                                                                                                                                                                                     |
| Apache Hadoop HDFS Client                   | [Apache-2.0][3]                                                                                                                                                                                     |
| [Kotlin Stdlib][26]                         | [The Apache License, Version 2.0][7]                                                                                                                                                                |
| [Alluxio Core - Client - HDFS][27]          | [Apache License][28]                                                                                                                                                                                |
| [Metrics Core][29]                          | [Apache License 2.0][13]                                                                                                                                                                            |
| [Protocol Buffers [Core]][30]               | [BSD-3-Clause][20]                                                                                                                                                                                  |
| [gcs-connector-hadoop3][31]                 | [Apache License, Version 2.0][7]                                                                                                                                                                    |
| [Google OAuth Client Library for Java][32]  | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [ORC Core][33]                              | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| [aircompressor][34]                         | [Apache License 2.0][13]                                                                                                                                                                            |
| [Apache Avro][35]                           | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Commons Compress][36]               | [Apache-2.0][3]                                                                                                                                                                                     |
| [Nimbus JOSE+JWT][37]                       | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [delta-core][38]                            | [Apache-2.0][39]                                                                                                                                                                                    |
| [Spark Project SQL][40]                     | [Apache 2.0 License][41]                                                                                                                                                                            |
| [Apache Ivy][42]                            | [The Apache Software License, Version 2.0][7]                                                                                                                                                       |
| [janino][43]                                | [BSD-3-Clause][44]                                                                                                                                                                                  |
| [Parquet for Java][45]                      | [MIT License][46]                                                                                                                                                                                   |
| [JUL to SLF4J bridge][47]                   | [MIT License][48]                                                                                                                                                                                   |
| [Apache Log4j API][49]                      | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j 1.x Compatibility API][50]    | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j Core][51]                     | [Apache-2.0][3]                                                                                                                                                                                     |
| [scala-logging][52]                         | [Apache 2.0 License][41]                                                                                                                                                                            |
| [jersey-core-common][53]                    | [EPL 2.0][54]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][55]; [Apache License, 2.0][41]; [Public Domain][56]                                                      |
| [jersey-core-client][57]                    | [EPL 2.0][54]; [GPL2 w/ CPE][55]; [EDL 1.0][58]; [BSD 2-Clause][59]; [Apache License, 2.0][41]; [Public Domain][56]; [Modified BSD][60]; [jQuery license][61]; [MIT license][48]; [W3C license][62] |
| [jersey-core-server][63]                    | [EPL 2.0][54]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][55]; [Apache License, 2.0][41]; [Modified BSD][60]                                                       |
| [jersey-container-servlet][64]              | [EPL 2.0][54]; [GPL2 w/ CPE][55]; [EDL 1.0][58]; [BSD 2-Clause][59]; [Apache License, 2.0][41]; [Public Domain][56]; [Modified BSD][60]; [jQuery license][61]; [MIT license][48]; [W3C license][62] |
| [jersey-container-servlet-core][65]         | [EPL 2.0][54]; [GPL2 w/ CPE][55]; [EDL 1.0][58]; [BSD 2-Clause][59]; [Apache License, 2.0][41]; [Public Domain][56]; [Modified BSD][60]; [jQuery license][61]; [MIT license][48]; [W3C license][62] |
| [jersey-inject-hk2][66]                     | [EPL 2.0][54]; [GPL2 w/ CPE][55]; [EDL 1.0][58]; [BSD 2-Clause][59]; [Apache License, 2.0][41]; [Public Domain][56]; [Modified BSD][60]; [jQuery license][61]; [MIT license][48]; [W3C license][62] |

### Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [scalatest][67]                            | [the Apache License, ASL Version 2.0][39] |
| [scalatestplus-mockito][68]                | [Apache-2.0][39]                          |
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
| [Logback Classic Module][89] | [Eclipse Public License - v 1.0][90]; [GNU Lesser General Public License][91] |
| [Logback Core Module][92]    | [Eclipse Public License - v 1.0][90]; [GNU Lesser General Public License][91] |

### Plugin Dependencies

| Dependency                                               | License                                       |
| -------------------------------------------------------- | --------------------------------------------- |
| [Project Keeper Maven plugin][93]                        | [The MIT License][94]                         |
| [Scalastyle Maven Plugin][95]                            | [Apache 2.0][41]                              |
| [spotless-maven-plugin][96]                              | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][97]                              | [BSD-3-Clause][20]                            |
| [Exec Maven Plugin][98]                                  | [Apache License 2][3]                         |
| [Apache Maven Clean Plugin][99]                          | [Apache-2.0][3]                               |
| [Apache Maven Install Plugin][100]                       | [Apache-2.0][3]                               |
| [Apache Maven Resources Plugin][101]                     | [Apache-2.0][3]                               |
| [Apache Maven Site Plugin][102]                          | [Apache-2.0][3]                               |
| [SonarQube Scanner for Maven][103]                       | [GNU LGPL 3][104]                             |
| [Apache Maven Toolchains Plugin][105]                    | [Apache-2.0][3]                               |
| [Apache Maven Compiler Plugin][106]                      | [Apache-2.0][3]                               |
| [Apache Maven Enforcer Plugin][107]                      | [Apache-2.0][3]                               |
| [Maven Flatten Plugin][108]                              | [Apache Software Licenese][3]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][109] | [ASL2][7]                                     |
| [scala-maven-plugin][110]                                | [Public domain (Unlicense)][111]              |
| [ScalaTest Maven Plugin][112]                            | [the Apache License, ASL Version 2.0][39]     |
| [Apache Maven Javadoc Plugin][113]                       | [Apache-2.0][3]                               |
| [Maven Surefire Plugin][114]                             | [Apache-2.0][3]                               |
| [Versions Maven Plugin][115]                             | [Apache License, Version 2.0][3]              |
| [duplicate-finder-maven-plugin Maven Mojo][116]          | [Apache License 2.0][41]                      |
| [Apache Maven Artifact Plugin][117]                      | [Apache-2.0][3]                               |
| [Apache Maven Assembly Plugin][118]                      | [Apache-2.0][3]                               |
| [Apache Maven JAR Plugin][119]                           | [Apache-2.0][3]                               |
| [Artifact reference checker and unifier][120]            | [MIT License][121]                            |
| [Maven Failsafe Plugin][122]                             | [Apache-2.0][3]                               |
| [JaCoCo :: Maven Plugin][123]                            | [EPL-2.0][124]                                |
| [Quality Summarizer Maven Plugin][125]                   | [MIT License][126]                            |
| [error-code-crawler-maven-plugin][127]                   | [MIT License][128]                            |
| [Git Commit Id Maven Plugin][129]                        | [GNU Lesser General Public License 3.0][130]  |

## Extension

### Compile Dependencies

| Dependency                                 | License |
| ------------------------------------------ | ------- |
| [@exasol/extension-manager-interface][131] | MIT     |

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
[10]: https://netty.io/netty-codec-http2/
[11]: https://commons.apache.org/proper/commons-beanutils
[12]: https://github.com/xerial/snappy-java
[13]: https://www.apache.org/licenses/LICENSE-2.0.html
[14]: https://github.com/exasol/import-export-udf-common-scala/
[15]: https://github.com/exasol/import-export-udf-common-scala/blob/main/LICENSE
[16]: https://github.com/exasol/error-reporting-java/
[17]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[18]: https://commons.apache.org/proper/commons-io/
[19]: https://github.com/dnsjava/dnsjava
[20]: https://opensource.org/licenses/BSD-3-Clause
[21]: https://github.com/mwiede/jsch
[22]: https://github.com/mwiede/jsch/blob/master/LICENSE.txt
[23]: https://github.com/mwiede/jsch/blob/master/LICENSE.JZlib.txt
[24]: https://github.com/mwiede/jsch/blob/master/LICENSE.jBCrypt.txt
[25]: http://zookeeper.apache.org/zookeeper
[26]: https://kotlinlang.org/
[27]: https://www.alluxio.io/alluxio-dora/alluxio-core/alluxio-core-client/alluxio-core-client-hdfs/
[28]: https://github.com/alluxio/alluxio/blob/master/LICENSE
[29]: https://metrics.dropwizard.io/metrics-core
[30]: https://developers.google.com/protocol-buffers/protobuf-java/
[31]: https://github.com/GoogleCloudPlatform/BigData-interop/gcs-connector/
[32]: https://github.com/googleapis/google-oauth-java-client/google-oauth-client
[33]: https://orc.apache.org/orc-core
[34]: https://github.com/airlift/aircompressor
[35]: https://avro.apache.org
[36]: https://commons.apache.org/proper/commons-compress/
[37]: https://bitbucket.org/connect2id/nimbus-jose-jwt
[38]: https://delta.io/
[39]: http://www.apache.org/licenses/LICENSE-2.0
[40]: https://spark.apache.org/
[41]: http://www.apache.org/licenses/LICENSE-2.0.html
[42]: http://ant.apache.org/ivy/
[43]: http://janino-compiler.github.io/janino/
[44]: https://spdx.org/licenses/BSD-3-Clause.html
[45]: https://github.com/exasol/parquet-io-java/
[46]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[47]: http://www.slf4j.org
[48]: http://www.opensource.org/licenses/mit-license.php
[49]: https://logging.apache.org/log4j/2.x/log4j/log4j-api/
[50]: https://logging.apache.org/log4j/2.x/log4j/log4j-1.2-api/
[51]: https://logging.apache.org/log4j/2.x/log4j/log4j-core/
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
[90]: http://www.eclipse.org/legal/epl-v10.html
[91]: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
[92]: http://logback.qos.ch/logback-core
[93]: https://github.com/exasol/project-keeper/
[94]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[95]: http://www.scalastyle.org
[96]: https://github.com/diffplug/spotless
[97]: https://github.com/evis/scalafix-maven-plugin
[98]: https://www.mojohaus.org/exec-maven-plugin
[99]: https://maven.apache.org/plugins/maven-clean-plugin/
[100]: https://maven.apache.org/plugins/maven-install-plugin/
[101]: https://maven.apache.org/plugins/maven-resources-plugin/
[102]: https://maven.apache.org/plugins/maven-site-plugin/
[103]: http://docs.sonarqube.org/display/PLUG/Plugin+Library/sonar-scanner-maven/sonar-maven-plugin
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
[117]: https://maven.apache.org/plugins/maven-artifact-plugin/
[118]: https://maven.apache.org/plugins/maven-assembly-plugin/
[119]: https://maven.apache.org/plugins/maven-jar-plugin/
[120]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[121]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[122]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[123]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[124]: https://www.eclipse.org/legal/epl-2.0/
[125]: https://github.com/exasol/quality-summarizer-maven-plugin/
[126]: https://github.com/exasol/quality-summarizer-maven-plugin/blob/main/LICENSE
[127]: https://github.com/exasol/error-code-crawler-maven-plugin/
[128]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[129]: https://github.com/git-commit-id/git-commit-id-maven-plugin
[130]: http://www.gnu.org/licenses/lgpl-3.0.txt
[131]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.4.3.tgz
