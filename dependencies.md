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
| [jaxb-api][17]                              | [CDDL 1.1][18]; [GPL2 w/ CPE][18]               |
| [error-reporting-java][19]                  | [MIT License][20]                               |
| Apache Hadoop Common                        | [Apache-2.0][3]                                 |
| [Apache Commons IO][21]                     | [Apache-2.0][3]                                 |
| [dnsjava][22]                               | [BSD-3-Clause][23]                              |
| [SLF4J API Module][24]                      | [MIT][25]                                       |
| [JSch][26]                                  | [Revised BSD][27]; [Revised BSD][28]; [ISC][29] |
| Apache Hadoop Amazon Web Services support   | [Apache-2.0][3]                                 |
| [Apache ZooKeeper - Server][30]             | [Apache License, Version 2.0][3]                |
| Apache Hadoop Azure support                 | [Apache-2.0][3]                                 |
| Apache Hadoop Azure Data Lake support       | [Apache-2.0][3]                                 |
| Apache Hadoop HDFS                          | [Apache-2.0][3]                                 |
| Apache Hadoop HDFS Client                   | [Apache-2.0][3]                                 |
| [Alluxio Core - Client - HDFS][31]          | [Apache License][32]                            |
| [Metrics Core][33]                          | [Apache License 2.0][14]                        |
| [gcs-connector-hadoop3][34]                 | [Apache License, Version 2.0][7]                |
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
| [JUL to SLF4J bridge][24]                   | [MIT][25]                                       |
| [Apache Log4j API][50]                      | [Apache-2.0][3]                                 |
| [Apache Log4j 1.x Compatibility API][50]    | [Apache-2.0][3]                                 |
| [Apache Log4j Core][50]                     | [Apache-2.0][3]                                 |

### Test Dependencies

| Dependency                                 | License                           |
| ------------------------------------------ | --------------------------------- |
| [mockito-core][51]                         | [MIT][52]                         |
| [Hamcrest][53]                             | [BSD-3-Clause][54]                |
| [Testcontainers :: Localstack][55]         | [MIT][56]                         |
| [Test containers for Exasol on Docker][57] | [MIT License][58]                 |
| [Test Database Builder for Java][59]       | [MIT License][60]                 |
| [Matcher for SQL Result Sets][61]          | [MIT License][62]                 |
| [EqualsVerifier \| release normal jar][63] | [Apache License, Version 2.0][3]  |
| [JUnit Jupiter (Aggregator)][64]           | [Eclipse Public License v2.0][65] |
| [Maven Project Version Getter][66]         | [MIT License][67]                 |
| [Extension integration tests library][68]  | [MIT License][69]                 |

### Runtime Dependencies

| Dependency                                   | License                            |
| -------------------------------------------- | ---------------------------------- |
| [AWS Java SDK :: HTTP Clients :: Apache][70] | [Apache License, Version 2.0][9]   |
| [Logback Classic Module][71]                 | [EPL-2.0][65]; [LGPL-2.1-only][72] |
| [Logback Core Module][73]                    | [EPL-2.0][65]; [LGPL-2.1-only][72] |
| [aircompressor][74]                          | [Apache License 2.0][14]           |

### Plugin Dependencies

| Dependency                                              | License                                       |
| ------------------------------------------------------- | --------------------------------------------- |
| [SonarQube Scanner for Maven][75]                       | [GNU LGPL 3][76]                              |
| [Apache Maven Toolchains Plugin][77]                    | [Apache-2.0][3]                               |
| [Apache Maven Compiler Plugin][78]                      | [Apache-2.0][3]                               |
| [Apache Maven Enforcer Plugin][79]                      | [Apache-2.0][3]                               |
| [Maven Flatten Plugin][80]                              | [Apache Software License][3]                  |
| [Project Keeper Maven plugin][81]                       | [The MIT License][82]                         |
| [Exec Maven Plugin][83]                                 | [Apache License 2][3]                         |
| [Apache Maven Clean Plugin][84]                         | [Apache-2.0][3]                               |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][85] | [ASL2][7]                                     |
| [Apache Maven Javadoc Plugin][86]                       | [Apache-2.0][3]                               |
| [Maven Surefire Plugin][87]                             | [Apache-2.0][3]                               |
| [Versions Maven Plugin][88]                             | [Apache License, Version 2.0][3]              |
| [duplicate-finder-maven-plugin Maven Mojo][89]          | [Apache License 2.0][44]                      |
| [Apache Maven Artifact Plugin][90]                      | [Apache-2.0][3]                               |
| [Apache Maven Assembly Plugin][91]                      | [Apache-2.0][3]                               |
| [Apache Maven JAR Plugin][92]                           | [Apache-2.0][3]                               |
| [Artifact reference checker and unifier][93]            | [MIT License][94]                             |
| [spdx-maven-plugin Maven Plugin][95]                    | [The Apache Software License, Version 2.0][7] |
| [Maven Failsafe Plugin][96]                             | [Apache-2.0][3]                               |
| [JaCoCo :: Maven Plugin][97]                            | [EPL-2.0][98]                                 |
| [error-code-crawler-maven-plugin][99]                   | [MIT License][100]                            |
| [Git Commit Id Maven Plugin][101]                       | [GNU Lesser General Public License 3.0][102]  |
| [Apache Maven Resources Plugin][103]                    | [Apache-2.0][3]                               |
| [Apache Maven Install Plugin][104]                      | [Apache-2.0][3]                               |
| [Apache Maven Site Plugin][105]                         | [Apache-2.0][3]                               |

## Extension

### Compile Dependencies

| Dependency                                 | License |
| ------------------------------------------ | ------- |
| [@exasol/extension-manager-interface][106] | MIT     |

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
[17]: https://github.com/eclipse-ee4j/jaxb-api
[18]: https://oss.oracle.com/licenses/CDDL+GPL-1.1
[19]: https://github.com/exasol/error-reporting-java/
[20]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[21]: https://commons.apache.org/proper/commons-io/
[22]: https://github.com/dnsjava/dnsjava
[23]: https://opensource.org/licenses/BSD-3-Clause
[24]: http://www.slf4j.org
[25]: https://opensource.org/license/mit
[26]: https://github.com/mwiede/jsch
[27]: https://github.com/mwiede/jsch/blob/master/LICENSE.txt
[28]: https://github.com/mwiede/jsch/blob/master/LICENSE.JZlib.txt
[29]: https://github.com/mwiede/jsch/blob/master/LICENSE.jBCrypt.txt
[30]: http://zookeeper.apache.org/zookeeper
[31]: https://www.alluxio.io/alluxio-dora/alluxio-core/alluxio-core-client/alluxio-core-client-hdfs/
[32]: https://github.com/alluxio/alluxio/blob/master/LICENSE
[33]: https://metrics.dropwizard.io/metrics-core
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
[50]: https://logging.apache.org/log4j/2.x/
[51]: https://github.com/mockito/mockito
[52]: https://opensource.org/licenses/MIT
[53]: http://hamcrest.org/JavaHamcrest/
[54]: https://raw.githubusercontent.com/hamcrest/JavaHamcrest/master/LICENSE
[55]: https://java.testcontainers.org
[56]: http://opensource.org/licenses/MIT
[57]: https://github.com/exasol/exasol-testcontainers/
[58]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[59]: https://github.com/exasol/test-db-builder-java/
[60]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[61]: https://github.com/exasol/hamcrest-resultset-matcher/
[62]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[63]: https://www.jqno.nl/equalsverifier
[64]: https://junit.org/
[65]: https://www.eclipse.org/legal/epl-v20.html
[66]: https://github.com/exasol/maven-project-version-getter/
[67]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[68]: https://github.com/exasol/extension-manager/
[69]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[70]: https://aws.amazon.com/sdkforjava/http-clients/apache-client
[71]: http://logback.qos.ch/logback-classic
[72]: https://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
[73]: http://logback.qos.ch/logback-core
[74]: https://github.com/airlift/aircompressor
[75]: https://docs.sonarsource.com/sonarqube-server/latest/extension-guide/developing-a-plugin/plugin-basics/sonar-scanner-maven/sonar-maven-plugin/
[76]: http://www.gnu.org/licenses/lgpl.txt
[77]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[78]: https://maven.apache.org/plugins/maven-compiler-plugin/
[79]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[80]: https://www.mojohaus.org/flatten-maven-plugin/
[81]: https://github.com/exasol/project-keeper/
[82]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[83]: https://www.mojohaus.org/exec-maven-plugin
[84]: https://maven.apache.org/plugins/maven-clean-plugin/
[85]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[86]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[87]: https://maven.apache.org/surefire/maven-surefire-plugin/
[88]: https://www.mojohaus.org/versions/versions-maven-plugin/
[89]: https://basepom.github.io/duplicate-finder-maven-plugin
[90]: https://maven.apache.org/plugins/maven-artifact-plugin/
[91]: https://maven.apache.org/plugins/maven-assembly-plugin/
[92]: https://maven.apache.org/plugins/maven-jar-plugin/
[93]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[94]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[95]: https://github.com/spdx/spdx-maven-plugin
[96]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[97]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[98]: https://www.eclipse.org/legal/epl-2.0/
[99]: https://github.com/exasol/error-code-crawler-maven-plugin/
[100]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[101]: https://github.com/git-commit-id/git-commit-id-maven-plugin
[102]: http://www.gnu.org/licenses/lgpl-3.0.txt
[103]: https://maven.apache.org/plugins/maven-resources-plugin/
[104]: https://maven.apache.org/plugins/maven-install-plugin/
[105]: https://maven.apache.org/plugins/maven-site-plugin/
[106]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.4.3.tgz
