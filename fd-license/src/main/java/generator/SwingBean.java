/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package generator;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.futuredata.base.core.license.License;

import util.SimpleUtil;

/**
 * 图形界面.
 *
 * @author kexin.ding
 * @date 2017年9月22日
 */
public class SwingBean extends JFrame {

  private static final long serialVersionUID = -7078637480053438223L;

  private static String now = SimpleUtil.getDate(null, "yyyy-MM-dd HH:mm:ss");
  private static String[] regexs = {"([A-Fa-f0-9]{2}:){5}[A-Fa-f0-9]{2}","(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})","[a-zA-Z]:(((\\\\)|/)\\w+)(\\2\\w+)*(\\.\\w+)?","([\\s\\S]*)"};

  public SwingBean(String exeTitle, String panelTitle) {
      super(exeTitle);
      String blank = "<span style='color:#eeeeee;'><span style='color:#eeeeee'>X</span>1111111</span>";

      final JPanel root = new JPanel();
      //标题
      JLabel title = new JLabel("<html><h1 align='center' style='width:240px;'>" + panelTitle + "</h1></html>");

      //mac地址
      JLabel macAddrLabel = new JLabel("<html>"+blank+"<span style=''>MAC地址：</span></html>");
      final JTextField macAddrField = new JTextField(SimpleUtil.getLocalMac(), 12);

      //截止日期
      JLabel deadlineLabel = new JLabel("<html>"+blank+"<span style=''>截止日期：</span></html>");
      final JTextField deadlineField = new JTextField(now, 12);

      //文件绝对路径
      JLabel filePathLabel = new JLabel("<html>"+blank+"<span style=''>文件位置：</span></html>");
      final JTextField filePathField = new JTextField("c:/work", 12);

      //license名称
      JLabel fileNameLabel = new JLabel("<html>"+blank+"<span style=''>文件名称：</span></html>");
      final JTextField fileNameField = new JTextField("license.dat", 12);

      //按钮
      JButton submit = new JButton("确定");
      final JButton cancel = new JButton("取消");

      root.add(title);

      root.add(macAddrLabel);
      root.add(macAddrField);
      JLabel br1 = new JLabel("<html>"+blank+"</html>");
      root.add(br1);

      root.add(deadlineLabel);
      root.add(deadlineField);
      JLabel br2 = new JLabel("<html>"+blank+"</html>");
      root.add(br2);

      root.add(filePathLabel);
      root.add(filePathField);
      JLabel br3 = new JLabel("<html>"+blank+"</html>");
      root.add(br3);

      root.add(fileNameLabel);
      root.add(fileNameField);
      JLabel br4 = new JLabel("<html>"+blank+"</html>");
      root.add(br4);

      root.add(submit);
      root.add(cancel);
      add(root);
      setSize(360, 240);
      this.setIconImage(Toolkit.getDefaultToolkit().createImage(SwingBean.class.getResource("license.jpg")));
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setResizable(false);
      setVisible(true);
      setLocationRelativeTo(getOwner());  //界面居中
      //监听 提交+验证
      submit.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          if(verify(root)) {
            String macAddr = macAddrField.getText();
            String deadline = String.valueOf(SimpleUtil.getDiff(now, deadlineField.getText(),"yyyy-MM-dd HH:mm:ss"));
            String filePath = filePathField.getText();
            String fileName = fileNameField.getText();
            genLicense(macAddr, deadline, filePath, fileName);
          }
        }
      });
      //监听 取消+重置
      cancel.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          verify(cancel);
        }
      });
   }

  public static boolean verify(Object obj) {
    boolean flag = true;
    if (obj instanceof JButton) {
      System.exit(ABORT);
    }
    JPanel root = (JPanel)obj;
    int count = root.getComponentCount();
    for (int j = 2; j < count; j++) {//重置
      Component comp = root.getComponent(j);
      if (comp instanceof JLabel) {
        JLabel label = (JLabel)comp;
        label.setText(label.getText().replaceAll("red'", "#eeeeee'"));
      }
    }

    for (int i = 2; i < count; i++) {//2.5.8.11
      Component comp = root.getComponent(i);
      if(comp instanceof JTextField){
        JTextField text = (JTextField)comp;
        String value = text.getText();
        if (i == 5) { //时间有效性验证
          String deadline = SimpleUtil.getDate(value, "yyyy-MM-dd HH:mm:ss");
          if (deadline == null) {
            JLabel label = (JLabel)root.getComponent(i + 1);
            String desc = label.getText();
            label.setText(desc.replaceAll("#eeeeee'", "red'"));
            flag = false;
          }
        }

        if (!value.matches(regexs[(i-2)/3])) {
          JLabel label = (JLabel)root.getComponent(i + 1);
          String desc = label.getText();
          label.setText(desc.replaceAll("#eeeeee'", "red'"));
          flag = false;
        }
      }
    }
    return flag;
  }

  public static void genLicense(String macAddr,String deadline, String filePath, String fileName) {
  //公钥
  String pubKey = SimpleUtil.genPubKey();
  //license bean
  License license = new License(SimpleUtil.genRandomNum(32), macAddr, deadline);
  try {
    LicenseGen.save(license, pubKey, filePath, fileName);
  } catch (Exception e) {
    e.printStackTrace();
    //失败提示
    SimpleUtil.alert("生成license文件失败：\n" + e.getMessage());
    return;
  }

  //成功提示
  boolean flag = SimpleUtil.confirm("生成license文件成功，是否打开对应文件夹？");
  try {
    if (flag) {
      //打开生成文件的对应文件夹
      filePath = filePath.replaceAll("/", "\\\\") + "\\";
      Runtime.getRuntime().exec("explorer.exe " + filePath);
    }
  } catch (IOException e) {}

  }

}