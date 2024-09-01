package de.flavormate.ba_entities.recipe.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.flavormate.ba_entities.ingredientGroup.wrapper.IngredientGroupDraft;
import de.flavormate.ba_entities.instructionGroup.wrapper.InstructionGroupDraft;
import de.flavormate.ba_entities.recipe.enums.RecipeCourse;
import de.flavormate.ba_entities.recipe.enums.RecipeDiet;
import de.flavormate.ba_entities.serving.wrapper.ServingDraft;
import de.flavormate.ba_entities.tag.wrapper.TagDraft;

import java.time.Duration;
import java.util.List;

@JsonIgnoreProperties({"id"})
public record RecipeDraft(List<Long> categories, List<IngredientGroupDraft> ingredientGroups,
                          List<InstructionGroupDraft> instructionGroups, ServingDraft serving, List<TagDraft> tags,
                          Duration cookTime, Duration prepTime, Duration restTime, RecipeCourse course,
                          RecipeDiet diet, String description, String label, String url, List<Long> files) {

}
