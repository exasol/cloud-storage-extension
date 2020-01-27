# Developer Guide

This guide provides development workflows that used to develop and maintain the
cloud-storage-etl-udfs. It is intended for those who wish to address the issues,
merge pull request, perform release or deep dive into the codebase of the
cloud-storage-etl-udfs.

## Contributing

Please read the [contributing guide](../CONTRIBUTING.md) first. It provides
instructions on how to fork the projects and get started working on it.

## Prerequisites

You need to have Java version 1.8 or above installed on your development
machine.

Additionally, we assume you have some experience doing Scala development. If you
have any questions in general or about the development process, please feel free
to [get in touch](#getting-in-touch).

## Build the project

First clone a local copy of the repository:

```bash
git clone https://github.com/exasol/cloud-storage-etl-udfs.git
```

Then run `./sbtx`, and run any of these commands:

- `clean`: cleans previously compiled outputs; to start clean again.
- `compile`: compiles the source files.
- `test:compile`: compiles the unit test files.
- `it:compile`: compiles the integration test files.
- `test`: run all the unit tests.
- `it:test`: run all the integration tests.
- `doc`: generate the api documentation.

You can also run several commands combined together:

```
;clean;test;it:test
```

Additionally, you can run `testOnly filename` or `it:testOnly filename` commands
to only run single file tests.

### Running E2E build script

Inside the `scripts/` folder, you will find the `ci.sh` bash file, that runs
end-to-end build process. This file is intented to be run in continous
integration (CI) environment. For the continous integration we use the [Travis
CI](https://travis-ci.org/).

Please run this file to make sure that everything is working before commiting
code or submitting a pull request.

```bash
./scripts/ci.sh
```

Additionally, ensure that the `ci.sh` scripts works with different versions of
the Scala programming language. You can check that with the following command:

```bash
TRAVIS_SCALA_VERSION=2.11.12 ./scripts/ci.sh
```

## Checking the test coverage

The `ci.sh` script also creates the code coverage reports. They are located in
the target path, `target/scala-<SCALA.VERSION>/scoverage-report/`.

You can open the `index.html` file, it should show the code coverage reports per
file.

![alt text](images/code_coverage_example.png "Code Coverage Example")

You can also generage the coverage reports using the `sbt` command line, by
running:

```bash
;clean;coverage;test;it:test;coverageReport
```

## Checking the dependency updates

It is important to keep the dependencies up to date.

You can check out if any of dependencies or plugins have new versions, by
running the following commands.

Check if any plugins have new versions:

```bash
pluginUpdates
```

Check if any dependencies have new versions:

```bash
dependencyUpdates
```

### Dependency tree and artifact eviction

You can check the dependency tree by running the comman below:

```bash
coursierDependencyTree
```

Additionally, it is also good practice to check the evicted artifacts, and maybe
to exclude them explicitly when declaring the library dependencies. In order to
check the evicted artifacts, run:

```bash
evicted
```

## Releasing

Currently, the releasing is performed using the git tags and artifacts are
uploaded to the [Github releases][gh-releases].

### Pre release process

Update the `CHANGES.md` file with the summary of all the changes since the last
release. Similarly, update the `AUTHORS.md` file if there were any new
contributors.

Please make sure to update any other necessary files, for example, `README.md`
needs to be changed to update new versions.

### Releasing steps

Follow these steps in order to create a new release:

- Run `./scripts/ci.sh` and make sure everything is working.
- Add a git tag, for example, `git tag -a v0.4.4 -m "Release version v0.4.4"`.
- Push tags to remote, `git push --tags`.

Please make sure that the new version tag follows the [Semantic Versioning
2.0.0](https://semver.org/).

The next Travis CI run will detect the new tag and create a new Github release
and publish the artifacts.

### Post release process

After the release process, the new [Github release][gh-releases] notes should be
added. It should be same as the pre-release update to the `CHANGES.md` file.

Click on the "Edit release" button on the latest release version on the Github
releases page, and add the release notes.

## Editor Setups

We try to keep the codebase code editor agnostic. 

Any setups required for IntelliJ or Eclipse is out of scope. However, this can
change when we get contributors who use those code editors :)

## Getting in touch

Please feel free to report a bug, suggest an idea for a feature, or ask a
question about the code.

You can create an issue using [Github issues][gh-issues] or follow a standard
[fork and pull][fork-and-pull] process to contribute a code via [Github pull
requests][gh-pulls].

## Conclusion

This guide is expected to change and evolve with the changes to the project.
Any pull requests to keep this document updated are very much appreciated!

[gh-issues]: https://github.com/exasol/cloud-storage-etl-udfs/issues
[gh-pulls]: https://github.com/exasol/cloud-storage-etl-udfs/pulls
[fork-and-pull]: https://help.github.com/articles/using-pull-requests/
[gh-releases]: https://github.com/exasol/cloud-storage-etl-udfs/releases
