/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.categoryGroup.daos.models

import de.flavormate.features.category.daos.models.CategoryEntity
import de.flavormate.shared.models.entities.CoreEntity
import io.quarkus.runtime.annotations.RegisterForReflection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "v3__category_group")
@RegisterForReflection
class CategoryGroupEntity : CoreEntity() {
  lateinit var label: String

  @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
  lateinit var categories: MutableList<CategoryEntity>

  @OneToMany(mappedBy = "categoryGroup", fetch = FetchType.LAZY)
  lateinit var localizations: MutableList<CategoryGroupLocalizationEntity>

  fun translate(language: String) {
    val localization = localizations.first { it.id.language == language }
    label = localization.value
  }
}
