/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.role.repository;

import de.flavormate.ba_entities.role.model.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

  Optional<Role> findByLabel(String label);
}
