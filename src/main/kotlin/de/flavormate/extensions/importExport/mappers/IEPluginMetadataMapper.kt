/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.mappers

import de.flavormate.extensions.importExport.models.IEPluginMetadata
import de.flavormate.extensions.importExport.models.IEPluginMetadataDto
import de.flavormate.shared.enums.Language

object IEPluginMetadataMapper {
  fun map(input: IEPluginMetadata, language: Language): IEPluginMetadataDto =
    IEPluginMetadataDto(
      id = input.id,
      name = input.name[language]!!,
      version = input.version,
      author = input.author,
      import = input.import,
      importShortDescription = input.importShortDescription[language]!!,
      importLongDescription = input.importLongDescription[language]!!,
      importMimeTypes = input.importMimeTypes,
      importExtensions = input.importExtensions,
      export = input.export,
      exportShortDescription = input.exportShortDescription[language]!!,
      exportLongDescription = input.exportLongDescription[language]!!,
    )
}
