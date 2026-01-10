/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.shared.enums

enum class Diet {
  Meat,
  Fish,
  Vegetarian,
  Vegan;

  val allowedDiets
    get(): Set<Diet> =
      when (this) {
        Meat -> setOf(Meat, Fish, Vegetarian, Vegan)
        Fish -> setOf(Fish, Vegetarian, Vegan)
        Vegetarian -> setOf(Vegetarian, Vegan)
        Vegan -> setOf(Vegan)
      }
}
