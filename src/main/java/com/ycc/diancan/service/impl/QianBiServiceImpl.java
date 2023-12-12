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
import com.ycc.diancan.definition.spider.QianBi;
import com.ycc.diancan.enums.QianBiType;
import com.ycc.diancan.enums.SourceType;
import com.ycc.diancan.mapper.QianBiMapper;
import com.ycc.diancan.service.QianBiService;
import com.ycc.diancan.service.SpiderService;
import com.ycc.diancan.util.ContentsUtils;
import com.ycc.diancan.util.ConvertHelper;
import com.ycc.diancan.util.HtmlUtils;
import com.ycc.diancan.util.JsonUtils;
import com.ycc.diancan.vo.BookSection;
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
import java.util.Objects;

/**
 * BaShiShuKuServiceImpl.
 *
 * @author ycc
 * @date 2023-12-04 09:49:53
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QianBiServiceImpl extends ServiceImpl<QianBiMapper, QianBi> implements QianBiService, SpiderService {

	private final QianBiMapper qianBiMapper;

	@Override
	public void startSpider() {
		log.info("start qian bi spider....");
		spiderQianBi();
	}

	@Override
	public List<QianBi> searchByTitle(String title) {
		return this.qianBiMapper.selectByTitle(title);
	}

	@Override
	public List<QianBi> searchByAuthor(String author) {
		return this.qianBiMapper.selectByAuthor(author);
	}

	private void spiderQianBi() {
		Document doc = HtmlUtils.getHtmlContentSimple(SpiderConstants.QIAN_BI_URL);
		if (doc == null) {
			return;
		}
		Elements navElements = doc.getElementsByClass("nav");
		Elements navSelects = navElements.select("a[href]");
		Map<String, String> navsMap = Maps.newLinkedHashMap();
		for (Element navSelect : navSelects) {
			String text = navSelect.text();
			if (StringUtils.equalsAny(text, "首页", "排行榜", "书库")) {
				continue;
			}
			String href = navSelect.attr("href");
			if (StringUtils.isBlank(href)) {
				continue;
			}
			href = SpiderConstants.QIAN_BI_URL + href;
			navsMap.put(text, href);
			log.info("##### nav is {}, href is {}", text, href);
		}
		if (MapUtils.isEmpty(navsMap)) {
			return;
		}
		for (Map.Entry<String, String> navMap : navsMap.entrySet()) {
			String navKey = navMap.getKey();
			if (StringUtils.isBlank(navKey)) {
				continue;
			}
			String qianBiType = ConvertHelper.convertEnumEn(QianBiType.class, navKey, "小说类型");
			log.info("##### start handle : {}， type is {}", navKey, qianBiType);
			List<QianBi> qianBiTypes = parserNovelType(qianBiType, navMap.getValue());
			if (CollectionUtils.isEmpty(qianBiTypes)) {
				continue;
			}
			// log.info("##### start save {}, size is {}", navKey, otherBaShiShuKus.size());
			// saveToDb(otherBaShiShuKus);
		}

	}

	private List<QianBi> parserNovelType(String qianBiType, String novelTypeUrl) {
		List<QianBi> qianBis = Lists.newArrayList();
		if (StringUtils.isBlank(novelTypeUrl)) {
			return qianBis;
		}
		Document htmlContent = HtmlUtils.getHtmlContentSimple(novelTypeUrl);
		if (Objects.isNull(htmlContent)) {
			return qianBis;
		}
		Elements lastElements = htmlContent.getElementsByClass("last");
		String totalPage = lastElements.text();
		if (StringUtils.isBlank(totalPage)) {
			return qianBis;
		}
		StringBuilder novelTypeUrlBuilder = new StringBuilder(novelTypeUrl);
		for (int i = 0; i < Integer.parseInt(totalPage); i++) {
			if (i != 0) {
				novelTypeUrlBuilder.append(i + 1).append("/");
				htmlContent = HtmlUtils.getHtmlContentSimple(novelTypeUrlBuilder.toString());
				if (Objects.isNull(htmlContent)) {
					return qianBis;
				}
			}
			qianBis.addAll(parserQianBiNovel(qianBiType, htmlContent));
		}
		return qianBis;

	}

	private List<QianBi> parserQianBiNovel(String qianBiType, Document htmlContent) {
		List<QianBi> qianBis = Lists.newArrayList();
		Element sitebox = htmlContent.getElementById("sitebox");
		Elements dlElements = sitebox.select("dl");
		if (CollectionUtils.isNotEmpty(dlElements)) {
			return qianBis;
		}
		for (Element dlElement : dlElements) {
			QianBi qianBi = generateEntity(qianBiType);
			qianBis.add(qianBi);
			Elements dt = dlElement.select("dt");
			Elements selects = dt.select("a[href]");
			if (CollectionUtils.isNotEmpty(selects)) {
				String href = selects.get(0).attr("href");
				if (StringUtils.isNotBlank(href)) {
					qianBi.setImgUrl(SpiderConstants.QIAN_BI_URL + href);
				}
			}
			Elements h3 = dlElement.select("h3");
			Elements select = h3.select("a[href]");
			if (CollectionUtils.isNotEmpty(select)) {
				String text = select.get(0).text();
				if (StringUtils.isBlank(text)) {
					continue;
				}
				qianBi.setTitle(text);
				String href = select.get(0).attr("href");
				if (StringUtils.isNotBlank(href)) {
					qianBi.setDetailSourceUrl(SpiderConstants.QIAN_BI_URL + href);
				}
			}
			analysisQianBiDetail(qianBi, qianBi.getDetailSourceUrl());

		}
		return qianBis;


	}

	private void analysisQianBiDetail(QianBi qianBi, String detailSourceUrl) {
		if (StringUtils.isBlank(detailSourceUrl)) {
			return;
		}
		Document qianBiDetail = HtmlUtils.getHtmlContentSimple(detailSourceUrl);
		Element countElement = qianBiDetail.getElementById("count");
		Elements select = countElement.select("a[href]");
		if (CollectionUtils.isNotEmpty(select)) {
			String author = select.get(0).text();
			if (StringUtils.isNotBlank(author)) {
				qianBi.setAuthor(author);
			}
		}
		Elements elementsByClass = qianBiDetail.getElementsByClass("hm-scroll");
		String description = elementsByClass.text();
		if (StringUtils.isNotBlank(description)) {
			qianBi.setDescription(description);
		}
		Element chapterList = qianBiDetail.getElementById("chapterList");
		Elements liElements = chapterList.select("li");
		Map<String, String> novelDetailMap = Maps.newLinkedHashMap();
		for (Element liElement : liElements) {
			Elements links = liElement.select("a[href]");
			if (CollectionUtils.isNotEmpty(links)) {
				String text = links.get(0).text();
				String href = links.get(0).attr("href");
				if (StringUtils.isAllBlank(text, href)) {
					continue;
				}
				novelDetailMap.put(text, SpiderConstants.QIAN_BI_URL + href);
			}
		}
		if (MapUtils.isEmpty(novelDetailMap)) {
			return;
		}
		int index = 0;
		List<BookSection> bookSections = Lists.newArrayList();
		for (Map.Entry<String, String> novelDetail : novelDetailMap.entrySet()) {
			bookSections.add(obtainNovelContents(novelDetail.getKey(), novelDetail.getValue(), index));
			index++;
		}
		if (CollectionUtils.isNotEmpty(bookSections)) {
			qianBi.setContents(JsonUtils.convertObject2JSON(bookSections));
		}


	}

	private BookSection obtainNovelContents(String title, String sectionUrl, int index) {
		BookSection bookSection = new BookSection();
		bookSection.setIndex(index);
		bookSection.setSectionIndex(ContentsUtils.parseSectionIndex(title));
		bookSection.setTitle(title);
		Document contentHtml = HtmlUtils.getHtmlContentSimple(sectionUrl);
		if (Objects.isNull(contentHtml)) {
			return bookSection;
		}
		Elements elementsByClass = contentHtml.getElementsByClass("read-content");
		Elements pElements = elementsByClass.select("p");
		String content = pElements.text();
		if (StringUtils.isNotBlank(content)) {
			bookSection.setContent(content);
		}
		return bookSection;
	}

	private QianBi generateEntity(String qianBiType) {
		QianBi qianBi = new QianBi();
		qianBi.setSourceType(SourceType.QIAN_BI.name());
		qianBi.setQianBiType(qianBiType);
		return qianBi;
	}

	private void saveToDb(List<QianBi> qianBis) {
		qianBis.forEach(qianbi -> {
			String title = qianbi.getTitle();
			String author = qianbi.getAuthor();
			List<QianBi> existQianBis = this.qianBiMapper.selectByTitleWithWrapper(title, author);
			if (CollectionUtils.isNotEmpty(existQianBis)) {
				qianbi.setId(existQianBis.get(0).getId());
				this.qianBiMapper.updateById(qianbi);
			} else {
				this.qianBiMapper.insert(qianbi);
			}
		});
	}
}
