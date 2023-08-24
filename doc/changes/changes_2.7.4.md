# Cloud Storage Extension 2.7.4, released 2023-??-??

Code name: Upgrade Dependencies

## Summary

This release fixes vulnerability CVE-2022-46751 in transitive dependency `org.apache.ivy:ivy` by upgrading it to the latest version.

The release also updates the extension to use common code from `@exasol/extension-manager-interface`.

## Security

* #269: Fixed CVE-2022-46751 in `org.apache.ivy:ivy`

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Added `org.apache.ivy:ivy:2.5.2`

### Extension

#### Compile Dependency Updates

* Updated `@exasol/extension-manager-interface:0.3.0` to `0.3.1`

#### Development Dependency Updates

* Updated `eslint:^8.46.0` to `^8.47.0`
* Added `@jest/globals:^29.6.3`
* Updated `@types/node:^20.4.9` to `^20.5.4`
* Updated `@typescript-eslint/parser:^6.3.0` to `^6.4.1`
* Updated `@typescript-eslint/eslint-plugin:^6.3.0` to `^6.4.1`
* Updated `jest:29.6.2` to `29.6.3`
* Updated `esbuild:^0.19.0` to `^0.19.2`
* Removed `@types/jest:^29.5.3`
