/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.enums;

/**
 * ZeiSHuNovelType(贼书吧小说类型).
 *
 * @author ycc
 * @date 2023-11-27 16:44:49
 */
public enum ZeiSHuNovelType {
	/**
	 * 历史穿越.
	 */
	LISHI_CHUANYUE("历史穿越"),
	/**
	 * 传统言情.
	 */
	CHUANTONG_YANQING("传统言情"),
	/**
	 * 都市社会.
	 */
	DUSHI_SHEHUI("都市社会"),
	/**
	 * 幻想现言.
	 */
	HUANXIANG_XIANSHI("幻想现言"),
	/**
	 * 耽美百合.
	 */
	DANMEI_BAIHE("耽美百合"),
	/**
	 * 东方玄幻.
	 */
	DONGFANG_XUANHUAN("东方玄幻"),
	/**
	 * 传统武侠.
	 */
	CHUANTONG_WUXIA("传统武侠"),
	/**
	 * 恐怖惊悚.
	 */
	JINGSONG_KONGBU("恐怖惊悚"),
	/**
	 * 现代都市.
	 */
	XIANDAI_DUSHI("现代都市"),
	/**
	 * 人物传记.
	 */
	RENWU_ZHUANJI("人物传记"),
	/**
	 * 网游动漫.
	 */
	WANGYOU_DONGMAN("网游动漫"),
	/**
	 * 军事历史.
	 */
	JUNSHI_LISHI("军事历史"),
	/**
	 * 热点资源.
	 */
	REDIAN_ZIYUAN("热点资源"),
	/**
	 * 西方奇幻.
	 */
	XIFANG_XUANHUAN("西方奇幻"),
	/**
	 * 仙侠修真.
	 */
	XIANXIA_XIUZHEN("仙侠修真"),
	/**
	 * 侦探推理.
	 */
	ZHENTAN_TUILI("侦探推理"),
	/**
	 * 经典科幻.
	 */
	JINGDIAN_KEHUAN("经典科幻"),
	/**
	 * 名著杂志.
	 */
	MINGZHU_ZAZHI("名著杂志");

	private final String alias;

	ZeiSHuNovelType(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return this.alias;
	}
}
