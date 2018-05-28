/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package com.futuredata.data.security.encrypt;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * RSA加密.
 *
 * @author kexin.ding
 * @create 2017-11-24 11:37
 **/
public class RsaEncode {

  /**
   * 加密
   */
  public static byte[] encrypt(RSAPublicKey publicKey, byte[] srcBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

    if (publicKey != null) {
      //Cipher负责完成加密或解密工作，基于RSA
      Cipher cipher = Cipher.getInstance("RSA");
      //根据公钥，对Cipher对象进行初始化
      cipher.init(Cipher.ENCRYPT_MODE, publicKey);
      byte[] resultBytes = cipher.doFinal(srcBytes);
      return resultBytes;
    }
    return null;
  }

}
