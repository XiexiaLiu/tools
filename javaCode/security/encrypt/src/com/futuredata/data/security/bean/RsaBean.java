/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package com.futuredata.data.security.bean;

import java.io.Serializable;
import java.security.interfaces.RSAPrivateKey;

/**
 * RSA密钥.
 *
 * @author kexin.ding
 * @create 2017-11-24 16:39
 **/
public class RsaBean implements Serializable {

  private static final long serialVersionUID = 5762570036639120942L;

  private RSAPrivateKey priKey;
  private byte[] resultBytes;

  public RsaBean() {
  }

  public RsaBean(RSAPrivateKey priKey, byte[] resultBytes) {
    this.priKey = priKey;
    this.resultBytes = resultBytes;
  }

  public RSAPrivateKey getPriKey() {
    return priKey;
  }

  public void setPriKey(RSAPrivateKey priKey) {
    this.priKey = priKey;
  }

  public byte[] getResultBytes() {
    return resultBytes;
  }

  public void setResultBytes(byte[] resultBytes) {
    this.resultBytes = resultBytes;
  }

}
