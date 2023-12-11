/*
 * Copyright 2004-2020 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.apache.commons.lang3.StringUtils;

public final class EncryptStringHelper {
	private EncryptStringHelper() {
	}

	/**
	 * ENCRYPT_PREFIX.
	 */
	public static final String ENCRYPT_PREFIX = "Encrypt:";
	/**
	 * ENCRYPT_KEY.
	 */
	public static final String ENCRYPT_KEY = "ylky4CM5IkwuX68UGwvKBw==";

	public static String encrypt(String raw) {
		if (StringUtils.isBlank(raw) || StringUtils.startsWith(raw, ENCRYPT_PREFIX)) {
			return raw;
		}
		SymmetricCrypto sm4 = SmUtil.sm4(Base64.decode(ENCRYPT_KEY));
		return ENCRYPT_PREFIX + sm4.encryptBase64(raw);
	}

	public static String decrypt(String encryptStr) {
		if (StringUtils.isBlank(encryptStr) || !StringUtils.startsWith(encryptStr, ENCRYPT_PREFIX)) {
			return encryptStr;
		}
		encryptStr = StringUtils.replaceOnce(encryptStr, ENCRYPT_PREFIX, "");
		SymmetricCrypto sm4 = SmUtil.sm4(Base64.decode(ENCRYPT_KEY));
		return sm4.decryptStr(encryptStr);
	}

	public static void main(String[] args) {
		String ycc = encrypt("ycc");
		String decrypt = decrypt(ycc);
		System.out.println(ycc);
		System.out.println(decrypt);

	}

}
