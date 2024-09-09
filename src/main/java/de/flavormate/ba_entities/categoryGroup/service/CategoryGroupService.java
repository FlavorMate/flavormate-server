package de.flavormate.ba_entities.categoryGroup.service;

import de.flavormate.aa_interfaces.services.BaseService;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ba_entities.categoryGroup.model.CategoryGroup;
import de.flavormate.ba_entities.categoryGroup.repository.CategoryGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryGroupService extends BaseService {
	private final CategoryGroupRepository repository;

	protected CategoryGroupService(CategoryGroupRepository repository) {
		this.repository = repository;
	}

	public List<CategoryGroup> findAll(String language) throws CustomException {
		return repository.findAll(language);
	}


}
