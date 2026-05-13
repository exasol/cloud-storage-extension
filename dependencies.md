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

| Dependency                                 | License                           |
| ------------------------------------------ | --------------------------------- |
| [mockito-core][67]                         | [MIT][68]                         |
| [Hamcrest][69]                             | [BSD-3-Clause][70]                |
| [Testcontainers :: Localstack][71]         | [MIT][72]                         |
| [Test containers for Exasol on Docker][73] | [MIT License][74]                 |
| [Test Database Builder for Java][75]       | [MIT License][76]                 |
| [Matcher for SQL Result Sets][77]          | [MIT License][78]                 |
| [EqualsVerifier \| release normal jar][79] | [Apache License, Version 2.0][3]  |
| [JUnit Jupiter (Aggregator)][80]           | [Eclipse Public License v2.0][81] |
| [Maven Project Version Getter][82]         | [MIT License][83]                 |
| [Extension integration tests library][84]  | [MIT License][85]                 |

### Runtime Dependencies

| Dependency                   | License                                                                       |
| ---------------------------- | ----------------------------------------------------------------------------- |
| [Logback Classic Module][86] | [Eclipse Public License - v 2.0][81]; [GNU Lesser General Public License][87] |
| [Logback Core Module][88]    | [Eclipse Public License - v 2.0][81]; [GNU Lesser General Public License][87] |

### Plugin Dependencies

| Dependency                                              | License                                      |
| ------------------------------------------------------- | -------------------------------------------- |
| [SonarQube Scanner for Maven][89]                       | [GNU LGPL 3][90]                             |
| [Apache Maven Toolchains Plugin][91]                    | [Apache-2.0][3]                              |
| [Apache Maven Compiler Plugin][92]                      | [Apache-2.0][3]                              |
| [Apache Maven Enforcer Plugin][93]                      | [Apache-2.0][3]                              |
| [Maven Flatten Plugin][94]                              | [Apache Software License][3]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][95] | [ASL2][7]                                    |
| [Apache Maven Javadoc Plugin][96]                       | [Apache-2.0][3]                              |
| [Maven Surefire Plugin][97]                             | [Apache-2.0][3]                              |
| [Versions Maven Plugin][98]                             | [Apache License, Version 2.0][3]             |
| [duplicate-finder-maven-plugin Maven Mojo][99]          | [Apache License 2.0][43]                     |
| [Apache Maven Artifact Plugin][100]                     | [Apache-2.0][3]                              |
| [Apache Maven Assembly Plugin][101]                     | [Apache-2.0][3]                              |
| [Apache Maven JAR Plugin][102]                          | [Apache-2.0][3]                              |
| [Artifact reference checker and unifier][103]           | [MIT License][104]                           |
| [Maven Failsafe Plugin][105]                            | [Apache-2.0][3]                              |
| [JaCoCo :: Maven Plugin][106]                           | [EPL-2.0][107]                               |
| [Quality Summarizer Maven Plugin][108]                  | [MIT License][109]                           |
| [error-code-crawler-maven-plugin][110]                  | [MIT License][111]                           |
| [Git Commit Id Maven Plugin][112]                       | [GNU Lesser General Public License 3.0][113] |
| [Project Keeper Maven plugin][114]                      | [The MIT License][115]                       |
| [Exec Maven Plugin][116]                                | [Apache License 2][3]                        |
| [Apache Maven Clean Plugin][117]                        | [Apache-2.0][3]                              |
| [Apache Maven Resources Plugin][118]                    | [Apache-2.0][3]                              |
| [Apache Maven Install Plugin][119]                      | [Apache-2.0][3]                              |
| [Apache Maven Site Plugin][120]                         | [Apache-2.0][3]                              |

## Extension

### Compile Dependencies

| Dependency                                 | License |
| ------------------------------------------ | ------- |
| [@exasol/extension-manager-interface][121] | MIT     |

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
[67]: https://github.com/mockito/mockito
[68]: https://opensource.org/licenses/MIT
[69]: http://hamcrest.org/JavaHamcrest/
[70]: https://raw.githubusercontent.com/hamcrest/JavaHamcrest/master/LICENSE
[71]: https://java.testcontainers.org
[72]: http://opensource.org/licenses/MIT
[73]: https://github.com/exasol/exasol-testcontainers/
[74]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[75]: https://github.com/exasol/test-db-builder-java/
[76]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[77]: https://github.com/exasol/hamcrest-resultset-matcher/
[78]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[79]: https://www.jqno.nl/equalsverifier
[80]: https://junit.org/junit5/
[81]: https://www.eclipse.org/legal/epl-v20.html
[82]: https://github.com/exasol/maven-project-version-getter/
[83]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[84]: https://github.com/exasol/extension-manager/
[85]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[86]: http://logback.qos.ch/logback-classic
[87]: https://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
[88]: http://logback.qos.ch/logback-core
[89]: https://docs.sonarsource.com/sonarqube-server/latest/extension-guide/developing-a-plugin/plugin-basics/sonar-scanner-maven/sonar-maven-plugin/
[90]: http://www.gnu.org/licenses/lgpl.txt
[91]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[92]: https://maven.apache.org/plugins/maven-compiler-plugin/
[93]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[94]: https://www.mojohaus.org/flatten-maven-plugin/
[95]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[96]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[97]: https://maven.apache.org/surefire/maven-surefire-plugin/
[98]: https://www.mojohaus.org/versions/versions-maven-plugin/
[99]: https://basepom.github.io/duplicate-finder-maven-plugin
[100]: https://maven.apache.org/plugins/maven-artifact-plugin/
[101]: https://maven.apache.org/plugins/maven-assembly-plugin/
[102]: https://maven.apache.org/plugins/maven-jar-plugin/
[103]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[104]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[105]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[106]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[107]: https://www.eclipse.org/legal/epl-2.0/
[108]: https://github.com/exasol/quality-summarizer-maven-plugin/
[109]: https://github.com/exasol/quality-summarizer-maven-plugin/blob/main/LICENSE
[110]: https://github.com/exasol/error-code-crawler-maven-plugin/
[111]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[112]: https://github.com/git-commit-id/git-commit-id-maven-plugin
[113]: http://www.gnu.org/licenses/lgpl-3.0.txt
[114]: https://github.com/exasol/project-keeper/
[115]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[116]: https://www.mojohaus.org/exec-maven-plugin
[117]: https://maven.apache.org/plugins/maven-clean-plugin/
[118]: https://maven.apache.org/plugins/maven-resources-plugin/
[119]: https://maven.apache.org/plugins/maven-install-plugin/
[120]: https://maven.apache.org/plugins/maven-site-plugin/
[121]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.4.3.tgz
