/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.utils

import de.flavormate.exceptions.FBadRequestException
import org.apache.hc.core5.net.URIBuilder

object URLUtils {
  fun cleanURL(url: String): URIBuilder {
    try {
      val builder = URIBuilder(url)
      validateUrl(builder)

      val nonTrackingParams = builder.queryParams.filter { !it.name.startsWith("utm_") }
      builder.setParameters(nonTrackingParams)

      return builder.optimize()
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
