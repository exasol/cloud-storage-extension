import { Context, ExaScriptsRow, InternalServerError } from "@exasol/extension-manager-interface";
import { AdapterScript } from "./adapterScript";

/** Definition of an Exasol `SCRIPT` with all information required for creating it in the database. */
export interface ScriptDefinition {
    name: string
    type: "SET" | "SCALAR"
    args: string
    scriptClass: string
}

/** Script definitions for the required scripts. */
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

/** The same information from {@link SCRIPTS} but in form of a list. */
export function getAllScripts(): ScriptDefinition[] {
    return [
        SCRIPTS.importPath,
        SCRIPTS.importMetadata,
        SCRIPTS.importFiles,
        SCRIPTS.exportPath,
        SCRIPTS.exportTable,
    ];
}

/** All installed scripts required for this extension. */
export interface InstalledScripts {
    importPath: AdapterScript
    importMetadata: AdapterScript
    importFiles: AdapterScript
    exportPath: AdapterScript
    exportTable: AdapterScript
}

/** Successful result of a function. */
export type SuccessResult<T> = { type: "success", result: T }
/** Failure result of a function. */
export type FailureResult = { type: "failure", message: string }

/**
 * Represents a result of an operation that can be successful or a failure.
 * In case of success it contains the result, in case of error it contains an error message.
 */
export type Result<T> = SuccessResult<T> | FailureResult

/**
 * Create a new {@link SuccessResult}.
 * @param result the result value
 * @returns a new {@link SuccessResult}
 */
export function successResult<T>(result: T): SuccessResult<T> {
    return { type: "success", result }
}

/**
 * Create a new {@link FailureResult}.
 * @param message error message
 * @returns a new {@link FailureResult}
 */
export function failureResult(message: string): FailureResult {
    return { type: "failure", message }
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

/**
 * Validate that all required scripts are installed.
 * @param scriptRows list of all installed scripts
 * @returns a successful or a failed {@link Result} with all installed scripts
 */
export function validateInstalledScripts(scriptRows: ExaScriptsRow[]): Result<InstalledScripts> {
    const scripts = createMap(scriptRows)
    const allScripts = getAllScripts();
    const validationErrors = allScripts.map(script => validateScript(script, scripts.get(script.name)))
        .flatMap(finding => finding);
    const metadataScript = scripts.get(SCRIPTS.importMetadata.name);
    if (metadataScript == undefined || validationErrors.length > 0) {
        return failureResult(`Validation failed: ${validationErrors.join(', ')}`)
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
    return successResult({
        importPath,
        importMetadata,
        importFiles,
        exportPath,
        exportTable
    })
}

/**
 * Verify that all scripts have the same version.
 * @param scripts installed scripts
 * @returns a failure {@link Result} if not all scripts have the same version, else a successful {@link Result} with the common version.
 */
export function validateVersions(scripts: InstalledScripts): Result<string> {
    let key: keyof InstalledScripts;
    const versionSet = new Set<string>()
    for (key in scripts) {
        const version = scripts[key].getVersion()
        if (version) {
            versionSet.add(version)
        } else {
            return failureResult(`Failed to get version from script '${key}' with text '${scripts[key].text}'`)
        }
    }
    const versions: string[] = []
    versionSet.forEach(value => versions.push(value))
    if (versions.length === 1) {
        return successResult(versions[0])
    }
    return failureResult(`Not all scripts use the same version. Found versions: '${versions.join(', ')}'`)
}

/** Information about the current extension version. */
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
