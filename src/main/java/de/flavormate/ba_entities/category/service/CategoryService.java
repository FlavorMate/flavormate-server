package de.flavormate.ba_entities.category.service;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.aa_interfaces.services.*;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ba_entities.category.model.Category;
import de.flavormate.ba_entities.category.repository.CategoryRepository;
import de.flavormate.ba_entities.category.wrapper.CategoryDraft;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.recipe.repository.RecipeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService extends BaseService implements ICRUDService<Category, CategoryDraft>, IExtractRecipesService, IPageableL10nService<Category>, ISearchL10nService<Category> {
	private final CategoryRepository repository;
	private final RecipeRepository recipeRepository;

	protected CategoryService(CategoryRepository repository, RecipeRepository recipeRepository) {
		this.repository = repository;
		this.recipeRepository = recipeRepository;
	}

	@Override
	public Category create(CategoryDraft object) throws CustomException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Category update(Long id, JsonNode json) throws CustomException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean deleteById(Long id) throws CustomException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Category findById(Long id) throws CustomException {
		return repository.findById(id).orElseThrow(() -> new NotFoundException(Category.class));
	}

	@Override
	public List<Category> findAll() throws CustomException {
		return repository.findAll();
	}


	public Page<Category> findByNotEmpty(String language, Pageable pageable) {
		return repository.findByNotEmpty(language, pageable);
	}

	@Override
	public Page<Recipe> findRecipesFromParent(Long id, Pageable pageable) throws CustomException {
		return recipeRepository.findByCategory(id, pageable);
	}

	@Override
	public Page<Category> findByPage(String language, Pageable pageable) throws CustomException {
		return repository.findByPage(language, pageable);
	}

	@Override
	public Page<Category> findBySearch(String language, String searchTerm, Pageable pageable) {
		return repository.findBySearch(language, searchTerm, pageable);
	}

	public List<Category> findByRaw(String language) {
		var categories = repository.findByRaw(language);
		for (var category : categories) {
			category.getRecipes().clear();
		}

		return categories;
	}
}
