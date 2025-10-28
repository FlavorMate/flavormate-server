/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.configuration.properties.server

interface ServerProperties {
    fun url(): String
    fun path(): String
    fun port(): Int
}
