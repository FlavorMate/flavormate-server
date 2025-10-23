/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.schemas.mappers;

import de.flavormate.aa_interfaces.models.Localization;
import de.flavormate.aa_interfaces.models.ManualBaseEntity;
import de.flavormate.ad_configurations.flavormate.CommonConfig;
import de.flavormate.ba_entities.category.model.Category;
import de.flavormate.ba_entities.category.repository.CategoryRepository;
import de.flavormate.ba_entities.file.model.File;
import de.flavormate.ba_entities.ingredient.wrapper.IngredientDraft;
import de.flavormate.ba_entities.ingredientGroup.model.IngredientGroup;
import de.flavormate.ba_entities.ingredientGroup.wrapper.IngredientGroupDraft;
import de.flavormate.ba_entities.instruction.wrapper.InstructionDraft;
import de.flavormate.ba_entities.instructionGroup.model.InstructionGroup;
import de.flavormate.ba_entities.instructionGroup.wrapper.InstructionGroupDraft;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.recipe.wrapper.RecipeDraft;
import de.flavormate.ba_entities.schemas.SRecipe;
import de.flavormate.ba_entities.schemas.helpers.SPerson;
import de.flavormate.ba_entities.serving.wrapper.ServingDraft;
import de.flavormate.ba_entities.tag.model.Tag;
import de.flavormate.ba_entities.tag.wrapper.TagDraft;
import de.flavormate.ba_entities.unit.model.UnitLocalized;
import de.flavormate.ba_entities.unit.repository.UnitLocalizedRepository;
import de.flavormate.utils.NumberUtils;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SRecipeMapper {
  private final CommonConfig commonConfig;

  private final CategoryRepository categoryRepository;
  private final UnitLocalizedRepository unitLocalizedRepository;

  public SRecipe fromRecipe(Recipe recipe, Integer requestedServing, MessageSource messageSource) {
    final var factor = requestedServing / recipe.getServing().getAmount();

    final var author = recipe.getAuthor().getAccount().getDisplayName();

    final var cookTime = recipe.getCookTime();

    final var dateCreated = recipe.getCreatedOn();

    final var dateModified = recipe.getLastModifiedOn();

    final var datePublished = recipe.getCreatedOn();

    final var description = recipe.getDescription();

    final var image = recipe.getFiles().stream().map(File::getFullPath).toList();

    // TODO: Add language field in recipe
    final var inLanguage = "";

    final var keywords = recipe.getTags().stream().map(Tag::getLabel).toList();

    final var name = recipe.getLabel();

    final var prepTime = recipe.getPrepTime();

    final var recipeCategory =
        recipe.getCategories().stream()
            .map(this::getLocalizationValue)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toCollection(ArrayList::new));
    recipeCategory.add(
        recipe.getCourse().getName(messageSource, commonConfig.getPreferredLanguage()));
    recipeCategory.add(
        recipe.getDiet().getName(messageSource, commonConfig.getPreferredLanguage()));

    final var recipeIngredient =
        recipe.getIngredientGroups().stream()
            .map(IngredientGroup::getIngredients)
            .flatMap(List::stream)
            .map(i -> i.requestServing(factor))
            .toList();

    final var recipeInstruction =
        recipe.getInstructionGroups().stream()
            .map(InstructionGroup::getInstructions)
            .flatMap(List::stream)
            .map(i -> i.getCalculatedLabel(factor))
            .toList();

    final var recipeYield = recipe.getServing().toString();

    final var totalTime = recipe.getTotalTime();

    final var url = recipe.getUrl();

    final var yield = recipe.getServing().toString();

    final var sRecipe = new SRecipe();
    sRecipe.setAuthor(new SPerson(author));
    sRecipe.setCookTime(cookTime);
    sRecipe.setDateCreated(dateCreated);
    sRecipe.setDateModified(dateModified);
    sRecipe.setDatePublished(datePublished);
    sRecipe.setDescription(description);
    sRecipe.setImage(image);
    sRecipe.setInLanguage(inLanguage);
    sRecipe.setKeywords(keywords);
    sRecipe.setName(name);
    sRecipe.setPrepTime(prepTime);
    sRecipe.setRecipeCategory(recipeCategory);
    sRecipe.setRecipeIngredient(recipeIngredient);
    sRecipe.setRecipeInstructions(recipeInstruction);
    sRecipe.setRecipeYield(recipeYield);
    sRecipe.setTotalTime(totalTime);
    sRecipe.setUrl(url);
    sRecipe.setYield(yield);
    return sRecipe;
  }

  private Optional<String> getLocalizationValue(Category category) {
    return category.getLocalizations().stream()
        .filter(
            localization ->
                localization.getId().getLanguage().equals(commonConfig.getPreferredLanguage()))
        .findFirst()
        .map(Localization::getValue);
  }

  public RecipeDraft toRecipeDraft(SRecipe sRecipe, String url) {
    var categories = getCategories(sRecipe.getRecipeCategory());
    List<IngredientGroupDraft> ingredientGroups =
        getIngredientGroups(sRecipe.getRecipeIngredient(), commonConfig.getPreferredLanguage());
    var instructionGroups = getInstructionGroups(sRecipe.getRecipeInstructions());
    var serving = getServiceDraft(sRecipe.getRecipeYield());
    var tags = getTags(sRecipe.getKeywords());
    var cookTime = sRecipe.getCookTime();
    var prepTime = sRecipe.getPrepTime();
    var description = StringUtils.trimToNull(sRecipe.getDescription());
    var label = StringUtils.trimToNull(sRecipe.getName());

    return new RecipeDraft(
        categories,
        ingredientGroups,
        instructionGroups,
        serving,
        tags,
        cookTime,
        prepTime,
        Duration.ZERO,
        null,
        null,
        description,
        label,
        url,
        null);
  }

  private List<Long> getCategories(List<String> recipeCategory) {
    return recipeCategory.stream()
        .map(
            (category) ->
                categoryRepository.findByLabel(category).map(ManualBaseEntity::getId).orElse(null))
        .filter(Objects::nonNull)
        .toList();
  }

  private List<IngredientGroupDraft> getIngredientGroups(
      List<String> recipeIngredient, String language) {
    ArrayList<IngredientDraft> ingredients = new ArrayList<>();

    for (var ingredient : recipeIngredient) {
      // e.g. Convert ¼ into 0.25
      ingredient = NumberUtils.convertExtendedFractionString(ingredient);
      ingredient = StringUtils.trimToEmpty(ingredient);

      var ingredientParts = ingredient.split(" ");

      Double amount = NumberUtils.tryParseDouble(ingredientParts[0], null);
      String label;
      UnitLocalized unit;

      int startIndex;

      // The ingredient has no amount
      if (amount == null || amount <= 0) {
        unit =
            unitLocalizedRepository.findByLabelAndLanguage(ingredientParts[0], language).stream()
                .findFirst()
                .orElse(null);
        if (unit == null) {
          startIndex = 0;
        } else {
          startIndex = 1;
        }
      } else {
        unit =
            unitLocalizedRepository.findByLabelAndLanguage(ingredientParts[1], language).stream()
                .findFirst()
                .orElse(null);
        if (unit == null) {
          startIndex = 1;
        } else {
          startIndex = 2;
        }
      }
      var labelParts = List.of(ingredientParts).subList(startIndex, ingredientParts.length);
      label = String.join(" ", labelParts);

      ingredients.add(new IngredientDraft(amount, null, label, unit, null));
    }

    return List.of(new IngredientGroupDraft("", ingredients));
  }

  private List<InstructionGroupDraft> getInstructionGroups(List<String> recipeInstructions) {
    var instructions = new ArrayList<InstructionDraft>();

    for (var instruction : recipeInstructions) {
      for (var line : instruction.split("\n")) {
        line = StringUtils.trimToEmpty(line);
        if (line.isBlank()) continue;
        instructions.add(new InstructionDraft(line));
      }
    }

    return List.of(new InstructionGroupDraft("", instructions));
  }

  private ServingDraft getServiceDraft(String recipeYield) {
    final var parts = recipeYield.split(" ");

    Double amount = NumberUtils.tryParseDouble(parts[0], null);

    if (amount != null && amount > 0)
      return new ServingDraft(amount, String.join(" ", List.of(parts).subList(1, parts.length)));
    else return new ServingDraft(amount, recipeYield);
  }

  private List<TagDraft> getTags(List<String> keywords) {
    var tags = new ArrayList<TagDraft>();
    for (var keyword : keywords) {
      var keywordParts = keyword.split(",");
      for (var keywordPart : keywordParts) {
        var tag = StringUtils.trimToEmpty(keywordPart);
        if (tag.isBlank()) continue;
        tags.add(new TagDraft(tag));
      }
    }
    return tags;
  }
}
