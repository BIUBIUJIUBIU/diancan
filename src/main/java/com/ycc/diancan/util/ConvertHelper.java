package com.ycc.diancan.util;

import cn.hutool.core.util.EnumUtil;
import com.ycc.diancan.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class ConvertHelper {

	private final static String[] TIMESFORMAT = new String[]{"yyyy-MM-dd", "yyyy年MM月dd日", "yyyy年M月d日", "yyyy年M月dd日", "yyyy年MM月d日",
			"yyyy.MM.dd", "yyyy.M.d", "yyyy.M.dd", "yyyy.MM.d",
			"yyyy/MM/dd", "yyyy/M/d", "yyyy/M/dd", "yyyy/MM/d"
	};

	public static String convertEnumEn(Class<? extends Enum> enumType, String value, String title) {
		if (StringUtils.isBlank(value)) {
			return null;
		}
		LinkedHashMap<String, ? extends Enum> enumMap = EnumUtil.getEnumMap(enumType);
		for (Map.Entry<String, ? extends Enum> entry : enumMap.entrySet()) {
			if (StringUtils.equals(value, MessageUtils.getLocaleMessage(entry.getValue()))) {
				return entry.getKey();
			}
		}
		throw new ValidationException(title + " [" + value + "] 描述不规范，请重新设置");
	}

	public Date convertDate(String value, String title) {
		if (StringUtils.isBlank(value)) {
			return null;
		}
		try {
			return DateUtils.parseDate(value, TIMESFORMAT);
		} catch (ParseException e) {
			throw new ValidationException(title + " [" + value + "] 日期格式有错误，请重新设置");
		}
	}


}
