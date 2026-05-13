# GH-393 Migrate production code from Scala to Java

## Goal

Migrate the production code in this repository from Scala to Java without changing behavior. The intent is to reduce the mixed-language maintenance cost, simplify the build, and make future dependency management and code ownership more uniform.

## Scope

In scope:

* Translate all production sources under `src/main/scala` to Java under `src/main/java`.
* Preserve external behavior, supported storage systems, supported formats, and existing public entry points.
* Update build configuration so the production build compiles Java sources cleanly.
* Update developer-facing documentation if it still refers to Scala-specific production build steps.
* Keep the existing unit and integration test suite green during and after the migration.

Out of scope:

* Feature work or user-visible behavior changes.
* Format, protocol, or data-layout changes.
* Test rewrites that are not required by the source-language migration itself.

## Design References

* [README](../README.md)
* [User Guide](../user_guide/user_guide.md)
* [Developers Guide](../developers_guide/developers_guide.md)
* [Quality Criteria](../quality_requirements.md)
* [Build Configuration](../../pom.xml)

## Strategy

Start by inventorying the Scala-specific constructs that need 1:1 Java equivalents or small adaptation layers. The current production code uses traits, case classes, singleton objects, pattern matching, `Option`, and mutable collection idioms, so the migration needs a clear translation rule for each of those.

Migrate the code in layers rather than file order:

1. utility and value packages (`constants`, `data`, `helper`, `storage`, `filesystem`, `parallelism`)
2. I/O and format packages (`bucket`, `source`, `sink`, `parquet`, `orc`, `emitter`, `transform`)
3. UDF/script entrypoints (`scriptclasses`)

Adjust the Maven build as needed so Java becomes the production source language while keeping test support intact. Verify each layer with the existing tests before moving to the next one.

## Task List

- [ ] Create a migration inventory for all Scala production files and map each Scala construct to its Java translation pattern.
- [ ] Update the build so production compilation targets `src/main/java` and no longer depends on Scala sources for main code.
- [ ] Update developer documentation if the build or local workflow changes for contributors.
- [ ] Migrate the utility and value packages: `constants`, `data`, `helper`, `storage`, `filesystem`, and `parallelism`.
- [ ] Migrate the I/O and processing packages: `bucket`, `source`, `sink`, `parquet`, `orc`, `emitter`, and `transform`.
- [ ] Migrate the script entrypoints in `scriptclasses` and remove the remaining production Scala sources.
- [ ] Run the full verification suite, including compile, unit tests, integration tests, static checks, and the coverage gate required by the repository CI.
- [ ] Confirm that production code is pure Java and that the existing tests remain green.

