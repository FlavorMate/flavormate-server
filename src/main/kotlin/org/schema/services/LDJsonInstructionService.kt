/* Licensed under AGPLv3 2024 - 2026 */
package org.schema.services

import de.flavormate.features.recipeDraft.daos.models.RecipeDraftEntity
import de.flavormate.features.recipeDraft.daos.models.instructions.RecipeDraftInstructionGroupEntity
import de.flavormate.features.recipeDraft.daos.models.instructions.RecipeDraftInstructionGroupItemEntity
import jakarta.enterprise.context.ApplicationScoped
import org.schema.models.types.step.LDJsonHowToSection
import org.schema.models.types.step.LDJsonHowToStep
import org.schema.models.types.step.LDJsonStep

@ApplicationScoped
class LDJsonInstructionService {

  /**
   * Maps a list of LDJsonStep objects into a set of [RecipeDraftInstructionGroupEntity] objects.
   * The mapping logic considers whether the input steps contain groups of instructions or represent
   * a single or multiple instruction groups.
   *
   * @param input A list of [org.schema.models.types.step.LDJsonStep] objects that represent the
   *   input steps to be mapped.
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
   * Checks if the given list of [org.schema.models.types.step.LDJsonStep] contains any elements of
   * type [org.schema.models.types.step.LDJsonHowToSection].
   *
   * @param input A list of [org.schema.models.types.step.LDJsonStep] objects to be checked.
   * @return True if the list contains at least one
   *   [org.schema.models.types.step.LDJsonHowToSection]; otherwise, false.
   */
  private fun containsGroups(input: List<LDJsonStep>): Boolean =
    input.any { it is LDJsonHowToSection }

  /**
   * Checks whether the provided list of [org.schema.models.types.step.LDJsonStep] contains any
   * objects that represent single instruction groups. This is determined by verifying if the
   * element is either an [org.schema.models.types.step.LDJsonHowToStep] or an
   * [org.schema.models.types.step.LDJsonHowToSection] containing at most one instructional element.
   *
   * @param input A list of [org.schema.models.types.step.LDJsonStep] objects to evaluate. This may
   *   include instances of [org.schema.models.types.step.LDJsonHowToStep] and
   *   [org.schema.models.types.step.LDJsonHowToSection].
   * @return True if any element in the list qualifies as a single instruction group; false
   *   otherwise.
   */
  private fun hasSingleInstructionGroups(input: List<LDJsonStep>): Boolean =
    input.any {
      it is LDJsonHowToStep || ((it as? LDJsonHowToSection)?.itemListElement?.size ?: 0) <= 1
    }

  /**
   * Flattens a list of [org.schema.models.types.step.LDJsonStep] objects by checking if any of the
   * steps are of type [org.schema.models.types.step.LDJsonHowToSection]. If an element is a
   * [org.schema.models.types.step.LDJsonHowToSection], it creates a new section with a flattened
   * list of its item elements. Otherwise, the Original step is retained.
   *
   * @param input A list of [org.schema.models.types.step.LDJsonStep] objects to be flattened. This
   *   list may contain instances of [org.schema.models.types.step.LDJsonHowToSection] that
   *   themselves hold nested [org.schema.models.types.step.LDJsonStep] objects.
   * @return A new list of [org.schema.models.types.step.LDJsonStep] objects where any
   *   [org.schema.models.types.step.LDJsonHowToSection] instances have been replaced with a
   *   flattened version, while other step types are kept unchanged.
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
   * @param steps A list of [org.schema.models.types.step.LDJsonStep] objects to be converted into a
   *   single instruction group.
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
   * groups (e.g., instances of [org.schema.models.types.step.LDJsonHowToSection]), their nested
   * steps are flattened into individual instructions. All steps are then mapped and combined into a
   * single instruction group.
   *
   * @param steps A list of [org.schema.models.types.step.LDJsonStep] objects representing the steps
   *   to be flattened and grouped into a single instruction group.
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
   * [org.schema.models.types.step.LDJsonHowToSection] within the input steps, with its name used as
   * the group label and its elements mapped into instructions.
   *
   * @param steps A list of [org.schema.models.types.step.LDJsonStep] objects, which only includes
   *   [org.schema.models.types.step.LDJsonHowToSection] instances that represent groups of
   *   instructions.
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
   * Maps an [org.schema.models.types.step.LDJsonHowToStep] object to an
   * [RecipeDraftInstructionGroupItemEntity]. The mapping sets the `label` of the resulting entity
   * to the `text` of the input step.
   *
   * @param input The [org.schema.models.types.step.LDJsonHowToStep] object to be mapped.
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
