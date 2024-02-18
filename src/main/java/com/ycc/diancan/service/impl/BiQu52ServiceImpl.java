/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ycc.diancan.constant.SpiderConstants;
import com.ycc.diancan.definition.spider.BiQu52;
import com.ycc.diancan.enums.SourceType;
import com.ycc.diancan.mapper.BiQu52Mapper;
import com.ycc.diancan.service.BiQu52Service;
import com.ycc.diancan.service.SpiderService;
import com.ycc.diancan.util.HtmlUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * BiQu52ServiceImpl.
 *
 * @author ycc
 * @date 2023-12-04 09:49:53
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BiQu52ServiceImpl extends ServiceImpl<BiQu52Mapper, BiQu52> implements BiQu52Service, SpiderService {

	private final BiQu52Mapper biQU52Mapper;

	@Override
	public void startSpider() {
		log.info("start bi qu ge 5200 spider....");
		spiderBiQu52();
	}

	@Override
	public List<BiQu52> searchByTitle(String title) {
		return this.biQU52Mapper.selectByTitle(title);
	}

	@Override
	public List<BiQu52> searchByAuthor(String author) {
		return this.biQU52Mapper.selectByAuthor(author);
	}


	private void spiderBiQu52() {
		String pageUrl = SpiderConstants.BI_QU_52;
		Document doc = HtmlUtils.getHtmlContentSimple(pageUrl);
		if (doc == null) {
			return;
		}
		Elements bookListElements = doc.getElementsByClass("imgmain");
		Elements navSelects = bookListElements.select("a[href]");
		for (int i = 0; i < 2443; i++) {
			if (i != 0) {
				pageUrl = SpiderConstants.BI_QU_52 + "_" + (i + 1) + ".shtml";
				doc = HtmlUtils.getHtmlContentSimple(pageUrl);
				if (doc == null) {
					return;
				}
				bookListElements = doc.getElementsByClass("imgmain");
				navSelects = bookListElements.select("a[href]");
			}
			log.info("##### current url is {}", pageUrl);
			for (Element navSelect : navSelects) {
				String bookListType = navSelect.text();
				String href = navSelect.attr("href");
				if (StringUtils.isBlank(href)) {
					continue;
				}
				String bookListHref = SpiderConstants.BI_QU_52_BASE + href;
				parserBookLists(bookListHref, bookListType);
			}


		}
	}


	private void parserBookLists(String bookListUrl, String bookListType) {
		Document doc = HtmlUtils.getHtmlContentSimple(bookListUrl);
		if (doc == null) {
			return;
		}
		Elements imgmainElements = doc.getElementsByClass("imgmain");
		Elements selectElements = imgmainElements.select("a[href]");
		for (Element selectElement : selectElements) {
			BiQu52 biQu52 = generateEntity(bookListType);
			String title = selectElement.select("span").attr("title");
			String href = selectElement.attr("href");
			if (StringUtils.isAnyBlank(title, href)) {
				continue;
			}
			biQu52.setTitle(title);
			biQu52.setDetailSourceUrl(href);
			parserBiQu52Detail(biQu52);
			saveToDb(biQu52);
		}

		Elements textlistElements = doc.getElementsByClass("textlist");
		if (CollectionUtils.isEmpty(textlistElements)) {
			return;
		}
		Element textlistElement = textlistElements.get(0);
		Elements textlistEls = textlistElement.select("a[href]");
		for (Element textlistEl : textlistEls) {
			BiQu52 biQu52 = generateEntity(bookListType);
			String title = textlistEl.select("span").attr("title");
			String href = textlistEl.attr("href");
			if (StringUtils.isAnyBlank(title, href)) {
				return;
			}
			biQu52.setTitle(title);
			biQu52.setDetailSourceUrl(href);
			parserBiQu52Detail(biQu52);
			saveToDb(biQu52);
		}


	}

	private void parserBiQu52Detail(BiQu52 biQu52) {
		String detailSourceUrl = biQu52.getDetailSourceUrl();
		if (StringUtils.isBlank(detailSourceUrl)) {
			return;
		}
		Document detailContent = HtmlUtils.getHtmlContentSimple(detailSourceUrl);
		if (detailContent == null) {
			return;
		}
		Elements infoElements = detailContent.getElementsByClass("info");
		if (CollectionUtils.isEmpty(infoElements)) {
			log.warn("##### 找不到详情页：{}", detailSourceUrl);
			return;
		}
		Element infoElement = infoElements.get(0);
		Elements imgSelect = infoElement.select("img[src]");
		String imgSrc = imgSelect.attr("src");
		biQu52.setImgUrl(imgSrc);
		Elements titleElements = infoElement.getElementsByClass("title");
		Elements titleSelect = titleElements.select("a[href]");
		String author = titleSelect.text();
		biQu52.setAuthor(author);
		Elements pSelects = infoElement.select("p");
		for (Element pSelect : pSelects) {
			String text = pSelect.text();
			if (StringUtils.isBlank(text)) {
				continue;
			}
			boolean isDescription = false;
			boolean isSynopsis = false;
			boolean isText = false;
			boolean isMp3 = false;
			if (text.startsWith("小说简介")) {
				String description = text.replace("小说简介：", "");
				biQu52.setDescription(description);
				isDescription = true;
			}
			if (text.startsWith("内容摘要")) {
				String synopsis = text.replace("内容摘要：", "");
				biQu52.setSynopsis(synopsis);
				isSynopsis = true;
			}
			if (text.startsWith("TXT下载：")) {
				Elements select = pSelect.select("a[href]");
				biQu52.setDownloadSourceUrl(select.attr("href"));
				isText = true;
			}
			if (text.startsWith("MP3下载：")) {
				Elements select = pSelect.select("a[href]");
				biQu52.setMp3DownloadUrl(select.attr("href"));
				isMp3 = true;
			}
			if (isSynopsis && isMp3 && isDescription && isText) {
				return;
			}
		}
	}


	private BiQu52 generateEntity(String bookListType) {
		BiQu52 biQu52 = new BiQu52();
		biQu52.setSourceType(SourceType.BI_QU_52.name());
		biQu52.setBookListType(bookListType);
		return biQu52;
	}


	private void saveToDb(BiQu52 biQu52) {
		String title = biQu52.getTitle();
		String author = biQu52.getAuthor();
		String detailSourceUrl = biQu52.getDetailSourceUrl();
		List<BiQu52> existBiQu52s = this.biQU52Mapper.selectByTitleWithWrapper(title, author, detailSourceUrl);
		if (CollectionUtils.isNotEmpty(existBiQu52s)) {
			biQu52.setId(existBiQu52s.get(0).getId());
			this.biQU52Mapper.updateById(biQu52);
		} else {
			this.biQU52Mapper.insert(biQu52);
		}
	}
}
