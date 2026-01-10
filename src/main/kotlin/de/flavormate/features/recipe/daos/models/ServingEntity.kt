/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.recipe.daos.models

import de.flavormate.shared.models.entities.CoreEntity
import de.flavormate.utils.NumberUtils
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "v3__recipe__serving")
class ServingEntity : CoreEntity() {
  var amount: Double = 0.0

  lateinit var label: String

  override fun toString(): String {
    return NumberUtils.beautify(amount) + " " + label
  }

  fun toString(requestedAmount: Int): String {
    return "$requestedAmount $label"
  }
}
