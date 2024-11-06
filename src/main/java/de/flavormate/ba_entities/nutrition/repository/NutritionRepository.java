/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.nutrition.repository;

import de.flavormate.ba_entities.nutrition.model.Nutrition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NutritionRepository extends JpaRepository<Nutrition, Long> {}
