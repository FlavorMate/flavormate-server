package de.flavormate.ba_entities.categoryGroup.repository;

import de.flavormate.ba_entities.categoryGroup.model.CategoryGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryGroupRepository extends JpaRepository<CategoryGroup, Long> {
	@Query("select new de.flavormate.ba_entities.categoryGroup.model.CategoryGroup(c, l.value) from CategoryGroup c join c.localizations l where l.id.language = ?1 order by l.value")
	List<CategoryGroup> findAll(@Param("language") String language);

	@Query("select new de.flavormate.ba_entities.categoryGroup.model.CategoryGroup(c, l.value) from CategoryGroup c join c.localizations l where l.id.language = ?1 and c.id = ?2 order by l.value")
	List<CategoryGroup> findById(String language, Long id);
}