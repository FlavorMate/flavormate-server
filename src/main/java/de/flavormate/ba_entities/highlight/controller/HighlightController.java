/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.highlight.controller;

import de.flavormate.aa_interfaces.controllers.IPageableController;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ba_entities.highlight.model.Highlight;
import de.flavormate.ba_entities.highlight.service.HighlightService;
import de.flavormate.ba_entities.recipe.enums.RecipeDiet;
import de.flavormate.utils.RequestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/highlights")
public class HighlightController implements IPageableController<Highlight> {
  private final HighlightService highlightService;

  @GetMapping("/list/{diet}")
  public Page<Highlight> findAllByDiet(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "6") Integer size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "DESC") String sortDirection,
      @PathVariable("diet") RecipeDiet filter)
      throws CustomException {

    var pageable = RequestUtils.convertPageable(page, size, sortBy, sortDirection);

    return highlightService.findByPageAndDiet(pageable, filter);
  }

  @Override
  public Page<Highlight> findByPage(int page, int size, String sortBy, String sortDirection)
      throws CustomException {
    var pageable = RequestUtils.convertPageable(page, size, sortBy, sortDirection);
    return highlightService.findByPageAndDiet(pageable, RecipeDiet.Meat);
  }
}
