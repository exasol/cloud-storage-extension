
export const ADAPTER_SCRIPT_NAME = "S3_FILES_ADAPTER";
export const IMPORT_SCRIPT_NAME = "IMPORT_FROM_S3_DOCUMENT_FILES";

export interface ExtensionInfo {
    version: string;
    fileName: string;
}

export function getConnectionName(virtualSchemaName: string): string {
    return `${virtualSchemaName}_CONNECTION`;
}

function identity(arg: string): string {
    return arg;
}

export const convertInstanceIdToSchemaName = identity
export const convertSchemaNameToInstanceId = identity
