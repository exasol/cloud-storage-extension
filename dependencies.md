<!-- @formatter:off -->
# Dependencies

## Cloud Storage Extension

### Compile Dependencies

| Dependency                                 | License                                       |
| ------------------------------------------ | --------------------------------------------- |
| [Scala Library][0]                         | [Apache-2.0][1]                               |
| [Apache Commons Lang][2]                   | [Apache-2.0][3]                               |
| [Apache Commons Configuration][4]          | [Apache-2.0][3]                               |
| [Guava: Google Core Libraries for Java][5] | [Apache License, Version 2.0][6]              |
| [io.grpc:grpc-netty][7]                    | [Apache 2.0][8]                               |
| [Netty/Codec/HTTP2][9]                     | [Apache License, Version 2.0][1]              |
| [snappy-java][10]                          | [Apache-2.0][11]                              |
| [Import Export UDF Common Scala][12]       | [MIT License][13]                             |
| [error-reporting-java][14]                 | [MIT License][15]                             |
| Apache Hadoop Common                       | [Apache License, Version 2.0][3]              |
| Apache Hadoop Amazon Web Services support  | [Apache License, Version 2.0][3]              |
| [Apache ZooKeeper - Server][16]            | [Apache License, Version 2.0][3]              |
| Apache Hadoop Azure support                | [Apache License, Version 2.0][3]              |
| Apache Hadoop Azure Data Lake support      | [Apache License, Version 2.0][3]              |
| Apache Hadoop HDFS                         | [Apache License, Version 2.0][3]              |
| Apache Hadoop HDFS Client                  | [Apache License, Version 2.0][3]              |
| [Kotlin Stdlib][17]                        | [The Apache License, Version 2.0][6]          |
| [Alluxio Core - Client - HDFS][18]         | [Apache License][19]                          |
| [Metrics Core][20]                         | [Apache License 2.0][11]                      |
| [Protocol Buffers [Core]][21]              | [BSD-3-Clause][22]                            |
| [gcs-connector-hadoop3][23]                | [Apache License, Version 2.0][6]              |
| [Google OAuth Client Library for Java][24] | [The Apache Software License, Version 2.0][3] |
| [ORC Core][25]                             | [Apache License, Version 2.0][3]              |
| [Apache Avro][26]                          | [Apache-2.0][3]                               |
| [Apache Commons Compress][27]              | [Apache-2.0][3]                               |
| [Nimbus JOSE+JWT][28]                      | [The Apache Software License, Version 2.0][3] |
| [delta-core][29]                           | [Apache-2.0][30]                              |
| [Spark Project SQL][31]                    | [Apache 2.0 License][32]                      |
| [Apache Ivy][33]                           | [The Apache Software License, Version 2.0][6] |
| [Parquet for Java][34]                     | [MIT License][35]                             |
| [JUL to SLF4J bridge][36]                  | [MIT License][37]                             |
| [Apache Log4j API][38]                     | [Apache-2.0][3]                               |
| [Apache Log4j 1.x Compatibility API][39]   | [Apache-2.0][3]                               |
| [Apache Log4j Core][40]                    | [Apache-2.0][3]                               |
| [scala-logging][41]                        | [Apache 2.0 License][32]                      |

### Test Dependencies

| Dependency                                 | License                                                                                                                                                                                             |
| ------------------------------------------ | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| [scalatest][42]                            | [the Apache License, ASL Version 2.0][30]                                                                                                                                                           |
| [scalatestplus-mockito][43]                | [Apache-2.0][30]                                                                                                                                                                                    |
| [mockito-core][44]                         | [MIT][45]                                                                                                                                                                                           |
| [Hamcrest][46]                             | [BSD License 3][47]                                                                                                                                                                                 |
| [testcontainers-scala-scalatest][48]       | [The MIT License (MIT)][45]                                                                                                                                                                         |
| [Testcontainers :: Localstack][49]         | [MIT][50]                                                                                                                                                                                           |
| [Test containers for Exasol on Docker][51] | [MIT License][52]                                                                                                                                                                                   |
| [Test Database Builder for Java][53]       | [MIT License][54]                                                                                                                                                                                   |
| [Matcher for SQL Result Sets][55]          | [MIT License][56]                                                                                                                                                                                   |
| [EqualsVerifier \| release normal jar][57] | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| [JUnit Jupiter Engine][58]                 | [Eclipse Public License v2.0][59]                                                                                                                                                                   |
| [Maven Project Version Getter][60]         | [MIT License][61]                                                                                                                                                                                   |
| [Extension integration tests library][62]  | [MIT License][63]                                                                                                                                                                                   |
| [jersey-core-common][64]                   | [EPL 2.0][65]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][66]; [Apache License, 2.0][32]; [Public Domain][67]                                                      |
| [jersey-core-client][68]                   | [EPL 2.0][65]; [GPL2 w/ CPE][66]; [EDL 1.0][69]; [BSD 2-Clause][70]; [Apache License, 2.0][32]; [Public Domain][67]; [Modified BSD][71]; [jQuery license][72]; [MIT license][37]; [W3C license][73] |
| [jersey-core-server][74]                   | [EPL 2.0][65]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][66]; [Apache License, 2.0][32]; [Modified BSD][71]                                                       |
| [jersey-container-servlet][75]             | [EPL 2.0][65]; [GPL2 w/ CPE][66]; [EDL 1.0][69]; [BSD 2-Clause][70]; [Apache License, 2.0][32]; [Public Domain][67]; [Modified BSD][71]; [jQuery license][72]; [MIT license][37]; [W3C license][73] |
| [jersey-container-servlet-core][76]        | [EPL 2.0][65]; [GPL2 w/ CPE][66]; [EDL 1.0][69]; [BSD 2-Clause][70]; [Apache License, 2.0][32]; [Public Domain][67]; [Modified BSD][71]; [jQuery license][72]; [MIT license][37]; [W3C license][73] |
| [jersey-inject-hk2][77]                    | [EPL 2.0][65]; [GPL2 w/ CPE][66]; [EDL 1.0][69]; [BSD 2-Clause][70]; [Apache License, 2.0][32]; [Public Domain][67]; [Modified BSD][71]; [jQuery license][72]; [MIT license][37]; [W3C license][73] |

### Runtime Dependencies

| Dependency                   | License                                                                       |
| ---------------------------- | ----------------------------------------------------------------------------- |
| [Logback Classic Module][78] | [Eclipse Public License - v 1.0][79]; [GNU Lesser General Public License][80] |
| [Logback Core Module][81]    | [Eclipse Public License - v 1.0][79]; [GNU Lesser General Public License][80] |

### Plugin Dependencies

| Dependency                                              | License                                       |
| ------------------------------------------------------- | --------------------------------------------- |
| [SonarQube Scanner for Maven][82]                       | [GNU LGPL 3][83]                              |
| [Apache Maven Toolchains Plugin][84]                    | [Apache License, Version 2.0][3]              |
| [Apache Maven Compiler Plugin][85]                      | [Apache-2.0][3]                               |
| [Apache Maven Enforcer Plugin][86]                      | [Apache-2.0][3]                               |
| [Maven Flatten Plugin][87]                              | [Apache Software Licenese][3]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][88] | [ASL2][6]                                     |
| [scala-maven-plugin][89]                                | [Public domain (Unlicense)][90]               |
| [ScalaTest Maven Plugin][91]                            | [the Apache License, ASL Version 2.0][30]     |
| [Apache Maven Javadoc Plugin][92]                       | [Apache-2.0][3]                               |
| [Maven Surefire Plugin][93]                             | [Apache-2.0][3]                               |
| [Versions Maven Plugin][94]                             | [Apache License, Version 2.0][3]              |
| [duplicate-finder-maven-plugin Maven Mojo][95]          | [Apache License 2.0][32]                      |
| [Apache Maven Assembly Plugin][96]                      | [Apache-2.0][3]                               |
| [Apache Maven JAR Plugin][97]                           | [Apache License, Version 2.0][3]              |
| [Artifact reference checker and unifier][98]            | [MIT License][99]                             |
| [Maven Failsafe Plugin][100]                            | [Apache-2.0][3]                               |
| [JaCoCo :: Maven Plugin][101]                           | [EPL-2.0][102]                                |
| [error-code-crawler-maven-plugin][103]                  | [MIT License][104]                            |
| [Reproducible Build Maven Plugin][105]                  | [Apache 2.0][6]                               |
| [Project Keeper Maven plugin][106]                      | [The MIT License][107]                        |
| [OpenFastTrace Maven Plugin][108]                       | [GNU General Public License v3.0][109]        |
| [Scalastyle Maven Plugin][110]                          | [Apache 2.0][32]                              |
| [spotless-maven-plugin][111]                            | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][112]                            | [BSD-3-Clause][22]                            |
| [Exec Maven Plugin][113]                                | [Apache License 2][3]                         |
| [Apache Maven Clean Plugin][114]                        | [Apache-2.0][3]                               |

## Extension

### Compile Dependencies

| Dependency                                 | License |
| ------------------------------------------ | ------- |
| [@exasol/extension-manager-interface][115] | MIT     |

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
[16]: http://zookeeper.apache.org/zookeeper
[17]: https://kotlinlang.org/
[18]: https://www.alluxio.io/alluxio-dora/alluxio-core/alluxio-core-client/alluxio-core-client-hdfs/
[19]: https://github.com/alluxio/alluxio/blob/master/LICENSE
[20]: https://metrics.dropwizard.io/metrics-core
[21]: https://developers.google.com/protocol-buffers/protobuf-java/
[22]: https://opensource.org/licenses/BSD-3-Clause
[23]: https://github.com/GoogleCloudPlatform/BigData-interop/gcs-connector/
[24]: https://github.com/googleapis/google-oauth-java-client/google-oauth-client
[25]: https://orc.apache.org/orc-core
[26]: https://avro.apache.org
[27]: https://commons.apache.org/proper/commons-compress/
[28]: https://bitbucket.org/connect2id/nimbus-jose-jwt
[29]: https://delta.io/
[30]: http://www.apache.org/licenses/LICENSE-2.0
[31]: https://spark.apache.org/
[32]: http://www.apache.org/licenses/LICENSE-2.0.html
[33]: http://ant.apache.org/ivy/
[34]: https://github.com/exasol/parquet-io-java/
[35]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[36]: http://www.slf4j.org
[37]: http://www.opensource.org/licenses/mit-license.php
[38]: https://logging.apache.org/log4j/2.x/log4j/log4j-api/
[39]: https://logging.apache.org/log4j/2.x/log4j/log4j-1.2-api/
[40]: https://logging.apache.org/log4j/2.x/log4j/log4j-core/
[41]: https://github.com/lightbend/scala-logging
[42]: http://www.scalatest.org
[43]: https://github.com/scalatest/scalatestplus-mockito
[44]: https://github.com/mockito/mockito
[45]: https://opensource.org/licenses/MIT
[46]: http://hamcrest.org/JavaHamcrest/
[47]: http://opensource.org/licenses/BSD-3-Clause
[48]: https://github.com/testcontainers/testcontainers-scala
[49]: https://java.testcontainers.org
[50]: http://opensource.org/licenses/MIT
[51]: https://github.com/exasol/exasol-testcontainers/
[52]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[53]: https://github.com/exasol/test-db-builder-java/
[54]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[55]: https://github.com/exasol/hamcrest-resultset-matcher/
[56]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[57]: https://www.jqno.nl/equalsverifier
[58]: https://junit.org/junit5/
[59]: https://www.eclipse.org/legal/epl-v20.html
[60]: https://github.com/exasol/maven-project-version-getter/
[61]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[62]: https://github.com/exasol/extension-manager/
[63]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[64]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-common
[65]: http://www.eclipse.org/legal/epl-2.0
[66]: https://www.gnu.org/software/classpath/license.html
[67]: https://creativecommons.org/publicdomain/zero/1.0/
[68]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-client
[69]: http://www.eclipse.org/org/documents/edl-v10.php
[70]: https://opensource.org/licenses/BSD-2-Clause
[71]: https://asm.ow2.io/license.html
[72]: jquery.org/license
[73]: https://www.w3.org/Consortium/Legal/copyright-documents-19990405
[74]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-server
[75]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet
[76]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet-core
[77]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-hk2
[78]: http://logback.qos.ch/logback-classic
[79]: http://www.eclipse.org/legal/epl-v10.html
[80]: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
[81]: http://logback.qos.ch/logback-core
[82]: http://sonarsource.github.io/sonar-scanner-maven/
[83]: http://www.gnu.org/licenses/lgpl.txt
[84]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[85]: https://maven.apache.org/plugins/maven-compiler-plugin/
[86]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[87]: https://www.mojohaus.org/flatten-maven-plugin/
[88]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[89]: http://github.com/davidB/scala-maven-plugin
[90]: http://unlicense.org/
[91]: https://www.scalatest.org/user_guide/using_the_scalatest_maven_plugin
[92]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[93]: https://maven.apache.org/surefire/maven-surefire-plugin/
[94]: https://www.mojohaus.org/versions/versions-maven-plugin/
[95]: https://basepom.github.io/duplicate-finder-maven-plugin
[96]: https://maven.apache.org/plugins/maven-assembly-plugin/
[97]: https://maven.apache.org/plugins/maven-jar-plugin/
[98]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[99]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[100]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[101]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[102]: https://www.eclipse.org/legal/epl-2.0/
[103]: https://github.com/exasol/error-code-crawler-maven-plugin/
[104]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[105]: http://zlika.github.io/reproducible-build-maven-plugin
[106]: https://github.com/exasol/project-keeper/
[107]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[108]: https://github.com/itsallcode/openfasttrace-maven-plugin
[109]: https://www.gnu.org/licenses/gpl-3.0.html
[110]: http://www.scalastyle.org
[111]: https://github.com/diffplug/spotless
[112]: https://github.com/evis/scalafix-maven-plugin
[113]: https://www.mojohaus.org/exec-maven-plugin
[114]: https://maven.apache.org/plugins/maven-clean-plugin/
[115]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.4.1.tgz
