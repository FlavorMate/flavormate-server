/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.bring.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.flavormate.ba_entities.file.model.File;
import de.flavormate.ba_entities.ingredient.model.Ingredient;
import de.flavormate.ba_entities.ingredientGroup.model.IngredientGroup;
import de.flavormate.ba_entities.instruction.model.Instruction;
import de.flavormate.ba_entities.instructionGroup.model.InstructionGroup;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.tag.model.Tag;
import de.flavormate.ba_entities.unit.model.Unit;
import de.flavormate.ba_entities.unit.model.UnitLocalized;
import de.flavormate.utils.NumberUtils;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.MessageSource;

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

  public static RecipeSchema fromRecipe(
      Recipe recipe, Integer serving, MessageSource messageSource) {

    var name = recipe.getLabel();

    var images =
        recipe.getFiles().stream().filter(Objects::nonNull).map(File::getFullPath).toList();

    var author = new AuthorSchema(recipe.getAuthor().getAccount().getDisplayName());

    var datePublished = recipe.getCreatedOn().toString();

    var description = recipe.getDescription();

    var prepTime = recipe.getPrepTime().toString();

    var cookTime = recipe.getCookTime().plus(recipe.getRestTime()).toString();

    var totalTime =
        recipe.getCookTime().plus(recipe.getRestTime()).plus(recipe.getPrepTime()).toString();

    var keywords = recipe.getTags().stream().map(Tag::getLabel).collect(Collectors.joining(", "));

    var recipeYield = recipe.getServing().toString(serving);

    var recipeCategory = recipe.getCourse().getName(messageSource);

    var factor = serving / recipe.getServing().getAmount();

    var recipeIngredients =
        recipe.getIngredientGroups().stream()
            .map(group -> handleIngredientGroup(group, factor))
            .flatMap(Collection::stream)
            .toList();

    var recipeInstructions =
        recipe.getInstructionGroups().stream()
            .map(iG -> handleInstructionGroup(iG, factor))
            .flatMap(Collection::stream)
            .map(InstructionSchema::new)
            .toList();

    var url = recipe.getUrl();

    return new RecipeSchema(
        name,
        images,
        author,
        datePublished,
        description,
        prepTime,
        cookTime,
        totalTime,
        keywords,
        recipeYield,
        recipeCategory,
        recipeIngredients,
        recipeInstructions,
        url);
  }

  private static List<String> handleIngredientGroup(
      IngredientGroup ingredientGroup, double factor) {
    return ingredientGroup.getIngredients().stream()
        .map((ingredient -> handleIngredients(ingredient, factor)))
        .toList();
  }

  private static String handleIngredients(Ingredient ingredient, double factor) {
    var amount =
        Optional.ofNullable(ingredient.getAmount())
            .map((a) -> NumberUtils.beautify(a * factor))
            .orElse(null);

    var unit = handleUnit(ingredient.getUnit(), ingredient.getUnitLocalized(), factor);

    var label = ingredient.getLabel();

    return Stream.of(amount, unit, label).filter(Objects::nonNull).collect(Collectors.joining(" "));
  }

  private static String handleUnit(Unit unit, UnitLocalized unitLocalized, double factor) {
    // Unit v1
    if (unit != null) {
      return unit.getLabel();
    }
    // Unit v2
    else if (unitLocalized != null) {
      return unitLocalized.getLabel(factor);
    } else {
      return null;
    }
  }

  private static List<String> handleInstructionGroup(
      InstructionGroup instructionGroup, double factor) {
    return instructionGroup.getInstructions().stream()
        .map((instruction) -> handleInstruction(instruction, factor))
        .toList();
  }

  private static String handleInstruction(Instruction instruction, double factor) {
    return instruction.getCalculatedLabel(factor);
  }
}
