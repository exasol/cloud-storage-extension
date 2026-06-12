<!-- @formatter:off -->
# Dependencies

## Cloud Storage Extension

### Compile Dependencies

| Dependency                                  | License                                         |
| ------------------------------------------- | ----------------------------------------------- |
| [Scala Library][0]                          | [Apache-2.0][1]                                 |
| [Apache Commons Lang][2]                    | [Apache-2.0][3]                                 |
| [Gson][4]                                   | [Apache-2.0][3]                                 |
| [Apache Commons Configuration][5]           | [Apache-2.0][3]                                 |
| [Guava: Google Core Libraries for Java][6]  | [Apache License, Version 2.0][7]                |
| [AWS Java SDK :: Services :: Amazon S3][8]  | [Apache License, Version 2.0][9]                |
| [AWS Java SDK :: S3 :: Transfer Manager][8] | [Apache License, Version 2.0][9]                |
| [io.grpc:grpc-netty][10]                    | [Apache 2.0][11]                                |
| [Apache Commons BeanUtils][12]              | [Apache-2.0][3]                                 |
| [snappy-java][13]                           | [Apache-2.0][14]                                |
| [Import Export UDF Common Scala][15]        | [MIT License][16]                               |
| [error-reporting-java][17]                  | [MIT License][18]                               |
| Apache Hadoop Common                        | [Apache-2.0][3]                                 |
| [Apache Commons IO][19]                     | [Apache-2.0][3]                                 |
| [dnsjava][20]                               | [BSD-3-Clause][21]                              |
| [SLF4J API Module][22]                      | [MIT][23]                                       |
| [JSch][24]                                  | [Revised BSD][25]; [Revised BSD][26]; [ISC][27] |
| Apache Hadoop Amazon Web Services support   | [Apache-2.0][3]                                 |
| [Apache ZooKeeper - Server][28]             | [Apache License, Version 2.0][3]                |
| Apache Hadoop Azure support                 | [Apache-2.0][3]                                 |
| Apache Hadoop Azure Data Lake support       | [Apache-2.0][3]                                 |
| Apache Hadoop HDFS                          | [Apache-2.0][3]                                 |
| Apache Hadoop HDFS Client                   | [Apache-2.0][3]                                 |
| [Alluxio Core - Client - HDFS][29]          | [Apache License][30]                            |
| [Metrics Core][31]                          | [Apache License 2.0][14]                        |
| [Protocol Buffers [Core]][32]               | [BSD-3-Clause][21]                              |
| [Protocol Buffers [Util]][33]               | [BSD-3-Clause][21]                              |
| [gcs-connector][34]                         | [Apache License, Version 2.0][3]                |
| [Google OAuth Client Library for Java][35]  | [The Apache Software License, Version 2.0][3]   |
| [ORC Core][36]                              | [Apache License, Version 2.0][3]                |
| [Apache Avro][37]                           | [Apache-2.0][3]                                 |
| lz4-java                                    |                                                 |
| [LZ4 Java Compression][38]                  | [Apache License, Version 2.0][1]                |
| [Apache Commons Compress][39]               | [Apache-2.0][3]                                 |
| [Nimbus JOSE+JWT][40]                       | [The Apache Software License, Version 2.0][3]   |
| [delta-spark][41]                           | [Apache-2.0][42]                                |
| [Spark Project SQL][43]                     | [Apache-2.0][44]                                |
| [Apache Ivy][45]                            | [The Apache Software License, Version 2.0][7]   |
| [janino][46]                                | [BSD-3-Clause][47]                              |
| [Parquet for Java][48]                      | [MIT License][49]                               |
| [JUL to SLF4J bridge][22]                   | [MIT][23]                                       |
| [Apache Log4j API][50]                      | [Apache-2.0][3]                                 |
| [Apache Log4j 1.x Compatibility API][50]    | [Apache-2.0][3]                                 |
| [Apache Log4j Core][50]                     | [Apache-2.0][3]                                 |
| [scala-logging][51]                         | [Apache 2.0 License][44]                        |

### Test Dependencies

| Dependency                                 | License                           |
| ------------------------------------------ | --------------------------------- |
| [mockito-core][52]                         | [MIT][53]                         |
| [Hamcrest][54]                             | [BSD-3-Clause][55]                |
| [Testcontainers :: Localstack][56]         | [MIT][57]                         |
| [Test containers for Exasol on Docker][58] | [MIT License][59]                 |
| [Test Database Builder for Java][60]       | [MIT License][61]                 |
| [Matcher for SQL Result Sets][62]          | [MIT License][63]                 |
| [EqualsVerifier \| release normal jar][64] | [Apache License, Version 2.0][3]  |
| [JUnit Jupiter (Aggregator)][65]           | [Eclipse Public License v2.0][66] |
| [Maven Project Version Getter][67]         | [MIT License][68]                 |
| [Extension integration tests library][69]  | [MIT License][70]                 |

### Runtime Dependencies

| Dependency                                   | License                            |
| -------------------------------------------- | ---------------------------------- |
| [AWS Java SDK :: HTTP Clients :: Apache][71] | [Apache License, Version 2.0][9]   |
| [Logback Classic Module][72]                 | [EPL-2.0][66]; [LGPL-2.1-only][73] |
| [Logback Core Module][74]                    | [EPL-2.0][66]; [LGPL-2.1-only][73] |
| [aircompressor][75]                          | [Apache License 2.0][14]           |

### Plugin Dependencies

| Dependency                                              | License                                      |
| ------------------------------------------------------- | -------------------------------------------- |
| [SonarQube Scanner for Maven][76]                       | [GNU LGPL 3][77]                             |
| [Apache Maven Toolchains Plugin][78]                    | [Apache-2.0][3]                              |
| [Apache Maven Compiler Plugin][79]                      | [Apache-2.0][3]                              |
| [Apache Maven Enforcer Plugin][80]                      | [Apache-2.0][3]                              |
| [Maven Flatten Plugin][81]                              | [Apache Software License][3]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][82] | [ASL2][7]                                    |
| [Apache Maven Javadoc Plugin][83]                       | [Apache-2.0][3]                              |
| [Maven Surefire Plugin][84]                             | [Apache-2.0][3]                              |
| [Versions Maven Plugin][85]                             | [Apache License, Version 2.0][3]             |
| [duplicate-finder-maven-plugin Maven Mojo][86]          | [Apache License 2.0][44]                     |
| [Apache Maven Artifact Plugin][87]                      | [Apache-2.0][3]                              |
| [Apache Maven Assembly Plugin][88]                      | [Apache-2.0][3]                              |
| [Apache Maven JAR Plugin][89]                           | [Apache-2.0][3]                              |
| [Artifact reference checker and unifier][90]            | [MIT License][91]                            |
| [Maven Failsafe Plugin][92]                             | [Apache-2.0][3]                              |
| [JaCoCo :: Maven Plugin][93]                            | [EPL-2.0][94]                                |
| [Quality Summarizer Maven Plugin][95]                   | [MIT License][96]                            |
| [error-code-crawler-maven-plugin][97]                   | [MIT License][98]                            |
| [Git Commit Id Maven Plugin][99]                        | [GNU Lesser General Public License 3.0][100] |
| [Project Keeper Maven plugin][101]                      | [The MIT License][102]                       |
| [Exec Maven Plugin][103]                                | [Apache License 2][3]                        |
| [Apache Maven Clean Plugin][104]                        | [Apache-2.0][3]                              |
| [Apache Maven Resources Plugin][105]                    | [Apache-2.0][3]                              |
| [Apache Maven Install Plugin][106]                      | [Apache-2.0][3]                              |
| [Apache Maven Site Plugin][107]                         | [Apache-2.0][3]                              |

## Extension

### Compile Dependencies

| Dependency                                 | License |
| ------------------------------------------ | ------- |
| [@exasol/extension-manager-interface][108] | MIT     |

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
[22]: http://www.slf4j.org
[23]: https://opensource.org/license/mit
[24]: https://github.com/mwiede/jsch
[25]: https://github.com/mwiede/jsch/blob/master/LICENSE.txt
[26]: https://github.com/mwiede/jsch/blob/master/LICENSE.JZlib.txt
[27]: https://github.com/mwiede/jsch/blob/master/LICENSE.jBCrypt.txt
[28]: http://zookeeper.apache.org/zookeeper
[29]: https://www.alluxio.io/alluxio-dora/alluxio-core/alluxio-core-client/alluxio-core-client-hdfs/
[30]: https://github.com/alluxio/alluxio/blob/master/LICENSE
[31]: https://metrics.dropwizard.io/metrics-core
[32]: https://developers.google.com/protocol-buffers/protobuf-java/
[33]: https://developers.google.com/protocol-buffers/protobuf-java-util/
[34]: https://github.com/GoogleCloudDataproc/hadoop-connectors/gcs-connector
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
[50]: https://logging.apache.org/log4j/2.x/
[51]: https://github.com/lightbend/scala-logging
[52]: https://github.com/mockito/mockito
[53]: https://opensource.org/licenses/MIT
[54]: http://hamcrest.org/JavaHamcrest/
[55]: https://raw.githubusercontent.com/hamcrest/JavaHamcrest/master/LICENSE
[56]: https://java.testcontainers.org
[57]: http://opensource.org/licenses/MIT
[58]: https://github.com/exasol/exasol-testcontainers/
[59]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[60]: https://github.com/exasol/test-db-builder-java/
[61]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[62]: https://github.com/exasol/hamcrest-resultset-matcher/
[63]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[64]: https://www.jqno.nl/equalsverifier
[65]: https://junit.org/
[66]: https://www.eclipse.org/legal/epl-v20.html
[67]: https://github.com/exasol/maven-project-version-getter/
[68]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[69]: https://github.com/exasol/extension-manager/
[70]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[71]: https://aws.amazon.com/sdkforjava/http-clients/apache-client
[72]: http://logback.qos.ch/logback-classic
[73]: https://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
[74]: http://logback.qos.ch/logback-core
[75]: https://github.com/airlift/aircompressor
[76]: https://docs.sonarsource.com/sonarqube-server/latest/extension-guide/developing-a-plugin/plugin-basics/sonar-scanner-maven/sonar-maven-plugin/
[77]: http://www.gnu.org/licenses/lgpl.txt
[78]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[79]: https://maven.apache.org/plugins/maven-compiler-plugin/
[80]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[81]: https://www.mojohaus.org/flatten-maven-plugin/
[82]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[83]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[84]: https://maven.apache.org/surefire/maven-surefire-plugin/
[85]: https://www.mojohaus.org/versions/versions-maven-plugin/
[86]: https://basepom.github.io/duplicate-finder-maven-plugin
[87]: https://maven.apache.org/plugins/maven-artifact-plugin/
[88]: https://maven.apache.org/plugins/maven-assembly-plugin/
[89]: https://maven.apache.org/plugins/maven-jar-plugin/
[90]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[91]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[92]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[93]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[94]: https://www.eclipse.org/legal/epl-2.0/
[95]: https://github.com/exasol/quality-summarizer-maven-plugin/
[96]: https://github.com/exasol/quality-summarizer-maven-plugin/blob/main/LICENSE
[97]: https://github.com/exasol/error-code-crawler-maven-plugin/
[98]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[99]: https://github.com/git-commit-id/git-commit-id-maven-plugin
[100]: http://www.gnu.org/licenses/lgpl-3.0.txt
[101]: https://github.com/exasol/project-keeper/
[102]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[103]: https://www.mojohaus.org/exec-maven-plugin
[104]: https://maven.apache.org/plugins/maven-clean-plugin/
[105]: https://maven.apache.org/plugins/maven-resources-plugin/
[106]: https://maven.apache.org/plugins/maven-install-plugin/
[107]: https://maven.apache.org/plugins/maven-site-plugin/
[108]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.4.3.tgz
