/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.storyDraft.dtos.models

import java.util.*

data class StoryDraftUpdateDto(
  val label: Optional<String>?,
  val recipe: Optional<String>?,
  val content: Optional<String>?,
)
