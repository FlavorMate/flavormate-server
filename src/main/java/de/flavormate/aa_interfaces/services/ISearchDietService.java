/* Licensed under AGPLv3 2024 */
package de.flavormate.aa_interfaces.services;

import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ba_entities.recipe.enums.RecipeDiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ISearchDietService<T> {
  Page<T> findBySearch(String searchTerm, RecipeDiet diet, Pageable pageable)
      throws CustomException;
}
