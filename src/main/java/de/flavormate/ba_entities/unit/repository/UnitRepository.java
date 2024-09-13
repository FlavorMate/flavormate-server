package de.flavormate.ba_entities.unit.repository;

import de.flavormate.ba_entities.unit.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UnitRepository extends JpaRepository<Unit, Long> {

	Optional<Unit> findByLabel(String label);

	@Query(nativeQuery = true, value = "SELECT u.* FROM units u LEFT JOIN ingredients i ON u.id = i.unit_id WHERE i.unit_id IS NULL")
	List<Unit> findEmpty();
}
