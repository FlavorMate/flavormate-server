/* Licensed under AGPLv3 2024 - 2025 */
package de.flavormate.utils

import java.text.DecimalFormat
import java.util.regex.Pattern
import kotlin.math.floor

object NumberUtils {
  fun beautify(value: Double): String? {
    return when {
      isInteger(value) -> return value.toInt().toString()
      value < 0 -> return null
      else -> DecimalFormat("0.00").format(value)
    }
  }

  fun tryParseDouble(value: String, defaultVal: Double): Double {
    return try {
      value.toDouble()
    } catch (e: NumberFormatException) {
      defaultVal
    }
  }

  fun isInteger(value: Double): Boolean {
    return (value == floor(value)) && !java.lang.Double.isInfinite(value)
  }

  fun convertExtendedFractionString(input: String): String {
    // Define a regular expression pattern to match fractions like "¼", "½", "⅓", "⅔", etc.
    val pattern =
      Pattern.compile("(\\d+)\\s*¼|¼|(\\d+)\\s*½|½|(\\d+)\\s*¾|¾|(\\d+)\\s*⅓|⅓|(\\d+)\\s*⅔|⅔")

    // Create a Matcher object to find matches in the input string.
    val matcher = pattern.matcher(input)

    // Iterate through the matches and replace them with the decimal equivalents.
    val result = StringBuilder()
    while (matcher.find()) {
      val match = matcher.group() // Get the matched text
      val decimalValue =
        when (match) {
          "¼" -> 0.25
          "½" -> 0.5
          "¾" -> 0.75
          "⅓" -> 1.0 / 3.0
          "⅔" -> 2.0 / 3.0
          else -> parseFraction(match)
        }

      matcher.appendReplacement(result, decimalValue.toString())
    }
    matcher.appendTail(result)

    return result.toString()
  }

  // Custom fraction parser
  private fun parseFraction(fraction: String): Double {
    val cleanedFraction = fraction.replace("\\s".toRegex(), "")
    val numerator = cleanedFraction.substring(0, cleanedFraction.length - 1).toInt()
    val fractionChar = cleanedFraction[cleanedFraction.length - 1]

    return when (fractionChar) {
      '¼' -> numerator + 0.25
      '½' -> numerator + 0.5
      '¾' -> numerator + 0.75
      '⅓' -> numerator + 1.0 / 3.0
      '⅔' -> numerator + 2.0 / 3.0
      else -> throw IllegalArgumentException("Invalid fraction: $fraction")
    }
  }
}
