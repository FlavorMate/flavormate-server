package de.flavormate.ba_entities.bring.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ad_configurations.flavormate.CommonConfig;
import de.flavormate.ba_entities.bring.model.RecipeSchema;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.recipe.repository.RecipeRepository;
import de.flavormate.utils.JSONUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class BringService {
	private final CommonConfig commonConfig;

	private final RecipeRepository recipeRepository;

	private final TemplateEngine templateEngine;

	public String get(Long id, Integer serving)
			throws JsonProcessingException, CustomException {

		var recipe = recipeRepository.findById(id).orElseThrow(() -> new NotFoundException(Recipe.class));

		Context context = new Context();

		RecipeSchema json = RecipeSchema.fromRecipe(recipe, serving, commonConfig.backendUrl().toString());

		context.setVariable("backendUrl", commonConfig.backendUrl());

		context.setVariable("json",
				JSONUtils.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));


		return templateEngine.process("bring.html", context);
	}


}
