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

## Working With the Managed Extension

This describes how to develop the extension for the [Extension Manager](https://github.com/exasol/extension-manager/).

### Running Unit Tests

```shell
cd extension
npm install
npm run build && npm test
```

To run tests continuously each time a file is changed on disk (useful during development), start the following command:

```shell
npm run test-watch
```

### Running Integration Tests With a Local Extension Manager

To use a local, non-published version of the extension manager during integration tests, first checkout the [extension-manager](https://github.com/exasol/extension-manager). Then create file `extension-test.properties` in this project directory with the following content:

```properties
localExtensionManager = /path/to/extension-manager
```

This will build the extension manager from source and use the built executable for the integration tests.

### Running Linter

To run static code analysis for the extension code, run

```shell
cd extension
npm run lint
```

### Using a Local Extension Manager Interface

To use a local, non-published version of the extension manager interface during development, edit [extension/package.json](../../extension/package.json) and replace the version of `"@exasol/extension-manager-interface"` with the path to your local clone of [extension-manager-interface](https://github.com/exasol/extension-manager-interface).

Then run `npm install` and restart your IDE.
