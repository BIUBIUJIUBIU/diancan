/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.ycc.diancan.constant.SpiderConstants;
import com.ycc.diancan.definition.spider.JianShiShu;
import com.ycc.diancan.enums.JianShiShuType;
import com.ycc.diancan.enums.SourceType;
import com.ycc.diancan.mapper.JianShiShuMapper;
import com.ycc.diancan.service.JianShiShuService;
import com.ycc.diancan.service.SpiderService;
import com.ycc.diancan.util.ConvertHelper;
import com.ycc.diancan.util.HtmlUtils;
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

/**
 * JianShiShuServiceImpl.
 *
 * @author ycc
 * @date 2023-12-04 09:49:53
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JianShiShuServiceImpl extends ServiceImpl<JianShiShuMapper, JianShiShu> implements JianShiShuService, SpiderService {

	private final JianShiShuMapper jianShiShuMapper;

	@Override
	public void startSpider() {
		log.info("start jain shi shu spider....");
		spiderJianShiShu();
	}

	@Override
	public List<JianShiShu> searchByTitle(String title) {
		return this.jianShiShuMapper.selectByTitle(title);
	}

	@Override
	public List<JianShiShu> searchByAuthor(String author) {
		return this.jianShiShuMapper.selectByAuthor(author);
	}


	private void spiderJianShiShu() {
		Document doc = HtmlUtils.getHtmlContentSimple(SpiderConstants.JIAN_SHI_SHU_BASE);
		if (doc == null) {
			return;
		}
		Elements navElements = doc.getElementsByClass("nav");
		Elements navSelects = navElements.select("a[href]");
		Map<String, String> navsMap = Maps.newLinkedHashMap();
		for (Element navSelect : navSelects) {
			String text = navSelect.text();
			if (StringUtils.equalsAny(text, "首页", "我的书架", "作品分类", "榜单作品")) {
				continue;
			}
			String href = navSelect.attr("href");
			if (StringUtils.isBlank(href)) {
				continue;
			}
			href = SpiderConstants.JIAN_SHI_SHU_BASE + href;
			navsMap.put(text, href);
			log.info("##### nav is {}, href is {}", text, href);
		}
		if (MapUtils.isEmpty(navsMap)) {
			return;
		}
		for (Map.Entry<String, String> navMap : navsMap.entrySet()) {
			String navKey = navMap.getKey();
			log.info("##### start handle : {}", navKey);
			parserJianShiShus(navKey, navMap.getValue());
		}
	}


	private void parserJianShiShus(String navType, String jianShiTypeUrl) {
		Document doc = HtmlUtils.getHtmlContentSimple(jianShiTypeUrl);
		if (doc == null) {
			return;
		}
		int pageIndex = 1;
		Element newscontent = doc.getElementById("newscontent");
		if (newscontent == null) {
			return;
		}
		Elements contentElements = newscontent.getElementsByClass("l");
		Elements liElements = contentElements.select("li");
		while (CollectionUtils.isNotEmpty(liElements)) {
			log.info("##### current url is {}", jianShiTypeUrl);
			for (Element liElement : liElements) {
				JianShiShu jianShiShu = generateEntity(navType);
				Elements titleElements = liElement.getElementsByClass("s2");
				if (CollectionUtils.isEmpty(titleElements)) {
					continue;
				}
				Element spanElement = titleElements.get(0);
				Elements titleElement = spanElement.select("a[href]");
				String title = titleElement.text();
				if (StringUtils.isBlank(title)) {
					continue;
				}
				jianShiShu.setTitle(title);
				String detailSourceUrl = titleElement.attr("href");

				jianShiShu.setDetailSourceUrl(SpiderConstants.JIAN_SHI_SHU_BASE + detailSourceUrl);
				parserJianShiShusDetail(jianShiShu);
				saveToDb(jianShiShu);
			}
			pageIndex++;
			jianShiTypeUrl = obtainNextDetailUrl(jianShiTypeUrl, pageIndex);
			if (StringUtils.isBlank(jianShiTypeUrl)) {
				return;
			}
			doc = HtmlUtils.getHtmlContentSimple(jianShiTypeUrl);
			if (doc == null) {
				return;
			}
			newscontent = doc.getElementById("newscontent");
			if (newscontent == null) {
				return;
			}
			contentElements = newscontent.getElementsByClass("l");
			liElements = contentElements.select("li");
		}
	}

	private String obtainNextDetailUrl(String currentUrl, int pageIndex) {
		if (StringUtils.isBlank(currentUrl)) {
			return null;
		}
		int index = currentUrl.indexOf("_");
		if (index == -1) {
			return null;
		}
		String nextPageUrl = currentUrl.substring(0, index + 1);
		nextPageUrl = nextPageUrl + pageIndex + ".html";
		return nextPageUrl;
	}

	private void parserJianShiShusDetail(JianShiShu jianShiShu) {
		String detailSourceUrl = jianShiShu.getDetailSourceUrl();
		if (StringUtils.isBlank(detailSourceUrl)) {
			return;
		}
		Document detailContent = HtmlUtils.getHtmlContentSimple(detailSourceUrl);
		if (detailContent == null) {
			return;
		}
		Element infoElement = detailContent.getElementById("info");
		if (infoElement == null) {
			log.warn("##### 找不到详情页：{}", detailSourceUrl);
			return;
		}
		Elements pElements = infoElement.select("p");
		int size = pElements.size();
		if (size != 0) {
			String author = pElements.get(0).text();
			jianShiShu.setAuthor(author);
			String description = pElements.get(size - 1).text();
			jianShiShu.setDescription(description);
		}
	}



	private JianShiShu generateEntity(String navType) {
		JianShiShu jianShiShu = new JianShiShu();
		jianShiShu.setSourceType(SourceType.JIAN_SHI_SHU.name());
		jianShiShu.setJianShiShuType(ConvertHelper.convertEnumEn(JianShiShuType.class, navType, "小说类型"));
		return jianShiShu;
	}

	private void saveToDb(JianShiShu jianShiShu) {
		String title = jianShiShu.getTitle();
		String author = jianShiShu.getAuthor();
		String detailSourceUrl = jianShiShu.getDetailSourceUrl();
		List<JianShiShu> existJianShiShus = this.jianShiShuMapper.selectByTitleWithWrapper(title, author, detailSourceUrl);
		if (CollectionUtils.isNotEmpty(existJianShiShus)) {
			jianShiShu.setId(existJianShiShus.get(0).getId());
			this.jianShiShuMapper.updateById(jianShiShu);
		} else {
			this.jianShiShuMapper.insert(jianShiShu);
		}
	}
}
