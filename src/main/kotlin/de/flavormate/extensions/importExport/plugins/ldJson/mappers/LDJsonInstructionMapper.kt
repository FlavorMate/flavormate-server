/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.plugins.ldJson.mappers

import de.flavormate.extensions.importExport.models.ieRecipeDraft.IERecipeDraftInstructionGroup
import de.flavormate.extensions.importExport.models.ieRecipeDraft.IERecipeDraftInstructionGroupItem
import de.flavormate.extensions.importExport.plugins.ldJson.models.types.step.LDJsonHowToSection
import de.flavormate.extensions.importExport.plugins.ldJson.models.types.step.LDJsonHowToStep
import de.flavormate.extensions.importExport.plugins.ldJson.models.types.step.LDJsonStep

object LDJsonInstructionMapper {
  /**
   * Maps a list of `LDJsonStep` elements to a list of `IERecipeDraftInstructionGroup` objects by
   * determining their grouping structure and applying the appropriate transformation.
   *
   * If the input does not contain any grouped elements, it creates a single instruction group. If
   * the input contains single instruction groups, these are flattened and combined into one group.
   * Otherwise, multiple instruction groups are created based on the grouping structure in the
   * input.
   *
   * @param input A list of `LDJsonStep` elements representing the instructional steps to be
   *   grouped. This list may contain elements of types such as `LDJsonHowToStep` and
   *   `LDJsonHowToSection`.
   * @return A list of `IERecipeDraftInstructionGroup` instances that represent the transformed and
   *   grouped instructions derived from the input steps.
   */
  fun mapInstructionGroups(input: List<LDJsonStep>): List<IERecipeDraftInstructionGroup> =
    when {
      !containsGroups(input) -> createSingleGroup(input)
      hasSingleInstructionGroups(input) -> createFlattenedGroup(flattenGroups(input))
      else -> createMultipleGroups(flattenGroups(input))
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
   * Creates a single instruction group from a list of LDJsonStep objects.
   *
   * @param steps A list of LDJsonStep instances that represent the steps to be included in the
   *   instruction group.
   * @return A list containing a single IERecipeDraftInstructionGroup, where the steps are mapped to
   *   instruction items and indexed sequentially.
   */
  private fun createSingleGroup(steps: List<LDJsonStep>): List<IERecipeDraftInstructionGroup> =
    IERecipeDraftInstructionGroup(
        label = null,
        index = 0,
        instructions =
          steps.mapIndexed { index, it -> mapInstructionGroupItem(it as LDJsonHowToStep, index) },
      )
      .let { listOf(it) }

  private fun createFlattenedGroup(steps: List<LDJsonStep>): List<IERecipeDraftInstructionGroup> =
    IERecipeDraftInstructionGroup(
        label = null,
        index = 0,
        instructions =
          steps.flatMapIndexed { index, step ->
            when (step) {
              is LDJsonHowToSection -> {
                step.itemListElement.map { mapInstructionGroupItem(it as LDJsonHowToStep, index) }
              }

              else -> {
                listOf(mapInstructionGroupItem(step as LDJsonHowToStep, index))
              }
            }
          },
      )
      .let { listOf(it) }

  /**
   * Creates a list of `IERecipeDraftInstructionGroup` from a list of `LDJsonStep` elements by
   * processing each step and grouping related instructions into structured groups.
   *
   * @param steps A list of `LDJsonStep` elements representing the steps to be converted into
   *   instruction groups.
   * @return A list of `IERecipeDraftInstructionGroup` where each group contains a label, index, and
   *   a list of instructions.
   */
  private fun createMultipleGroups(steps: List<LDJsonStep>): List<IERecipeDraftInstructionGroup> =
    steps.mapIndexedNotNull { index, step ->
      (step as? LDJsonHowToSection)?.let { section ->
        IERecipeDraftInstructionGroup(
          label = section.name,
          index = index,
          instructions =
            section.itemListElement.mapIndexed { index, it ->
              mapInstructionGroupItem(it as LDJsonHowToStep, index)
            },
        )
      }
    }

  /**
   * Maps an `LDJsonHowToStep` object and its positional index to an instance of
   * `IERecipeDraftInstructionGroupItem`.
   *
   * @param input Represents an individual step or text data from LD+JSON (supports HowToStep or
   *   Text schema).
   * @param index Positional index of the step in the sequence.
   * @return A new instance of `IERecipeDraftInstructionGroupItem`, populated with the step's label
   *   and index values.
   */
  private fun mapInstructionGroupItem(
    input: LDJsonHowToStep,
    index: Int,
  ): IERecipeDraftInstructionGroupItem =
    IERecipeDraftInstructionGroupItem(label = input.text, index = index)
}
