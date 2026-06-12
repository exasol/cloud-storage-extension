# Cloud Storage Extension 2.9.6, released 2026-06-12

Code name: Fixed vulnerabilities CVE-2026-42587, CVE-2026-45205, CVE-2026-41417, CVE-2026-42580, CVE-2026-42581, CVE-2026-42584, CVE-2026-42585, CVE-2026-42587, CVE-2026-0636, CVE-2026-5588, CVE-2026-5598, CVE-2026-42578, CVE-2026-42582, CVE-2026-42583, CVE-2026-34477, CVE-2026-34478, CVE-2026-34479, CVE-2026-34480, CVE-2026-42577

## Summary

This release fixes the following 19 vulnerabilities:

### CVE-2026-42587 (CWE-400) in dependency `io.netty:netty-codec-http2:jar:4.2.12.Final:compile`
Netty is an asynchronous, event-driven network application framework. Prior to 4.2.13.Final and 4.1.133.Final, HttpContentDecompressor accepts a maxAllocation parameter to limit decompression buffer size and prevent decompression bomb attacks. This limit is correctly enforced for gzip and deflate encodings via ZlibDecoder, but is silently ignored when the content encoding is br (Brotli), zstd, or snappy. An attacker can bypass the configured decompression limit by sending a compressed payload with Content-Encoding: br instead of Content-Encoding: gzip, causing unbounded memory allocation and out-of-memory denial of service. The same vulnerability exists in DelegatingDecompressorFrameListener for HTTP/2 connections. This vulnerability is fixed in 4.2.13.Final and 4.1.133.Final.

Sonatype's research suggests that this CVE's details differ from those defined at NVD. See https://guide.sonatype.com/vulnerability/CVE-2026-42587 for details
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-42587?component-type=maven&component-name=io.netty%2Fnetty-codec-http2&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-42587
* https://github.com/advisories/GHSA-f6hv-jmp6-3vwv

### CVE-2026-45205 (CWE-674) in dependency `org.apache.commons:commons-configuration2:jar:2.11.0:compile`
Uncontrolled Recursion vulnerability in Apache Commons.

When processing an untrusted configuration file, Commons Configuration will throw a StackOverflowError for YAML input with cycles.
This issue affects Apache Commons: from 2.2 before 2.15.0.

Users are recommended to upgrade to version 2.15.0, which fixes the issue.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-45205?component-type=maven&component-name=org.apache.commons%2Fcommons-configuration2&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-45205
* https://github.com/advisories/GHSA-337m-mw94-2v6g
* https://lists.apache.org/thread/q3q3j10ohcqhs6o0rg1v7kz6kk27vtkk

### CVE-2026-41417 (CWE-444) in dependency `io.netty:netty-codec-http:jar:4.2.12.Final:compile`
Netty allows request-line validation to be bypassed when a `DefaultHttpRequest` or `DefaultFullHttpRequest` is created first and its URI is later changed via `setUri()`. The constructors reject CRLF and whitespace characters that would break the start-line, but `setUri()` does not apply the same validation. `HttpRequestEncoder` and `RtspEncoder` then write the URI into the request line verbatim. If attacker-controlled input reaches `setUri()`, this enables CRLF injection and insertion of additional HTTP or RTSP requests, leading to HTTP request smuggling or desynchronization on the HTTP side and request injection on the RTSP side. This issue is fixed in versions 4.2.13.Final and 4.1.133.Final.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-41417?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-41417
* https://github.com/advisories/GHSA-v8h7-rr48-vmmv

### CVE-2026-42580 (CWE-190) in dependency `io.netty:netty-codec-http:jar:4.2.12.Final:compile`
Netty is an asynchronous, event-driven network application framework. Prior to 4.2.13.Final and 4.1.133.Final, Netty's chunk size parser silently overflows int, enabling request smuggling attacks. This vulnerability is fixed in 4.2.13.Final and 4.1.133.Final.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-42580?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-42580
* https://github.com/advisories/GHSA-m4cv-j2px-7723

### CVE-2026-42581 (CWE-444) in dependency `io.netty:netty-codec-http:jar:4.2.12.Final:compile`
Netty is an asynchronous, event-driven network application framework. Prior to 4.2.13.Final and 4.1.133.Final, HttpObjectDecoder strips a conflicting Content-Length header when a request carries both Transfer-Encoding: chunked and Content-Length, but only for HTTP/1.1 messages. The guard is absent for HTTP/1.0. An attacker that sends an HTTP/1.0 request with both headers causes Netty to decode the body as chunked while leaving Content-Length intact in the forwarded HttpMessage. Any downstream proxy or handler that trusts Content-Length over Transfer-Encoding will disagree on message boundaries, enabling request smuggling. This vulnerability is fixed in 4.2.13.Final and 4.1.133.Final.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-42581?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-42581
* https://github.com/advisories/GHSA-xxqh-mfjm-7mv9

### CVE-2026-42584 (CWE-444) in dependency `io.netty:netty-codec-http:jar:4.2.12.Final:compile`
Netty is an asynchronous, event-driven network application framework. Prior to 4.2.13.Final and 4.1.133.Final, HttpClientCodec pairs each inbound response with an outbound request by queue.poll() once per response, including for 1xx. If the client pipelines GET then HEAD and the server sends 103, then 200 with GET body, then 200 for HEAD, the queue pairs HEAD with the first 200. The HEAD rule then skips reading that messageâs body, so the GET entity bytes stay on the stream and the following 200 is parsed from the wrong offset. This vulnerability is fixed in 4.2.13.Final and 4.1.133.Final.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-42584?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-42584
* https://github.com/advisories/GHSA-57rv-r2g8-2cj3

### CVE-2026-42585 (CWE-444) in dependency `io.netty:netty-codec-http:jar:4.2.12.Final:compile`
Netty is an asynchronous, event-driven network application framework. Prior to 4.2.13.Final and 4.1.133.Final, Netty incorrectly parses malformed Transfer-Encoding, enabling request smuggling attacks. This vulnerability is fixed in 4.2.13.Final and 4.1.133.Final.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-42585?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-42585
* https://github.com/netty/netty/security/advisories/GHSA-38f8-5428-x5cv
* https://github.com/advisories/GHSA-38f8-5428-x5cv

### CVE-2026-42587 (CWE-400) in dependency `io.netty:netty-codec-http:jar:4.2.12.Final:compile`
Netty is an asynchronous, event-driven network application framework. Prior to 4.2.13.Final and 4.1.133.Final, HttpContentDecompressor accepts a maxAllocation parameter to limit decompression buffer size and prevent decompression bomb attacks. This limit is correctly enforced for gzip and deflate encodings via ZlibDecoder, but is silently ignored when the content encoding is br (Brotli), zstd, or snappy. An attacker can bypass the configured decompression limit by sending a compressed payload with Content-Encoding: br instead of Content-Encoding: gzip, causing unbounded memory allocation and out-of-memory denial of service. The same vulnerability exists in DelegatingDecompressorFrameListener for HTTP/2 connections. This vulnerability is fixed in 4.2.13.Final and 4.1.133.Final.

Sonatype's research suggests that this CVE's details differ from those defined at NVD. See https://guide.sonatype.com/vulnerability/CVE-2026-42587 for details
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-42587?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-42587
* https://github.com/advisories/GHSA-f6hv-jmp6-3vwv

### CVE-2026-0636 (CWE-90) in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.78.1:compile`
Improper neutralization of special elements used in an LDAP query ('LDAP injection') vulnerability in Legion of the Bouncy Castle Inc. BC-JAVA bcprov on all (prov modules).

 This vulnerability is associated with program files LDAPStoreHelper.

This issue affects BC-JAVA: from 1.74 before 1.80.2, from 1.81 before 1.81.1, from 1.82 before 1.84.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-0636?component-type=maven&component-name=org.bouncycastle%2Fbcprov-jdk18on&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-0636
* https://github.com/advisories/GHSA-c3fc-8qff-9hwx

### CVE-2026-5588 (CWE-327) in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.78.1:compile`
Use of a Broken or Risky Cryptographic Algorithm vulnerability in Legion of the Bouncy Castle Inc. BC-JAVA bcpkix on all (pkix modules), Legion of the Bouncy Castle Inc. BCPKIX-FIPS bcpkix on All (pkix modules), Legion of the Bouncy Castle Inc. BCPIX-LTS bcpkix on All (pkix modules).

 This vulnerability is associated with program files JcaContentVerifierProviderBuilder.Java, JcaContentVerfierProviderBuilder.Java.

This issue affects BC-JAVA: from 1.67 before 1.80.2, from 1.81 before 1.81.1, from 1.82 before 1.84; BCPKIX-FIPS: from 2.0.6 before 2.0.11, from 2.1.7 before 2.1.11; BCPIX-LTS: from 2.73.7 before 2.73.11.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-5588?component-type=maven&component-name=org.bouncycastle%2Fbcprov-jdk18on&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-5588
* https://github.com/bcgit/bc-java/wiki/CVE%E2%80%902026%E2%80%905588

### CVE-2026-5598 (CWE-385) in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.78.1:compile`
Covert timing channel vulnerability in Legion of the Bouncy Castle Inc. BC-JAVA core on all (core modules).

 This vulnerability is associated with program files FrodoEngine.Java.

This issue affects BC-JAVA: from 1.71 before 1.80.2, from 1.81 before 1.80.1, from 1.82 before 1.84.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-5598?component-type=maven&component-name=org.bouncycastle%2Fbcprov-jdk18on&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-5598
* https://access.redhat.com/security/cve/cve-2026-5598

### CVE-2026-42578 (CWE-113) in dependency `io.netty:netty-handler-proxy:jar:4.2.12.Final:compile`
Netty is an asynchronous, event-driven network application framework. Prior to 4.2.13.Final and 4.1.133.Final, Netty's HttpProxyHandler constructs HTTP CONNECT requests with header validation explicitly disabled. The newInitialMessage() method creates headers using DefaultHttpHeadersFactory.headersFactory().withValidation(false), then adds user-provided outboundHeaders without any CRLF validation. This allows an attacker who can influence the outbound headers to inject arbitrary HTTP headers into the CONNECT request sent to the proxy server. This vulnerability is fixed in 4.2.13.Final and 4.1.133.Final.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-42578?component-type=maven&component-name=io.netty%2Fnetty-handler-proxy&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-42578
* https://github.com/advisories/GHSA-45q3-82m4-75jr

### CVE-2026-42582 (CWE-770) in dependency `io.netty:netty-codec-http3:jar:4.2.12.Final:compile`
Netty is an asynchronous, event-driven network application framework. Prior to 4.2.13.Final, when decoding header blocks, the non-Huffman branch of io.netty.handler.codec.http3.QpackDecoder#decodeHuffmanEncodedLiteral may execute new byte[length] for a string literal before verifying that length bytes are actually present in the compressed field section. The wire encoding allows a very large length to be expressed in few bytes. There is no check that length <= in.readableBytes() before new byte[length]. This vulnerability is fixed in 4.2.13.Final.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-42582?component-type=maven&component-name=io.netty%2Fnetty-codec-http3&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-42582
* https://github.com/advisories/GHSA-2c5c-chwr-9hqw

### CVE-2026-42583 (CWE-400) in dependency `io.netty:netty-codec-compression:jar:4.2.12.Final:compile`
Netty is an asynchronous, event-driven network application framework. Prior to 4.2.13.Final and 4.1.133.Final, Lz4FrameDecoder allocates a ByteBuf of size decompressedLength (up to 32 MB per block) before LZ4 runs. A peer only needs a 21-byte header plus compressedLength payload bytes - 22 bytes if compressedLength == 1 - to force that allocation. This vulnerability is fixed in 4.2.13.Final and 4.1.133.Final.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-42583?component-type=maven&component-name=io.netty%2Fnetty-codec-compression&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-42583
* https://github.com/advisories/GHSA-mj4r-2hfc-f8p6

### CVE-2026-34477 (CWE-295) in dependency `org.apache.logging.log4j:log4j-core:jar:2.25.3:compile`
The fix for  CVE-2025-68161 https://logging.apache.org/security.html#CVE-2025-68161  was incomplete: it addressed hostname verification only when enabled via the  log4j2.sslVerifyHostName https://logging.apache.org/log4j/2.x/manual/systemproperties.html#log4j2.sslVerifyHostName  system property, but not when configured through the  verifyHostName https://logging.apache.org/log4j/2.x/manual/appenders/network.html#SslConfiguration-attr-verifyHostName  attribute of the <Ssl> element.

Although the verifyHostName configuration attribute was introduced in Log4j Core 2.12.0, it was silently ignored in all versions through 2.25.3, leaving TLS connections vulnerable to interception regardless of the configured value.

A network-based attacker may be able to perform a man-in-the-middle attack when all of the following conditions are met:

  *  An SMTP, Socket, or Syslog appender is in use.
  *  TLS is configured via a nested <Ssl> element.
  *  The attacker can present a certificate issued by a CA trusted by the appender's configured trust store, or by the default Java trust store if none is configured.
This issue does not affect users of the HTTP appender, which uses a separate  verifyHostname https://logging.apache.org/log4j/2.x/manual/appenders/network.html#HttpAppender-attr-verifyHostName  attribute that was not subject to this bug and verifies host names by default.

Users are advised to upgrade to Apache Log4j Core 2.25.4, which corrects this issue.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-34477?component-type=maven&component-name=org.apache.logging.log4j%2Flog4j-core&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-34477
* https://github.com/apache/logging-log4j2/pull/4075
* https://lists.apache.org/thread/lkx8cl46t2bvkcwfcb2pd43ygc097lq4
* https://github.com/advisories/GHSA-6hg6-v5c8-fphq

### CVE-2026-34478 (CWE-117) in dependency `org.apache.logging.log4j:log4j-core:jar:2.25.3:compile`
Apache Log4j Core's  Rfc5424Layout https://logging.apache.org/log4j/2.x/manual/layouts.html#RFC5424Layout , in versions 2.21.0 through 2.25.3, is vulnerable to log injection via CRLF sequences due to undocumented renames of security-relevant configuration attributes.

Two distinct issues affect users of stream-based syslog services who configure Rfc5424Layout directly:

  *  The newLineEscape attribute was silently renamed, causing newline escaping to stop working for users of TCP framing (RFC 6587), exposing them to CRLF injection in log output.
  *  The useTlsMessageFormat attribute was silently renamed, causing users of TLS framing (RFC 5425) to be silently downgraded to unframed TCP (RFC 6587), without newline escaping.

Users of the SyslogAppender are not affected, as its configuration attributes were not modified.

Users are advised to upgrade to Apache Log4j Core 2.25.4, which corrects this issue.

Sonatype's research suggests that this CVE's details differ from those defined at NVD. See https://guide.sonatype.com/vulnerability/CVE-2026-34478 for details
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-34478?component-type=maven&component-name=org.apache.logging.log4j%2Flog4j-core&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-34478
* https://logging.apache.org/security.html#CVE-2026-34478
* https://github.com/advisories/GHSA-445c-vh5m-36rj

### CVE-2026-34479 (CWE-116) in dependency `org.apache.logging.log4j:log4j-core:jar:2.25.3:compile`
The Log4j1XmlLayout from the Apache Log4j 1-to-Log4j 2 bridge fails to escape characters forbidden by the XML 1.0 standard, producing malformed XML output. Conforming XML parsers are required to reject documents containing such characters with a fatal error, which may cause downstream log processing systems to drop or fail to index affected records.

Two groups of users are affected:

  *  Those using Log4j1XmlLayout directly in a Log4j Core 2 configuration file.
  *  Those using the Log4j 1 configuration compatibility layer with org.apache.log4j.xml.XMLLayout specified as the layout class.

Users are advised to upgrade to Apache Log4j 1-to-Log4j 2 bridge version 2.25.4, which corrects this issue.

Note: The Apache Log4j 1-to-Log4j 2 bridge is deprecated and will not be present in Log4j 3. Users are encouraged to consult the  Log4j 1 to Log4j 2 migration guide https://logging.apache.org/log4j/2.x/migrate-from-log4j1.html , and specifically the section on eliminating reliance on the bridge.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-34479?component-type=maven&component-name=org.apache.logging.log4j%2Flog4j-core&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-34479
* https://logging.apache.org/security.html#CVE-2026-34479

### CVE-2026-34480 (CWE-116) in dependency `org.apache.logging.log4j:log4j-core:jar:2.25.3:compile`
Apache Log4j Core's  XmlLayout https://logging.apache.org/log4j/2.x/manual/layouts.html#XmlLayout , in versions up to and including 2.25.3, fails to sanitize characters forbidden by the  XML 1.0 specification https://www.w3.org/TR/xml/#charsets  producing invalid XML output whenever a log message or MDC value contains such characters.

The impact depends on the StAX implementation in use:

  *  JRE built-in StAX: Forbidden characters are silently written to the output, producing malformed XML. Conforming parsers must reject such documents with a fatal error, which may cause downstream log-processing systems to drop the affected records.
  *  Alternative StAX implementations (e.g.,  Woodstox https://github.com/FasterXML/woodstox , a transitive dependency of the Jackson XML Dataformat module): An exception is thrown during the logging call, and the log event is never delivered to its intended appender, only to Log4j's internal status logger.

Users are advised to upgrade to Apache Log4j Core 2.25.4, which corrects this issue by sanitizing forbidden characters before XML output.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-34480?component-type=maven&component-name=org.apache.logging.log4j%2Flog4j-core&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-34480
* https://github.com/apache/logging-log4j2/pull/4077
* https://logging.apache.org/security.html#CVE-2026-34480
* https://github.com/advisories/GHSA-3pxv-7cmr-fjr4

### CVE-2026-42577 (CWE-772) in dependency `io.netty:netty-transport-classes-epoll:jar:4.2.12.Final:compile`
Netty is an asynchronous, event-driven network application framework. From 4.2.0.Final to 4.2.13.Final , Netty's epoll transport fails to detect and close TCP connections that receive a RST after being half-closed, leading to stale channels that are never cleaned up and, in some code paths, a 100% CPU busy-loop in the event loop thread. This vulnerability is fixed in 4.2.13.Final.

Sonatype's research suggests that this CVE's details differ from those defined at NVD. See https://guide.sonatype.com/vulnerability/CVE-2026-42577 for details
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-42577?component-type=maven&component-name=io.netty%2Fnetty-transport-classes-epoll&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-42577
* https://github.com/advisories/GHSA-rwm7-x88c-3g2p

## Security

* #408: Fixed vulnerability CVE-2026-42587 in dependency `io.netty:netty-codec-http2:jar:4.2.12.Final:compile`
* #409: Fixed vulnerability CVE-2026-45205 in dependency `org.apache.commons:commons-configuration2:jar:2.11.0:compile`
* #410: Fixed vulnerability CVE-2026-41417 in dependency `io.netty:netty-codec-http:jar:4.2.12.Final:compile`
* #411: Fixed vulnerability CVE-2026-42580 in dependency `io.netty:netty-codec-http:jar:4.2.12.Final:compile`
* #412: Fixed vulnerability CVE-2026-42581 in dependency `io.netty:netty-codec-http:jar:4.2.12.Final:compile`
* #413: Fixed vulnerability CVE-2026-42584 in dependency `io.netty:netty-codec-http:jar:4.2.12.Final:compile`
* #414: Fixed vulnerability CVE-2026-42585 in dependency `io.netty:netty-codec-http:jar:4.2.12.Final:compile`
* #415: Fixed vulnerability CVE-2026-42587 in dependency `io.netty:netty-codec-http:jar:4.2.12.Final:compile`
* #416: Fixed vulnerability CVE-2026-0636 in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.78.1:compile`
* #417: Fixed vulnerability CVE-2026-5588 in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.78.1:compile`
* #418: Fixed vulnerability CVE-2026-5598 in dependency `org.bouncycastle:bcprov-jdk18on:jar:1.78.1:compile`
* #419: Fixed vulnerability CVE-2026-42578 in dependency `io.netty:netty-handler-proxy:jar:4.2.12.Final:compile`
* #420: Fixed vulnerability CVE-2026-42582 in dependency `io.netty:netty-codec-http3:jar:4.2.12.Final:compile`
* #421: Fixed vulnerability CVE-2026-42583 in dependency `io.netty:netty-codec-compression:jar:4.2.12.Final:compile`
* #422: Fixed vulnerability CVE-2026-34477 in dependency `org.apache.logging.log4j:log4j-core:jar:2.25.3:compile`
* #423: Fixed vulnerability CVE-2026-34478 in dependency `org.apache.logging.log4j:log4j-core:jar:2.25.3:compile`
* #424: Fixed vulnerability CVE-2026-34479 in dependency `org.apache.logging.log4j:log4j-core:jar:2.25.3:compile`
* #425: Fixed vulnerability CVE-2026-34480 in dependency `org.apache.logging.log4j:log4j-core:jar:2.25.3:compile`
* #426: Fixed vulnerability CVE-2026-42577 in dependency `io.netty:netty-transport-classes-epoll:jar:4.2.12.Final:compile`

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Updated `at.yawk.lz4:lz4-java:1.10.2` to `1.11.0`
* Updated `com.exasol:error-reporting-java:1.0.1` to `1.0.2`
* Updated `com.exasol:parquet-io-java:2.0.12` to `2.0.16`
* Updated `com.github.mwiede:jsch:0.2.21` to `2.28.2`
* Updated `com.google.cloud.bigdataoss:gcs-connector:1.9.4-hadoop3` to `3.1.17`
* Updated `com.google.code.gson:gson:2.13.1` to `2.14.0`
* Updated `com.google.guava:guava:33.3.1-jre` to `33.6.0-jre`
* Updated `com.google.oauth-client:google-oauth-client:1.36.0` to `1.39.0`
* Updated `com.google.protobuf:protobuf-java-util:3.25.8` to `4.35.0`
* Updated `com.google.protobuf:protobuf-java:3.25.8` to `4.35.0`
* Updated `com.nimbusds:nimbus-jose-jwt:9.47` to `10.9.1`
* Updated `com.typesafe.scala-logging:scala-logging_2.13:3.9.5` to `3.9.6`
* Updated `commons-io:commons-io:2.18.0` to `2.22.0`
* Updated `dnsjava:dnsjava:3.6.2` to `3.6.5`
* Updated `io.dropwizard.metrics:metrics-core:4.2.28` to `4.2.39`
* Updated `io.grpc:grpc-netty:1.65.1` to `1.81.0`
* Updated `org.apache.commons:commons-compress:1.27.1` to `1.28.0`
* Updated `org.apache.commons:commons-configuration2:2.11.0` to `2.15.1`
* Updated `org.apache.commons:commons-lang3:3.18.0` to `3.20.0`
* Updated `org.apache.hadoop:hadoop-aws:3.4.1` to `3.4.3`
* Updated `org.apache.hadoop:hadoop-azure-datalake:3.4.1` to `3.4.3`
* Updated `org.apache.hadoop:hadoop-azure:3.4.1` to `3.4.3`
* Updated `org.apache.hadoop:hadoop-common:3.4.1` to `3.4.3`
* Updated `org.apache.hadoop:hadoop-hdfs-client:3.4.1` to `3.4.3`
* Updated `org.apache.hadoop:hadoop-hdfs:3.4.1` to `3.4.3`
* Updated `org.apache.ivy:ivy:2.5.2` to `2.5.3`
* Updated `org.apache.logging.log4j:log4j-1.2-api:2.25.3` to `2.26.0`
* Updated `org.apache.logging.log4j:log4j-api:2.25.3` to `2.26.0`
* Updated `org.apache.logging.log4j:log4j-core:2.25.3` to `2.26.0`
* Updated `org.apache.spark:spark-sql_2.13:3.5.7` to `3.5.8`
* Removed `org.glassfish.jersey.containers:jersey-container-servlet-core:2.47`
* Removed `org.glassfish.jersey.containers:jersey-container-servlet:2.47`
* Removed `org.glassfish.jersey.core:jersey-client:2.47`
* Removed `org.glassfish.jersey.core:jersey-common:2.47`
* Removed `org.glassfish.jersey.core:jersey-server:2.47`
* Removed `org.glassfish.jersey.inject:jersey-hk2:2.47`
* Removed `org.jetbrains.kotlin:kotlin-stdlib:1.9.25`
* Updated `org.scala-lang:scala-library:2.13.11` to `2.13.18`
* Updated `org.slf4j:jul-to-slf4j:2.0.16` to `2.0.18`
* Updated `org.xerial.snappy:snappy-java:1.1.10.7` to `1.1.10.8`
* Updated `software.amazon.awssdk:s3-transfer-manager:2.34.0` to `2.46.7`
* Updated `software.amazon.awssdk:s3:2.34.0` to `2.46.7`

#### Runtime Dependency Updates

* Updated `ch.qos.logback:logback-classic:1.5.29` to `1.5.34`
* Updated `ch.qos.logback:logback-core:1.5.29` to `1.5.34`
* Added `io.airlift:aircompressor:2.0.3`
* Added `software.amazon.awssdk:apache-client:2.46.7`

#### Test Dependency Updates

* Updated `com.exasol:exasol-testcontainers:7.1.4` to `7.3.0`
* Updated `com.exasol:hamcrest-resultset-matcher:1.7.0` to `1.7.2`
* Updated `com.exasol:maven-project-version-getter:1.2.0` to `1.2.2`
* Updated `com.exasol:test-db-builder-java:3.6.0` to `4.0.0`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.17.3` to `3.19.4`
* Updated `org.junit.jupiter:junit-jupiter:5.10.3` to `5.14.4`
* Updated `org.mockito:mockito-core:5.12.0` to `5.23.0`

#### Plugin Dependency Updates

* Updated `org.apache.maven.plugins:maven-clean-plugin:3.3.2` to `3.5.0`
* Updated `org.codehaus.mojo:exec-maven-plugin:3.2.0` to `3.6.3`
