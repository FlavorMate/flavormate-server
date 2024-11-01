/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.categoryGroup.controller;

import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ba_entities.categoryGroup.model.CategoryGroup;
import de.flavormate.ba_entities.categoryGroup.service.CategoryGroupService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/categoryGroups")
public class CategoryGroupController {

  private final CategoryGroupService categoryGroupService;

  @GetMapping("/")
  public List<CategoryGroup> findAll(@RequestParam String language) throws CustomException {
    return categoryGroupService.findAll(language);
  }
}
