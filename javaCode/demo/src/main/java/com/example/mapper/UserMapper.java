/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package com.example.mapper;

//import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
import com.example.entity.User;

import java.util.List;

/**
 * 用户 mapper映射.
 *
 * @author kexin.ding
 * @create 2017-12-04 10:38
 **/
//@Mapper//说明这是一个mapper
//@Component
public interface UserMapper {

  //根据姓名读取年龄
  String selectAgeByName(@Param("name") String name);

  //获取所有user列表
  List<User> getUsers();



}
