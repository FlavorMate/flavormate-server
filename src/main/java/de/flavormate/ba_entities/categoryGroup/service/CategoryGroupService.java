package de.flavormate.ba_entities.categoryGroup.service;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.aa_interfaces.services.BaseService;
import de.flavormate.aa_interfaces.services.ICRUDService;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ba_entities.categoryGroup.model.CategoryGroup;
import de.flavormate.ba_entities.categoryGroup.repository.CategoryGroupRepository;
import de.flavormate.ba_entities.categoryGroup.wrapper.CategoryGroupDraft;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryGroupService extends BaseService implements ICRUDService<CategoryGroup, CategoryGroupDraft> {
	private final CategoryGroupRepository repository;

	protected CategoryGroupService(CategoryGroupRepository repository) {
		this.repository = repository;
	}

	@Override
	public CategoryGroup create(CategoryGroupDraft object) throws CustomException {
		throw new UnsupportedOperationException();
	}

	@Override
	public CategoryGroup update(Long id, JsonNode json) throws CustomException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean deleteById(Long id) throws CustomException {
		throw new UnsupportedOperationException();
	}

	@Override
	public CategoryGroup findById(Long id) throws CustomException {
		return repository.findById(id).orElseThrow(() -> new NotFoundException(CategoryGroup.class));
	}

	@Override
	public List<CategoryGroup> findAll() throws CustomException {
		return repository.findAll();
	}

	
}
