package de.flavormate.ba_entities.unit.repository;

import de.flavormate.ba_entities.unit.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnitRepository extends JpaRepository<Unit, Long> {

	Optional<Unit> findByLabel(String label);
}
