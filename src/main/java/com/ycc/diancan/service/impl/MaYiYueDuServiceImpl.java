/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ycc.diancan.constant.SpiderConstants;
import com.ycc.diancan.definition.spider.MaYiYueDu;
import com.ycc.diancan.enums.MaYiYueDuType;
import com.ycc.diancan.enums.SourceType;
import com.ycc.diancan.mapper.MaYiYueDuMapper;
import com.ycc.diancan.service.MaYiYueDuService;
import com.ycc.diancan.service.SpiderService;
import com.ycc.diancan.util.ConvertHelper;
import com.ycc.diancan.util.HtmlUtils;
import com.ycc.diancan.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * MaYiYueDuServiceImpl.
 *
 * @author ycc
 * @date 2023-12-04 09:49:53
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MaYiYueDuServiceImpl extends ServiceImpl<MaYiYueDuMapper, MaYiYueDu> implements MaYiYueDuService, SpiderService {

	private final MaYiYueDuMapper maYiYueDuMapper;

	@Override
	public void startSpider() {
		log.info("start ma yi yue du spider....");
		spiderMaYiYueDu();
	}

	@Override
	public List<MaYiYueDu> searchByTitle(String title) {
		return this.maYiYueDuMapper.selectByTitle(title);
	}

	@Override
	public List<MaYiYueDu> searchByAuthor(String author) {
		return this.maYiYueDuMapper.selectByAuthor(author);
	}

	private void spiderMaYiYueDu() {
		Document htmlContent = HtmlUtils.getHtmlContentSimple(SpiderConstants.MA_YI_YUE_DU_URL);
		if (htmlContent == null) {
			return;
		}
		Map<String, String> navMap = getNavMap(htmlContent);
		for (Map.Entry<String, String> navKeyValueMap : navMap.entrySet()) {
			String navText = navKeyValueMap.getKey();
			String navHref = navKeyValueMap.getValue();
			Elements trElements = htmlContent.select("tr");
			int pageIndex = 1;
			while (CollectionUtils.isNotEmpty(trElements)) {
				for (int i = 1; i < trElements.size(); i++) {
					Element trElement = trElements.get(i);
					Elements tdElements = trElement.select("td");
					MaYiYueDu maYiYueDu = new MaYiYueDu();
					maYiYueDu.setSourceType(SourceType.MA_YI_YUE_DU.name());
					String novelTypeStr = tdElements.get(1).text();
					novelTypeStr = novelTypeStr.replace("[", "").replace("]", "");
					String novelType = ConvertHelper.convertEnumEn(MaYiYueDuType.class, novelTypeStr, "小说类别");
					maYiYueDu.setMaYiYueDuType(novelType);
					Element titleElement = tdElements.get(2);
					String title = titleElement.text();
					log.info("##### title is {}", title);
					if (StringUtils.isBlank(title)) {
						continue;
					}
					maYiYueDu.setTitle(title);
					Elements select = titleElement.select("a[href]");
					String detailSourceUrl = select.get(0).attr("href");
					maYiYueDu.setDetailSourceUrl(detailSourceUrl);
					String author = tdElements.get(5).text();
					maYiYueDu.setAuthor(author);
					analysisDetailHtml(maYiYueDu, detailSourceUrl);
					saveToDb(maYiYueDu);
				}
				pageIndex++;
				String nextHref = navHref.substring(0, navHref.lastIndexOf("/") + 1) + pageIndex + ".html";
				htmlContent = HtmlUtils.getHtmlContentSimple(nextHref);
				if (htmlContent == null) {
					break;
				}
				trElements = htmlContent.select("tr");
			}
			log.info("navText:{},navHref:{}", navText, navHref);
		}
	}

	private Map<String, String> getNavMap(Document htmlContent) {
		Elements navElements = htmlContent.getElementsByClass("list js-up-list");
		Elements navLinks = navElements.select("a[href]");
		Map<String, String> resultMap = Maps.newLinkedHashMap();
		for (Element navLink : navLinks) {
			String navText = navLink.text();
			String navHref = navLink.attr("href");
			if (StringUtils.isAnyBlank(navText, navHref)) {
				continue;
			}
			resultMap.put(navText, navHref);
		}
		return resultMap;
	}

	private void saveToDb(MaYiYueDu maYiYueDu) {
		String title = maYiYueDu.getTitle();
		String author = maYiYueDu.getAuthor();
		List<MaYiYueDu> maYiYueDus = this.maYiYueDuMapper.selectByTitleWithWrapper(title, author);
		if (CollectionUtils.isEmpty(maYiYueDus)) {
			this.maYiYueDuMapper.insert(maYiYueDu);
		} else {
			maYiYueDu.setId(maYiYueDus.get(0).getId());
			this.maYiYueDuMapper.updateById(maYiYueDu);
		}
	}

	private void analysisDetailHtml(MaYiYueDu maYiYueDu, String detailSourceUrl) {
		if (StringUtils.isBlank(detailSourceUrl)) {
			return;
		}
		Document detailHtmlContent = HtmlUtils.getHtmlContentSimple(detailSourceUrl);
		if (detailHtmlContent == null) {
			return;
		}
		Elements briefBreviary = detailHtmlContent.getElementsByClass("brief breviary");
		String description = briefBreviary.text();
		if (StringUtils.isNotBlank(description)) {
			maYiYueDu.setDescription(description);
		}
		Elements arctileRightd = detailHtmlContent.getElementsByClass("arctile_rightd");
		Elements pElements = arctileRightd.select("p");
		for (Element pElement : pElements) {
			String pText = pElement.text();
			if (!StringUtils.equals(pText, "下载本书TXT")) {
				continue;
			}
			Elements select = pElement.select("a[href]");
			String href = select.get(0).attr("href");
			if (StringUtils.isNotBlank(href)) {
				maYiYueDu.setDownloadSourceUrl(SpiderConstants.MA_YI_YUE_DU_HOME_URL + href);
				analysisDownloadUrl(maYiYueDu, maYiYueDu.getDownloadSourceUrl());
			}
		}
	}


	private void analysisDownloadUrl(MaYiYueDu maYiYueDu, String downloadUrlPath) {
		Document downloadHtmlContent = HtmlUtils.getHtmlContentSimple(downloadUrlPath);
		if (downloadHtmlContent == null) {
			return;
		}
		Elements dataList = downloadHtmlContent.getElementsByClass("data_list");
		Set<String> downloadUrls = Sets.newHashSet();
		for (Element element : dataList) {
			Elements downlaodSelect = element.select("a[href]");
			if (CollectionUtils.isEmpty(downlaodSelect)) {
				continue;
			}
			for (Element downlaodElement : downlaodSelect) {
				String downloadUrl = downlaodElement.attr("href");
				if (StringUtils.isBlank(downloadUrl)) {
					continue;
				}
				downloadUrls.add(downloadUrl);
			}
		}
		if (CollectionUtils.isNotEmpty(downloadUrls)) {
			maYiYueDu.setDownloadUrls(JsonUtils.convertObject2JSON(new ArrayList<>(downloadUrls)));
		}
	}
}
