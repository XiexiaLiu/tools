/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import com.sun.org.apache.xml.internal.security.utils.Base64;

/**.
 * <p>
 * license文件生成原理:
 * </p>
 * <p>
 * 现在很多J2EE应用都采用一个license文件来授权系统的使用，特别是在系统购买的早期，会提供有限制的license文件对系统进行限制，比如试用版有譬如IP、日期、最大用户数量的限制等。
 * </p>
 * <p>
 * 而license控制的方法又有很多，目前比较流行，只要设计的好就很难破解的方法就是采用一对密匙（私匙加密公匙解密）来生成License文件中的Sinature签名内容，
 * 再通过Base64或Hex来进行编码。
 * </p>
 */

/**
 * 生成公钥私钥对.
 * 
 * @author kexin.ding
 * @date 2017年9月1日
 */
public class KeyGenerater {

  private byte[] priKey;

  private byte[] pubKey;

  private String random;

  public KeyGenerater() {
    // TODO Auto-generated constructor stub
  }

  public KeyGenerater(String random) {
    this.random = random;
    generater();
  }

  /**
   * 生成公钥密钥.
   */
  public void generater() {
    try {
      KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");

      SecureRandom secrand = new SecureRandom();
      secrand.setSeed(random.getBytes()); // 初始化随机产生器

      keygen.initialize(1024, secrand);
      KeyPair keys = keygen.genKeyPair();
      PublicKey pubkey = keys.getPublic();
      PrivateKey prikey = keys.getPrivate();

      pubKey = Base64.encode(pubkey.getEncoded()).getBytes();
      // Base64.encodeToByte(pubkey.getEncoded());
      priKey = Base64.encode(prikey.getEncoded()).getBytes();
      // Base64.encodeToByte(prikey.getEncoded());

      System.out.println("pubKey = " + new String(pubKey));
      System.out.println("priKey = " + new String(priKey));

    } catch (java.lang.Exception e) {
      System.out.println("生成密钥对失败");
      e.printStackTrace();
    }

  }

  // 获取私钥
  public byte[] getPriKey() {
    return priKey;
  }

  // 获取公钥
  public byte[] getPubKey() {
    return pubKey;
  }

}