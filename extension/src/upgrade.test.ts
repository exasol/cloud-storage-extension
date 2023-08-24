import { ExaScriptsRow, PreconditionFailedError } from '@exasol/extension-manager-interface';
import { describe, expect, it } from '@jest/globals';
import { createExtension } from './extension';
import { EXTENSION_DESCRIPTION } from './extension-description';
import { createMockContext, scriptWithVersion } from './test-utils';

const currentVersion = EXTENSION_DESCRIPTION.version

describe("upgrade()", () => {
    const version = "1.2.3"
    const importPath = scriptWithVersion("IMPORT_PATH", version)
    const importMetadata = scriptWithVersion("IMPORT_METADATA", version)
    const importFiles = scriptWithVersion("IMPORT_FILES", version)
    const exportPath = scriptWithVersion("EXPORT_PATH", version)
    const exportTable = scriptWithVersion("EXPORT_TABLE", version)
    const allScripts = [importPath, importMetadata, importFiles, exportPath, exportTable]

    describe("validateInstalledScripts()", () => {
        it("success", () => {
            const context = createMockContext()
            context.mocks.simulateScripts(allScripts)
            expect(createExtension().upgrade(context)).toStrictEqual({
                previousVersion: version, newVersion: currentVersion
            })
            const executeCalls = context.mocks.sqlExecute.mock.calls
            expect(executeCalls.length).toBe(10)
        })
        describe("failure", () => {
            const tests: { name: string; scripts: ExaScriptsRow[], expectedMessage: string }[] = [
                { name: "no script", scripts: [], expectedMessage: "Not all required scripts are installed: Validation failed: Script 'IMPORT_PATH' is missing, Script 'IMPORT_METADATA' is missing, Script 'IMPORT_FILES' is missing, Script 'EXPORT_PATH' is missing, Script 'EXPORT_TABLE' is missing" },
                { name: "one missing script", scripts: [importPath, importMetadata, importFiles, exportPath], expectedMessage: "Not all required scripts are installed: Validation failed: Script 'EXPORT_TABLE' is missing" },
                { name: "inconsistent versions", scripts: [importPath, importMetadata, importFiles, exportPath, scriptWithVersion("EXPORT_TABLE", "1.2.4")], expectedMessage: "Failed to validate script versions: Not all scripts use the same version. Found 2 different versions: '1.2.3, 1.2.4'" },
                {
                    name: "version already up-to-date", scripts: [
                        scriptWithVersion("IMPORT_PATH", currentVersion), scriptWithVersion("IMPORT_METADATA", currentVersion),
                        scriptWithVersion("IMPORT_FILES", currentVersion), scriptWithVersion("EXPORT_PATH", currentVersion), scriptWithVersion("EXPORT_TABLE", currentVersion)
                    ],
                    expectedMessage: `Extension is already installed in latest version ${currentVersion}`
                },
            ]
            tests.forEach(test => it(test.name, () => {
                const context = createMockContext()
                context.mocks.simulateScripts(test.scripts)
                expect(() => createExtension().upgrade(context)).toThrowError(new PreconditionFailedError(test.expectedMessage))
                const executeCalls = context.mocks.sqlExecute.mock.calls
                expect(executeCalls.length).toBe(0)
            }))
        })
    })
})
