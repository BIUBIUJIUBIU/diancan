/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.exception;

/**
 * 校验异常.
 *
 * @author ycc
 * @date 2023-11-29 16:36:26
 */
public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 7705576562761296144L;

	public ValidationException() {
		super();
	}

	/**
	 * 构造方法.
	 *
	 * @param msg   描述
	 * @param cause 原因
	 */
	public ValidationException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * 构造方法.
	 *
	 * @param msg 描述
	 */
	public ValidationException(String msg) {
		super(msg);
	}

	/**
	 * 构造方法.
	 *
	 * @param cause 原因
	 */
	public ValidationException(Throwable cause) {
		super(cause);
	}
}
