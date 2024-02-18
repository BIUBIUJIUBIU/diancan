package com.ycc.diancan.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ycc.diancan.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.time.Duration;
import java.time.Period;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public final class JsonUtils {

	private JsonUtils() {
	}

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public static boolean isJSON(String s) {
		if (s == null) {
			return false;
		}
		s = s.trim();
		if (s.startsWith("{") && s.endsWith("}")) {
			return true;
		}
		if (s.startsWith("[") && s.endsWith("]")) {
			return true;
		}
		if (s.startsWith("\"") && s.endsWith("\"")) {
			return true;
		}
		if (s.startsWith("'") && s.endsWith("'")) {
			return true;
		}
		return s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false") || s.equalsIgnoreCase("null");
	}

	public static String convertBasicTypeOrObject2String(Object object) {
		if (object == null) {
			return null;
		}
		if (object instanceof String) {
			return (String) object;
		} else if (object instanceof Date) {
			return FormatUtils.formatDateTime((Date) object);
		} else if (object instanceof Period) {
			return ((Period) object).toString();
		} else if (object instanceof Duration) {
			return ((Duration) object).toString();
		} else if (BaseTypeConvertor.isBaseType(object)) {
			return object.toString();
		} else {
			return convertObject2JSON(object);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T convertString2Java(String string, Class<T> clazz) throws JsonProcessingException, ParseException {
		if (StringUtils.isBlank(string)) {
			return null;
		}
		if (clazz.equals(String.class)) {
			return (T) string;
		} else if (clazz.equals(Date.class)) {
			return (T) org.apache.commons.lang3.time.DateUtils.parseDate(string, Constants.DATE_FORMATS.toArray(new String[0]));
		} else if (clazz == Period.class) {
			return (T) Period.parse(string);
		} else if (clazz == Duration.class) {
			return (T) Duration.parse(string);
		} else {
			return OBJECT_MAPPER.readValue(string, clazz);
		}
	}

	public static String convertObject2JSON(Object object) {
		if (object == null) {
			return null;
		}
		try {
			return OBJECT_MAPPER.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			log.error("parse object to json error", e);
			return null;
		}
	}

	public static <T> T convertJSON2Object(String json, Class<T> clazz) {
		if (StringUtils.isBlank(json)) {
			return null;
		}
		try {
			return OBJECT_MAPPER.readValue(json, clazz);
		} catch (Exception e) {
			log.error("parse json to object error", e);
			return null;
		}
	}

	public static <T> T convertJSON2Object(String json, TypeReference<T> typeReference) {
		if (StringUtils.isBlank(json)) {
			return null;
		}
		try {
			return OBJECT_MAPPER.readValue(json, typeReference);
		} catch (Exception e) {
			log.error("parse json to object error", e);
			return null;
		}
	}

	/**
	 * 转换String为JSON.
	 *
	 * @param arg0 字符串型
	 * @return JSON字符串
	 */
	public static String convertJava2JSON(String arg0) {
		if (arg0 == null) {
			return "null";
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append('"');
			int l = arg0.length();
			for (int i = 0; i < l; i++) {
				char c = arg0.charAt(i);
				if (c == '\\') {
					sb.append("\\\\");
				} else if (c == '"') {
					sb.append("\\\"");
				} else if (c == '\b') {
					sb.append("\\b");
				} else if (c == '\f') {
					sb.append("\\f");
				} else if (c == '\t') {
					sb.append("\\t");
				} else if (c == '\n') {
					sb.append("\\n");
				} else if (c == '\r') {
					sb.append("\\r");
				} else {
					sb.append(c);
				}
			}
			sb.append('"');
			return sb.toString();
		}
	}

	/**
	 * 转换String[]为JSON.
	 *
	 * @param arg0 数组型字符串
	 * @return JSON字符串
	 */
	public static String convertJava2JSON(String[] arg0) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		int l = arg0.length;
		if (l > 0) {
			sb.append(convertJava2JSON(arg0[0]));
		}
		for (int i = 1; i < l; i++) {
			sb.append(",");
			sb.append(convertJava2JSON(arg0[i]));
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 转换Object为JSON.
	 *
	 * @param arg0 对象型
	 * @return JSON字符串
	 */
	@SuppressWarnings("rawtypes")
	public static String convertJava2JSON(Object arg0) {
		if (arg0 == null) {
			return "null";
		} else if (arg0 instanceof String) {
			return convertJava2JSON((String) arg0);
		} else if (arg0 instanceof Boolean) {
			return convertJava2JSON(((Boolean) arg0).booleanValue());
		} else if (arg0 instanceof Integer) {
			return convertJava2JSON(((Integer) arg0).intValue());
		} else if (arg0 instanceof Long) {
			return convertJava2JSON(((Long) arg0).longValue());
		} else if (arg0 instanceof Double) {
			return convertJava2JSON(((Double) arg0).doubleValue());
		} else if (arg0 instanceof Float) {
			return convertJava2JSON(((Float) arg0).floatValue());
		} else if (arg0 instanceof Map) {
			StringBuilder sb = new StringBuilder();
			sb.append("{");
			Iterator itor = ((Map) arg0).entrySet().iterator();
			if (itor.hasNext()) {
				Map.Entry entry = (Map.Entry) itor.next();
				sb.append(convertJava2JSON(entry.getKey()));
				sb.append(": ");
				sb.append(convertJava2JSON(entry.getValue()));
			}
			while (itor.hasNext()) {
				sb.append(",");
				Map.Entry entry = (Map.Entry) itor.next();
				String propName = (String) entry.getKey();
				sb.append(convertJava2JSON(propName));
				sb.append(": ");
				sb.append(convertJava2JSON(entry.getValue()));
			}
			sb.append("}");
			return sb.toString();
		} else if (arg0 instanceof JSONObject) {
			return arg0.toString();
		} else if (arg0 instanceof JSONArray) {
			return arg0.toString();
		}
		throw new JSONException("convertJava2JSON#JSONUtils has not implemented!");
	}

	/**
	 * 转换Object[]为JSON.
	 *
	 * @param arg0 数组型对象
	 * @return JSON字符串
	 */
	public static String convertJava2JSON(Object[] arg0) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		int l = arg0.length;
		if (l > 0) {
			sb.append(convertJava2JSON(arg0[0]));
		}
		for (int i = 1; i < l; i++) {
			sb.append(",");
			sb.append(convertJava2JSON(arg0[i]));
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 转换int为JSON.
	 *
	 * @param arg0 整型
	 * @return JOSN字符串
	 */
	public static String convertJava2JSON(int arg0) {
		return String.valueOf(arg0);
	}

	/**
	 * 转换int[]为JSON.
	 *
	 * @param arg0 整型数组
	 * @return JSON字符串
	 */
	public static String convertJava2JSON(int[] arg0) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		int l = arg0.length;
		if (l > 0) {
			sb.append(convertJava2JSON(arg0[0]));
		}
		for (int i = 1; i < l; i++) {
			sb.append(",");
			sb.append(convertJava2JSON(arg0[i]));
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 转换float为JSON.
	 *
	 * @param arg0 浮点型
	 * @return JSON字符串
	 */
	public static String convertJava2JSON(float arg0) {
		return String.valueOf(arg0);
	}

	/**
	 * 转换float[]为JSON.
	 *
	 * @param arg0 浮点型数组
	 * @return JSON字符串
	 */
	public static String convertJava2JSON(float[] arg0) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		int l = arg0.length;
		if (l > 0) {
			sb.append(convertJava2JSON(arg0[0]));
		}
		for (int i = 1; i < l; i++) {
			sb.append(",");
			sb.append(convertJava2JSON(arg0[i]));
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 转换long为JSON.
	 *
	 * @param arg0 长整形
	 * @return JSON字符串
	 */
	public static String convertJava2JSON(long arg0) {
		return String.valueOf(arg0);
	}

	/**
	 * 转换long[]为JSON.
	 *
	 * @param arg0 长整形数组
	 * @return JSON字符串
	 */
	public static String convertJava2JSON(long[] arg0) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		int l = arg0.length;
		if (l > 0) {
			sb.append(convertJava2JSON(arg0[0]));
		}
		for (int i = 1; i < l; i++) {
			sb.append(",");
			sb.append(convertJava2JSON(arg0[i]));
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 转换double为JSON.
	 *
	 * @param arg0 小数型
	 * @return JSON字符串
	 */
	public static String convertJava2JSON(double arg0) {
		return String.valueOf(arg0);
	}

	/**
	 * 转换double[]为JSON.
	 *
	 * @param arg0 小数型数组
	 * @return JSON字符串
	 */
	public static String convertJava2JSON(double[] arg0) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		int l = arg0.length;
		if (l > 0) {
			sb.append(convertJava2JSON(arg0[0]));
		}
		for (int i = 1; i < l; i++) {
			sb.append(",");
			sb.append(convertJava2JSON(arg0[i]));
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 转换boolean为JSON.
	 *
	 * @param arg0 布尔型
	 * @return JSON字符串
	 */
	public static String convertJava2JSON(boolean arg0) {
		return arg0 ? "true" : "false";
	}

	/**
	 * 转换boolean[]为JSON.
	 *
	 * @param arg0 布尔型数组
	 * @return JSON字符串
	 */
	public static String convertJava2JSON(boolean[] arg0) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		int l = arg0.length;
		if (l > 0) {
			sb.append(convertJava2JSON(arg0[0]));
		}
		for (int i = 1; i < l; i++) {
			sb.append(",");
			sb.append(convertJava2JSON(arg0[i]));
		}
		sb.append("]");
		return sb.toString();
	}
}

