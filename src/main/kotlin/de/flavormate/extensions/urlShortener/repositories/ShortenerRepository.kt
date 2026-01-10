/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.urlShortener.repositories

import de.flavormate.extensions.urlShortener.models.ShortenerEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class ShortenerRepository : PanacheRepositoryBase<ShortenerEntity, String> {
  fun findByOriginalPath(originalPath: String): String? {
    val params = mapOf("originalPath" to originalPath)

    return find("originalPath = :originalPath", params).firstResult()?.shortPath
  }

  fun findByShortPath(shortPath: String): String? {
    val params = mapOf("shortPath" to shortPath)

    return find("shortPath = :shortPath", params).firstResult()?.originalPath
  }
}
