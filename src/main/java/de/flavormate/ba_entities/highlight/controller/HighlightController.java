package de.flavormate.ba_entities.highlight.controller;

import de.flavormate.aa_interfaces.controllers.IPageableController;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ba_entities.highlight.model.Highlight;
import de.flavormate.ba_entities.highlight.service.HighlightService;
import de.flavormate.ba_entities.recipe.enums.RecipeDiet;
import de.flavormate.utils.RequestUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2/highlights")
public class HighlightController implements IPageableController<Highlight> {
	private final HighlightService service;

	protected HighlightController(HighlightService service) {
		this.service = service;
	}

	@GetMapping("/list/{diet}")
	public Page<Highlight> findAllByDiet(@RequestParam(defaultValue = "0") Integer page,
	                                     @RequestParam(defaultValue = "6") Integer size,
	                                     @RequestParam(defaultValue = "id") String sortBy,
	                                     @RequestParam(defaultValue = "DESC") String sortDirection,
	                                     @PathVariable("diet") RecipeDiet filter) throws CustomException {

		var pageable = RequestUtils.convertPageable(page, size, sortBy, sortDirection);

		return service.findByPageAndDiet(pageable, filter);
	}

	@Override
	public Page<Highlight> findByPage(int page, int size, String sortBy, String sortDirection) throws CustomException {
		var pageable = RequestUtils.convertPageable(page, size, sortBy, sortDirection);
		return service.findByPageAndDiet(pageable, RecipeDiet.Meat);
	}
}
