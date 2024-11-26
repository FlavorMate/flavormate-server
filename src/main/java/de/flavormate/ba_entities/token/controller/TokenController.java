/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.token.controller;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.aa_interfaces.controllers.ICRUDController;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.token.model.Token;
import de.flavormate.ba_entities.token.model.TokenForm;
import de.flavormate.ba_entities.token.service.TokenService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ConditionalOnProperty(
    prefix = "flavormate.features.share",
    value = "enabled",
    havingValue = "true")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/token")
public class TokenController implements ICRUDController<Token, TokenForm> {

  private final TokenService tokenService;

  @Override
  public Token create(TokenForm form) throws CustomException {
    return tokenService.create(form.id());
  }

  @Override
  public Token update(Long id, JsonNode form) throws CustomException {
    throw new NotFoundException(Token.class);
  }

  @Override
  public boolean deleteById(Long id) throws CustomException {
    return tokenService.deleteById(id);
  }

  @Override
  public Token findById(Long id) throws CustomException {
    throw new NotFoundException(Token.class);
  }

  @Secured("ROLE_ADMIN")
  @Override
  public List<Token> findAll() throws CustomException {
    throw new NotFoundException(Recipe.class);
  }
}
