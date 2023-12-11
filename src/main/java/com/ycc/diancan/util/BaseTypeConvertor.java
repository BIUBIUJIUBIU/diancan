package com.ycc.diancan.util;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public abstract class BaseTypeConvertor {
	private static final Class[] BASE_TYPES = new Class[]{
			String.class, Integer.class, Long.class, Boolean.class, Date.class, Float.class, Double.class};

	public static boolean isBaseType(Object value) {
		return Arrays.stream(BASE_TYPES).anyMatch(c -> c.isInstance(value));
	}

	public static String convert2String(Object value) {
		if (value == null) {
			return null;
		} else if (value instanceof String) {
			return "string:" + value;
		} else if (value instanceof Integer) {
			return "int:" + value;
		} else if (value instanceof Short) {
			return "short:" + value;
		} else if (value instanceof Long) {
			return "long:" + value;
		} else if (value instanceof Boolean) {
			return "boolean:" + value;
		} else if (value instanceof Date) {
			return "date:" + ((Date) value).getTime();
		} else if (value instanceof Float) {
			return "float:" + value;
		} else if (value instanceof Double) {
			return "double:" + value;
		} else if (value instanceof Collection<?>) {
			List<String> strList = new ArrayList<>();
			((Collection<?>) value).forEach(v -> strList.add(convert2String(v)));
			return "list:" + new JSONArray(strList);
		} else {
			return value.toString();
		}
	}

	public static Object convert2Object(String value) {
		if (value == null) {
			return null;
		}
		int offset = value.indexOf(':');
		if (offset == -1) {
			return value;
		}
		String type = value.substring(0, offset);
		if ("string".equalsIgnoreCase(type)) {
			return value.substring(offset + 1);
		} else if ("int".equalsIgnoreCase(type)) {
			return Integer.valueOf(value.substring(offset + 1));
		} else if ("short".equalsIgnoreCase(type)) {
			return Short.valueOf(value.substring(offset + 1));
		} else if ("long".equalsIgnoreCase(type)) {
			return Long.valueOf(value.substring(offset + 1));
		} else if ("boolean".equalsIgnoreCase(type)) {
			return Boolean.valueOf(value.substring(offset + 1));
		} else if ("date".equalsIgnoreCase(type)) {
			return new Date(Long.parseLong(value.substring(offset + 1)));
		} else if ("float".equalsIgnoreCase(type)) {
			return Float.valueOf(value.substring(offset + 1));
		} else if ("double".equalsIgnoreCase(type)) {
			return Double.valueOf(value.substring(offset + 1));
		} else if ("list".equalsIgnoreCase(type)) {
			JSONArray array = new JSONArray(value.substring(offset + 1));
			List objList = new ArrayList();
			for (int i = 0; i < array.length(); i++) {
				objList.add(convert2Object(array.getString(i)));
			}
			return objList;
		} else {
			return value;
		}
	}

}
