/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.utils

object ValidatorUtils {

  fun validateUUID4(uuid: String): Boolean {
    return uuid.matches(
      Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    )
  }

  /**
   * AppName/AppVersion (Device; OS-Version) OtherProps e.g. "FlavorMate/3.0.0 (iPhone 14; iOS
   * 15.4.1)"
   */
  fun validateUserAgent(input: String?): Boolean {
    if (input.isNullOrBlank() || input.length > 255) return false

    // Regex pattern for validating the User-Agent format
    val regex =
      Regex("""^(FlavorMate)/([0-9]+\.[0-9]+\.[0-9]+) \((.+); (.+)\)$""", RegexOption.IGNORE_CASE)
    // Validate the User-Agent against the regex pattern
    return regex.matches(input)
  }
}
