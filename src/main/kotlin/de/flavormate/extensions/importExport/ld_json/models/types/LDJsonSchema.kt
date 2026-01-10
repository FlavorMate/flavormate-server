/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.extensions.importExport.ld_json.models.types

import com.fasterxml.jackson.annotation.JsonProperty

open class LDJsonSchema(@JsonProperty("@type") open val type: String?) {

  @JsonProperty("@context") val context: String = "https://schema.org"
}
