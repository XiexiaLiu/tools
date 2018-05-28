/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package com.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * girl配置.
 *
 * @author kexin.ding
 * @create 2017-12-02 13:34
 **/
@Component
@ConfigurationProperties(prefix = "girl")
public class GirlProperties {

  private String cupSize;
  private Integer age;

  public String getCupSize() {
    return cupSize;
  }

  public void setCupSize(String cupSize) {
    this.cupSize = cupSize;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }
}
