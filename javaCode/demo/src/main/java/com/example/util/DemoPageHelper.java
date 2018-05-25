/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package com.example.util;

import com.github.pagehelper.PageHelper;

/**
 * 分页.
 *
 * @author kexin.ding
 * @create 2017-12-06 13:57
 **/
public class DemoPageHelper extends PageHelper {

  /**
   * 开始分页.
   */
  public static void start(Integer pageNow, Integer pageSize) {
    pageSize = pageSize < 0 ? 0 : pageSize;

    int pageNum = pageSize == 0 ? 1 : pageNow/pageSize + 1;

    startPage(pageNum, pageSize);
  }

  /**
   * 开始分页(从1开始).
   */
  public static void start2(Integer pageNow, Integer pageSize) {

    start(++pageNow, pageSize);
  }

}
