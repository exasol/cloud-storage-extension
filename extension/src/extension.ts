/* eslint-disable @typescript-eslint/no-unused-vars */
import {
    Context, ExaMetadata,
    ExasolExtension,
    Installation,
    Instance, NotFoundError, Parameter, ParameterValues,
    UpgradeResult,
    registerExtension
} from "@exasol/extension-manager-interface";
import { ExtensionInfo, extendContext } from "./common";
import { EXTENSION_DESCRIPTION } from "./extension-description";
import { findInstallations } from "./findInstallations";
import { installExtension } from "./install";
import { uninstall } from "./uninstall";
import { upgrade } from "./upgrade";

function createExtensionInfo(): ExtensionInfo {
    const version = EXTENSION_DESCRIPTION.version;
    const fileName = EXTENSION_DESCRIPTION.fileName;
    return { version, fileName };
}

export function createExtension(): ExasolExtension {
    const extensionInfo = createExtensionInfo()
    const repoBaseUrl = "https://github.com/exasol/cloud-storage-extension"
    const downloadUrl = `${repoBaseUrl}/releases/download/${extensionInfo.version}/${extensionInfo.fileName}`;
    const licenseUrl = `${repoBaseUrl}/blob/main/LICENSE`;
    return {
        name: "Cloud Storage Extension",
        description: "Access data formatted with Avro, Orc and Parquet on public cloud storage systems",
        category: "cloud-storage-importer",
        installableVersions: [{ name: extensionInfo.version, latest: true, deprecated: false }],
        bucketFsUploads: [{ bucketFsFilename: extensionInfo.fileName, downloadUrl, fileSize: EXTENSION_DESCRIPTION.fileSizeBytes, name: "Cloud Storage Extension file", licenseUrl, licenseAgreementRequired: false }],
        install(context: Context, version: string) {
            installExtension(extendContext(context), extensionInfo, version);
        },
        findInstallations(_context: Context, metadata: ExaMetadata): Installation[] {
            return findInstallations(metadata.allScripts.rows);
        },
        uninstall(context: Context, version: string): void {
            uninstall(extendContext(context), extensionInfo, version);
        },
        addInstance(context: Context, version: string, params: ParameterValues): Instance {
            throw new NotFoundError("Creating instances not supported")
        },
        upgrade(context: Context): UpgradeResult {
            return upgrade(extendContext(context), extensionInfo);
        },
        findInstances(context: Context, version: string): Instance[] {
            throw new NotFoundError("Finding instances not supported")
        },
        deleteInstance(context: Context, version: string, instanceId: string): void {
            throw new NotFoundError("Deleting instances not supported")
        },
        getInstanceParameters(context: Context, version: string): Parameter[] {
            throw new NotFoundError("Creating instances not supported")
        },
        readInstanceParameterValues(_context: Context, _version: string, _instanceId: string): ParameterValues {
            throw new NotFoundError("Reading instance parameter values not supported")
        }
    }
}

registerExtension(createExtension())
