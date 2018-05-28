/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package com.futuredata.data.security.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密.
 *
 * @author kexin.ding
 * @create 2017-11-27 13:56
 **/
public class MD5Utils {

  public static void Md5(String plainText) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(plainText.getBytes());
      byte b[] = md.digest();
      int i;
      StringBuffer buf = new StringBuffer("");
      for (int offset = 0; offset < b.length; offset++) {
        i = b[offset];
        if (i < 0) {
          i += 256;
        }
        if (i < 16) {
          buf.append("0");
        }
        buf.append(Integer.toHexString(i));
      }

      System.out.println("result: " + buf.toString()); //32位的加密
      System.out.println("result: " + buf.toString().substring(8, 24)); //16位的加密
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

  }

  // MD5加码。32位
  public static String MD5(String inStr) {
    MessageDigest md5 = null;
    try {
      md5 = MessageDigest.getInstance("MD5");
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    char[] charArray = inStr.toCharArray();
    byte[] byteArray = new byte[charArray.length];

    for (int i = 0; i < charArray.length; i++) {
      byteArray[i] = (byte) charArray[i];
    }

    byte[] md5Bytes = md5.digest(byteArray);
    StringBuffer hexValue = new StringBuffer("");
    for (int i = 0; i < md5Bytes.length; i++) {
      int val = ((int) md5Bytes[i]) & 0xff;
      if (val < 16) {
        hexValue.append("0");
      }
      hexValue.append(Integer.toHexString(val));
    }

    return hexValue.toString();
  }

}
