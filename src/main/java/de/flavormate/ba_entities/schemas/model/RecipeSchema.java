package de.flavormate.ba_entities.schemas.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.flavormate.ba_entities.recipe.model.Recipe;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@RequiredArgsConstructor
public class RecipeSchema {

	@JsonProperty("@context")
	private final String context = "https://schema.org/";

	@JsonProperty("@type")
	private final String type = "Recipe";

	private final String name;

	private final List<String> image;

	private final AuthorSchema author;

	private final String datePublished;

	private final String description;

	private final String prepTime;

	private final String cookTime;

	private final String totalTime;

	private final String keywords;

	private final String recipeYield;

	private final String recipeCategory;

	@JsonProperty("recipeIngredient")
	private final List<String> recipeIngredients;

	@JsonProperty("recipeInstructions")
	private final List<InstructionSchema> recipeInstructions;

	private final String url;

	public static RecipeSchema fromRecipe(Recipe recipe, Integer serving, String backendBaseUrl) {

		var name = recipe.getLabel();

		var images =
				recipe.getFiles().stream().map(f -> backendBaseUrl + "/" + f.getPath()).toList();

		var author = new AuthorSchema(recipe.getAuthor().getAccount().getDisplayName());

		var datePublished = recipe.getCreatedOn().toString();

		var description = recipe.getDescription();

		var prepTime = recipe.getPrepTime().toString();

		var cookTime = recipe.getCookTime().plus(recipe.getRestTime()).toString();

		var totalTime = recipe.getCookTime().plus(recipe.getRestTime()).plus(recipe.getPrepTime())
				.toString();

		var keywords =
				StringUtils.join(recipe.getTags().stream().map(t -> t.getLabel()).toList(), ", ");

		var recipeYield = recipe.getServing().toString(serving);

		var recipeCategory = recipe.getCourse().toString();

		var recipeIngredients = recipe.getIngredientGroups().stream()
				.map(iG -> iG.getIngredients().stream().map(i -> {
					i.setAmount(i.getAmount() * (serving / recipe.getServing().getAmount()));
					return i.toString();
				}).toList()).flatMap(Collection::stream).toList();

		var recipeInstructions = recipe.getInstructionGroups().stream()
				.map(iG -> iG.getInstructions().stream()
						.map(i -> new InstructionSchema(i.getLabel())).toList())
				.flatMap(Collection::stream).toList();

		var url = recipe.getUrl();

		return new RecipeSchema(name, images, author, datePublished, description, prepTime,
				cookTime, totalTime, keywords, recipeYield, recipeCategory, recipeIngredients,
				recipeInstructions, url);
	}
}


