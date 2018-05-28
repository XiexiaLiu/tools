package com.futuredata.base.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZipUtils {

  // 日志
  private static final Logger logger = LoggerFactory.getLogger(ZipUtils.class);

  // 缓存的大小
  private static final int BUFFER_SIZE = 4096;

  private ZipUtils() {
    throw new IllegalStateException("ZipUtils class");
  }

  /**
   * 压缩文件。
   * 
   * @param zipFile 压缩后的文件
   * @param files 被压缩的问题
   */
  public static String zip(String zipFile, List<String> files) {

    byte[] buffer = new byte[BUFFER_SIZE];
    ZipOutputStream out = null;
    FileInputStream fis = null;
    try {
      out = new ZipOutputStream(new FileOutputStream(zipFile));
      for (String file : files) {
        File temp = new File(file);
        fis = new FileInputStream(temp);

        out.putNextEntry(new ZipEntry(temp.getName()));

        int len;
        // 读入需要下载的文件的内容，打包到zip文件
        while ((len = fis.read(buffer)) > 0) {
          out.write(buffer, 0, len);
        }
        out.closeEntry();
        fis.close();
      }
    } catch (IOException ex) {
      logger.error("文件压缩失败", ex);
    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (IOException ex) {
          logger.error("文件压缩失败", ex);
        }
      }
      if (fis != null) {
        try {
          fis.close();
        } catch (IOException ex) {
          logger.error("文件压缩失败", ex);
        }
      }
    }

    return zipFile;

  }
}
