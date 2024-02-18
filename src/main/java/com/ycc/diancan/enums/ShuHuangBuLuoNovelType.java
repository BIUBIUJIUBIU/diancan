/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.enums;

/**
 * ShuHuangBuLuoNovelType.
 *
 * @author ycc
 * @date 2023-11-30 17:05:40
 */
public enum ShuHuangBuLuoNovelType {
	/**
	 * 无限流.
	 */
	WUXIANLIU("无限流"),
	/**
	 * 科幻.
	 */
	KEHUAN("科幻"),
	/**
	 * 都市.娱乐.
	 */
	DUSHU_YULE("都市.娱乐"),
	/**
	 * 末世.
	 */
	MOSHI("末世"),
	/**
	 * 玄幻修真.
	 */
	XUANHUANXIUZHEN("玄幻修真"),
	/**
	 * 网游.
	 */
	WANGYOU("网游"),
	/**
	 * 西方.
	 */
	XIFANG("西方"),
	/**
	 * 架空历史.
	 */
	JIAKONGLISHI("架空历史"),
	/**
	 * 西幻.
	 */
	XIHUAN("西幻"),
	/**
	 * 竟技.
	 */
	JINGJI("竟技"),
	/**
	 * 科幻.灵异.
	 */
	KEHUANLINGYI("科幻.灵异"),
	/**
	 * 盗墓.
	 */
	DAOMU("盗墓"),
	/**
	 * 幻想.
	 */
	HUANXIANG("幻想"),
	/**
	 * 言情.
	 */
	YANQING("言情"),
	/**
	 * 历史穿越.
	 */
	LISHICHUANYUE("历史穿越"),
	/**
	 * 末日.
	 */
	MORI("末日"),
	/**
	 * 网游幻想.
	 */
	WANGYOUHUANXIANG("网游幻想"),
	/**
	 * 后宫.
	 */
	HOUGONG("后宫"),
	/**
	 * 东方玄幻.
	 */
	DONGFANGXUANHUAN("东方玄幻"),
	/**
	 * 虚拟网游.
	 */
	XUNIWANGYOU("虚拟网游"),
	/**
	 * 军事.
	 */
	JUNSHI("军事"),
	/**
	 * 游戏.
	 */
	YOUXI("游戏"),
	/**
	 * 两晋隋唐.
	 */
	LIANGJINSUITANG("两晋隋唐"),
	/**
	 * 西方玄幻.
	 */
	XIFANGXUANHUAN("西方玄幻"),
	/**
	 * 古代言情.
	 */
	GUDAIYANQING("古代言情"),
	/**
	 * 奇幻.
	 */
	QIHUAN("奇幻"),
	/**
	 * 历史.军事.
	 */
	LISHIJUNSHI("历史.军事"),
	/**
	 * 教育书刊.
	 */
	JIAOYUSHUKAN("教育书刊"),
	/**
	 * 穿越.
	 */
	CHUANYUE("穿越"),
	/**
	 * 网络.
	 */
	WANGLUO("网络"),
	/**
	 * 历史.
	 */
	LISHI("历史"),
	/**
	 * 西方奇幻.
	 */
	XIFANGQIHUAN("西方奇幻"),
	/**
	 * 悬疑.
	 */
	XUANYI("悬疑"),
	/**
	 * 竞技.
	 */
	JINJI("竞技"),
	/**
	 * 仙侠.
	 */
	XIANXIA("仙侠"),
	/**
	 * 都市.
	 */
	DUSHI("都市"),
	/**
	 * 都市异能.
	 */
	DUSHIYINENG("都市异能"),
	/**
	 * 都市生活.
	 */
	DUSHISHENGHUO("都市生活"),
	/**
	 * 武侠.
	 */
	WUXIA("武侠"),
	/**
	 * 异能.
	 */
	YINENG("异能"),
	/**
	 * 游戏异界.
	 */
	YOUXIYIJIE("游戏异界"),
	/**
	 * 玄幻.
	 */
	XUANHUAN("玄幻"),
	/**
	 * 古装迷情.
	 */
	GUZHUANGMIQING("古装迷情"),
	/**
	 * 长篇.
	 */
	CHANGPIAN("长篇"),
	/**
	 * 玄幻.奇幻.
	 */
	XUANHUANQIHUAN("玄幻.奇幻"),
	/**
	 * 科换.
	 */
	KEHUANG("科换"),
	/**
	 * 都市架空.
	 */
	DUSHIJIAKONG("都市架空");


	ShuHuangBuLuoNovelType(String alias) {
		this.alias = alias;
	}

	private final String alias;

	public String getAlias() {
		return this.alias;
	}

}
