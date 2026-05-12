# Developers Guide

This guide contains information for developers.

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
