package de.flavormate.ba_entities.backup.model.flavorMate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.flavormate.ba_entities.instruction.model.Instruction;
import de.flavormate.ba_entities.instruction.wrapper.InstructionDraft;
import de.flavormate.ba_entities.instructionGroup.model.InstructionGroup;
import de.flavormate.ba_entities.instructionGroup.wrapper.InstructionGroupDraft;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public final class FMInstructionGroup {
	private String label;
	private List<String> instructions;

	public static FMInstructionGroup fromInstructionGroup(InstructionGroup instructionGroup) {
		return new FMInstructionGroup(
				instructionGroup.getLabel(),
				instructionGroup.getInstructions().stream().map(Instruction::getLabel).toList()
		);
	}

	@JsonIgnore
	public InstructionGroupDraft toInstructionGroupDraft() {
		return new InstructionGroupDraft(
				label,
				instructions.stream().map(InstructionDraft::new).toList()
		);
	}
}
