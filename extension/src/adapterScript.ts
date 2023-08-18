import { ExaScriptsRow } from "@exasol/extension-manager-interface";

export class AdapterScript {
    #script: ExaScriptsRow
    constructor(script: ExaScriptsRow) {
        this.#script = script
    }
    getVersion() {
        return extractVersion(this.#script.text)
    }
    get name() {
        return this.#script.name
    }
    get qualifiedName() {
        return `${this.#script.schema}.${this.#script.name}`
    }
    get schema() {
        return this.#script.schema
    }
    get text() {
        return this.#script.text
    }
}

const adapterScriptFileNamePattern = /.*%jar\s+[\w-/]+\/([^/]+.jar)\s*;.*/
const jarNameVersionPattern = /exasol-cloud-storage-extension-(\d+\.\d+\.\d+).jar/

function extractVersion(adapterScriptText: string): string | undefined {
    const jarNameMatch = adapterScriptFileNamePattern.exec(adapterScriptText)
    if (!jarNameMatch) {
        console.warn(`WARN: Could not find jar filename in adapter script "${adapterScriptText}"`)
        return undefined
    }
    const jarFileName = jarNameMatch[1];
    const versionMatch = jarNameVersionPattern.exec(jarFileName)
    if (!versionMatch) {
        console.warn(`WARN: Could not find version in jar file name "${jarFileName}"`)
        return undefined
    }
    return versionMatch[1]
}
