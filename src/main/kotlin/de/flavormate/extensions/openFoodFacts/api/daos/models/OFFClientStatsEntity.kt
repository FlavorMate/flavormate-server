/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.openFoodFacts.api.daos.models

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "v3__ext__off__api_usage")
class OFFClientStatsEntity() {
  @Id var id: String = UUID.randomUUID().toString()

  var timestamp: LocalDateTime = LocalDateTime.now().withSecond(0).withNano(0)

  var requests: Int = 0
}
