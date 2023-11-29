/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.enums;

/**
 * NovelChannel.
 *
 * @author ycc
 * @date 2023-11-29 10:15:02
 */
public enum NovelChannel {

	/**
	 * 男性频道.
	 */
	MALE_CHANNEL("男性频道"),
	/**
	 * 女性频道.
	 */
	FEMALE_CHANNEL("女性频道");

	private final String alias;

	NovelChannel(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return this.alias;
	}

}
