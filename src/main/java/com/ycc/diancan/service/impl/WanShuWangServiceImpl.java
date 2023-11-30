/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.ycc.diancan.constant.SpiderConstants;
import com.ycc.diancan.definition.spider.WanShuWang;
import com.ycc.diancan.enums.SourceType;
import com.ycc.diancan.enums.WanShuNovelType;
import com.ycc.diancan.mapper.WanShuWangMapper;
import com.ycc.diancan.service.SpiderService;
import com.ycc.diancan.service.WanShuWangService;
import com.ycc.diancan.util.ConvertHelper;
import com.ycc.diancan.util.HtmlUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.List;

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
	private final WanShuWangMapper wanShuWangMapper;

	@Override
	public void startSpider() {
		log.info("start wan shu wang spider....");
		spiderWanShu();
	}


	private void spiderWanShu() {
		// 发起 HTTP GET 请求
		String htmlContent = HtmlUtils.getHtmlContentByUrl(SpiderConstants.WAN_SHU_WANG_URL);
		// 解析 HTML 内容
		if (htmlContent == null) {
			log.info("wan shu wang html is null...");
			return;
		}
		List<WanShuWang> wangShuWangList = parseWangShuHtml(htmlContent);
		if (CollectionUtils.isEmpty(wangShuWangList)) {
			return;
		}
		for (WanShuWang wanShuWang : wangShuWangList) {
			String detailSourceUrl = wanShuWang.getDetailSourceUrl();
			if (StringUtils.isBlank(detailSourceUrl)) {
				continue;
			}
			String novelHtmlContent = HtmlUtils.getHtmlContentByUrl(detailSourceUrl);
			if (StringUtils.isBlank(novelHtmlContent)) {
				continue;
			}
			analysisWanShuInfo(novelHtmlContent, wanShuWang);
			this.wanShuWangMapper.insert(wanShuWang);
		}
	}


	private void analysisWanShuInfo(String novelHtmlContent, WanShuWang wanShuWang) {
		Document novelHtmlDoc = Jsoup.parse(novelHtmlContent);
		Elements novelContentDetail = novelHtmlDoc.getElementsByClass("s2");
		Elements novelContentLinks = novelContentDetail.select("a[href]");
		for (Element novelContentLink : novelContentLinks) {
			String text = novelContentLink.text();
			if (!StringUtils.equals(text, "TXT下载")) {
				continue;
			}
			log.info("##### text: " + wanShuWang.getTitle());
			String downloadHref = novelContentLink.attr("href");
			wanShuWang.setDownloadSourceUrl(downloadHref);
		}
		Elements novelAuthorDetail = novelHtmlDoc.getElementsByClass("s1");
		Elements novelAuthorLinks = novelAuthorDetail.select("a[href]");
		for (Element novelAuthorLink : novelAuthorLinks) {
			String author = novelAuthorLink.text();
			log.info("##### author: " + author);
			if (StringUtils.isBlank(author)) {
				continue;
			}
			wanShuWang.setAuthor(author);
		}
		Element novelIntroDetail = novelHtmlDoc.getElementById("intro");
		String novelIntro = novelIntroDetail.text();
		if (StringUtils.isBlank(novelIntro)) {
			return;
		}
		wanShuWang.setDescription(novelIntro);
		log.info("##### novelIntro: " + novelIntro);
	}


	private static List<WanShuWang> parseWangShuHtml(String htmlContent) {
		List<WanShuWang> wanSHuWangList = Lists.newArrayList();
		// 使用 Jsoup 解析 HTML
		Document doc = Jsoup.parse(htmlContent);
		Element wrapperElements = doc.getElementById("wrapper");
		if (wrapperElements == null) {
			return wanSHuWangList;
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
				}
				if (StringUtils.isBlank(moduleName)) {
					continue;
				}
				log.info("##### moduleName: " + moduleName);
				// 解析小说类型
				moduleName = moduleName.replace("小说列表", "");
				String novelType = ConvertHelper.convertEnum(WanShuNovelType.class, moduleName, "小说类型");
				Elements links = doc.select("a[href]");
				for (Element link : links) {
					WanShuWang wanShuWang = new WanShuWang();
					wanShuWang.setWanShuNovelType(novelType);
					wanShuWang.setSourceType(SourceType.WAN_SHU_WANG.name());
					String href = link.attr("href");
					String title = link.attr("title");
					if (StringUtils.isBlank(title)) {
						continue;
					}
					wanShuWang.setDetailSourceUrl(href);
					wanShuWang.setTitle(title);
					wanSHuWangList.add(wanShuWang);
				}
			}
		}
		return wanSHuWangList;
	}

}
