package de.flavormate.ba_entities.unit.service;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.aa_interfaces.services.BaseService;
import de.flavormate.aa_interfaces.services.ICRUDService;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ba_entities.unit.model.Unit;
import de.flavormate.ba_entities.unit.model.UnitConversion;
import de.flavormate.ba_entities.unit.model.UnitLocalized;
import de.flavormate.ba_entities.unit.repository.UnitConversionRepository;
import de.flavormate.ba_entities.unit.repository.UnitLocalizedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UnitService extends BaseService implements ICRUDService<UnitLocalized, Object> {
	private final UnitLocalizedRepository unitLocalizedRepository;
	private final UnitConversionRepository unitConversionRepository;

	@Override
	public UnitLocalized create(Object form) throws CustomException {
		throw new UnsupportedOperationException();
	}

	@Override
	public UnitLocalized update(Long id, JsonNode form) throws CustomException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean deleteById(Long id) throws CustomException {
		throw new UnsupportedOperationException();
	}

	@Override
	public UnitLocalized findById(Long id) throws CustomException {
		return unitLocalizedRepository.findById(id).orElseThrow(() -> new NotFoundException(Unit.class));
	}

	@Override
	public List<UnitLocalized> findAll() throws CustomException {
		return unitLocalizedRepository.findAll();
	}

	public List<UnitConversion> getAllConversions() {
		return unitConversionRepository.findAll();
		
	}
}
