package de.flavormate.ba_entities.author.controller;

import de.flavormate.aa_interfaces.controllers.IExtractRecipesController;
import de.flavormate.aa_interfaces.controllers.ISearchController;
import de.flavormate.ba_entities.author.model.Author;
import de.flavormate.ba_entities.author.service.AuthorService;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.utils.RequestUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/authors")
public class AuthorController implements IExtractRecipesController, ISearchController<Author> {

	private final AuthorService service;

	protected AuthorController(AuthorService service) {
		this.service = service;
	}

	public Page<Recipe> findRecipesFromParent(Long id, int page, int size, String sortBy, String sortDirection) {
		var pageable = RequestUtils.convertPageable(page, size, sortBy, sortDirection);

		return service.findRecipesFromParent(id, pageable);
	}


	public Page<Author> findBySearch(String searchTerm, int page, int size, String sortBy, String sortDirection) {
		var pageable = RequestUtils.convertPageable(page, size, sortBy, sortDirection);

		return service.findBySearch(searchTerm, pageable);

	}


}
