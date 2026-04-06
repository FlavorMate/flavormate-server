/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.interfaces

import de.flavormate.extensions.importExport.models.inputSource.IEImportType

data class IEPluginMetadata(
  val id: String,
  val name: String,
  val version: String,
  val author: String? = null,
  val description: String? = null,
  val import: List<IEImportType>,
  val export: Boolean,
  val supportedMimeTypes: List<String>,
  val supportedExtensions: List<String>,
)
