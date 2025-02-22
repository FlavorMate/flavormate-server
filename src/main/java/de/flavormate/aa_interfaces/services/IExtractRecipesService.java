/* Licensed under AGPLv3 2024 */
package de.flavormate.aa_interfaces.services;

import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ba_entities.recipe.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IExtractRecipesService {
  Page<Recipe> findRecipesFromParent(Long id, Pageable pageable) throws CustomException;
}
