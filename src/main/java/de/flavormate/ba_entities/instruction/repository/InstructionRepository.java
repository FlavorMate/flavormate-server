/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.instruction.repository;

import de.flavormate.ba_entities.instruction.model.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructionRepository extends JpaRepository<Instruction, Long> {}
