/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package com.example.entity;

/**
 * 用户bean.
 *
 * @author kexin.ding
 * @create 2017-12-04 10:14
 **/
public class User {

    private String name;//用户姓名
    private String age;//用户年龄
    private String password;//用户密码

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getAge() {
      return age;
    }

    public void setAge(String age) {
      this.age = age;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

}
