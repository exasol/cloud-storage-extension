import { Context } from "@exasol/extension-manager-interface";


export interface ScriptDefinition {
    name: string
    type: "SET" | "SCALAR"
    args: string
    scriptClass: string
}

export const SCRIPTS: { [key: string]: ScriptDefinition } = {
    importPath: {
        name: "IMPORT_PATH",
        type: "SET",
        args: "...",
        scriptClass: "com.exasol.cloudetl.scriptclasses.FilesImportQueryGenerator"
    },
    importMetadata: {
        name: "IMPORT_METADATA",
        type: "SCALAR",
        args: `filename VARCHAR(2000), partition_index VARCHAR(100), start_index DECIMAL(36, 0), end_index DECIMAL(36, 0)`,
        scriptClass: "com.exasol.cloudetl.scriptclasses.FilesMetadataReader"
    },
    importFiles: {
        name: "IMPORT_FILES",
        type: "SET",
        args: "...",
        scriptClass: "com.exasol.cloudetl.scriptclasses.FilesDataImporter"
    },
    exportPath: {
        name: "EXPORT_PATH",
        type: "SET",
        args: "...",
        scriptClass: "com.exasol.cloudetl.scriptclasses.TableExportQueryGenerator"
    },
    exportTable: {
        name: "EXPORT_TABLE",
        type: "SET",
        args: "ROWS_AFFECTED INT",
        scriptClass: "com.exasol.cloudetl.scriptclasses.TableDataExporter"
    }
}

export function getAllScripts(): ScriptDefinition[] {
    return [
        SCRIPTS.importPath,
        SCRIPTS.importMetadata,
        SCRIPTS.importFiles,
        SCRIPTS.exportPath,
        SCRIPTS.exportTable,
    ];
}

export interface ExtensionInfo {
    version: string;
    fileName: string;
}

export type ExtendedContext = Context & {
    qualifiedName(name: string): string
}

export function extendContext(context: Context): ExtendedContext {
    return {
        ...context,
        qualifiedName(name: string) {
            return `"${context.extensionSchemaName}"."${name}"`;
        },
    }
}
