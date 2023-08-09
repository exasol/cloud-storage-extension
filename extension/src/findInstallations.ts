import { ExaScriptsRow, Installation } from "@exasol/extension-manager-interface";
import { AdapterScript } from "./adapterScript";
import { SCRIPTS, ScriptDefinition, getAllScripts } from "./common";


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

export function findInstallations(scriptRows: ExaScriptsRow[]): Installation[] {
    const scripts = createMap(scriptRows)
    const validationErrors = getAllScripts().map(script => validateScript(script, scripts.get(script.name)))
        .flatMap(finding => finding);
    const metadataScript = scripts.get(SCRIPTS.importMetadata.name);
    if (metadataScript == undefined || validationErrors.length > 0) {
        console.log("Validation failed:", validationErrors)
        return []
    }
    return [{
        name: "Cloud Storage Extension",
        version: metadataScript.getVersion() ?? "(unknown)"
    }];
}
