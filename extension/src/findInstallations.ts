import { ExaScriptsRow, Installation } from "@exasol/extension-manager-interface";
import { getAllScripts, ScriptDefinition, SCRIPTS } from "./common";


function findScriptByName(scripts: ExaScriptsRow[], name: string): ExaScriptsRow | undefined {
    return scripts.find(script => script.name === name);
}

function createMap(scripts: ExaScriptsRow[]): Map<string, ExaScriptsRow> {
    const map = new Map<string, ExaScriptsRow>();
    scripts.forEach(script => {
        map.set(script.name, script)
    });
    return map;
}

function validateScript(expectedScript: ScriptDefinition, actualScript: ExaScriptsRow | undefined): string[] {
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
        version: extractVersion(metadataScript.text)
    }];
}

const unknownVersion = "(unknown)"
const fileNamePattern = /.*%jar\s+[\w-/]+\/([^/]+.jar)\s*;.*/
const jarNameVersionPattern = /exasol-cloud-storage-extension-(\d+\.\d+\.\d+).jar/

function extractVersion(scriptText: string): string {
    const jarNameMatch = fileNamePattern.exec(scriptText)
    if (!jarNameMatch) {
        console.log(`WARN: Could not find jar filename in adapter script "${scriptText}"`)
        return unknownVersion
    }
    const jarFileName = jarNameMatch[1];
    const versionMatch = jarNameVersionPattern.exec(jarFileName)
    if (!versionMatch) {
        console.log(`WARN: Could not find version in jar file name "${jarFileName}"`)
        return unknownVersion
    }
    return versionMatch[1]
}

