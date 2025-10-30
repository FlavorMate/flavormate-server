/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.urlShortener.models

import de.flavormate.shared.models.entities.TracedEntity
import io.viascom.nanoid.NanoId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "v3__ext__url_shortener")
class ShortenerEntity : TracedEntity() {
  @Column(name = "short_path") lateinit var shortPath: String

  @Column(name = "original_path") lateinit var originalPath: String

  companion object {
    fun create(originalPath: String): ShortenerEntity {
      return ShortenerEntity().apply {
        this.originalPath = originalPath
        this.shortPath = NanoId.generate(size = 7)
      }
    }
  }
}
