package com.ycc.diancan.result;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class MessageCode {
	/**
	 * SUCCESS_CODE.
	 */
	public static final int SUCCESS_CODE = 1;
	/**
	 * WARNING_CODE.
	 */
	public static final int WARNING_CODE = 2;
	/**
	 * NOT_FOUND_CODE.
	 */
	public static final int NOT_FOUND_CODE = -1;
	/**
	 * VALIDATION_ERROR_CODE.
	 */
	public static final int VALIDATION_ERROR_CODE = -2;
	/**
	 * FAILURE_CODE.
	 */
	public static final int FAILURE_CODE = 9;
	/**
	 * SUCCESS.
	 */
	public static final MessageCode SUCCESS = new MessageCode(1, "调用成功");
	/**
	 * WARNING.
	 */
	public static final MessageCode WARNING = new MessageCode(2, "警告信息");
	/**
	 * NOT_FOUND.
	 */
	public static final MessageCode NOT_FOUND = new MessageCode(-1, "数据未找到");
	/**
	 * VALIDATION_ERROR.
	 */
	public static final MessageCode VALIDATION_ERROR = new MessageCode(-2, "校验错误");
	/**
	 * FAILURE.
	 */
	public static final MessageCode FAILURE = new MessageCode(9, "系统异常");
	private final int code;
	private final String description;

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (obj == this) {
			return true;
		} else if (obj.getClass() != this.getClass()) {
			return false;
		} else {
			MessageCode rhs = (MessageCode) obj;
			return (new EqualsBuilder()).append(this.code, rhs.getCode()).isEquals();
		}
	}

	public int hashCode() {
		return super.hashCode();
	}

	public int getCode() {
		return this.code;
	}

	public String getDescription() {
		return this.description;
	}

	protected MessageCode(final int code, final String description) {
		this.code = code;
		this.description = description;
	}
}
