package com.ycc.diancan.config.mybatis;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// @Configuration
// @AutoConfigureAfter(DataSourceConfig.class)
public class MyBatisMapperScannerConfig {
	//
	// @Bean
	// public MapperScannerConfigurer mapperScannerConfigurer() {
	// 	MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
	// 	mapperScannerConfigurer.setBasePackage("com.ycc.diancan.mapper;com.gitee.sunchenbin.mybatis.actable.dao.*");
	// 	mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
	// 	return mapperScannerConfigurer;
	// }

}
