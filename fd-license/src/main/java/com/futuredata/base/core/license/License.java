/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package com.futuredata.base.core.license;

import java.io.IOException;
import java.io.ObjectOutputStream;

import util.Base64;

/**
 * license信息类（包名与base一致）.
 * 
 * @author kexin.ding
 * @date 2017年9月1日
 */
public class License implements java.io.Serializable {
  //与base对应license bean保持一致
  private static final long serialVersionUID = 5662313497082937475L;

  // 客户id
  private String id;
  // 硬件信息
  private String hardInfos;
  // 有效期限
  private String expirationDate;

  public License() {
    super();
  }

  public License(String id, String hardInfos, String expirationDate) {
    super();
    this.id = id;
    this.hardInfos = hardInfos;
    this.expirationDate = expirationDate;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getHardInfos() {
    return hardInfos;
  }

  public void setHardInfos(String hardInfos) {
    this.hardInfos = hardInfos;
  }

  public String getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(String expirationDate) {
    this.expirationDate = expirationDate;
  }

  private void writeObject(ObjectOutputStream oos) {
    try {
      // 有序写入！！！
      oos.writeObject(Base64.encode(expirationDate.getBytes("UTF-8")));
      oos.writeObject(Base64.encode(hardInfos.getBytes("UTF-8")));
      oos.writeObject(Base64.encode(id.getBytes("UTF-8")));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}