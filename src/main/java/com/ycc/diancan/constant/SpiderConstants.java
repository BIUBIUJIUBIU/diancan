/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.constant;

import java.util.regex.Pattern;

/**
 * SpiderConstant.
 *
 * @author ycc
 * @date 2023-11-29 10:27:44
 */
public final class SpiderConstants {
	private SpiderConstants() {

	}

	/**
	 * 截取书本名称.
	 */
	public static final Pattern CUT_OUT_BOOK_NAME = Pattern.compile("(?<=《)(.*?)(?=》)");
	/**
	 * 贼书吧地址.
	 */
	public static final String ZEI_SHU_WANG_URL = "https://www.zei8.vip";
	/**
	 * 万书网地址.
	 */
	public static final String WAN_SHU_WANG_URL = "http://www.wanshu5.info/all.html";
	/**
	 * 书荒部落地址.
	 */
	public static final String SHU_HUANG_BU_LUO_URL = "https://noveless.com/alltext";
}
