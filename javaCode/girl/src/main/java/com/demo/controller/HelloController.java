/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package com.demo.controller;

import com.demo.GirlProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * hello控制层.
 *
 * @author kexin.ding
 * @create 2017-12-02 11:05
 **/
@RestController
@RequestMapping("/hello")
//等价于@Controller + @ResponsBody
public class HelloController {

  /*@Value("${cupSize}")
  private String cupSize;
  @Value("${age}")
  private int age;

  @Value("${content}")
  private String content;*/

  @Autowired
  private GirlProperties grilProperties;

  //@RequestMapping(value = {"/hello", "/hi"}, method = RequestMethod.GET)
  //method不写则默认get，不推荐
  //@RequestMapping(value = "/say/{id}", method = RequestMethod.GET)
  @GetMapping("say/{id}")
  public String say(
          @PathVariable(value = "id")String id,
          @RequestParam(value = "name", required = false, defaultValue = "no-one")String name) {
    //return grilProperties.getCupSize();
    return "id:"+ id+",name:"+name;
  }

}
