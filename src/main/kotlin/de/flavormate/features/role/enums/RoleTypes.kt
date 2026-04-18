/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.role.enums

enum class RoleTypes {
  // User roles
  Admin,
  User,
  Import,
  Export,

  // Public accessors
  Bring,
  Share,

  // Used for account verification
  Verify,

  // Used for JWT
  Refresh,
  Reset;

  companion object {
    // User roles
    const val ADMIN_VALUE = "Admin"
    const val USER_VALUE = "User"
    const val IMPORT_VALUE = "Import"
    const val EXPORT_VALUE = "Export"

    // Technical roles
    const val BRING_VALUE = "Bring"
    const val SHARE_VALUE = "Share"
    const val VERIFY_VALUE = "Verify"
    const val REFRESH_VALUE = "Refresh"
    const val RESET_VALUE = "Reset"
  }
}
