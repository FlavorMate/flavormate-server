/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.bring.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ad_configurations.flavormate.CommonConfig;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.recipe.repository.RecipeRepository;
import de.flavormate.ba_entities.schemas.mappers.SRecipeMapper;
import de.flavormate.bb_thymeleaf.Fragments;
import de.flavormate.bb_thymeleaf.MainPage;
import de.flavormate.utils.JSONUtils;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

@Service
@RequiredArgsConstructor
public class BringService {
  private final CommonConfig commonConfig;

  private final RecipeRepository recipeRepository;

  private final TemplateEngine templateEngine;

  private final MessageSource messageSource;

  private final SRecipeMapper sRecipeMapper;

  public String get(Long id, Integer serving) throws JsonProcessingException, CustomException {

    var recipe =
        recipeRepository.findById(id).orElseThrow(() -> new NotFoundException(Recipe.class));

    final var sRecipe = sRecipeMapper.fromRecipe(recipe, serving, messageSource);

    Map<String, Object> data = Map.ofEntries(Map.entry("recipe", JSONUtils.toJsonString(sRecipe)));

    return new MainPage(templateEngine, commonConfig).process(Fragments.BRING, data);
  }
}
