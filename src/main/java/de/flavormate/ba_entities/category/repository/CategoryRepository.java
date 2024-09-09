package de.flavormate.ba_entities.category.repository;

import de.flavormate.ba_entities.category.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	@Query("select c from Category c join c.localizations l where lower(l.value) = lower(:label) order by l.value")
	Optional<Category> findByLabel(@Param("label") String label);

	@Query("select new de.flavormate.ba_entities.category.model.Category(c, l.value) from Category c join c.localizations l where l.id.language = ?1 order by l.value")
	Page<Category> findByPage(String language, Pageable pageable);

	@Query("select new de.flavormate.ba_entities.category.model.Category(c, l.value) from Category c join c.localizations l where l.id.language = ?1 order by l.value")
	List<Category> findAll(String language);

	@Query("select new de.flavormate.ba_entities.category.model.Category(c, l.value) from Category c join c.localizations l where l.id.language = ?1 and c.id = ?2 order by l.value")
	List<Category> findById(String language, Long id);

	@Query("select new de.flavormate.ba_entities.category.model.Category(c, l.value) from Category c join c.localizations l where l.id.language = ?1 and c.recipes is not empty order by l.value")
	Page<Category> findByNotEmpty(String language, Pageable pageable);

	@Query("select new de.flavormate.ba_entities.category.model.Category(c, l.value) from Category c join c.localizations l where l.id.language = :language and lower(l.value) like lower(concat('%', :search, '%')) order by l.value")
	Page<Category> findBySearch(@Param("language") String language, @Param("search") String searchTerm, Pageable pageable);

	@Query("select new de.flavormate.ba_entities.category.model.Category(c, l.value) from Category c join c.localizations l where l.id.language = :language order by l.value")
	List<Category> findByRaw(String language);
}
