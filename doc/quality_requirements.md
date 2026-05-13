# Quality Requirements

This chapter documents architecture-relevant quality requirements and technical quality goals.

User-facing acceptance scenarios are defined in [System Requirements](../system_requirements.md).

## Requirement Quality

Changes that affect user-visible behavior or architecture must be traced from a GitHub issue or changeset to requirements, design notes, implementation, and verification. The preferred OFT hierarchy is:

1. `feat`: top-level feature
2. `req`: user requirement
3. `scn`: Given-When-Then acceptance scenario
4. `constr`: architecture constraint
5. `dsn`: design requirement covering scenarios and constraints
6. `impl`: implementation
7. `utest`: unit test
8. `itest`: integration test

Runtime design requirements `dsn` should cover one scenario or constraint at a time. Use OFT forwarding notation if a design layer adds no new information.

## Code Quality

Production code must compile with Java 11 and Maven's compiler warnings treated as errors. The compiler is configured with `-Xlint:all,-path,-processing` and `-Werror`, so new code must avoid unchecked operations, deprecated API usage, and unused or misleading declarations.

Production code should be Java under `src/main/java`. Scala compatibility helpers are allowed only at boundaries where existing Scala tests or dependencies still require Scala types.

Public error messages must use `error-reporting-java` message builders and include actionable mitigations when the caller can correct the problem.

## Test Quality

The project uses ScalaTest for JVM tests. Unit and integration tests must preserve existing behavior, cover supported storage backends and file formats, and keep contract checks such as `equalsverifier` focused on classes that intentionally implement value equality.

Tests must use deterministic local fixtures where possible. Integration tests that require Exasol or external services must be isolated behind Maven profiles, CI workflow setup, or Testcontainers configuration.

## Dependency Policy

JVM dependencies are managed in `pom.xml`, with selected versions centralized in Maven properties or dependency management when shared. Transitive dependency overrides must document the CVE, compatibility issue, or runtime reason that requires the override.

The browser extension dependencies are managed by `extension/package.json` and its lock file. Dependency update automation must keep generated project metadata consistent with Project Keeper.

## Static Analysis and Security Gates

CI runs Maven verification, Project Keeper verification, the error-code crawler, duplicate dependency checks, and OSS Index vulnerability audits. Release workflows must pass the same project verification gates before publishing.

Generated files such as `pk_generated_parent.pom` must be updated through Project Keeper instead of edited manually.

## Testability and Coverage

The full verification suite must include compilation, unit tests, integration tests, static checks, and extension build checks. For the Scala-to-Java migration in issue #393, production behavior must remain unchanged, all existing tests must stay green, and overall code coverage must remain above 80%.

Manual verification is required only for behavior that cannot be exercised by the automated suite, such as release packaging or external cloud provider credentials.

## Open Issues

None currently known.
