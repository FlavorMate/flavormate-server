/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.recipe.controller;

import de.flavormate.ab_exeptions.exceptions.CustomException;
import de.flavormate.ba_entities.recipe.model.Recipe;
import de.flavormate.ba_entities.recipe.service.PublicRecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Secured({"ROLE_ANONYMOUS", "ROLE_USER"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/public/recipes")
public class PublicRecipeController {

  private final PublicRecipeService publicRecipesService;

  @GetMapping("/{id}/l10n")
  public Recipe findById(
      @PathVariable Long id, @RequestParam String language, @RequestParam String token)
      throws CustomException {
    return publicRecipesService.findByIdL10n(id, language, token);
  }
}
