package de.flavormate.ac_scripts;

import de.flavormate.aa_interfaces.scripts.AScript;
import de.flavormate.ba_entities.highlight.model.Highlight;
import de.flavormate.ba_entities.highlight.repository.HighlightRepository;
import de.flavormate.ba_entities.recipe.enums.RecipeDiet;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.recipe.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class S21_HighlightGeneratorScript extends AScript {
	@Autowired
	Environment env;

	@Autowired
	private RecipeRepository recipeRepository;

	@Autowired
	private HighlightRepository repository;

	public S21_HighlightGeneratorScript() {
		super("Highlight Generator");
	}

	@Override
	public void run() {
		log("Starting highlight generation");
		for (var diet : RecipeDiet.values()) {
			generateHighlightsByDiet(diet);
		}
	}

	private void generateHighlightsByDiet(RecipeDiet diet) {
		final var now = LocalDate.now();
		long PAST_DAYS = 14;
		var CHECK_PAST_DAYS = PAST_DAYS;

		var existingRecipes = recipeRepository.countByDietIn(RecipeDiet.getFilter(diet));

		if (existingRecipes == 0) {
			log("No recipes for {} available. Skipping!", diet.toString());
			return;
		}
		if (existingRecipes <= PAST_DAYS) {
			PAST_DAYS = existingRecipes;
		}
		if (existingRecipes <= CHECK_PAST_DAYS) {
			CHECK_PAST_DAYS = existingRecipes - 1;
		}

		var existingHighlights = repository.countByDiet(diet);
		var highlights = repository.findAllByDiet(diet);

		// init database if empty
		if (existingHighlights == 0) {
			log("Generate all highlights...");
			insertHighlights(PAST_DAYS - 1, 0, highlights.size() - CHECK_PAST_DAYS, now, highlights,
					diet);
		}

		// check if recipe of the day is up to date
		var latestHighlight = repository.findAllByDiet(diet,
				PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "date"))).getContent().get(0);

		// get number of recipes to be created, range between 0 and PAST_DAYS
		var difference = Math.clamp(ChronoUnit.DAYS.between(latestHighlight.getDate(), now), 0, PAST_DAYS);

		// insert missing recipes
		if (difference > 0) {
			log("Generate {} missing highlights", difference);
			insertHighlights(difference - 1, 0, highlights.size() - CHECK_PAST_DAYS, now,
					highlights, diet);

			log("Generated highlights for {}.", diet.toString());
		} else {
			log("No new highlights for {} needed. Skipping!", diet.toString());
		}
	}

	private Boolean insertHighlights(long start, long end, long border, LocalDate now,
	                                 List<Highlight> highlights, RecipeDiet diet) {
		for (var i = start; i >= end; i--) {
			var date = now.minusDays(i);
			boolean insert;
			Recipe recipe;

			do {
				insert = true;
				recipe = recipeRepository.findRandomRecipeByDiet(RecipeDiet.getFilterNames(diet), "%", 1)
						.getFirst();

				for (var j = highlights.size() - 1; !highlights.isEmpty() && j >= border && j >= 0; j--) {
					var highlight = highlights.get(j);
					if (highlight.getRecipe().getId().equals(recipe.getId())) {
						insert = false;
						break;
					}
				}

				if (insert) {
					var highlight = Highlight.builder().diet(diet).recipe(recipe).date(date).build();
					repository.save(highlight);
					log("Inserted {} for {} ({})", recipe.getLabel(), date.toString(),
							diet.toString());
				}

			} while (!insert);
		}
		return true;
	}

}
