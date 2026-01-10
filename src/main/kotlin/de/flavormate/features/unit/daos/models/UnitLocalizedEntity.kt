/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.unit.daos.models

import de.flavormate.shared.models.entities.CoreEntity
import jakarta.persistence.*

@Entity
@Table(name = "v3__unit_l10n")
class UnitLocalizedEntity : CoreEntity() {
  @ManyToOne
  @JoinColumn(name = "unit_ref", referencedColumnName = "id", nullable = false)
  lateinit var unitRef: UnitRefEntity

  lateinit var language: String

  @Column(name = "label_sg") lateinit var labelSg: String

  @Column(name = "label_sg_abrv") var labelSgAbrv: String? = null

  @Column(name = "label_pl") var labelPl: String? = null

  @Column(name = "label_pl_abrv") var labelPlAbrv: String? = null

  @Transient
  fun getLabel(amount: Double?): String {
    if (amount != null && amount != 1.0) return labelPlAbrv ?: labelPl ?: labelSgAbrv ?: labelSg

    return labelSgAbrv ?: labelSg
  }
}
