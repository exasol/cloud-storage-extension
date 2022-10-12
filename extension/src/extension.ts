/* eslint-disable @typescript-eslint/no-unused-vars */
import {
    Context, ExaMetadata,
    ExasolExtension,
    Installation,
    Instance, InternalServerError, NotFoundError, Parameter, ParameterValues,
    registerExtension
} from "@exasol/extension-manager-interface";
import { extendContext, ExtensionInfo } from "./common";
import { CONFIG } from "./extension-config";
import { installExtension } from "./install";

function createExtensionInfo(): ExtensionInfo {
    const version = CONFIG.version;
    const fileName = CONFIG.fileName;
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
        installableVersions: [{ name: extensionInfo.version, latest: true, deprecated: false }],
        bucketFsUploads: [{ bucketFsFilename: extensionInfo.fileName, downloadUrl, fileSize: CONFIG.fileSizeBytes, name: "Cloud Storage Extension file", licenseUrl, licenseAgreementRequired: false }],
        install(context: Context, version: string) {
            installExtension(extendContext(context), extensionInfo, version);
        },
        findInstallations(_context: Context, metadata: ExaMetadata): Installation[] {
            return [];
        },
        uninstall(context: Context, version: string): void {
            throw new InternalServerError("Not yet implemented")
        },
        addInstance(context: Context, version: string, params: ParameterValues): Instance {
            throw new NotFoundError("Creating instances not supported")
        },
        findInstances(context: Context, version: string): Instance[] {
            throw new NotFoundError("Finding instances not supported")
        },
        deleteInstance(context: Context, version: string, instanceId: string): void {
            throw new NotFoundError("Deleting instances not supported")
        },
        getInstanceParameters(context: Context, version: string): Parameter[] {
            throw new NotFoundError("Getting instance parameters not supported")
        },
        readInstanceParameterValues(_context: Context, _version: string, _instanceId: string): ParameterValues {
            throw new NotFoundError("Reading instance parameter values not supported")
        }
    }
}

registerExtension(createExtension())
