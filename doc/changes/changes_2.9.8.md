# Cloud Storage Extension 2.9.8, released 2026-10-27

Code name: Fixed vulnerabilities CVE-2026-104721, CVE-2026-19880, CVE-2026-68498, CVE-2026-110879, CVE-2026-87795, CVE-2026-87823, CVE-2026-89045, CVE-2026-86231, CVE-2026-15432, CVE-2026-102554, CVE-2026-109395, CVE-2026-100665, CVE-2026-89044, CVE-2026-93488, CVE-2026-93491, CVE-2026-93558, CVE-2026-93562, CVE-2026-93565, CVE-2026-93574, CVE-2026-93566, CVE-2026-93573, CVE-2026-100666, CVE-2026-89044, CVE-2026-93492, CVE-2026-93567, CVE-2026-93568, CVE-2026-93579, CVE-2026-100659, CVE-2026-100660, CVE-2026-100661, CVE-2026-100662, CVE-2026-100663, CVE-2026-100664, CVE-2026-89044, CVE-2026-93568, CVE-2026-62380, CVE-2026-62243, CVE-2026-71290, CVE-2026-57914, CVE-2026-73334, CVE-2026-59739, CVE-2026-59969, CVE-2026-79993, CVE-2026-84439, CVE-2026-84501, CVE-2026-17508, CVE-2026-18036, CVE-2026-18040, CVE-2026-71889, CVE-2026-71891, CVE-2026-97873, CVE-2026-18659, CVE-2026-18662, CVE-2026-19864, CVE-2026-77420, CVE-2026-77422, CVE-2026-108104, CVE-2026-108106, CVE-2026-90559, CVE-2026-93451, CVE-2026-93452, CVE-2026-116486

## Summary

This release fixes the following 62 vulnerabilities:

### CVE-2026-104721 (CWE-22) in dependency `ch.qos.logback:logback-classic:jar:1.6.0:compile`

Path-traversal vulnerability in QOS.CH Sarl Logback-classic on Java (logback-classic module) allows path-traversal vulnerability. More specifically, an 
MDC-based discriminator value flows unsanitized into a nested 
FileAppender path, letting an attacker who influences that MDC value 
(e.g. via an HTTP header)
 create and append log files outside the intended directory. 

This issue affects Logback-classic: from 0.9.14 through 1.6.4.Â  This vulnerability is similar to CVE-2026-19880 but involves other attack techniques.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-104721?component-type=maven&component-name=ch.qos.logback%2Flogback-classic&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-104721
* https://logback.qos.ch/news.html#1.6.5

### CVE-2026-19880 (CWE-22) in dependency `ch.qos.logback:logback-classic:jar:1.6.0:compile`

Path-traversal vulnerability in QOS.CH Sarl Logback-classic on Java (logback-classic module) allows path-traversal vulnerability. More specifically, an 
MDC-based discriminator value flows unsanitized into a nested 
FileAppender path, letting an attacker who influences that MDC value 
(e.g. via an HTTP header)
 create and append log files outside the intended directory. 

This issue affects Logback-classic: from 0.9.14 through 1.6.2.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-19880?component-type=maven&component-name=ch.qos.logback%2Flogback-classic&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-19880
* https://logback.qos.ch/news.html#1.6.3

### CVE-2026-68498 (CWE-400) in dependency `com.fasterxml.jackson.core:jackson-core:jar:2.22.1:compile`

StreamReadConstraints.maxNameLength (default 50,000 characters) is intended to bound how much memory a single JSON Object property name can consume while being parsed, independently of the much larger maxStringLength (default 100,000,000 characters) that bounds ordinary string values. For parsers built over character input â Reader, String or char[] sources, all served by ReaderBasedJsonParser â that bound is not applied while the name is being accumulated. The code path instead re-uses the string-value length check, which is roughly 2000x larger by default.

In the character-input slow path, ReaderBasedJsonParser._parseName2() accumulates the name into the TextBuffer and, when a segment fills, calls TextBuffer.finishCurrentSegment(), which validates only maxStringLength. The correct check, StreamReadConstraints.validateNameLength(int), is reached afterwards in CharsToNameCanonicalizer.findSymbol(), once the entire name has been scanned to its closing quote. By that point the full name â up to maxStringLength characters â has already been buffered in memory.

A remote attacker who can have a single sufficiently long JSON Object property name parsed through this path can force allocation on the order of 100,000,000 characters (approximately 200 MB, with segment bookkeeping pushing real peak usage higher) before any StreamConstraintsException is thrown, instead of the approximately 50,000-character (about 100 KB) bound the API promises. Repeating this across concurrent requests can exhaust the heap and crash the process. No authentication or user interaction is required. Because StreamReadConstraints.maxDocumentLength is disabled by default (-1), nothing else bounds the token first, so the issue is exploitable under default configuration.

Byte-input parsers are not affected: UTF8StreamJsonParser, UTF8DataInputJsonParser and the non-blocking (async) parsers re-validate maxNameLength on every growth of the name-decode buffer via ParserBase._growNameDecodeBuffer(), rejecting an oversized name after allocating only tens of kilobytes. Applications are exposed when untrusted JSON is parsed from a String, Reader or char[] â for example an HTTP request body already read into a String â rather than from raw UTF-8 bytes.

This issue affects com.fasterxml.jackson.core:jackson-core from 2.16.0 and tools.jackson.core:jackson-core from 3.0.0. Releases before 2.16.0 are not affected, because StreamReadConstraints.maxNameLength was first introduced in jackson-core 2.16.0.

Sonatype's research suggests that this CVE's details differ from those defined at NVD. See https://guide.sonatype.com/vulnerability/CVE-2026-68498 for details

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-68498?component-type=maven&component-name=com.fasterxml.jackson.core%2Fjackson-core&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-68498
* https://github.com/FasterXML/jackson-core/security/advisories/GHSA-649p-m576-vr99

### CVE-2026-110879 (CWE-502) in dependency `com.fasterxml.jackson.core:jackson-databind:jar:2.22.1:compile`

jackson-databind - Deserialization of Untrusted Data

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-110879?component-type=maven&component-name=com.fasterxml.jackson.core%2Fjackson-databind&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-110879
* https://github.com/FasterXML/jackson/wiki/Jackson-Release-2.18.12
* https://github.com/FasterXML/jackson-databind/pull/6287

### CVE-2026-87795 (CWE-125) in dependency `com.github.luben:zstd-jni:jar:1.5.5-4:compile`

zstd-jni versions before 1.5.7-14 fail to validate offset and length parameters in the ZstdDictCompress constructor, allowing out-of-bounds memory reads. Attackers can supply untrusted offset or length values to read native heap memory into the compression dictionary, typically causing JVM crashes.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-87795?component-type=maven&component-name=com.github.luben%2Fzstd-jni&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-87795
* https://github.com/luben/zstd-jni/security/advisories/GHSA-ff36-7w3w-g8rm

### CVE-2026-87823 (CWE-190) in dependency `com.github.luben:zstd-jni:jar:1.5.5-4:compile`

zstd-jni before 1.5.7-14 performs 32-bit signed bounds checks on three direct-ByteBuffer frame-size native methods, allowing out-of-bounds memory reads via negative or overflowing offsets. Attackers can supply negative offset values near Integer.MIN_VALUE to read unmapped memory, causing JVM termination or extracting arbitrary frame size data from unintended memory locations.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-87823?component-type=maven&component-name=com.github.luben%2Fzstd-jni&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-87823
* https://github.com/luben/zstd-jni/security/advisories/GHSA-jfr6-9xqw-2g2q
* https://www.vulncheck.com/advisories/zstd-jni-1.1.1-through-1.5.7-13-out-of-bounds-read-via-direct-bytebuffer-frame-size-methods

### CVE-2026-89045 (CWE-835) in dependency `com.github.luben:zstd-jni:jar:1.5.5-4:compile`

zstd-jni versions 1.4.8-4 through 1.5.7-13 fail to validate negative length parameters in ZstdInputStreamNoFinalizer.read(), allowing attackers to trigger infinite loops. Attackers can pass negative length values to cause the read method to spin indefinitely while holding the stream monitor, blocking all other threads from accessing the stream.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-89045?component-type=maven&component-name=com.github.luben%2Fzstd-jni&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-89045
* https://github.com/luben/zstd-jni/releases/tag/v1.5.7-14
* https://github.com/luben/zstd-jni/security/advisories/GHSA-9jx2-gfp9-phfm

### CVE-2026-86231 (CWE-298) in dependency `com.github.mwiede:jsch:jar:2.28.5:compile`

A security flaw has been discovered in mwiede jsch up to 2.28.5. Affected is the function getRevokedKeys of the file src/main/java/com/jcraft/jsch/KnownHosts.java. Performing a manipulation of the argument known_hosts results in improper check for certificate revocation. The attack is possible to be carried out remotely. The attack is considered to have high complexity. The exploitability is told to be difficult. The exploit has been released to the public and may be used for attacks. Upgrading to version 2.28.6 is able to address this issue. The patch is named 194a2f76a5c0f1c3f778565be3fd66bcafc42d23. You should upgrade the affected component.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-86231?component-type=maven&component-name=com.github.mwiede%2Fjsch&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-86231
* https://github.com/mwiede/jsch/issues/1091
* https://github.com/mwiede/jsch/pull/1098
* https://github.com/mwiede/jsch/releases/tag/jsch-2.28.6

### CVE-2026-15432 (CWE-208) in dependency `com.google.crypto.tink:tink:jar:1.9.0:compile`

When verifying a mac with a ChunkedMacVerification object, Tink compares the resulting tag with non constant time comparison. This potentially allows an attacker to use timinig information as a side channel in order to get information how many bytes of a given tag match the correct tag. This in turn could allow to find a correct tag bytewise.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-15432?component-type=maven&component-name=com.google.crypto.tink%2Ftink&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-15432
* https://github.com/tink-crypto/tink-java/issues/75

### CVE-2026-102554 (CWE-502) in dependency `com.google.guava:guava:jar:33.6.0-jre:compile`

Allocation of resources without limits or throttling (CWE-770) during Java object deserialization in Google Guava versions 4.0 through 33.7.1 allows an attacker to cause a Denial of Service via OutOfMemoryError. When deserializing CompactHashMap, CompactHashSet, or MapMakerInternalMap instances, Guava eagerly allocates an array based on a caller-specified size parameter without throttling, permitting memory exhaustion from crafted serialization streams.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-102554?component-type=maven&component-name=com.google.guava%2Fguava&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-102554
* https://github.com/google/guava/security/advisories/GHSA-xxph-c9ww-hj94

### CVE-2026-109395 (CWE-125) in dependency `com.ning:compress-lzf:jar:1.1.2:compile`

Ning compress-lzf before 1.2.0 contains an out-of-bounds memory access vulnerability in the sun.misc.Unsafe based UnsafeChunkDecoder that fails to validate back-references and literal runs. Attackers can supply crafted LZF streams to applications using the default LZFDecoder to leak unrelated JVM heap memory into decompressed output or crash the JVM.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-109395?component-type=maven&component-name=com.ning%2Fcompress-lzf&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-109395
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/109xxx/CVE-2026-109395.json

### CVE-2026-100665 (CWE-295) in dependency `io.netty:netty-codec-classes-quic:jar:4.2.16.Final:compile`

Netty versions from 4.2.11.Final before 4.2.18.Final contain an incomplete hostname verification fix in the QUIC certificate verification path when using a plain X509TrustManager. The BoringSSLCertificateVerifyCallback discards the SSLEngine for plain trust managers, preventing endpoint identification from running even when HTTPS verification is configured. Attackers on the network path can present a certificate chain for the wrong hostname that the plain trust manager accepts, bypassing hostname authentication for QUIC clients.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-100665?component-type=maven&component-name=io.netty%2Fnetty-codec-classes-quic&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-100665
* https://github.com/netty/netty/security/advisories/GHSA-mj35-3qqm-q387
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/100xxx/CVE-2026-100665.json

### CVE-2026-89044 (CWE-444) in dependency `io.netty:netty-codec-http:jar:4.2.16.Final:compile`

Netty versions 4.1.133.Final through 4.1.137.Final and 4.2.13.Final through 4.2.17.Final fail to properly validate the final transfer coding in the Transfer-Encoding header, allowing attackers to smuggle requests by using malformed encoding declarations. Attackers can split Transfer-Encoding headers across multiple lines or use values like 'chunked, xchunked' to bypass validation and decode messages as chunked when the final coding is not chunked, enabling request smuggling attacks.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-89044?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-89044
* https://github.com/netty/netty/releases/tag/netty-4.1.138.Final
* https://github.com/netty/netty/releases/tag/netty-4.2.18.Final
* https://github.com/netty/netty/security/advisories/GHSA-hcvj-94mj-jp5c

### CVE-2026-93488 (CWE-770) in dependency `io.netty:netty-codec-http:jar:4.2.16.Final:compile`

A flaw was found in Netty. SpdySessionHandler accepts an unlimited number of concurrent remote-initiated streams because localConcurrentStreams defaults to Integer.MAX_VALUE and the handler provides no API to change it. A remote peer can open a SPDY connection and send a large number of SYN_STREAM frames with FLAG_FIN=0, causing unbounded heap and direct memory allocation that can lead to JVM OutOfMemoryError and a denial of service.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-93488?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93488
* https://github.com/netty/netty/security/advisories/GHSA-rmcw-9fcq-wjq7
* https://bugzilla.redhat.com/show_bug.cgi?id=2536887

### CVE-2026-93491 (CWE-770) in dependency `io.netty:netty-codec-http:jar:4.2.16.Final:compile`

A flaw was found in Netty's HttpServerCodec. A remote, unauthenticated attacker can exploit this vulnerability by pipelining HTTP/1.1 requests on a single connection and withholding reads. This action causes the methodOverflowQueue to grow without limit, leading to unbounded heap memory consumption and a denial of service due to memory exhaustion.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-93491?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93491
* https://bugzilla.redhat.com/show_bug.cgi?id=2536891
* https://github.com/netty/netty/releases/tag/netty-4.1.138.Final
* https://github.com/netty/netty/releases/tag/netty-4.2.18.Final

### CVE-2026-93558 (CWE-1035) in dependency `io.netty:netty-codec-http:jar:4.2.16.Final:compile`

A flaw was found in Netty's WebSocketServerExtensionHandler. A remote, unauthenticated attacker can exploit this vulnerability by using HTTP/1.1 pipelining to send requests faster than the application can respond. This leads to an unbounded growth of a per-connection queue, consuming excessive memory. Eventually, this can cause the Java Virtual Machine (JVM) to exhaust its heap, resulting in a Denial of Service (DoS) for the affected server.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-93558?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93558
* https://github.com/netty/netty/security/advisories/GHSA-2g37-3h88-55hc

### CVE-2026-93562 (CWE-1035) in dependency `io.netty:netty-codec-http:jar:4.2.16.Final:compile`

A flaw was found in Netty's HTTP/1 decoder. Incomplete validation of malformed Transfer-Encoding headers allows a remote attacker to perform HTTP request smuggling. By sending specially crafted HTTP requests, an attacker can inject arbitrary HTTP requests, potentially bypassing security controls or accessing unauthorized resources.

Sonatype's research suggests that this CVE's details differ from those defined at NVD. See https://guide.sonatype.com/vulnerability/CVE-2026-93562 for details

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-93562?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93562
* https://github.com/netty/netty/security/advisories/GHSA-hcvj-94mj-jp5c
* https://bugzilla.redhat.com/show_bug.cgi?id=2536951

### CVE-2026-93565 (CWE-1035) in dependency `io.netty:netty-codec-http:jar:4.2.16.Final:compile`

A flaw was found in Netty RtspDecoder. The `RtspMethods.valueOf()` function incorrectly strips trailing control bytes from method tokens in Real-Time Streaming Protocol (RTSP) requests. A remote attacker can exploit this by sending a specially crafted RTSP request, leading to method-token smuggling. This vulnerability allows an attacker to bypass method-based access controls and can also be used to launder malicious requests through Netty-based RTSP proxies, making them appear legitimate to backend systems.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-93565?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93565
* https://github.com/netty/netty/security/advisories/GHSA-h75q-xqrh-59rf
* https://bugzilla.redhat.com/show_bug.cgi?id=2536952

### CVE-2026-93574 (CWE-444) in dependency `io.netty:netty-codec-http:jar:4.2.16.Final:compile`

A flaw was found in Netty's `netty-codec-http` component. A remote attacker could exploit this vulnerability by sending a specially crafted HTTP/1.1 chunk-size token that includes post-digit whitespace. This incorrect parsing of the chunk size can lead to HTTP request smuggling. This allows an attacker to bypass security controls or access unauthorized resources in proxy/backend deployments.

Sonatype's research suggests that this CVE's details differ from those defined at NVD. See https://guide.sonatype.com/vulnerability/CVE-2026-93574 for details

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-93574?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93574
* https://github.com/netty/netty/security/advisories/GHSA-j4mg-hqgv-34qc

### CVE-2026-93566 (CWE-444) in dependency `io.netty:netty-codec-http:jar:4.2.16.Final:compile`

io.netty:netty-codec-http - HTTP Request/Response Smuggling [CVE-2026-93566]

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-93566?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93566
* https://github.com/netty/netty/security/advisories/GHSA-rq4j-fc47-9698

### CVE-2026-93573 (CWE-436) in dependency `io.netty:netty-codec-http:jar:4.2.16.Final:compile`

io.netty:netty-codec-http - Interpretation Conflict [CVE-2026-93573]

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-93573?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93573
* https://github.com/netty/netty/security/advisories/GHSA-3jrc-fchc-59pw

### CVE-2026-100666 (CWE-444) in dependency `io.netty:netty-codec-http:jar:4.2.16.Final:compile`

io.netty:netty-codec-http - HTTP Request/Response Smuggling [CVE-2026-100666]

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-100666?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-100666
* https://github.com/netty/netty/security/advisories/GHSA-2g6j-r8q9-5hr8

### CVE-2026-89044 (CWE-444) in dependency `io.netty:netty-codec-http2:jar:4.2.16.Final:compile`

Netty versions 4.1.133.Final through 4.1.137.Final and 4.2.13.Final through 4.2.17.Final fail to properly validate the final transfer coding in the Transfer-Encoding header, allowing attackers to smuggle requests by using malformed encoding declarations. Attackers can split Transfer-Encoding headers across multiple lines or use values like 'chunked, xchunked' to bypass validation and decode messages as chunked when the final coding is not chunked, enabling request smuggling attacks.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-89044?component-type=maven&component-name=io.netty%2Fnetty-codec-http2&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-89044
* https://github.com/netty/netty/releases/tag/netty-4.1.138.Final
* https://github.com/netty/netty/releases/tag/netty-4.2.18.Final
* https://github.com/netty/netty/security/advisories/GHSA-hcvj-94mj-jp5c

### CVE-2026-93492 (CWE-1035) in dependency `io.netty:netty-codec-http2:jar:4.2.16.Final:compile`

A flaw was found in Netty's HTTP/2 HpackEncoder. A remote attacker can exploit this by sending HTTP/2 SETTINGS frames with a very large MAX_HEADER_TABLE_SIZE. This causes the HpackEncoder to store an excessive number of unique headers, leading to increased CPU usage and memory consumption, ultimately resulting in a Denial of Service (DoS).

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-93492?component-type=maven&component-name=io.netty%2Fnetty-codec-http2&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93492
* https://github.com/netty/netty/security/advisories/GHSA-8352-h356-c9qh
* https://access.redhat.com/security/cve/CVE-2026-93492
* https://bugzilla.redhat.com/show_bug.cgi?id=2536895

### CVE-2026-93567 (CWE-20) in dependency `io.netty:netty-codec-http2:jar:4.2.16.Final:compile`

A flaw was found in Netty's HTTP/2 codec. When converting HTTP/1 CONNECT requests to HTTP/2, the component incorrectly uses the Host header instead of the CONNECT authority-form request-target for the tunnel authority. A remote attacker can exploit this by supplying a different Host header, leading to a malformed HTTP/2 CONNECT request. This can bypass security controls such as tunnel allow-lists or egress policies, resulting in integrity loss.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-93567?component-type=maven&component-name=io.netty%2Fnetty-codec-http2&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93567
* https://access.redhat.com/security/cve/CVE-2026-93567
* https://bugzilla.redhat.com/show_bug.cgi?id=2536955

### CVE-2026-93568 (CWE-20) in dependency `io.netty:netty-codec-http2:jar:4.2.16.Final:compile`

A flaw was found in Netty. A remote attacker could exploit this vulnerability by sending specially crafted HTTP/2 or HTTP/3 Extended CONNECT requests. Netty's HTTP-object conversion path incorrectly processes these requests as regular HTTP/1.1 CONNECT requests, leading to a loss of critical protocol and path information. This misinterpretation can allow attackers to bypass security policies, such as routing or authorization logic, in applications that rely on Netty for HTTP/2 or HTTP/3 communication, resulting in integrity loss.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-93568?component-type=maven&component-name=io.netty%2Fnetty-codec-http2&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93568
* https://github.com/netty/netty/security/advisories/GHSA-w6j8-x45j-w75f
* https://bugzilla.redhat.com/show_bug.cgi?id=2536961

### CVE-2026-93579 (CWE-1035) in dependency `io.netty:netty-codec-http2:jar:4.2.16.Final:compile`

A flaw was found in Netty's HTTP/2 stack. This vulnerability allows a remote attacker to inject prohibited characters, such as NUL, Line Feed, and Carriage Return, into HTTP/2 header field values due to insufficient validation. When these values cross an HTTP/2 to HTTP/1.1 translation boundary, they can be exploited for request smuggling, header injection, or response splitting. This could lead to unauthorized access, data manipulation, or other security bypasses.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-93579?component-type=maven&component-name=io.netty%2Fnetty-codec-http2&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93579
* https://bugzilla.redhat.com/show_bug.cgi?id=2536970
* https://github.com/netty/netty/security/advisories/GHSA-8whp-c7w8-2m72

### CVE-2026-100659 (CWE-444) in dependency `io.netty:netty-codec-http3:jar:4.2.16.Final:compile`

Netty's HTTP/3 codec (io.netty:netty-codec-http3) in versions 4.2.0.Final through 4.2.17.Final does not enforce the RFC 9114 requirement that the :authority pseudo-header field and a literal host header field, when both present, carry the same value. A remote unauthenticated peer can send a single HEADERS frame containing both fields with differing, attacker-controlled values; the request is accepted and delivered to the application with two conflicting authorities, allowing routing, virtual-host, and access-control decisions to be bypassed when different components in the request path consult different fields. This issue is fixed in 4.2.18.Final.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-100659?component-type=maven&component-name=io.netty%2Fnetty-codec-http3&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-100659
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/100xxx/CVE-2026-100659.json

### CVE-2026-100660 (CWE-770) in dependency `io.netty:netty-codec-http3:jar:4.2.16.Final:compile`

Netty's HTTP/3 codec (io.netty:netty-codec-http3) from 4.2.0.Final through 4.2.17.Final retains unbounded per-stream QPACK encoder state. QpackEncoder stores a queue and a dynamic-table index tracker for every encoded field section that references the QPACK dynamic table, keyed by the peer-controlled QUIC stream ID, and these entries are released only when the remote decoder sends a Section Acknowledgment or Stream Cancellation instruction â not when the HTTP/3 stream completes. There is no limit on the number of tracked streams, field sections, or retained bytes. A remote, unauthenticated HTTP/3 client can advertise a non-zero QPACK dynamic-table capacity, acknowledge the table insertion so the server reuses a dynamically indexed response header, and then omit all mandatory Section Acknowledgments while issuing sequential requests over a single QUIC connection, bypassing concurrent-stream limits and causing unbounded heap growth until the server exhausts memory (denial of service). Fixed in 4.2.18.Final.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-100660?component-type=maven&component-name=io.netty%2Fnetty-codec-http3&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-100660
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/100xxx/CVE-2026-100660.json

### CVE-2026-100661 (CWE-400) in dependency `io.netty:netty-codec-http3:jar:4.2.16.Final:compile`

Netty's HTTP/3 codec (io.netty:netty-codec-http3) versions 4.2.0.Final through 4.2.17.Final contain a denial-of-service vulnerability in the QPACK prefixed-integer decoder (QpackUtil.decodePrefixedInteger), which does not bound the number of continuation bytes it will process. A remote, unauthenticated peer can open a QPACK unidirectional stream (type 0x02 encoder or 0x03 decoder) and send a first byte with all prefix bits set (e.g. 0xFF for a 7-bit prefix or 0x3F for a 5-bit prefix) followed by an endless run of 0x80 continuation bytes. The decoder returns -1 ('need more bytes'), so callers never consume the input, the ByteToMessageDecoder cumulator grows without bound, and each decode() invocation re-scans the whole accumulated buffer, yielding O(N^2) CPU cost. The result is unbounded per-connection heap growth (OutOfMemoryError) and event-loop CPU starvation, reachable in every configuration. Fixed in 4.2.18.Final.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-100661?component-type=maven&component-name=io.netty%2Fnetty-codec-http3&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-100661
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/100xxx/CVE-2026-100661.json

### CVE-2026-100662 (CWE-400) in dependency `io.netty:netty-codec-http3:jar:4.2.16.Final:compile`

Netty's HTTP/3 codec (io.netty:netty-codec-http3) versions 4.2.0.Final through 4.2.17.Final contain an uncontrolled resource consumption vulnerability in the QPACK encoder-stream instruction decoder (QpackEncoderHandler, installed on the peer-initiated unidirectional QPACK encoder stream, type 0x02). The handler accepts an attacker-declared string-literal length of up to Integer.MAX_VALUE (~2 GiB) for the Name Length and Value Length fields of the "Insert With Literal Name" instruction (RFC 9204 Â§4.3.3), with no per-instruction or per-literal length cap and no cumulation-size limit; the existing HTTP/3 limits (maxHeaderListSize, maxUnknownFramePayloadLength, DEFAULT_MAX_FIELD_SECTION_SIZE) are not applied to this handler. A remote, unauthenticated peer with an established HTTP/3 connection to a default Netty HTTP/3 server can declare a very large literal length and then trickle fewer bytes than declared, causing the ByteToMessageDecoder MERGE cumulator to retain and grow the per-connection buffer, and ultimately triggering a large byte-array allocation. This leads to unbounded per-connection heap growth and OutOfMemoryError, resulting in denial of service. Fixed in 4.2.18.Final.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-100662?component-type=maven&component-name=io.netty%2Fnetty-codec-http3&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-100662
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/100xxx/CVE-2026-100662.json

### CVE-2026-100663 (CWE-20) in dependency `io.netty:netty-codec-http3:jar:4.2.16.Final:compile`

Netty's HTTP/3 codec (io.netty:netty-codec-http3) from 4.2.2.Final through 4.2.17.Final does not special-case HTTP/1 CONNECT authority-form request-targets when converting HTTP/1 messages to HTTP/3 in HttpConversionUtil.toHttp3Headers. The authority-form target (e.g., "CONNECT trusted.example:443") is parsed as a URI, so its host is emitted as :scheme, :path is set to "/", and the HTTP/1 Host header is used as :authority; if no Host header is present the CONNECT target is dropped. In a Netty-based HTTP/1-to-HTTP/3 proxy or gateway, a remote client can send a CONNECT request whose Host header names a different authority than the request-target, producing a malformed HTTP/3 CONNECT whose tunnel :authority is attacker-controlled. This can bypass tunnel allow-lists, egress policy, backend selection, or audit controls that validate the HTTP/1 CONNECT request-target before forwarding over HTTP/3. The issue is fixed in 4.2.18.Final.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-100663?component-type=maven&component-name=io.netty%2Fnetty-codec-http3&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-100663
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/100xxx/CVE-2026-100663.json

### CVE-2026-100664 (CWE-20) in dependency `io.netty:netty-codec-http3:jar:4.2.16.Final:compile`

Netty's HTTP/3 codec (io.netty:netty-codec-http3) versions 4.2.2.Final through 4.2.17.Final builds the HTTP/3 :authority pseudo-header from the HTTP/1 Host header before considering the authority of an absolute-form HTTP/1 request-target. In HttpConversionUtil.toHttp3Headers(HttpMessage, boolean) â reached via Http3FrameToHttpObjectCodec(false) â a non-empty Host header takes precedence over the request-target authority, contrary to the HTTP/1.1 rule that a server receiving an absolute-form request-target must ignore the Host header. In a Netty-based HTTP/1-to-HTTP/3 gateway, proxy, or protocol bridge, a remote client can send a request such as "GET https://trusted.example/admin HTTP/1.1" with "Host: attacker.example", causing components that validate, authorize, or route on the RFC-defined request-target authority to reach a different decision than the upstream HTTP/3 peer, which receives :authority derived from the conflicting Host header. This authority confusion can affect virtual-host routing, allow-list checks, backend selection, cache keys, and URL generation. The advisory reports integrity impact only (no code execution, memory corruption, or availability impact). Fixed in 4.2.18.Final.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-100664?component-type=maven&component-name=io.netty%2Fnetty-codec-http3&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-100664
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/100xxx/CVE-2026-100664.json

### CVE-2026-89044 (CWE-444) in dependency `io.netty:netty-codec-http3:jar:4.2.16.Final:compile`

Netty versions 4.1.133.Final through 4.1.137.Final and 4.2.13.Final through 4.2.17.Final fail to properly validate the final transfer coding in the Transfer-Encoding header, allowing attackers to smuggle requests by using malformed encoding declarations. Attackers can split Transfer-Encoding headers across multiple lines or use values like 'chunked, xchunked' to bypass validation and decode messages as chunked when the final coding is not chunked, enabling request smuggling attacks.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-89044?component-type=maven&component-name=io.netty%2Fnetty-codec-http3&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-89044
* https://github.com/netty/netty/releases/tag/netty-4.1.138.Final
* https://github.com/netty/netty/releases/tag/netty-4.2.18.Final
* https://github.com/netty/netty/security/advisories/GHSA-hcvj-94mj-jp5c

### CVE-2026-93568 (CWE-20) in dependency `io.netty:netty-codec-http3:jar:4.2.16.Final:compile`

A flaw was found in Netty. A remote attacker could exploit this vulnerability by sending specially crafted HTTP/2 or HTTP/3 Extended CONNECT requests. Netty's HTTP-object conversion path incorrectly processes these requests as regular HTTP/1.1 CONNECT requests, leading to a loss of critical protocol and path information. This misinterpretation can allow attackers to bypass security policies, such as routing or authorization logic, in applications that rely on Netty for HTTP/2 or HTTP/3 communication, resulting in integrity loss.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-93568?component-type=maven&component-name=io.netty%2Fnetty-codec-http3&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93568
* https://github.com/netty/netty/security/advisories/GHSA-w6j8-x45j-w75f
* https://bugzilla.redhat.com/show_bug.cgi?id=2536961

### CVE-2026-62380 (CWE-626) in dependency `io.netty:netty-codec-socks:jar:4.2.16.Final:compile`

io.netty:netty-codec-socks - Null Byte Interaction Error (Poison Null Byte)

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-62380?component-type=maven&component-name=io.netty%2Fnetty-codec-socks&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-62380
* https://github.com/netty/netty/security/advisories/GHSA-cc6x-ffm5-83wf

### CVE-2026-62243 (CWE-297) in dependency `io.netty:netty-handler:jar:4.2.16.Final:compile`

io.netty:netty-handler - Improper Validation of Certificate with Host Mismatch

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-62243?component-type=maven&component-name=io.netty%2Fnetty-handler&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-62243
* https://github.com/netty/netty/security/advisories/GHSA-p85m-gvr3-788c

### CVE-2026-71290 (CWE-295) in dependency `org.apache.httpcomponents.client5:httpclient5:jar:5.6.2:compile`

Improper TLS hostname verification vulnerability in Apache HttpComponents Client 5.4 or newer.Â HostnameVerificationPolicy#BUILTIN setting has no effect when used with the async version of HttpClient. An attacker that can intercept and modify traffic between the client and the server can impersonate the server by presenting a valid certificate for a different domain.Â 

Please note the classic version of HttpClient is not affected by this vulnerability.Â 

Affected users are recommended to upgrade to at least version 5.6.4, which fixes the issue.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-71290?component-type=maven&component-name=org.apache.httpcomponents.client5%2Fhttpclient5&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-71290
* https://lists.apache.org/thread/bhf7g2zwpom2ohvwjjjlonc93br2s8vq
* https://github.com/advisories/GHSA-72q8-9rgw-5g6j

### CVE-2026-57914 (CWE-400) in dependency `org.apache.kerby:kerby-asn1:jar:2.0.3:compile`

By sending a deeply nested ASN1 structure to a Apache Kerby client or service, it's possible to trigger a StackOverFlow Exception which can lead to denial of service issues. Users are recommended to upgrade to version 2.1.2, which fixes this issue.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-57914?component-type=maven&component-name=org.apache.kerby%2Fkerby-asn1&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-57914
* http://www.openwall.com/lists/oss-security/2026/06/26/7
* https://lists.apache.org/thread/w98h2q8wz0bq97vhz4vf55hqomcb2j1m

### CVE-2026-73334 (CWE-20) in dependency `org.apache.parquet:parquet-hadoop:jar:1.13.1:compile`

Potential problem for users of theÂ org.apache.parquet.crypto.keytools package in Apache Parquet, versions 1.12 to 1.18. 
This package enables users to encrypt Parquet files via an envelope encryption mechanism that wraps (encrypts) data keys via a Key Management Service (KMS).Â 
On the reader side, the KMS URL can be application-controlled or file-controlled.
If the user does not leverage application control for this parameter, a file-controlled KMS URL is forwarded to a pluggable KmsClient implementation. 
If the pluggable implementation does not perform host validation, a KMS token can be sent to a malicious host set by an attacker in the file.

Before the problem is fixed, users are recommended toÂ leverage application control for KMS URL parameter in readers (versions 1.12-1.18).
After the problem is fixed (presumably in version 1.19), the upgrade will disable file-controlled KMS URL by default. Users of the KMS URL parameterÂ 
will have two options then:Â leverage application control for KMS URL parameter in readers, or enable file-controlledÂ KMS URL (via a new app parameter).
The latter option will explicitly require (in the new parameter documentation) to validate the KMS URL and use authentication in the custom implementation of the KMS client plug in.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-73334?component-type=maven&component-name=org.apache.parquet%2Fparquet-hadoop&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-73334
* https://lists.apache.org/thread/ro2vomk9xxhv34xhvopgys96c9j8ojm0
* http://www.openwall.com/lists/oss-security/2026/09/08/11

### CVE-2026-59739 (CWE-862) in dependency `org.apache.zookeeper:zookeeper:jar:3.9.5:compile`

Information disclosure via SetWatches reconnect replay in Apache ZooKeeper due to missing ACL check.Â An attacker can discover ACL-restricted paths by registering exists-watches on non-existent paths, then reconnecting after the paths are created with restricted ACLs. Issue is caused by incomplete fix forÂ CVE-2024-23944 (ZOOKEEPER-4799).Â The fix added ACL checking to WatchManager.triggerWatch(). However, DataTree.setWatches() â the SetWatches/SetWatches2 reconnect replay handler â still calls watcher.process(event) with null ACL, bypassing the check entirely. It's important to note that only the path is exposed by this vulnerability, not the data of znode, but since znode path can contain sensitive information like user name or login ID, this issue is potentially critical.

Users are recommended to upgrade to version 3.9.6, 3.8.7 which fixes the issue.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-59739?component-type=maven&component-name=org.apache.zookeeper%2Fzookeeper&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-59739
* https://lists.apache.org/thread/v0lrwsbdxvx6w67p3z7fxf3hzyncq55l

### CVE-2026-59969 (CWE-297) in dependency `org.apache.zookeeper:zookeeper:jar:3.9.5:compile`

Apache ZooKeeper quorum TLS fails to enforce peer hostname verification in FIPS-mode deployments. When sslQuorum=true, zookeeper.fips-mode=true, ssl.quorum.hostnameVerification=true, and ssl.quorum.clientHostnameVerification=trueÂ are enabled, the Java SSLSocketÂ quorum path accepts a CA-trusted peer certificate whose SAN does not match the connected host. A malicious or misissued peer certificate can therefore join quorum traffic, participate in leader election, and enter replication flows.

Users are recommended to upgrade to version 3.8.7 or 3.9.6, which fixes the issue.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-59969?component-type=maven&component-name=org.apache.zookeeper%2Fzookeeper&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-59969
* https://lists.apache.org/thread/qf9kwsgtgbgtvwls6f41281846xg0rss

### CVE-2026-79993 (CWE-862) in dependency `org.apache.zookeeper:zookeeper:jar:3.9.5:compile`

The `deleteContainer` opcode (0x14/20) is processed without verifying the caller's ACL permissions, allowing any authenticated client to delete specific znodes in the data tree regardless of the ACL restrictions on the znode or its parent. This opcode is considered internal-only and the official client doesn't have API for it, but aÂ client that can open a plain TCP session on the ZooKeeper client port (2181 by default)Â - with NO authentication and NO ACL permissions - can delete anyÂ empty persistent znode (including regular persistent nodes, containerÂ nodes, and TTL nodes) by issuing the raw protocol OpCode deleteContainer (20).Â The deleteContainer request path completely skips both the session check andÂ the DELETE ACL check that are enforced by the regular delete (OpCode 2) path.Â This is an authorization bypass / ACL enforcement bug.

This issue affects Apache ZooKeeper: from 3.9.0 through 3.9.5, from 3.8.0 through 3.8.6.

Users are recommended to upgrade to version 3.9.6 or 3.8.7, which fixes the issue.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-79993?component-type=maven&component-name=org.apache.zookeeper%2Fzookeeper&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-79993
* https://lists.apache.org/thread/9cw3rmrzhqpct26v8j18fy41bghlq8y5
* https://www.openwall.com/lists/oss-security/2026/09/15/5

### CVE-2026-84439 (CWE-117) in dependency `org.apache.zookeeper:zookeeper:jar:3.9.5:compile`

When audit logging is enabled (zookeeper.audit.enable=true), an unauthenticated attacker can inject arbitrary fields into Apache ZooKeeper's audit log by sending a digest authentication request with tab characters (\t) embedded in the username. Because the audit log uses tab-separatedÂ key=valueÂ format, the injected tabs are parsed as legitimate field separators, allowing the attacker to spoof audit results (e.g., injectingÂ result=success), forge operation types, and corrupt forensic evidence.

A log injection vulnerability in Apache ZooKeeper allows a client that can callÂ setACLÂ to inject forged key-value fields intoÂ zookeeper_audit.log. When audit logging is enabled, the server serializes attacker-controlled digest ACL ids into theÂ acl=Â audit field without escaping tab characters. Because audit events are emitted as tab-separatedÂ key=valueÂ records, a crafted ACL id can make one successfulÂ setAclÂ event appear to contain forged fields such asÂ operation=deleteÂ andÂ znode=/forged. This undermines the integrity of downstream audit parsing, alerting, and incident response.

This issue affects Apache ZooKeeper: from 3.9.0 through 3.9.5, from 3.8.0 through 3.8.6.

Users are recommended to upgrade to version 3.9.6 or 3.8.7, which fixes the issue.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-84439?component-type=maven&component-name=org.apache.zookeeper%2Fzookeeper&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-84439
* https://lists.apache.org/thread/b7qnjvqjh393l0j07tmnb5ggg40sx3m6
* http://www.openwall.com/lists/oss-security/2026/09/15/6

### CVE-2026-84501 (CWE-117) in dependency `org.apache.zookeeper:zookeeper:jar:3.9.5:compile`

An unauthenticated attacker can inject arbitrary fake log lines into Apache ZooKeeper's operational log by sending a crafted add_auth("ensemble", ...) request containing newline characters (\n). When the ensemble name doesn't match, EnsembleAuthenticationProvider.handleAuthentication() logs the raw, unsanitized name via LOG.warn(). Because SLF4J's {} placeholder preserves embedded newlines, the attacker can forge complete log entries â with arbitrary timestamps, log levels, class names, and messages â that are visually indistinguishable from genuine ZooKeeper log output.

This issue affects Apache ZooKeeper: from 3.9.0 through 3.9.5, from 3.8.0 through 3.8.6.

Users are recommended to upgrade to version 3.8.7 or 3.9.6, which fixes the issue.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-84501?component-type=maven&component-name=org.apache.zookeeper%2Fzookeeper&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-84501
* https://lists.apache.org/thread/ohby1g6zjt72p0w79rhbr41nczzvr33y
* http://www.openwall.com/lists/oss-security/2026/09/15/7

### CVE-2026-17508 (CWE-770) in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.85:compile`

In Bouncy Castle for Java before 1.86, several password-based key derivation entry points ran the KDF with cost parameters taken from the untrusted input being processed, without bounding them, so a small input could dictate an arbitrary amount of work before any password or integrity check could reject it. The affected paths are the RFC 9579 PBMAC1 MAC calculator builders, which took the PBKDF2 iteration count and derived-key length straight out of PBMAC1Params (JcePBMac1CalculatorBuilder, and PKCS12PBEUtils.createPBMac1Calculator reached from PKCS12PfxPdu.isMacValid); the scrypt parallelization parameter p in the PKCS#8 and PKCS#12 cost guards, which bounded only the cost parameter N and the block size r even though the scratch buffer scales with r times p, so the configured memory ceiling could be evaded entirely; the raw JCA PBKDF2 provider (org.bouncycastle.jcajce.provider.symmetric.PBEPBKDF2); and the bcrypt round count read from an encrypted OpenSSH v1 private key's own kdfoptions. Each now bounds the parameter before deriving, in line with the caps already applied elsewhere in the tree, with the OpenSSH round count configurable through the new org.bouncycastle.openssh.max_rounds property. This completes the bounding begun in 1.85 for the PKCS#8 / PBES2 decryptors (CVE-2026-15055). This issue also affects Bouncy Castle for Java LTS before 2.73.13, and Bouncy Castle for Java FIPS (BC-FJA) before bcpkix-fips 1.0.13 (1.0.X series), 2.0.13 (2.0.X series) and 2.1.13 (2.1.X series).

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-17508?component-type=maven&component-name=org.bouncycastle%2Fbcprov-jdk18on&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-17508
* https://github.com/bcgit/bc-java/discussions/2449

### CVE-2026-18036 (CWE-208) in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.85:compile`

In Bouncy Castle for Java before 1.86, NTRU reduced secret values with the % operator in three helpers whose reference implementations are deliberately division-free, so each reduction was carried out by an integer division whose latency depends on the secret operand. Polynomial.modQ divided by a variable divisor, which a compiler cannot strength-reduce to a multiply the way it can a constant one, so it emitted a division on every call including on the decapsulation path where the dividend derives from the private key; Polynomial.mod3 and NTRUSampling.mod3 divided the secret key polynomials f and g during key generation, the message polynomials r and m during encapsulation, and coefficients recovered during decapsulation. An attacker able to measure that timing can recover information about the NTRU private key. modQ now masks, which is exact because q is always a power of two, and mod3 uses the reference implementation's division-free fold and select; the results are unchanged.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-18036?component-type=maven&component-name=org.bouncycastle%2Fbcprov-jdk18on&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-18036
* https://github.com/bcgit/bc-java/discussions/2449
* https://github.com/bcgit/bc-java/wiki/CVE%E2%80%902026%E2%80%9018036

### CVE-2026-18040 (CWE-208) in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.85:compile`

In Bouncy Castle for Java before 1.86, HQC leaked secret-derived data through two side channels: its GF(2^8) arithmetic used lookup tables indexed by field elements, making the cache line touched a function of the operand, and its fixed-weight support sampler left its duplicate scan as soon as a collision was found and stored accepted positions at a secret index. Both run on secret inputs during encapsulation and decapsulation, and the sampler re-expands the secret key from its seed on every decapsulation, so an attacker able to observe cache behaviour or decapsulation timing can recover information about the HQC private key. The field arithmetic is now table-free and the sampler branch-free within a batch of candidates, with output and randomness consumption unchanged.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-18040?component-type=maven&component-name=org.bouncycastle%2Fbcprov-jdk18on&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-18040
* https://github.com/bcgit/bc-java/discussions/2449

### CVE-2026-71889 (CWE-295) in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.85:compile`

In Bouncy Castle for Java before 1.86, neither copy of PKIXCertPathReviewer - org.bouncycastle.pkix.jcajce.PKIXCertPathReviewer nor the legacy org.bouncycastle.x509.PKIXCertPathReviewer - applied X.509 name constraints to the end-entity certificate. checkNameConstraints walked the path with a loop bound of index greater than zero, which is the bound the CA-only steps require, but index zero is the target certificate under the standard CertPath ordering, so the permitted and excluded subtree checks of RFC 5280 sec. 6.1.3 (b) and (c) never ran against the leaf's subject DN or its subjectAltName. A chain whose leaf violated a NameConstraints extension imposed by its own issuing CA therefore reported isValidCertPath() true with an empty error list, while CertPathValidator.getInstance("PKIX", "BC"), which shares no code with the reviewer, rejected the identical chain against the identical trust anchor. An application using the reviewer to make the trust decision rather than for diagnostics alongside a real validation accepted a certificate the constrained CA was never authorised to issue. Both copies now check every certificate in the path including the target, waive the sec. 4.2.1.10 self-issued exemption for the final certificate as sec. 6.1.3 requires, and skip the sec. 6.1.4 (g) constraint-accumulation step for the target. This issue also affects Bouncy Castle for Java LTS before 2.73.13, which carries only the org.bouncycastle.pkix.jcajce copy of the reviewer. It also affects Bouncy Castle for Java FIPS (BC-FJA) before bcpkix-fips 1.0.13 (1.0.X series), 2.0.13 (2.0.X series) and 2.1.13 (2.1.X series).

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-71889?component-type=maven&component-name=org.bouncycastle%2Fbcprov-jdk18on&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-71889
* https://github.com/bcgit/bc-java/discussions/2449

### CVE-2026-71891 (CWE-347) in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.85:compile`

In Bouncy Castle for Java before 1.86, BLS12_381BasicScheme.keyValidate, and so BLSPublicKeyParameters and every BasicScheme, MessageAugmentation and ProofOfPossession verify and aggregateVerify that gate on it, accepted a public key built on a foreign ECCurve that merely shares BLS12-381's field characteristic. The prime-order subgroup check trusts a point's own curve to name its cofactor, since ECPoint.satisfiesOrder returns true outright when the curve's cofactor is one, so a point on a curve with a different equation and a cofactor forged to one passed keyValidate despite not being a G1 point at all. In BC's pairing implementation such a point contributes the identity in the target group, so an aggregate signature verified against a set of public keys including it is accepted even though it contains no signature for that key and message pair, admitting a phantom signer. keyValidate now first confirms that the point's curve carries exactly the canonical G1 field, equation, order and cofactor before any subgroup check. The issue is reachable only where an application constructs an ECPoint on an explicit, non-canonical curve and accepts it as an authority-bearing key; the standard 48-byte compressed-point decoder always supplies the canonical curve and was never affected.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-71891?component-type=maven&component-name=org.bouncycastle%2Fbcprov-jdk18on&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-71891
* https://github.com/bcgit/bc-java/discussions/2449

### CVE-2026-97873 (CWE-770) in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.85:compile`

In Bouncy Castle for Java before 1.86, the raw JCA provider's legacy PBES1 (PKCS#5 scheme 1) and PKCS#12 PBE families ran their password-based key derivation with an iteration count taken from untrusted input without bounding it, so a small input could dictate an arbitrary amount of work before anything could be verified. The AlgorithmParameters implementations (PKCS12PBE and its object identifier aliases, and PBKDF1) accepted any count from an encoded PKCS12PBEParams or PBEParameter, narrowing a value beyond the int range with intValue(), and every Cipher, Mac and SecretKeyFactory in these families derived with whatever count it was given, including one decoded by another provider's AlgorithmParameters, as when javax.crypto.EncryptedPrivateKeyInfo.getKeySpec() decrypts a PKCS#12 PBE-protected private key with BC. Both the parameter parse and the derivations now reject a negative or over-limit count under the org.bouncycastle.pbe.max_iteration_count property (default 10,000,000) that already bounded PBKDF2 (CVE-2026-17508), and the parse rejects a count beyond the int range rather than narrowing it. This issue also affects Bouncy Castle for Java LTS before 2.73.13.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-97873?component-type=maven&component-name=org.bouncycastle%2Fbcprov-jdk18on&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-97873
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/97xxx/CVE-2026-97873.json

### CVE-2026-18659 (CWE-863) in dependency `org.eclipse.jetty:jetty-security:jar:9.4.57.v20241219:compile`

In Eclipse Jetty, across various versions of `jetty-security`, and `jetty-eeN-security`, and `jetty-eeN-servlet`, the Jakarta Spec constraints defined as `http-method-omission` can be bypassed by using substrings of the defined HTTP Methods.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-18659?component-type=maven&component-name=org.eclipse.jetty%2Fjetty-security&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-18659
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/18xxx/CVE-2026-18659.json

### CVE-2026-18662 (CWE-863) in dependency `org.eclipse.jetty:jetty-security:jar:9.4.57.v20241219:compile`

In Eclipse Jetty, across various versions of `jetty-security`, and `jetty-eeN-security`, two or more security constraints with the same http-method-omission set, and no url-patterns, are not combined properly per Jakarta Servlet Spec.

Resulting in later http-method-omission sets overriding previous constraints with the same http-method-omission set.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-18662?component-type=maven&component-name=org.eclipse.jetty%2Fjetty-security&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-18662
* https://github.com/jetty/jetty.project/security/advisories/GHSA-phjh-v7c6-g7mv
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/18xxx/CVE-2026-18662.json

### CVE-2026-19864 (CWE-312) in dependency `org.eclipse.jetty:jetty-security:jar:9.4.57.v20241219:compile`

Eclipse Jetty's `SessionAuthentication` retains authentication credentials in a serializable object. When the credentials contain a plaintext password and the authenticated session is persisted to a session data store, the password may consequently be written to persistent storage in plaintext.

An attacker with sufficient privileges to read the persisted session data may obtain the plaintext passwords of authenticated users.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-19864?component-type=maven&component-name=org.eclipse.jetty%2Fjetty-security&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-19864
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/19xxx/CVE-2026-19864.json

### CVE-2026-77420 (CWE-1333) in dependency `org.jline:jline:jar:3.9.0:compile`

JLine is a Java library for handling console input. From 3.0.0 until 3.30.15 and 4.3.1, DefaultHistory.matchPatterns(String patterns, String line) in reader/src/main/java/org/jline/reader/impl/history/DefaultHistory.java converts the HISTORY_IGNORE configuration value into a Java regular expression while escaping only part of its syntax, allowing other regex metacharacters to reach the backtracking engine. An attacker who can control application or user configuration can supply a nested-quantifier expression that is reevaluated whenever a command is added to history, consuming excessive CPU and indefinitely blocking the reader thread. This issue is fixed in versions 3.30.15 and 4.3.1.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-77420?component-type=maven&component-name=org.jline%2Fjline&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-77420
* https://github.com/jline/jline3/pull/2018
* https://github.com/jline/jline3/releases/tag/3.30.15
* https://github.com/jline/jline3/security/advisories/GHSA-5q95-hrpc-m3w3

### CVE-2026-77422 (CWE-1333) in dependency `org.jline:jline:jar:3.9.0:compile`

JLine is a Java library for handling console input. From 3.0.0 until 3.30.15 and 4.3.1, the JLine built-in grep command in builtins/src/main/java/org/jline/builtins/PosixCommands.java accepts a user-controlled regular expression in grep(...) and, unless line-regexp mode is used, automatically adds a dot-star prefix and suffix before compiling it with Java's backtracking regular expression engine. The wrapping expands the backtracking search space, so a short nested-quantifier expression evaluated against non-matching input can consume excessive CPU and indefinitely block a command worker, including in remotely exposed shell sessions. This issue is fixed in versions 3.30.15 and 4.3.1.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-77422?component-type=maven&component-name=org.jline%2Fjline&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-77422
* https://github.com/jline/jline3/pull/2018
* https://github.com/jline/jline3/releases/tag/3.30.15
* https://github.com/jline/jline3/releases/tag/4.3.1
* https://github.com/jline/jline3/security/advisories/GHSA-r2xf-8xr9-62gw

### CVE-2026-116486 (CWE-386) in dependency `org.objenesis:objenesis:jar:3.3:compile`

Objenesis versions before 3.6 cache object instantiators by class name. Classes with the same name loaded by different class loaders can therefore share the wrong instantiator, allowing an object of an unexpected type to be returned. This is fixed in version 3.6.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-116486?component-type=maven&component-name=org.objenesis%2Fobjenesis&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* https://github.com/easymock/objenesis/security/advisories/GHSA-743r-26gf-97ff

### CVE-2026-108104 (CWE-415) in dependency `org.xerial.snappy:snappy-java:jar:1.1.10.8:compile`

Xerial snappy-java from 1.1.7.4 before 1.1.10.10 contains a double release vulnerability in SnappyFramedInputStream that returns pooled buffers twice when replacement allocation fails. Attackers can supply framed data with a large declared chunk length to trigger OutOfMemoryError, causing shared backing arrays that expose or overwrite other streams' decompressed data.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-108104?component-type=maven&component-name=org.xerial.snappy%2Fsnappy-java&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-108104
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/108xxx/CVE-2026-108104.json

### CVE-2026-108106 (CWE-789) in dependency `org.xerial.snappy:snappy-java:jar:1.1.10.8:compile`

Xerial snappy-java before 1.1.10.9 contains an unbounded memory allocation vulnerability that allows attackers to exhaust JVM memory by declaring a large uncompressed length in compressed input. Attackers can supply a few crafted bytes to Snappy.uncompress, uncompressString, SnappyInputStream or SnappyFramedInputStream to force allocations up to 2 GB, causing OutOfMemoryError and denial of service.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-108106?component-type=maven&component-name=org.xerial.snappy%2Fsnappy-java&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-108106
* https://github.com/xerial/snappy-java/security/advisories/GHSA-q233-g92q-qxp6
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/108xxx/CVE-2026-108106.json

### CVE-2026-90559 (CWE-787) in dependency `org.xerial.snappy:snappy-java:jar:1.1.10.8:compile`

snappy-java through 1.1.10.8 contains an out-of-bounds write vulnerability in Snappy.uncompress(ByteBuffer, ByteBuffer) because destination buffer capacity is never validated against decompressed size. Attackers can supply valid compressed data that decompresses larger than the destination buffer, causing writes past buffer boundaries and JVM termination.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-90559?component-type=maven&component-name=org.xerial.snappy%2Fsnappy-java&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-90559
* https://github.com/xerial/snappy-java/issues/728

### CVE-2026-93451 (CWE-787) in dependency `org.xerial.snappy:snappy-java:jar:1.1.10.8:compile`

snappy-java through 1.1.10.8 contains a buffer overflow vulnerability in typed Snappy.uncompress*Array methods that allocate output arrays by dividing uncompressed length by element size but pass the undivided length to native code. Attackers controlling compressed input can cause misaligned length values to write past array bounds with attacker-controlled bytes, corrupting heap memory.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-93451?component-type=maven&component-name=org.xerial.snappy%2Fsnappy-java&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93451
* https://github.com/xerial/snappy-java/issues/729

### CVE-2026-93452 (CWE-787) in dependency `org.xerial.snappy:snappy-java:jar:1.1.10.8:compile`

snappy-java through 1.1.10.8 contains a buffer overflow vulnerability in Snappy.compress(ByteBuffer, ByteBuffer) that writes past the end of the destination buffer. Attackers can supply incompressible data that exceeds the destination buffer's remaining capacity, corrupting off-heap memory and causing JVM termination.

#### References

* https://guide.sonatype.com/vulnerability/CVE-2026-93452?component-type=maven&component-name=org.xerial.snappy%2Fsnappy-java&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93452
* https://github.com/xerial/snappy-java/issues/732

## Security

* #519: Fixed vulnerability CVE-2026-104721 in dependency `ch.qos.logback:logback-classic:jar:1.6.0:compile`
* #519: Fixed vulnerability CVE-2026-19880 in dependency `ch.qos.logback:logback-classic:jar:1.6.0:compile`
* #486: Fixed vulnerability CVE-2026-68498 in dependency `com.fasterxml.jackson.core:jackson-core:jar:2.22.1:compile`
* #532: Fixed vulnerability CVE-2026-110879 in dependency `com.fasterxml.jackson.core:jackson-databind:jar:2.22.1:compile`
* #469: Fixed vulnerability CVE-2026-87795 in dependency `com.github.luben:zstd-jni:jar:1.5.5-4:compile`
* #470: Fixed vulnerability CVE-2026-87823 in dependency `com.github.luben:zstd-jni:jar:1.5.5-4:compile`
* #471: Fixed vulnerability CVE-2026-89045 in dependency `com.github.luben:zstd-jni:jar:1.5.5-4:compile`
* #467: Fixed vulnerability CVE-2026-86231 in dependency `com.github.mwiede:jsch:jar:2.28.5:compile`
* #512: Fixed vulnerability CVE-2026-15432 in dependency `com.google.crypto.tink:tink:jar:1.9.0:compile`
* #531: Fixed vulnerability CVE-2026-102554 in dependency `com.google.guava:guava:jar:33.6.0-jre:compile`
* #533: Fixed vulnerability CVE-2026-109395 in dependency `com.ning:compress-lzf:jar:1.1.2:compile`
* #515: Fixed vulnerability CVE-2026-100665 in dependency `io.netty:netty-codec-classes-quic:jar:4.2.16.Final:compile`
* #468: Fixed vulnerability CVE-2026-89044 in dependency `io.netty:netty-codec-http:jar:4.2.16.Final:compile`
* #493: Fixed vulnerability CVE-2026-93488 in dependency `io.netty:netty-codec-http:jar:4.2.16.Final:compile`
* #494: Fixed vulnerability CVE-2026-93491 in dependency `io.netty:netty-codec-http:jar:4.2.16.Final:compile`
* #495: Fixed vulnerability CVE-2026-93558 in dependency `io.netty:netty-codec-http:jar:4.2.16.Final:compile`
* #496: Fixed vulnerability CVE-2026-93562 in dependency `io.netty:netty-codec-http:jar:4.2.16.Final:compile`
* #497: Fixed vulnerability CVE-2026-93565 in dependency `io.netty:netty-codec-http:jar:4.2.16.Final:compile`
* #498: Fixed vulnerability CVE-2026-93574 in dependency `io.netty:netty-codec-http:jar:4.2.16.Final:compile`
* #499: Fixed vulnerability CVE-2026-93566 in dependency `io.netty:netty-codec-http:jar:4.2.16.Final:compile`
* #500: Fixed vulnerability CVE-2026-93573 in dependency `io.netty:netty-codec-http:jar:4.2.16.Final:compile`
* #492: Fixed vulnerability CVE-2026-100666 in dependency `io.netty:netty-codec-http:jar:4.2.16.Final:compile`
* #468: Fixed vulnerability CVE-2026-89044 in dependency `io.netty:netty-codec-http2:jar:4.2.16.Final:compile`
* #501: Fixed vulnerability CVE-2026-93492 in dependency `io.netty:netty-codec-http2:jar:4.2.16.Final:compile`
* #502: Fixed vulnerability CVE-2026-93567 in dependency `io.netty:netty-codec-http2:jar:4.2.16.Final:compile`
* #511: Fixed vulnerability CVE-2026-93568 in dependency `io.netty:netty-codec-http2:jar:4.2.16.Final:compile`
* #504: Fixed vulnerability CVE-2026-93579 in dependency `io.netty:netty-codec-http2:jar:4.2.16.Final:compile`
* #505: Fixed vulnerability CVE-2026-100659 in dependency `io.netty:netty-codec-http3:jar:4.2.16.Final:compile`
* #506: Fixed vulnerability CVE-2026-100660 in dependency `io.netty:netty-codec-http3:jar:4.2.16.Final:compile`
* #507: Fixed vulnerability CVE-2026-100661 in dependency `io.netty:netty-codec-http3:jar:4.2.16.Final:compile`
* #508: Fixed vulnerability CVE-2026-100662 in dependency `io.netty:netty-codec-http3:jar:4.2.16.Final:compile`
* #509: Fixed vulnerability CVE-2026-100663 in dependency `io.netty:netty-codec-http3:jar:4.2.16.Final:compile`
* #510: Fixed vulnerability CVE-2026-100664 in dependency `io.netty:netty-codec-http3:jar:4.2.16.Final:compile`
* #468: Fixed vulnerability CVE-2026-89044 in dependency `io.netty:netty-codec-http3:jar:4.2.16.Final:compile`
* #511: Fixed vulnerability CVE-2026-93568 in dependency `io.netty:netty-codec-http3:jar:4.2.16.Final:compile`
* #460: Fixed vulnerability CVE-2026-62380 in dependency `io.netty:netty-codec-socks:jar:4.2.16.Final:compile`
* #463: Fixed vulnerability CVE-2026-62243 in dependency `io.netty:netty-handler:jar:4.2.16.Final:compile`
* #461: Fixed vulnerability CVE-2026-71290 in dependency `org.apache.httpcomponents.client5:httpclient5:jar:5.6.2:compile`
* #454: Fixed vulnerability CVE-2026-57914 in dependency `org.apache.kerby:kerby-asn1:jar:2.0.3:compile`
* #464: Fixed vulnerability CVE-2026-73334 in dependency `org.apache.parquet:parquet-hadoop:jar:1.13.1:compile`
* #480: Fixed vulnerability CVE-2026-59739 in dependency `org.apache.zookeeper:zookeeper:jar:3.9.5:compile`
* #481: Fixed vulnerability CVE-2026-59969 in dependency `org.apache.zookeeper:zookeeper:jar:3.9.5:compile`
* #482: Fixed vulnerability CVE-2026-79993 in dependency `org.apache.zookeeper:zookeeper:jar:3.9.5:compile`
* #483: Fixed vulnerability CVE-2026-84439 in dependency `org.apache.zookeeper:zookeeper:jar:3.9.5:compile`
* #484: Fixed vulnerability CVE-2026-84501 in dependency `org.apache.zookeeper:zookeeper:jar:3.9.5:compile`
* #518: Fixed vulnerability CVE-2026-17508 in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.85:compile`
* #473: Fixed vulnerability CVE-2026-18036 in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.85:compile`
* #474: Fixed vulnerability CVE-2026-18040 in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.85:compile`
* #475: Fixed vulnerability CVE-2026-71889 in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.85:compile`
* #476: Fixed vulnerability CVE-2026-71891 in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.85:compile`
* #518: Fixed vulnerability CVE-2026-97873 in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.85:compile`
* #528: Fixed vulnerability CVE-2026-18659 in dependency `org.eclipse.jetty:jetty-security:jar:9.4.57.v20241219:compile`
* #529: Fixed vulnerability CVE-2026-18662 in dependency `org.eclipse.jetty:jetty-security:jar:9.4.57.v20241219:compile`
* #530: Fixed vulnerability CVE-2026-19864 in dependency `org.eclipse.jetty:jetty-security:jar:9.4.57.v20241219:compile`
* #487: Fixed vulnerability CVE-2026-77420 in dependency `org.jline:jline:jar:3.9.0:compile`
* #488: Fixed vulnerability CVE-2026-77422 in dependency `org.jline:jline:jar:3.9.0:compile`
* Fixed vulnerability CVE-2026-116486 in dependency `org.objenesis:objenesis:jar:3.3:compile`
* #525: Fixed vulnerability CVE-2026-108104 in dependency `org.xerial.snappy:snappy-java:jar:1.1.10.8:compile`
* #526: Fixed vulnerability CVE-2026-108106 in dependency `org.xerial.snappy:snappy-java:jar:1.1.10.8:compile`
* #485: Fixed vulnerability CVE-2026-90559 in dependency `org.xerial.snappy:snappy-java:jar:1.1.10.8:compile`
* #513: Fixed vulnerability CVE-2026-93451 in dependency `org.xerial.snappy:snappy-java:jar:1.1.10.8:compile`
* #514: Fixed vulnerability CVE-2026-93452 in dependency `org.xerial.snappy:snappy-java:jar:1.1.10.8:compile`

## Dependency Updates

### Compile Dependency Updates

* Updated `at.yawk.lz4:lz4-java:1.11.1` to `1.12.1`
* Updated `com.github.mwiede:jsch:2.28.5` to `2.28.8`
* Updated `com.google.guava:guava:33.6.0-jre` to `33.7.2-jre`
* Updated `com.nimbusds:nimbus-jose-jwt:10.9.1` to `10.10`
* Updated `io.dropwizard.metrics:metrics-core:4.2.39` to `4.2.40`
* Updated `org.apache.avro:avro:1.12.1` to `1.12.2`
* Updated `org.apache.commons:commons-lang3:3.20.0` to `3.21.0`
* Added `org.apache.parquet:parquet-column:1.18.1`
* Added `org.apache.parquet:parquet-common:1.18.1`
* Updated `org.apache.zookeeper:zookeeper:3.9.5` to `3.9.6`
* Updated `org.slf4j:jul-to-slf4j:2.0.18` to `2.0.20`
* Updated `org.slf4j:slf4j-api:2.0.18` to `2.0.20`
* Updated `org.xerial.snappy:snappy-java:1.1.10.8` to `1.1.10.12`
* Updated `software.amazon.awssdk:s3-transfer-manager:2.49.3` to `2.57.2`
* Updated `software.amazon.awssdk:s3:2.49.3` to `2.57.2`

### Runtime Dependency Updates

* Updated `ch.qos.logback:logback-classic:1.6.0` to `1.6.5`
* Updated `ch.qos.logback:logback-core:1.6.0` to `1.6.5`
* Updated `software.amazon.awssdk:apache-client:2.49.3` to `2.57.2`

### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:8.0.1` to `8.0.2`
* Removed `com.exasol:extension-manager-integration-test-java:0.5.20`
* Updated `com.exasol:hamcrest-resultset-matcher:1.7.2` to `1.7.4`
* Removed `com.exasol:maven-project-version-getter:1.2.2`
* Updated `com.exasol:test-db-builder-java:4.0.1` to `4.0.3`
* Updated `org.mockito:mockito-core:5.23.0` to `5.24.0`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.1.0` to `2.1.2`
* Updated `com.exasol:project-keeper-maven-plugin:5.7.4` to `5.8.1`
* Updated `io.github.git-commit-id:git-commit-id-maven-plugin:10.0.0` to `10.0.1`
* Updated `org.apache.maven.plugins:maven-artifact-plugin:3.6.1` to `3.7.0`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.15.0` to `3.16.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.6` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-install-plugin:3.1.4` to `3.2.0`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.5.0` to `3.5.1`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.6` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-toolchains-plugin:3.2.0` to `3.3.0`
* Removed `org.codehaus.mojo:exec-maven-plugin:3.6.3`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.7.3` to `1.8.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.21.0` to `2.23.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.7.0.6970` to `5.8.0.7211`
