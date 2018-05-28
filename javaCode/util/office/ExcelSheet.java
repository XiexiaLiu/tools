/*
 * @copyright 2014-2016 Future-data Inc. All rights reserved.
 */
package com.futuredata.base.core.util.office;

import java.util.List;

/**
 *
 * TODO
 *
 * @author yifan
 */
public class ExcelSheet {
  private String sheetName;
  private List<String> headList;
  private List<List<Object>> dataList;

  private String infoBeforeHead;

  public ExcelSheet() {}

  public ExcelSheet(String sheetName, List<String> headList, List<List<Object>> dataList) {
    this.sheetName = sheetName;
    this.headList = headList;
    this.dataList = dataList;
  }

  public String getSheetName() {
    return sheetName;
  }

  public void setSheetName(String sheetName) {
    this.sheetName = sheetName;
  }

  public List<String> getHeadList() {
    return headList;
  }

  public void setHeadList(List<String> headList) {
    this.headList = headList;
  }

  public List<List<Object>> getDataList() {
    return dataList;
  }

  public void setDataList(List<List<Object>> dataList) {
    this.dataList = dataList;
  }

  public String getInfoBeforeHead() {
    return infoBeforeHead;
  }

  public void setInfoBeforeHead(String infoBeforeHead) {
    this.infoBeforeHead = infoBeforeHead;
  }


}
