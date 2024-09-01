package de.flavormate.ba_entities.recipe.service;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.ab_exeptions.exceptions.ConflictException;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ba_entities.category.model.Category;
import de.flavormate.ba_entities.category.repository.CategoryRepository;
import de.flavormate.ba_entities.ingredient.wrapper.IngredientDraft;
import de.flavormate.ba_entities.ingredientGroup.wrapper.IngredientGroupDraft;
import de.flavormate.ba_entities.instruction.wrapper.InstructionDraft;
import de.flavormate.ba_entities.instructionGroup.wrapper.InstructionGroupDraft;
import de.flavormate.ba_entities.recipe.wrapper.RecipeDraft;
import de.flavormate.ba_entities.recipe.wrapper.ScrapeResponse;
import de.flavormate.ba_entities.serving.wrapper.ServingDraft;
import de.flavormate.ba_entities.tag.wrapper.TagDraft;
import de.flavormate.ba_entities.unit.model.Unit;
import de.flavormate.ba_entities.unit.repository.UnitRepository;
import de.flavormate.utils.JSONUtils;
import de.flavormate.utils.NumberUtils;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ScraperService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private UnitRepository unitRepository;

	public ScrapeResponse scrape(String url) throws CustomException {
		try {
			Document doc = Jsoup.connect(url).get();

			Elements scriptTags = doc.getElementsByTag("script");

			for (Element scriptTag : scriptTags) {
				if (scriptTag.attr("type").equals("application/ld+json")
						&& scriptTag.html().contains("\"@type\": \"Recipe\"")) {

					var recipeJSON = JSONUtils.mapper.readTree(scriptTag.html());


					String name = recipeJSON.has("name") ? recipeJSON.get("name").asText() : "";

					Optional<String> image = getImage(recipeJSON);

					String description =
							recipeJSON.has("description") ? recipeJSON.get("description").asText()
									: "";

					Duration prepTime =
							recipeJSON.has("prepTime") && !recipeJSON.get("prepTime").isNull()
									? Duration.parse(recipeJSON.get("prepTime").asText())
									: Duration.ZERO;

					Duration cookTime =
							recipeJSON.has("cookTime") && !recipeJSON.get("cookTime").isNull()
									? Duration.parse(recipeJSON.get("cookTime").asText())
									: Duration.ZERO;

					Duration restTime =
							recipeJSON.has("restTime") && !recipeJSON.get("restTime").isNull()
									? Duration.parse(recipeJSON.get("restTime").asText())
									: Duration.ZERO;

					ServingDraft serving = getServing(recipeJSON);

					List<Long> categories = getCategories(recipeJSON);

					List<TagDraft> tags = getTags(recipeJSON);

					InstructionGroupDraft instructionGroup = getInstructionGroup(recipeJSON);

					IngredientGroupDraft ingredientGroup = getIngredientGroup(recipeJSON);


					var recipe = new RecipeDraft(categories, List.of(ingredientGroup),
							List.of(instructionGroup), serving, tags, cookTime, prepTime, restTime,
							null, null, description, name, url, categories);

					return new ScrapeResponse(recipe, image.orElse(null));


				}
			}

		} catch (Exception e) {
			throw new ConflictException(ScrapeResponse.class);
		}
		throw new NotFoundException(ScrapeResponse.class);
	}

	private IngredientGroupDraft getIngredientGroup(JsonNode json) {
		final String FIELD = "recipeIngredient";
		ArrayList<String> ingredients = new ArrayList<>();

		if (json.has(FIELD)) {
			// check for multiple ingredients
			if (json.get(FIELD).isArray()) {
				// check if instruction is object
				int size = json.get(FIELD).get(0).size();
				if (size < 1) {
					ingredients = new ArrayList<>(
							StreamSupport.stream(json.get(FIELD).spliterator(), false)
									.map(r -> r.asText("")).toList());
				} else {
					ingredients = new ArrayList<>(
							StreamSupport.stream(json.get(FIELD).spliterator(), false).map(r -> {
								if (r.has("??????")) {
									return r.get("?????").asText();
								} else {
									return null;
								}
							}).toList());
				}
			} else {
				ingredients =
						new ArrayList<String>(Arrays.asList(json.get(FIELD).asText().split("\n")));
			}

			ingredients = ingredients.stream().map(
							ingredient -> NumberUtils.convertExtendedFractionString(ingredient))
					.collect(Collectors.toCollection(ArrayList::new));

			var ttt = ingredients.stream().map(i -> {
				var list = new ArrayList<String>(Arrays.asList(i.split(" ")));// List.of(i.split("
				// "));//
				// .removeAll();
				list.removeAll(Arrays.asList("", null));

				String[] parts = list.toArray(new String[0]);

				double amount = NumberUtils.tryParseDouble(parts[0], -1);
				String label;
				Optional<Unit> unit;

				int startIndex = 0;

				if (amount < 0) {
					var unitList = unitRepository.findByLabelOrShortLabel(parts[0], parts[0]);
					unit = unitList.isEmpty() ? Optional.empty() : Optional.of(unitList.get(0));
					if (unit.isEmpty()) {
						unit = unitRepository.findByLabel("");
						startIndex = 0;
					} else {
						startIndex = 1;
					}
				} else {
					var unitList = unitRepository.findByLabelOrShortLabel(parts[1], parts[1]);
					unit = unitList.isEmpty() ? Optional.empty() : Optional.of(unitList.get(0));

					if (unit.isEmpty()) {
						unit = unitRepository.findByLabel("");
						startIndex = 1;
					} else {
						startIndex = 2;
					}
				}

				label = String.join(" ", List.of(parts).subList(startIndex, parts.length));

				return new IngredientDraft(amount, unit.get(), label);
			}).toList();

			return new IngredientGroupDraft("", ttt);
		} else {
			return new IngredientGroupDraft("", List.of());
		}
	}

	private InstructionGroupDraft getInstructionGroup(JsonNode json) {
		final String FIELD = "recipeInstructions";
		ArrayList<String> instructions = new ArrayList<>();

		if (json.has(FIELD)) {
			// check for multiple instructions
			if (json.get(FIELD).isArray()) {
				// check if instruction is object
				int size = json.get(FIELD).get(0).size();
				if (size < 1) {
					instructions = new ArrayList<>(
							StreamSupport.stream(json.get(FIELD).spliterator(), false)
									.map(r -> r.asText("")).toList());
				} else {
					instructions = new ArrayList<>(
							StreamSupport.stream(json.get(FIELD).spliterator(), false).map(r -> {
								if (r.has("??????")) {
									return r.get("?????").asText();
								} else {
									return null;
								}
							}).toList());
				}

			} else {
				instructions =
						new ArrayList<String>(Arrays.asList(json.get(FIELD).asText().split("\n")));
			}

			instructions.removeIf(String::isBlank);

			var ttt = instructions.stream().map(i -> new InstructionDraft(i)).toList();

			return new InstructionGroupDraft("", ttt);
		} else {
			return new InstructionGroupDraft("", List.of());
		}
	}

	private Optional<String> getImage(JsonNode json) {
		final String FIELD = "image";
		String imageUrl = null;
		try {
			if (json.has(FIELD)) {
				JsonNode imageNode = json.get(FIELD);
				if (imageNode.size() >= 1) {
					imageUrl = imageNode.get("url").asText();
				} else {
					imageUrl = imageNode.asText();
				}

				// fetch image
				try (InputStream in = URI.create(imageUrl).toURL().openStream()) {
					byte[] bytes = IOUtils.toByteArray(in);
					imageUrl = Base64.getEncoder().encodeToString(bytes);
				}
			}

			return Optional.ofNullable(imageUrl);
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	private List<Long> getCategories(JsonNode json) {
		final String FIELD = "recipeCategory";
		List<String> rawCategories;

		if (json.has(FIELD)) {
			// check for multiple categories
			if (json.get(FIELD).isArray()) {
				rawCategories = StreamSupport.stream(json.get(FIELD).spliterator(), false)
						.map(c -> c.asText()).toList();

			} else {
				rawCategories = List.of(json.get(FIELD).asText(FIELD).split(","));
			}

			return rawCategories.stream().map(c -> {
				Optional<Category> foundCategory = categoryRepository.findByLabel(c);
				if (foundCategory.isPresent())
					return foundCategory.get().getId();
				else
					return null;
			}).filter(Objects::nonNull).collect(Collectors.toList());

		} else {
			return new ArrayList<>();
		}
	}

	private List<TagDraft> getTags(JsonNode json) {
		final String FIELD = "keywords";
		List<String> rawTags;

		if (json.has(FIELD)) {
			// check for multiple tags
			if (json.get(FIELD).isArray()) {
				rawTags = StreamSupport.stream(json.get(FIELD).spliterator(), false)
						.map(c -> c.asText()).toList();

			} else {
				rawTags = List.of(json.get(FIELD).asText(FIELD).split(","));
			}

			return rawTags.stream().map(tag -> new TagDraft(tag.toLowerCase())).toList();

		} else {
			return new ArrayList<>();
		}
	}

	private ServingDraft getServing(JsonNode json) {
		final String FIELD = "recipeYield";
		String longestRecipeYield;

		if (json.has(FIELD)) {
			// check for multiple servings
			if (json.get(FIELD).isArray()) {
				longestRecipeYield = StreamSupport.stream(json.get(FIELD).spliterator(), false)
						.map(r -> r.asText()).max(Comparator.comparingInt(String::length)).get();
			} else {
				longestRecipeYield = json.get(FIELD).asText();
			}

			List<String> recipeYield =
					new ArrayList<>(Arrays.asList(longestRecipeYield.split(" ")));
			recipeYield.removeAll(Arrays.asList("", null));

			try {
				return new ServingDraft(Double.parseDouble(recipeYield.get(0)),
						String.join(" ", recipeYield.subList(1, recipeYield.size())));

			} catch (NumberFormatException e) {
				return new ServingDraft(1.0,
						String.join(" ", recipeYield.subList(0, recipeYield.size())));
			}

		} else {
			return new ServingDraft(1.0, "Portion");
		}

	}
}
