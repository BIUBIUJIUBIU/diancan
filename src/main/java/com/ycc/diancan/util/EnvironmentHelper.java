package com.ycc.diancan.util;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.Assert;

import java.util.Map;

public class EnvironmentHelper {

	/**
	 * web环境配置属性源文件名.
	 */
	public static final String WEB_PROPERTY_SOURCE_NAME = "web.env";

	private final StandardEnvironment environment;

	public EnvironmentHelper(Environment environment) {
		this.environment = (StandardEnvironment) environment;
	}

	protected Object getWebEnvProperty(String key) {
		Assert.isTrue(getPropertySources().contains(WEB_PROPERTY_SOURCE_NAME), String.format("PropertySource named [%s] does not exist",
				WEB_PROPERTY_SOURCE_NAME));
		PropertySource<?> ps = getPropertySources().get(WEB_PROPERTY_SOURCE_NAME);
		if (ps != null) {
			Object value = ps.getProperty(key);
			if (value != null) {
				return value;
			}
		}
		return null;
	}

	/**
	 * 获取属性的来源.
	 *
	 * @return 来源
	 * @see org.springframework.core.env.AbstractEnvironment#getPropertySources()
	 */
	public MutablePropertySources getPropertySources() {
		return this.environment.getPropertySources();
	}

	/**
	 * 获取系统环境.
	 *
	 * @return 系统环境.
	 * @see org.springframework.core.env.AbstractEnvironment#getSystemEnvironment()
	 */
	public Map<String, Object> getSystemEnvironment() {
		return this.environment.getSystemEnvironment();
	}

	/**
	 * 获取系统属性.
	 *
	 * @return 系统属性
	 * @see org.springframework.core.env.AbstractEnvironment#getSystemProperties()
	 */
	public Map<String, Object> getSystemProperties() {
		return this.environment.getSystemProperties();
	}

	/**
	 * 根据属性名获取对应的值.
	 *
	 * @param key 属性名
	 * @return 值
	 * @see org.springframework.core.env.AbstractEnvironment#getProperty(String)
	 */
	public String getProperty(String key) {
		return getProperty(key, String.class);
	}

	/**
	 * 根据属性名获取对应的值,如果没有则返回缺省值.
	 *
	 * @param key          属性名
	 * @param defaultValue 默认值
	 * @return 值
	 * @see org.springframework.core.env.AbstractEnvironment#getProperty(String, String)
	 */
	public String getProperty(String key, String defaultValue) {
		String value = getProperty(key);
		return value == null ? defaultValue : value;
	}

	/**
	 * 根据属性名获得指定类型的属性值.
	 *
	 * @param key        属性名
	 * @param targetType 目标类型
	 * @param <T>        指定的类型
	 * @return 值
	 * @see org.springframework.core.env.AbstractEnvironment#getProperty(String, Class)
	 */
	public <T> T getProperty(String key, Class<T> targetType) {
		Object value;
		if (getPropertySources().contains(WEB_PROPERTY_SOURCE_NAME)) {
			value = getWebEnvProperty(key);
		} else {
			value = this.environment.getProperty(key);
		}
		if (value != null) {
			Class<?> valueType = value.getClass();
			ConversionService conversionService = this.environment.getConversionService();
			if (!conversionService.canConvert(valueType, targetType)) {
				throw new IllegalArgumentException(String.format("Cannot convert value [%s] from source type [%s] to target type [%s]",
						value, valueType.getSimpleName(), targetType.getSimpleName()));
			}
			return conversionService.convert(value, targetType);
		}
		return this.environment.getProperty(key, targetType);
	}

	/**
	 * 根据属性名获得指定类型的属性值,如果没有则返回缺省值.
	 *
	 * @param key          属性名
	 * @param targetType   转换类型
	 * @param defaultValue 默认值
	 * @param <T>          指定的类型
	 * @return 值
	 * @see org.springframework.core.env.AbstractEnvironment#getProperty(String, Class, Object)
	 */
	public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
		T value = getProperty(key, targetType);
		return value == null ? defaultValue : value;
	}

}
