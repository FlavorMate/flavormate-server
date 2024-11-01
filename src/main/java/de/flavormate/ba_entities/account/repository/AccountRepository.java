/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.account.repository;

import de.flavormate.ba_entities.account.model.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

  Optional<Account> findByUsername(String username);

  Optional<Account> findByMail(String mail);

  Boolean existsByMail(String mail);

  Boolean existsByUsername(String username);
}
