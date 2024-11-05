/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.instructionGroup.wrapper;

import de.flavormate.ba_entities.instruction.wrapper.InstructionDraft;
import java.util.List;

public record InstructionGroupDraft(String label, List<InstructionDraft> instructions) {}
