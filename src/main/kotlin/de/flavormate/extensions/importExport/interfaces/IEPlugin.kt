/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.interfaces

import de.flavormate.extensions.importExport.models.ieRecipeDraft.IERecipeDraft
import de.flavormate.extensions.importExport.models.inputSource.IEInputSource
import java.io.OutputStream

interface IEPlugin {
  val metadata: IEPluginMetadata

  fun import(input: IEInputSource, context: IEPluginContext): IERecipeDraft {
    throw UnsupportedOperationException("Import not supported by plugin ${metadata.name}")
  }

  fun export(draft: IERecipeDraft, output: OutputStream, context: IEPluginContext) {
    throw UnsupportedOperationException("Export not supported by plugin ${metadata.name}")
  }
}
