import {
    PreconditionFailedError,
    UpgradeResult
} from "@exasol/extension-manager-interface";
import { ExtendedContext, ExtensionInfo, SCRIPTS, validateInstalledScripts, validateVersions } from "./common";
import { installExtension } from "./install";

/**
 * Assertion and filter function to verify a value is really defined and not {@code undefined} or {@code null}.
 * @param value the value to check
 * @returns {@code true} if the value is not null or undefined.
 */
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
        throw new PreconditionFailedError(`Extension is already installed in latest version ${newVersion}`)
    }
    installExtension(context, extensionInfo, newVersion)
    return { previousVersion: previousVersion.result, newVersion };
}
