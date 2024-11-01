/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.scrape.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.flavormate.ad_configurations.jackson.ImageDeserializer;
import de.flavormate.ad_configurations.jackson.ListOrStringDeserializer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ScrapeResult {
  private String name;
  private String description;

  @JsonDeserialize(using = ImageDeserializer.class)
  private List<String> image = new ArrayList<>();

  private String recipeYield;

  @JsonDeserialize(using = ListOrStringDeserializer.class)
  private List<Object> recipeIngredient = new ArrayList<>(); // Can be a list of strings or objects

  @JsonDeserialize(using = ListOrStringDeserializer.class)
  private List<Object> recipeInstructions = new ArrayList<>(); // Can be list of steps or sections

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

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    additionalProperties.put(name, value);
  }
}

// Polymorphic handling of instructions (HowToStep or HowToSection)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = HowToStep.class, name = "HowToStep"),
  @JsonSubTypes.Type(value = HowToSection.class, name = "HowToSection")
})
@Getter
@Setter
class ScrapeInstruction {
  private String name;
  private String text;
  private List<ScrapeInstruction>
      itemListElement; // If it's a section, it may contain multiple steps
}

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
class HowToStep extends ScrapeInstruction {
  private String url;
  private String image;
}

@JsonIgnoreProperties(ignoreUnknown = true)
class HowToSection extends ScrapeInstruction {
  // HowToSection may contain multiple HowToStep objects

}
