import { ExaScriptsRow, Installation } from "@exasol/extension-manager-interface";
import { validateInstalledScripts } from "./common";

export function findInstallations(scriptRows: ExaScriptsRow[]): Installation[] {
    const scripts = validateInstalledScripts(scriptRows)
    if (scripts) {
        return [{
            name: "Cloud Storage Extension",
            version: scripts.importMetadata.getVersion() ?? "(unknown)"
        }];
    } else {
        return [];
    }
}
