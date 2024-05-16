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
| [Apache Avro][30]                          | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Commons Compress][31]              | [Apache-2.0][3]                                                                                                                                                                                     |
| [Nimbus JOSE+JWT][32]                      | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [delta-core][33]                           | [Apache-2.0][34]                                                                                                                                                                                    |
| [Spark Project SQL][35]                    | [Apache 2.0 License][36]                                                                                                                                                                            |
| [Apache Ivy][37]                           | [The Apache Software License, Version 2.0][6]                                                                                                                                                       |
| [Parquet for Java][38]                     | [MIT License][39]                                                                                                                                                                                   |
| [JUL to SLF4J bridge][40]                  | [MIT License][41]                                                                                                                                                                                   |
| [Apache Log4j API][42]                     | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j 1.x Compatibility API][43]   | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j Core][44]                    | [Apache-2.0][3]                                                                                                                                                                                     |
| [scala-logging][45]                        | [Apache 2.0 License][36]                                                                                                                                                                            |
| [jersey-core-common][46]                   | [EPL 2.0][47]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][48]; [Apache License, 2.0][36]; [Public Domain][49]                                                      |
| [jersey-core-client][50]                   | [EPL 2.0][47]; [GPL2 w/ CPE][48]; [EDL 1.0][51]; [BSD 2-Clause][52]; [Apache License, 2.0][36]; [Public Domain][49]; [Modified BSD][53]; [jQuery license][54]; [MIT license][41]; [W3C license][55] |
| [jersey-core-server][56]                   | [EPL 2.0][47]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][48]; [Apache License, 2.0][36]; [Modified BSD][53]                                                       |
| [jersey-container-servlet][57]             | [EPL 2.0][47]; [GPL2 w/ CPE][48]; [EDL 1.0][51]; [BSD 2-Clause][52]; [Apache License, 2.0][36]; [Public Domain][49]; [Modified BSD][53]; [jQuery license][54]; [MIT license][41]; [W3C license][55] |
| [jersey-container-servlet-core][58]        | [EPL 2.0][47]; [GPL2 w/ CPE][48]; [EDL 1.0][51]; [BSD 2-Clause][52]; [Apache License, 2.0][36]; [Public Domain][49]; [Modified BSD][53]; [jQuery license][54]; [MIT license][41]; [W3C license][55] |
| [jersey-inject-hk2][59]                    | [EPL 2.0][47]; [GPL2 w/ CPE][48]; [EDL 1.0][51]; [BSD 2-Clause][52]; [Apache License, 2.0][36]; [Public Domain][49]; [Modified BSD][53]; [jQuery license][54]; [MIT license][41]; [W3C license][55] |

### Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [scalatest][60]                            | [the Apache License, ASL Version 2.0][34] |
| [scalatestplus-mockito][61]                | [Apache-2.0][34]                          |
| [mockito-core][62]                         | [MIT][63]                                 |
| [Hamcrest][64]                             | [BSD License 3][65]                       |
| [testcontainers-scala-scalatest][66]       | [The MIT License (MIT)][63]               |
| [Testcontainers :: Localstack][67]         | [MIT][68]                                 |
| [Test containers for Exasol on Docker][69] | [MIT License][70]                         |
| [Test Database Builder for Java][71]       | [MIT License][72]                         |
| [Matcher for SQL Result Sets][73]          | [MIT License][74]                         |
| [EqualsVerifier \| release normal jar][75] | [Apache License, Version 2.0][3]          |
| [JUnit Jupiter Engine][76]                 | [Eclipse Public License v2.0][77]         |
| [Maven Project Version Getter][78]         | [MIT License][79]                         |
| [Extension integration tests library][80]  | [MIT License][81]                         |

### Runtime Dependencies

| Dependency                   | License                                                                       |
| ---------------------------- | ----------------------------------------------------------------------------- |
| [Logback Classic Module][82] | [Eclipse Public License - v 1.0][83]; [GNU Lesser General Public License][84] |
| [Logback Core Module][85]    | [Eclipse Public License - v 1.0][83]; [GNU Lesser General Public License][84] |

### Plugin Dependencies

| Dependency                                              | License                                       |
| ------------------------------------------------------- | --------------------------------------------- |
| [SonarQube Scanner for Maven][86]                       | [GNU LGPL 3][87]                              |
| [Apache Maven Toolchains Plugin][88]                    | [Apache-2.0][3]                               |
| [Apache Maven Compiler Plugin][89]                      | [Apache-2.0][3]                               |
| [Apache Maven Enforcer Plugin][90]                      | [Apache-2.0][3]                               |
| [Maven Flatten Plugin][91]                              | [Apache Software Licenese][3]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][92] | [ASL2][6]                                     |
| [scala-maven-plugin][93]                                | [Public domain (Unlicense)][94]               |
| [ScalaTest Maven Plugin][95]                            | [the Apache License, ASL Version 2.0][34]     |
| [Apache Maven Javadoc Plugin][96]                       | [Apache-2.0][3]                               |
| [Maven Surefire Plugin][97]                             | [Apache-2.0][3]                               |
| [Versions Maven Plugin][98]                             | [Apache License, Version 2.0][3]              |
| [duplicate-finder-maven-plugin Maven Mojo][99]          | [Apache License 2.0][36]                      |
| [Apache Maven Assembly Plugin][100]                     | [Apache-2.0][3]                               |
| [Apache Maven JAR Plugin][101]                          | [Apache-2.0][3]                               |
| [Artifact reference checker and unifier][102]           | [MIT License][103]                            |
| [Maven Failsafe Plugin][104]                            | [Apache-2.0][3]                               |
| [JaCoCo :: Maven Plugin][105]                           | [EPL-2.0][106]                                |
| [error-code-crawler-maven-plugin][107]                  | [MIT License][108]                            |
| [Reproducible Build Maven Plugin][109]                  | [Apache 2.0][6]                               |
| [Project Keeper Maven plugin][110]                      | [The MIT License][111]                        |
| [OpenFastTrace Maven Plugin][112]                       | [GNU General Public License v3.0][113]        |
| [Scalastyle Maven Plugin][114]                          | [Apache 2.0][36]                              |
| [spotless-maven-plugin][115]                            | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][116]                            | [BSD-3-Clause][26]                            |
| [Exec Maven Plugin][117]                                | [Apache License 2][3]                         |
| [Apache Maven Clean Plugin][118]                        | [Apache-2.0][3]                               |

## Extension

### Compile Dependencies

| Dependency                                 | License |
| ------------------------------------------ | ------- |
| [@exasol/extension-manager-interface][119] | MIT     |

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
[30]: https://avro.apache.org
[31]: https://commons.apache.org/proper/commons-compress/
[32]: https://bitbucket.org/connect2id/nimbus-jose-jwt
[33]: https://delta.io/
[34]: http://www.apache.org/licenses/LICENSE-2.0
[35]: https://spark.apache.org/
[36]: http://www.apache.org/licenses/LICENSE-2.0.html
[37]: http://ant.apache.org/ivy/
[38]: https://github.com/exasol/parquet-io-java/
[39]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[40]: http://www.slf4j.org
[41]: http://www.opensource.org/licenses/mit-license.php
[42]: https://logging.apache.org/log4j/2.x/log4j/log4j-api/
[43]: https://logging.apache.org/log4j/2.x/log4j/log4j-1.2-api/
[44]: https://logging.apache.org/log4j/2.x/log4j/log4j-core/
[45]: https://github.com/lightbend/scala-logging
[46]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-common
[47]: http://www.eclipse.org/legal/epl-2.0
[48]: https://www.gnu.org/software/classpath/license.html
[49]: https://creativecommons.org/publicdomain/zero/1.0/
[50]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-client
[51]: http://www.eclipse.org/org/documents/edl-v10.php
[52]: https://opensource.org/licenses/BSD-2-Clause
[53]: https://asm.ow2.io/license.html
[54]: jquery.org/license
[55]: https://www.w3.org/Consortium/Legal/copyright-documents-19990405
[56]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-server
[57]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet
[58]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet-core
[59]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-hk2
[60]: http://www.scalatest.org
[61]: https://github.com/scalatest/scalatestplus-mockito
[62]: https://github.com/mockito/mockito
[63]: https://opensource.org/licenses/MIT
[64]: http://hamcrest.org/JavaHamcrest/
[65]: http://opensource.org/licenses/BSD-3-Clause
[66]: https://github.com/testcontainers/testcontainers-scala
[67]: https://java.testcontainers.org
[68]: http://opensource.org/licenses/MIT
[69]: https://github.com/exasol/exasol-testcontainers/
[70]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[71]: https://github.com/exasol/test-db-builder-java/
[72]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[73]: https://github.com/exasol/hamcrest-resultset-matcher/
[74]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[75]: https://www.jqno.nl/equalsverifier
[76]: https://junit.org/junit5/
[77]: https://www.eclipse.org/legal/epl-v20.html
[78]: https://github.com/exasol/maven-project-version-getter/
[79]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[80]: https://github.com/exasol/extension-manager/
[81]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[82]: http://logback.qos.ch/logback-classic
[83]: http://www.eclipse.org/legal/epl-v10.html
[84]: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
[85]: http://logback.qos.ch/logback-core
[86]: http://sonarsource.github.io/sonar-scanner-maven/
[87]: http://www.gnu.org/licenses/lgpl.txt
[88]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[89]: https://maven.apache.org/plugins/maven-compiler-plugin/
[90]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[91]: https://www.mojohaus.org/flatten-maven-plugin/
[92]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[93]: http://github.com/davidB/scala-maven-plugin
[94]: http://unlicense.org/
[95]: https://www.scalatest.org/user_guide/using_the_scalatest_maven_plugin
[96]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[97]: https://maven.apache.org/surefire/maven-surefire-plugin/
[98]: https://www.mojohaus.org/versions/versions-maven-plugin/
[99]: https://basepom.github.io/duplicate-finder-maven-plugin
[100]: https://maven.apache.org/plugins/maven-assembly-plugin/
[101]: https://maven.apache.org/plugins/maven-jar-plugin/
[102]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[103]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[104]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[105]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[106]: https://www.eclipse.org/legal/epl-2.0/
[107]: https://github.com/exasol/error-code-crawler-maven-plugin/
[108]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[109]: http://zlika.github.io/reproducible-build-maven-plugin
[110]: https://github.com/exasol/project-keeper/
[111]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[112]: https://github.com/itsallcode/openfasttrace-maven-plugin
[113]: https://www.gnu.org/licenses/gpl-3.0.html
[114]: http://www.scalastyle.org
[115]: https://github.com/diffplug/spotless
[116]: https://github.com/evis/scalafix-maven-plugin
[117]: https://www.mojohaus.org/exec-maven-plugin
[118]: https://maven.apache.org/plugins/maven-clean-plugin/
[119]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.4.2.tgz
