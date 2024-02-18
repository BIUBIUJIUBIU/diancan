package com.ycc.diancan.enums;

/**
 * jianShiShuType.
 *
 * @author ycc
 * @date 2023-12-21 2023/12/21
 */
public enum JianShiShuType {
	/**
	 * 玄幻小说.
	 */
	XUANHUANXIAOSHUO("玄幻小说"),
	/**
	 * 虚拟网游.
	 */
	XUNIWANGYOU("虚拟网游"),
	/**
	 * 历史军事.
	 */
	LISHIJUNSHI("历史军事"),
	/**
	 * 架空历史.
	 */
	JIAKONGLISHI("架空历史"),
	/**
	 * 恐怖灵异.
	 */
	KONGBULINGYI("恐怖灵异"),
	/**
	 * 惊悚小说.
	 */
	JINGSONGXIAOSHUO("惊悚小说"),
	/**
	 * 都市言情.
	 */
	DUSHIYANQING("都市言情"),
	/**
	 * 全本小说.
	 */
	QUANBENXIAOSHUO("全本小说");

	private final String alias;

	JianShiShuType(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return this.alias;
	}
}
