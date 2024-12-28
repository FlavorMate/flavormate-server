/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.schemas;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.flavormate.ba_entities.schemas.helpers.SNutritionInformation;
import de.flavormate.ba_entities.schemas.helpers.SRestrictedDiet;
import de.flavormate.ba_entities.schemas.serializers.SRecipeYieldDeserializer;
import de.flavormate.ba_entities.schemas.serializers.SStepDeserializer;
import de.flavormate.ba_entities.schemas.serializers.StringListDeserializer;
import java.time.Duration;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SRecipe extends SHowTo {
  private Duration cookTime;

  private String cookingMethod;

  private SNutritionInformation nutrition;

  @JsonDeserialize(using = StringListDeserializer.class)
  private List<String> recipeCategory;

  @JsonDeserialize(using = StringListDeserializer.class)
  private List<String> recipeCuisine;

  @JsonDeserialize(using = StringListDeserializer.class)
  private List<String> recipeIngredient;

  @JsonDeserialize(using = SStepDeserializer.class)
  private List<String> recipeInstructions;

  @JsonDeserialize(using = SRecipeYieldDeserializer.class)
  private String recipeYield;

  private SRestrictedDiet suitableForDiet;

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
