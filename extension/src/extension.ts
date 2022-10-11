/* eslint-disable @typescript-eslint/no-unused-vars */
import {
    BadRequestError,
    Context, ExaMetadata,
    ExasolExtension,
    Installation,
    Instance, Parameter, ParameterValues,
    registerExtension
} from "@exasol/extension-manager-interface";
import { ExtensionInfo } from "./common";
import { CONFIG } from "./extension-config";

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

        },
        addInstance(context: Context, version: string, params: ParameterValues): Instance {
            throw new BadRequestError("Creating instances not supported")
        },
        findInstallations(_context: Context, metadata: ExaMetadata): Installation[] {
            return [];
        },
        findInstances(context: Context, version: string): Instance[] {
            throw new BadRequestError("Finding instances not supported")
        },
        uninstall(context: Context, version: string): void {
        },
        deleteInstance(context: Context, version: string, instanceId: string): void {
            throw new BadRequestError("Deleting instances not supported")
        },
        getInstanceParameters(context: Context, version: string): Parameter[] {
            throw new BadRequestError("Getting instance parameters not supported")
        },
        readInstanceParameterValues(_context: Context, _version: string, _instanceId: string): ParameterValues {
            throw new BadRequestError("Reading instance parameter values not supported")
        }
    }
}

registerExtension(createExtension())
