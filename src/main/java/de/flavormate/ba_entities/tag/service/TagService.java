/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.tag.service;

import com.fasterxml.jackson.databind.JsonNode;
import de.flavormate.aa_interfaces.services.*;
import de.flavormate.ab_exeptions.exceptions.ConflictException;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ab_exeptions.exceptions.NotFoundException;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.recipe.repository.RecipeRepository;
import de.flavormate.ba_entities.tag.model.Tag;
import de.flavormate.ba_entities.tag.repository.TagRepository;
import de.flavormate.ba_entities.tag.wrapper.TagDraft;
import de.flavormate.utils.JSONUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TagService extends BaseService
    implements ICRUDService<Tag, TagDraft>,
        IExtractRecipesService,
        IPageableService<Tag>,
        ISearchService<Tag> {
  private final TagRepository tagRepository;
  private final RecipeRepository recipeRepository;

  @Override
  public Tag create(TagDraft body) throws CustomException {
    if (tagRepository.findByLabel(body.label().toLowerCase()).isPresent()) {
      throw new ConflictException(Tag.class);
    }

    var tag = Tag.builder().label(body.label().toLowerCase()).build();
    return tagRepository.save(tag);
  }

  @Override
  public Tag update(Long id, JsonNode json) throws CustomException {
    var tag = this.findById(id);

    if (json.has("label")) {
      var label = JSONUtils.parseObject(json.get("label"), String.class);
      tag.setLabel(label);
    }

    return tagRepository.save(tag);
  }

  @Override
  public boolean deleteById(Long id) throws CustomException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Tag findById(Long id) throws CustomException {
    return tagRepository.findById(id).orElseThrow(() -> new NotFoundException(Tag.class));
  }

  @Override
  public List<Tag> findAll() throws CustomException {
    return tagRepository.findAll();
  }

  @Override
  public Page<Tag> findBySearch(String searchTerm, Pageable pageable) {
    return tagRepository.findBySearch(searchTerm, pageable);
  }

  @Override
  public Page<Recipe> findRecipesFromParent(Long id, Pageable pageable) {
    return recipeRepository.findByTag(id, pageable);
  }

  @Override
  public Page<Tag> findByPage(Pageable pageable) throws CustomException {
    return tagRepository.findAll(pageable);
  }
}
