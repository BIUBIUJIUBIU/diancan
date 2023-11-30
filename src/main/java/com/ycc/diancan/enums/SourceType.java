/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.enums;

/**
 * SourceType.
 *
 * @author ycc
 * @date 2023-11-27 16:40:58
 */
public enum SourceType {
	/**
	 * 万书网.
	 */
	WAN_SHU_WANG("万书网"),
	/**
	 * 贼书吧.
	 */
	ZEI_SHU_BA("贼书吧");

	SourceType(String alias) {
		this.alias = alias;
	}

	private final String alias;

	public String getAlias() {
		return this.alias;
	}
}