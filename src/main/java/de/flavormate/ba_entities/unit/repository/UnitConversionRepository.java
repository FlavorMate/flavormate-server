/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.unit.repository;

import de.flavormate.ba_entities.unit.model.UnitConversion;
import de.flavormate.ba_entities.unit.model.UnitConversionId;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UnitConversionRepository extends JpaRepository<UnitConversion, UnitConversionId> {

  @Query(
      "SELECT uc.factor FROM UnitConversion uc WHERE uc.id.from.id = :from AND uc.id.to.id = :to")
  Optional<Double> findFactor(@Param("from") Long from, @Param("to") Long to);

  @Query("SELECT uc.id.to.id FROM UnitConversion uc WHERE uc.id.from.id = :from")
  List<Long> findConversions(@Param("from") Long from);
}
