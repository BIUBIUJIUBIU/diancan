package com.ycc.diancan.util;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

public final class MessageUtils {

	private MessageUtils() {

	}

	private static MessageSource defaultMessageSource;

	private static MessageSource customMessageSource;

	private static String[] resources;

	public static String[] getResources() {
		if (resources == null) {
			return new String[0];
		}
		System.arraycopy(resources, 0, new String[resources.length], 0, resources.length);
		return ArrayUtils.clone(resources);
	}

	public static MessageSource getDefaultMessageSource() {
		return defaultMessageSource;
	}

	public static void initDefaultMessageResources(String[] resources) {
		MessageUtils.resources = (resources == null ? null : ArrayUtils.clone(resources));
		ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
		if (resources != null) {
			ms.setBasenames(resources);
		}
		setDefaultMessageSource(ms);
	}

	public static void setDefaultMessageSource(MessageSource defaultMessageSource) {
		MessageUtils.defaultMessageSource = defaultMessageSource;
	}

	public static MessageSource getCustomMessageSource() {
		return customMessageSource;
	}

	public static void setCustomMessageSource(MessageSource customMessageSource) {
		MessageUtils.customMessageSource = customMessageSource;
	}

	public static String getLocaleMessage(String name) {
		return getLocaleMessage(name, null);
	}

	public static String getLocaleMessage(String name, String defaultMsg) {
		String message = null;
		if (customMessageSource != null) {
			message = customMessageSource.getMessage(name, null, null, Locale.getDefault());
		}
		if (message == null && defaultMessageSource != null) {
			message = defaultMessageSource.getMessage(name, null, defaultMsg, Locale.getDefault());
		}
		return message;
	}

	public static String getLocaleMessage(Enum<?> obj, String defaultMsg) {
		return getLocaleMessage(obj.getClass().getName() + "." + obj.name(), defaultMsg);
	}

	public static String getLocaleMessage(Enum<?> obj) {
		return getLocaleMessage(obj, obj.name());
	}

}
