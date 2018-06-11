/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package generator;

import java.util.List;
import java.util.Map;

/**
 * 页面生成.
 * 
 * @author kexin.ding
 * @date 2017年9月20日
 */
public class PageUtil {
  
  /**
   * 生成css.
   */
  public static String genCss(String cssName) {
    String css = "<style type='text/css'>";
    
    css += ".elegant-aero {margin-left:auto;margin-right:auto;max-width: 500px;background: #D2E9FF;padding: 20px 20px 20px 20px;font: 12px Arial, Helvetica, sans-serif;color: #666;}"
        + ".elegant-aero h1 {font: 24px 'Trebuchet MS', Arial, Helvetica, sans-serif;padding: 10px 10px 10px 20px;display: block;background: #C0E1FF;border-bottom: 1px solid #B8DDFF;margin: -20px -20px 15px;}"
        + ".elegant-aero h1>span {display: block;font-size: 11px;}"
        + ".elegant-aero label>span {float: left;margin-top: 10px;color: #5E5E5E;}"
        + ".elegant-aero label {display: block;margin: 0px 0px 5px;}"
        + ".elegant-aero label>span {float: left;width: 20%;text-align: right;padding-right: 15px;margin-top: 10px;font-weight: bold;}"
        + ".elegant-aero input[type='text'], .elegant-aero input[type='email'], .elegant-aero textarea, .elegant-aero select {color: #888;width: 70%;padding: 0px 0px 0px 5px;border: 1px solid #C5E2FF;background: #FBFBFB;outline: 0;-webkit-box-shadow:inset 0px 1px 6px #ECF3F5;box-shadow: inset 0px 1px 6px #ECF3F5;font: 200 12px/25px Arial, Helvetica, sans-serif;height: 30px;line-height:15px;margin: 2px 6px 16px 0px;}"
        + ".elegant-aero textarea{height:100px;padding: 5px 0px 0px 5px;width: 70%;}"
        + ".elegant-aero select {background: #fbfbfb url('down-arrow.png') no-repeat right;background: #fbfbfb url('down-arrow.png') no-repeat right;appearance:none;-webkit-appearance:none;-moz-appearance: none;text-indent: 0.01px;text-overflow: '';width: 70%;}"
        + ".elegant-aero .button{padding: 10px 30px 10px 30px;background: #66C1E4;border: none;color: #FFF;box-shadow: 1px 1px 1px #4C6E91;-webkit-box-shadow: 1px 1px 1px #4C6E91;-moz-box-shadow: 1px 1px 1px #4C6E91;text-shadow: 1px 1px 1px #5079A3;}"
        + ".elegant-aero .button:hover{background: #3EB1DD;}";
    
    css += "</style>";
    return css;
  }
  
  /**
   * 生成html格式语句.
   */
  public static String genHtml(String cssName, String htmlName) {
    return "<html><head><meta charset='utf-8'>" + genCss(cssName) + "</head><body>" + genForm(null) + "</body></html>";
  }
  
  /**
   * 生成简单表单.
   */
  private static String genForm(List<Map<String,Object>> list) {
    String content = "<form class='elegant-aero'>";
    
    content += "<h1>Contact Form<span>Please fill all the texts in the fields.</span></h1><label><span>Your Name :</span><input id='name' type='text' name='name' placeholder='Your Full Name' /></label><label><span>Your Email :</span><input id='email' type='email' name='email' placeholder='Valid Email Address' /></label><label><span>Message :</span><textarea id='message' name='message' placeholder='Your Message to Us'></textarea></label><label><span>Subject :</span><select name='selection'><option value='Job Inquiry'>Job Inquiry</option><option value='General Question'>General Question</option></select></label><label><span>&nbsp;</span><input type='button' class='button' value='Send' /></label>";
    
    content += "</form>";
    return content;
  }

}
