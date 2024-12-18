/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.recipe.wrapper;

import java.util.List;

public record ScrapeResponse(RecipeDraft recipe, List<String> images) {}
