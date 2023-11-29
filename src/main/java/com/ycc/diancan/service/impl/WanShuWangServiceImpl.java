/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ycc.diancan.constant.SpiderConstants;
import com.ycc.diancan.definition.spider.WanShuWang;
import com.ycc.diancan.enums.NovelChannel;
import com.ycc.diancan.mapper.WanShuWangMapper;
import com.ycc.diancan.service.SpiderService;
import com.ycc.diancan.service.WanShuWangService;
import com.ycc.diancan.util.HtmlUtils;
import com.ycc.diancan.util.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * WanShuWangServiceImpl.
 *
 * @author ycc
 * @date 2023-11-28 10:52:27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WanShuWangServiceImpl extends ServiceImpl<WanShuWangMapper, WanShuWang> implements WanShuWangService, SpiderService {

	@Override
	public void startSpider() {
		Map<String, List<WanShuWang>> stringListMap = spiderWanShu();
		log.info("总数: " + stringListMap.size());
	}


	private static Map<String, List<WanShuWang>> spiderWanShu() {
		Map<String, List<WanShuWang>> resultMap = Maps.newHashMap();
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
			List<WanShuWang> htmlModelList = resultMap.get(htmlModelKey);
			if (CollectionUtils.isEmpty(htmlModelList)) {
				htmlModelList = Lists.newArrayList();
				resultMap.put(htmlModelKey, htmlModelList);
			}
			List<Map<String, String>> novelsList = hrefTitleMap.getValue();
			if (CollectionUtils.isEmpty(novelsList)) {
				continue;
			}
			// for (Map<String, String> novelMap : novelsList) {
			// 	WanShuWang wanShuWang = new WanShuWang();
			// 	htmlModelList.add(wanShuWang);
			// 	String novelTitle = novelMap.get("title");
			// 	wanShuWang.setTitle(novelTitle);
			// 	String novelDetailHref = novelMap.get("href");
			// 	wanShuWang.setDetailSourceUrl(novelDetailHref);
			// 	String novelHtmlContent = HtmlUtils.getHtmlContentByUrl(novelDetailHref);
			// 	String novelDownloadUrl = getDownloadUrlByHtml(novelHtmlContent);
			// 	if (StringUtils.isBlank(novelDownloadUrl)) {
			// 		continue;
			// 	}
			// 	novelDetailMap.put("novelDownloadUrl", novelDownloadUrl);
			// }
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
