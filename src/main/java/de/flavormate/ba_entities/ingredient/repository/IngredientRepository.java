package de.flavormate.ba_entities.ingredient.repository;

import de.flavormate.ba_entities.ingredient.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

}
