/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.unit.repository;

import de.flavormate.ba_entities.unit.model.UnitLocalized;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UnitLocalizedRepository extends JpaRepository<UnitLocalized, Long> {
  @Query(
      "SELECT lu FROM UnitLocalized lu JOIN lu.unitRef u WHERE u.id in :ids AND lu.language ="
          + " :language")
  List<UnitLocalized> findByIdsAndLanguage(
      @Param("ids") List<Long> ids, @Param("language") String language);

  @Query(
      "SELECT lu FROM UnitLocalized lu WHERE lower(lu.labelSg) = lower(:label) OR lower(lu.labelPl)"
          + " = lower(:label) OR lower(lu.labelSgAbrv) = lower(:label) OR lower(lu.labelPlAbrv) ="
          + " lower(:label)")
  List<UnitLocalized> findByLabel(@Param("label") String label);

  @Query(
      "SELECT lu FROM UnitLocalized lu WHERE (lower(lu.labelSg) = lower(:label) OR"
          + " lower(lu.labelPl) = lower(:label) OR lower(lu.labelSgAbrv) = lower(:label) OR"
          + " lower(lu.labelPlAbrv) = lower(:label)) AND lu.language = :language")
  List<UnitLocalized> findByLabelAndLanguage(
      @Param("label") String label, @Param("language") String language);
}
