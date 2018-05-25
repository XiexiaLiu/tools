/*
 * @copyright 2014-2017 Future-data Inc. All rights reserved.
 */

package com.example.util;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 分页bean.
 *
 * @author kexin.ding
 * @create 2017-12-05 15:27
 **/
public class PageBean<T> extends PageInfo {

  // 当前页
  private Integer pageNow;
  // 每页显示的总条数
  private Integer pageSize;
  // 总条数
  private Integer totalNum;
  // 是否有下一页
  private Integer isMore;
  // 总页数
  private Integer totalPage;
  // 开始索引
  private Integer startIndex;
  // 分页结果
  private List<T> items;

  public PageBean() {
    super();
  }

  public PageBean(List<T> items) {
    //this(0, items.size(), items.size());
    this.items = items;
  }

  public PageBean(Integer pageNow, Integer pageSize, Integer totalNum) {
    super();
    this.pageNow = pageNow;
    this.pageSize = pageSize;
    this.totalNum = totalNum;
    this.totalPage = this.pageSize <= 0 ? 0 : (this.totalNum+this.pageSize-1)/this.pageSize;
    this.startIndex = this.pageSize <= 0 ? 0 : (this.pageNow-1)*this.pageSize;
    this.isMore = this.pageNow >= this.totalPage?0:1;
  }

  public Integer getpageNow() {
    return pageNow;
  }

  public void setpageNow(Integer pageNow) {
    this.pageNow = pageNow;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public Integer getTotalNum() {
    return totalNum;
  }

  public void setTotalNum(Integer totalNum) {
    this.totalNum = totalNum;
  }

  public Integer getIsMore() {
    return isMore;
  }

  public void setIsMore(Integer isMore) {
    this.isMore = isMore;
  }

  public Integer getTotalPage() {
    return totalPage;
  }

  public void setTotalPage(Integer totalPage) {
    this.totalPage = totalPage;
  }

  public Integer getStartIndex() {
    return startIndex;
  }

  public void setStartIndex(Integer startIndex) {
    this.startIndex = startIndex;
  }

  public List<T> getItems() {
    return items;
  }

  public void setItems(List<T> items) {
    this.items = items;
  }

}
