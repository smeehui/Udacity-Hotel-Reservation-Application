package utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {
  private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd");

  public static Date plusDays(Date date, int days) {
    return fromLocalDate(toLocalDate(date).plusDays(days));
  }

  public static LocalDate toLocalDate(Date date) {
    return LocalDate.from(date.toInstant().atZone(ZoneId.systemDefault()));
  }

  public static String toString(Date date) {
    return date.toInstant().toString().substring(0, 10);
  }

  public static Date fromLocalDate(LocalDate date) {
    return Date.from(date.atTime(1,0).toInstant(ZoneOffset.UTC));
  }

  public static Date fromString(String input) {
    return fromLocalDate(LocalDate.parse(input, DEFAULT_DATE_FORMATTER));
  }

  public static boolean isDateBetween(Date dateToCheck, Date start, Date end) {
    return dateToCheck.equals(start)
        || dateToCheck.equals(end)
        || (dateToCheck.after(start) && dateToCheck.before(end));
  }
}
