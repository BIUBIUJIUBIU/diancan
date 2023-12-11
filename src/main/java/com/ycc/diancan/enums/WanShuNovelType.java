/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.enums;

/**
 * WanShuNovelType(万书网小说类型).
 *
 * @author ycc
 * @date 2023-11-27 16:44:49
 */
public enum WanShuNovelType {
	/**
	 * 玄幻奇幻.
	 */
	XUANHUAN_QIHUAN("玄幻奇幻"),
	/**
	 * 武侠仙侠.
	 */
	WUXIA_XIANXIA("武侠仙侠"),
	/**
	 * 都市言情.
	 */
	DUSHI_YANQING("都市言情"),
	/**
	 * 历史军事.
	 */
	LISHI_JUNSHI("历史军事"),
	/**
	 * 游戏竞技.
	 */
	YOUXI_JINGJI("游戏竞技"),
	/**
	 * 科幻灵异.
	 */
	KEHUAN_LINGYI("科幻灵异"),
	/**
	 * 其他类型.
	 */
	OTHER("其他类型");

	private final String alias;

	WanShuNovelType(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return this.alias;
	}
}
