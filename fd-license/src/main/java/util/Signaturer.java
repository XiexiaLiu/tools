package util;
/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */



import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * 使用私钥进行签名.
 * 
 * @author kexin.ding
 * @date 2017年9月1日
 */
public class Signaturer {

  /**
   * 签名.
   * 
   * @param priKeyText 私钥
   * @param plainText 明文
   */
  public static byte[] sign(byte[] priKeyText, String plainText) {

    try {
      PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(priKeyText));

      KeyFactory keyf = KeyFactory.getInstance("RSA");
      PrivateKey prikey = keyf.generatePrivate(priPKCS8);

      // 用私钥对信息生成数字签名
      Signature signet = Signature.getInstance("MD5withRSA");
      signet.initSign(prikey);
      signet.update(plainText.getBytes());

      byte[] signed = Base64.encode(signet.sign()).getBytes(); 
      // Base64.encodeToByte(signet.sign());

      return signed;

    } catch (Exception e) {
      System.out.println("签名失败");
      e.printStackTrace();
    }

    return null;
  }

}