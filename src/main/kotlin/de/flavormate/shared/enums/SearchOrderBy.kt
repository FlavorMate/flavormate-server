/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.shared.enums

enum class SearchOrderBy {
  CreatedOn,
  Label,

  // Only used for BookEntity
  Visible,

  // Only used for AccountEntity
  DisplayName,
  Username,
}
