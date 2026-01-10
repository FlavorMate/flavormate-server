/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.category.daos.models

import de.flavormate.shared.models.localizations.Localization
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import jakarta.persistence.Table

@Entity
@Table(name = "v3__category__l10n")
class CategoryLocalizationEntity : Localization() {
  @ManyToOne @MapsId("foreignId") lateinit var category: CategoryEntity
}
