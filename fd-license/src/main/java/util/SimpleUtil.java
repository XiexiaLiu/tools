/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * 工具类.
 *
 * @author kexin.ding
 * @date 2017年9月7日
 */
public class SimpleUtil {

  /**
   * 随机生成密钥对(只保留公钥).
   */
  public static String genPubKey() {
    // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
    KeyPairGenerator keyPairGen = null;
    try {
      keyPairGen = KeyPairGenerator.getInstance("RSA");
    } catch (NoSuchAlgorithmException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // 初始化密钥对生成器，密钥大小为96-1024位
    keyPairGen.initialize(1024, new SecureRandom());
    // 生成一个密钥对，保存在keyPair中
    KeyPair keyPair = keyPairGen.generateKeyPair();
    // 得到私钥
    //RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
    // 得到公钥
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

    String publicKeyString = null;
    try {
      // 得到公钥字符串
      publicKeyString = Base64.encode(publicKey.getEncoded());
      // 得到私钥字符串
      //String privateKeyString = Base64.encode(privateKey.getEncoded());
    } catch (Exception e) {
      e.printStackTrace();
    }

    return publicKeyString;
  }


  /**
  * 生成随机密码
  *
  * @param pwdLen 生成的密码的总长度
  * @return 密码的字符串
  */
  public static String genRandomNum(int pwdLen) {
    // 35是因为数组是从0开始的，26个字母+10个数字
    final int maxNum = 36;
    int i; // 生成的随机数
    int count = 0; // 生成的密码的长度
    char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',  'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
    'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    StringBuffer pwd = new StringBuffer();
    Random r = new Random();
    while (count < pwdLen) {
      // 生成随机数，取绝对值，防止生成负数，
      i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1
      if (i >= 0 && i < str.length) {
        pwd.append(str[i]);
        count++;
      }
    }
    return pwd.toString();
  }

  /**
   * 警告消息框.
   */
  public static void alert(String msg) {
    if (msg.contains("成功")) {
      JOptionPane.showMessageDialog(null, msg, "结果", JOptionPane.INFORMATION_MESSAGE);
    } else {
      JOptionPane.showMessageDialog(null, msg, "结果", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * 消息确认框.
   */
  public static boolean confirm(String msg) {
    int n = JOptionPane.showConfirmDialog(null, msg, "询问", JOptionPane.YES_NO_OPTION);
    return n == 0;
  }

  /**
   * 消息提示框.
   */
  public static String prompt(String title, String msg, String defaultStr) {
    Object result = JOptionPane.showInputDialog(null, msg + "：\n", title, JOptionPane.YES_NO_CANCEL_OPTION, new ImageIcon("icon.png"), null, defaultStr);

    if (result == null) { //用正则来写
      System.exit(0);
    }

    return result.toString();
  }

  /**
   * 获取当前日期、时间.
   */
  public static String getDate(String dateStr, String pattern) {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    Date d = new Date();
    String result = sdf.format(d);
    if (dateStr != null) {
      try {
        sdf.setLenient(false);
        long timer1 = sdf.parse(dateStr).getTime();
        long timer2 = d.getTime();
        d = timer1 > timer2 ? d : null;
        result = sdf.format(d);
      } catch (Exception e) {
        //e.printStackTrace();
        return null;
      }
    }

    return result;
  }

  /**
   * 日期之差.
   */
  public static long getDiff(String start, String end, String pattern) {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    long diff = 0L;
    try {
      diff = sdf.parse(end).getTime() - sdf.parse(start).getTime();
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return diff;
  }

  /**
   * 获取本机mac地址：6A:00:E3:D7:49:C9（17位）
   */
  public static String getLocalMac() {
    String macAddr = null;
    try {
      String osName = getOsName();
      if (osName.startsWith("linux")) {
        return getUnixMACAddress();
      }

      InetAddress ia = InetAddress.getLocalHost();

      //获取网卡，获取地址
      byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
      StringBuffer sb = new StringBuffer("");
      for (int i = 0; i < mac.length; i++) {
        if (i != 0) {
          sb.append(":");
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
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (macAddr == null || macAddr.length() < 17) {
      macAddr = null;
    }

    return macAddr;
  }

  /**
   * Return Opertaion System Name;
   *
   * @return os name.
   */
  public static String getOsName() {
    String os = "";
    os = System.getProperty("os.name");
    return os.toLowerCase();
  }


  /**
   * 获取unix网卡的mac地址. 非windows的系统默认调用本方法获取.
   * 如果有特殊系统请继续扩充新的取mac地址方法.
   *
   * @return mac地址
   */
  public static String getUnixMACAddress() {
    String mac = null;
    BufferedReader bufferedReader = null;
    Process process = null;
    try {
      // linux下的命令，一般取eth0作为本地主网卡
      process = Runtime.getRuntime().exec("ifconfig"); //centos7不支持 TODO
      // 显示信息中包含有mac地址信息
      bufferedReader = new BufferedReader(new InputStreamReader(
              process.getInputStream()));
      String line = null;
      int index = -1;
      while ((line = bufferedReader.readLine()) != null) {
        // 寻找标示字符串[hwaddr]
        index = line.toLowerCase().indexOf("hwaddr");
        if (index >= 0) {// 找到了
          // 取出mac地址并去除2边空格
          mac = line.substring(index + "hwaddr".length() + 1).trim();
          break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (bufferedReader != null) {
          bufferedReader.close();
        }
      } catch (IOException e1) {
        e1.printStackTrace();
      }
      bufferedReader = null;
      process = null;
    }
    if (mac == null) {
      return getCentosMacAddress();
    }

    return mac;
  }

  /**
   * 获取centos7系统下mac地址.
   *
   * @return mac地址
   */
  public static String getCentosMacAddress() {

    String mac = null;
    BufferedReader bufferedReader = null;
    Process process = null;
    try {
      // linux下的命令，一般取eth0作为本地主网卡
      process = Runtime.getRuntime().exec("ifconfig eno16777736"); //centos7支持 TODO
      // 显示信息中包含有mac地址信息
      bufferedReader = new BufferedReader(new InputStreamReader(
              process.getInputStream()));
      String line = null;
      int index = -1;
      while ((line = bufferedReader.readLine()) != null) {
        // 寻找标示字符串[hwaddr]
        index = line.toLowerCase().indexOf("hwaddr");
        if (index >= 0) {// 找到了
          // 取出mac地址并去除2边空格
          mac = line.substring(index + "hwaddr".length() + 1).trim();
          break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (bufferedReader != null) {
          bufferedReader.close();
        }
      } catch (IOException e1) {
        e1.printStackTrace();
      }
      bufferedReader = null;
      process = null;
    }

    return mac;
  }

  public static void main(String[] args)
  {
    System.out.println("Operation System=" + getOsName());
    System.out.println("Mac Address=" + getUnixMACAddress());
    System.out.println("Mac Address=" + getCentosMacAddress());
  }

}