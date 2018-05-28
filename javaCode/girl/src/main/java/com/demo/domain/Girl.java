/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package com.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * .
 *
 * @author kexin.ding
 * @create 2017-12-02 15:18
 **/
@Entity
public class Girl {
  @Id
  @GeneratedValue
  private Integer id;

  private String age;
  private String cupSize;


  public Girl() {
  }

  public Girl(String age, String cupSize) {
    this.age = age;
    this.cupSize = cupSize;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getAge() {
    return age;
  }

  public void setAge(String age) {
    this.age = age;
  }

  public String getCupSize() {
    return cupSize;
  }

  public void setCupSize(String cupSize) {
    this.cupSize = cupSize;
  }
}
