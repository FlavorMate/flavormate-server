package de.flavormate.utils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtils {
	public static double tryParseDouble(String value, double defaultVal) {
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return defaultVal;
		}
	}

	public static String isDoubleInt(double value) {

		if ((value == Math.floor(value)) && !Double.isInfinite(value)) {
			return ((int) value) + "";
		} else {
			return value + "";
		}
	}

	public static String isPositive(String number) {
		if (tryParseDouble(number, -1) > 0) {
			return number;
		} else {
			return "";
		}
	}

	public static String convertExtendedFractionString(String input) {
		// Define a regular expression pattern to match fractions like "¼", "½", "⅓", "⅔", etc.
		Pattern pattern = Pattern
				.compile("(\\d+)\\s*¼|¼|(\\d+)\\s*½|½|(\\d+)\\s*¾|¾|(\\d+)\\s*⅓|⅓|(\\d+)\\s*⅔|⅔");

		// Create a Matcher object to find matches in the input string.
		Matcher matcher = pattern.matcher(input);

		// Iterate through the matches and replace them with the decimal equivalents.
		StringBuffer result = new StringBuffer();
		while (matcher.find()) {
			String match = matcher.group(); // Get the matched text
			double decimalValue;

			if (match.equals("¼")) {
				decimalValue = 0.25;
			} else if (match.equals("½")) {
				decimalValue = 0.5;
			} else if (match.equals("¾")) {
				decimalValue = 0.75;
			} else if (match.equals("⅓")) {
				decimalValue = 1.0 / 3.0;
			} else if (match.equals("⅔")) {
				decimalValue = 2.0 / 3.0;
			} else {
				decimalValue = parseFraction(match); // Parse the fraction
			}

			matcher.appendReplacement(result, String.valueOf(decimalValue));
		}
		matcher.appendTail(result);

		return result.toString();
	}

	// Custom fraction parser
	private static double parseFraction(String fraction) {
		String cleanedFraction = fraction.replaceAll("\\s", "");
		int numerator =
				Integer.parseInt(cleanedFraction.substring(0, cleanedFraction.length() - 1));
		char fractionChar = cleanedFraction.charAt(cleanedFraction.length() - 1);

		switch (fractionChar) {
			case '¼':
				return numerator + 0.25;
			case '½':
				return numerator + 0.5;
			case '¾':
				return numerator + 0.75;
			case '⅓':
				return numerator + 1.0 / 3.0;
			case '⅔':
				return numerator + 2.0 / 3.0;
			default:
				throw new IllegalArgumentException("Invalid fraction: " + fraction);
		}
	}

	public static String beautifyDouble(double value) {
		if (value % 1 == 0) {
			return (int) value + "";
		} else {
			return new DecimalFormat("0.00").format(value);
		}
	}
}


