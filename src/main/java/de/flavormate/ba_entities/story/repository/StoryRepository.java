/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.story.repository;

import de.flavormate.ba_entities.story.model.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StoryRepository extends JpaRepository<Story, Long> {

  void deleteAllByRecipeId(Long id);

  @Query("select s from Story s where lower(s.label) like lower(concat('%', ?1, '%'))")
  Page<Story> findBySearch(String searchTerm, Pageable pageable);
}
