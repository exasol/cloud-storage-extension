import { Context, ExaScriptsRow, InternalServerError } from "@exasol/extension-manager-interface";
import { AdapterScript } from "./adapterScript";


export interface ScriptDefinition {
    name: string
    type: "SET" | "SCALAR"
    args: string
    scriptClass: string
}

export interface InstalledScripts {
    importPath: AdapterScript
    importMetadata: AdapterScript
    importFiles: AdapterScript
    exportPath: AdapterScript
    exportTable: AdapterScript
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


function createMap(scripts: ExaScriptsRow[]): Map<string, AdapterScript> {
    const map = new Map<string, AdapterScript>();
    scripts.forEach(script => {
        map.set(script.name, new AdapterScript(script))
    });
    return map;
}

function validateScript(expectedScript: ScriptDefinition, actualScript: AdapterScript | undefined): string[] {
    if (actualScript == undefined) {
        return [`Script '${expectedScript.name}' is missing`]
    }
    return []
}

export function validateInstalledScripts(scriptRows: ExaScriptsRow[]): InstalledScripts | undefined {
    const scripts = createMap(scriptRows)
    const allScripts = getAllScripts();
    const validationErrors = allScripts.map(script => validateScript(script, scripts.get(script.name)))
        .flatMap(finding => finding);
    const metadataScript = scripts.get(SCRIPTS.importMetadata.name);
    if (metadataScript == undefined || validationErrors.length > 0) {
        console.log("Validation failed:", validationErrors)
        return undefined
    }
    function getScript(scriptDefinition: ScriptDefinition): AdapterScript {
        const script = scripts.get(scriptDefinition.name)
        if (!script) {
            throw new InternalServerError(`Script '${scriptDefinition.name}' not found`)
        }
        return script
    }
    const importPath: AdapterScript = getScript(SCRIPTS.importPath)
    const importMetadata: AdapterScript = getScript(SCRIPTS.importMetadata)
    const importFiles: AdapterScript = getScript(SCRIPTS.importPath)
    const exportPath: AdapterScript = getScript(SCRIPTS.exportPath)
    const exportTable: AdapterScript = getScript(SCRIPTS.exportTable)
    return {
        importPath,
        importMetadata,
        importFiles,
        exportPath,
        exportTable
    }
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
