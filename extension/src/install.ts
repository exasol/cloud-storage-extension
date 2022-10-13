import { BadRequestError } from "@exasol/extension-manager-interface";
import { ALL_SCRIPTS, EXPORT_PATH_SCRIPT, EXPORT_TABLE_SCRIPT, ExtendedContext, ExtensionInfo, IMPORT_FILES_SCRIPT, IMPORT_METADATA_SCRIPT, IMPORT_PATH_SCRIPT } from "./common";

export function installExtension(context: ExtendedContext, extension: ExtensionInfo, versionToInstall: string): void {
  if (extension.version !== versionToInstall) {
    throw new BadRequestError(`Installing version '${versionToInstall}' not supported, try '${extension.version}'.`);
  }
  function createComment(scriptName: string): void {
    context.sqlClient.execute(`COMMENT ON SCRIPT ${context.qualifiedName(scriptName)} IS 'Created by extension manager for Cloud Storage Extension ${extension.version}'`);
  }
  const jarPath = context.bucketFs.resolvePath(extension.fileName);

  context.sqlClient.execute(`
        CREATE OR REPLACE JAVA SET SCRIPT ${context.qualifiedName(IMPORT_PATH_SCRIPT)}(...) EMITS (...) AS
          %scriptclass com.exasol.cloudetl.scriptclasses.FilesImportQueryGenerator;
          %jar ${jarPath};`);
  context.sqlClient.execute(`
        CREATE OR REPLACE JAVA SCALAR SCRIPT ${context.qualifiedName(IMPORT_METADATA_SCRIPT)}(...) EMITS (
          filename VARCHAR(2000),
          partition_index VARCHAR(100),
          start_index DECIMAL(36, 0),
          end_index DECIMAL(36, 0)
        ) AS
          %scriptclass com.exasol.cloudetl.scriptclasses.FilesMetadataReader;
          %jar ${jarPath};`);
  context.sqlClient.execute(`
        CREATE OR REPLACE JAVA SET SCRIPT ${context.qualifiedName(IMPORT_FILES_SCRIPT)}(...) EMITS (...) AS
          %scriptclass com.exasol.cloudetl.scriptclasses.FilesDataImporter;
          %jar ${jarPath};`);
  context.sqlClient.execute(`
        CREATE OR REPLACE JAVA SET SCRIPT ${context.qualifiedName(EXPORT_PATH_SCRIPT)}(...) EMITS (...) AS
          %scriptclass com.exasol.cloudetl.scriptclasses.TableExportQueryGenerator;
          %jar ${jarPath};`);
  context.sqlClient.execute(`
        CREATE OR REPLACE JAVA SET SCRIPT ${context.qualifiedName(EXPORT_TABLE_SCRIPT)}(...) EMITS (ROWS_AFFECTED INT) AS
          %scriptclass com.exasol.cloudetl.scriptclasses.TableDataExporter;
          %jar ${jarPath};`);

  ALL_SCRIPTS.forEach(createComment);
}