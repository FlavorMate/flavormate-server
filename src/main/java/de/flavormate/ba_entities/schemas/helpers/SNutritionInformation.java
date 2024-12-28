/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.schemas.helpers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SNutritionInformation(
    String calories,
    String carbohydrateContent,
    String cholesterolContent,
    String fatContent,
    String fiberContent,
    String proteinContent,
    String saturatedFatContent,
    String servingSize,
    String sodiumContent,
    String sugarContent,
    String transFatContent,
    String unsaturatedFatContent) {

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(
        this, ToStringStyle.JSON_STYLE, false, false, true, null);
  }
}
