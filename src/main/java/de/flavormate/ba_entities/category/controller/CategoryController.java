package de.flavormate.ba_entities.category.controller;

import de.flavormate.aa_interfaces.controllers.IExtractRecipesController;
import de.flavormate.aa_interfaces.controllers.IPageableL10nController;
import de.flavormate.aa_interfaces.controllers.ISearchL10nController;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ba_entities.category.model.Category;
import de.flavormate.ba_entities.category.service.CategoryService;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.utils.RequestUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/categories")
public class CategoryController implements IExtractRecipesController, IPageableL10nController<Category>, ISearchL10nController<Category> {

	private final CategoryService service;

	protected CategoryController(CategoryService service) {
		this.service = service;
	}

	@Override
	public Page<Recipe> findRecipesFromParent(Long id, int page, int size, String sortBy, String sortDirection) throws CustomException {
		var pageable = RequestUtils.convertPageable(page, size, sortBy, sortDirection);
		return service.findRecipesFromParent(id, pageable);
	}

	@Override
	public Page<Category> findByPage(String language, int page, int size, String sortBy, String sortDirection) throws CustomException {
		var pageable = RequestUtils.convertPageable(page, size, sortBy, sortDirection);
		return service.findByPage(language, pageable);
	}

	@Override
	public Page<Category> findBySearch(String language, String searchTerm, int page, int size, String sortBy, String sortDirection) {
		var pageable = RequestUtils.convertPageable(page, size, sortBy, sortDirection);
		return service.findBySearch(language, searchTerm, pageable);
	}

	@GetMapping("/notEmpty")
	public Page<Category> findByNotEmpty(@RequestParam String language,
	                                     @RequestParam(defaultValue = "0") int page,
	                                     @RequestParam(defaultValue = "6") int size,
	                                     @RequestParam(defaultValue = "id") String sortBy,
	                                     @RequestParam(defaultValue = "DESC") String sortDirection) {
		var pageable = RequestUtils.convertPageable(page, size, sortBy, sortDirection);

		return service.findByNotEmpty(language, pageable);
	}
}