import {
    PreconditionFailedError,
    UpgradeResult
} from "@exasol/extension-manager-interface";
import { ExtendedContext, ExtensionInfo, SCRIPTS, validateInstalledScripts, validateVersions } from "./common";
import { installExtension } from "./install";


function notEmpty<TValue>(value: TValue | null | undefined): value is TValue {
    return value !== null && value !== undefined;
}

export function upgrade(context: ExtendedContext, extensionInfo: ExtensionInfo): UpgradeResult {

    const scriptList = Object.entries(SCRIPTS).map(([_key, value]) => value.name)
        .map(scriptName => context.metadata.getScriptByName(scriptName))
        .filter(notEmpty);

    const installedScripts = validateInstalledScripts(scriptList)
    if (installedScripts.type === "failure") {
        throw new PreconditionFailedError(`Not all required scripts are installed: ${installedScripts.message}`)
    }
    const previousVersion = validateVersions(installedScripts.result)
    if (previousVersion.type === "failure") {
        throw new PreconditionFailedError(`Installed script use inconsistent versions: ${previousVersion.message}`)
    }
    const newVersion = extensionInfo.version
    if (previousVersion.result === newVersion) {
        throw new PreconditionFailedError(`Current version ${newVersion} already installed`)
    }
    installExtension(context, extensionInfo, newVersion)
    return { previousVersion: previousVersion.result, newVersion };
}
