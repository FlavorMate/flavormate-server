/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.utils

import de.flavormate.shared.resourceBundles.AppMessages
import java.time.Duration
import org.apache.commons.lang3.StringUtils

object DurationUtils {
  fun beautify(duration: Duration, messageSource: AppMessages): String {
    val formattedParts = buildList {
      addTimeComponent(duration.toDaysPart(), messageSource.recipe_durationDays())
      addTimeComponent(duration.toHoursPart().toLong(), messageSource.recipe_durationHours())
      addTimeComponent(duration.toMinutesPart().toLong(), messageSource.recipe_durationMinutes())
      addTimeComponent(duration.toSecondsPart().toLong(), messageSource.recipe_durationSeconds())
    }

    return formattedParts.joinToString(" ")
  }

  private fun MutableList<String>.addTimeComponent(part: Long, label: String) {
    if (part > 0) {
      val value = StringUtils.leftPad(part.toString(), 2, '0')
      add("$value $label")
    }
  }
}
