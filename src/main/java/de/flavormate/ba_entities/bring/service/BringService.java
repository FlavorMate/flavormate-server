package de.flavormate.ba_entities.bring.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ad_configurations.flavormate.CommonConfig;
import de.flavormate.ba_entities.bring.model.RecipeSchema;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.recipe.repository.RecipeRepository;
import de.flavormate.bb_thymeleaf.Fragments;
import de.flavormate.bb_thymeleaf.MainPage;
import de.flavormate.utils.JSONUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BringService {
	private final CommonConfig commonConfig;

	private final RecipeRepository recipeRepository;

	private final TemplateEngine templateEngine;

	public String get(Long id, Integer serving)
			throws JsonProcessingException, CustomException {

		var recipe = recipeRepository.findById(id).orElseThrow(() -> new NotFoundException(Recipe.class));

		RecipeSchema json = RecipeSchema.fromRecipe(recipe, serving);

		Map<String, Object> data = Map.ofEntries(
				Map.entry("json", JSONUtils.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json))
		);
		
		return new MainPage(templateEngine, commonConfig).process(Fragments.BRING, data);
	}


}