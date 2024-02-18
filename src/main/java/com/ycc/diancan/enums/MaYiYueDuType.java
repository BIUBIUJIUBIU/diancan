/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.enums;

/**
 * BaShiShuKuNovelType.
 *
 * @author ycc
 * @date 2023-12-04 09:40:14
 */
public enum MaYiYueDuType {
	/**
	 * 玄幻.
	 */
	XUANHUAN("玄幻"),
	/**
	 * 仙侠.
	 */
	XIANXIA("仙侠"),
	/**
	 * 都市.
	 */
	DUSHI("都市"),
	/**
	 * 历史.
	 */
	LISHI("历史"),
	/**
	 * 军事.
	 */
	JUNSHI("军事"),
	/**
	 * 悬疑.
	 */
	XUANYI("悬疑"),
	/**
	 * 灵异.
	 */
	LINGYI("灵异"),
	/**
	 * 科幻.
	 */
	KEHUAN("科幻"),
	/**
	 * 游戏.
	 */
	YOUXI("游戏"),
	/**
	 * 现言.
	 */
	XIANYAN("现言"),
	/**
	 * 古言.
	 */
	GUYAN("古言"),
	/**
	 * 穿越.
	 */
	CHUANYUE("穿越"),
	/**
	 * 青春.
	 */
	QINGCHUN("青春"),
	/**
	 * 豪门.
	 */
	HAOMEN("豪门"),
	/**
	 * 总裁.
	 */
	ZONGZAI("总裁"),
	/**
	 * 耽美.
	 */
	DANMEI("耽美"),
	/**
	 * 同人.
	 */
	TONGREN("同人"),
	/**
	 * 其他.
	 */
	QITA("其他");


	private final String alias;

	MaYiYueDuType(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return this.alias;
	}

}
