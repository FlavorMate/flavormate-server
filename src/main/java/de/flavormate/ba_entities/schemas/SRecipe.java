/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.schemas;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.flavormate.ba_entities.schemas.helpers.SNutritionInformation;
import de.flavormate.ba_entities.schemas.helpers.SRestrictedDiet;
import de.flavormate.ba_entities.schemas.serializers.SRecipeYieldDeserializer;
import de.flavormate.ba_entities.schemas.serializers.SStepDeserializer;
import de.flavormate.ba_entities.schemas.serializers.StringListCommaDeserializer;
import de.flavormate.ba_entities.schemas.serializers.StringListDeserializer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SRecipe extends SHowTo {
  @JsonProperty("@type")
  private final String type = "Recipe";

  private Duration cookTime = Duration.ZERO;

  private String cookingMethod;

  private SNutritionInformation nutrition;

  @JsonDeserialize(using = StringListCommaDeserializer.class)
  private List<String> recipeCategory = new ArrayList<>();

  @JsonDeserialize(using = StringListCommaDeserializer.class)
  private List<String> recipeCuisine = new ArrayList<>();

  @JsonDeserialize(using = StringListDeserializer.class)
  private List<String> recipeIngredient = new ArrayList<>();

  @JsonDeserialize(using = SStepDeserializer.class)
  private List<String> recipeInstructions = new ArrayList<>();

  @JsonDeserialize(using = SRecipeYieldDeserializer.class)
  private String recipeYield;

  private SRestrictedDiet suitableForDiet;
}
