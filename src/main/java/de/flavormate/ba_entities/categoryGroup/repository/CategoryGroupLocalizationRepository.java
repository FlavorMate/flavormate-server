/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.categoryGroup.repository;

import de.flavormate.aa_interfaces.models.LocalizationId;
import de.flavormate.ba_entities.categoryGroup.model.CategoryGroupLocalization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryGroupLocalizationRepository
    extends JpaRepository<CategoryGroupLocalization, LocalizationId> {}
