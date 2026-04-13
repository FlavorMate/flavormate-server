/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.models

import de.flavormate.extensions.importExport.models.inputSource.IEImportType
import de.flavormate.shared.enums.Language

data class IEPluginMetadata(
  val id: String,
  val name: Map<Language, String>,
  val version: String,
  val author: String? = null,
  val description: Map<Language, String>? = emptyMap(),
  val import: List<IEImportType>,
  val export: Boolean,
  val supportedMimeTypes: List<String>,
  val supportedExtensions: List<String>,
)
