/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package com.futuredata.base.core.license;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimerTask {
  
  private static long timer = 0L;
  
  /**
   * 创建定时任务,周期性（2s）清空待删除队列.
   */
  private static void executeClearPeriod() {
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    executor.scheduleAtFixedRate(new Runnable() {
      public void run() {
        timer += 2;
        if (timer % 60 == 0) { //3600
          System.out.println("修改license,写入剩余时间"+(3600-timer));
        }
      }
    }, 0L, 2L, TimeUnit.SECONDS);
    
  }

}