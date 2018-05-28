/*
 * @copyright 2014-2016 Future-data Inc. All rights reserved.
 */
package com.futuredata.base.core.util.office;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.futuredata.base.core.util.StringUtils;



/**
 *
 * TODO
 *
 * @author yifan
 */
public abstract class ExcelRow {
  public ExcelRow() {}

  public abstract boolean fillMyself(Row row);

  protected Object getValue(Cell cell) {
    if (cell == null) {
      return null;
    }
    if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
      // 返回布尔类型的值
      return cell.getBooleanCellValue();
    } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
      // 返回日期格式
      if (HSSFDateUtil.isCellDateFormatted(cell)) {
        SimpleDateFormat sdf = null;
        if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("yyyy/MM/dd")) {
          sdf = new SimpleDateFormat("yyyy/MM/dd");
        } else {// 日期
          sdf = new SimpleDateFormat("yyyy-MM-dd");
        }
        Date date = cell.getDateCellValue();
        return sdf.format(date);
      }
      // 返回数值类型的值
      return cell.getNumericCellValue();
    } else {
      // 返回字符串类型的值
      return cell.getStringCellValue();
    }
  }

  protected String getStringValue(Cell cell) {
    Object value = getValue(cell);
    if (StringUtils.isEmpty(value)) {
      return "";
    }
    return value.toString();
  }

  protected int getIntValue(Cell cell) {
    Object value = getValue(cell);
    if (StringUtils.isEmpty(value)) {
      return 0;
    }
    return Integer.valueOf(value.toString());
  }

  protected Double getDoubleValue(Cell cell) {
    Object value = getValue(cell);
    if (StringUtils.isEmpty(value)) {
      return 0.0;
    }
    return Double.valueOf(value.toString());
  }

  protected String getDateValue(Cell cell) {
    String defaultDate = "1990-01-01";
    Object value = getValue(cell);
    if (StringUtils.isEmpty(value)) {
      return defaultDate;
    }
    return value.toString();
  }
}
