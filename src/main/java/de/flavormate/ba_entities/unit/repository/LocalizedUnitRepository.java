package de.flavormate.ba_entities.unit.repository;

import de.flavormate.ba_entities.unit.model.LocalizedUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocalizedUnitRepository extends JpaRepository<LocalizedUnit, Long> {
	@Query("SELECT lu FROM LocalizedUnit lu JOIN lu.unitRef u WHERE u.id in :ids AND lu.language = :language")
	List<LocalizedUnit> findByIdsAndLanguage(@Param("ids") List<Long> ids, @Param("language") String language);
}


