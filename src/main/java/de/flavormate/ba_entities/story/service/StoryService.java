/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.story.service;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.aa_interfaces.services.BaseService;
import de.flavormate.aa_interfaces.services.ICRUDService;
import de.flavormate.aa_interfaces.services.IPageableService;
import de.flavormate.aa_interfaces.services.ISearchService;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ba_entities.author.repository.AuthorRepository;
import de.flavormate.ba_entities.author.service.AuthorService;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.recipe.repository.RecipeRepository;
import de.flavormate.ba_entities.story.model.Story;
import de.flavormate.ba_entities.story.repository.StoryRepository;
import de.flavormate.ba_entities.story.wrapper.StoryDraft;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StoryService extends BaseService
    implements ICRUDService<Story, StoryDraft>, IPageableService<Story>, ISearchService<Story> {
  private final StoryRepository storyRepository;
  private final RecipeRepository recipeRepository;
  private final AuthorRepository authorRepository;

  @Override
  public Story update(Long id, JsonNode json) throws CustomException {
    var story = this.findById(id);

    if (StringUtils.isNotBlank(json.get("label").asText())) {
      story.setLabel(json.get("label").asText());
    }

    if (StringUtils.isNotBlank(json.get("content").asText())) {
      story.setContent(json.get("content").asText());
    }

    if (json.has("recipe")) {
      var recipe =
          recipeRepository
              .findById(json.get("recipe").get("id").asLong())
              .orElseThrow(() -> new NotFoundException(Recipe.class));

      story.setRecipe(recipe);
    }

    return storyRepository.save(story);
  }

  @Override
  public boolean deleteById(Long id) throws CustomException {
    storyRepository.deleteById(id);
    return true;
  }

  @Override
  public Story findById(Long id) throws CustomException {
    return storyRepository.findById(id).orElseThrow(() -> new NotFoundException(Story.class));
  }

  @Override
  public List<Story> findAll() throws CustomException {
    return storyRepository.findAll();
  }

  @Override
  public Page<Story> findBySearch(String searchTerm, Pageable pageable) {
    return storyRepository.findBySearch(searchTerm, pageable);
  }

  @Override
  public Story create(StoryDraft object) throws CustomException {

    var author =
        authorRepository
            .findByAccountUsername(getPrincipal().getUsername())
            .orElseThrow(() -> new NotFoundException(AuthorService.class));

    var recipe =
        recipeRepository
            .findById(object.recipe().getId())
            .orElseThrow(() -> new NotFoundException(Recipe.class));

    Story story =
        Story.builder()
            .author(author)
            .label(object.label())
            .content(object.content())
            .recipe(recipe)
            .build();

    return storyRepository.save(story);
  }

  @Override
  public Page<Story> findByPage(Pageable pageable) throws CustomException {
    return storyRepository.findAll(pageable);
  }
}
