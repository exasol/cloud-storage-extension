import { ExaMetadata, Installation, QueryResult, SqlClient } from '@exasol/extension-manager-interface';
import { ExaScriptsRow } from '@exasol/extension-manager-interface/dist/exasolSchema';
import { describe, expect, it } from '@jest/globals';
import * as jestMock from "jest-mock";
import { createExtension } from "./extension";
import { CONFIG } from './extension-config';

const EXTENSION_SCHEMA_NAME = "ext-schema"

function getInstalledExtension(): any {
  return (global as any).installedExtension
}

function createMockContext() {
  const execute = jestMock.fn<(query: string, ...args: any) => void>()
  const query = jestMock.fn<(query: string, ...args: any) => QueryResult>()

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
    executeMock: execute,
    queryMock: query
  }
}

describe("Cloud Storage Extension", () => {

  describe("installableVersions", () => {
    it("contains exactly one 'latest', non deprecated version", () => {
      const latestVersions = createExtension().installableVersions.filter(version => version.latest)
      expect(latestVersions).toHaveLength(1)
      expect(latestVersions[0].deprecated).toEqual(false)
    })
  })

  describe("extension registration", () => {
    it("creates an extension", () => {
      const ext = createExtension();
      expect(ext).not.toBeNull()
    })

    it("creates a new object for every call", () => {
      const ext1 = createExtension();
      const ext2 = createExtension();
      expect(ext1).not.toBe(ext2)
    })

    it("registers when loaded", () => {
      const installedExtension = getInstalledExtension();
      expect(installedExtension.extension).not.toBeNull()
      expect(typeof installedExtension.apiVersion).toBe('string');
      expect(installedExtension.apiVersion).not.toBe('');
    })
  })

  describe("findInstallations()", () => {
    function findInstallations(allScripts: ExaScriptsRow[]): Installation[] {
      const metadata: ExaMetadata = {
        allScripts: { rows: allScripts },
        virtualSchemaProperties: { rows: [] },
        virtualSchemas: { rows: [] }
      }
      const installations = createExtension().findInstallations(createMockContext(), metadata)
      expect(installations).toBeDefined()
      return installations
    }

    it("returns empty list when no adapter script is available", () => {
      expect(findInstallations([])).toHaveLength(0)
    })

    describe("returns expected installations", () => {
      function installation({ name = "schema.S3_FILES_ADAPTER", version = "(unknown)" }: Partial<Installation>): Installation {
        return { name, version }
      }
      function script({ schema = "schema", name = "name", inputType, resultType, type = "", text = "", comment }: Partial<ExaScriptsRow>): ExaScriptsRow {
        return { schema, name, inputType, resultType, type, text, comment }
      }
      function adapterScript({ name = "S3_FILES_ADAPTER", type = "ADAPTER", text = "adapter script" }: Partial<ExaScriptsRow>): ExaScriptsRow {
        return script({ name, type, text })
      }
      function importScript({ name = "IMPORT_FROM_S3_DOCUMENT_FILES", type = "UDF", inputType = "SET", resultType = "EMITS" }: Partial<ExaScriptsRow>): ExaScriptsRow {
        return script({ name, type, inputType, resultType })
      }
      const tests: { name: string; scripts: ExaScriptsRow[], expected?: Installation }[] = [
        { name: "all values match", scripts: [adapterScript({}), importScript({})], expected: installation({}) },
        { name: "adapter has wrong type", scripts: [adapterScript({ type: "wrong" }), importScript({})], expected: undefined },
        { name: "adapter has wrong name", scripts: [adapterScript({ name: "wrong" }), importScript({})], expected: undefined },
        { name: "adapter missing", scripts: [importScript({})], expected: undefined },
        { name: "importer has wrong type", scripts: [adapterScript({}), importScript({ type: "wrong" })], expected: undefined },
        { name: "importer has wrong input type", scripts: [adapterScript({}), importScript({ inputType: "wrong" })], expected: undefined },
        { name: "importer has wrong result type", scripts: [adapterScript({}), importScript({ resultType: "wrong" })], expected: undefined },
        { name: "importer has wrong name", scripts: [adapterScript({}), importScript({ name: "wrong" })], expected: undefined },
        { name: "importer missing", scripts: [adapterScript({})], expected: undefined },
        { name: "adapter and importer missing", scripts: [], expected: undefined },
        { name: "version found in filename", scripts: [adapterScript({ text: "CREATE ... %jar /path/to/exasol-cloud-storage-extension--1.2.3.jar; more text" }), importScript({})], expected: installation({ version: "1.2.3" }) },
        { name: "script contains LF", scripts: [adapterScript({ text: "CREATE ...\n %jar /path/to/exasol-cloud-storage-extension--1.2.3.jar; more text" }), importScript({})], expected: installation({ version: "1.2.3" }) },
        { name: "script contains CRLF", scripts: [adapterScript({ text: "CREATE ...\r\n %jar /path/to/exasol-cloud-storage-extension--1.2.3.jar; more text" }), importScript({})], expected: installation({ version: "1.2.3" }) },
        { name: "version not found in filename", scripts: [adapterScript({ text: "CREATE ... %jar /path/to/invalid-file-name-1.2.3.jar;" }), importScript({})], expected: installation({ version: "(unknown)" }) },
        { name: "filename not found in script", scripts: [adapterScript({ text: "CREATE ... %wrong /path/to/exasol-cloud-storage-extension--1.2.3.jar;" }), importScript({})], expected: installation({ version: "(unknown)" }) },
      ]
      tests.forEach(test => {
        it(test.name, () => {
          const actual = findInstallations(test.scripts)
          if (test.expected) {
            expect(actual).toHaveLength(1)
            expect(actual[0].name).toStrictEqual(test.expected.name)
            expect(actual[0].version).toStrictEqual(test.expected.version)
          } else {
            expect(actual).toHaveLength(0)
          }
        })
      });
    })
  })


  describe("install()", () => {
    it("executes expected statements", () => {
      const context = createMockContext();
      createExtension().install(context, CONFIG.version);
      const executeCalls = context.executeMock.mock.calls
      expect(executeCalls.length).toBe(4)
      const adapterScript = executeCalls[0][0]
      const setScript = executeCalls[1][0]
      expect(adapterScript).toContain(`CREATE OR REPLACE JAVA ADAPTER SCRIPT "ext-schema"."S3_FILES_ADAPTER" AS`)
      expect(adapterScript).toContain(`%jar /bucketfs/${CONFIG.fileName};`)
      expect(setScript).toContain(`CREATE OR REPLACE JAVA SET SCRIPT "ext-schema"."IMPORT_FROM_S3_DOCUMENT_FILES"`)
      expect(setScript).toContain(`%jar /bucketfs/${CONFIG.fileName};`)
      const expectedComment = `Created by extension manager for S3 virtual schema extension ${CONFIG.version}`
      expect(executeCalls[2]).toEqual([`COMMENT ON SCRIPT "ext-schema"."IMPORT_FROM_S3_DOCUMENT_FILES" IS '${expectedComment}'`])
      expect(executeCalls[3]).toEqual([`COMMENT ON SCRIPT "ext-schema"."S3_FILES_ADAPTER\" IS '${expectedComment}'`])
    })
    it("fails for wrong version", () => {
      expect(() => { createExtension().install(createMockContext(), "wrongVersion") })
        .toThrow(`Installing version 'wrongVersion' not supported, try '${CONFIG.version}'.`)
    })
  })

  describe("uninstall()", () => {
    it("executes query to check if schema exists", () => {
      const context = createMockContext()
      context.queryMock.mockReturnValue({ columns: [], rows: [] });
      createExtension().uninstall(context, CONFIG.version)
      const calls = context.queryMock.mock.calls
      expect(calls.length).toEqual(1)
      expect(calls[0]).toEqual(["SELECT 1 FROM SYS.EXA_ALL_SCHEMAS WHERE SCHEMA_NAME=?", "ext-schema"])
    })
    it("skips drop statements when schema does not exist", () => {
      const context = createMockContext()
      context.queryMock.mockReturnValue({ columns: [], rows: [] });
      createExtension().uninstall(context, CONFIG.version)
      expect(context.executeMock.mock.calls.length).toEqual(0)
    })
    it("executes expected statements", () => {
      const context = createMockContext()
      context.queryMock.mockReturnValue({ columns: [], rows: [[1]] });
      createExtension().uninstall(context, CONFIG.version)
      const calls = context.executeMock.mock.calls
      expect(calls.length).toEqual(2)
      expect(calls[0]).toEqual(['DROP ADAPTER SCRIPT "ext-schema"."S3_FILES_ADAPTER"'])
      expect(calls[1]).toEqual(['DROP SCRIPT "ext-schema"."IMPORT_FROM_S3_DOCUMENT_FILES"'])
    })
    it("fails for wrong version", () => {
      expect(() => { createExtension().uninstall(createMockContext(), "wrongVersion") })
        .toThrow(`Uninstalling version 'wrongVersion' not supported, try '${CONFIG.version}'.`)
    })
  })


  describe("getInstanceParameters()", () => {
    it("is not supported", () => {
      expect(() => { createExtension().getInstanceParameters(createMockContext(), "version") })
        .toThrow("Getting instance parameters not supported")
    })
  })

  describe("addInstance()", () => {
    it("is not supported", () => {
      expect(() => { createExtension().addInstance(createMockContext(), "version", { values: [] }) })
        .toThrow("Creating instances not supported")
    })
  })

  describe("findInstances()", () => {
    it("is not supported", () => {
      expect(() => { createExtension().findInstances(createMockContext(), "version") })
        .toThrow("Finding instances not supported")
    })
  })

  describe("deleteInstance()", () => {
    it("is not supported", () => {
      expect(() => { createExtension().deleteInstance(createMockContext(), "version", "instId") })
        .toThrow("Deleting instances not supported")
    })
  })
})

