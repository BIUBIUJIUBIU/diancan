/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.enums;

/**
 * FreeNovelType.
 *
 * @author ycc
 * @date 2023-12-01 16:20:25
 */
public enum FreeNovelType {

	/**
	 * 都市娱乐.
	 */
	DUSHIYULE("都市娱乐", 12),
	/**
	 * 武侠仙侠.
	 */
	WUXIAXIANXIA("武侠仙侠", 13),
	/**
	 * 奇幻玄幻.
	 */
	QIHUANXUANHUAN("奇幻玄幻", 14),
	/**
	 * 科幻灵异.
	 */
	KEHUANLINGYI("科幻灵异", 15),
	/**
	 * 历史军事.
	 */
	LISHIJUNSHI("历史军事", 16),
	/**
	 * 竞技游戏.
	 */
	JINGJIYOUXI("竞技游戏", 17),
	/**
	 * 实体书版.
	 */
	SHITISHUBAN("实体书版", 18),
	/**
	 * 二次元.
	 */
	ERCIYUAN("二次元", 19);

	private final String alias;
	private final int index;

	FreeNovelType(String alias, int index) {
		this.alias = alias;
		this.index = index;
	}

	public String getAlias() {
		return this.alias;
	}

	public int getIndex() {
		return this.index;
	}
	}
