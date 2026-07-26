<!-- @formatter:off -->
# Dependencies

## Cloud Storage Extension

### Compile Dependencies

| Dependency                                  | License                                         |
| ------------------------------------------- | ----------------------------------------------- |
| [scala-library-bootstrapped][0]             | [Apache-2.0][1]                                 |
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
| [gcs-connector][34]                         | [Apache License, Version 2.0][3]                |
| [Google OAuth Client Library for Java][35]  | [The Apache Software License, Version 2.0][3]   |
| [ORC Core][36]                              | [Apache-2.0][3]                                 |
| [Apache Avro][37]                           | [Apache-2.0][3]                                 |
| lz4-java                                    |                                                 |
| [LZ4 Java Compression][38]                  | [Apache License, Version 2.0][1]                |
| [Apache Commons Compress][39]               | [Apache-2.0][3]                                 |
| [Nimbus JOSE+JWT][40]                       | [The Apache Software License, Version 2.0][3]   |
| [delta-spark][41]                           | [Apache-2.0][42]                                |
| [Spark Project SQL][43]                     | [Apache-2.0][14]                                |
| [Apache Ivy][44]                            | [The Apache Software License, Version 2.0][7]   |
| [janino][45]                                | [BSD-3-Clause][46]                              |
| [Parquet for Java][47]                      | [MIT License][48]                               |
| [JUL to SLF4J bridge][24]                   | [MIT][25]                                       |
| [Apache Log4j API][49]                      | [Apache-2.0][3]                                 |
| [Apache Log4j 1.x Compatibility API][49]    | [Apache-2.0][3]                                 |
| [Apache Log4j Core][49]                     | [Apache-2.0][3]                                 |

### Test Dependencies

| Dependency                                 | License                           |
| ------------------------------------------ | --------------------------------- |
| [mockito-core][50]                         | [MIT][51]                         |
| [Hamcrest][52]                             | [BSD-3-Clause][53]                |
| [Testcontainers :: Localstack][54]         | [MIT][55]                         |
| [Test containers for Exasol on Docker][56] | [MIT License][57]                 |
| [Test Database Builder for Java][58]       | [MIT License][59]                 |
| [Matcher for SQL Result Sets][60]          | [MIT License][61]                 |
| [EqualsVerifier \| release normal jar][62] | [Apache License, Version 2.0][3]  |
| [JUnit Jupiter (Aggregator)][63]           | [Eclipse Public License v2.0][64] |
| [Maven Project Version Getter][65]         | [MIT License][66]                 |
| [Extension integration tests library][67]  | [MIT License][68]                 |

### Runtime Dependencies

| Dependency                                   | License                            |
| -------------------------------------------- | ---------------------------------- |
| [AWS Java SDK :: HTTP Clients :: Apache][69] | [Apache License, Version 2.0][9]   |
| [Logback Classic Module][70]                 | [EPL-2.0][64]; [LGPL-2.1-only][71] |
| [Logback Core Module][72]                    | [EPL-2.0][64]; [LGPL-2.1-only][71] |
| [aircompressor][73]                          | [Apache License 2.0][14]           |

### Plugin Dependencies

| Dependency                                              | License                                     |
| ------------------------------------------------------- | ------------------------------------------- |
| [SonarQube Scanner for Maven][74]                       | [GNU LGPL 3][75]                            |
| [Apache Maven Toolchains Plugin][76]                    | [Apache-2.0][3]                             |
| [Apache Maven Compiler Plugin][77]                      | [Apache-2.0][3]                             |
| [Apache Maven Enforcer Plugin][78]                      | [Apache-2.0][3]                             |
| [Maven Flatten Plugin][79]                              | [Apache Software License][3]                |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][80] | [ASL2][7]                                   |
| [Apache Maven Javadoc Plugin][81]                       | [Apache-2.0][3]                             |
| [Maven Surefire Plugin][82]                             | [Apache-2.0][3]                             |
| [Versions Maven Plugin][83]                             | [Apache License, Version 2.0][3]            |
| [duplicate-finder-maven-plugin Maven Mojo][84]          | [Apache License 2.0][85]                    |
| [Apache Maven Artifact Plugin][86]                      | [Apache-2.0][3]                             |
| [Apache Maven Assembly Plugin][87]                      | [Apache-2.0][3]                             |
| [Apache Maven JAR Plugin][88]                           | [Apache-2.0][3]                             |
| [Artifact reference checker and unifier][89]            | [MIT License][90]                           |
| [Maven Failsafe Plugin][91]                             | [Apache-2.0][3]                             |
| [JaCoCo :: Maven Plugin][92]                            | [EPL-2.0][93]                               |
| [Quality Summarizer Maven Plugin][94]                   | [MIT License][95]                           |
| [error-code-crawler-maven-plugin][96]                   | [MIT License][97]                           |
| [Git Commit Id Maven Plugin][98]                        | [GNU Lesser General Public License 3.0][99] |
| [Project Keeper Maven plugin][100]                      | [The MIT License][101]                      |
| [Exec Maven Plugin][102]                                | [Apache License 2][3]                       |
| [Apache Maven Clean Plugin][103]                        | [Apache-2.0][3]                             |
| [Apache Maven Resources Plugin][104]                    | [Apache-2.0][3]                             |
| [Apache Maven Install Plugin][105]                      | [Apache-2.0][3]                             |
| [Apache Maven Site Plugin][106]                         | [Apache-2.0][3]                             |

## Extension

### Compile Dependencies

| Dependency                                 | License |
| ------------------------------------------ | ------- |
| [@exasol/extension-manager-interface][107] | MIT     |

[0]: https://scala-lang.org/
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
[44]: http://ant.apache.org/ivy/
[45]: http://janino-compiler.github.io/janino/
[46]: https://spdx.org/licenses/BSD-3-Clause.html
[47]: https://github.com/exasol/parquet-io-java/
[48]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[49]: https://logging.apache.org/log4j/2.x/
[50]: https://github.com/mockito/mockito
[51]: https://opensource.org/licenses/MIT
[52]: http://hamcrest.org/JavaHamcrest/
[53]: https://raw.githubusercontent.com/hamcrest/JavaHamcrest/master/LICENSE
[54]: https://java.testcontainers.org
[55]: http://opensource.org/licenses/MIT
[56]: https://github.com/exasol/exasol-testcontainers/
[57]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[58]: https://github.com/exasol/test-db-builder-java/
[59]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[60]: https://github.com/exasol/hamcrest-resultset-matcher/
[61]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[62]: https://www.jqno.nl/equalsverifier
[63]: https://junit.org/
[64]: https://www.eclipse.org/legal/epl-v20.html
[65]: https://github.com/exasol/maven-project-version-getter/
[66]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[67]: https://github.com/exasol/extension-manager/
[68]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[69]: https://aws.amazon.com/sdkforjava/http-clients/apache-client
[70]: http://logback.qos.ch/logback-classic
[71]: https://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
[72]: http://logback.qos.ch/logback-core
[73]: https://github.com/airlift/aircompressor
[74]: https://docs.sonarsource.com/sonarqube-server/latest/extension-guide/developing-a-plugin/plugin-basics/sonar-scanner-maven/sonar-maven-plugin/
[75]: http://www.gnu.org/licenses/lgpl.txt
[76]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[77]: https://maven.apache.org/plugins/maven-compiler-plugin/
[78]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[79]: https://www.mojohaus.org/flatten-maven-plugin/
[80]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[81]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[82]: https://maven.apache.org/surefire/maven-surefire-plugin/
[83]: https://www.mojohaus.org/versions/versions-maven-plugin/
[84]: https://basepom.github.io/duplicate-finder-maven-plugin
[85]: http://www.apache.org/licenses/LICENSE-2.0.html
[86]: https://maven.apache.org/plugins/maven-artifact-plugin/
[87]: https://maven.apache.org/plugins/maven-assembly-plugin/
[88]: https://maven.apache.org/plugins/maven-jar-plugin/
[89]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[90]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[91]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[92]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[93]: https://www.eclipse.org/legal/epl-2.0/
[94]: https://github.com/exasol/quality-summarizer-maven-plugin/
[95]: https://github.com/exasol/quality-summarizer-maven-plugin/blob/main/LICENSE
[96]: https://github.com/exasol/error-code-crawler-maven-plugin/
[97]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[98]: https://github.com/git-commit-id/git-commit-id-maven-plugin
[99]: http://www.gnu.org/licenses/lgpl-3.0.txt
[100]: https://github.com/exasol/project-keeper/
[101]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[102]: https://www.mojohaus.org/exec-maven-plugin
[103]: https://maven.apache.org/plugins/maven-clean-plugin/
[104]: https://maven.apache.org/plugins/maven-resources-plugin/
[105]: https://maven.apache.org/plugins/maven-install-plugin/
[106]: https://maven.apache.org/plugins/maven-site-plugin/
[107]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.4.3.tgz
