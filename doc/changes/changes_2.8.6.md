# Cloud Storage Extension 2.8.6, released 2025-03-??

Code name: Security fixes in NPM components, netty upgrade

## Summary
Fixed CVE-2024-4068 in braces

## Security

* #340: Fix CVE-2024-4068 in braces
* #344: Fix CVE-2025-24970: io.netty:netty-handler:jar:4.1.115.Final:compile
* #345: Fix CVE-2025-25193: io.netty:netty-common:jar:4.1.115.Final:compile
* Add SECURITY.md file

## Dependency Updates

### Cloud Storage Extension

#### Compile Dependency Updates

* Updated `io.netty:netty-codec-http2:4.1.115.Final` to `4.1.119.Final`

### Extension

#### Development Dependency Updates

* Updated `eslint:9.14.0` to `9.18.0`
* Updated `@types/node:^22.9.1` to `^22.10.7`
* Updated `typescript-eslint:^8.14.0` to `^8.20.0`
* Updated `typescript:^5.6.3` to `^5.7.3`
* Updated `esbuild:^0.24.0` to `^0.24.2`
