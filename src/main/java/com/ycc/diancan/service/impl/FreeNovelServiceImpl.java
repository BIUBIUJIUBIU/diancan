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
import com.ycc.diancan.definition.spider.FreeNovel;
import com.ycc.diancan.enums.FreeNovelType;
import com.ycc.diancan.enums.SourceType;
import com.ycc.diancan.mapper.FreeNovelMapper;
import com.ycc.diancan.service.FreeNovelService;
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

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * FreeNovelServiceImpl.
 *
 * @author ycc
 * @date 2023-12-01 11:29:43
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FreeNovelServiceImpl extends ServiceImpl<FreeNovelMapper, FreeNovel> implements FreeNovelService, SpiderService {
	private final FreeNovelMapper freeNovelMapper;
	@Override
	public void startSpider() {
		log.info("start free novel spider....");
		spiderShuHuangBuLuo();
	}

	@Override
	public List<FreeNovel> searchByTitle(String title) {
		return this.freeNovelMapper.selectByTitle(title);
	}

	@Override
	public List<FreeNovel> searchByAuthor(String author) {
		return this.freeNovelMapper.selectByAuthor(author);
	}


	private void spiderShuHuangBuLuo() {
		Document doc = HtmlUtils.getHtmlContentSimple(SpiderConstants.FREE_NOVEL_HOME_URL);
		if (doc == null) {
			return;
		}
		Elements snavBodyElements = doc.getElementsByClass("snavbody");
		Elements liTextHrefElements = snavBodyElements.select("a[href]");
		List<Map<String, String>> typeHrefsList = Lists.newArrayList();
		for (Element liElement : liTextHrefElements) {
			Map<String, String> typeHrefsMap = Maps.newHashMap();
			String text = liElement.text();
			if (StringUtils.isBlank(text) || StringUtils.equalsAny(text, "首页", "小说社区", "小说手机版")) {
				continue;
			}
			typeHrefsList.add(typeHrefsMap);
			String href = liElement.attr("href");
			href = SpiderConstants.FREE_NOVEL_URL + href;
			typeHrefsMap.put("type", text);
			typeHrefsMap.put("href", href);
		}
		if (CollectionUtils.isEmpty(typeHrefsList)) {
			return;
		}
		for (Map<String, String> typeHrefMap : typeHrefsList) {
			List<FreeNovel> freeNovelsList = Lists.newArrayList();
			String href = typeHrefMap.get("href");
			Document typeHtmlContent = HtmlUtils.getHtmlContentSimple(href);
			if (typeHtmlContent == null) {
				continue;
			}
			Elements pageInfoElements = typeHtmlContent.getElementsByClass("pageinfo");
			String type = typeHrefMap.get("type");
			String novelTypeStr = ConvertHelper.convertEnumEn(FreeNovelType.class, type, "小说类型");
			FreeNovelType freeNovelType = FreeNovelType.valueOf(novelTypeStr);

			int totalPage = parsePageInfo(pageInfoElements.text());
			for (int i = 0; i < totalPage; i++) {
				if (i != 0) {
					String pageHref = href + "list_" + freeNovelType.getIndex() + "_" + (i + 1) + ".html";
					typeHtmlContent = HtmlUtils.getHtmlContentSimple(pageHref);
					if (typeHtmlContent == null) {
						continue;
					}
				}
				Elements pldalLeftElements = typeHtmlContent.getElementsByClass("pldal_left");
				parseHtmlContent(freeNovelsList, pldalLeftElements, novelTypeStr);
			}
			savaToDb(freeNovelsList);
		}
	}

	private void savaToDb(List<FreeNovel> freeNovelsList) {
		if (CollectionUtils.isEmpty(freeNovelsList)) {
			return;
		}
		freeNovelsList.forEach(freeNovel -> {
			String title = freeNovel.getTitle();
			String author = freeNovel.getAuthor();
			List<FreeNovel> freeNovels = this.freeNovelMapper.selectByTitleWithWrapper(title, author);
			if (CollectionUtils.isNotEmpty(freeNovels)) {
				freeNovel.setId(freeNovels.get(0).getId());
				this.freeNovelMapper.updateById(freeNovel);
			} else {
				this.freeNovelMapper.insert(freeNovel);
			}
		});
	}

	private Integer parsePageInfo(String pageInfo) {
		int start = pageInfo.indexOf("共");
		int middle = pageInfo.indexOf("页");
		int last = pageInfo.indexOf("条");
		String totalPage = pageInfo.substring(start + 1, middle).replace(" ", "");
		return Integer.parseInt(totalPage);
	}

	private void parseHtmlContent(List<FreeNovel> freeNovelsList, Elements pldalLeftElements, String novelTypeStr) {
		for (Element pldalLeftElement : pldalLeftElements) {
			FreeNovel freeNovel = new FreeNovel();
			freeNovel.setFreeNovelType(novelTypeStr);
			freeNovel.setSourceType(SourceType.FREE_NOVEL.name());
			// 获取图片信息
			Elements pldalLeft = pldalLeftElement.getElementsByClass("pldal_l1");
			Elements imageInfos = pldalLeft.select("img[src]");
			if (!imageInfos.isEmpty()) {
				String imgSrc = imageInfos.get(0).attr("src");
				if (StringUtils.isBlank(imgSrc)) {
					continue;
				}
				freeNovel.setImgUrl(SpiderConstants.FREE_NOVEL_URL + imgSrc);
			}
			// 获取基本信息
			Elements pldalRights = pldalLeftElement.getElementsByClass("pldal_r1");
			for (Element pldalRight : pldalRights) {
				String detailUrl = null;
				Elements pldalR1Bt = pldalRight.getElementsByClass("pldal_r1_bt");
				if (!pldalR1Bt.isEmpty()) {
					Elements infoElements = pldalR1Bt.get(0).select("a[href]");
					String novelTitle = infoElements.text();
					if (StringUtils.isBlank(novelTitle)) {
						continue;
					}
					if (StringUtils.containsAny(novelTitle, "《", "》")) {
						Matcher matcher = SpiderConstants.CUT_OUT_BOOK_NAME.matcher(novelTitle);
						if (matcher.find()) {
							novelTitle = matcher.group(1);
						}
					}
					freeNovel.setTitle(novelTitle);
					detailUrl = infoElements.attr("href");
					freeNovel.setDetailSourceUrl(SpiderConstants.FREE_NOVEL_URL + detailUrl);
				}

				Elements pldalR1Jj = pldalRight.getElementsByClass("pldal_r1_jj");
				String description = pldalR1Jj.text();
				if (StringUtils.isNotBlank(description)) {
					freeNovel.setDescription(description);
				}
				String detailSourceUrl = freeNovel.getDetailSourceUrl();
				if (StringUtils.isBlank(detailSourceUrl)) {
					continue;
				}
				Document detailHtmlContent = HtmlUtils.getHtmlContentSimple(detailSourceUrl);
				if (detailHtmlContent == null) {
					continue;
				}
				Elements dlKkInfoElements = detailHtmlContent.getElementsByClass("dl_kk_info");
				Elements liElements = dlKkInfoElements.select("li");
				for (Element liElement : liElements) {
					String text = liElement.text();
					if (StringUtils.isNotBlank(text) && text.startsWith("小说作者")) {
						text = text.replace("小说作者", "").replace(" ", "").replace("：", "");
						freeNovel.setAuthor(text);
						break;
					}
				}
				Elements dlLinkBdElements = detailHtmlContent.getElementsByClass("dl_link_bd");
				Elements liDownloadUrlElements = dlLinkBdElements.select("li");
				List<String> downloadUrls = Lists.newArrayList();
				for (Element liDownloadUrlElement : liDownloadUrlElements) {
					Elements liDownloadUrls = liDownloadUrlElement.select("a[href]");
					for (Element liDownloadUrl : liDownloadUrls) {
						String href = liDownloadUrl.attr("href");
						if (StringUtils.isBlank(href)) {
							continue;
						}
						downloadUrls.add(SpiderConstants.FREE_NOVEL_URL + href);
					}
				}
				if (CollectionUtils.isNotEmpty(downloadUrls)) {
					freeNovel.setDownloadUrls(JsonUtils.convertObject2JSON(downloadUrls));
					freeNovel.setDownloadSourceUrl(downloadUrls.get(0));
				}
			}
			freeNovelsList.add(freeNovel);
		}
	}
}
