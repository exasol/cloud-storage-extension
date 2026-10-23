# Developers Guide

This guide contains information for developers.

## JVM Test Logging

JVM tests use Logback as the SLF4J backend. Many test dependencies, including Testcontainers and Docker client libraries, log through SLF4J. Keeping one backend avoids conflicting SLF4J providers on the test classpath and lets us control noisy third-party output consistently. In particular, Docker Java HTTP wire logs must stay disabled by default because they can produce very large integration-test logs.

Configure SLF4J test logging in [src/test/resources/logback-test.xml](../../src/test/resources/logback-test.xml). Logback loads this file from the test classpath. Use the root logger for the default test log level and package-specific `<logger>` entries for noisy dependencies, for example:

```xml
<logger name="com.github.dockerjava" level="WARN"/>
<logger name="org.testcontainers" level="INFO"/>

<root level="INFO">
    <appender-ref ref="CONSOLE"/>
</root>
```

For local troubleshooting, temporarily lower a package logger level in `logback-test.xml` or use an IDE-specific run configuration. Do not commit broad `DEBUG` or `TRACE` settings for `com.github.dockerjava`, Apache HTTP clients, or Testcontainers unless the corresponding log volume is intentional. Java Util Logging output is configured separately in [src/test/resources/logging.properties](../../src/test/resources/logging.properties), which Maven passes via `java.util.logging.config.file`.
