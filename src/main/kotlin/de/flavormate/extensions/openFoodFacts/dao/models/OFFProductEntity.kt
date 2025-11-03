/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.openFoodFacts.dao.models

import de.flavormate.extensions.openFoodFacts.enums.OFFProductState
import de.flavormate.shared.models.entities.CoreEntity
import jakarta.persistence.*

@Entity
@Table(name = "v3__ext__off__product")
class OFFProductEntity : CoreEntity() {
  var carbohydrates: Double? = null

  @Column(name = "energy_kcal") var energyKcal: Double? = null

  var fat: Double? = null

  @Column(name = "saturated_fat") var saturatedFat: Double? = null

  var sugars: Double? = null

  var fiber: Double? = null

  var proteins: Double? = null

  var salt: Double? = null

  var sodium: Double? = null

  @Enumerated(EnumType.STRING) var state: OFFProductState? = null

  companion object {
    fun new(id: String): OFFProductEntity {
      return OFFProductEntity().apply {
        this.id = id
        this.state = OFFProductState.New
      }
    }
  }
}
