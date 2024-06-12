<!-- @formatter:off -->
# Dependencies

## Cloud Storage Extension

### Compile Dependencies

| Dependency                                 | License                                                                                                                                                                                             |
| ------------------------------------------ | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| [Scala Library][0]                         | [Apache-2.0][1]                                                                                                                                                                                     |
| [Apache Commons Lang][2]                   | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Commons Configuration][4]          | [Apache-2.0][3]                                                                                                                                                                                     |
| [Guava: Google Core Libraries for Java][5] | [Apache License, Version 2.0][6]                                                                                                                                                                    |
| [io.grpc:grpc-netty][7]                    | [Apache 2.0][8]                                                                                                                                                                                     |
| [Netty/Codec/HTTP2][9]                     | [Apache License, Version 2.0][1]                                                                                                                                                                    |
| [snappy-java][10]                          | [Apache-2.0][11]                                                                                                                                                                                    |
| [Import Export UDF Common Scala][12]       | [MIT License][13]                                                                                                                                                                                   |
| [error-reporting-java][14]                 | [MIT License][15]                                                                                                                                                                                   |
| Apache Hadoop Common                       | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| [JSch][16]                                 | [Revised BSD][17]; [Revised BSD][18]; [ISC][19]                                                                                                                                                     |
| Apache Hadoop Amazon Web Services support  | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| [Apache ZooKeeper - Server][20]            | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| Apache Hadoop Azure support                | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| Apache Hadoop Azure Data Lake support      | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| Apache Hadoop HDFS                         | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| Apache Hadoop HDFS Client                  | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| [Kotlin Stdlib][21]                        | [The Apache License, Version 2.0][6]                                                                                                                                                                |
| [Alluxio Core - Client - HDFS][22]         | [Apache License][23]                                                                                                                                                                                |
| [Metrics Core][24]                         | [Apache License 2.0][11]                                                                                                                                                                            |
| [Protocol Buffers [Core]][25]              | [BSD-3-Clause][26]                                                                                                                                                                                  |
| [gcs-connector-hadoop3][27]                | [Apache License, Version 2.0][6]                                                                                                                                                                    |
| [Google OAuth Client Library for Java][28] | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [ORC Core][29]                             | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| [aircompressor][30]                        | [Apache License 2.0][11]                                                                                                                                                                            |
| [Apache Avro][31]                          | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Commons Compress][32]              | [Apache-2.0][3]                                                                                                                                                                                     |
| [Nimbus JOSE+JWT][33]                      | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [delta-core][34]                           | [Apache-2.0][35]                                                                                                                                                                                    |
| [Spark Project SQL][36]                    | [Apache 2.0 License][37]                                                                                                                                                                            |
| [Apache Ivy][38]                           | [The Apache Software License, Version 2.0][6]                                                                                                                                                       |
| [Parquet for Java][39]                     | [MIT License][40]                                                                                                                                                                                   |
| [JUL to SLF4J bridge][41]                  | [MIT License][42]                                                                                                                                                                                   |
| [Apache Log4j API][43]                     | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j 1.x Compatibility API][44]   | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j Core][45]                    | [Apache-2.0][3]                                                                                                                                                                                     |
| [scala-logging][46]                        | [Apache 2.0 License][37]                                                                                                                                                                            |
| [jersey-core-common][47]                   | [EPL 2.0][48]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][49]; [Apache License, 2.0][37]; [Public Domain][50]                                                      |
| [jersey-core-client][51]                   | [EPL 2.0][48]; [GPL2 w/ CPE][49]; [EDL 1.0][52]; [BSD 2-Clause][53]; [Apache License, 2.0][37]; [Public Domain][50]; [Modified BSD][54]; [jQuery license][55]; [MIT license][42]; [W3C license][56] |
| [jersey-core-server][57]                   | [EPL 2.0][48]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][49]; [Apache License, 2.0][37]; [Modified BSD][54]                                                       |
| [jersey-container-servlet][58]             | [EPL 2.0][48]; [GPL2 w/ CPE][49]; [EDL 1.0][52]; [BSD 2-Clause][53]; [Apache License, 2.0][37]; [Public Domain][50]; [Modified BSD][54]; [jQuery license][55]; [MIT license][42]; [W3C license][56] |
| [jersey-container-servlet-core][59]        | [EPL 2.0][48]; [GPL2 w/ CPE][49]; [EDL 1.0][52]; [BSD 2-Clause][53]; [Apache License, 2.0][37]; [Public Domain][50]; [Modified BSD][54]; [jQuery license][55]; [MIT license][42]; [W3C license][56] |
| [jersey-inject-hk2][60]                    | [EPL 2.0][48]; [GPL2 w/ CPE][49]; [EDL 1.0][52]; [BSD 2-Clause][53]; [Apache License, 2.0][37]; [Public Domain][50]; [Modified BSD][54]; [jQuery license][55]; [MIT license][42]; [W3C license][56] |

### Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [scalatest][61]                            | [the Apache License, ASL Version 2.0][35] |
| [scalatestplus-mockito][62]                | [Apache-2.0][35]                          |
| [mockito-core][63]                         | [MIT][64]                                 |
| [Hamcrest][65]                             | [BSD License 3][66]                       |
| [testcontainers-scala-scalatest][67]       | [The MIT License (MIT)][64]               |
| [Testcontainers :: Localstack][68]         | [MIT][69]                                 |
| [Test containers for Exasol on Docker][70] | [MIT License][71]                         |
| [Test Database Builder for Java][72]       | [MIT License][73]                         |
| [Matcher for SQL Result Sets][74]          | [MIT License][75]                         |
| [EqualsVerifier \| release normal jar][76] | [Apache License, Version 2.0][3]          |
| [JUnit Jupiter Engine][77]                 | [Eclipse Public License v2.0][78]         |
| [Maven Project Version Getter][79]         | [MIT License][80]                         |
| [Extension integration tests library][81]  | [MIT License][82]                         |

### Runtime Dependencies

| Dependency                   | License                                                                       |
| ---------------------------- | ----------------------------------------------------------------------------- |
| [Logback Classic Module][83] | [Eclipse Public License - v 1.0][84]; [GNU Lesser General Public License][85] |
| [Logback Core Module][86]    | [Eclipse Public License - v 1.0][84]; [GNU Lesser General Public License][85] |

### Plugin Dependencies

| Dependency                                              | License                                       |
| ------------------------------------------------------- | --------------------------------------------- |
| [SonarQube Scanner for Maven][87]                       | [GNU LGPL 3][88]                              |
| [Apache Maven Toolchains Plugin][89]                    | [Apache-2.0][3]                               |
| [Apache Maven Compiler Plugin][90]                      | [Apache-2.0][3]                               |
| [Apache Maven Enforcer Plugin][91]                      | [Apache-2.0][3]                               |
| [Maven Flatten Plugin][92]                              | [Apache Software Licenese][3]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][93] | [ASL2][6]                                     |
| [scala-maven-plugin][94]                                | [Public domain (Unlicense)][95]               |
| [ScalaTest Maven Plugin][96]                            | [the Apache License, ASL Version 2.0][35]     |
| [Apache Maven Javadoc Plugin][97]                       | [Apache-2.0][3]                               |
| [Maven Surefire Plugin][98]                             | [Apache-2.0][3]                               |
| [Versions Maven Plugin][99]                             | [Apache License, Version 2.0][3]              |
| [duplicate-finder-maven-plugin Maven Mojo][100]         | [Apache License 2.0][37]                      |
| [Apache Maven Assembly Plugin][101]                     | [Apache-2.0][3]                               |
| [Apache Maven JAR Plugin][102]                          | [Apache-2.0][3]                               |
| [Artifact reference checker and unifier][103]           | [MIT License][104]                            |
| [Maven Failsafe Plugin][105]                            | [Apache-2.0][3]                               |
| [JaCoCo :: Maven Plugin][106]                           | [EPL-2.0][107]                                |
| [error-code-crawler-maven-plugin][108]                  | [MIT License][109]                            |
| [Reproducible Build Maven Plugin][110]                  | [Apache 2.0][6]                               |
| [Project Keeper Maven plugin][111]                      | [The MIT License][112]                        |
| [OpenFastTrace Maven Plugin][113]                       | [GNU General Public License v3.0][114]        |
| [Scalastyle Maven Plugin][115]                          | [Apache 2.0][37]                              |
| [spotless-maven-plugin][116]                            | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][117]                            | [BSD-3-Clause][26]                            |
| [Exec Maven Plugin][118]                                | [Apache License 2][3]                         |
| [Apache Maven Clean Plugin][119]                        | [Apache-2.0][3]                               |

## Extension

### Compile Dependencies

| Dependency                                 | License |
| ------------------------------------------ | ------- |
| [@exasol/extension-manager-interface][120] | MIT     |

[0]: https://www.scala-lang.org/
[1]: https://www.apache.org/licenses/LICENSE-2.0
[2]: https://commons.apache.org/proper/commons-lang/
[3]: https://www.apache.org/licenses/LICENSE-2.0.txt
[4]: https://commons.apache.org/proper/commons-configuration/
[5]: https://github.com/google/guava
[6]: http://www.apache.org/licenses/LICENSE-2.0.txt
[7]: https://github.com/grpc/grpc-java
[8]: https://opensource.org/licenses/Apache-2.0
[9]: https://netty.io/netty-codec-http2/
[10]: https://github.com/xerial/snappy-java
[11]: https://www.apache.org/licenses/LICENSE-2.0.html
[12]: https://github.com/exasol/import-export-udf-common-scala/
[13]: https://github.com/exasol/import-export-udf-common-scala/blob/main/LICENSE
[14]: https://github.com/exasol/error-reporting-java/
[15]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[16]: https://github.com/mwiede/jsch
[17]: https://github.com/mwiede/jsch/blob/master/LICENSE.txt
[18]: https://github.com/mwiede/jsch/blob/master/LICENSE.JZlib.txt
[19]: https://github.com/mwiede/jsch/blob/master/LICENSE.jBCrypt.txt
[20]: http://zookeeper.apache.org/zookeeper
[21]: https://kotlinlang.org/
[22]: https://www.alluxio.io/alluxio-dora/alluxio-core/alluxio-core-client/alluxio-core-client-hdfs/
[23]: https://github.com/alluxio/alluxio/blob/master/LICENSE
[24]: https://metrics.dropwizard.io/metrics-core
[25]: https://developers.google.com/protocol-buffers/protobuf-java/
[26]: https://opensource.org/licenses/BSD-3-Clause
[27]: https://github.com/GoogleCloudPlatform/BigData-interop/gcs-connector/
[28]: https://github.com/googleapis/google-oauth-java-client/google-oauth-client
[29]: https://orc.apache.org/orc-core
[30]: https://github.com/airlift/aircompressor
[31]: https://avro.apache.org
[32]: https://commons.apache.org/proper/commons-compress/
[33]: https://bitbucket.org/connect2id/nimbus-jose-jwt
[34]: https://delta.io/
[35]: http://www.apache.org/licenses/LICENSE-2.0
[36]: https://spark.apache.org/
[37]: http://www.apache.org/licenses/LICENSE-2.0.html
[38]: http://ant.apache.org/ivy/
[39]: https://github.com/exasol/parquet-io-java/
[40]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[41]: http://www.slf4j.org
[42]: http://www.opensource.org/licenses/mit-license.php
[43]: https://logging.apache.org/log4j/2.x/log4j/log4j-api/
[44]: https://logging.apache.org/log4j/2.x/log4j/log4j-1.2-api/
[45]: https://logging.apache.org/log4j/2.x/log4j/log4j-core/
[46]: https://github.com/lightbend/scala-logging
[47]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-common
[48]: http://www.eclipse.org/legal/epl-2.0
[49]: https://www.gnu.org/software/classpath/license.html
[50]: https://creativecommons.org/publicdomain/zero/1.0/
[51]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-client
[52]: http://www.eclipse.org/org/documents/edl-v10.php
[53]: https://opensource.org/licenses/BSD-2-Clause
[54]: https://asm.ow2.io/license.html
[55]: jquery.org/license
[56]: https://www.w3.org/Consortium/Legal/copyright-documents-19990405
[57]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-server
[58]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet
[59]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet-core
[60]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-hk2
[61]: http://www.scalatest.org
[62]: https://github.com/scalatest/scalatestplus-mockito
[63]: https://github.com/mockito/mockito
[64]: https://opensource.org/licenses/MIT
[65]: http://hamcrest.org/JavaHamcrest/
[66]: http://opensource.org/licenses/BSD-3-Clause
[67]: https://github.com/testcontainers/testcontainers-scala
[68]: https://java.testcontainers.org
[69]: http://opensource.org/licenses/MIT
[70]: https://github.com/exasol/exasol-testcontainers/
[71]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[72]: https://github.com/exasol/test-db-builder-java/
[73]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[74]: https://github.com/exasol/hamcrest-resultset-matcher/
[75]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[76]: https://www.jqno.nl/equalsverifier
[77]: https://junit.org/junit5/
[78]: https://www.eclipse.org/legal/epl-v20.html
[79]: https://github.com/exasol/maven-project-version-getter/
[80]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[81]: https://github.com/exasol/extension-manager/
[82]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[83]: http://logback.qos.ch/logback-classic
[84]: http://www.eclipse.org/legal/epl-v10.html
[85]: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
[86]: http://logback.qos.ch/logback-core
[87]: http://sonarsource.github.io/sonar-scanner-maven/
[88]: http://www.gnu.org/licenses/lgpl.txt
[89]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[90]: https://maven.apache.org/plugins/maven-compiler-plugin/
[91]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[92]: https://www.mojohaus.org/flatten-maven-plugin/
[93]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[94]: http://github.com/davidB/scala-maven-plugin
[95]: http://unlicense.org/
[96]: https://www.scalatest.org/user_guide/using_the_scalatest_maven_plugin
[97]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[98]: https://maven.apache.org/surefire/maven-surefire-plugin/
[99]: https://www.mojohaus.org/versions/versions-maven-plugin/
[100]: https://basepom.github.io/duplicate-finder-maven-plugin
[101]: https://maven.apache.org/plugins/maven-assembly-plugin/
[102]: https://maven.apache.org/plugins/maven-jar-plugin/
[103]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[104]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[105]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[106]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[107]: https://www.eclipse.org/legal/epl-2.0/
[108]: https://github.com/exasol/error-code-crawler-maven-plugin/
[109]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[110]: http://zlika.github.io/reproducible-build-maven-plugin
[111]: https://github.com/exasol/project-keeper/
[112]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[113]: https://github.com/itsallcode/openfasttrace-maven-plugin
[114]: https://www.gnu.org/licenses/gpl-3.0.html
[115]: http://www.scalastyle.org
[116]: https://github.com/diffplug/spotless
[117]: https://github.com/evis/scalafix-maven-plugin
[118]: https://www.mojohaus.org/exec-maven-plugin
[119]: https://maven.apache.org/plugins/maven-clean-plugin/
[120]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.4.2.tgz
