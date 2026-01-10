/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.shared.constants

import de.flavormate.shared.enums.SearchOrderBy

object AllowedSorts {
  val accounts =
    mapOf(
      SearchOrderBy.CreatedOn to "a.createdOn",
      SearchOrderBy.DisplayName to "a.displayName",
      SearchOrderBy.Username to "a.username",
    )

  val books =
    mapOf(
      SearchOrderBy.Label to "b.label",
      SearchOrderBy.CreatedOn to "b.createdOn",
      SearchOrderBy.Visible to "b.visible",
    )
  val categories = mapOf(SearchOrderBy.Label to "l.value")
  val categoryGroups = mapOf(SearchOrderBy.Label to "l.value")

  val files = mapOf(SearchOrderBy.CreatedOn to "f.createdOn")

  val highlights = mapOf(SearchOrderBy.CreatedOn to "h.date")

  val recipes = mapOf(SearchOrderBy.Label to "r.label", SearchOrderBy.CreatedOn to "r.createdOn")

  val recipeDrafts =
    mapOf(SearchOrderBy.CreatedOn to "r.createdOn", SearchOrderBy.Label to "r.label")

  val stories = mapOf(SearchOrderBy.Label to "s.label", SearchOrderBy.CreatedOn to "s.createdOn")

  val storyDrafts =
    mapOf(SearchOrderBy.CreatedOn to "s.createdOn", SearchOrderBy.Label to "s.label")

  val tags = mapOf(SearchOrderBy.Label to "t.label")

  val units = mapOf(SearchOrderBy.Label to "u.labelSg")
}
