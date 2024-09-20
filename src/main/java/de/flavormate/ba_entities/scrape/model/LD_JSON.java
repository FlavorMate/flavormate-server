package de.flavormate.ba_entities.scrape.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.flavormate.ad_configurations.jackson.ImageDeserializer;
import de.flavormate.ad_configurations.jackson.ListOrStringDeserializer;
import de.flavormate.ba_entities.category.model.Category;
import de.flavormate.ba_entities.ingredient.model.Ingredient;
import de.flavormate.ba_entities.instruction.model.Instruction;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.tag.model.Tag;
import de.flavormate.utils.NumberUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LD_JSON {
	@JsonProperty("@context")
	private final String context = "https://schema.org";

	@JsonProperty("@type")
	private final String type = "Recipe";

	private String name;
	private String description;
	private String datePublished;
	@JsonDeserialize(using = ImageDeserializer.class)
	private List<String> image = new ArrayList<>();
	private String recipeYield;
	// Can be a list of strings or objects
	@JsonDeserialize(using = ListOrStringDeserializer.class)
	private List<Object> recipeIngredient = new ArrayList<>();
	// Can be list of steps or sections
	@JsonDeserialize(using = ListOrStringDeserializer.class)
	private List<Object> recipeInstructions = new ArrayList<>();
	private String cookTime;
	private String prepTime;
	private String totalTime;
	@JsonDeserialize(using = ListOrStringDeserializer.class)
	private List<String> recipeCategory = new ArrayList<>();
	private String recipeCuisine;
	@JsonDeserialize(using = ListOrStringDeserializer.class)
	private List<String> keywords = new ArrayList<>();
	private List<Object> tool = new ArrayList<>(); // Can be strings or other objects

	// To handle any unmapped fields
	private Map<String, Object> additionalProperties = new HashMap<>();

	public static LD_JSON fromRecipe(Recipe recipe) {
		var name = recipe.getLabel();
		List<String> image = null;
		if (!recipe.getFiles().isEmpty())
			image = recipe.getFiles().stream().map((file) -> "./data/" + recipe.getId() + "/" + file.getId() + ".jpg").toList();
		var datePublished = LocalDate.ofInstant(recipe.getCreatedOn(), ZoneOffset.UTC).toString();
		var description = recipe.getDescription();
		var prepTime = recipe.getPrepTime().toString();
		var cookTime = recipe.getCookTime().toString();
		var totalTime = recipe.getPrepTime().plus(recipe.getCookTime()).plus(recipe.getRestTime()).toString();
		var keywords = recipe.getTags().stream().map(Tag::getLabel).toList();
		var recipeYield = recipe.getServing().toString();
		var recipeCategory = recipe.getCategories().stream().map(Category::getLabel).toList();
		var recipeIngredient = new ArrayList<Object>(recipe.getIngredientGroups().stream().map((iG) -> iG.getIngredients().stream().map(LD_JSON::getIngredient).toList()).flatMap(Collection::stream).toList());
		var recipeInstructions = new ArrayList<Object>(recipe.getInstructionGroups().stream().map((iG) -> iG.getInstructions().stream().map(Instruction::getLabel).toList()).flatMap(Collection::stream).toList());


		return new LD_JSON(name, description, datePublished, image, recipeYield, recipeIngredient, recipeInstructions, cookTime, prepTime, totalTime, recipeCategory, null, keywords, null, null);
	}

	public static List<String> extractIngredients(List<Object> recipeIngredient) {
		List<String> ingredientList = new ArrayList<>();

		for (Object ingredient : recipeIngredient) {
			if (ingredient instanceof String) {
				// If the ingredient is a plain string, add it to the list
				ingredientList.add(StringUtils.trimToEmpty(ingredient.toString()));
			} else if (ingredient instanceof Map) {
				// If the ingredient is a structured object, try to extract the "item" or "name" field
				Map<String, Object> ingredientMap = (Map<String, Object>) ingredient;

				// Check for known keys in the structured object (like 'item' or 'name')
				if (ingredientMap.containsKey("item")) {
					ingredientList.add(StringUtils.trimToEmpty(ingredientMap.get("item").toString()));
				} else if (ingredientMap.containsKey("name")) {
					ingredientList.add(StringUtils.trimToEmpty(ingredientMap.get("name").toString()));
				}
			} else if (ingredient instanceof List) {
				// If it's a nested list (like from HowToSection), recurse through the list
				List<Object> nestedIngredients = (List<Object>) ingredient;
				ingredientList.addAll(extractIngredients(nestedIngredients)); // Recursive call
			}
		}

		return ingredientList;
	}

	public static List<String> extractInstructions(List<Object> recipeInstructions) {
		List<String> instructionList = new ArrayList<>();

		for (Object instruction : recipeInstructions) {
			if (instruction instanceof String) {
				// If the instruction is a plain string, add it to the list
				instructionList.add(StringUtils.trimToEmpty(instruction.toString()));
			} else if (instruction instanceof Map) {
				// If the instruction is a structured object, try to extract the "text" or "itemListElement"
				Map<String, Object> instructionMap = (Map<String, Object>) instruction;

				// Check for known keys in the structured object (like 'text' for HowToStep or HowToSection)
				if (instructionMap.containsKey("text")) {
					instructionList.add(StringUtils.trimToEmpty(instructionMap.get("text").toString()));
				} else if (instructionMap.containsKey("itemListElement")) {
					// If the instruction is a HowToSection, recursively extract from the itemListElement
					List<Object> nestedInstructions = (List<Object>) instructionMap.get("itemListElement");
					instructionList.addAll(extractInstructions(nestedInstructions)); // Recursive call
				}
			} else if (instruction instanceof List) {
				// If it's a nested list, recurse through the list (similar to recipeIngredient handling)
				List<Object> nestedInstructions = (List<Object>) instruction;
				instructionList.addAll(extractInstructions(nestedInstructions)); // Recursive call
			}
		}

		instructionList.removeIf(String::isBlank);
		return instructionList;
	}

	@JsonIgnore
	private static String getIngredient(Ingredient ingredient) {
		var parts = new ArrayList<String>();

		if (ingredient.getAmount() > 0) {
			parts.add(NumberUtils.beautifyDouble(ingredient.getAmount()));
		}

		if (ingredient.getUnit() != null) {
			parts.add(ingredient.getUnit().getLabel());
		}

		parts.add(ingredient.getLabel());

		return String.join(" ", parts);
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		additionalProperties.put(name, value);
	}
}





