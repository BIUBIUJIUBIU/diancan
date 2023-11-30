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
import com.ycc.diancan.definition.spider.ZeiShuBa;
import com.ycc.diancan.enums.NovelChannel;
import com.ycc.diancan.enums.SourceType;
import com.ycc.diancan.mapper.ZeiShuBaMapper;
import com.ycc.diancan.service.SpiderService;
import com.ycc.diancan.service.ZeiShuBaService;
import com.ycc.diancan.util.ConvertHelper;
import com.ycc.diancan.util.HtmlUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;


/**
 * ZeiShuBaServiceImpl.
 *
 * @author ycc
 * @date 2023-11-28 10:52:27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ZeiShuBaServiceImpl extends ServiceImpl<ZeiShuBaMapper, ZeiShuBa> implements ZeiShuBaService, SpiderService {

	@Override
	public void startSpider() {
		log.info("start zei shu ba spider....");
		spiderZeiShu();

	}

	private void spiderZeiShu() {
		// 发起 HTTP GET 请求
		String htmlContent = HtmlUtils.getHtmlContentByUrl(SpiderConstants.ZEI_SHU_WANG_URL);
		Map<String, List<Map<String, String>>> modelListMap = parseZeiShuHtml(htmlContent);
		for (Map.Entry<String, List<Map<String, String>>> modelListKeyMap : modelListMap.entrySet()) {
			String modelName = modelListKeyMap.getKey();
			modelName = modelName.replace("：", "");
			String novelChannel = ConvertHelper.convertEnum(NovelChannel.class, modelName, "小说频道");
			log.info("小说频道: " + novelChannel);
			List<Map<String, String>> modelDetailList = modelListKeyMap.getValue();
			if (CollectionUtils.isEmpty(modelDetailList)) {
				continue;
			}
			for (Map<String, String> modelDetailMap : modelDetailList) {
				ZeiShuBa zeiShuBa = new ZeiShuBa();
				zeiShuBa.setSourceType(SourceType.ZEI_SHU_BA.name());
				zeiShuBa.setNovelChannel(novelChannel);
				String modelHref = modelDetailMap.get("modelHref");
				String modelListContent = HtmlUtils.getHtmlContentByUrl(modelHref);
				String modelNameKey = modelDetailMap.get("modelName");
				log.info("#### modelName: " + modelNameKey);
				// List<Map<String, String>> modelNameList = modelDetailsMap.get(modelNameKey);
				// if (CollectionUtils.isEmpty(modelNameList)) {
				// 	modelNameList = Lists.newArrayList();
				// 	modelDetailsMap.put(modelNameKey, modelNameList);
				// }
				// modelDetailsMap.get(modelNameKey).addAll(parseModelListHtmlContent(modelListContent, modelHref));
				log.info("success");
			}
		}
	}

	private List<Map<String, String>> parseModelListHtmlContent(String modelListContent, String modelHref) {
		List<Map<String, String>> resultList = Lists.newArrayList();
		Document pageDoc = Jsoup.parse(modelListContent);
		Elements pageinfo = pageDoc.getElementsByClass("pageinfo");
		Elements strong = pageinfo.select("strong");
		String modelListTotalStr = strong.text();
		int modelListTotal = Integer.parseInt(modelListTotalStr);
		int calculatePageCount = calculatePageCount(modelListTotal);
		for (int i = 1; i <= calculatePageCount; i++) {
			// 分页读取内容
			String pageHtmlPrefix = "index";
			String pageHtmlSuffix = ".html";
			String pageHtmlUrl;
			if (i == 1) {
				pageHtmlUrl = modelHref + "/" + pageHtmlPrefix + pageHtmlSuffix;
			} else {
				pageHtmlUrl = modelHref + "/" + pageHtmlPrefix + "_" + i + pageHtmlSuffix;
			}
			String pageHtmlContent = HtmlUtils.getHtmlContentByUrl(pageHtmlUrl);
			if (StringUtils.isBlank(pageHtmlContent)) {
				log.error("pageHtmlContent is blank");
				continue;
			}
			Document doc = Jsoup.parse(pageHtmlContent);
			Elements listBoxElements = doc.getElementsByClass("listbox");
			Elements liElements = listBoxElements.select("li");
			for (Element liElement : liElements) {
				Map<String, String> novelDetailMap = Maps.newHashMap();
				resultList.add(novelDetailMap);
				Elements dlElements = liElement.select("dl");
				Elements links = dlElements.select("a[href]");
				for (Element link : links) {
					String bookDetailHref = link.attr("href");
					String bookName = link.text();
					if (StringUtils.containsAny(bookName, "《", "》")) {
						Matcher matcher = SpiderConstants.CUT_OUT_BOOK_NAME.matcher(bookName);
						if (matcher.find()) {
							bookName = matcher.group(1);
						}
					}
					novelDetailMap.put("title", bookName);
					novelDetailMap.put("bookDetailHref", modelHref + bookDetailHref);
				}
				Elements descriptionElements = liElement.getElementsByClass("intro");
				String description = descriptionElements.text();
				novelDetailMap.put("description", description);

			}
		}
		log.info("success");
		return resultList;


	}

	private static Map<String, List<Map<String, String>>> parseZeiShuHtml(String htmlContent) {
		Map<String, List<Map<String, String>>> resultMap = Maps.newHashMap();
		// 使用 Jsoup 解析 HTML
		Document doc = Jsoup.parse(htmlContent);
		Elements toplantxts = doc.getElementsByClass("toplantxt");
		for (Element toplantxt : toplantxts) {
			Elements modelNameElements = toplantxt.getElementsByClass("name");
			String modelName = modelNameElements.text();
			if (!StringUtils.equalsAny(modelName, "男频小说：", "女频女主：")) {
				continue;
			}
			List<Map<String, String>> modelList = resultMap.get(modelName);
			if (CollectionUtils.isEmpty(modelList)) {
				modelList = Lists.newArrayList();
				resultMap.put(modelName, modelList);
			}
			Elements modelContent = toplantxt.getElementsByClass("c6");
			Elements links = modelContent.select("a[href]");
			for (Element link : links) {
				Map<String, String> modelLinkMap = Maps.newHashMap();
				modelList.add(modelLinkMap);
				String href = link.attr("href");
				href = SpiderConstants.ZEI_SHU_WANG_URL + href;
				String text = link.text();
				modelLinkMap.put("modelName", text);
				modelLinkMap.put("modelHref", href);
			}
		}
		return resultMap;
	}

	private static int calculatePageCount(int total) {
		return (int) Math.ceil((double) total / 15);
	}

}
