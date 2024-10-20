package de.flavormate.ba_entities.unit.repository;

import de.flavormate.ba_entities.unit.model.UnitLocalized;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UnitLocalizedRepository extends JpaRepository<UnitLocalized, Long> {
	@Query("SELECT lu FROM UnitLocalized lu JOIN lu.unitRef u WHERE u.id in :ids AND lu.language = :language")
	List<UnitLocalized> findByIdsAndLanguage(@Param("ids") List<Long> ids, @Param("language") String language);

	@Query("SELECT lu FROM UnitLocalized lu WHERE lower(lu.labelSg) = lower(:label) OR lower(lu.labelPl) = lower(:label) OR lower(lu.labelSgAbrv) = lower(:label) OR lower(lu.labelPlAbrv) = lower(:label)")
	List<UnitLocalized> findByLabel(@Param("label") String label);
}


