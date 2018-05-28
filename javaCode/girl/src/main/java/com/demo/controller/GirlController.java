/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package com.demo.controller;

import com.demo.repository.GirlRespository;
import com.demo.domain.Girl;
import com.demo.service.GirlService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.annotation.Resource;

/**
 * girl控制层.
 *
 * @author kexin.ding
 * @create 2017-12-02 15:31
 **/
@RestController
@RequestMapping("/girls")
public class GirlController {

  @Autowired
  private GirlRespository girlRespository;
  @Resource
  private GirlService service;

  /**
   * 获取列表.
   */
  @GetMapping("")
  public List<Girl> getGirls() {
    return girlRespository.findAll();
  }

  /**
   * 查询单项.
   */
  @GetMapping("/{id}")
  public Girl getGirlById(@PathVariable("id") Integer id) {
    return girlRespository.findOne(id);
  }

  /**
   * 查询单项.
   */
  @GetMapping("/age")
  public List<Girl> getGirlByAge(
          @RequestParam("age1") String age1,
          @RequestParam("age2") String age2) {

    return girlRespository.findByAgeBetween(age1, age2);
  }

  /**
   * 新增单项.
   */
  @PostMapping("")
  public Girl addGirl(
          @RequestParam("age") String age,
          @RequestParam(value = "cupSize", defaultValue = "A") String cupSize) {

    Girl girl = new Girl(age, cupSize);


    return girlRespository.save(girl);
  }

  /**
   * 新增多项.
   */
  @PostMapping("/batch")
  public void addGirls() {

    service.addGrils();
  }

  /**
   * 更新单项.
   */
  @PutMapping("/{id}")
  public Girl updGrilById(
          @PathVariable("id") Integer id,
          @RequestParam("age") String age,
          @RequestParam("cupSize") String cupSize) {

    Girl girl = new Girl(age, cupSize);
    girl.setId(id);

    return girlRespository.save(girl);
  }

  /**
   * 删除单项.
   */
  @DeleteMapping("/{id}")
  public void delGrilById(@PathVariable("id") Integer id) {
      girlRespository.delete(id);
  }

}
