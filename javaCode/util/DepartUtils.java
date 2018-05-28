package com.futuredata.base.core.util;

public class DepartUtils {

  /**
   * 是否为高级法院
   * 
   * @param departId
   * @return
   */
  public static boolean isHighDepart(String departId) {
    if (StringUtils.isEmpty(departId) || departId.length() != 6) {
      return false;
    }
    return departId.endsWith("0000");
  }

  /**
   * 是否为中级法院
   * 
   * @param departId
   * @return
   */
  public static boolean isMidDepart(String departId) {
    if (StringUtils.isEmpty(departId) || departId.length() != 6) {
      return false;
    }
    return !departId.endsWith("0000") && departId.endsWith("00");
  }

  /**
   * 是否为基层法院
   * 
   * @param departId
   * @return
   */
  public static boolean isBaseDepart(String departId) {
    if (StringUtils.isEmpty(departId) || departId.length() != 6) {
      return false;
    }
    return !departId.endsWith("00");
  }

}
