/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.token.service;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.aa_interfaces.services.BaseService;
import de.flavormate.aa_interfaces.services.ICRUDService;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ab_exeptions.exceptions.ForbiddenException;
import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ba_entities.account.model.Account;
import de.flavormate.ba_entities.account.repository.AccountRepository;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.recipe.repository.RecipeRepository;
import de.flavormate.ba_entities.token.enums.TokenType;
import de.flavormate.ba_entities.token.model.Token;
import de.flavormate.ba_entities.token.repository.TokenRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@ConditionalOnProperty(
    prefix = "flavormate.features.share",
    value = "enabled",
    havingValue = "true")
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService extends BaseService implements ICRUDService<Token, Long> {

  private final AccountRepository accountRepository;
  private final RecipeRepository recipeRepository;
  private final TokenRepository tokenRepository;

  @Override
  public Token create(Long recipeId) throws CustomException {
    recipeRepository.findById(recipeId).orElseThrow(() -> new NotFoundException(Recipe.class));

    var account =
        accountRepository
            .findByUsername(getPrincipal().getUsername())
            .orElseThrow(() -> new NotFoundException(Account.class));

    var token = Token.ShareToken(account, recipeId);

    return tokenRepository.save(token);
  }

  @Override
  public Token update(Long id, JsonNode form) throws CustomException {
    throw new NotFoundException(Token.class);
  }

  @Override
  public boolean deleteById(Long id) throws CustomException {
    var token = tokenRepository.findById(id).orElseThrow(() -> new NotFoundException(Token.class));

    var owner =
        accountRepository
            .findByUsername(getPrincipal().getUsername())
            .orElseThrow(() -> new NotFoundException(Account.class));

    if (!(owner.hasRole("ROLE_ADMIN") || token.getOwner().getId().equals(owner.getId()))) {
      throw new ForbiddenException(Token.class);
    }

    tokenRepository.deleteById(id);
    return true;
  }

  @Override
  public Token findById(Long id) throws CustomException {
    throw new NotFoundException(Token.class);
  }

  @Override
  public List<Token> findAll() throws CustomException {
    return tokenRepository.findAllByType(TokenType.SHARE);
  }
}
