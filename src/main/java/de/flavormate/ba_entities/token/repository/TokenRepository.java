/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.token.repository;

import de.flavormate.ba_entities.token.model.Token;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {

  Optional<Token> findByToken(String token);
}
