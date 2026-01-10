/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.categoryGroup.daos.models

import de.flavormate.shared.models.localizations.Localization
import jakarta.persistence.*

@Entity
@Table(name = "v3__category_group__l10n")
class CategoryGroupLocalizationEntity : Localization() {
  @ManyToOne
  @JoinColumn(name = "category_group_id", referencedColumnName = "id")
  @MapsId("foreignId")
  lateinit var categoryGroup: CategoryGroupEntity
}
