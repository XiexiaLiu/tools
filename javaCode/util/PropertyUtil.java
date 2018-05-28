package com.futuredata.court.px.core.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * @title PropertyUtil属性文件读写工具类
 * @description 
 * @author LiuJiefeng
 * @date 2016-04-19
 */ 

public class PropertyUtil {  
  
    /** 
     * 根据Key 读取Value 
     *  
     * @param key 
     * @return 
     */  
    public static String readData(final String PROPERTY_FILE,String key) {  
        Properties props = new Properties();  
        try {  
            InputStream in = new BufferedInputStream(new FileInputStream(  
                    PROPERTY_FILE));  
            props.load(in);  
            in.close();  
            String value = props.getProperty(key);  
            return value;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
  
      
    /** 
     * 修改或添加键值对 如果key存在，修改 反之，添加。 
     *  
     * @param key 
     * @param value 
     */  
    public static void writeData(final String PROPERTY_FILE,String key, String value) {  
        Properties prop = new Properties();  
        try {  
            File file = new File(PROPERTY_FILE);  
            if (!file.exists())  
                file.createNewFile();  
            InputStream fis = new FileInputStream(file);  
            prop.load(fis);  
            fis.close();//一定要在修改值之前关闭fis  
            OutputStream fos = new FileOutputStream(PROPERTY_FILE);  
            prop.setProperty(key, value);  
            prop.store(fos, "Update '" + key + "' value");  
            fos.close();  
        } catch (IOException e) {  
            System.err.println("Visit " + PROPERTY_FILE + " for updating "  
                    + value + " value error");  
        }  
    }  

    public static void main(String[] args) {  
        String a =PropertyUtil.readData("src/main/resources/editor/ckeditorplus.properties", "img_dir");
        System.out.println(a);
    }  
}  