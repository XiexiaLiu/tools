/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package com.futuredata.data.security.encrypt;

import com.futuredata.data.security.util.SecurityUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;

/**
 * AES加密.
 *
 * @author kexin.ding
 * @create 2017-11-24 11:36
 **/
public class AesEncode {

  /**
   * 加密文件.
   */
  public static void encrypt(String srcFile, String destFile, String privateKey) {
    int BUFFER_SIZE = 8192;
    byte[] readBuffer = new byte[BUFFER_SIZE];
    Cipher enCipher = SecurityUtils.getCipher(Cipher.ENCRYPT_MODE, privateKey);

    if (enCipher == null) {
      return; //init failed.
    }

    CipherOutputStream fos = null;
    BufferedInputStream fis = null;
    int size;

    try {
      fos = new CipherOutputStream(new BufferedOutputStream(new FileOutputStream(destFile)), enCipher);
      fis = new BufferedInputStream(new FileInputStream(SecurityUtils.mkdirFiles(srcFile)));

      while((size = fis.read(readBuffer,0,BUFFER_SIZE)) >= 0) {
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
