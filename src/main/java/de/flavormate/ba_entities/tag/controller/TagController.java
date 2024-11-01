/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.tag.controller;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.aa_interfaces.controllers.ICRUDController;
import de.flavormate.aa_interfaces.controllers.IExtractRecipesController;
import de.flavormate.aa_interfaces.controllers.IPageableController;
import de.flavormate.aa_interfaces.controllers.ISearchController;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.tag.model.Tag;
import de.flavormate.ba_entities.tag.service.TagService;
import de.flavormate.ba_entities.tag.wrapper.TagDraft;
import de.flavormate.utils.RequestUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/tags")
public class TagController
    implements ICRUDController<Tag, TagDraft>,
        IExtractRecipesController,
        IPageableController<Tag>,
        ISearchController<Tag> {
  private final TagService tagService;

  @Override
  public Page<Recipe> findRecipesFromParent(
      @PathVariable Long id,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "6") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "DESC") String sortDirection) {

    var pageable = RequestUtils.convertPageable(page, size, sortBy, sortDirection);

    return tagService.findRecipesFromParent(id, pageable);
  }

  @Transactional
  @Override
  public Page<Tag> findBySearch(
      String searchTerm, int page, int size, String sortBy, String sortDirection) {
    var pageable = RequestUtils.convertPageable(page, size, sortBy, sortDirection);

    return tagService.findBySearch(searchTerm, pageable);
  }

  @Override
  public Tag create(TagDraft form) throws CustomException {
    return tagService.create(form);
  }

  @Override
  public Tag update(Long id, JsonNode form) throws CustomException {
    return tagService.update(id, form);
  }

  @Override
  public boolean deleteById(Long id) throws CustomException {
    return tagService.deleteById(id);
  }

  @Override
  public Tag findById(Long id) throws CustomException {
    return tagService.findById(id);
  }

  @Override
  public List<Tag> findAll() throws CustomException {
    return tagService.findAll();
  }

  @Override
  public Page<Tag> findByPage(int page, int size, String sortBy, String sortDirection)
      throws CustomException {
    var pageable = RequestUtils.convertPageable(page, size, sortBy, sortDirection);
    return tagService.findByPage(pageable);
  }
}
