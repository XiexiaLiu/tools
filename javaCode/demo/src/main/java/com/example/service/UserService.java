/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package com.example.service;

import com.example.entity.User;

import java.util.List;

/**
 * 用户 业务层.
 *
 * @author kexin.ding
 * @create 2017-12-04 10:41
 **/
public interface UserService {

  //根据姓名读取年龄
  String selectAgeByName(String name);

  //获取所有user列表
  List<User> getUsers(int pageNow, int pageSize);

}
