/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.author.service;

import de.flavormate.aa_interfaces.services.IExtractRecipesService;
import de.flavormate.aa_interfaces.services.ISearchService;
import de.flavormate.ba_entities.author.model.Author;
import de.flavormate.ba_entities.author.repository.AuthorRepository;
import de.flavormate.ba_entities.recipe.model.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthorService implements IExtractRecipesService, ISearchService<Author> {
  private final AuthorRepository authorRepository;

  public Page<Recipe> findRecipesFromParent(Long id, Pageable pageable) {
    return authorRepository.findRecipesFromParent(id, pageable);
  }

  public Page<Author> findBySearch(String searchTerm, Pageable pageable) {
    return authorRepository.findBySearch(searchTerm, pageable);
  }
}
