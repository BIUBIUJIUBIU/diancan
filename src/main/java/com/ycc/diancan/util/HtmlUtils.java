/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 页面工具类.
 *
 * @author ycc
 * @date 2023-11-27 15:25:27
 */
@Slf4j
public final class HtmlUtils {
	private HtmlUtils() {

	}

	/**
	 * 根据给定的URL获取HTML内容.
	 *
	 * @param targetUrl 待获取HTML内容的URL
	 * @return 获取到的HTML内容，如果获取失败则返回null
	 */
	public static String getHtmlContentByUrl(String targetUrl) {
		HttpGet httpGet = new HttpGet(targetUrl);
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
			log.error("获取HTML内容失败, 链接地址: {}, 错误原因: {}", targetUrl, e.getMessage());
		}
		return null;
	}

	/**
	 * 根据给定的URL获取HTML内容(简单操作).
	 *
	 * @param targetUrl 待获取HTML内容的URL
	 * @return Document
	 */
	public static Document getHtmlContentSimple(String targetUrl) {
		try {
			return Jsoup.connect(targetUrl).timeout(50000).get();
		} catch (IOException e) {
			log.error("获取HTML内容失败, 链接地址: {}, 错误原因: {}", targetUrl, e.getMessage());
		}
		return null;
	}
}
