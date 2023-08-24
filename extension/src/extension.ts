import { ExasolExtension, registerExtension } from "@exasol/extension-manager-interface";
import { JavaBaseExtension, ScriptDefinition, convertBaseExtension, jarFileVersionExtractor } from "@exasol/extension-manager-interface/dist/base";
import { EXTENSION_DESCRIPTION } from "./extension-description";

/** Script definitions for the required scripts. */
const SCRIPTS: ScriptDefinition[] = [
    {
        name: "IMPORT_PATH",
        type: "SET",
        args: "...",
        scriptClass: "com.exasol.cloudetl.scriptclasses.FilesImportQueryGenerator"
    },
    {
        name: "IMPORT_METADATA",
        type: "SCALAR",
        args: `filename VARCHAR(2000), partition_index VARCHAR(100), start_index DECIMAL(36, 0), end_index DECIMAL(36, 0)`,
        scriptClass: "com.exasol.cloudetl.scriptclasses.FilesMetadataReader"
    },
    {
        name: "IMPORT_FILES",
        type: "SET",
        args: "...",
        scriptClass: "com.exasol.cloudetl.scriptclasses.FilesDataImporter"
    },
    {
        name: "EXPORT_PATH",
        type: "SET",
        args: "...",
        scriptClass: "com.exasol.cloudetl.scriptclasses.TableExportQueryGenerator"
    },
    {
        name: "EXPORT_TABLE",
        type: "SET",
        args: "ROWS_AFFECTED INT",
        scriptClass: "com.exasol.cloudetl.scriptclasses.TableDataExporter"
    }
]

export function createExtension(): ExasolExtension {
    const baseExtension: JavaBaseExtension = {
        name: "Cloud Storage Extension",
        description: "Access data formatted with Avro, Orc and Parquet on public cloud storage systems",
        category: "cloud-storage-importer",
        version: EXTENSION_DESCRIPTION.version,
        file: { name: EXTENSION_DESCRIPTION.fileName, size: EXTENSION_DESCRIPTION.fileSizeBytes },
        scripts: SCRIPTS,
        scriptVersionExtractor: jarFileVersionExtractor(/exasol-cloud-storage-extension-(\d+\.\d+\.\d+).jar/)
    }
    return convertBaseExtension(baseExtension)
}

registerExtension(createExtension())