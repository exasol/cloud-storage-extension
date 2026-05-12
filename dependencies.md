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
| [jackson-databind][17]                      | [The Apache Software License, Version 2.0][7]                                                                                                                                                       |
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
| [Metrics Core][31]                          | [Apache License 2.0][14]                                                                                                                                                                            |
| [Protocol Buffers [Core]][32]               | [BSD-3-Clause][22]                                                                                                                                                                                  |
| [Protocol Buffers [Util]][33]               | [BSD-3-Clause][22]                                                                                                                                                                                  |
| [gcs-connector-hadoop3][34]                 | [Apache License, Version 2.0][7]                                                                                                                                                                    |
| [Google OAuth Client Library for Java][35]  | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [ORC Core][36]                              | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| [Apache Avro][37]                           | [Apache-2.0][3]                                                                                                                                                                                     |
| lz4-java                                    |                                                                                                                                                                                                     |
| [LZ4 Java Compression][38]                  | [Apache License, Version 2.0][1]                                                                                                                                                                    |
| [Apache Commons Compress][39]               | [Apache-2.0][3]                                                                                                                                                                                     |
| [Nimbus JOSE+JWT][40]                       | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [delta-spark][41]                           | [Apache-2.0][42]                                                                                                                                                                                    |
| [Spark Project SQL][43]                     | [Apache-2.0][44]                                                                                                                                                                                    |
| [Apache Ivy][45]                            | [The Apache Software License, Version 2.0][7]                                                                                                                                                       |
| [janino][46]                                | [BSD-3-Clause][47]                                                                                                                                                                                  |
| [Parquet for Java][48]                      | [MIT License][49]                                                                                                                                                                                   |
| [JUL to SLF4J bridge][50]                   | [MIT License][51]                                                                                                                                                                                   |
| [Apache Log4j API][52]                      | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j 1.x Compatibility API][52]    | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j Core][52]                     | [Apache-2.0][3]                                                                                                                                                                                     |
| [scala-logging][53]                         | [Apache 2.0 License][44]                                                                                                                                                                            |
| [jersey-core-common][54]                    | [EPL 2.0][55]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][56]; [Apache License, 2.0][44]; [Public Domain][57]                                                      |
| [jersey-core-client][58]                    | [EPL 2.0][55]; [GPL2 w/ CPE][56]; [EDL 1.0][59]; [BSD 2-Clause][60]; [Apache License, 2.0][44]; [Public Domain][57]; [Modified BSD][61]; [jQuery license][62]; [MIT license][51]; [W3C license][63] |
| [jersey-core-server][64]                    | [EPL 2.0][55]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][56]; [Apache License, 2.0][44]; [Modified BSD][61]                                                       |
| [jersey-container-servlet][65]              | [EPL 2.0][55]; [GPL2 w/ CPE][56]; [EDL 1.0][59]; [BSD 2-Clause][60]; [Apache License, 2.0][44]; [Public Domain][57]; [Modified BSD][61]; [jQuery license][62]; [MIT license][51]; [W3C license][63] |
| [jersey-container-servlet-core][66]         | [EPL 2.0][55]; [GPL2 w/ CPE][56]; [EDL 1.0][59]; [BSD 2-Clause][60]; [Apache License, 2.0][44]; [Public Domain][57]; [Modified BSD][61]; [jQuery license][62]; [MIT license][51]; [W3C license][63] |
| [jersey-inject-hk2][67]                     | [EPL 2.0][55]; [GPL2 w/ CPE][56]; [EDL 1.0][59]; [BSD 2-Clause][60]; [Apache License, 2.0][44]; [Public Domain][57]; [Modified BSD][61]; [jQuery license][62]; [MIT license][51]; [W3C license][63] |

### Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [scalatest][68]                            | [the Apache License, ASL Version 2.0][42] |
| [scalatestplus-mockito][69]                | [Apache-2.0][42]                          |
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
| [Logback Classic Module][90] | [Eclipse Public License - v 2.0][85]; [GNU Lesser General Public License][91] |
| [Logback Core Module][92]    | [Eclipse Public License - v 2.0][85]; [GNU Lesser General Public License][91] |

### Plugin Dependencies

| Dependency                                              | License                                       |
| ------------------------------------------------------- | --------------------------------------------- |
| [SonarQube Scanner for Maven][93]                       | [GNU LGPL 3][94]                              |
| [Apache Maven Toolchains Plugin][95]                    | [Apache-2.0][3]                               |
| [Apache Maven Compiler Plugin][96]                      | [Apache-2.0][3]                               |
| [Apache Maven Enforcer Plugin][97]                      | [Apache-2.0][3]                               |
| [Maven Flatten Plugin][98]                              | [Apache Software License][3]                  |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][99] | [ASL2][7]                                     |
| [scala-maven-plugin][100]                               | [Public domain (Unlicense)][101]              |
| [ScalaTest Maven Plugin][102]                           | [the Apache License, ASL Version 2.0][42]     |
| [Apache Maven Javadoc Plugin][103]                      | [Apache-2.0][3]                               |
| [Maven Surefire Plugin][104]                            | [Apache-2.0][3]                               |
| [Versions Maven Plugin][105]                            | [Apache License, Version 2.0][3]              |
| [duplicate-finder-maven-plugin Maven Mojo][106]         | [Apache License 2.0][44]                      |
| [Apache Maven Artifact Plugin][107]                     | [Apache-2.0][3]                               |
| [Apache Maven Assembly Plugin][108]                     | [Apache-2.0][3]                               |
| [Apache Maven JAR Plugin][109]                          | [Apache-2.0][3]                               |
| [Artifact reference checker and unifier][110]           | [MIT License][111]                            |
| [Maven Failsafe Plugin][112]                            | [Apache-2.0][3]                               |
| [JaCoCo :: Maven Plugin][113]                           | [EPL-2.0][114]                                |
| [Quality Summarizer Maven Plugin][115]                  | [MIT License][116]                            |
| [error-code-crawler-maven-plugin][117]                  | [MIT License][118]                            |
| [Git Commit Id Maven Plugin][119]                       | [GNU Lesser General Public License 3.0][120]  |
| [Project Keeper Maven plugin][121]                      | [The MIT License][122]                        |
| [spotless-maven-plugin][123]                            | [The Apache Software License, Version 2.0][3] |
| [Exec Maven Plugin][124]                                | [Apache License 2][3]                         |
| [Apache Maven Clean Plugin][125]                        | [Apache-2.0][3]                               |
| [Apache Maven Resources Plugin][126]                    | [Apache-2.0][3]                               |
| [Apache Maven Install Plugin][127]                      | [Apache-2.0][3]                               |
| [Apache Maven Site Plugin][128]                         | [Apache-2.0][3]                               |

## Extension

### Compile Dependencies

| Dependency                                 | License |
| ------------------------------------------ | ------- |
| [@exasol/extension-manager-interface][129] | MIT     |

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
[17]: http://github.com/FasterXML/jackson
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
[33]: https://developers.google.com/protocol-buffers/protobuf-java-util/
[34]: https://github.com/GoogleCloudPlatform/BigData-interop/gcs-connector/
[35]: https://github.com/googleapis/google-oauth-java-client/google-oauth-client
[36]: https://orc.apache.org/orc-core
[37]: https://avro.apache.org
[38]: https://github.com/yawkat/lz4-java
[39]: https://commons.apache.org/proper/commons-compress/
[40]: https://bitbucket.org/connect2id/nimbus-jose-jwt
[41]: https://delta.io/
[42]: http://www.apache.org/licenses/LICENSE-2.0
[43]: https://spark.apache.org/
[44]: http://www.apache.org/licenses/LICENSE-2.0.html
[45]: http://ant.apache.org/ivy/
[46]: http://janino-compiler.github.io/janino/
[47]: https://spdx.org/licenses/BSD-3-Clause.html
[48]: https://github.com/exasol/parquet-io-java/
[49]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[50]: http://www.slf4j.org
[51]: http://www.opensource.org/licenses/mit-license.php
[52]: https://logging.apache.org/log4j/2.x/
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
[91]: https://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
[92]: http://logback.qos.ch/logback-core
[93]: https://docs.sonarsource.com/sonarqube-server/latest/extension-guide/developing-a-plugin/plugin-basics/sonar-scanner-maven/sonar-maven-plugin/
[94]: http://www.gnu.org/licenses/lgpl.txt
[95]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[96]: https://maven.apache.org/plugins/maven-compiler-plugin/
[97]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[98]: https://www.mojohaus.org/flatten-maven-plugin/
[99]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[100]: http://github.com/davidB/scala-maven-plugin
[101]: http://unlicense.org/
[102]: https://www.scalatest.org/user_guide/using_the_scalatest_maven_plugin
[103]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[104]: https://maven.apache.org/surefire/maven-surefire-plugin/
[105]: https://www.mojohaus.org/versions/versions-maven-plugin/
[106]: https://basepom.github.io/duplicate-finder-maven-plugin
[107]: https://maven.apache.org/plugins/maven-artifact-plugin/
[108]: https://maven.apache.org/plugins/maven-assembly-plugin/
[109]: https://maven.apache.org/plugins/maven-jar-plugin/
[110]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[111]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[112]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[113]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[114]: https://www.eclipse.org/legal/epl-2.0/
[115]: https://github.com/exasol/quality-summarizer-maven-plugin/
[116]: https://github.com/exasol/quality-summarizer-maven-plugin/blob/main/LICENSE
[117]: https://github.com/exasol/error-code-crawler-maven-plugin/
[118]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[119]: https://github.com/git-commit-id/git-commit-id-maven-plugin
[120]: http://www.gnu.org/licenses/lgpl-3.0.txt
[121]: https://github.com/exasol/project-keeper/
[122]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[123]: https://github.com/diffplug/spotless
[124]: https://www.mojohaus.org/exec-maven-plugin
[125]: https://maven.apache.org/plugins/maven-clean-plugin/
[126]: https://maven.apache.org/plugins/maven-resources-plugin/
[127]: https://maven.apache.org/plugins/maven-install-plugin/
[128]: https://maven.apache.org/plugins/maven-site-plugin/
[129]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.4.3.tgz
