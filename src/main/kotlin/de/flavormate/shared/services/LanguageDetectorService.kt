/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.shared.services

import de.flavormate.shared.enums.Language
import de.flavormate.shared.extensions.mapToSet
import jakarta.enterprise.context.ApplicationScoped
import org.apache.tika.langdetect.optimaize.OptimaizeLangDetector

@ApplicationScoped
class LanguageDetectorService {
  private val detector by lazy {
    OptimaizeLangDetector().loadModels(Language.entries.mapToSet { it.value })
  }

  fun getLanguage(input: String): Language? {
    val result = detector.detect(input)

    if (!result.isReasonablyCertain) return null

    return Language.from(result.language)
  }
}
