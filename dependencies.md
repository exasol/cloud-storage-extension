<!-- @formatter:off -->
# Dependencies

## Cloud Storage Extension

### Compile Dependencies

| Dependency                                 | License                                                                                                                                                                                             |
| ------------------------------------------ | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| [Scala Library][0]                         | [Apache-2.0][1]                                                                                                                                                                                     |
| [Apache Commons Lang][2]                   | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Commons Configuration][4]          | [Apache-2.0][3]                                                                                                                                                                                     |
| [AWS Java SDK :: Services :: Amazon S3][5] | [Apache License, Version 2.0][6]                                                                                                                                                                    |
| [snappy-java][7]                           | [Apache-2.0][8]                                                                                                                                                                                     |
| [Import Export UDF Common Scala][9]        | [MIT License][10]                                                                                                                                                                                   |
| [error-reporting-java][11]                 | [MIT License][12]                                                                                                                                                                                   |
| Apache Hadoop Common                       | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Commons IO][13]                    | [Apache-2.0][3]                                                                                                                                                                                     |
| [dnsjava][14]                              | [BSD-3-Clause][15]                                                                                                                                                                                  |
| [JSch][16]                                 | [Revised BSD][17]; [Revised BSD][18]; [ISC][19]                                                                                                                                                     |
| Apache Hadoop Amazon Web Services support  | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache ZooKeeper - Server][20]            | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| Apache Hadoop Azure support                | [Apache-2.0][3]                                                                                                                                                                                     |
| Apache Hadoop Azure Data Lake support      | [Apache-2.0][3]                                                                                                                                                                                     |
| Apache Hadoop HDFS                         | [Apache-2.0][3]                                                                                                                                                                                     |
| Apache Hadoop HDFS Client                  | [Apache-2.0][3]                                                                                                                                                                                     |
| [Kotlin Stdlib][21]                        | [The Apache License, Version 2.0][22]                                                                                                                                                               |
| [Alluxio Core - Client - HDFS][23]         | [Apache License][24]                                                                                                                                                                                |
| [Metrics Core][25]                         | [Apache License 2.0][8]                                                                                                                                                                             |
| [Protocol Buffers [Core]][26]              | [BSD-3-Clause][15]                                                                                                                                                                                  |
| [gcs-connector-hadoop3][27]                | [Apache License, Version 2.0][22]                                                                                                                                                                   |
| [Google OAuth Client Library for Java][28] | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [ORC Core][29]                             | [Apache License, Version 2.0][3]                                                                                                                                                                    |
| [aircompressor][30]                        | [Apache License 2.0][8]                                                                                                                                                                             |
| [Apache Avro][31]                          | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Commons Compress][32]              | [Apache-2.0][3]                                                                                                                                                                                     |
| [Nimbus JOSE+JWT][33]                      | [The Apache Software License, Version 2.0][3]                                                                                                                                                       |
| [delta-core][34]                           | [Apache-2.0][35]                                                                                                                                                                                    |
| [Spark Project SQL][36]                    | [Apache 2.0 License][37]                                                                                                                                                                            |
| [Apache Ivy][38]                           | [The Apache Software License, Version 2.0][22]                                                                                                                                                      |
| [janino][39]                               | [BSD-3-Clause][40]                                                                                                                                                                                  |
| [Parquet for Java][41]                     | [MIT License][42]                                                                                                                                                                                   |
| [JUL to SLF4J bridge][43]                  | [MIT License][44]                                                                                                                                                                                   |
| [Apache Log4j API][45]                     | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j 1.x Compatibility API][46]   | [Apache-2.0][3]                                                                                                                                                                                     |
| [Apache Log4j Core][47]                    | [Apache-2.0][3]                                                                                                                                                                                     |
| [scala-logging][48]                        | [Apache 2.0 License][37]                                                                                                                                                                            |
| [jersey-core-common][49]                   | [EPL 2.0][50]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][51]; [Apache License, 2.0][37]; [Public Domain][52]                                                      |
| [jersey-core-client][53]                   | [EPL 2.0][50]; [GPL2 w/ CPE][51]; [EDL 1.0][54]; [BSD 2-Clause][55]; [Apache License, 2.0][37]; [Public Domain][52]; [Modified BSD][56]; [jQuery license][57]; [MIT license][44]; [W3C license][58] |
| [jersey-core-server][59]                   | [EPL 2.0][50]; [The GNU General Public License (GPL), Version 2, With Classpath Exception][51]; [Apache License, 2.0][37]; [Modified BSD][56]                                                       |
| [jersey-container-servlet][60]             | [EPL 2.0][50]; [GPL2 w/ CPE][51]; [EDL 1.0][54]; [BSD 2-Clause][55]; [Apache License, 2.0][37]; [Public Domain][52]; [Modified BSD][56]; [jQuery license][57]; [MIT license][44]; [W3C license][58] |
| [jersey-container-servlet-core][61]        | [EPL 2.0][50]; [GPL2 w/ CPE][51]; [EDL 1.0][54]; [BSD 2-Clause][55]; [Apache License, 2.0][37]; [Public Domain][52]; [Modified BSD][56]; [jQuery license][57]; [MIT license][44]; [W3C license][58] |
| [jersey-inject-hk2][62]                    | [EPL 2.0][50]; [GPL2 w/ CPE][51]; [EDL 1.0][54]; [BSD 2-Clause][55]; [Apache License, 2.0][37]; [Public Domain][52]; [Modified BSD][56]; [jQuery license][57]; [MIT license][44]; [W3C license][58] |

### Test Dependencies

| Dependency                                 | License                                   |
| ------------------------------------------ | ----------------------------------------- |
| [scalatest][63]                            | [the Apache License, ASL Version 2.0][35] |
| [scalatestplus-mockito][64]                | [Apache-2.0][35]                          |
| [mockito-core][65]                         | [MIT][66]                                 |
| [Hamcrest][67]                             | [BSD-3-Clause][68]                        |
| [testcontainers-scala-scalatest][69]       | [The MIT License (MIT)][66]               |
| [Testcontainers :: Localstack][70]         | [MIT][71]                                 |
| [Test containers for Exasol on Docker][72] | [MIT License][73]                         |
| [Test Database Builder for Java][74]       | [MIT License][75]                         |
| [Matcher for SQL Result Sets][76]          | [MIT License][77]                         |
| [EqualsVerifier \| release normal jar][78] | [Apache License, Version 2.0][3]          |
| [JUnit Jupiter API][79]                    | [Eclipse Public License v2.0][80]         |
| [Maven Project Version Getter][81]         | [MIT License][82]                         |
| [Extension integration tests library][83]  | [MIT License][84]                         |

### Runtime Dependencies

| Dependency                   | License                                                                       |
| ---------------------------- | ----------------------------------------------------------------------------- |
| [Logback Classic Module][85] | [Eclipse Public License - v 1.0][86]; [GNU Lesser General Public License][87] |
| [Logback Core Module][88]    | [Eclipse Public License - v 1.0][86]; [GNU Lesser General Public License][87] |

### Plugin Dependencies

| Dependency                                               | License                                       |
| -------------------------------------------------------- | --------------------------------------------- |
| [Project Keeper Maven plugin][89]                        | [The MIT License][90]                         |
| [Scalastyle Maven Plugin][91]                            | [Apache 2.0][37]                              |
| [spotless-maven-plugin][92]                              | [The Apache Software License, Version 2.0][3] |
| [scalafix-maven-plugin][93]                              | [BSD-3-Clause][15]                            |
| [Exec Maven Plugin][94]                                  | [Apache License 2][3]                         |
| [Apache Maven Clean Plugin][95]                          | [Apache-2.0][3]                               |
| [Apache Maven Install Plugin][96]                        | [Apache-2.0][3]                               |
| [Apache Maven Resources Plugin][97]                      | [Apache-2.0][3]                               |
| [Apache Maven Site Plugin][98]                           | [Apache-2.0][3]                               |
| [SonarQube Scanner for Maven][99]                        | [GNU LGPL 3][100]                             |
| [Apache Maven Toolchains Plugin][101]                    | [Apache-2.0][3]                               |
| [Apache Maven Compiler Plugin][102]                      | [Apache-2.0][3]                               |
| [Apache Maven Enforcer Plugin][103]                      | [Apache-2.0][3]                               |
| [Maven Flatten Plugin][104]                              | [Apache Software Licenese][3]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][105] | [ASL2][22]                                    |
| [scala-maven-plugin][106]                                | [Public domain (Unlicense)][107]              |
| [ScalaTest Maven Plugin][108]                            | [the Apache License, ASL Version 2.0][35]     |
| [Apache Maven Javadoc Plugin][109]                       | [Apache-2.0][3]                               |
| [Maven Surefire Plugin][110]                             | [Apache-2.0][3]                               |
| [Versions Maven Plugin][111]                             | [Apache License, Version 2.0][3]              |
| [duplicate-finder-maven-plugin Maven Mojo][112]          | [Apache License 2.0][37]                      |
| [Apache Maven Artifact Plugin][113]                      | [Apache-2.0][3]                               |
| [Apache Maven Assembly Plugin][114]                      | [Apache-2.0][3]                               |
| [Apache Maven JAR Plugin][115]                           | [Apache-2.0][3]                               |
| [Artifact reference checker and unifier][116]            | [MIT License][117]                            |
| [Maven Failsafe Plugin][118]                             | [Apache-2.0][3]                               |
| [JaCoCo :: Maven Plugin][119]                            | [EPL-2.0][120]                                |
| [Quality Summarizer Maven Plugin][121]                   | [MIT License][122]                            |
| [error-code-crawler-maven-plugin][123]                   | [MIT License][124]                            |
| [Git Commit Id Maven Plugin][125]                        | [GNU Lesser General Public License 3.0][126]  |

## Extension

### Compile Dependencies

| Dependency                                 | License |
| ------------------------------------------ | ------- |
| [@exasol/extension-manager-interface][127] | MIT     |

[0]: https://www.scala-lang.org/
[1]: https://www.apache.org/licenses/LICENSE-2.0
[2]: https://commons.apache.org/proper/commons-lang/
[3]: https://www.apache.org/licenses/LICENSE-2.0.txt
[4]: https://commons.apache.org/proper/commons-configuration/
[5]: https://aws.amazon.com/sdkforjava
[6]: https://aws.amazon.com/apache2.0
[7]: https://github.com/xerial/snappy-java
[8]: https://www.apache.org/licenses/LICENSE-2.0.html
[9]: https://github.com/exasol/import-export-udf-common-scala/
[10]: https://github.com/exasol/import-export-udf-common-scala/blob/main/LICENSE
[11]: https://github.com/exasol/error-reporting-java/
[12]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[13]: https://commons.apache.org/proper/commons-io/
[14]: https://github.com/dnsjava/dnsjava
[15]: https://opensource.org/licenses/BSD-3-Clause
[16]: https://github.com/mwiede/jsch
[17]: https://github.com/mwiede/jsch/blob/master/LICENSE.txt
[18]: https://github.com/mwiede/jsch/blob/master/LICENSE.JZlib.txt
[19]: https://github.com/mwiede/jsch/blob/master/LICENSE.jBCrypt.txt
[20]: http://zookeeper.apache.org/zookeeper
[21]: https://kotlinlang.org/
[22]: http://www.apache.org/licenses/LICENSE-2.0.txt
[23]: https://www.alluxio.io/alluxio-dora/alluxio-core/alluxio-core-client/alluxio-core-client-hdfs/
[24]: https://github.com/alluxio/alluxio/blob/master/LICENSE
[25]: https://metrics.dropwizard.io/metrics-core
[26]: https://developers.google.com/protocol-buffers/protobuf-java/
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
[39]: http://janino-compiler.github.io/janino/
[40]: https://spdx.org/licenses/BSD-3-Clause.html
[41]: https://github.com/exasol/parquet-io-java/
[42]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[43]: http://www.slf4j.org
[44]: http://www.opensource.org/licenses/mit-license.php
[45]: https://logging.apache.org/log4j/2.x/log4j/log4j-api/
[46]: https://logging.apache.org/log4j/2.x/log4j/log4j-1.2-api/
[47]: https://logging.apache.org/log4j/2.x/log4j/log4j-core/
[48]: https://github.com/lightbend/scala-logging
[49]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-common
[50]: http://www.eclipse.org/legal/epl-2.0
[51]: https://www.gnu.org/software/classpath/license.html
[52]: https://creativecommons.org/publicdomain/zero/1.0/
[53]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-client
[54]: http://www.eclipse.org/org/documents/edl-v10.php
[55]: https://opensource.org/licenses/BSD-2-Clause
[56]: https://asm.ow2.io/license.html
[57]: jquery.org/license
[58]: https://www.w3.org/Consortium/Legal/copyright-documents-19990405
[59]: https://projects.eclipse.org/projects/ee4j.jersey/jersey-server
[60]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet
[61]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-container-servlet-core
[62]: https://projects.eclipse.org/projects/ee4j.jersey/project/jersey-hk2
[63]: http://www.scalatest.org
[64]: https://github.com/scalatest/scalatestplus-mockito
[65]: https://github.com/mockito/mockito
[66]: https://opensource.org/licenses/MIT
[67]: http://hamcrest.org/JavaHamcrest/
[68]: https://raw.githubusercontent.com/hamcrest/JavaHamcrest/master/LICENSE
[69]: https://github.com/testcontainers/testcontainers-scala
[70]: https://java.testcontainers.org
[71]: http://opensource.org/licenses/MIT
[72]: https://github.com/exasol/exasol-testcontainers/
[73]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[74]: https://github.com/exasol/test-db-builder-java/
[75]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[76]: https://github.com/exasol/hamcrest-resultset-matcher/
[77]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[78]: https://www.jqno.nl/equalsverifier
[79]: https://junit.org/junit5/
[80]: https://www.eclipse.org/legal/epl-v20.html
[81]: https://github.com/exasol/maven-project-version-getter/
[82]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[83]: https://github.com/exasol/extension-manager/
[84]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[85]: http://logback.qos.ch/logback-classic
[86]: http://www.eclipse.org/legal/epl-v10.html
[87]: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
[88]: http://logback.qos.ch/logback-core
[89]: https://github.com/exasol/project-keeper/
[90]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[91]: http://www.scalastyle.org
[92]: https://github.com/diffplug/spotless
[93]: https://github.com/evis/scalafix-maven-plugin
[94]: https://www.mojohaus.org/exec-maven-plugin
[95]: https://maven.apache.org/plugins/maven-clean-plugin/
[96]: https://maven.apache.org/plugins/maven-install-plugin/
[97]: https://maven.apache.org/plugins/maven-resources-plugin/
[98]: https://maven.apache.org/plugins/maven-site-plugin/
[99]: http://docs.sonarqube.org/display/PLUG/Plugin+Library/sonar-scanner-maven/sonar-maven-plugin
[100]: http://www.gnu.org/licenses/lgpl.txt
[101]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[102]: https://maven.apache.org/plugins/maven-compiler-plugin/
[103]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[104]: https://www.mojohaus.org/flatten-maven-plugin/
[105]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[106]: http://github.com/davidB/scala-maven-plugin
[107]: http://unlicense.org/
[108]: https://www.scalatest.org/user_guide/using_the_scalatest_maven_plugin
[109]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[110]: https://maven.apache.org/surefire/maven-surefire-plugin/
[111]: https://www.mojohaus.org/versions/versions-maven-plugin/
[112]: https://basepom.github.io/duplicate-finder-maven-plugin
[113]: https://maven.apache.org/plugins/maven-artifact-plugin/
[114]: https://maven.apache.org/plugins/maven-assembly-plugin/
[115]: https://maven.apache.org/plugins/maven-jar-plugin/
[116]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[117]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[118]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[119]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[120]: https://www.eclipse.org/legal/epl-2.0/
[121]: https://github.com/exasol/quality-summarizer-maven-plugin/
[122]: https://github.com/exasol/quality-summarizer-maven-plugin/blob/main/LICENSE
[123]: https://github.com/exasol/error-code-crawler-maven-plugin/
[124]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[125]: https://github.com/git-commit-id/git-commit-id-maven-plugin
[126]: http://www.gnu.org/licenses/lgpl-3.0.txt
[127]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.4.3.tgz
