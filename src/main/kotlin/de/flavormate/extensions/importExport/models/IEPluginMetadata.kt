/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.models

import de.flavormate.extensions.importExport.models.inputSource.IEImportType
import de.flavormate.shared.enums.Language

data class IEPluginMetadata(
  val id: String,
  val name: Map<Language, String>,
  val version: String,
  val author: String,
  val import: List<IEImportType>,
  val importShortDescription: Map<Language, String>,
  val importLongDescription: Map<Language, String>,
  val importMimeTypes: List<String>,
  val importExtensions: List<String>,
  val export: Boolean,
  val exportShortDescription: Map<Language, String>,
  val exportLongDescription: Map<Language, String>,
) {
  init {
    val requiredLanguages = Language.entries.toSet()

    validateLanguageMap("name", name, requiredLanguages)
    validateLanguageMap("importDescriptionShort", importShortDescription, requiredLanguages)
    validateLanguageMap("importDescriptionLong", importLongDescription, requiredLanguages)
    validateLanguageMap("exportDescriptionShort", exportShortDescription, requiredLanguages)
    validateLanguageMap("exportDescriptionLong", exportLongDescription, requiredLanguages)
  }

  private fun validateLanguageMap(
    fieldName: String,
    value: Map<Language, String>,
    requiredLanguages: Set<Language>,
  ) {
    val missingLanguages = requiredLanguages - value.keys

    require(missingLanguages.isEmpty()) {
      "IEPluginMetadata.$fieldName is missing required languages: $missingLanguages"
    }
  }
}
