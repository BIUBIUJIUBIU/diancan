package com.ycc.diancan.enums;

/**
 * LongTengType.
 *
 * @author ycc
 * @date 2023-12-25 2023/12/25
 */
public enum LongTengType {
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
	 * 侦探推理.
	 */
	ZHENTANTUILI("侦探推理"),
	/**
	 * 网游动漫.
	 */
	WANGYOUDONGMAN("网游动漫"),
	/**
	 * 科幻小说.
	 */
	KEHUANXIAOSHUO("科幻小说"),
	/**
	 * 恐怖灵异.
	 */
	KONGBULINGYI("恐怖灵异"),
	/**
	 * 散文诗词.
	 */
	SANWENSHICI("散文诗词"),
	/**
	 * 肉文辣文.
	 */
	ROUWENLAWEN("肉文辣文");

	private final String alias;

	LongTengType(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return this.alias;
	}
}
