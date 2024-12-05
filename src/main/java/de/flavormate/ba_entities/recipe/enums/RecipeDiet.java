/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.recipe.enums;

import java.util.List;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public enum RecipeDiet {
  Meat,
  Fish,
  Vegetarian,
  Vegan;

  public static List<RecipeDiet> getFilter(RecipeDiet diet) {
    return switch (diet) {
      case Meat ->
          List.of(RecipeDiet.Meat, RecipeDiet.Fish, RecipeDiet.Vegetarian, RecipeDiet.Vegan);
      case Fish -> List.of(RecipeDiet.Fish, RecipeDiet.Vegetarian, RecipeDiet.Vegan);
      case Vegetarian -> List.of(RecipeDiet.Vegetarian, RecipeDiet.Vegan);
      case Vegan -> List.of(RecipeDiet.Vegan);
    };
  }

  public static String[] getFilterNames(RecipeDiet diet) {
    return getFilter(diet).stream().map(Enum::toString).toArray(String[]::new);
  }

  public String getName(MessageSource messageSource) {
    final var locale = LocaleContextHolder.getLocale();
    return switch (this) {
      case Meat -> messageSource.getMessage("diet_meat", null, locale);
      case Fish -> messageSource.getMessage("diet_fish", null, locale);
      case Vegetarian -> messageSource.getMessage("diet_vegetarian", null, locale);
      case Vegan -> messageSource.getMessage("diet_vegan", null, locale);
    };
  }
}
