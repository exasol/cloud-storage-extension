/* eslint-disable @typescript-eslint/no-explicit-any */
import { Context, QueryResult, SqlClient } from '@exasol/extension-manager-interface';
import { ExaScriptsRow } from '@exasol/extension-manager-interface/dist/exasolSchema';
import * as jestMock from "jest-mock";

const EXTENSION_SCHEMA_NAME = "ext-schema"

export function getInstalledExtension(): any {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-member-access
    return (global as any).installedExtension
}

export type ContextMock = Context & {
    mocks: {
        sqlExecute: jestMock.Mock<(query: string, ...args: any) => void>,
        sqlQuery: jestMock.Mock<(query: string, ...args: any) => QueryResult>
        getScriptByName: jestMock.Mock<(scriptName: string) => ExaScriptsRow | null>
        simulateScripts: (scripts: ExaScriptsRow[]) => void
    }
}

export function createMockContext(): ContextMock {
    const mockedScripts: Map<string, ExaScriptsRow> = new Map()
    const execute = jestMock.fn<(query: string, ...args: any) => void>().mockName("sqlClient.execute()")
    const query = jestMock.fn<(query: string, ...args: any) => QueryResult>().mockName("sqlClient.query()")
    const getScriptByName = jestMock.fn<(scriptName: string) => ExaScriptsRow | null>().mockName("metadata.getScriptByName()")
    getScriptByName.mockImplementation((scriptName) => mockedScripts.get(scriptName) ?? null)
    const sqlClient: SqlClient = {
        execute: execute,
        query: query
    }

    return {
        extensionSchemaName: EXTENSION_SCHEMA_NAME,
        sqlClient,
        bucketFs: {
            resolvePath(fileName: string) {
                return "/bucketfs/" + fileName;
            },
        },
        metadata: {
            getScriptByName
        },
        mocks: {
            sqlExecute: execute,
            sqlQuery: query,
            getScriptByName: getScriptByName,
            simulateScripts(scripts: ExaScriptsRow[]) {
                mockedScripts.clear()
                scripts.forEach(script => mockedScripts.set(script.name, script))
            },
        }
    }
}

export function script({ schema = "schema", name = "name", inputType, resultType, type = "", text = "", comment }: Partial<ExaScriptsRow>): ExaScriptsRow {
    return { schema, name, inputType, resultType, type, text, comment }
}
export function adapterScript({ name = "S3_FILES_ADAPTER", type = "ADAPTER", text = "adapter script" }: Partial<ExaScriptsRow>): ExaScriptsRow {
    return script({ name, type, text })
}
export function importScript({ name = "IMPORT_FROM_S3_DOCUMENT_FILES", type = "UDF", inputType = "SET", resultType = "EMITS" }: Partial<ExaScriptsRow>): ExaScriptsRow {
    return script({ name, type, inputType, resultType })
}
export function scriptWithVersion(name: string, version: string): ExaScriptsRow {
    return script({ name, text: `CREATE ... %jar /path/to/exasol-cloud-storage-extension-${version}.jar; more text` })
}
