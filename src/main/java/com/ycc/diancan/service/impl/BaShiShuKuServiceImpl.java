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
import com.ycc.diancan.definition.spider.BaShiShuKu;
import com.ycc.diancan.enums.BaShiShuKuNovelType;
import com.ycc.diancan.enums.SourceType;
import com.ycc.diancan.mapper.BaShiShuKuMapper;
import com.ycc.diancan.service.BaShiShuKuService;
import com.ycc.diancan.service.SpiderService;
import com.ycc.diancan.util.CommonUtils;
import com.ycc.diancan.util.ConvertHelper;
import com.ycc.diancan.util.HtmlUtils;
import com.ycc.diancan.util.JsonUtils;
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
 * BaShiShuKuServiceImpl.
 *
 * @author ycc
 * @date 2023-12-04 09:49:53
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BaShiShuKuServiceImpl extends ServiceImpl<BaShiShuKuMapper, BaShiShuKu> implements BaShiShuKuService, SpiderService {

	private final BaShiShuKuMapper baShiShuKuMapper;

	@Override
	public void startSpider() {
		log.info("start ba shi shu ku spider....");
		spiderBaShiShuKu();
	}

	@Override
	public List<BaShiShuKu> searchByTitle(String title) {
		return this.baShiShuKuMapper.selectByTitle(title);
	}

	@Override
	public List<BaShiShuKu> searchByAuthor(String author) {
		return this.baShiShuKuMapper.selectByAuthor(author);
	}


	private void spiderBaShiShuKu() {
		Document doc = HtmlUtils.getHtmlContentSimple(SpiderConstants.BA_SHI_SHU_KU_URL);
		if (doc == null) {
			return;
		}
		Elements navElements = doc.getElementsByClass("nav");
		Elements navSelects = navElements.select("a[href]");
		Map<String, String> navsMap = Maps.newLinkedHashMap();
		for (Element navSelect : navSelects) {
			String text = navSelect.text();
			if (StringUtils.equals(text, "首页")) {
				continue;
			}
			String href = navSelect.attr("href");
			if (StringUtils.isBlank(href)) {
				continue;
			}
			href = SpiderConstants.BA_SHI_SHU_KU_URL + href;
			navsMap.put(text, href);
			log.info("##### nav is {}, href is {}", text, href);
		}
		if (MapUtils.isEmpty(navsMap)) {
			return;
		}
		for (Map.Entry<String, String> navMap : navsMap.entrySet()) {
			String navKey = navMap.getKey();
			log.info("##### start handle : {}", navKey);
			if (StringUtils.equals(navKey, "言情耽美")) {
				List<BaShiShuKu> loveBeauties = parserLoveBeauties(navMap.getValue());
				if (CollectionUtils.isNotEmpty(loveBeauties)) {
					log.info("##### start save {}, size is {}", navKey, loveBeauties.size());
						saveToDb(loveBeauties);
				}
				continue;
			}
			List<BaShiShuKu> otherBaShiShuKus = parserBaShiShuKuOthers(navMap.getValue());
			if (CollectionUtils.isEmpty(otherBaShiShuKus)) {
				continue;
			}
			log.info("##### start save {}, size is {}", navKey, otherBaShiShuKus.size());
			saveToDb(otherBaShiShuKus);
		}
	}

	private void saveToDb(List<BaShiShuKu> baShiShuKus) {
		baShiShuKus.forEach(baShiShuKu -> {
			String title = baShiShuKu.getTitle();
			String author = baShiShuKu.getAuthor();
			List<BaShiShuKu> existBaShiShuKus = this.baShiShuKuMapper.selectByTitleWithWrapper(title, author);
			if (CollectionUtils.isNotEmpty(existBaShiShuKus)) {
				baShiShuKu.setId(existBaShiShuKus.get(0).getId());
				this.baShiShuKuMapper.updateById(baShiShuKu);
			} else {
				this.baShiShuKuMapper.insert(baShiShuKu);
			}
		});
	}

	private List<BaShiShuKu> parserLoveBeauties(String loveBeautifulUrl) {
		List<BaShiShuKu> loveBeauties = Lists.newArrayList();
		Document doc = HtmlUtils.getHtmlContentSimple(loveBeautifulUrl);
		if (doc == null) {
			return loveBeauties;
		}
		Elements cellElements = doc.getElementsByClass("cell");
		for (Element cellElement : cellElements) {
			Elements ficTypeTab = cellElement.getElementsByClass("fic_type_Tab").select("h1");
			String text = ficTypeTab.text();
			if (StringUtils.isBlank(text)) {
				continue;
			}
			String baShiShuKuNovelType = ConvertHelper.convertEnumEn(BaShiShuKuNovelType.class, text, "巴士书库类型");
			Elements fglLpPic = cellElement.getElementsByClass("fgl_lp_pic");
			Elements fglLpInfoFglTlInfo = cellElement.getElementsByClass("fgl_lp_info fgl_tl_info");
			handleSpecial(loveBeauties, fglLpPic, fglLpInfoFglTlInfo, baShiShuKuNovelType);

			Elements fglTlList = cellElement.getElementsByClass("fgl_tl_list");
			handleNovelList(loveBeauties, fglTlList, baShiShuKuNovelType);
		}
		return loveBeauties;
	}

	private List<BaShiShuKu> parserBaShiShuKuOthers(String navTargetUrl) {
		List<BaShiShuKu> baShiShuKus = Lists.newArrayList();
		Document docNav = HtmlUtils.getHtmlContentSimple(navTargetUrl);
		if (docNav == null) {
			return baShiShuKus;
		}
		Elements listBox = docNav.getElementsByClass("listBox");
		Elements h1Elements = listBox.select("h1");
		if (h1Elements.isEmpty()) {
			return baShiShuKus;
		}
		String baShiShuKuNovelTypeStr = h1Elements.get(0).text();
		if (StringUtils.isBlank(baShiShuKuNovelTypeStr)) {
			return baShiShuKus;
		}
		String baShiShuKuNovelType = ConvertHelper.convertEnumEn(BaShiShuKuNovelType.class, baShiShuKuNovelTypeStr, "巴士书库类型");
		Elements tsPage = docNav.getElementsByClass("tspage");
		String pageText = tsPage.text();
		if (StringUtils.isBlank(pageText)) {
			return baShiShuKus;
		}
		int totalPage = parsePageInfo(pageText);
		if (totalPage == -1) {
			return baShiShuKus;
		}
		for (int i = 0; i < totalPage; i++) {
			if (i != 0) {
				String otherDetailUrl = navTargetUrl + "index_" + (i + 1) + ".html";
				docNav = HtmlUtils.getHtmlContentSimple(otherDetailUrl);
				if (docNav == null) {
					continue;
				}
				listBox = docNav.getElementsByClass("listBox");
			}
			Elements liElements = listBox.select("li");
			for (Element liElement : liElements) {
				BaShiShuKu baShiShuKu = generateCellEntity(baShiShuKuNovelType);
				Elements baseInfo = liElement.select("a");
				Elements linkElements = baseInfo.select("a[href]");
				for (Element linkElement : linkElements) {
					String href = linkElement.attr("href");
					if (StringUtils.isBlank(href)) {
						continue;
					}
					String title = linkElement.text();
					if (StringUtils.isBlank(title)) {
						continue;
					}
					baShiShuKu.setTitle(title);
					if (!href.startsWith("/c")) {
						continue;
					}
					baShiShuKu.setDetailSourceUrl(SpiderConstants.BA_SHI_SHU_KU_URL + href);
					completeInfo(baShiShuKu, baShiShuKu.getDetailSourceUrl());
					baShiShuKus.add(baShiShuKu);
				}
			}
		}
		return baShiShuKus;
	}


	private BaShiShuKu generateCellEntity(String baShiShuKuNovelType) {
		BaShiShuKu baShiShuKu = new BaShiShuKu();
		baShiShuKu.setSourceType(SourceType.BA_SHI_SHU_KU.name());
		baShiShuKu.setBaShiShuKuNovelType(baShiShuKuNovelType);
		return baShiShuKu;
	}

	private void handleSpecial(List<BaShiShuKu> loveBeauties, Elements fglLp, Elements fglLpInfoFglTlInfo, String baShiShuKuNovelType) {
		BaShiShuKu baShiShuKu = generateCellEntity(baShiShuKuNovelType);
		for (Element infoElement : fglLpInfoFglTlInfo) {
			Elements h4 = infoElement.select("h4");
			Elements h4Select = h4.select("a[href]");
			if (h4Select.isEmpty()) {
				continue;
			}
			Element titleElement = h4Select.get(0);
			String title = titleElement.text();
			if (StringUtils.isBlank(title)) {
				continue;
			}
			loveBeauties.add(baShiShuKu);
			baShiShuKu.setTitle(title);
			String detailUrl = titleElement.attr("href");
			baShiShuKu.setDetailSourceUrl(SpiderConstants.BA_SHI_SHU_KU_URL + detailUrl);
		}
		completeInfo(baShiShuKu, baShiShuKu.getDetailSourceUrl());
	}

	private void handleNovelList(List<BaShiShuKu> loveBeauties, Elements fglTlList, String baShiShuKuNovelType) {
		Elements selectElements = fglTlList.select("a[href]");
		for (Element selectElement : selectElements) {
			BaShiShuKu baShiShuKu = generateCellEntity(baShiShuKuNovelType);
			baShiShuKu.setDetailSourceUrl(SpiderConstants.BA_SHI_SHU_KU_URL + selectElement.attr("href"));
			baShiShuKu.setTitle(selectElement.text());
			loveBeauties.add(baShiShuKu);
			completeInfo(baShiShuKu, baShiShuKu.getDetailSourceUrl());
		}
	}


	private void completeInfo(BaShiShuKu baShiShuKu, String detailUrl) {
		Document detailDoc = HtmlUtils.getHtmlContentSimple(detailUrl);
		if (detailDoc == null) {
			return;
		}
		Elements detailPicElements = detailDoc.getElementsByClass("detail_pic");
		Elements imgElements = detailPicElements.select("img[src]");
		if (!imgElements.isEmpty()) {
			Element imgElement = imgElements.get(0);
			String imgSrc = imgElement.attr("src");
			if (StringUtils.isNotBlank(imgSrc)) {
				baShiShuKu.setImgUrl(SpiderConstants.BA_SHI_SHU_KU_URL + imgSrc);
			}
		}
		Elements smallElements = detailDoc.getElementsByClass("small");
		for (Element smallElement : smallElements) {
			String text = smallElement.text();
			if (StringUtils.isBlank(text)) {
				continue;
			}
			if (!text.startsWith("小说作者")) {
				continue;
			}
			baShiShuKu.setAuthor(text.substring(5));
			break;
		}
		Element content = detailDoc.getElementById("content");
		if (content == null) {
			return;
		}
		Elements ulElements = content.select("ul");
		for (Element ulElement : ulElements) {
			String style = ulElement.attributes().get("style");
			if (!style.contains("block")) {
				continue;
			}
			String description = ulElement.text();
			if (StringUtils.isBlank(description)) {
				continue;
			}
			baShiShuKu.setDescription(description);
		}
		Elements downButtons = detailDoc.getElementsByClass("downButton");
		if (downButtons.isEmpty()) {
			return;
		}
		Element downButton = downButtons.get(0);
		String downloadHtmlHref = downButton.attr("href");
		if (StringUtils.isBlank(downloadHtmlHref)) {
			return;
		}
		baShiShuKu.setDownloadUrls(SpiderConstants.BA_SHI_SHU_KU_URL + downloadHtmlHref);
		parserDownloadHtml(baShiShuKu, baShiShuKu.getDownloadUrls());
	}

	private void parserDownloadHtml(BaShiShuKu baShiShuKu, String downloadHtmlHref) {
		Document downloadHtmlContent = HtmlUtils.getHtmlContentSimple(downloadHtmlHref);
		if (downloadHtmlContent == null) {
			return;
		}
		Elements selectDownloadElements = downloadHtmlContent.select("a[href]");
		List<String> downloadUrls = Lists.newArrayList();
		for (Element selectDownloadElement : selectDownloadElements) {
			String text = selectDownloadElement.text();
			if (StringUtils.isBlank(text)) {
				continue;
			}
			if (text.contains("格式下载")) {
				String href = selectDownloadElement.attr("href");
				if (StringUtils.isBlank(href)) {
					continue;
				}
				downloadUrls.add(SpiderConstants.BA_SHI_SHU_KU_URL + href);
			}
		}
		if (CollectionUtils.isEmpty(downloadUrls)) {
			return;
		}
		baShiShuKu.setDownloadUrls(JsonUtils.convertObject2JSON(downloadUrls));
		baShiShuKu.setDownloadSourceUrl(downloadUrls.get(0));

	}


	private int parsePageInfo(String pageText) {
		String pageInfo = pageText.substring(3, pageText.indexOf("每页"));
		String[] split = pageInfo.split("/");
		if (split.length != 2) {
			return -1;
		}
		return Integer.parseInt(CommonUtils.trimBlankChar(split[1]));
	}
}
