package com.example;

import com.github.pagehelper.PageHelper;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

@SpringBootApplication//自动加载配置文件 因为配置了插件maven会自动扫描resources下面的yml文件，如果用了properties，则扫描的是application.properties
@EnableTransactionManagement//开启事务管理
@MapperScan("com.example.mapper")//扫描包，必须加这个，不加报错，如果不加，也可以在每个mapper上添加@Mapper注释
public class DemoApplication {

	//配置mybatis的分页插件pageHelper
	@Bean
	public PageHelper pageHelper(){
		PageHelper pageHelper = new PageHelper();
		Properties properties = new Properties();
		properties.setProperty("offsetAsPageNum","true");
		properties.setProperty("rowBoundsWithCount","true");
		properties.setProperty("reasonable","true");
		properties.setProperty("dialect","mysql");    //配置mysql数据库的方言
		pageHelper.setProperties(properties);
		return pageHelper;
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
