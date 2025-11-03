/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.shared.extensions

/**
 * Transforms the elements of a set using the given transformation function and returns a new
 * mutable set containing the transformed elements, or null if the resulting set is empty.
 *
 * @param transform A lambda function used to transform each element of the Original set.
 * @return A new mutable set containing the transformed elements, or null if the resulting set is
 *   empty.
 */
fun <S, T> Set<S>.mapToSet(transform: (S) -> T): MutableSet<T>? {
  return this.mapTo(mutableSetOf(), transform).takeIf { it.isNotEmpty() }
}

fun <S, T> List<S>.mapToSet(transform: (S) -> T): MutableSet<T>? {
  return this.mapTo(mutableSetOf(), transform).takeIf { it.isNotEmpty() }
}

fun String?.trimToNull() = this?.trim()?.ifEmpty { null }

fun String?.trimToBlank() = this?.trim()?.ifEmpty { "" }

fun String.toKebabCase(): String {
  return this.trim() // Remove leading and trailing spaces
    .replace(
      Regex("[^\\p{L}\\p{N}\\s]"),
      "",
    ) // Allow letters (including umlauts) and numbers, remove special characters
    .replace(Regex("\\s+"), "-") // Replace spaces with hyphens
    .lowercase() // Convert to lowercase
}
