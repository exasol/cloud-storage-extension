import { ExaScriptsRow, Installation } from "@exasol/extension-manager-interface";
import { ALL_SCRIPTS } from "./common";


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

function validateScript(scriptName: string, script: ExaScriptsRow | undefined): string[] {
    if (script == undefined) {
        return [`Script '${scriptName}' is missing`]
    }
    return []
}

export function findInstallations(scriptRows: ExaScriptsRow[]): Installation[] {
    const scripts = createMap(scriptRows)
    const validationErrors = ALL_SCRIPTS.map(scriptName => validateScript(scriptName, scripts.get(scriptName)))
        .flatMap(finding => finding);
    const firstScript = scripts.get(ALL_SCRIPTS[0]);
    if (firstScript == undefined || validationErrors.length > 0) {
        console.log("Validation failed:", validationErrors)
        return []
    }
    return [{
        name: "Cloud Storage Extension",
        version: extractVersion(firstScript.text)
    }];
}

const unknownVersion = "(unknown)"
const fileNamePattern = /.*%jar\s+[\w-/]+\/([^/]+.jar)\s*;.*/
const jarNameVersionPattern = /exasol-cloud-storage-extension-(\d+\.\d+\.\d+).jar/

function extractVersion(adapterScriptText: string): string {
    const jarNameMatch = fileNamePattern.exec(adapterScriptText)
    if (!jarNameMatch) {
        console.log(`WARN: Could not find jar filename in adapter script "${adapterScriptText}"`)
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

