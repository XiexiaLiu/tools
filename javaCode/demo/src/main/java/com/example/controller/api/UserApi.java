/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package com.example.controller.api;

import com.example.entity.User;
import com.example.service.UserService;
import com.example.util.PageBean;
import com.github.pagehelper.PageInfo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户 api.
 *
 * @author kexin.ding
 * @create 2017-12-04 10:53
 **/
@RestController
@RequestMapping("/api/users")
public class UserApi {

  @Resource
  private UserService service;

  @GetMapping("age/{name}")
  public String getAgeByName(@PathVariable("name") String name) {

    return service.selectAgeByName(name);
  }

  @GetMapping("")
  public PageInfo<User> getUsers(
          @RequestParam(value = "paeNow", defaultValue = "0") int pageNow,
          @RequestParam(value = "pageSize", defaultValue = "0") int pageSize) {

    return new PageInfo<>(service.getUsers(pageNow, pageSize));
  }

}
