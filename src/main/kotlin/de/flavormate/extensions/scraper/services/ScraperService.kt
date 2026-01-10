/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.scraper.services

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.treeToValue
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequestBlocking
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.select.Elements
import de.flavormate.configuration.jackson.CustomObjectMapper
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.extensions.importExport.ld_json.models.LDJsonRecipe
import de.flavormate.extensions.importExport.ld_json.services.LDJsonService
import de.flavormate.shared.extensions.stripHTMLTags
import de.flavormate.shared.services.AuthorizationDetails
import de.flavormate.utils.URLUtils
import jakarta.enterprise.context.RequestScoped

@RequestScoped
class ScraperService(
  val authorizationDetails: AuthorizationDetails,
  val ldJsonService: LDJsonService,
) {
  private val mapper = CustomObjectMapper.instance

  fun scrape(url: String): String {
    val cleanedUrl = URLUtils.cleanURL(url)
    val html = fetchHTML(cleanedUrl)

    val langAttr = html.selectFirst("html[lang]")?.attr("lang") ?: "en"
    val language =
      when {
        langAttr.startsWith("de", ignoreCase = true) -> "de"
        langAttr.startsWith("en", ignoreCase = true) -> "en"
        else -> "en"
      }

    val ldJson = processHTML(html, cleanedUrl)
    return ldJsonService.ldJsonRecipeToRecipeDraftEntity(ldJson, language).id
  }

  private fun fetchHTML(url: String) = Ksoup.parseGetRequestBlocking(url)

  fun processHTML(html: Document, url: String): LDJsonRecipe {
    // Extract all <script> tags containing ld+json data
    val jsonLdElements: Elements = html.select("script[type=application/ld+json]")

    // Loop through each script tag and check for Recipe objects
    for (jsonLdElement in jsonLdElements) {
      var json: String = jsonLdElement.html()

      json = json.stripHTMLTags()

      // Parse the JSON into a generic JsonNode first
      val rootNode = mapper.readTree(json)

      val recipeNode: JsonNode? = findRecipeNode(rootNode)

      if (recipeNode != null) {
        return mapper.treeToValue<LDJsonRecipe>(recipeNode).apply { this.url = url }
      }
    }
    throw FNotFoundException(message = "Not Found")
  }

  private fun findRecipeNode(node: JsonNode): JsonNode? {
    // Base case: Check if the current node is a Recipe node
    if (isRecipeNode(node)) {
      return node
    }

    // Recursive case: Check nested objects and arrays
    if (node.isObject) {
      // Check all fields of the object
      for (child in node) {
        val found = findRecipeNode(child)
        if (found != null) {
          return found
        }
      }
    } else if (node.isArray) {
      // Check each element in the array
      for (child in node) {
        val found = findRecipeNode(child)
        if (found != null) {
          return found
        }
      }
    }

    // Return null if no Recipe node is found
    return null
  }

  // Helper method to check if a JsonNode is a Recipe
  private fun isRecipeNode(node: JsonNode): Boolean {
    return node.path("@type").asText().equals("recipe", ignoreCase = true)
  }
}
