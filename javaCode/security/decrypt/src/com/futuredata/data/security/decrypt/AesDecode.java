/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package com.futuredata.data.security.decrypt;

import com.futuredata.data.security.util.SecurityUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;

/**
 * AES解密.
 *
 * @author kexin.ding
 * @create 2017-11-24 11:38
 **/
public class AesDecode {

  /**
   * 解密文件.
   */
  public static void decrypt(String srcFile, String destFile, String privateKey) {
    byte[] readBuffer = new byte[8192];

    Cipher deCipher = SecurityUtils.getCipher(Cipher.DECRYPT_MODE, privateKey);

    if (deCipher == null) {
      return; //init failed.
    }

    CipherInputStream fis = null;
    BufferedOutputStream fos = null;
    int size;

    try {
      fis = new CipherInputStream(new BufferedInputStream(new FileInputStream(srcFile)), deCipher);
      fos = new BufferedOutputStream(new FileOutputStream(SecurityUtils.mkdirFiles(destFile)));

      while ((size = fis.read(readBuffer, 0, 8192)) >= 0) {
        fos.write(readBuffer, 0, size);
      }

      fos.flush();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (fis != null) {
          fis.close();
        }
        if (fos != null) {
          fos.flush();
          fos.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

}