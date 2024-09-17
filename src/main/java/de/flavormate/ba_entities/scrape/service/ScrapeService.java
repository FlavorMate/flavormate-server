package de.flavormate.ba_entities.scrape.service;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.aa_interfaces.models.ManualBaseEntity;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ba_entities.category.repository.CategoryRepository;
import de.flavormate.ba_entities.ingredient.wrapper.IngredientDraft;
import de.flavormate.ba_entities.ingredientGroup.wrapper.IngredientGroupDraft;
import de.flavormate.ba_entities.instruction.wrapper.InstructionDraft;
import de.flavormate.ba_entities.instructionGroup.wrapper.InstructionGroupDraft;
import de.flavormate.ba_entities.recipe.wrapper.RecipeDraft;
import de.flavormate.ba_entities.recipe.wrapper.ScrapeResponse;
import de.flavormate.ba_entities.scrape.model.ScrapeResult;
import de.flavormate.ba_entities.serving.wrapper.ServingDraft;
import de.flavormate.ba_entities.tag.wrapper.TagDraft;
import de.flavormate.ba_entities.unit.model.Unit;
import de.flavormate.ba_entities.unit.repository.UnitRepository;
import de.flavormate.utils.JSONUtils;
import de.flavormate.utils.NumberUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Service
public class ScrapeService {

	private final CategoryRepository categoryRepository;
	private final UnitRepository unitRepository;

	public ScrapeService(CategoryRepository categoryRepository, UnitRepository unitRepository) {
		this.categoryRepository = categoryRepository;
		this.unitRepository = unitRepository;
	}

	public ScrapeResponse fetchAndParseRecipe(String url) throws IOException, CustomException {
		var scrapeResult = fetch(url);

		var categories = getCategories(scrapeResult.getRecipeCategory());
		var ingredientGroups = getIngredientGroups(ScrapeResult.extractIngredients(scrapeResult.getRecipeIngredient()));
		var instructionGroups = getInstructionGroups(ScrapeResult.extractInstructions(scrapeResult.getRecipeInstructions()));
		var serving = getServiceDraft(scrapeResult.getRecipeYield());
		var tags = getTags(scrapeResult.getKeywords());
		var cookTime = getDuration(scrapeResult.getCookTime());
		var prepTime = getDuration(scrapeResult.getPrepTime());
		var description = StringUtils.trimToNull(scrapeResult.getDescription());
		var label = StringUtils.trimToNull(scrapeResult.getName());

		var files = getFiles(scrapeResult.getImage());

		var recipe = new RecipeDraft(categories, ingredientGroups, instructionGroups, serving, tags, cookTime, prepTime, Duration.ZERO, null, null, description, label, url, null);


		return new ScrapeResponse(recipe, files);
	}

	private List<String> getFiles(List<String> images) {
		var files = new ArrayList<String>();

		var imageSelection = images.subList(0, Math.min(images.size(), 3));

		for (var imageUrl : imageSelection) {
			String base64;

			// fetch image
			try (InputStream in = URI.create(imageUrl).toURL().openStream()) {
				byte[] bytes = IOUtils.toByteArray(in);
				base64 = Base64.getEncoder().encodeToString(bytes);
			} catch (Exception e) {
				continue;
			}

			files.add(base64);
		}

		return files;
	}


	private ScrapeResult fetch(String url) throws IOException, CustomException {
		// Fetch the HTML page using JSoup
		Document document = Jsoup.connect(url).get();

		// Extract all <script> tags containing ld+json data
		Elements jsonLdElements = document.select("script[type=application/ld+json]");

		// Loop through each script tag and check for Recipe objects
		for (Element jsonLdElement : jsonLdElements) {
			String json = jsonLdElement.html();

			// Clean the String to be properly parsed
			json = json.replaceAll("\\n+", "");
			json = json.replaceAll("\\r+", "");
			json = json.replaceAll("\\t", " ");
			json = json.replaceAll("\\s+", " ");
			json = StringEscapeUtils.unescapeHtml4(json);

			// Parse the JSON into a generic JsonNode first
			JsonNode rootNode = JSONUtils.mapper.readTree(json);

			// Handle cases where the JSON-LD contains an array of objects
			if (rootNode.isArray()) {
				for (JsonNode node : rootNode) {
					if (isRecipeNode(node)) {
						// Parse the node into a Recipe object and add to the list
						return JSONUtils.mapper.treeToValue(node, ScrapeResult.class);
					}
				}
			} else if (isRecipeNode(rootNode)) {
				// If it's a single object and a recipe, parse and add it to the list
				return JSONUtils.mapper.treeToValue(rootNode, ScrapeResult.class);
			}
		}
		throw new NotFoundException(ScrapeResult.class);
	}

	// Helper method to check if a JsonNode is a Recipe
	private boolean isRecipeNode(JsonNode node) {
		JsonNode typeNode = node.get("@type");
		return typeNode != null && "Recipe".equals(typeNode.asText());
	}

	private List<Long> getCategories(List<String> recipeCategory) {
		return recipeCategory.stream().map((category) -> categoryRepository.findByLabel(category).map(ManualBaseEntity::getId).orElse(null)).filter(Objects::nonNull).toList();
	}

	private List<IngredientGroupDraft> getIngredientGroups(List<String> recipeIngredient) {
		ArrayList<IngredientDraft> ingredients = new ArrayList<>();

		for (var ingredient : recipeIngredient) {
			// Convert 1/4 into 0.25
			ingredient = NumberUtils.convertExtendedFractionString(ingredient);
			ingredient = StringUtils.trimToEmpty(ingredient);

			var ingredientParts = ingredient.split(" ");

			double amount = NumberUtils.tryParseDouble(ingredientParts[0], -1);
			String label;
			Unit unit;

			int startIndex;

			// The ingredient has no amount
			if (amount <= 0) {
				unit = unitRepository.findByLabel(ingredientParts[0]).orElse(null);
				if (unit == null) {
					startIndex = 0;
				} else {
					startIndex = 1;
				}
			} else {
				unit = unitRepository.findByLabel(ingredientParts[1]).orElse(null);
				if (unit == null) {
					startIndex = 1;
				} else {
					startIndex = 2;
				}
			}
			var labelParts = List.of(ingredientParts).subList(startIndex, ingredientParts.length);
			label = String.join(" ", labelParts);

			ingredients.add(new IngredientDraft(amount, unit, label));
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
		var amount = NumberUtils.tryParseDouble(recipeYield, -1);
		return new ServingDraft(amount, "");
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

	private Duration getDuration(String time) {
		if (StringUtils.isBlank(time)) return Duration.ZERO;
		try {
			return Duration.parse(time);
		} catch (Exception e) {
			return Duration.ZERO;
		}
	}
}


