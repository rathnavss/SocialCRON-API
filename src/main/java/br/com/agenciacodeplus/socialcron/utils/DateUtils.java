package br.com.agenciacodeplus.socialcron.utils;

import java.util.Date;
import java.util.Calendar;

public class DateUtils {

  private static int SUM = 0;
  private static int SUBTRACT = 1;

  private static Date manipulateDate(Date date, int type, Integer quantity, int operation) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);

    if (operation == 0) {
      c.set(type, c.get(type) + quantity);
    } else {
      c.set(type, c.get(type) - quantity);
    }

    return c.getTime();
  }

  public static Date sumDate(Date date, int type, Integer quantity) {
    return manipulateDate(date, type, quantity, SUM);
  }

  public static Date subtractDate(Date date, int type, Integer quantity) {
    return manipulateDate(date, type, quantity, SUBTRACT);
  }
  
  public static Integer stringToDateConstant(String str) {
    
    if (str == null || str.isEmpty()) {
      throw new IllegalArgumentException("The string cannot be null");
    }
 
    if (str.equalsIgnoreCase("minute")) {
      return Calendar.MINUTE;
    }
 
    if (str.equalsIgnoreCase("hour")) {
      return Calendar.HOUR_OF_DAY;
    }
 
    if (str.equalsIgnoreCase("day")) {
      return Calendar.DAY_OF_MONTH;
    }
 
    if (str.equalsIgnoreCase("week")) {
      return Calendar.WEEK_OF_MONTH;
    }
 
    if (str.equalsIgnoreCase("month")) {
      return Calendar.MONTH;
    }
 
    if (str.equalsIgnoreCase("year")) {
      return Calendar.YEAR;
    }
 
    throw new IllegalArgumentException("The string doesn't correspond to a constant");
 
  }
  
}
