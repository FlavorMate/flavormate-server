package de.flavormate.ba_entities.account.repository;

import de.flavormate.ba_entities.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

	Optional<Account> findByUsername(String username);

	Optional<Account> findByMail(String mail);

	Boolean existsByMail(String mail);

	Boolean existsByUsername(String username);

}
