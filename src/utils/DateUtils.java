package utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {
  public static Date plusDays(Date date, int days) {
    return Date.from(LocalDate.from(date.toInstant()).plusDays(days).atStartOfDay(ZoneId.systemDefault()).toInstant());
  }
}
