/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.recipe.service;

import de.flavormate.aa_interfaces.services.BaseService;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ab_exeptions.exceptions.ForbiddenException;
import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.recipe.repository.RecipeRepository;
import de.flavormate.ba_entities.token.enums.TokenType;
import de.flavormate.ba_entities.token.model.Token;
import de.flavormate.ba_entities.token.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicRecipeService extends BaseService {

  private final RecipeRepository recipeRepository;
  private final TokenRepository tokenRepository;

  public Recipe findByIdL10n(Long id, String language, String token) throws CustomException {
    var t =
        tokenRepository.findByToken(token).orElseThrow(() -> new NotFoundException(Token.class));

    if (!t.isValid() || !t.getType().equals(TokenType.SHARE) || !t.getContent().equals(id)) {
      throw new ForbiddenException(Token.class);
    }

    var recipe =
        recipeRepository.findById(id).orElseThrow(() -> new NotFoundException(Recipe.class));

    recipe.translate(language);
    return recipe;
  }
}
