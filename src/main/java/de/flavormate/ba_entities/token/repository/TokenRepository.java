package de.flavormate.ba_entities.token.repository;

import de.flavormate.ba_entities.token.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

	Optional<Token> findByToken(String token);

}
