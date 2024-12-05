/* Licensed under AGPLv3 2024 */
package de.flavormate.utils;

import java.time.Duration;
import java.util.ArrayList;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public abstract class DurationUtils {
  private static String twoDigits(long n) {
    if (n == 0) return "";
    if (n >= 10) return n + "";
    return "0" + n;
  }

  public static String beautify(Duration duration, MessageSource messageSource) {
    var list = new ArrayList<String>();
    long days = duration.toDaysPart();
    long hours = duration.toHoursPart();
    long minutes = duration.toMinutesPart();
    long seconds = duration.toSecondsPart();

    if (days > 0) {
      var daysStr =
          messageSource.getMessage(
              "public-recipe_duration_days", null, LocaleContextHolder.getLocale());
      list.add(twoDigits(days) + " " + daysStr);
    }
    if (hours > 0) {
      var hoursStr =
          messageSource.getMessage(
              "public-recipe_duration_hours", null, LocaleContextHolder.getLocale());
      list.add(twoDigits(hours) + " " + hoursStr);
    }
    if (minutes > 0) {
      var minutesStr =
          messageSource.getMessage(
              "public-recipe_duration_minutes", null, LocaleContextHolder.getLocale());
      list.add(twoDigits(minutes) + " " + minutesStr);
    }
    if (seconds > 0) {
      var secondsStr =
          messageSource.getMessage(
              "public-recipe_duration_seconds", null, LocaleContextHolder.getLocale());
      list.add(twoDigits(seconds) + " " + secondsStr);
    }

    return String.join(" ", list);
  }
}
