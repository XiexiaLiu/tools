/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package com.futuredata.data.security.decrypt;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * RSA解密.
 *
 * @author kexin.ding
 * @create 2017-11-24 11:38
 **/
public class RsaDecode {

  /**
   * 解密
   */
  public static byte[] decrypt(RSAPrivateKey privateKey, byte[] srcBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    if (privateKey != null) {
      //Cipher负责完成加密或解密工作，基于RSA
      Cipher cipher = Cipher.getInstance("RSA");
      //根据公钥，对Cipher对象进行初始化
      cipher.init(Cipher.DECRYPT_MODE, privateKey);
      byte[] resultBytes = cipher.doFinal(srcBytes);
      return resultBytes;
    }
    return null;
  }

}
