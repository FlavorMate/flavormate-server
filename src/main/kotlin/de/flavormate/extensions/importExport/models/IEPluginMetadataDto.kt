/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.models

import de.flavormate.extensions.importExport.models.inputSource.IEImportType

data class IEPluginMetadataDto(
  val id: String,
  val name: String,
  val version: String,
  val author: String,
  val import: List<IEImportType>,
  val importShortDescription: String,
  val importLongDescription: String,
  val importMimeTypes: List<String>,
  val importExtensions: List<String>,
  val export: Boolean,
  val exportShortDescription: String,
  val exportLongDescription: String,
)
