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
import com.ycc.diancan.definition.spider.ShuHuangBuLuo;
import com.ycc.diancan.enums.ShuHuangBuLuoNovelType;
import com.ycc.diancan.enums.SourceType;
import com.ycc.diancan.mapper.ShuHuangBuLuoMapper;
import com.ycc.diancan.service.ShuHuangBuLuoService;
import com.ycc.diancan.service.SpiderService;
import com.ycc.diancan.util.ConvertHelper;
import com.ycc.diancan.util.HtmlUtils;
import com.ycc.diancan.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * ShuHuangBuLuoServiceImpl.
 *
 * @author ycc
 * @date 2023-11-30 16:36:17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShuHuangBuLuoServiceImpl extends ServiceImpl<ShuHuangBuLuoMapper, ShuHuangBuLuo> implements ShuHuangBuLuoService, SpiderService {

	private final ShuHuangBuLuoMapper shuHuangBuLuoMapper;

	@Override
	public void startSpider() {
		log.info("start shu huang bu luo spider....");
		spiderShuHuangBuLuo();
	}

	private void spiderShuHuangBuLuo() {
		String shuHuangHtmlContent = HtmlUtils.getHtmlContentByUrl(SpiderConstants.SHU_HUANG_BU_LUO_URL);
		if (StringUtils.isBlank(shuHuangHtmlContent)) {
			log.error("shu huang bu luo spider error, shu huang html content is null");
			return;
		}
		List<ShuHuangBuLuo> shuHuangBuLuosList = parseShuHuangHtml(shuHuangHtmlContent);
		if (CollectionUtils.isEmpty(shuHuangBuLuosList)) {
			return;
		}
		for (ShuHuangBuLuo shuHuangBuLuo : shuHuangBuLuosList) {
			analysisDownloadInfo(shuHuangBuLuo.getDetailSourceUrl(), shuHuangBuLuo);
			this.shuHuangBuLuoMapper.insert(shuHuangBuLuo);
		}
		log.info("shu huang bu luo size is {}", shuHuangBuLuosList.size());


	}

	private List<ShuHuangBuLuo> parseShuHuangHtml(String shuHuangHtmlContent) {
		// 使用 Jsoup 解析 HTML
		Document doc = Jsoup.parse(shuHuangHtmlContent);
		Elements shuHuangBuLuoElements = doc.getElementsByClass("excerpt excerpt-one");
		List<ShuHuangBuLuo> shuHuangBuLuos = parseShuHuangHtml(shuHuangBuLuoElements);
		Elements nextPageElements = doc.getElementsByClass("next-page");
		Elements selectLinks = nextPageElements.select("a[href]");
		String nextPageHref = null;
		for (Element selectLink : selectLinks) {
			String actionName = selectLink.text();
			if (StringUtils.isBlank(actionName)) {
				return shuHuangBuLuos;
			}
			nextPageHref = selectLink.attr("href");
		}
		if (StringUtils.isBlank(nextPageHref)) {
			return shuHuangBuLuos;
		}
		String nextPageHtmlContent = HtmlUtils.getHtmlContentByUrl(nextPageHref);
		if (StringUtils.isBlank(nextPageHtmlContent)) {
			return shuHuangBuLuos;
		}
		shuHuangBuLuos.addAll(parseShuHuangHtml(nextPageHtmlContent));
		return shuHuangBuLuos;
	}

	private List<ShuHuangBuLuo> parseShuHuangHtml(Elements shuHuangBuLuoElements) {
		List<ShuHuangBuLuo> ShuHuangBuLuoList = Lists.newArrayList();
		for (Element shuHuangBuLuoElement : shuHuangBuLuoElements) {
			ShuHuangBuLuo shuHuangBuLuo = new ShuHuangBuLuo();
			shuHuangBuLuo.setSourceType(SourceType.SHU_HUANG_BU_LUO.name());
			Elements h2Elements = shuHuangBuLuoElement.select("h2");
			Elements novelInfoLinks = h2Elements.select("a[href]");
			for (Element novelInfoLink : novelInfoLinks) {
				String href = novelInfoLink.attr("href");
				String title = novelInfoLink.text();
				if (StringUtils.isAnyBlank(href, title)) {
					continue;
				}
				title = title.replace("精校全本", "").replace(" ", "");
				List<ShuHuangBuLuo> shuHuangBuLuos = this.shuHuangBuLuoMapper.selectByTitleWithWrapper(title);
				HashMap<String, Object> attributes = Maps.newHashMap();
				if (CollectionUtils.isEmpty(shuHuangBuLuos)) {
					attributes.put("isCreate", true);
					shuHuangBuLuo.setAttributes(JsonUtils.convertObject2JSON(attributes));
				} else {
					shuHuangBuLuo.setId(shuHuangBuLuos.get(0).getId());
					attributes.put("isCreate", false);
					shuHuangBuLuo.setAttributes(JsonUtils.convertObject2JSON(attributes));
				}
				shuHuangBuLuo.setTitle(title);
				shuHuangBuLuo.setDetailSourceUrl(href);
			}
			Elements imgElements = shuHuangBuLuoElement.select("img");
			for (Element imgElement : imgElements) {
				String imgSrc = imgElement.attr("data-original");
				if (StringUtils.isBlank(imgSrc)) {
					continue;
				}
				shuHuangBuLuo.setImgUrl(imgSrc);
				break;
			}
			Elements noteElements = shuHuangBuLuoElement.getElementsByClass("note");
			String novelNote = noteElements.text();
			if (StringUtils.isBlank(novelNote)) {
				continue;
			}
			String[] novelNoteArray = novelNote.split(" ");
			analysisNovelNote(novelNoteArray, shuHuangBuLuo);
			ShuHuangBuLuoList.add(shuHuangBuLuo);
		}


		return ShuHuangBuLuoList;
	}

	private void analysisNovelNote(String[] novelNoteArray, ShuHuangBuLuo shuHuangBuLuo) {
		int author = -1;
		int type = -1;
		int description = -1;
		for (int i = 0; i < novelNoteArray.length; i++) {
			if (novelNoteArray[i].startsWith("作者")) {
				author = i;
			} else if (novelNoteArray[i].startsWith("类别")) {
				type = i;
			} else if (novelNoteArray[i].startsWith("内容简介")) {
				description = i;
			}
			if (author != -1 && type != -1 && description != -1) {
				break;
			}
		}
		StringBuilder descriptionStr = new StringBuilder();
		StringBuilder authorStr = new StringBuilder();
		StringBuilder typeStr = new StringBuilder();
		for (int i = author; i < type; i++) {
			if (i == -1) {
				break;
			}
			String novelNote = novelNoteArray[i];
			if (StringUtils.isBlank(novelNote)) {
				continue;
			}
			authorStr.append(novelNote.replace("作者", "").replace("：", "").replace(":", ""));
		}
		shuHuangBuLuo.setAuthor(authorStr.toString());
		boolean haveNovelType = false;
		String novelType = null;
		for (int i = type; i < description; i++) {
			if (i == -1) {
				break;
			}
			String novelNote = novelNoteArray[i];
			novelType = analysisNovelType(novelNote);
			if (StringUtils.isNotBlank(novelType)) {
				haveNovelType = true;
				break;
			}
		}
		if (haveNovelType) {
			typeStr.append(novelType);
			shuHuangBuLuo.setShuHuangBuLuoNovelType(ConvertHelper.convertEnumEn(ShuHuangBuLuoNovelType.class, novelType, "小说类型"));
		}
		shuHuangBuLuo.setSourceType(typeStr.toString());
		for (int i = description; i < novelNoteArray.length; i++) {
			if (i == -1) {
				break;
			}
			String novelNote = novelNoteArray[i];
			if (StringUtils.isBlank(novelNote)) {
				continue;
			}
			if (StringUtils.equals("内容简介", novelNote)) {
				continue;
			}
			descriptionStr.append(novelNote);
		}
		shuHuangBuLuo.setDescription(descriptionStr.toString());
	}

	private void analysisDownloadInfo(String detailUrl, ShuHuangBuLuo shuHuangBuLuo) {
		String detailHtmlContent = HtmlUtils.getHtmlContentByUrl(detailUrl);
		Document doc = Jsoup.parse(detailHtmlContent);
		Elements downloadElements = doc.getElementsByClass("xydown_down_link");
		Elements pElements = downloadElements.select("p");
		for (Element pElement : pElements) {
			if (StringUtils.isNotBlank(shuHuangBuLuo.getUnZipPassword()) &&
					StringUtils.isNotBlank(shuHuangBuLuo.getDownloadSourceUrl())) {
				break;
			}
			String text = pElement.text();
			if (StringUtils.isBlank(text)) {
				continue;
			}
			if (text.contains("点击下载")) {
				String downloadSourceUrl = pElement.getElementsByClass("downbtn").attr("href");
				shuHuangBuLuo.setDownloadSourceUrl(downloadSourceUrl);
			}
			if (text.contains("解压密码")) {
				String unZipPassword = text.replace("解压密码", "").replace("：", "");
				shuHuangBuLuo.setUnZipPassword(unZipPassword);
			}

		}
	}

	private String analysisNovelType(String novelType) {
		if (StringUtils.isBlank(novelType)) {
			return null;
		}
		novelType = novelType.replace("类别", "")
				.replace("：", "")
				.replace(":", "");
		int novelIndex = -1;
		if (novelType.startsWith("轻小说")) {
			novelIndex = novelType.indexOf("轻小说");
		} else if (novelType.contains("无限流")) {
			novelType = "无限流";
		} else if (novelType.contains("武侠")) {
			novelType = "武侠";
		} else if (novelType.startsWith("作者简介") || novelType.startsWith("此版本")) {
			return null;
		} else {
			novelIndex = novelType.indexOf("小说");
		}
		if (novelIndex != -1) {
			novelType = novelType.substring(0, novelIndex);
		}
		if (StringUtils.isBlank(novelType)) {
			return null;
		}
		return novelType;
	}


}
