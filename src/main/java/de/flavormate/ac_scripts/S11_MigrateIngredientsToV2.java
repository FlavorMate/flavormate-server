package de.flavormate.ac_scripts;

import de.flavormate.aa_interfaces.models.CSVLog;
import de.flavormate.aa_interfaces.scripts.AScript;
import de.flavormate.ad_configurations.flavormate.CommonConfig;
import de.flavormate.ad_configurations.flavormate.PathsConfig;
import de.flavormate.ba_entities.ingredient.model.Ingredient;
import de.flavormate.ba_entities.ingredient.repository.IngredientRepository;
import de.flavormate.ba_entities.recipe.repository.RecipeRepository;
import de.flavormate.ba_entities.unit.model.Unit;
import de.flavormate.ba_entities.unit.repository.LocalizedUnitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class S11_MigrateIngredientsToV2 implements AScript {

	private final CSVLog csv = new CSVLog(
			"Migration_Unit_to_V2",
			List.of("status", "recipe_label", "recipe_id", "ingredient_id", "unit_label")
	);


	private final PathsConfig pathsConfig;

	private final CommonConfig commonConfig;


	private final IngredientRepository ingredientRepository;

	private final LocalizedUnitRepository localizedUnitRepository;

	private final RecipeRepository recipeRepository;


	/**
	 * The run method is responsible for migrating ingredient data from schema version 1 to schema version 2.
	 * The migration process involves updating the unit
	 */
	@Override
	public void run() throws Exception {
		if (!commonConfig.version().sameMajor(2)) return;


		if (ingredientRepository.countBySchema(1) == 0) {
			log.info("No migration needed");
			return;
		}

		try {
			int successCounter = 0;
			var oldIngredients = ingredientRepository.findBySchema(1);

			for (var ingredient : oldIngredients) {
				var label = Optional.of(ingredient).map(Ingredient::getUnit).map(Unit::getLabel).orElse(null);

				// If unit is set, try to find the new localized unit
				if (label != null) {
					var possibleUnits = localizedUnitRepository.findByLabel(label);
					if (possibleUnits.isEmpty()) {
						var recipe = recipeRepository.findByIngredient(ingredient.getId());
						csv.addRow(List.of("error", recipe.getLabel(), recipe.getId().toString(), ingredient.getId().toString(), label));
						continue;
					} else {

						// get the localized units that match the preferred language
						var possibleUnitsLocalized = possibleUnits.stream().filter(u -> u.getLanguage().equals(commonConfig.preferredLanguage())).toList();

						// if there are multiple units, try to find the one that matches the label
						if (possibleUnitsLocalized.size() > 1) {
							// if the amount is 1, try to find the singular unit
							if (ingredient.getAmount() == 1) {
								var possibleUnitLocalized = possibleUnitsLocalized.stream().filter(u -> u.getLabelSg().equals(label)).findFirst();
								// if the singular unit is found, use it
								if (possibleUnitLocalized.isPresent()) {
									ingredient.setUnitLocalized(possibleUnitLocalized.get());
								} else {
									// if the singular unit is not found, use the first unit
									var recipe = recipeRepository.findByIngredient(ingredient.getId());
									csv.addRow(List.of("warning", recipe.getLabel(), recipe.getId().toString(), ingredient.getId().toString(), possibleUnitsLocalized.getFirst().getLabelSg()));
									ingredient.setUnitLocalized(possibleUnitsLocalized.getFirst());
								}
							} else {
								// if the amount is not 1, try to find the plural unit
								var possibleUnitLocalized = possibleUnitsLocalized.stream().filter(u -> u.getLabelPl().equals(label)).findFirst();

								// if the plural unit is found, use it
								if (possibleUnitLocalized.isPresent()) {
									ingredient.setUnitLocalized(possibleUnitLocalized.get());
								} else {
									// if the plural unit is not found, use the first unit
									var recipe = recipeRepository.findByIngredient(ingredient.getId());
									csv.addRow(List.of("warning", recipe.getLabel(), recipe.getId().toString(), ingredient.getId().toString(), possibleUnitsLocalized.getFirst().getLabelPl()));
									ingredient.setUnitLocalized(possibleUnitsLocalized.getFirst());
								}
							}
						} else {
							// if there is only one unit, use it
							ingredient.setUnitLocalized(possibleUnits.getFirst());
						}

						ingredient.setUnit(null);
					}
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
					log.error("Not all ingredients could be migrated and the migration log could not be written");
				}

			log.info("Migrated {}/{} successfully", successCounter, oldIngredients.size());
		} catch (Exception e) {
			log.error("Migration failed");
			throw e;
		}
	}
}


