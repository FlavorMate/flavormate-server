/* Licensed under AGPLv3 2024 */
package de.flavormate.ac_scripts;

import de.flavormate.aa_interfaces.models.CSVLog;
import de.flavormate.aa_interfaces.scripts.AScript;
import de.flavormate.ad_configurations.flavormate.CommonConfig;
import de.flavormate.ad_configurations.flavormate.PathsConfig;
import de.flavormate.ba_entities.ingredient.model.Ingredient;
import de.flavormate.ba_entities.ingredient.repository.IngredientRepository;
import de.flavormate.ba_entities.recipe.repository.RecipeRepository;
import de.flavormate.ba_entities.unit.model.Unit;
import de.flavormate.ba_entities.unit.model.UnitLocalized;
import de.flavormate.ba_entities.unit.repository.UnitLocalizedRepository;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class S11_MigrateIngredientsToV2 implements AScript {

  private final CSVLog csv =
      new CSVLog(
          "Migration_Unit_to_V2",
          List.of("status", "desc", "recipe_label", "recipe_id", "ingredient_id", "unit_label"));

  private final PathsConfig pathsConfig;

  private final CommonConfig commonConfig;

  private final IngredientRepository ingredientRepository;

  private final UnitLocalizedRepository unitLocalizedRepository;

  private final RecipeRepository recipeRepository;

  /**
   * The run method is responsible for migrating ingredient data from schema version 1 to schema
   * version 2. The migration process involves updating the unit
   */
  @Override
  public void run() throws Exception {
    if (!commonConfig.getVersion().sameMajor(2)) return;

    if (ingredientRepository.countBySchema(1) == 0) {
      log.info("No migration needed");
      return;
    }

    try {
      int successCounter = 0;
      var oldIngredients = ingredientRepository.findBySchema(1);

      for (var ingredient : oldIngredients) {
        var label =
            Optional.of(ingredient).map(Ingredient::getUnit).map(Unit::getLabel).orElse(null);

        // If unit is set, try to find the new localized unit
        if (label != null) {

          var possibleUnits = unitLocalizedRepository.findByLabel(label);

          // no units were found - error
          if (possibleUnits.isEmpty()) {
            var recipe = recipeRepository.findByIngredient(ingredient.getId());
            csv.addRow(
                List.of(
                    "error",
                    "no possible units found",
                    recipe.getLabel(),
                    recipe.getId().toString(),
                    ingredient.getId().toString(),
                    label));
            continue;
          }

          // get the localized units that match the preferred language
          var possibleUnitsLocalized =
              possibleUnits.stream()
                  .filter(u -> u.getLanguage().equals(commonConfig.getPreferredLanguage()))
                  .toList();

          if (possibleUnitsLocalized.isEmpty()) {
            var recipe = recipeRepository.findByIngredient(ingredient.getId());
            csv.addRow(
                List.of(
                    "warning",
                    "unit may not be correct",
                    recipe.getLabel(),
                    recipe.getId().toString(),
                    ingredient.getId().toString(),
                    label));

            ingredient.setUnitLocalized(possibleUnits.getFirst());
            ingredient.setUnit(null);
            continue;
          }

          if (possibleUnitsLocalized.size() == 1) {
            ingredient.setUnitLocalized(possibleUnitsLocalized.getFirst());
            ingredient.setUnit(null);
            continue;
          }

          Optional<UnitLocalized> possibleUnitLocalized;

          // multiple possible units are available
          // if the amount is 1, try to find the singular unit
          if (ingredient.getAmount() == 1) {
            possibleUnitLocalized =
                possibleUnitsLocalized.stream()
                    .filter(
                        u ->
                            u.getLabelSg().equals(label)
                                || Optional.ofNullable(u.getLabelSgAbrv()).orElse("").equals(label))
                    .findFirst();
          } else {
            // if the amount is not 1, try to find the plural unit
            possibleUnitLocalized =
                possibleUnitsLocalized.stream()
                    .filter(
                        u ->
                            Optional.ofNullable(u.getLabelPl()).orElse("").equals(label)
                                || Optional.ofNullable(u.getLabelPlAbrv()).orElse("").equals(label)
                                || Optional.ofNullable(u.getLabelSgAbrv()).orElse("").equals(label))
                    .findFirst();
          }

          if (possibleUnitLocalized.isPresent()) {
            ingredient.setUnitLocalized(possibleUnitLocalized.get());
          } else {
            var recipe = recipeRepository.findByIngredient(ingredient.getId());
            csv.addRow(
                List.of(
                    "warning",
                    "unit may not be correct",
                    recipe.getLabel(),
                    recipe.getId().toString(),
                    ingredient.getId().toString(),
                    possibleUnitsLocalized.getFirst().getLabelSg()));

            ingredient.setUnitLocalized(possibleUnitsLocalized.getFirst());
          }

          ingredient.setUnit(null);
        }

        // Updating schema to 2
        ingredient.setSchema(2);
        successCounter++;
        ingredientRepository.save(ingredient);
      }

      if (csv.hasContent())
        if (csv.writeFile(Paths.get(pathsConfig.logs().getPath(), "migration"))) {
          log.error("Not all ingredients could be migrated. Please see the migration log");
        } else {
          log.error(
              "Not all ingredients could be migrated and the migration log could not be written");
        }

      log.info("Migrated {}/{} successfully", successCounter, oldIngredients.size());
    } catch (Exception e) {
      log.error("Migration failed");
      throw e;
    }
  }
}
