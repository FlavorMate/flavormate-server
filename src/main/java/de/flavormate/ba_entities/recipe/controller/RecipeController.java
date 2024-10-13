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
import de.flavormate.ba_entities.recipe.wrapper.ChangeOwnerForm;
import de.flavormate.ba_entities.recipe.wrapper.RecipeDraft;
import de.flavormate.ba_entities.recipe.wrapper.ScrapeResponse;
import de.flavormate.ba_entities.scrape.service.ScrapeService;
import de.flavormate.utils.RequestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/recipes")
public class RecipeController implements ICRUDController<Recipe, RecipeDraft>, ISearchDietController<Recipe>, IPageableDietController<Recipe> {

	private final RecipeService recipeService;
	private final ScrapeService scrapeService;

	@GetMapping("/crawl")
	public ScrapeResponse crawl(@RequestParam String url) throws Exception {
		return scrapeService.fetchAndParseRecipe(url);
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

		return recipeService.findRandomByDietAndCourse(diet, course, amount);
	}

	@Secured({"ROLE_ADMIN"})
	@PutMapping("/{id}/changeOwner")
	public Boolean changeOwner(@PathVariable Long id, @RequestBody ChangeOwnerForm form)
			throws CustomException {
		return recipeService.changeOwner(id, form);
	}


	@Override
	public Recipe create(RecipeDraft form) throws CustomException {
		return recipeService.create(form);
	}

	@Override
	public Recipe update(Long id, JsonNode form) throws CustomException {
		return recipeService.update(id, form);
	}

	@Override
	public boolean deleteById(Long id) throws CustomException {
		return recipeService.deleteById(id);
	}

	@Override
	public Recipe findById(Long id) throws CustomException {
		return recipeService.findById(id);
	}

	@GetMapping("/{id}/l10n")
	public Recipe findByIdL10n(@PathVariable Long id, @RequestParam String language) throws CustomException {
		return recipeService.findByIdL10n(id, language);
	}

	@Override
	public List<Recipe> findAll() throws CustomException {
		return recipeService.findAll();
	}

	@Override
	public Page<Recipe> findByPage(RecipeDiet diet, int page, int size, String sortBy, String sortDirection) throws CustomException {
		var pageable = RequestUtils.convertPageable(page, size, sortBy, sortDirection);
		return recipeService.findByPage(diet, pageable);
	}

	@Override
	public Page<Recipe> findBySearch(String searchTerm, RecipeDiet diet, int page, int size, String sortBy, String sortDirection) {
		var pageable = RequestUtils.convertPageable(page, size, sortBy, sortDirection);
		return recipeService.findBySearch(searchTerm, diet, pageable);
	}
}
