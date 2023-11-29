/*
 * @(#)FormatUtils.java 0.1 Nov 28, 2007
 * Copyright 2004-2007 Homolo Co., Ltd. All rights reserved.
 */

package com.ycc.diancan.util;

import com.google.common.collect.Maps;
import com.ycc.diancan.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

@Slf4j
public final class FormatUtils {

	private static final Logger logger = LoggerFactory.getLogger(FormatUtils.class);

	private FormatUtils() {
	}

	public static String formatDate(Date date, String format) {
		return (date == null) ? "" : new SimpleDateFormat(format).format(date);
	}

	public static Date parseDate(String str, String format) {
		try {
			return org.apache.commons.lang3.StringUtils.isBlank(str) ? null : DateUtils.parseDate(str, format);
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 格式化日期对象.
	 *
	 * @param date 日期
	 * @return 格式化的结果
	 */
	public static String formatDate(Date date) {
		return formatDate(date, "yyyy-MM-dd");
	}

	/**
	 * 得到一个日期对象根据指定的日期串.
	 *
	 * @param str 日期字符串
	 * @return 日期或者null
	 */
	public static Date parseDate(String str) {
		try {
			if (org.apache.commons.lang3.StringUtils.isBlank(str)) {
				return null;
			}
			return DateUtils.parseDate(str, Locale.ENGLISH, Constants.DATE_FORMATS.toArray(new String[0]));
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 格式化日期时间对象.
	 *
	 * @param date 日期
	 * @return 格式化的字符串
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到一个日期对象根据指定的日期时间串.
	 *
	 * @param str 日期字符串
	 * @return 日期
	 */
	public static Date parseDateTime(String str) {
		return parseDate(str, "yyyy-MM-dd HH:mm:ss");
	}

	public static String formatDateToGMTWithWeek(Date date) {
		DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		return format.format(date);
	}

	public static Date parseDateToGMTWithWeek(String source) {
		try {
			DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
			format.setTimeZone(TimeZone.getTimeZone("GMT"));
			return format.parse(source);
		} catch (ParseException e) {
			log.error(e.getLocalizedMessage(), e);
			return null;
		}
	}

	public static String formatShortDate(Date date) {
		return formatDate(date, "MM-dd");
	}

	/**
	 * 格式化日期时间对象.
	 *
	 * @param date 日期
	 * @return 返回格式化的字符串
	 */
	public static String formatShortDateTime(Date date) {
		return formatDate(date, "MM-dd HH:mm");
	}

	// 定义日期format对象

	public static String formatCNDate(Date date) {
		return formatDate(date, "yyyy年MM月dd日");
	}

	public static String formatCNWeekDay(Date date) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_WEEK);
		switch (day) {
			case Calendar.SUNDAY:
				return "星期日";
			case Calendar.MONDAY:
				return "星期一";
			case Calendar.TUESDAY:
				return "星期二";
			case Calendar.WEDNESDAY:
				return "星期三";
			case Calendar.THURSDAY:
				return "星期四";
			case Calendar.FRIDAY:
				return "星期五";
			case Calendar.SATURDAY:
				return "星期六";
			default:
				return "error";
		}
	}

	public static String formatJSONDate(Date date) {
		return formatDate(date, "yyyy-MM-dd'T'HH:mm:ss");
	}

	public static Date parseJSONDate(String str) {
		return parseDate(str, "yyyy-MM-dd'T'HH:mm:ss");
	}

	// 日期的数字映射
	static Map<String, String> cnm = Maps.newLinkedHashMap();

	static {
		cnm.put("三十一", "31");
		cnm.put("三十", "30");
		cnm.put("二十九", "29");
		cnm.put("二十八", "28");
		cnm.put("二十七", "27");
		cnm.put("二十六", "26");
		cnm.put("二十五", "25");
		cnm.put("二十四", "24");
		cnm.put("二十三", "23");
		cnm.put("二十二", "22");
		cnm.put("二十一", "21");
		cnm.put("二十", "20");
		cnm.put("十九", "19");
		cnm.put("十八", "18");
		cnm.put("十七", "17");
		cnm.put("十六", "16");
		cnm.put("十五", "15");
		cnm.put("十四", "14");
		cnm.put("十三", "13");
		cnm.put("十二", "12");
		cnm.put("十一", "11");
		cnm.put("十", "10");
		cnm.put("九", "9");
		cnm.put("八", "8");
		cnm.put("七", "7");
		cnm.put("六", "6");
		cnm.put("五", "5");
		cnm.put("四", "4");
		cnm.put("三", "3");
		cnm.put("二", "2");
		cnm.put("一", "1");
		cnm.put("〇", "0");
	}

	/**
	 * 解析中文数字日期例如：二〇一二年十月十六日.
	 *
	 * @param str 字符串
	 * @return 日期
	 */
	public static Date parseCNDate(String str) {
		for (Map.Entry<String, String> entry : cnm.entrySet()) {
			str = str.replaceAll(entry.getKey(), entry.getValue());
		}
		return parseDate(str, "yyyy年M月d日");
	}

	/**
	 * 解析中文数字日期例如：二〇一二年十月.
	 *
	 * @param str 字符串
	 * @return 日期
	 */

	public static Date parseCNDate2(String str) {
		for (Map.Entry<String, String> entry : cnm.entrySet()) {
			str = str.replaceAll(entry.getKey(), entry.getValue());
		}
		return parseDate(str, "yyyy年M月");
	}

}
