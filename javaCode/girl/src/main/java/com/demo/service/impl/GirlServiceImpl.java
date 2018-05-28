/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package com.demo.service.impl;

import com.demo.domain.Girl;
import com.demo.repository.GirlRespository;
import com.demo.service.GirlService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * girl业务实现类.
 *
 * @author kexin.ding
 * @create 2017-12-02 16:33
 **/
@Service
@Transactional
public class GirlServiceImpl implements GirlService {

  @Resource
  private GirlRespository respository;


  @Override
  public void addGrils() {
    Girl g1 = new Girl("20", "D");
    //g1.setId();
    respository.save(g1);

    Girl g2 = new Girl("23", "EE");
    respository.save(g2);

  }
}
