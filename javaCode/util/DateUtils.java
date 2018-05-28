package com.futuredata.base.core.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * DateUtils : 日期工具类
 *
 */
public class DateUtils {

  public static String Format(Date date) {

    if (null == date) {
      return "";
    }
    return (new SimpleDateFormat("YYYY-MM-dd HH:mm:ss")).format(date);
  }

  /**
   * 默认格式标准化日期显示
   *
   * TODO
   *
   * @param date
   * @return
   */
  public static String getDafaultDateString(Date date) {
    if (null == date) {
      return "";
    }
    return (new SimpleDateFormat("YYYY-MM-dd HH:mm:ss")).format(date);
  }

  /**
   * 获取指定的date, 参看calendar api
   *
   * @param field
   * @param amount
   * @return
   */
  public static Date getDefineTime(int field, int amount) {
    Calendar calendar = Calendar.getInstance();
    calendar.add(field, amount);
    return calendar.getTime();
  }

  /**
   * 获取指定日期的起始值, e.g: 2014-11-11 10:11:22 -> 2014-11-11 00:00:00
   *
   * @param date
   * @return
   */
  public static Date getDateStartByDate(Date date) {

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);

    return calendar.getTime();
  }

  /**
   * 获取指定日期的结束值, e.g: 2014-11-11 10:11:22 -> 2014-11-11 23:59:59
   *
   * @param date
   * @return
   */
  public static Date getDateEndByDate(Date date) {

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);

    return calendar.getTime();
  }

  /**
   * 返回当前日期
   *
   * @return
   */
  public static Date getNow() {
    Calendar calendar = Calendar.getInstance();
    return calendar.getTime();
  }

  public static String getNow(String format) {
    Calendar calendar = Calendar.getInstance();
    return new SimpleDateFormat(format).format(calendar.getTime());
  }

  public static void main(String[] args) {

  }

  /**
   * 将日期格式如 Feb. 2,2016转成 2016-02-02
   *
   * TODO
   *
   * @param date
   * @return
   */
  public static String convertToNormarlDateFormat(String date) {
    if (date == null) {
      return date;
    }
    String[] splitByCom = date.split(",");
    if (splitByCom.length > 1) {
      String year = splitByCom[1].trim();
      String[] splitByDot = splitByCom[0].split("\\.");
      if (splitByDot.length > 1) {
        String day = splitByDot[1].trim();
        String month = null;
        switch (splitByDot[0].trim().toLowerCase()) {
          case "jan":
            month = "01";
            break;
          case "feb":
            month = "02";
            break;
          case "mar":
            month = "03";
            break;
          case "apr":
            month = "04";
            break;
          case "may":
            month = "05";
            break;
          case "jun":
            month = "06";
            break;
          case "jul":
            month = "07";
            break;
          case "aug":
            month = "08";
            break;
          case "sep":
            month = "09";
            break;
          case "oct":
            month = "10";
            break;
          case "nov":
            month = "11";
            break;
          case "dec":
            month = "12";
            break;
          default:
            break;
        }

        if (month != null) {
          day = Integer.valueOf(day) < 10 ? "0" + day : day;
          return year + "-" + month + "-" + day;
        }
      }
    }

    return date;
  }

  public static String loadMothNum(String mothCn) {
    if (null == mothCn) {
      return null;
    }
    if ("一月".equals(mothCn) || "jan".equals(mothCn.trim().toLowerCase())) {
      return "1";
    } else if ("二月".equals(mothCn) || "feb".equalsIgnoreCase(mothCn.trim())) {
      return "2";
    } else if ("三月".equals(mothCn) || "mar".equalsIgnoreCase(mothCn.trim())) {
      return "3";
    } else if ("四月".equals(mothCn) || "apr".equalsIgnoreCase(mothCn.trim())) {
      return "4";
    } else if ("五月".equals(mothCn) || "may".equalsIgnoreCase(mothCn.trim())) {
      return "5";
    } else if ("六月".equals(mothCn) || "jun".equalsIgnoreCase(mothCn.trim())) {
      return "6";
    } else if ("七月".equals(mothCn) || "jul".equalsIgnoreCase(mothCn.trim())) {
      return "7";
    } else if ("八月".equals(mothCn) || "aug".equalsIgnoreCase(mothCn.trim())) {
      return "8";
    } else if ("九月".equals(mothCn) || "sep".equalsIgnoreCase(mothCn.trim())) {
      return "9";
    } else if ("十月".equals(mothCn) || "oct".equalsIgnoreCase(mothCn.trim())) {
      return "10";
    } else if ("十一月".equals(mothCn) || "nov".equalsIgnoreCase(mothCn.trim())) {
      return "11";
    } else if ("十二月".equals(mothCn) || "dec".equalsIgnoreCase(mothCn.trim())) {
      return "12";
    } else {
      return null;
    }
  }

  /**
   * 字符串日期转换成中文格式日期
   * @param date  字符串日期 yyyy-MM-dd
   * @return  yyyy年MM月dd日
   * @throws Exception
   */
  public static String dateToCnDate(String date) {
      if(date == null || date.length()==0)
        return null;

      String result = "";
      String[]  cnDate = new String[]{"〇","一","二","三","四","五","六","七","八","九"};
      String ten = "十";
      String[] dateStr = date.split("-");
      if(dateStr.length<3 && date.length() == 8){
        dateStr = new String[3];
        dateStr[0] = date.substring(0, 4);
        dateStr[1] = date.substring(4, 6);
        dateStr[2] = date.substring(6, 8);
      }
      if(dateStr.length<3)
        return null;
      for (int i=0; i<dateStr.length; i++) {
          for (int j=0; j<dateStr[i].length(); j++) {
              String charStr = dateStr[i];
              String str = String.valueOf(charStr.charAt(j));
              if (charStr.length() == 2) {
                  if (charStr.equals("10")) {
                      result += ten;
                      break;
                  } else {
                      if (j == 0) {
                          if (charStr.charAt(j) == '1')
                              result += ten;
                          else if (charStr.charAt(j) == '0')
                              result += "";
                          else
                              result += cnDate[Integer.parseInt(str)] + ten;
                      }
                      if (j == 1) {
                          if (charStr.charAt(j) == '0')
                              result += "";
                           else
                              result += cnDate[Integer.parseInt(str)];
                      }
                  }
              } else {
                  result += cnDate[Integer.parseInt(str)];
              }
          }
          if (i == 0) {
              result += "年";
              continue;
          }
          if (i == 1) {
              result += "月";
              continue;
          }
          if (i == 2) {
              result += "日";
              continue;
          }
      }
      return result;
  }

}
