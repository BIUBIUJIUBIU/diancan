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
import com.ycc.diancan.definition.spider.SevenZBook;
import com.ycc.diancan.enums.SevenZBookType;
import com.ycc.diancan.enums.SourceType;
import com.ycc.diancan.mapper.SevenZBookMapper;
import com.ycc.diancan.service.SevenZBookService;
import com.ycc.diancan.service.SpiderService;
import com.ycc.diancan.util.ConvertHelper;
import com.ycc.diancan.util.FormatUtils;
import com.ycc.diancan.util.HtmlUtils;
import com.ycc.diancan.util.JsonUtils;
import com.ycc.diancan.vo.SevenZBookSection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SevenZBookServiceImpl.
 *
 * @author ycc
 * @date 2023-12-08 15:10:30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SevenZBookServiceImpl extends ServiceImpl<SevenZBookMapper, SevenZBook> implements SevenZBookService, SpiderService {

	private final SevenZBookMapper sevenZBookMapper;

	@Override
	public void startSpider() {
		log.info("start 7z book spider....");
		spiderSevenZBook();
	}

	private void spiderSevenZBook() {
		Document htmlContent = HtmlUtils.getHtmlContentSimple(SpiderConstants.SEVEN_Z_BOOK_URL);
		if (htmlContent == null) {
			return;
		}
		Elements subnav = htmlContent.getElementsByClass("subnav");
		Elements links = subnav.select("a[href]");
		List<String> novelTypeAllAlias = getNovelTypeAllAlias();
		Map<String, String> navsMap = Maps.newLinkedHashMap();
		for (Element link : links) {
			String text = link.text();
			if (StringUtils.isEmpty(text) || !novelTypeAllAlias.contains(text)) {
				continue;
			}
			String href = link.attr("href");
			navsMap.put(text, href);
		}

		parseTypeOfNovel(navsMap);
	}


	private void parseTypeOfNovel(Map<String, String> navsMap) {
		if (MapUtils.isEmpty(navsMap)) {
			return;
		}
		for (Map.Entry<String, String> navMap : navsMap.entrySet()) {
			String novelTypeStr = navMap.getKey();
			String novelType = ConvertHelper.convertEnumEn(SevenZBookType.class, novelTypeStr, "小说类别");
			String novelTypeUrl = navMap.getValue();
			String sevenZBookTypeUrl = SpiderConstants.SEVEN_Z_BOOK_URL + novelTypeUrl;
			SevenZBook sevenZBook = generateSevenZBook(novelType);
			analysisSevenZBookInfo(sevenZBookTypeUrl, sevenZBook);
		}
	}

	private void analysisSevenZBookInfo(String sevenZBookTypeUrl, SevenZBook sevenZBook) {
		Document htmlContent = HtmlUtils.getHtmlContentSimple(sevenZBookTypeUrl);
		if (htmlContent == null) {
			return;
		}
		Elements last = htmlContent.getElementsByClass("last");
		int totalPage = obtainTotalPage(last);
		for (int i = 0; i < totalPage; i++) {
			if (i == 1) {
				String nextPageUrl = sevenZBookTypeUrl.substring(0, sevenZBookTypeUrl.lastIndexOf("/") + 1) + (i + 1) + ".html";
				htmlContent = HtmlUtils.getHtmlContentSimple(nextPageUrl);
				if (htmlContent == null) {
					continue;
				}
			}
			Elements dlElements = htmlContent.select("dl");
			for (Element dlElement : dlElements) {
				Elements dt = dlElement.getElementsByClass("cover");
				Elements imgs = dt.select("img[src]");
				if (CollectionUtils.isEmpty(imgs)) {
					continue;
				}
				String srcText = imgs.get(0).attr("src");
				if (StringUtils.isNotBlank(srcText)) {
					sevenZBook.setImgUrl(srcText);
				}
				Elements dd = dlElement.select("dd");
				Elements h4 = dd.select("h4");
				Elements select = h4.select("a[href]");
				if (CollectionUtils.isEmpty(select)) {
					continue;
				}
				Elements span = h4.select("span");
				sevenZBook.setAuthor(span.text());
				String title = select.get(0).text();
				sevenZBook.setTitle(title);
				String detailUrl = select.get(0).attr("href");
				sevenZBook.setDetailSourceUrl(detailUrl);
				Elements pElements = dlElement.getElementsByClass("gray");
				sevenZBook.setDescription(pElements.text());
				analysisNovelContent(sevenZBook);
				saveToDb(sevenZBook);
			}
		}
	}

	private void analysisNovelContent(SevenZBook sevenZBook) {
		String detailSourceUrl = sevenZBook.getDetailSourceUrl();
		if (StringUtils.isBlank(detailSourceUrl)) {
			return;
		}
		Document detailHtmlContent = HtmlUtils.getHtmlContentSimple(detailSourceUrl);
		if (detailHtmlContent == null) {
			return;
		}
		Elements ddElements = detailHtmlContent.select("dd");
		Elements selects = ddElements.select("a[href]");
		Map<String, String> sectionsMap = Maps.newLinkedHashMap();
		for (Element selectElement : selects) {
			String text = selectElement.text();
			if (StringUtils.isBlank(text)) {
				continue;
			}
			sectionsMap.put(text, selectElement.attr("href"));
		}
		if (MapUtils.isEmpty(sectionsMap)) {
			return;
		}
		List<SevenZBookSection> sevenZBookSections = Lists.newArrayList();
		for (Map.Entry<String, String> sectionMap : sectionsMap.entrySet()) {
			SevenZBookSection sevenZBookSection = new SevenZBookSection();
			sevenZBookSections.add(sevenZBookSection);
			String sectionName = sectionMap.getKey();
			sevenZBookSection.setTitle(sectionName);
			int index = parseSectionIndex(sectionName);
			if (index != -1) {
				sevenZBookSection.setIndex(index);
			}
			String value = sectionMap.getValue();
			if (StringUtils.isNotBlank(value)) {
				Document htmlContentSimple = HtmlUtils.getHtmlContentSimple(SpiderConstants.SEVEN_Z_BOOK_URL + value);
				if (htmlContentSimple == null) {
					continue;
				}
				String content = obtainNovelContent(htmlContentSimple);
				if (StringUtils.isNotBlank(content)) {
					sevenZBookSection.setContent(content);
				}
			}
		}
		if (CollectionUtils.isNotEmpty(sevenZBookSections)) {
			sevenZBook.setContents(JsonUtils.convertObject2JSON(sevenZBookSections));
		}
	}

	private String obtainNovelContent(Document novelContent) {
		Elements content = novelContent.getElementsByClass("content");
		return content.text();
	}

	private int parseSectionIndex(String sectionName) {
		Pattern pattern = Pattern.compile("第(.*?)章");
		Matcher matcher = pattern.matcher(sectionName);
		if (matcher.find()) {
			String group = matcher.group(1);
			return FormatUtils.convertToLowerCaseNumber(group.replace("第", "").replace("章", ""));
		} else {
			return -1;
		}
	}

	private int obtainTotalPage(Elements lastElement) {
		if (lastElement == null) {
			return -1;
		}
		String text = lastElement.text();
		return Integer.parseInt(text);
	}

	private SevenZBook generateSevenZBook(String novelType) {
		SevenZBook sevenZBook = new SevenZBook();
		sevenZBook.setSourceType(SourceType.SEVEN_Z_BOOK.name());
		sevenZBook.setSevenZBookType(novelType);
		return sevenZBook;
	}

	public List<String> getNovelTypeAllAlias() {
		SevenZBookType[] values = SevenZBookType.values();
		List<String> result = Lists.newArrayList();
		for (SevenZBookType value : values) {
			result.add(value.getAlias());
		}
		return result;
	}

	private void saveToDb(SevenZBook sevenZBook) {
		String title = sevenZBook.getTitle();
		String author = sevenZBook.getAuthor();
		List<SevenZBook> sevenZBooks = this.sevenZBookMapper.selectByTitleWithWrapper(title, author);
		if (CollectionUtils.isEmpty(sevenZBooks)) {
			this.sevenZBookMapper.insert(sevenZBook);
		} else {
			sevenZBook.setId(sevenZBooks.get(0).getId());
			this.sevenZBookMapper.updateById(sevenZBook);
		}
	}
}
