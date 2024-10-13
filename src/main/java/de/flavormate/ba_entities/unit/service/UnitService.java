package de.flavormate.ba_entities.unit.service;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.aa_interfaces.services.BaseService;
import de.flavormate.aa_interfaces.services.ICRUDService;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ba_entities.unit.model.Unit;
import de.flavormate.ba_entities.unit.repository.UnitRepository;
import de.flavormate.ba_entities.unit.wrapper.UnitDraft;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UnitService extends BaseService implements ICRUDService<Unit, UnitDraft> {
	private final UnitRepository unitRepository;
	
	@Override
	public Unit create(UnitDraft form) throws CustomException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Unit update(Long id, JsonNode form) throws CustomException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean deleteById(Long id) throws CustomException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Unit findById(Long id) throws CustomException {
		return unitRepository.findById(id).orElseThrow(() -> new NotFoundException(Unit.class));
	}

	@Override
	public List<Unit> findAll() throws CustomException {
		return unitRepository.findAll();
	}
}
