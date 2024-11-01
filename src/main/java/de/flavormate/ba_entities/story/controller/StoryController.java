/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.story.controller;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.aa_interfaces.controllers.ICRUDController;
import de.flavormate.aa_interfaces.controllers.IPageableController;
import de.flavormate.aa_interfaces.controllers.ISearchController;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ba_entities.story.model.Story;
import de.flavormate.ba_entities.story.service.StoryService;
import de.flavormate.ba_entities.story.wrapper.StoryDraft;
import de.flavormate.utils.RequestUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ConditionalOnProperty(
    prefix = "flavormate.features.story",
    value = "enabled",
    havingValue = "true")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/stories")
public class StoryController
    implements ICRUDController<Story, StoryDraft>,
        IPageableController<Story>,
        ISearchController<Story> {
  private final StoryService storyService;

  @Override
  public Page<Story> findBySearch(
      String searchTerm, int page, int size, String sortBy, String sortDirection) {

    var pageable = RequestUtils.convertPageable(page, size, sortBy, sortDirection);

    return storyService.findBySearch(searchTerm, pageable);
  }

  @Override
  public Story create(StoryDraft form) throws CustomException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Story update(Long id, JsonNode form) throws CustomException {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean deleteById(Long id) throws CustomException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Story findById(Long id) throws CustomException {
    return storyService.findById(id);
  }

  @Override
  public List<Story> findAll() throws CustomException {
    return storyService.findAll();
  }

  @Override
  public Page<Story> findByPage(int page, int size, String sortBy, String sortDirection)
      throws CustomException {
    var pageable = RequestUtils.convertPageable(page, size, sortBy, sortDirection);
    return storyService.findByPage(pageable);
  }
}
