/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.utils

import de.flavormate.exceptions.FBadRequestException
import org.apache.hc.core5.net.URIBuilder

object URLUtils {
  /**
   * Removes tracking query parameters from the given URL.
   *
   * @param url the input URL to be cleaned
   * @return a cleaned URL string with tracking query parameters removed
   * @throws FBadRequestException if the given URL is invalid
   */
  fun cleanURL(url: String): String {
    try {
      val builder = URIBuilder(url)
      validateUrl(builder)

      val nonTrackingParams = builder.queryParams.filter { !it.name.startsWith("utm_") }
      builder.setParameters(nonTrackingParams)

      return builder.toString()
    } catch (_: Exception) {
      throw FBadRequestException(message = "Invalid url")
    }
  }

  fun validateUrl(builder: URIBuilder) {
    if (
      builder.scheme == null ||
        !builder.scheme.matches(Regex("^https?$")) ||
        builder.host.isNullOrBlank()
    ) {
      throw FBadRequestException(message = "Invalid url")
    }
  }
}
