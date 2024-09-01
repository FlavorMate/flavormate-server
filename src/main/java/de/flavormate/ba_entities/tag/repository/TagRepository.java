package de.flavormate.ba_entities.tag.repository;

import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.tag.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

	Optional<Tag> findByLabel(String label);

	@Query("select t from Tag t where t.recipes is empty")
	List<Tag> findAllEmpty();

	List<Tag> findAllByLabelIn(List<String> list);

	@Query(value = "select t from Tag t where lower(t.label) like lower(concat('%', :searchTerm, '%'))")
	Page<Tag> findBySearch(@Param("searchTerm") String searchTerm, Pageable pageable);

	@Query("select t.recipes from Tag t where t.id = ?1")
	Page<Recipe> findRecipesFromParent(Long id, Pageable pageable);
}
