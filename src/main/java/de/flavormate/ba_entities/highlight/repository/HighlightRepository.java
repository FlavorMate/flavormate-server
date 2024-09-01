package de.flavormate.ba_entities.highlight.repository;

import de.flavormate.ba_entities.highlight.model.Highlight;
import de.flavormate.ba_entities.recipe.enums.RecipeDiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HighlightRepository extends JpaRepository<Highlight, Long> {

	Long countByDiet(RecipeDiet diet);

	void deleteAllByRecipeId(Long id);

	List<Highlight> findAllByDateBefore(LocalDate date);

	List<Highlight> findAllByDiet(RecipeDiet diet);

	Page<Highlight> findAllByDiet(RecipeDiet diet, Pageable pageable);

}
