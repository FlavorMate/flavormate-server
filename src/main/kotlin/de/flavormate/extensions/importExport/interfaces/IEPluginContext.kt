/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.interfaces

import com.fasterxml.jackson.databind.ObjectMapper
import de.flavormate.features.account.dao.models.AccountEntity
import io.quarkus.runtime.configuration.MemorySize

data class IEPluginContext(
  val currentUser: AccountEntity?,
  val objectMapper: ObjectMapper,
  val maxImageSize: MemorySize,
)
