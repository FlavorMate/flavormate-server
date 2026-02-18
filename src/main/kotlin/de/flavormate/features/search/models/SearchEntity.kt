/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.features.search.models

import jakarta.persistence.*
import org.hibernate.annotations.Immutable

@Entity
@Immutable
@Table(name = "v3__search_index")
class SearchEntity {
  @Id @Column(name = "pk") lateinit var pk: String

  @Column(name = "entity_id") lateinit var entityId: String

  @Column(name = "source") @Enumerated(EnumType.STRING) lateinit var source: SearchFilter

  @Column(name = "label") lateinit var label: String

  @Column(name = "book_visible") var bookVisible: Boolean? = null

  @Column(name = "book_owned_by") var bookOwnedBy: String? = null

  @Column(name = "category_language") var categoryLanguage: String? = null

  @Column(name = "recipe_id") var recipeId: String? = null

  @Column(name = "file_id") var fileId: String? = null
}
