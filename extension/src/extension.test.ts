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

    function text(name: string, className: string, version: string): string {
      return `CREATE ${name} ...
        %scriptclass ${className};
        %jar /buckets/bfsdefault/default/exasol-cloud-storage-extension-${version}.jar;`
    }
    function script({ schema = "schema", name = "name", inputType, resultType = "EMITS", type = "UDF", text = "", comment }: Partial<ExaScriptsRow>): ExaScriptsRow {
      return { schema, name, inputType, resultType, type, text, comment }
    }
    function setScript(name: string, className: string, version = CONFIG.version): ExaScriptsRow {
      return script({ name, inputType: "SET", text: text(name, className, version) })
    }
    function scalarScript(name: string, className: string, version = CONFIG.version): ExaScriptsRow {
      return script({ name, inputType: "SCALAR", text: text(name, className, version) })
    }

    it("returns empty list when no adapter script is available", () => {
      expect(findInstallations([])).toHaveLength(0)
    })

    it("returns single item when all scripts are available", () => {
      const scripts: ExaScriptsRow[] = [
        setScript("EXPORT_PATH", "com.exasol.cloudetl.scriptclasses.TableExportQueryGenerator"),
        setScript("EXPORT_TABLE", "com.exasol.cloudetl.scriptclasses.TableDataExporter"),
        setScript("IMPORT_FILES", "com.exasol.cloudetl.scriptclasses.FilesDataImporter"),
        scalarScript("IMPORT_METADATA", "com.exasol.cloudetl.scriptclasses.FilesMetadataReader"),
        setScript("IMPORT_PATH", "com.exasol.cloudetl.scriptclasses.FilesImportQueryGenerator")
      ]
      expect(findInstallations(scripts)).toStrictEqual([{ name: "Cloud Storage Extension", version: CONFIG.version }])
    })

    it("uses version from export path script", () => {
      const scripts: ExaScriptsRow[] = [
        setScript("EXPORT_PATH", "com.exasol.cloudetl.scriptclasses.TableExportQueryGenerator"),
        setScript("EXPORT_TABLE", "com.exasol.cloudetl.scriptclasses.TableDataExporter"),
        setScript("IMPORT_FILES", "com.exasol.cloudetl.scriptclasses.FilesDataImporter"),
        scalarScript("IMPORT_METADATA", "com.exasol.cloudetl.scriptclasses.FilesMetadataReader", "0.0.0"),
        setScript("IMPORT_PATH", "com.exasol.cloudetl.scriptclasses.FilesImportQueryGenerator")
      ]
      expect(findInstallations(scripts)).toStrictEqual([{ name: "Cloud Storage Extension", version: "0.0.0" }])
    })

    describe("returns expected installations", () => {
    })
  })

  describe("install()", () => {
    it("executes expected statements", () => {
      const context = createMockContext();
      createExtension().install(context, CONFIG.version);
      const executeCalls = context.executeMock.mock.calls
      expect(executeCalls.length).toBe(10)

      const expectedScriptNames = ["IMPORT_PATH", "IMPORT_METADATA", "IMPORT_FILES", "EXPORT_PATH", "EXPORT_TABLE"]

      const createScriptStatements = executeCalls.slice(0, 5).map(args => args[0])
      const createCommentStatements = executeCalls.slice(5, 10).map(args => args[0])

      expect(createScriptStatements).toHaveLength(5)
      expect(createCommentStatements).toHaveLength(5)

      const expectedComment = `Created by extension manager for Cloud Storage Extension ${CONFIG.version}`
      for (let i = 0; i < expectedScriptNames.length; i++) {
        const name = expectedScriptNames[i];
        expect(createScriptStatements[i]).toContain(`CREATE OR REPLACE JAVA`)
        expect(createScriptStatements[i]).toContain(`SCRIPT "ext-schema"."${name}"`)
        expect(createScriptStatements[i]).toContain(`%scriptclass com.exasol.cloudetl.scriptclasses.`)
        expect(createCommentStatements[i]).toEqual(`COMMENT ON SCRIPT "ext-schema"."${name}" IS '${expectedComment}'`)
      }
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
      const expectedScriptNames = ["IMPORT_PATH", "IMPORT_METADATA", "IMPORT_FILES", "EXPORT_PATH", "EXPORT_TABLE"]
      expect(calls.length).toEqual(expectedScriptNames.length)
      for (let i = 0; i < expectedScriptNames.length; i++) {
        expect(calls[i]).toEqual([`DROP SCRIPT "ext-schema"."${expectedScriptNames[i]}"`])
      }
    })
    it("fails for wrong version", () => {
      expect(() => { createExtension().uninstall(createMockContext(), "wrongVersion") })
        .toThrow(`Uninstalling version 'wrongVersion' not supported, try '${CONFIG.version}'.`)
    })
  })


  describe("getInstanceParameters()", () => {
    it("is not supported", () => {
      expect(() => { createExtension().getInstanceParameters(createMockContext(), "version") })
        .toThrow("Creating instances not supported")
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

  describe("readInstanceParameterValues()", () => {
    it("is not supported", () => {
      expect(() => { createExtension().readInstanceParameterValues(createMockContext(), "version", "instId") })
        .toThrow("Reading instance parameter values not supported")
    })
  })
})

