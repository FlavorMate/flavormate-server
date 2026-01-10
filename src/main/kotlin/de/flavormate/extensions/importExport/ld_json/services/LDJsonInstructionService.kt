/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.ld_json.services

import de.flavormate.extensions.importExport.ld_json.models.types.step.LDJsonHowToSection
import de.flavormate.extensions.importExport.ld_json.models.types.step.LDJsonHowToStep
import de.flavormate.extensions.importExport.ld_json.models.types.step.LDJsonStep
import de.flavormate.features.recipeDraft.daos.models.RecipeDraftEntity
import de.flavormate.features.recipeDraft.daos.models.instructions.RecipeDraftInstructionGroupEntity
import de.flavormate.features.recipeDraft.daos.models.instructions.RecipeDraftInstructionGroupItemEntity
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class LDJsonInstructionService {

  /**
   * Maps a list of LDJsonStep objects into a set of [RecipeDraftInstructionGroupEntity] objects.
   * The mapping logic considers whether the input steps contain groups of instructions or represent
   * a single or multiple instruction groups.
   *
   * @param input A list of [LDJsonStep] objects that represent the input steps to be mapped.
   * @return A set of [RecipeDraftInstructionGroupEntity] objects created from the input steps.
   */
  fun mapInstructionGroupDrafts(
    input: List<LDJsonStep>,
    recipe: RecipeDraftEntity,
  ): MutableList<RecipeDraftInstructionGroupEntity> {
    val groups =
      when {
        !containsGroups(input) -> createSingleGroup(input)
        hasSingleInstructionGroups(input) -> createFlattenedGroup(flattenGroups(input))
        else -> createMultipleGroups(flattenGroups(input))
      }

    groups.forEach { it.recipe = recipe }

    return groups
  }

  /**
   * Checks if the given list of [LDJsonStep] contains any elements of type [LDJsonHowToSection].
   *
   * @param input A list of [LDJsonStep] objects to be checked.
   * @return True if the list contains at least one [LDJsonHowToSection]; otherwise, false.
   */
  private fun containsGroups(input: List<LDJsonStep>): Boolean =
    input.any { it is LDJsonHowToSection }

  /**
   * Checks whether the provided list of [LDJsonStep] contains any objects that represent single
   * instruction groups. This is determined by verifying if the element is either an
   * [LDJsonHowToStep] or an [LDJsonHowToSection] containing at most one instructional element.
   *
   * @param input A list of [LDJsonStep] objects to evaluate. This may include instances of
   *   [LDJsonHowToStep] and [LDJsonHowToSection].
   * @return True if any element in the list qualifies as a single instruction group; false
   *   otherwise.
   */
  private fun hasSingleInstructionGroups(input: List<LDJsonStep>): Boolean =
    input.any {
      it is LDJsonHowToStep || ((it as? LDJsonHowToSection)?.itemListElement?.size ?: 0) <= 1
    }

  /**
   * Flattens a list of [LDJsonStep] objects by checking if any of the steps are of type
   * [LDJsonHowToSection]. If an element is a [LDJsonHowToSection], it creates a new section with a
   * flattened list of its item elements. Otherwise, the Original step is retained.
   *
   * @param input A list of [LDJsonStep] objects to be flattened. This list may contain instances of
   *   [LDJsonHowToSection] that themselves hold nested [LDJsonStep] objects.
   * @return A new list of [LDJsonStep] objects where any [LDJsonHowToSection] instances have been
   *   replaced with a flattened version, while other step types are kept unchanged.
   */
  private fun flattenGroups(input: List<LDJsonStep>): List<LDJsonStep> =
    input.map { step ->
      when (step) {
        is LDJsonHowToSection -> LDJsonHowToSection(step.name, step.position, step.flatten())
        else -> step
      }
    }

  /**
   * Creates a single instruction group from a list of steps. The resulting group contains all steps
   * as mapped instructions.
   *
   * @param steps A list of [LDJsonStep] objects to be converted into a single instruction group.
   * @return A set containing one [RecipeDraftInstructionGroupEntity] with the mapped instructions.
   */
  private fun createSingleGroup(
    steps: List<LDJsonStep>
  ): MutableList<RecipeDraftInstructionGroupEntity> =
    RecipeDraftInstructionGroupEntity()
      .apply {
        instructions =
          steps.mapTo(mutableListOf()) { mapInstructionDraft(it as LDJsonHowToStep, this) }
      }
      .let { mutableListOf(it) }

  /**
   * Creates a flattened instruction group from the provided list of steps. If the steps contain any
   * groups (e.g., instances of [LDJsonHowToSection]), their nested steps are flattened into
   * individual instructions. All steps are then mapped and combined into a single instruction
   * group.
   *
   * @param steps A list of [LDJsonStep] objects representing the steps to be flattened and grouped
   *   into a single instruction group.
   * @return A set containing one [RecipeDraftInstructionGroupEntity] with the flattened and mapped
   *   instructions derived from the provided steps.
   */
  private fun createFlattenedGroup(
    steps: List<LDJsonStep>
  ): MutableList<RecipeDraftInstructionGroupEntity> =
    RecipeDraftInstructionGroupEntity()
      .apply {
        instructions =
          steps.flatMapTo(mutableListOf()) { step ->
            when (step) {
              is LDJsonHowToSection ->
                step.itemListElement.map { mapInstructionDraft(it as LDJsonHowToStep, this) }

              else -> listOf(mapInstructionDraft(step as LDJsonHowToStep, this))
            }
          }
      }
      .let { mutableListOf(it) }

  /**
   * Creates multiple instruction groups from a list of steps. Each group corresponds to a
   * [LDJsonHowToSection] within the input steps, with its name used as the group label and its
   * elements mapped into instructions.
   *
   * @param steps A list of [LDJsonStep] objects, which only includes [LDJsonHowToSection] instances
   *   that represent groups of instructions.
   * @return A set of [RecipeDraftInstructionGroupEntity] objects, where each entity represents a
   *   group of instructions derived from the input steps.
   */
  private fun createMultipleGroups(
    steps: List<LDJsonStep>
  ): MutableList<RecipeDraftInstructionGroupEntity> =
    steps.mapNotNullTo(mutableListOf()) { step ->
      (step as? LDJsonHowToSection)?.let { section ->
        RecipeDraftInstructionGroupEntity().apply {
          label = section.name
          instructions =
            section.itemListElement.mapTo(mutableListOf()) {
              mapInstructionDraft(it as LDJsonHowToStep, this)
            }
        }
      }
    }

  /**
   * Maps an [LDJsonHowToStep] object to an [RecipeDraftInstructionGroupItemEntity]. The mapping
   * sets the `label` of the resulting entity to the `text` of the input step.
   *
   * @param input The [LDJsonHowToStep] object to be mapped.
   * @return An [RecipeDraftInstructionGroupItemEntity] with its `label` property set to the `text`
   *   of the input.
   */
  private fun mapInstructionDraft(
    input: LDJsonHowToStep,
    group: RecipeDraftInstructionGroupEntity,
  ): RecipeDraftInstructionGroupItemEntity {
    return RecipeDraftInstructionGroupItemEntity().apply {
      this.label = input.text
      this.group = group
    }
  }
}
