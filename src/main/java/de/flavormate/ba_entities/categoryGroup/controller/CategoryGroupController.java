package de.flavormate.ba_entities.categoryGroup.controller;

import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ba_entities.categoryGroup.model.CategoryGroup;
import de.flavormate.ba_entities.categoryGroup.service.CategoryGroupService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v2/categoryGroups")
@Secured({"ROLE_ADMIN"})
public class CategoryGroupController {

	private final CategoryGroupService service;

	protected CategoryGroupController(CategoryGroupService service) {
		this.service = service;
	}


	@GetMapping("/")
	public List<CategoryGroup> findAll(@RequestParam String language) throws CustomException {
		return service.findAll(language);
	}
}
