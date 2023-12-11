/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.constant;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * Constant.
 *
 * @author ycc
 * @date 2023-11-27 16:18:00
 */
public final class Constants {
	private Constants() {

	}

	/**
	 * the full supported date formats.
	 */
	public static final List<String> DATE_FORMATS = ImmutableList.of(
			"yyyy-MM-dd'T'HH:mm:ss.SSSX",
			"yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
			"yyyy-MM-dd'T'HH:mm:ssX",
			"yyyy-MM-dd'T'HH:mm:ssZZ",
			"yyyy-MM-dd'T'HH:mm:ss",
			"yyyy-MM-dd HH:mm:ss",
			"yyyy-MM-dd HH:mm",
			"yyyy-MM-dd",
			"yyyy-MM",
			"yyyy/MM/dd'T'HH:mm:ss.SSSX",
			"yyyy/MM/dd'T'HH:mm:ssX",
			"yyyy/MM/dd'T'HH:mm:ssZZ",
			"yyyy/MM/dd'T'HH:mm:ss",
			"yyyy/MM/dd HH:mm:ss",
			"yyyy/MM/dd HH:mm",
			"yyyy/MM/dd",
			"yyyy/MM",
			"HH:mm:ss",
			"HH:mm",
			"yyyy年MM月",
			"yyyy年MM月dd日",
			"yyyy年MM月dd日 HH:mm",
			"yyyy年MM月dd日 HH时mm分",
			"yyyy年MM月dd日 HH:mm:ss",
			"yyyy年MM月dd日 HH时mm分ss秒",
			"EEE MMM dd HH:mm:ss Z yyyy");


}
