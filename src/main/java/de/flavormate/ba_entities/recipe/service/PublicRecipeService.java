/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.recipe.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.flavormate.aa_interfaces.services.BaseService;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ab_exeptions.exceptions.ForbiddenException;
import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ad_configurations.flavormate.CommonConfig;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.recipe.repository.RecipeRepository;
import de.flavormate.ba_entities.schemas.mappers.SRecipeMapper;
import de.flavormate.ba_entities.token.enums.TokenType;
import de.flavormate.ba_entities.token.model.Token;
import de.flavormate.ba_entities.token.repository.TokenRepository;
import de.flavormate.utils.DurationUtils;
import de.flavormate.utils.JSONUtils;
import java.util.Base64;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicRecipeService extends BaseService {

  private final RecipeRepository recipeRepository;
  private final TokenRepository tokenRepository;
  private final TemplateEngine templateEngine;
  private final CommonConfig commonConfig;
  private final MessageSource messageSource;
  private final SRecipeMapper sRecipeMapper;

  public Recipe findByIdL10n(Long id, String language, String token) throws CustomException {
    var t =
        tokenRepository.findByToken(token).orElseThrow(() -> new NotFoundException(Token.class));

    if (!t.isValid() || !t.getType().equals(TokenType.SHARE) || !t.getContent().equals(id)) {
      throw new ForbiddenException(Token.class);
    }

    try {
      t.increaseUses();
      tokenRepository.save(t);
    } catch (Exception e) {
      log.error("Could not update token usage");
    }

    var recipe =
        recipeRepository.findById(id).orElseThrow(() -> new NotFoundException(Recipe.class));

    recipe.translate(language);
    return recipe;
  }

  public String frontPage(Long id, String language, String token)
      throws CustomException, JsonProcessingException {

    var recipe = findByIdL10n(id, language, token);

    var json =
        sRecipeMapper.fromRecipe(recipe, recipe.getServing().getAmount().intValue(), messageSource);

    var prepTime = DurationUtils.beautify(recipe.getPrepTime(), messageSource);
    var cookTime = DurationUtils.beautify(recipe.getCookTime(), messageSource);
    var restTime = DurationUtils.beautify(recipe.getRestTime(), messageSource);

    var diet = recipe.getDiet().getName(messageSource, commonConfig.getPreferredLanguage());
    var course = recipe.getCourse().getName(messageSource, commonConfig.getPreferredLanguage());

    Map<String, Object> data =
        Map.ofEntries(
            Map.entry("recipe", recipe),
            Map.entry("json", JSONUtils.toJsonString(json)),
            Map.entry("prepTime", prepTime),
            Map.entry("cookTime", cookTime),
            Map.entry("restTime", restTime),
            Map.entry("diet", diet),
            Map.entry("course", course),
            Map.entry("appUrl", getAppUrl(recipe.getId(), token)));

    var context = new Context(LocaleContextHolder.getLocale());

    context.setVariables(data);
    context.setVariable("frontendUrl", commonConfig.getFrontendUrl());
    context.setVariable("backendUrl", commonConfig.getBackendUrl());

    return templateEngine.process("public/recipe.html", context);
  }

  private String getAppUrl(long id, String token) {
    var serverUrl = commonConfig.getBackendUrl().toString();
    var serverUrlBase64 = Base64.getEncoder().encodeToString(serverUrl.getBytes());

    return String.format(
        "flavormate://open?server=%s&page=recipe&id=%s&token=%s", serverUrlBase64, id, token);
  }
}
