import { BadRequestError } from "@exasol/extension-manager-interface";
import { ExtendedContext, ExtensionInfo } from "./common";



export function installExtension(context: ExtendedContext, extension: ExtensionInfo, versionToInstall: string): void {
    if (extension.version !== versionToInstall) {
        throw new BadRequestError(`Installing version '${versionToInstall}' not supported, try '${extension.version}'.`);
    }
    function createComment(scriptName: string): void {
        context.sqlClient.execute(`COMMENT ON SCRIPT ${context.qualifiedName(scriptName)} IS 'Created by extension manager for Cloud Storage Extension ${extension.version}'`);
    }
    const jarPath = context.bucketFs.resolvePath(extension.fileName);

    context.sqlClient.execute(`
        CREATE OR REPLACE JAVA SET SCRIPT ${context.qualifiedName("IMPORT_PATH")}(...) EMITS (...) AS
          %scriptclass com.exasol.cloudetl.scriptclasses.FilesImportQueryGenerator;
          %jar ${jarPath};`);
    context.sqlClient.execute(`
        CREATE OR REPLACE JAVA SCALAR SCRIPT ${context.qualifiedName("IMPORT_METADATA")}(...) EMITS (
          filename VARCHAR(2000),
          partition_index VARCHAR(100),
          start_index DECIMAL(36, 0),
          end_index DECIMAL(36, 0)
        ) AS
          %scriptclass com.exasol.cloudetl.scriptclasses.FilesMetadataReader;
          %jar ${jarPath};`);
    context.sqlClient.execute(`
        CREATE OR REPLACE JAVA SET SCRIPT ${context.qualifiedName("IMPORT_FILES")}(...) EMITS (...) AS
          %scriptclass com.exasol.cloudetl.scriptclasses.FilesDataImporter;
          %jar ${jarPath};`);
    context.sqlClient.execute(`
        CREATE OR REPLACE JAVA SET SCRIPT ${context.qualifiedName("EXPORT_PATH")}(...) EMITS (...) AS
          %scriptclass com.exasol.cloudetl.scriptclasses.TableExportQueryGenerator;
          %jar ${jarPath};`);
    context.sqlClient.execute(`
        CREATE OR REPLACE JAVA SET SCRIPT ${context.qualifiedName("EXPORT_TABLE")}(...) EMITS (ROWS_AFFECTED INT) AS
          %scriptclass com.exasol.cloudetl.scriptclasses.TableDataExporter;
          %jar ${jarPath};`);

    ["IMPORT_PATH", "IMPORT_METADATA", "IMPORT_FILES", "EXPORT_PATH", "EXPORT_TABLE"].forEach(createComment);
}