package com.futuredata.base.core.util;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class AesUtils {

  /** 默认秘钥 */
  protected static final String KEY = "26852a8198de449eb3e12eb50eb8b1e5";

  /**
   * AES解密
   *
   * @param encryptValue 待解密内容
   * @param key 秘钥
   * @return
   * @throws Exception
   */
  protected static String decrypt(String encryptValue, String key) {
    try {
      return aesDecryptByBytes(base64Decode(encryptValue), key);
    } catch (Exception ex) {
      return null;
    }
  }

  protected static String decrypt(String encryptValue) throws Exception {
    return aesDecryptByBytes(base64Decode(encryptValue), KEY);
  }

  /**
   * AES加密
   *
   * @param value 待加密内容
   * @param key 秘钥
   * @return
   * @throws Exception
   */
  protected static String encrypt(String value, String key) throws Exception {
    try {
      return base64Encode(aesEncryptToBytes(value, key));
    } catch (Exception ex) {
      return null;
    }
  }

  protected static String encrypt(String value) throws Exception {
    return base64Encode(aesEncryptToBytes(value, KEY));
  }

  private static String base64Encode(byte[] bytes) {
    return Base64.getEncoder().encodeToString(bytes);
  }

  private static byte[] base64Decode(String base64Code) throws Exception {
    return base64Code == null ? null : Base64.getDecoder().decode(base64Code);
  }

  private static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    kgen.init(128, new SecureRandom(encryptKey.getBytes()));

    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));

    return cipher.doFinal(content.getBytes("utf-8"));
  }

  private static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    kgen.init(128, new SecureRandom(decryptKey.getBytes()));

    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
    byte[] decryptBytes = cipher.doFinal(encryptBytes);

    return new String(decryptBytes);
  }
}
