/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.story.wrapper;

import de.flavormate.ba_entities.recipe.model.Recipe;

public record StoryDraft(String label, String content, Recipe recipe) {}
