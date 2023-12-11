package com.ycc.diancan;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(name = "diancan.env", value = {"classpath:/diancan/module.properties"})
@ComponentScan("com.ycc.diancan")
public class ModuleConfig {
	/**
	 * 模块名.
	 */
	public static final String NAME = "diancan";

	public String moduleInfo() {
		return NAME;
	}

}
