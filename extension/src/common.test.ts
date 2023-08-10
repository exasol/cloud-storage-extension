import { ExaScriptsRow } from '@exasol/extension-manager-interface';
import { describe, expect, it } from '@jest/globals';
import { AdapterScript } from './adapterScript';
import { InstalledScripts, failureResult, successResult, validateInstalledScripts, validateVersions } from './common';

function script({ schema = "schema", name = "name", inputType, resultType = "EMITS", type = "UDF", text = "", comment }: Partial<ExaScriptsRow>): ExaScriptsRow {
    return { schema, name, inputType, resultType, type, text, comment }
}

describe("common", () => {
    describe("validateInstalledScripts()", () => {
        const importPath = script({ name: "IMPORT_PATH" })
        const importMetadata = script({ name: "IMPORT_METADATA" })
        const importFiles = script({ name: "IMPORT_FILES" })
        const exportPath = script({ name: "EXPORT_PATH" })
        const exportTable = script({ name: "EXPORT_TABLE" })
        it("all scripts available", () => {
            const result = validateInstalledScripts([importPath, importMetadata, importFiles, exportPath, exportTable]);
            expect(result).toStrictEqual(successResult({
                importPath: new AdapterScript(importPath),
                importMetadata: new AdapterScript(importMetadata),
                importFiles: new AdapterScript(importFiles),
                exportPath: new AdapterScript(exportPath),
                exportTable: new AdapterScript(exportTable)
            }))
        })

        describe("scripts missing", () => {
            const tests: { name: string; scripts: ExaScriptsRow[], expectedMessage: string }[] = [
                { name: "all scripts missing", scripts: [], expectedMessage: "Validation failed: Script 'IMPORT_PATH' is missing, Script 'IMPORT_METADATA' is missing, Script 'IMPORT_FILES' is missing, Script 'EXPORT_PATH' is missing, Script 'EXPORT_TABLE' is missing" },
                { name: "importPath missing", scripts: [importMetadata, importFiles, exportPath, exportTable], expectedMessage: "Validation failed: Script 'IMPORT_PATH' is missing" },
                { name: "importMetadata missing", scripts: [importPath, importFiles, exportPath, exportTable], expectedMessage: "Validation failed: Script 'IMPORT_METADATA' is missing" },
                { name: "importFiles missing", scripts: [importPath, importMetadata, exportPath, exportTable], expectedMessage: "Validation failed: Script 'IMPORT_FILES' is missing" },
                { name: "exportPath missing", scripts: [importPath, importMetadata, importFiles, exportTable], expectedMessage: "Validation failed: Script 'EXPORT_PATH' is missing" },
                { name: "exportTable missing", scripts: [importPath, importMetadata, importFiles, exportPath], expectedMessage: "Validation failed: Script 'EXPORT_TABLE' is missing" },
            ]
            tests.forEach(test => it(test.name, () => {
                expect(validateInstalledScripts(test.scripts)).toStrictEqual(failureResult(test.expectedMessage))
            }));
        })

        function adapterScript(version: string | undefined): AdapterScript {
            return new AdapterScript(script({ text: `%jar /buckets/bfsdefault/default/exasol-cloud-storage-extension-${version ?? 'unknown-version'}.jar;` }))
        }

        function scriptVersions(importPathVersion: string | undefined,
            importMetadataVersion: string | undefined, importFilesVersion: string | undefined,
            exportPathVersion: string | undefined, exportTableVersion: string | undefined): InstalledScripts {
            return {
                importPath: adapterScript(importPathVersion),
                importMetadata: adapterScript(importMetadataVersion),
                importFiles: adapterScript(importFilesVersion),
                exportPath: adapterScript(exportPathVersion),
                exportTable: adapterScript(exportTableVersion)
            }
        }

        describe("validateVersions()", () => {
            const version = "1.2.3"
            it("all scripts have same version", () => {
                expect(validateVersions(scriptVersions(version, version, version, version, version))).toStrictEqual(successResult(version))
            })
            describe("not all scripts have same version", () => {
                const tests: { name: string; scripts: InstalledScripts, expectedMessage: string }[] = [
                    { name: "unknown version", scripts: scriptVersions(undefined, undefined, undefined, undefined, undefined), expectedMessage: "Failed to get version from script 'importPath' with text '%jar /buckets/bfsdefault/default/exasol-cloud-storage-extension-unknown-version.jar;'" },
                    { name: "one missing version", scripts: scriptVersions(version, version, version, version, undefined), expectedMessage: "Failed to get version from script 'exportTable' with text '%jar /buckets/bfsdefault/default/exasol-cloud-storage-extension-unknown-version.jar;'" },
                    { name: "two different versions", scripts: scriptVersions(version, version, version, version, "1.2.4"), expectedMessage: "Not all scripts use the same version. Found versions: '1.2.3, 1.2.4'" },
                    { name: "five different versions", scripts: scriptVersions("1.0.1", "1.0.2", "1.0.3", "1.0.4", "1.0.5"), expectedMessage: "Not all scripts use the same version. Found versions: '1.0.1, 1.0.2, 1.0.3, 1.0.4, 1.0.5'" },
                ]
                tests.forEach(test => it(test.name, () => {
                    expect(validateVersions(test.scripts)).toStrictEqual(failureResult(test.expectedMessage))
                }))
            })
        })
    })
})