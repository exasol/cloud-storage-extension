import { Context } from "@exasol/extension-manager-interface";

export interface ExtensionInfo {
    version: string;
    fileName: string;
}

export type ExtendedContext = Context & {
    qualifiedName(name: string): string
}

export function extendContext(context: Context, extensionInfo: ExtensionInfo): ExtendedContext {
    return {
        ...context,
        qualifiedName(name: string) {
            return `"${context.extensionSchemaName}"."${name}"`;
        },
    }
}

