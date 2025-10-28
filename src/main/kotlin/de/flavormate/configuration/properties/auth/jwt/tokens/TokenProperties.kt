/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.configuration.properties.auth.jwt.tokens

import java.time.Duration

interface TokenProperties {
    fun duration(): Duration
}
