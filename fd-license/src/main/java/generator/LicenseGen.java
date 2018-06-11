/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package generator;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.futuredata.base.core.license.License;

import util.Base64;
import util.SimpleUtil;

/**
 * License文件生成（程序入口）.
 *
 * @author kexin.ding
 * @date 2017年9月7日
 */
public class LicenseGen {

  public static void main(String[] args) {

    /*boolean flag = genLicense();
    while(!flag) {
      flag = genLicense();
    }*/
    new SwingBean("license生成器", "生成器");

  }

  /**
   * 生成器.
   */
  private static boolean genLicense() {
    //Mac地址
    String macAddr = SimpleUtil.prompt("step1", "请输入mac地址", SimpleUtil.getLocalMac());
    String regex1 = "([A-Fa-f0-9]{2}:){5}[A-Fa-f0-9]{2}";
    while(!macAddr.matches(regex1)) {
      macAddr = SimpleUtil.prompt("格式错误","请重新输入mac地址", macAddr);
    }

    //截止日期
    String now = SimpleUtil.getDate(null, "yyyy-MM-dd HH:mm:ss");
    String deadline = SimpleUtil.prompt("step2", "请输入授权截止日期", now);
    while(SimpleUtil.getDate(deadline, "yyyy-MM-dd HH:mm:ss") == null) {
      deadline = SimpleUtil.prompt("日期错误","请重新输入授权截止日期", deadline);
    }
    deadline = String.valueOf(SimpleUtil.getDiff(now, deadline, "yyyy-MM-dd HH:mm:ss"));

    //文件绝对路径
    String filePath = SimpleUtil.prompt("step3","请输入你要保存license文件路径", "c:/work");
    //文件名称
    String fileName = SimpleUtil.prompt("step4","请输入你要保存license文件名称", "license_"+ System.currentTimeMillis() + ".dat");

    //公钥
    String pubKey = SimpleUtil.genPubKey();
    //license bean
    License license = new License(SimpleUtil.genRandomNum(32), macAddr, deadline);
    try {
      save(license, pubKey, filePath, fileName);
    } catch (Exception e) {
      e.printStackTrace();
      //失败提示
      return SimpleUtil.confirm("生成license文件失败:\n" + e.getMessage() +"\n是否退出？");
    }

    //成功提示
    SimpleUtil.alert("生成license文件成功");
    try {
      //打开生成文件的对应文件夹
      filePath = filePath.replaceAll("/", "\\\\") + "\\";
      Runtime.getRuntime().exec("explorer.exe " + filePath);
    } catch (IOException e) {}

    return true;
  }

  /**
   * 保存license文件.
   */
  public static void save(License license, String pubKey, String filePath, String fileName) throws Exception {
    File dir = new File(filePath);
    if (!dir.exists()) {
      dir.mkdirs();
    }
    File file = new File(filePath, fileName);
    /*if (file.exists()) {
      file.delete();
    }*/
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
        throw new Exception("创建文件失败！");
      }
    }

    //存储文件全路径
    String path = filePath + "/" + fileName;
    // write
    BufferedOutputStream bufferedOut = new BufferedOutputStream(new FileOutputStream(path)); // 文件路径
    ObjectOutputStream out = new ObjectOutputStream(bufferedOut);

    out.writeObject(license);
    out.writeObject(Base64.encode(pubKey.getBytes()));

    out.flush();
    out.close();
  }

}