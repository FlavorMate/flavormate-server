package de.flavormate.ba_entities.unit.controller;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.aa_interfaces.controllers.ICRUDController;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ba_entities.unit.model.Unit;
import de.flavormate.ba_entities.unit.service.UnitService;
import de.flavormate.ba_entities.unit.wrapper.UnitDraft;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v2/units")
public class UnitController implements ICRUDController<Unit, UnitDraft> {
	private final UnitService service;

	protected UnitController(UnitService service) {
		this.service = service;
	}

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
		return service.findById(id);
	}

	@Override
	public List<Unit> findAll() throws CustomException {
		return service.findAll();
	}
}
