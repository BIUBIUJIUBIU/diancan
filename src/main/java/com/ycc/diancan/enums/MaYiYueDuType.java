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
	XUANHUAN("玄幻"),
	XIANXIA("仙侠"),
	DUSHI("都市"),
	LISHI("历史"),
	JUNSHI("军事"),
	XUANYI("悬疑"),
	LINGYI("灵异"),
	KEHUAN("科幻"),
	YOUXI("游戏"),
	XIANYAN("现言"),
	GUYAN("古言"),
	CHUANYUE("穿越"),
	QINGCHUN("青春"),
	HAOMEN("豪门"),
	ZONGZAI("总裁"),
	DANMEI("耽美"),
	TONGREN("同仁"),
	QITA("其他");


	private final String alias;

	MaYiYueDuType(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return this.alias;
	}

}
