package de.flavormate.ba_entities.recipe.repository;

import de.flavormate.ba_entities.recipe.enums.RecipeDiet;
import de.flavormate.ba_entities.recipe.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

	Long countByDietIn(List<RecipeDiet> diet);

	@Query("select r from Recipe r join r.books b where b.id = :id")
	Page<Recipe> findByBook(@Param("id") Long id, Pageable pageable);

	@Query("select r from Recipe r join r.books b where b.id = :id and b.owner.account.username = :username")
	Page<Recipe> findByBookPrivate(@Param("id") Long id, @Param("username") String username, Pageable pageable);

	@Query("select r from Recipe r join r.categories c where c.id = :id")
	Page<Recipe> findByCategory(@Param("id") Long id, Pageable pageable);

	@Query("select r from Recipe r join r.tags t where t.id = :id")
	Page<Recipe> findByTag(@Param("id") Long id, Pageable pageable);

	@Query("select r from Recipe r where lower(r.label) like lower(concat('%', :label, '%')) and r.diet in :diet")
	Page<Recipe> findBySearch(List<RecipeDiet> diet, String label, Pageable pageable);

	@Query(nativeQuery = true,
			value = "SELECT * FROM RECIPES WHERE diet = ANY (?1) AND course LIKE ?2 ORDER BY RANDOM() LIMIT ?3")
	List<Recipe> findRandomRecipeByDiet(String[] diets, String course, int amount);

	@Query("select r from Recipe r where r.diet in :diet order by r.label")
	Page<Recipe> findByDiet(@Param("diet") String[] diet, Pageable pageable);

}
