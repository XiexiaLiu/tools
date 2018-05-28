/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package com.futuredata.data.security.bean;

import com.futuredata.data.security.util.MD5Utils;
import com.futuredata.data.security.util.SecurityUtils;

import java.io.Serializable;

/**
 * 验证bean.
 *
 * @author kexin.ding
 * @create 2017-11-23 15:21
 **/
public class ValidateBean implements Serializable {

  private static final long serialVersionUID = 5992192485439920512L;

  //mac地址
  private String macAddr;
  //起始时间(单位：ms)
  private long start = System.currentTimeMillis();
  //有效期(默认30天)
  private long expired = 2592000000L;
  //是否永久(默认不永久)
  private boolean isForever = false;
  //密码（六位数字字母组合，默认123456）
  private String password = "123456";
  private String md5;

  public String getMacAddr() {
    return macAddr;
  }

  public void setMacAddr(String macAddr) {
    this.macAddr = macAddr;
  }

  public long getStart() {
    return start;
  }

  public void setStart(long start) {
    this.start = start;
  }

  public long getExpired() {
    return expired;
  }

  public void setExpired(long expired) {
    this.expired = expired;
  }

  public boolean getIsForever() {
    return isForever;
  }

  public void setIsForever(boolean forever) {
    isForever = forever;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getMd5() {
    return md5;
  }

  public void setMd5(String md5) {
    this.md5 = md5;
  }

  public ValidateBean() {
    super();
    this.macAddr = SecurityUtils.getLocalMac();
  }

  public ValidateBean(String macAddr) {
    this();
    this.macAddr = macAddr;
  }

  public ValidateBean(String macAddr, String password) {
    this(macAddr);
    this.password = password;
    this.md5 = MD5Utils.MD5(macAddr + password);
  }

  public ValidateBean(String macAddr, String password, long expired) {
    this(macAddr, password);
    this.expired = expired;
  }

  public ValidateBean(String macAddr, String password, long expired, boolean isForever) {
    this(macAddr, password, expired);
    this.isForever = isForever;
  }

  public ValidateBean(String macAddr, String password, long expired, boolean isForever, long start) {
    this(macAddr, password, expired, isForever);
    this.start = start;
  }


  /**
   * 重写toString()方法.
   */
  @Override
  public String toString() {
    String concent = "{\"md5\":\"" + md5 + "\", \"start\":\"" + start + "\", \"expired\":\"" + expired + "\", \"isForever\":\"" + isForever + "\"}";
    //System.out.println("msg长度：" + concent.length());

    if (concent.length() > 117) {
      System.out.println("密钥过长");
      return null;
    }

    return concent;
  }

}
