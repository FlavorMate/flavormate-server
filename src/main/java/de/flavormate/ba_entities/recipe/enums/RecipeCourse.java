/* Licensed under AGPLv3 2024 */
package de.flavormate.ba_entities.recipe.enums;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public enum RecipeCourse {
  Appetizer,
  MainDish,
  Dessert,
  SideDish,
  Bakery,
  Drink;

  public String getName(MessageSource messageSource) {
    final var locale = LocaleContextHolder.getLocale();
    return switch (this) {
      case Appetizer -> messageSource.getMessage("course_appetizer", null, locale);
      case MainDish -> messageSource.getMessage("course_main-dish", null, locale);
      case Dessert -> messageSource.getMessage("course_dessert", null, locale);
      case SideDish -> messageSource.getMessage("course_side-dish", null, locale);
      case Bakery -> messageSource.getMessage("course_bakery", null, locale);
      case Drink -> messageSource.getMessage("course_drink", null, locale);
    };
  }
}
