package de.flavormate.ba_entities.ingredient.repository;

import de.flavormate.ba_entities.ingredient.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

	@Query("SELECT i FROM Ingredient i WHERE trim(i.unit.label) = ''")
	List<Ingredient> findByEmptyUnit();

	List<Ingredient> findBySchema(int schema);

	@Query("SELECT count(i) FROM Ingredient i WHERE i.schema = :i")
	int countBySchema(@Param("i") int i);
}
