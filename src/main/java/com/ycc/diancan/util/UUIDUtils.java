/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * UUIDUtils.
 *
 * @author ycc
 * @date 2023-11-28 15:05:56
 */
public final class UUIDUtils {

	private UUIDUtils() {

	}

	private static final Pattern UUID_PATTERN = Pattern.compile("^[a-z0-9A-Z]{32}$");

	/**
	 * 获得一个32位指定分隔符的UUID串.
	 *
	 * @param separator 分隔符
	 * @return UUID
	 */
	public static String generateUUID(char separator) {
		if (separator != '-') {
			return java.util.UUID.randomUUID().toString().replace('-', separator);
		} else {
			return java.util.UUID.randomUUID().toString();
		}
	}

	/**
	 * 获得一个32位不带分隔符的UUID串.
	 *
	 * @return UUID
	 */
	public static String generateUUID() {
		return java.util.UUID.randomUUID().toString().replace("-", "");
	}

	public static boolean likeUUID(String uuid) {
		if (StringUtils.isBlank(uuid)) {
			return false;
		}
		return UUID_PATTERN.matcher(uuid).matches();
	}
}
