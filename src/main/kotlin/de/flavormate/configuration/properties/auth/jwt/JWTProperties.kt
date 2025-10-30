/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.configuration.properties.auth.jwt

import de.flavormate.configuration.properties.auth.jwt.tokens.TokenProperties
import io.smallrye.config.ConfigMapping
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
@ConfigMapping(prefix = "flavormate.auth.jwt")
interface JWTProperties {
    fun issuer(): String

    fun refreshToken(): TokenProperties

    fun accessToken(): TokenProperties

    fun resetToken(): TokenProperties

    fun verifyToken(): TokenProperties

    fun bringToken(): TokenProperties

    fun shareToken(): TokenProperties
}
