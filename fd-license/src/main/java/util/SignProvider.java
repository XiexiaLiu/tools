/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package util;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * 用公钥验证.
 * 
 * @author kexin.ding
 * @date 2017年9月1日
 */
public class SignProvider {

  private SignProvider() {}   
  
  /**
   * 校验数字签名,此方法不会抛出任务异常,成功返回true,失败返回false.
   * 
   * @param pubKeyText
   *            公钥,base64编码
   * @param plainText
   *            明文
   * @param signText
   *            数字签名的密文,base64编码
   * @return 校验成功返回true 失败返回false
   */
  @SuppressWarnings("restriction")
  public static boolean verify(byte[] pubKeyText, String plainText, byte[] signText) {

    try {
      // 解密由base64编码的公钥,并构造X509EncodedKeySpec对象   
      X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(Base64.decode(pubKeyText));
       
      // RSA对称加密算法
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");   
       
      // 取公钥匙对象   
      PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);   
       
      // 解密由base64编码的数字签名
      byte[] signed = Base64.decode(signText);  
       
      Signature signatureChecker = Signature.getInstance("MD5withRSA");   
      signatureChecker.initVerify(pubKey);
      signatureChecker.update(plainText.getBytes());
      
      // 验证签名是否正常
      if (signatureChecker.verify(signed)) {
        return true;   
      } else {
        return false;   
      }
    } catch (Throwable e) {   
      System.out.println("校验签名失败");   
      e.printStackTrace();   

      return false;   
    }
  }
   
}