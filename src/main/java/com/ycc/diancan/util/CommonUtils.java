/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.util;

import org.apache.commons.lang3.StringUtils;

/**
 * CommonUtils.
 *
 * @author ycc
 * @date 2023-12-04 15:47:30
 */
public final class CommonUtils {
	private CommonUtils() {

	}

	/**
	 * 去除文本中的空格.
	 *
	 * @param value 包含空格文本内容
	 * @return 去空格之后的文本内容
	 */
	public static String trimBlankChar(String value) {
		return StringUtils.replaceEach(value, new String[]{" ", "   ", "　", " "}, new String[]{"", "", "", ""});
	}
}
