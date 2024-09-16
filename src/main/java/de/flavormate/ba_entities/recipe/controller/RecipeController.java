package de.flavormate.ba_entities.recipe.controller;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.aa_interfaces.controllers.ICRUDController;
import de.flavormate.aa_interfaces.controllers.IPageableDietController;
import de.flavormate.aa_interfaces.controllers.ISearchDietController;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ba_entities.recipe.enums.RecipeCourse;
import de.flavormate.ba_entities.recipe.enums.RecipeDiet;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.recipe.service.RecipeService;
import de.flavormate.ba_entities.recipe.service.ScraperService;
import de.flavormate.ba_entities.recipe.wrapper.ChangeOwnerForm;
import de.flavormate.ba_entities.recipe.wrapper.RecipeDraft;
import de.flavormate.ba_entities.recipe.wrapper.ScrapeResponse;
import de.flavormate.utils.RequestUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v2/recipes")
public class RecipeController implements ICRUDController<Recipe, RecipeDraft>, ISearchDietController<Recipe>, IPageableDietController<Recipe> {

	private final RecipeService service;
	private final ScraperService scraperService;

	protected RecipeController(RecipeService service, ScraperService scraperService) {
		this.service = service;
		this.scraperService = scraperService;
	}

	@GetMapping("/crawl")
	public ScrapeResponse crawl(@RequestParam String url) throws Exception {
		return scraperService.scrape(url);
	}

	@GetMapping("/random/{diet}")
	public List<Recipe> getRandom(@PathVariable RecipeDiet diet,
	                              @RequestParam(name = "course", defaultValue = "%") String courseStr, @RequestParam(name = "amount", defaultValue = "1") int amount)
			throws CustomException {
		String course;

		try {
			course = RecipeCourse.valueOf(courseStr).toString();
		} catch (Exception ex) {
			course = "%";
		}

		return service.findRandomByDietAndCourse(diet, course, amount);
	}

	@Secured({"ROLE_ADMIN"})
	@PutMapping("/{id}/changeOwner")
	public Boolean changeOwner(@PathVariable Long id, @RequestBody ChangeOwnerForm form)
			throws CustomException {
		return service.changeOwner(id, form);
	}


	@Secured({"ROLE_USER", "ROLE_ANONYMOUS"})
	@GetMapping("/{id}/bring/{serving}")
	public String getBring(@PathVariable Long id, @PathVariable("serving") Integer serving)
			throws Exception {
		return service.getBring(id, serving);
	}

	@Override
	public Recipe create(RecipeDraft form) throws CustomException {
		return service.create(form);
	}

	@Override
	public Recipe update(Long id, JsonNode form) throws CustomException {
		return service.update(id, form);
	}

	@Override
	public boolean deleteById(Long id) throws CustomException {
		return service.deleteById(id);
	}

	@Override
	public Recipe findById(Long id) throws CustomException {
		return service.findById(id);
	}

	@GetMapping("/{id}/l10n")
	public Recipe findByIdL10n(@PathVariable Long id, @RequestParam String language) throws CustomException {
		return service.findByIdL10n(id, language);
	}

	@Override
	public List<Recipe> findAll() throws CustomException {
		return service.findAll();
	}

	@Override
	public Page<Recipe> findByPage(RecipeDiet diet, int page, int size, String sortBy, String sortDirection) throws CustomException {
		var pageable = RequestUtils.convertPageable(page, size, sortBy, sortDirection);
		return service.findByPage(diet, pageable);
	}

	@Override
	public Page<Recipe> findBySearch(String searchTerm, RecipeDiet diet, int page, int size, String sortBy, String sortDirection) {
		var pageable = RequestUtils.convertPageable(page, size, sortBy, sortDirection);
		return service.findBySearch(searchTerm, diet, pageable);
	}
}
