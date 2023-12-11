/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.enums;

/**
 * QianBiType.
 *
 * @author ycc
 * @date 2023-12-11 16:01:17
 */
public enum QianBiType {
	/**
	 * 言情女生.
	 */
	YANQINGNVSHENG("言情女生"),
	/**
	 * 玄幻奇幻.
	 */
	XUANHUANQIHUAN("玄幻奇幻"),
	/**
	 * 都市青春.
	 */
	DUSHIQINGCHUN("都市青春"),
	/**
	 * 武侠仙侠.
	 */
	WUXIAXIANXIA("武侠仙侠"),
	/**
	 * 唯美纯爱.
	 */
	WEIMEICHUNAI("唯美纯爱"),
	/**
	 * 轻小说の.
	 */
	QINGXIAOSHUO("轻小说の"),
	/**
	 * 历史军事.
	 */
	LISHIJUNSHI("历史军事");





	private final String alias;

	QianBiType(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return this.alias;
	}

}
