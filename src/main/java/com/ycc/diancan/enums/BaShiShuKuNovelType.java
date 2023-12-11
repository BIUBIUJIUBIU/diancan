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
public enum BaShiShuKuNovelType {
	/**
	 * 古代言情.
	 */
	GUDAIYANQING("古代言情"),
	/**
	 * 现代言情.
	 */
	XIANDAIYANQING("现代言情"),
	/**
	 * 纯爱耽美.
	 */
	CHUNAIDANMEI("纯爱耽美"),
	/**
	 * 百合GL.
	 */
	BAIHEGL("百合GL"),
	/**
	 * 仙侠玄幻.
	 */
	XIANXIAXUANHUAN("仙侠玄幻"),
	/**
	 * 科幻未来.
	 */
	KEHUANWEILAI("科幻未来"),
	/**
	 * 悬疑鬼怪.
	 */
	XUANYIGUIGUAI("悬疑鬼怪"),
	/**
	 * 游戏人生.
	 */
	YOUXIRENSHENG("游戏人生"),
	/**
	 * 玄幻奇幻.
	 */
	XUANHUANQIHUAN("玄幻奇幻"),
	/**
	 * 武侠仙侠.
	 */
	WUXIAXIANXIA("武侠仙侠"),
	/**
	 * 都市娱乐.
	 */
	DUSHIYULE("都市娱乐"),
	/**
	 * 历史军事.
	 */
	LISHIJUNSHI("历史军事"),
	/**
	 * 科幻时空.
	 */
	KEHUANSHIKONG("科幻时空"),
	/**
	 * 游戏体育.
	 */
	YOUXITIYU("游戏体育"),
	/**
	 * 灵异悬疑.
	 */
	LINGYIXUANYI("灵异悬疑"),
	/**
	 * 灵异悬疑.
	 */
	ERCIYUANTONGREN("二次元同人"),
	/**
	 * 其他类型.
	 */
	OTHER("其他类型");


	private final String alias;

	BaShiShuKuNovelType(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return this.alias;
	}

}
