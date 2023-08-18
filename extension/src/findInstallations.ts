import { ExaScriptsRow, Installation } from "@exasol/extension-manager-interface";
import { validateInstalledScripts } from "./common";

export function findInstallations(scriptRows: ExaScriptsRow[]): Installation[] {
    const result = validateInstalledScripts(scriptRows)
    if (result.type === "success") {
        return [{
            name: "Cloud Storage Extension",
            version: result.result.importMetadata.getVersion() ?? "(unknown)"
        }];
    } else {
        console.warn(result.message)
        return [];
    }
}
