/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * hello控制层(使用模版).
 *
 * @author kexin.ding
 * @create 2017-12-02 11:05
 **/
@Controller
public class HelloControllerTemplate {

  @RequestMapping(value = "/helloTemplate", method = RequestMethod.GET)
  public String say() {
    return "index";
  }

}
