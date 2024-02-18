package com.ycc.diancan.result;

import lombok.Data;

@Data
public class Message<T> {
	private int code; // 返回码
	private String description; // 描述
	private T result; // 返回结果

	public static <Y> Message<Y> success() {
		return new Message<>(MessageCode.SUCCESS);
	}

	public static <T> Message<T> success(T result) {
		return new Message<>(MessageCode.SUCCESS, result);
	}

	public static <Y> Message<Y> fail(int code, String description) {
		return new Message<>(code, description);
	}

	public static <Y> Message<Y> fail(MessageCode messageCode) {
		return new Message<>(messageCode);
	}

	public static <Y> Message<Y> of(int code, String description) {
		return new Message<>(code, description);
	}

	public static <T> Message<T> of(int code, String description, T result) {
		return new Message<>(code, description, result);
	}


	public Message() {
	}

	public Message(MessageCode messageCode) {
		this.code = messageCode.getCode();
		this.description = messageCode.getDescription();
	}

	public Message(MessageCode messageCode, T result) {
		this.code = messageCode.getCode();
		this.description = messageCode.getDescription();
		this.result = result;
	}

	public Message(int code) {
		this.code = code;
	}

	public Message(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public Message(int code, String description, T result) {
		this.code = code;
		this.description = description;
		this.result = result;
	}
}
