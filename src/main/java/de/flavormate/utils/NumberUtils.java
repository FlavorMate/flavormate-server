/* Licensed under AGPLv3 2024 */
package de.flavormate.utils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtils {

  public static String beautify(double value) {
    if (isInteger(value)) {
      return (int) value + "";
    } else if (value < 0) {
      return "";
    } else {
      return new DecimalFormat("0.00").format(value);
    }
  }

  public static double tryParseDouble(String value, double defaultVal) {
    try {
      return Double.parseDouble(value);
    } catch (NumberFormatException e) {
      return defaultVal;
    }
  }

  public static boolean isInteger(double value) {
    return (value == Math.floor(value)) && !Double.isInfinite(value);
  }

  public static String convertExtendedFractionString(String input) {
    // Define a regular expression pattern to match fractions like "¼", "½", "⅓", "⅔", etc.
    Pattern pattern =
        Pattern.compile("(\\d+)\\s*¼|¼|(\\d+)\\s*½|½|(\\d+)\\s*¾|¾|(\\d+)\\s*⅓|⅓|(\\d+)\\s*⅔|⅔");

    // Create a Matcher object to find matches in the input string.
    Matcher matcher = pattern.matcher(input);

    // Iterate through the matches and replace them with the decimal equivalents.
    var result = new StringBuilder();
    while (matcher.find()) {
      String match = matcher.group(); // Get the matched text
      double decimalValue =
          switch (match) {
            case "¼" -> 0.25;
            case "½" -> 0.5;
            case "¾" -> 0.75;
            case "⅓" -> 1.0 / 3.0;
            case "⅔" -> 2.0 / 3.0;
            default -> parseFraction(match); // Parse the fraction
          };

      matcher.appendReplacement(result, String.valueOf(decimalValue));
    }
    matcher.appendTail(result);

    return result.toString();
  }

  // Custom fraction parser
  private static double parseFraction(String fraction) {
    String cleanedFraction = fraction.replaceAll("\\s", "");
    int numerator = Integer.parseInt(cleanedFraction.substring(0, cleanedFraction.length() - 1));
    char fractionChar = cleanedFraction.charAt(cleanedFraction.length() - 1);

    return switch (fractionChar) {
      case '¼' -> numerator + 0.25;
      case '½' -> numerator + 0.5;
      case '¾' -> numerator + 0.75;
      case '⅓' -> numerator + 1.0 / 3.0;
      case '⅔' -> numerator + 2.0 / 3.0;
      default -> throw new IllegalArgumentException("Invalid fraction: " + fraction);
    };
  }
}
