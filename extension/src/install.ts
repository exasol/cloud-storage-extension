import { BadRequestError } from "@exasol/extension-manager-interface";
import { ExtendedContext, ExtensionInfo, getAllScripts, ScriptDefinition } from "./common";


export function installExtension(context: ExtendedContext, extension: ExtensionInfo, versionToInstall: string): void {
  if (extension.version !== versionToInstall) {
    throw new BadRequestError(`Installing version '${versionToInstall}' not supported, try '${extension.version}'.`);
  }
  const jarPath = context.bucketFs.resolvePath(extension.fileName);

  function createScript(script: ScriptDefinition): string {
    return `
      CREATE OR REPLACE JAVA ${script.type} SCRIPT ${context.qualifiedName(script.name)}(...) EMITS (${script.args}) AS
        %scriptclass ${script.scriptClass};
        %jar ${jarPath};`;
  }
  function createComment(script: ScriptDefinition): string {
    return `COMMENT ON SCRIPT ${context.qualifiedName(script.name)} IS 'Created by extension manager for Cloud Storage Extension ${extension.version}'`;
  }

  getAllScripts().forEach(script => context.sqlClient.execute(createScript(script)));
  getAllScripts().forEach(script => context.sqlClient.execute(createComment(script)));
}