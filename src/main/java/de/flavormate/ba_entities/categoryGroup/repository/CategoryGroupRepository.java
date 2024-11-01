/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.categoryGroup.repository;

import de.flavormate.ba_entities.categoryGroup.model.CategoryGroup;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryGroupRepository extends JpaRepository<CategoryGroup, Long> {
  @Query(
      "select new de.flavormate.ba_entities.categoryGroup.model.CategoryGroup(c, l.value) from"
          + " CategoryGroup c join c.localizations l where l.id.language = :language order by"
          + " l.value")
  List<CategoryGroup> findAll(@Param("language") String language);

  @Query(
      "select new de.flavormate.ba_entities.categoryGroup.model.CategoryGroup(c, l.value) from"
          + " CategoryGroup c join c.localizations l where l.id.language = :language and c.id = :id"
          + " order by l.value")
  List<CategoryGroup> findById(@Param("language") String language, @Param("id") Long id);
}
