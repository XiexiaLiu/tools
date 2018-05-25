/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package com.example.service.impl;

import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import com.example.util.DemoPageHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户 业务实现类.
 *
 * @author kexin.ding
 * @create 2017-12-04 10:43
 **/
@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserMapper mapper;

  //根据姓名读取年龄
  @Override
  public String selectAgeByName(String name) {

    return mapper.selectAgeByName(name);
  }

  @Override
  public List<User> getUsers(int pageNow, int pageSize) {
    //设置分页信息，分别是当前页数和每页显示的总记录数【记住：必须在mapper接口中的方法执行之前设置该分页信息】
    DemoPageHelper.start(pageNow, pageSize);

    return mapper.getUsers();
  }

}
