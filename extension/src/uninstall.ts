import { NotFoundError } from "@exasol/extension-manager-interface";
import { ExtendedContext, ExtensionInfo, getAllScripts } from "./common";

export function uninstall(context: ExtendedContext, extension: ExtensionInfo, versionToUninstall: string): void {
    if (extension.version !== versionToUninstall) {
        throw new NotFoundError(`Uninstalling version '${versionToUninstall}' not supported, try '${extension.version}'.`)
    }

    function extensionSchemaExists(): boolean {
        const result = context.sqlClient.query("SELECT 1 FROM SYS.EXA_ALL_SCHEMAS WHERE SCHEMA_NAME=?", context.extensionSchemaName)
        return result.rows.length > 0
    }

    if (extensionSchemaExists()) { // Drop commands fail when schema does not exist.
        getAllScripts().forEach(script =>
            context.sqlClient.execute(`DROP SCRIPT ${context.qualifiedName(script.name)}`)
        );
    }
}
