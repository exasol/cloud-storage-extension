import { describe, expect, it } from '@jest/globals';
import { AdapterScript } from './adapterScript';
import { script } from './test-utils';

describe("AdapterScript", () => {
    describe("properties", () => {
        it("name", () => {
            expect(new AdapterScript(script({ name: "script" })).name).toBe("script")
        })
        it("schema", () => {
            expect(new AdapterScript(script({ schema: "schema" })).schema).toBe("schema")
        })
        it("text", () => {
            expect(new AdapterScript(script({ text: "text" })).text).toBe("text")
        })
        it("qualifiedName", () => {
            expect(new AdapterScript(script({ schema: "schema", name: "name" })).qualifiedName).toBe("schema.name")
        })
    })
    describe("methods", () => {
        describe("getVersion()", () => {
            const tests: { name: string; scriptText: string, expected: string | undefined }[] = [
                { name: "found", scriptText: "CREATE ... %jar /path/to/exasol-cloud-storage-extension-1.2.3.jar; more text", expected: "1.2.3" },
                { name: "found with LF", scriptText: "CREATE ...\n %jar /path/to/exasol-cloud-storage-extension-1.2.3.jar; more text", expected: "1.2.3" },
                { name: "found with LFCR", scriptText: "CREATE ...\n\r %jar /path/to/exasol-cloud-storage-extension-1.2.3.jar; more text", expected: "1.2.3" },
                { name: "with CRLF", scriptText: "CREATE ...\r\n %jar /path/to/exasol-cloud-storage-extension-1.2.3.jar; more text", expected: "1.2.3" },
                { name: "not found in root dir", scriptText: "CREATE ... %jar /exasol-cloud-storage-extension-1.2.3.jar; more text", expected: undefined },
                { name: "not found invalid %jar", scriptText: "CREATE ... %invalid /path/to/exasol-cloud-storage-extension-1.2.3.jar; more text", expected: undefined },
                { name: "not found missing %jar", scriptText: "CREATE ... /path/to/exasol-cloud-storage-extension-1.2.3.jar; more text", expected: undefined },
                { name: "not found missing trailing semicolon", scriptText: "CREATE ...\r\n %jar /path/to/exasol-cloud-storage-extension-1.2.3.jar", expected: undefined },
                { name: "not found invalid version", scriptText: "CREATE ... %jar /path/to/exasol-cloud-storage-extension-a.b.c.jar;", expected: undefined },
                { name: "not found invalid filename", scriptText: "CREATE ... %jar /path/to/invalid-file-name-dist-0.0.0-s3-1.2.3.jar;", expected: undefined },
            ]
            tests.forEach(test => it(test.name, () => {
                expect(new AdapterScript(script({ text: test.scriptText })).getVersion()).toBe(test.expected)
            }))
        })
    })
})