/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 页面工具类.
 *
 * @author ycc
 * @date 2023-11-27 15:25:27
 */
public final class HtmlUtils {
	private HtmlUtils() {

	}

	/**
	 * 根据给定的URL获取HTML内容.
	 *
	 * @param url 待获取HTML内容的URL
	 * @return 获取到的HTML内容，如果获取失败则返回null
	 */
	public static String getHtmlContentByUrl(String url) {
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Content-Type", "text/plain; charset=UTF-8");
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(httpGet);
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));) {

			StringBuilder result = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				result.append(line);
			}
			return result.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
