/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.configuration.properties.general

import de.flavormate.configuration.properties.general.admin.AdminProperties
import de.flavormate.configuration.properties.general.highlights.HighlightsProperties

interface GeneralProperties {
    fun admin(): AdminProperties

    fun highlights(): HighlightsProperties
}
