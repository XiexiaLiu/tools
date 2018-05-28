/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package com.futuredata.data.security.encrypt;

import com.futuredata.data.security.bean.RsaBean;
import com.futuredata.data.security.bean.ValidateBean;
import com.futuredata.data.security.util.MD5Utils;
import com.futuredata.data.security.util.SecurityUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.SimpleDateFormat;

/**
 * 文件加密.
 *
 * @author kexin.ding
 * @create 2017-11-24 11:37
 **/
public class FileEncrypt {

  /**
   * 加密.
   */
  public static void encode(ValidateBean bean, String rsaFile, String srcFile, String targetFile) throws Exception {

    String msg = bean.toString();
    //KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
    KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
    //初始化密钥对生成器，密钥大小为1024位
    SecureRandom random= SecureRandom.getInstance("SHA1PRNG");
    //random.setSeed(key.getBytes());
    keyPairGen.initialize(1024, random);
    //生成一个密钥对，保存在keyPair中
    KeyPair keyPair = keyPairGen.generateKeyPair();
    //得到私钥
    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

    //得到公钥
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

    //用公钥加密
    byte[] srcBytes = msg.getBytes("utf-8");

    byte[] resultBytes = RsaEncode.encrypt(publicKey, srcBytes);
    //id.rsa文件对应bean
    RsaBean rsaBean = new RsaBean(privateKey, resultBytes);
    writeObject(rsaBean, rsaFile);

    //System.out.println("RSA明文是:" + msg);
    //System.out.println("加密后是:" + new String(resultBytes, "utf-8"));

    String key = msg;
    AesEncode.encrypt(srcFile, targetFile, key);

  }

  /**
   * 将加密密钥写入文件中.
   */
  private static void writeObject(Object obj, String file) {
    try {
      FileOutputStream outStream = new FileOutputStream(file);
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);

      objectOutputStream.writeObject(obj);
      outStream.close();
    }catch (IOException e) {
      e.printStackTrace();
      System.out.println("写入RSA文件失败");
    }
  }

  public static void main(String[] args) throws Exception {
    //args = new String[]{"c:\\work\\Firefox-47.0.1.exe", "c:\\work\\id.rsa", "c:\\work\\123", null , "123", null, "true", null};

    if (args == null || args.length != 8) {
      System.out.println("params error");
      System.out.println("param1： sourceFilePath 源文件，不许为空");
      System.out.println("param2： rsaFilePath 加密密钥文件，不许为空");
      System.out.println("param3： targetFilePath 加密后的文件，不许为空");
      System.out.println("param4： macAddr mac地址，如\"6A-00-E3-D7-49-C9\"（17位）");
      System.out.println("param5： password 密码（六位数字字母组合，默认\"123456\"）");
      System.out.println("param6： expired 有效期(默认30天,单位：ms)");
      System.out.println("param7： isForever 是否永久(\"false\"默认不永久)");
      System.out.println("param8： start 起始时间(\"yyyy-MM-dd HH:mm:ss\")");
      return;
    }

    try {
      String sourceFilePath = args[0];
      String rsaFilePath = args[1];
      String targetFilePath = args[2];

      ValidateBean bean = new ValidateBean();
      if (SecurityUtils.isNotNull(args[3])) {
        bean.setMacAddr(args[3]);
      }
      if (SecurityUtils.isNotNull(args[4])) {
        bean.setPassword(args[4]);
      }
      if (SecurityUtils.isNotNull(args[5])) {
        bean.setExpired(Long.parseLong(args[5]));
      }
      if (SecurityUtils.isNotNull(args[6])) {
        bean.setIsForever(Boolean.parseBoolean(args[6]));
      }
      if (SecurityUtils.isNotNull(args[7])) {
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bean.setStart(format.parse(args[7]).getTime());
      }
      bean.setMd5(MD5Utils.MD5(bean.getMacAddr() + bean.getPassword()));

      System.out.println("加密中...");
      encode(bean, rsaFilePath, sourceFilePath, targetFilePath);
      System.out.println("加密成功！");

    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("加密失败！");
    }
  }

}
