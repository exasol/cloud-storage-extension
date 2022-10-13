import { Context } from "@exasol/extension-manager-interface";

export const IMPORT_PATH_SCRIPT = "IMPORT_PATH";
export const IMPORT_METADATA_SCRIPT = "IMPORT_METADATA";
export const IMPORT_FILES_SCRIPT = "IMPORT_FILES";
export const EXPORT_PATH_SCRIPT = "EXPORT_PATH";
export const EXPORT_TABLE_SCRIPT = "EXPORT_TABLE";

export const ALL_SCRIPTS = [IMPORT_PATH_SCRIPT, IMPORT_METADATA_SCRIPT, IMPORT_FILES_SCRIPT, EXPORT_PATH_SCRIPT, EXPORT_TABLE_SCRIPT];
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
