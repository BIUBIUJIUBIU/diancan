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
	/**
	 * 免费小说网地址.
	 */
	public static final String FREE_NOVEL_URL = "http://www.12z.cn";
	/**
	 * 免费小说网首页地址.
	 */
	public static final String FREE_NOVEL_HOME_URL = "http://www.12z.cn/index.html";
	/**
	 * 巴士书库首页地址.
	 */
	public static final String BA_SHI_SHU_KU_URL = "https://www.84sk.com";
	/**
	 * 蚂蚁阅读地址.
	 */
	public static final String MA_YI_YUE_DU_URL = "http://www.mayitxt.org/sort/1/1.html";
	/**
	 * 蚂蚁阅读首页地址.
	 */
	public static final String MA_YI_YUE_DU_HOME_URL = "http://www.mayitxt.org";
	/**
	 * 7z小说网首页地址.
	 */
	public static final String SEVEN_Z_BOOK_URL = "http://www.7zxsw.com";
	/**
	 * 铅笔小说网首页地址.
	 */
	public static final String QIAN_BI_URL = "https://www.23qb.net";

	/**
	 * 见识小说网首页地址.
	 */
	public static final String JIAN_SHI_SHU = "https://m.jsshus.com/books/1_1.html";
	/**
	 * 见识小说网基本地址.
	 */
	public static final String JIAN_SHI_SHU_BASE = "https://www.jsshus.com";



}
