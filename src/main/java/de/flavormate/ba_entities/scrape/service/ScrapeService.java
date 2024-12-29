/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.scrape.service;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ba_entities.recipe.wrapper.ScrapeResponse;
import de.flavormate.ba_entities.schemas.SRecipe;
import de.flavormate.ba_entities.schemas.mappers.SRecipeMapper;
import de.flavormate.utils.JSONUtils;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ScrapeService {

  private final SRecipeMapper sRecipeMapper;

  public ScrapeResponse fetchAndParseRecipe(String url) throws IOException, CustomException {
    var sRecipe = fetch(url);

    final var recipeDraft = sRecipeMapper.toRecipeDraft(sRecipe, url);

    var files = getFiles(sRecipe.getImage());

    return new ScrapeResponse(recipeDraft, files);
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

  private SRecipe fetch(String url) throws IOException, CustomException {
    // Fetch the HTML page using JSoup
    Document document = Jsoup.connect(url).get();

    // Extract all <script> tags containing ld+json data
    Elements jsonLdElements = document.select("script[type=application/ld+json]");

    // Loop through each script tag and check for Recipe objects
    for (Element jsonLdElement : jsonLdElements) {
      String json = jsonLdElement.html();

      // Clean the String to be properly parsed
      //      json = json.replaceAll("\\n+", "\n");
      //      json = json.replaceAll("\\r+", "");
      //      json = json.replaceAll("\\t", " ");
      //      json = json.replaceAll("\\s+", " ");
      //      json = StringEscapeUtils.unescapeHtml4(json);

      // Parse the JSON into a generic JsonNode first
      JsonNode rootNode = JSONUtils.mapper.readTree(json);

      // Handle cases where the JSON-LD contains an array of objects
      if (rootNode.isArray()) {
        for (JsonNode node : rootNode) {
          if (isRecipeNode(node)) {
            // Parse the node into a Recipe object and add to the list
            return JSONUtils.mapper.treeToValue(node, SRecipe.class);
          }
        }
      } else if (isRecipeNode(rootNode)) {
        // If it's a single object and a recipe, parse and add it to the list
        return JSONUtils.mapper.treeToValue(rootNode, SRecipe.class);
      }
    }
    throw new NotFoundException(SRecipe.class);
  }

  // Helper method to check if a JsonNode is a Recipe
  private boolean isRecipeNode(JsonNode node) {
    return node.path("@type").asText().equals("Recipe");
  }
}
