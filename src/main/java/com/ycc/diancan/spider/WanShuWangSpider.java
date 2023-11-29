/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.spider;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ycc.diancan.constant.SpiderConstants;
import com.ycc.diancan.util.HtmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Map;

/**
 * 万书网爬虫.
 *
 * @author ycc
 * @date 2023-11-23 11:24:39
 */
@Slf4j
public final class WanShuWangSpider {
	private WanShuWangSpider() {

	}


	public static void main(String[] args) {
		// 目标网站的 URL
		Map<String, List<Map<String, String>>> stringListMap = spiderWanShu();
		log.info("总数" + stringListMap.size());

	}

	private static Map<String, List<Map<String, String>>> spiderWanShu() {
		Map<String, List<Map<String, String>>> resultMap = Maps.newHashMap();
		// 发起 HTTP GET 请求
		String htmlContent = HtmlUtils.getHtmlContentByUrl(SpiderConstants.WAN_SHU_WANG_URL);
		// 解析 HTML 内容
		if (htmlContent == null) {
			return resultMap;
		}
		Map<String, List<Map<String, String>>> hrefTitleMapList = parseWangShuHtml(htmlContent);
		if (MapUtils.isEmpty(hrefTitleMapList)) {
			return resultMap;
		}
		for (Map.Entry<String, List<Map<String, String>>> hrefTitleMap : hrefTitleMapList.entrySet()) {
			String htmlModelKey = hrefTitleMap.getKey();
			List<Map<String, String>> htmlModelList = resultMap.get(htmlModelKey);
			if (CollectionUtils.isEmpty(htmlModelList)) {
				htmlModelList = Lists.newArrayList();
				resultMap.put(htmlModelKey, htmlModelList);
			}
			List<Map<String, String>> novelsList = hrefTitleMap.getValue();
			if (CollectionUtils.isEmpty(novelsList)) {
				continue;
			}
			for (Map<String, String> novelMap : novelsList) {
				Map<String, String> novelDetailMap = Maps.newHashMap();
				htmlModelList.add(novelDetailMap);
				String novelTitle = novelMap.get("title");
				novelDetailMap.put("novelTitle", novelTitle);
				String novelDetailHref = novelMap.get("href");
				novelDetailMap.put("novelDetailHref", novelDetailHref);
				String novelHtmlContent = HtmlUtils.getHtmlContentByUrl(novelDetailHref);
				String novelDownloadUrl = getDownloadUrlByHtml(novelHtmlContent);
				if (StringUtils.isBlank(novelDownloadUrl)) {
					continue;
				}
				novelDetailMap.put("novelDownloadUrl", novelDownloadUrl);
			}
		}
		return resultMap;
	}


	private static String getDownloadUrlByHtml(String novelHtmlContent) {
		Document novelHtmlDoc = Jsoup.parse(novelHtmlContent);
		Elements novelContentDetail = novelHtmlDoc.getElementsByClass("s2");
		Elements novelContentLinks = novelContentDetail.select("a[href]");
		for (Element novelContentLink : novelContentLinks) {
			String text = novelContentLink.text();
			if (!StringUtils.equals(text, "TXT下载")) {
				continue;
			}
			return novelContentLink.attr("href");
		}
		return null;
	}


	private static Map<String, List<Map<String, String>>> parseWangShuHtml(String htmlContent) {
		Map<String, List<Map<String, String>>> resultMap = Maps.newHashMap();
		// 使用 Jsoup 解析 HTML
		Document doc = Jsoup.parse(htmlContent);
		Element wrapperElements = doc.getElementById("wrapper");
		if (wrapperElements == null) {
			return resultMap;
		}
		List<Node> childNodes = wrapperElements.childNodes();
		for (Node childNode : childNodes) {
			// 判断节点类型
			if (childNode instanceof Element) {
				// 如果是元素节点，可以进一步处理
				Element childElement = (Element) childNode;
				// 例如，获取元素的标签名
				Element main = childElement.getElementById("main");
				if (main == null) {
					continue;
				}
				Elements h2Element = main.select("h2");
				String moduleName = null;
				if (!h2Element.isEmpty()) {
					Element h2 = h2Element.get(0);
					moduleName = h2.text();
					// System.out.println("##### h2 text : " + moduleName);
				}
				if (StringUtils.isBlank(moduleName)) {
					continue;
				}
				List<Map<String, String>> moduleList = resultMap.get(moduleName);
				if (CollectionUtils.isEmpty(moduleList)) {
					moduleList = Lists.newArrayList();
					resultMap.put(moduleName, moduleList);
				}
				Elements links = doc.select("a[href]");
				for (Element link : links) {
					Map<String, String> moduleMap = Maps.newHashMap();
					String href = link.attr("href");
					String title = link.attr("title");
					// System.out.println("Link: " + href + " Title: " + title);
					moduleMap.put("href", href);
					moduleMap.put("title", title);
					if (StringUtils.isBlank(title)) {
						continue;
					}
					moduleList.add(moduleMap);
				}
			}
		}
		return resultMap;
	}

}
