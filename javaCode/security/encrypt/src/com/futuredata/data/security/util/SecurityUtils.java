/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package com.futuredata.data.security.util;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 工具类.
 *
 * @author kexin.ding
 * @create 2017-11-24 9:52
 **/
public class SecurityUtils {

  /**
   * 获取本机mac地址：6A-00-E3-D7-49-C9（17位）
   */
  public static String getLocalMac() {
    String macAddr = null;
    try {
      InetAddress ia = InetAddress.getLocalHost();

      //获取网卡，获取地址
      byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
      StringBuffer sb = new StringBuffer("");
      for (int i = 0; i < mac.length; i++) {
        if (i != 0) {
          sb.append("-");
        }
        //字节转换为整数
        int temp = mac[i] & 0xff;
        String str = Integer.toHexString(temp);
        if (str.length() == 1) {
          sb.append("0");
        }
        sb.append(str);
      }
      macAddr = sb.toString().toUpperCase();
    } catch (UnknownHostException | SocketException e) {
      e.printStackTrace();
    }

    if (macAddr == null || macAddr.length() < 17) {
      macAddr = null;
    }

    return macAddr;
  }


  /**
   * 获取AES的Cipher.
   */
  public static Cipher getCipher(int mode, String key) {

    //mode =Cipher.DECRYPT_MODE or Cipher.ENCRYPT_MODE
    byte[] keyPtr = new byte[16];
    IvParameterSpec ivParam = new IvParameterSpec(keyPtr);
    byte[] passPtr = key.getBytes();

    try {
      Cipher mCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

      for (int i = 0; i < 16; i++) {
        if (i < passPtr.length) {
          keyPtr[i] = passPtr[i];
        } else {
          keyPtr[i] = 0;
        }
      }

      SecretKeySpec keySpec = new SecretKeySpec(keyPtr, "AES");
      mCipher.init(mode, keySpec, ivParam);

      return mCipher;
    } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * 生成文件.
   */
  public static File mkdirFiles(String filePath) throws IOException {

    File file = new File(filePath);

    if (!file.getParentFile().exists()) {
      file.getParentFile().mkdirs();
    }

    file.createNewFile();

    return file;
  }

  /**
   * 判断非空和null字符串.
   */
  public static boolean isNotNull(String str) {

    return str != null && !"".equals(str.trim()) && !"null".equals(str);
  }

}
