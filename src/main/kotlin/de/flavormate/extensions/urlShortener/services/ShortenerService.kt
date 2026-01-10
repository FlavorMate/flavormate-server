/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.urlShortener.services

import de.flavormate.configuration.properties.FlavorMateProperties
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.extensions.urlShortener.models.ShortenerEntity
import de.flavormate.extensions.urlShortener.repositories.ShortenerRepository
import jakarta.enterprise.context.ApplicationScoped
import org.apache.hc.core5.net.URIBuilder

@ApplicationScoped
class ShortenerService(
  val repository: ShortenerRepository,
  val flavorMateProperties: FlavorMateProperties,
) {

  private val server
    get() = flavorMateProperties.server().url()

  fun findByShortPath(shortPath: String): String {
    val originalPath =
      repository.findByShortPath(shortPath)
        ?: throw FNotFoundException(message = "No shortened path found for $shortPath")

    return URIBuilder(server).appendPath(originalPath).toString()
  }

  fun generateUrl(originalPath: String): String {
    val shortenedPath =
      repository.findByOriginalPath(originalPath)
        ?: ShortenerEntity.create(originalPath).also { repository.persist(it) }.shortPath

    return URIBuilder(server).appendPathSegments("s", shortenedPath).toString()
  }
}
