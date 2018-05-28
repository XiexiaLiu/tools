/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package com.demo.repository;

import com.demo.domain.Girl;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * girl接口.
 *
 * @author kexin.ding
 * @create 2017-12-02 15:34
 **/
public interface GirlRespository extends JpaRepository<Girl, Integer>{

  //带条件查询
  List<Girl> findByAgeBetween(String age1, String age2);

}
