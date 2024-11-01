/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.categoryGroup.service;

import de.flavormate.aa_interfaces.services.BaseService;
import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ba_entities.categoryGroup.model.CategoryGroup;
import de.flavormate.ba_entities.categoryGroup.repository.CategoryGroupRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryGroupService extends BaseService {
  private final CategoryGroupRepository categoryGroupRepository;

  public List<CategoryGroup> findAll(String language) throws CustomException {
    return categoryGroupRepository.findAll(language);
  }
}
