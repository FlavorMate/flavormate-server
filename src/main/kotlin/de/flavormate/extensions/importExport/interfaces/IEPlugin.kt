/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.interfaces

import de.flavormate.extensions.importExport.models.ieRecipe.IERecipe
import de.flavormate.extensions.importExport.models.ieRecipeDraft.IERecipeDraft
import de.flavormate.extensions.importExport.models.inputSource.IEInputSource
import java.nio.file.Path

interface IEPlugin {
  val metadata: IEPluginMetadata

  fun import(input: List<IEInputSource>, context: IEPluginContext): IERecipeDraft {
    throw UnsupportedOperationException("Import not supported by plugin ${metadata.name}")
  }

  fun export(input: List<IERecipe>, workDirectory: Path, context: IEPluginContext): Path {
    throw UnsupportedOperationException("Export not supported by plugin ${metadata.name}")
  }
}
