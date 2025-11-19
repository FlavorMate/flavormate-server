/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.utils

object ValidatorUtils {

  fun validateUUID4(uuid: String): Boolean {
    return uuid.matches(
      Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    )
  }
}
