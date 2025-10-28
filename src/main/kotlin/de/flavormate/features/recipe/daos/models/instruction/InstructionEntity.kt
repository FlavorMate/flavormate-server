/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.features.recipe.daos.models.instruction

import com.fasterxml.jackson.annotation.JsonIgnore
import de.flavormate.shared.models.entities.CoreEntity
import de.flavormate.utils.NumberUtils
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "v3__recipe__instruction_group__item")
class InstructionEntity : CoreEntity() {
  lateinit var label: String

  var index: Int = -1

  @ManyToOne
  @JoinColumn(name = "group_id", referencedColumnName = "id")
  lateinit var group: InstructionGroupEntity

  @JsonIgnore
  fun getCalculatedLabel(factor: Double): String {
    var copy = label
    var lIndex = -1
    var rIndex = -1
    do {
      lIndex = copy.indexOf("[[", lIndex + 1)
      rIndex = copy.indexOf("]]", rIndex + 1)

      if (lIndex != -1) {
        val foundText = copy.substring(lIndex + 2, rIndex)
        var newValue: Double = NumberUtils.tryParseDouble(foundText, 1.0)
        newValue *= (factor)
        copy = copy.replace(("\\[\\[$foundText]]").toRegex(), NumberUtils.beautify(newValue)!!)
      }
    } while (lIndex != -1)
    return copy
  }
}
