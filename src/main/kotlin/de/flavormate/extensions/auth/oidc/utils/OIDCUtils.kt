/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.auth.oidc.utils

import de.flavormate.utils.URLUtils

object OIDCUtils {
  const val OPENID_SUFFIX = "/.well-known/openid-configuration"

  fun cleanURL(url: String): String {
    val urlWithSuffix = if (url.endsWith(OPENID_SUFFIX)) url else url + OPENID_SUFFIX
    return URLUtils.cleanURL(urlWithSuffix)
  }
}
