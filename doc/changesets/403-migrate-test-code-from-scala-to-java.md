# GH-403 Migrate test code from Scala to Java

## Goal

Migrate the remaining Scala test sources to Java without changing production behavior. This completes the language migration started in GH-393 and reduces build, maintenance, and dependency-management overhead.

## Scope

In scope:

* Translate all test sources under `src/test/scala` to Java under `src/test/java`.
* Preserve the existing unit and integration test behavior.
* Update the Maven test build so Java tests run through the standard Java test plugins.
* Remove test-only ScalaTest and Scala Testcontainers dependencies that are no longer needed.
* Keep overall code coverage above 80%.

Out of scope:

* User-visible behavior changes.
* Production source migration beyond build adjustments required by the test migration.
* Dependency upgrades unrelated to replacing Scala-only test infrastructure.

## Design References

* [Quality Requirements](../quality_requirements.md)
* [Developers Guide](../developers_guide/developers_guide.md)
* [Build Configuration](../../pom.xml)
* [GH-393 Production Migration Changeset](393-migrate-production-code-from-scala-to-java.md)

## Strategy

The migration keeps the same test layering as the Scala suite:

1. Translate package-object helpers and mixins into Java helper classes and base test classes.
2. Convert unit tests to JUnit Jupiter and Mockito.
3. Convert integration-test base classes and Testcontainers setup to Java equivalents.
4. Replace ScalaTest Maven execution with Surefire and Failsafe execution for Java tests.
5. Remove `src/test/scala` and verify that no Scala test sources remain.

## Task List

- [x] Create and checkout branch `issue-403-migrate-test-code-from-scala-to-java`.

### Requirements And Design

- [x] Confirm the issue is a pure refactor with no user-visible requirement change.
- [x] Keep `doc/quality_requirements.md` as the controlling verification reference.
- [x] Stop and ask user for a review of the changeset if the migration requires behavior or design changes.
  - No production behavior or public design changes were identified.

### Implementation

- [x] Inventory Scala test constructs and map them to Java replacements.
- [x] Migrate shared test helpers and factories to Java.
- [x] Migrate unit tests from ScalaTest to JUnit Jupiter.
- [x] Migrate integration tests from ScalaTest to JUnit Jupiter and Java Testcontainers APIs.
  - Scala integration tests were translated to Java `*IT` classes for Alluxio, Avro, Delta, export/import, ORC, Parquet, metadata partitioning, export parallelism, and UTC timestamp handling.
- [x] Update `pom.xml` to use Java test execution and remove Scala-only test infrastructure.
- [x] Remove all `src/test/scala` sources after their Java replacements compile.

### Verification

- [x] Run focused test compilation after each migration layer.
- [x] Run the unit test suite.
  - `mvn -DossindexSkip=true test`: 212 tests, 0 failures.
- [x] Run applicable integration-test verification or document any environment limitation.
  - `mvn -DossindexSkip=true -DskipTests=false -Dit.test=FilesMetadataReaderIT failsafe:integration-test failsafe:verify` could not execute because Testcontainers could not find a valid Docker environment.
  - Retried with the Lima Docker socket via `DOCKER_HOST=unix:///home/christoph.pirkl/.lima/default/sock/docker.sock TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE=/var/run/docker.sock`; the same Docker discovery error remained.
- [x] Confirm no Scala test sources remain.
- [x] Confirm overall code coverage remains above 80% or document the unavailable gate.
  - The repository does not define a Maven coverage threshold for this changeset; `mvn -DossindexSkip=true test` passed with 212 unit tests and generated the available JaCoCo execution data.
