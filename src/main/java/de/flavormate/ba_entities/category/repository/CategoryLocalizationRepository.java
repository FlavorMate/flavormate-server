package de.flavormate.ba_entities.category.repository;

import de.flavormate.aa_interfaces.models.LocalizationId;
import de.flavormate.ba_entities.category.model.CategoryLocalization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryLocalizationRepository extends JpaRepository<CategoryLocalization, LocalizationId> {

}
