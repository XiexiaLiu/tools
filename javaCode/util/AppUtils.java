/*
 * @copyright 2014-2015 Future-data Inc. All rights reserved.
 */

package com.futuredata.base.core.util;

import com.futuredata.base.core.exception.FdParameterException;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 程序工具类，提供大量的便捷方法.
 *
 * @author haichao.sang
 */
public class AppUtils {
  
  private AppUtils() {
    throw new IllegalStateException("AppUtils class");
  }


  /**
   * 产生一个18位递增的id.
   *
   * @return UUID
   */
  public static String randomUUID() {
    return String.valueOf(IdWorker.getInstance(0L).nextId());
  }

  /**
   * md5加密.
   *
   * @param value 要加密的值
   * @return md5加密后的值
   */
  public static String md5Hex(String value) {
    return DigestUtils.md5Hex(value);
  }

  /**
   * sha1加密.
   *
   * @param value 要加密的值
   * @return sha1加密后的值
   */
  public static String sha1Hex(String value) {
    return DigestUtils.sha1Hex(value);
  }

  /**
   * sha256加密.
   *
   * @param value 要加密的值
   * @return sha256加密后的值
   */
  public static String sha256Hex(String value) {
    return DigestUtils.sha256Hex(value);
  }

  /**
   * 验证表单内容.
   * 
   * @param model 用户输入的表单内容
   * 
   */
  public static <T> void validateModel(T model) {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    // 验证Model
    Set<ConstraintViolation<T>> constraintViolations = validator.validate(model);

    // 存放错误信息
    StringBuilder errors = new StringBuilder();

    for (ConstraintViolation<T> constraintViolation : constraintViolations) {

      // 错误字段的名称和错误信息
      errors.append(String.format("{name:%s,message:%s,rowNo:%s}",
          constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage(),
          1 + ""));
    }

    // 表单内容校验为通过时，直接抛出异常，捕捉后统一处理
    if (errors.length() > 0) {
      throw new FdParameterException(StringUtils.trimComma(errors));
    }
  }

  /**
   * 验证表单内容.
   * 
   * @param forms 表单集合
   */
  public static <T> void validateModel(List<T> forms) {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    // 存放错误信息
    StringBuilder errors = new StringBuilder();

    for (int i = 0; i < forms.size(); i++) {
      T model = forms.get(i);
      // 验证Model
      Set<ConstraintViolation<T>> constraintViolations = validator.validate(model);
      for (ConstraintViolation<T> constraintViolation : constraintViolations) {
        // 错误字段的名称和错误信息
        errors.append(String.format("{name:%s,message:%s,rowNo:%s}",
            constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage(),
            (i + 1) + ""));
      }

    }

    // 表单内容校验为通过时，直接抛出异常，捕捉后统一处理
    if (errors.length() > 0) {
      throw new FdParameterException(StringUtils.trimComma(errors));
    }
  }

  /**
   * 对文件名进行编码,避免中文乱码.
   *
   * @param userAgent 用户客户端
   * @param fileName 文件名
   * 
   * @return 编码后的文件名
   */
  public static String encodeFileName(String userAgent, String fileName) {
    try {
      // 针对IE或者以IE为内核的浏览器：
      if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {

        fileName = URLEncoder.encode(fileName, "UTF-8");

      } else {
        // 非IE浏览器的处理：
        fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
      }
    } catch (UnsupportedEncodingException ex) {
      // 不做处理
    }

    return fileName;
  }

  /**
   * 获取工程路径.
   *
   * @return 工程路径
   */
  public static String getProjectPath() {
    if ("\\".equals(File.separator)) {
      return AppUtils.class.getClassLoader().getResource("").toString().replace("file:/", "");
    } else {
      return AppUtils.class.getClassLoader().getResource("").toString().replace("file:", "");
    }
  }

  /**
   * 获取Web路径.
   *
   * @return Web路径
   */
  public static String getWebPath() {
    String projectPath = getProjectPath();

    int webIndex = projectPath.indexOf("/WEB-INF/");
    if (webIndex != -1) {
      return projectPath.substring(0, webIndex);
    }
    return projectPath;
  }

  /**
   * 获取真实IP.
   * 
   * @param request HttpServletRequest
   * @return 真实IP
   */
  public static String getIpAddr(HttpServletRequest request) {

    if (request == null) {
      return "";
    }

    String ipString = request.getHeader("x-forwarded-for");
    if (StringUtils.isEmpty(ipString) || "unknown".equalsIgnoreCase(ipString)) {
      ipString = request.getHeader("Proxy-Client-IP");
    }
    if (StringUtils.isEmpty(ipString) || "unknown".equalsIgnoreCase(ipString)) {
      ipString = request.getHeader("WL-Proxy-Client-IP");
    }
    if (StringUtils.isEmpty(ipString) || "unknown".equalsIgnoreCase(ipString)) {
      ipString = request.getRemoteAddr();
    }

    // 多个路由时，取第一个非unknown的ip
    final String[] arr = ipString.split(",");
    for (final String str : arr) {
      if (!"unknown".equalsIgnoreCase(str)) {
        ipString = str;
        break;
      }
    }

    return ipString;
  }

}
