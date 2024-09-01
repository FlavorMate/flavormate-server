package de.flavormate.ba_entities.role.repository;

import de.flavormate.ba_entities.role.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByLabel(String label);
}
