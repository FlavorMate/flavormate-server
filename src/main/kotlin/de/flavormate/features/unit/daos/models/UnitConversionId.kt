/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.unit.daos.models

import jakarta.persistence.Embeddable
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.util.*

@Embeddable
class UnitConversionId {
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "source", referencedColumnName = "id")
  lateinit var source: UnitRefEntity

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "target", referencedColumnName = "id")
  lateinit var target: UnitRefEntity

  override fun hashCode(): Int {
    return Objects.hash(source.id, target.id)
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null) return false
    if (javaClass != other.javaClass) return false
    return Objects.hash(source.id, target.id) == other.hashCode()
  }
}
