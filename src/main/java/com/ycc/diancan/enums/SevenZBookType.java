/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.enums;

/**
 * SevenZBookType.
 *
 * @author ycc
 * @date 2023-12-08 15:16:24
 */
public enum SevenZBookType {
	/**
	 * 玄幻魔法.
	 */
	XUANHUANMOFA("玄幻魔法"),
	/**
	 * 武侠修真.
	 */
	WUXIAXIUZHEN("武侠修真"),
	/**
	 * 都市言情.
	 */
	DUSHIYANQING("都市言情"),
	/**
	 * 历史军事.
	 */
	LISHIJUNSHI("历史军事"),
	/**
	 * 网游竞技.
	 */
	WANGYOUJINGJI("网游竞技"),
	/**
	 * 科幻小说.
	 */
	KEHUANXIAOSHUO("科幻小说"),
	/**
	 * 恐怖灵异.
	 */
	KONGBULINGYI("恐怖灵异");

	private final String alias;

	SevenZBookType(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return this.alias;
	}
}
