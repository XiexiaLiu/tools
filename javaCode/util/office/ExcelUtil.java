package com.futuredata.base.core.util.office;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.futuredata.base.core.util.AppUtils;
import com.futuredata.base.core.util.StringUtils;



/**
 * 
 *
 * TODO
 *
 * @author yifan
 */
public class ExcelUtil {

  private final static Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);

  private final static int MAX_SHEET_NUM = 60000; // excel 03 单sheet不能超过65535

  /**
   * 生成excel 通过httpServelt输出流返回
   * 
   * @param response
   * @param excelName
   * @param sheetName
   * @param heads
   * @param headCodes
   * @param datas
   * @return
   */
  public static boolean createExcel(HttpServletRequest request, HttpServletResponse response,
      String excelName, String sheetName, String[] heads, String[] headCodes,
      List<Map<String, Object>> datas) {
    List<String> headList = new ArrayList<String>();
    List<List<Object>> dataList = new ArrayList<List<Object>>();

    for (int i = 0; i < heads.length; i++) {
      headList.add(heads[i]);
    }
    for (int k = 0; k < datas.size(); k++) {
      Map<String, Object> map = datas.get(k);
      List<Object> rowData = new ArrayList<Object>();
      for (int i = 0; i < headCodes.length; i++) {
        rowData.add(map.get(headCodes[i]));
      }
      dataList.add(rowData);
    }
    return createExcel(request, response, excelName, sheetName, headList, dataList);
  }
  /**
   * 生成excel 通过httpServelt输出流返回
   * 
   * @param response
   * @param excelName
   * @param sheetName
   * @param heads
   * @param headCodes
   * @param datas
   * @return
   */
  public static boolean createExcel(HttpServletRequest request, HttpServletResponse response,
      String excelName, String sheetName, List<String> headList,List<String> headCodes,
      List<Map<String, Object>> datas) {
    return createExcel(request, response, excelName, sheetName, headList, headCodes,datas,null,null,null);
  }
  public static boolean createExcel(HttpServletRequest request, HttpServletResponse response,
      String excelName, String sheetName, List<String> headList,List<String> headCodes,
      List<Map<String, Object>> datas,Integer rowNo,String tplExcel,String path) {
    List<List<Object>> dataList = new ArrayList<List<Object>>();

    for (int k = 0; k < datas.size(); k++) {
      Map<String, Object> map = datas.get(k);
      List<Object> rowData = new ArrayList<Object>();
      for (int i = 0; i < headCodes.size(); i++) {
        rowData.add(map.get(headCodes.get(i)));
      }
      dataList.add(rowData);
    }
    return createExcel(request, response, excelName, sheetName, headList, dataList,rowNo,tplExcel,path);
  }

  /**
   * 生成excel 通过httpServelt输出流返回
   * 
   * @param response
   * @param excelName
   * @param sheetName
   * @param headList
   * @param dataList
   * @return
   */
  public static boolean createExcel(HttpServletRequest request, HttpServletResponse response,
      String excelName, String sheetName, List<String> headList, List<List<Object>> dataList) {
    return createExcel(request, response, excelName,sheetName,headList, dataList,null,null,null);
  }
  public static boolean createExcel(HttpServletRequest request, HttpServletResponse response,
      String excelName, String sheetName, List<String> headList, List<List<Object>> dataList,Integer rowNo,String tplExcel,String path) {
    ExcelSheet sheet = new ExcelSheet();
    sheet.setSheetName(sheetName);
    sheet.setHeadList(headList);
    sheet.setDataList(dataList);
    return createExcelSheet(request, response, excelName,rowNo,tplExcel,path, sheet);
  }

  /**
   * 生成excel 通过httpServelt输出流返回
   *
   * TODO
   *
   * @param response
   * @param excelName
   * @param sheets
   * @return
   */
  public static boolean createExcelSheet(HttpServletRequest request, HttpServletResponse response,
      String excelName,Integer rowNo,String tplExcel, String path,ExcelSheet... sheets) {
    try {
      excelName = excelName+".xls";
      OutputStream os = null;
      if(null!=path){
        os = new FileOutputStream(new File(path+excelName));
      }else{
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename="
            + AppUtils.encodeFileName(request.getHeader("User-Agent"), excelName));
        response.setHeader("Content-Type", "application/force-download");
        response.setCharacterEncoding("utf-8");
        os = response.getOutputStream();
      }
      if (sheets.length > 0) {
        
        createExcelSheet(os, sheets,rowNo,  path,tplExcel);
      } else {
        return false;
      }

    } catch (IOException e) {
      LOGGER.error("IOException happen when createExcel to httpServletResponse", e);
    }
    return true;
  }

  /**
   * 考虑到版本兼容性，默认生成的是excel 03版本，即以.xls结尾的文件
   *
   * TODO
   *
   * @param outputStream
   * @param sheets
   */
  public static void createExcelSheet(OutputStream outputStream, ExcelSheet[] sheets,Integer rowNo, String path,String tplExcel) {
    createExcelSheet(outputStream, sheets, ExcelType.Excel03,rowNo, path,tplExcel);
  }

  public static void createExcelSheet(OutputStream outputStream, ExcelSheet[] sheets,
      ExcelType excelType,Integer rowNo, String path,String tplExcel) {
    Workbook workbook = null;
    try {
      if (excelType == ExcelType.Excel03) {
        workbook = null==tplExcel?new HSSFWorkbook():new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(tplExcel)));
        for (ExcelSheet sheet : sheets) {
          createSheet03(sheet, (HSSFWorkbook) workbook,rowNo,tplExcel);
        }
      } else if (excelType == ExcelType.Excel07) {
        workbook = new XSSFWorkbook();
        for (ExcelSheet sheet : sheets) {
          createSheet07(sheet, (XSSFWorkbook) workbook,rowNo);
        }
      }
      if (workbook != null) {
        workbook.write(outputStream);
      }
    } catch (Exception e) {
      LOGGER.error("createExcel workbook write is error");
    } finally {
      if(null!=outputStream && null != path){
        try {
          outputStream.close();
        } catch (IOException e) {
          LOGGER.error("createExcel workbook close is error for workbook close");
        }
      }
      if (workbook != null) {
        try {
          workbook.close();
        } catch (IOException e) {
          LOGGER.error("createExcel workbook close is error for workbook close");
        }
      }
    }
  }

  /**
   * 03版有单sheet行数限制
   *
   * TODO
   *
   * @param sheet
   * @param workbook
   */
  private static void createSheet03(ExcelSheet sheet, HSSFWorkbook workbook,Integer rowNo,String tplExcel) {
    int sheetDataSize = sheet.getDataList().size();
    int needSheetNum = sheetDataSize / MAX_SHEET_NUM + (sheetDataSize % MAX_SHEET_NUM > 0 ? 1 : 0);
    if (needSheetNum == 0) { // 无数据时也要生成
      needSheetNum = 1;
    }
    for (int i = 0; i < needSheetNum; i++) {
      HSSFSheet sonSheet = null==tplExcel?workbook.createSheet(sheet.getSheetName() + "_" + i):workbook.getSheetAt(0);
      createSheet(sonSheet, workbook, sheet.getInfoBeforeHead(), sheet.getHeadList(),
          sheet.getDataList(), i * MAX_SHEET_NUM,
          (i + 1) * MAX_SHEET_NUM > sheetDataSize ? sheetDataSize : (i + 1) * MAX_SHEET_NUM,rowNo);
    }
  }

  private static void createSheet07(ExcelSheet excelSheet, XSSFWorkbook workbook,Integer rowNo) {
    createSheet(workbook.createSheet(excelSheet.getSheetName()), workbook,
        excelSheet.getInfoBeforeHead(), excelSheet.getHeadList(), excelSheet.getDataList(), 0,
        excelSheet.getDataList().size(),rowNo);
  }

  /**
   * 自定义样式 生成sheet
   * 
   * @param sheet
   * @param workbook
   * @param headList
   * @param dataList
   * @param begin
   * @param end
   */
  private static void createSheet(Sheet sheet, Workbook workbook, String infoBeforeHead,
      List<String> headList, List<List<Object>> dataList, int begin, int end,Integer rowNo) {
    // 设置excel每列宽度
    // sheet.setColumnWidth(0, 8000);
    // sheet.setColumnWidth(1, 8500);
    rowNo = null==rowNo?sheet.getLastRowNum():rowNo;
    // 创建单元格样式
    CellStyle styleTitle = workbook.createCellStyle();
    // 设置标头样式
    styleTitle.setFillForegroundColor(HSSFColor.WHITE.index);
    styleTitle.setFillPattern(CellStyle.SOLID_FOREGROUND);
    styleTitle.setBorderBottom(CellStyle.BORDER_THIN);
    styleTitle.setBorderLeft(CellStyle.BORDER_THIN);
    styleTitle.setBorderRight(CellStyle.BORDER_THIN);
    styleTitle.setBorderTop(CellStyle.BORDER_THIN);
    styleTitle.setAlignment(CellStyle.ALIGN_CENTER);
    Font font = workbook.createFont();
    font.setFontName("微软雅黑");
    font.setFontHeightInPoints((short) 15);
    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
    styleTitle.setFont(font);// 设置字体

    // 设置正文样式
    CellStyle styleContent = workbook.createCellStyle();

    styleContent.setFillForegroundColor(HSSFColor.WHITE.index);
    styleContent.setFillPattern(CellStyle.SOLID_FOREGROUND);
    styleContent.setBorderBottom(CellStyle.BORDER_THIN);
    styleContent.setBorderLeft(CellStyle.BORDER_THIN);
    styleContent.setBorderRight(CellStyle.BORDER_THIN);
    styleContent.setBorderTop(CellStyle.BORDER_THIN);
    styleContent.setAlignment(CellStyle.ALIGN_CENTER);
    styleContent.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    styleContent.setWrapText(true);
    Font fontContent = workbook.createFont();
    fontContent.setFontName("宋体");
    fontContent.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
    styleContent.setFont(fontContent);

    int rowCount = null==rowNo?0:rowNo;
    // 设置标题栏之前的备注信息
    if (!StringUtils.isEmpty(infoBeforeHead)) {
      Row row = sheet.createRow(rowCount);
      row.setHeight((short) 400);
      Cell hcell = row.createCell(0);
      hcell.setCellStyle(styleTitle);
      hcell.setCellType(HSSFCell.CELL_TYPE_STRING);
      hcell.setCellValue(infoBeforeHead);
      sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 9)); // 合并单元格
      rowCount++;
    }

    // 设置标头
    if(null != headList && 0<headList.size()){
      Row hrow = sheet.createRow(rowCount);
      hrow.setHeight((short) 500);
      for (int i = 0; i < headList.size(); i++) {
        Cell hcell = hrow.createCell(i);
        hcell.setCellStyle(styleTitle);
        hcell.setCellType(HSSFCell.CELL_TYPE_STRING);
        hcell.setCellValue(headList.get(i));
        int len = headList.get(i).length();
        sheet.setColumnWidth((short) (headList.size()), (short) ((len + 3) * 256));
      }
    }
    if (!dataList.isEmpty()) {
      // 设置正文
      for (int j = begin; j < end; j++) {
        rowCount++;
        Row drow = sheet.createRow(rowCount);
        if(null != headList && 0<headList.size()){
          sheet.autoSizeColumn((short) j);
        }

        int count = 0;

        for (Object cellData : dataList.get(j)) {
          Cell dcell = drow.createCell(count);
          if(null != headList && 0<headList.size()){
            dcell.setCellStyle(styleContent);
          }
          dcell.setCellType(HSSFCell.CELL_TYPE_STRING);
          if (cellData != null) {
            dcell.setCellValue(cellData.toString());
          }
          count++;
        }
      }
    }
  }

  /**
   * 读取excel
   * 
   * 要求excel 中要读取的每个sheet组织形式相同，因为每个excel只会指定一个读取类 TODO
   *
   * @param stream 输入流
   * @param clazz 解析excel每行数据的class
   * @param workSheetName 指定读取的sheetName,不指定则遍历读取全部
   * @return
   */
  public static final <T extends ExcelRow> List<T> readExcel(InputStream stream, Class<T> clazz,
      String... workSheetName) {
    List<T> list = new ArrayList<T>();
    Workbook workbook = null;

    try {
      workbook = WorkbookFactory.create(stream);

      if (workSheetName.length > 0) {
        for (String sheetName : workSheetName) {
          Sheet sheet = workbook.getSheet(sheetName);
          for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row xssfRow = sheet.getRow(rowNum);
            if (xssfRow != null) {
              T t = clazz.newInstance();
              if (t.fillMyself(xssfRow)) {
                list.add(t);
              }
            }
          }
        }
      } else {
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
          Sheet sheet = workbook.getSheetAt(i);
          for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row xssfRow = sheet.getRow(rowNum);
            if (xssfRow != null) {
              T t = clazz.newInstance();
              if (t.fillMyself(xssfRow)) {
                list.add(t);
              }
            }
          }
        }
      }
    } catch (IOException | InstantiationException | IllegalAccessException
        | EncryptedDocumentException | InvalidFormatException e) {
      LOGGER.error(e.getMessage(), e);
    } finally {
      if (workbook != null) {
        try {
          workbook.close();
        } catch (IOException e) {
          LOGGER.error(e.getMessage(), e);
        }
      }
    }
    return list;
  }

  /**
   * 读取excel
   * 
   * 要求excel 中要读取的每个sheet组织形式相同，因为每个excel只会指定一个读取类 TODO
   *
   * @param stream 输入流
   * @param clazz 解析excel每行数据的class
   * @param workSheetName 指定读取的sheetName,不指定则遍历读取全部
   * @return
   */
  public static final <T> List<T> xlsToBean(InputStream stream, int rnum, Class<T> clazz,
      String... workSheetName) {
    List<T> list = new ArrayList<T>();
    Workbook workbook = null;

    try {
      workbook = WorkbookFactory.create(stream);
      if (workSheetName.length > 0) {
        for (String sheetName : workSheetName) {
          Sheet sheet = workbook.getSheet(sheetName);
          list = toList(sheet, list, clazz, rnum);
        }
      } else {
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
          Sheet sheet = workbook.getSheetAt(i);
          list = toList(sheet, list, clazz, rnum);
        }
      }
    } catch (IOException | InstantiationException | IllegalAccessException
        | EncryptedDocumentException | InvalidFormatException e) {
      LOGGER.error(e.getMessage(), e);
    } finally {
      if (workbook != null) {
        try {
          workbook.close();
        } catch (IOException e) {
          LOGGER.error(e.getMessage(), e);
        }
      }
    }
    return list;
  }

  private static <T> List<T> toList(Sheet sheet, List<T> list, Class<T> clazz, int rnum)
      throws InstantiationException, IllegalAccessException {
    Field[] formField = clazz.getDeclaredFields();
    for (int rowNum = rnum; null != sheet && rowNum <= sheet.getLastRowNum(); rowNum++) {
      Row xssfRow = sheet.getRow(rowNum);
      T t = clazz.newInstance();
      for (int j = 0; null != xssfRow && j < xssfRow.getLastCellNum(); j++) {
        if (null == xssfRow.getCell(j)) {
          continue;
        }
        formField[j].setAccessible(true);
        formField[j].set(t, xssfRow.getCell(j).toString());
      }
      list.add(t);
    }
    return list;
  }

  public static enum ExcelType {
    Excel03, Excel07;
  }
}
