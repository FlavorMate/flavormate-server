/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.shared.models.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntityBase
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import java.util.*

@MappedSuperclass
open class CoreEntity : PanacheEntityBase {
  @Id var id: String = UUID.randomUUID().toString()

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || this::class != other::class) return false

    return hashCode() == other.hashCode()
  }

  override fun hashCode(): Int {
    return Objects.hash(id)
  }
}
