package de.flavormate.ba_entities.categoryGroup.controller;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.aa_interfaces.controllers.ICRUDController;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ba_entities.categoryGroup.model.CategoryGroup;
import de.flavormate.ba_entities.categoryGroup.service.CategoryGroupService;
import de.flavormate.ba_entities.categoryGroup.wrapper.CategoryGroupDraft;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v2/categoryGroups")
@Secured({"ROLE_ADMIN"})
public class CategoryGroupController implements ICRUDController<CategoryGroup, CategoryGroupDraft> {

	private final CategoryGroupService service;

	protected CategoryGroupController(CategoryGroupService service) {
		this.service = service;
	}

	@Override
	public CategoryGroup create(CategoryGroupDraft form) throws CustomException {
		throw new UnsupportedOperationException();
	}

	@Override
	public CategoryGroup update(Long id, JsonNode form) throws CustomException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean deleteById(Long id) throws CustomException {
		throw new UnsupportedOperationException();
	}

	@Override
	public CategoryGroup findById(Long id) throws CustomException {
		return service.findById(id);
	}

	@Override
	public List<CategoryGroup> findAll() throws CustomException {
		return service.findAll();
	}
}
