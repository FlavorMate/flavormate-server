/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.interfaces

data class IEPluginMetadata(
  val id: String,
  val name: String,
  val version: String,
  val author: String? = null,
  val description: String? = null,
  val import: Boolean = false,
  val export: Boolean = false,
  val supportedMimeTypes: List<String> = emptyList(),
  val supportedExtensions: List<String> = emptyList(),
)
