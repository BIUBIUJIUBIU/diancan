/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ContentsUtils.
 *
 * @author ycc
 * @date 2023-12-11 17:00:18
 */
public final class ContentsUtils {
	private ContentsUtils() {

	}

	public static int parseSectionIndex(String sectionName) {
		Pattern pattern = Pattern.compile("第(.*?)章");
		Matcher matcher = pattern.matcher(sectionName);
		if (matcher.find()) {
			String group = matcher.group(1);
			if (StringUtils.containsAny(group, "万", "千", "百", "十", "九", "八", "七", "六", "五", "四", "三", "二", "一", "零")) {
				return FormatUtils.convertToLowerCaseNumber(group.replace("第", "").replace("章", ""));
			} else {
				return Integer.parseInt(CommonUtils.trimBlankChar(group));
			}
		} else {
			return -1;
		}
	}
}
