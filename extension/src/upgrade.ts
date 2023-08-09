import {
    UpgradeResult
} from "@exasol/extension-manager-interface";
import { ExtendedContext, ExtensionInfo } from "./common";
import { installExtension } from "./install";


export function upgrade(context: ExtendedContext, extensionInfo: ExtensionInfo): UpgradeResult {
    const previousVersion = "getAdapterVersion(extensionInfo, scripts)"
    const newVersion = extensionInfo.version
    installExtension(context, extensionInfo, newVersion)
    return { previousVersion, newVersion };
}
