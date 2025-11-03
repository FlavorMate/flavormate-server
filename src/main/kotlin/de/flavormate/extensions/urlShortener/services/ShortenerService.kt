/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.extensions.urlShortener.services

import de.flavormate.configuration.properties.FlavorMateProperties
import de.flavormate.exceptions.FNotFoundException
import de.flavormate.extensions.urlShortener.models.ShortenerEntity
import de.flavormate.extensions.urlShortener.repositories.ShortenerRepository
import jakarta.enterprise.context.ApplicationScoped
import java.net.URI

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

    return URI.create(server).resolve(originalPath).toString()
  }

  fun generateUrl(originalPath: String): String {
    val shortenedPath =
      repository.findByOriginalPath(originalPath)
        ?: ShortenerEntity.create(originalPath).also { repository.persist(it) }.shortPath

    return URI.create(server).resolve("s").resolve(shortenedPath).toString()
  }
}
