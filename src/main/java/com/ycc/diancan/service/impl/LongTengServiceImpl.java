package com.ycc.diancan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.ycc.diancan.constant.SpiderConstants;
import com.ycc.diancan.definition.spider.LongTeng;
import com.ycc.diancan.enums.LongTengType;
import com.ycc.diancan.enums.SourceType;
import com.ycc.diancan.mapper.LongTengMapper;
import com.ycc.diancan.service.LongTengService;
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
 * LongTengServiceImpl.
 *
 * @author ycc
 * @date 2023-12-25 2023/12/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LongTengServiceImpl extends ServiceImpl<LongTengMapper, LongTeng> implements LongTengService, SpiderService {

	private final LongTengMapper longTengMapper;

	@Override
	public void startSpider() {
		log.info("start long teng spider....");
		spiderLongTeng();
	}

	@Override
	public List<LongTeng> searchByTitle(String title) {
		return this.longTengMapper.selectByTitle(title);
	}

	@Override
	public List<LongTeng> searchByAuthor(String author) {
		return this.longTengMapper.selectByAuthor(author);
	}

	private void spiderLongTeng() {
		Document htmlContentSimple = HtmlUtils.getHtmlContentSimple(SpiderConstants.LONG_TENG_BASE);
		if (htmlContentSimple == null) {
			return;
		}
		Elements mainMMenu = htmlContentSimple.getElementsByClass("main m_menu");
		Elements mainMMenus = mainMMenu.select("a[href]");
		Map<String, String> menusMap = Maps.newLinkedHashMap();
		for (Element mMenu : mainMMenus) {
			String navText = mMenu.text();
			if (StringUtils.equalsAny(navText, "首页", "全本")) {
				continue;
			}
			String href = mMenu.attr("href");
			if (StringUtils.isAnyBlank(navText, href)) {
				continue;
			}
			menusMap.put(navText, SpiderConstants.LONG_TENG_BASE + href);
		}
		if (MapUtils.isEmpty(menusMap)) {
			return;
		}
		for (Map.Entry<String, String> menuMap : menusMap.entrySet()) {
			String novelType = menuMap.getKey();
			if (!StringUtils.equals(novelType, "肉文辣文")) {
				continue;
			}
			String typeHref = menuMap.getValue();
			parseNovelType(novelType, typeHref);
		}
	}

	private void parseNovelType(String novelType, String typeHref) {

		Document typeHtml = HtmlUtils.getHtmlContentSimple(typeHref);
		Elements laseElements = typeHtml.getElementsByClass("last");
		String lastPage = laseElements.text();
		if (StringUtils.isBlank(lastPage)) {
			return;
		}
		int lasePageIndex = Integer.parseInt(lastPage);
		for (int i = 0; i < lasePageIndex; i++) {
			if (i != 0) {
				String nextPage = typeHref.substring(0, typeHref.indexOf("_") + 1) + (i + 1) + ".html";
				typeHtml = HtmlUtils.getHtmlContentSimple(nextPage);
			}
			Elements trElements = typeHtml.select("tr");
			for (int j = 1; j < trElements.size(); j++) {
				Element trElement = trElements.get(j);
				Elements tdElements = trElement.select("td");
				if (CollectionUtils.isEmpty(tdElements)) {
					continue;
				}
				Element element = tdElements.get(0);
				Elements select = element.select("a[href]");
				String title = select.text();
				String href = select.attr("href");
				if (StringUtils.isAnyBlank(title, href)) {
					continue;
				}
				LongTeng longTeng = generateEntity(novelType);
				longTeng.setTitle(title);
				longTeng.setDetailSourceUrl(href);
				Element authorElement = tdElements.get(2);
				String author = authorElement.text();
				if (StringUtils.isBlank(author)) {
					author = "匿名";
				}
				longTeng.setAuthor(author);
				paraseDetailInfo(longTeng);
				saveToDb(longTeng);
			}

		}


	}

	private LongTeng generateEntity(String navType) {
		LongTeng longTeng = new LongTeng();
		longTeng.setSourceType(SourceType.LONG_TENG.name());
		longTeng.setLongTengType(ConvertHelper.convertEnumEn(LongTengType.class, navType, "小说类型"));
		return longTeng;
	}

	private void paraseDetailInfo(LongTeng longTeng) {
		String detailSourceUrl = longTeng.getDetailSourceUrl();
		Document detailHtml = HtmlUtils.getHtmlContentSimple(detailSourceUrl);
		if (detailHtml == null) {
			return;
		}
		Element contentElement = detailHtml.getElementById("content");
		Elements flElements = contentElement.getElementsByClass("fl");
		Element imgElement = flElements.get(0);
		if (imgElement != null) {
			Elements imgSelect = imgElement.select("img[src]");
			if (CollectionUtils.isNotEmpty(imgSelect)) {
				String imgHref = imgSelect.get(0).attr("src");
				longTeng.setImgUrl(imgHref);
			}
		}
		Elements downloadElements = contentElement.getElementsByClass("btnlinks");
		Elements downloadSelect = downloadElements.select("a[href]");
		for (Element element : downloadSelect) {
			String text = element.text();
			if (!StringUtils.equals(text, "TXT下载")) {
				continue;
			}
			String href = element.attr("href");
			if (StringUtils.isBlank(href)) {
				continue;
			}
			longTeng.setDownloadSourceUrl(SpiderConstants.LONG_TENG_BASE + href);
			break;
		}
		Elements pElements = contentElement.select("p");
		for (int i = 0; i < pElements.size(); i++) {
			String text = pElements.get(i).text();
			if (StringUtils.isNotBlank(text) && text.contains("内容简介")) {
				String description = pElements.get(i + 1).text();
				longTeng.setDescription(description);
				return;
			}
		}

	}

	private void saveToDb(LongTeng longTeng) {
		String title = longTeng.getTitle();
		String author = longTeng.getAuthor();
		String detailSourceUrl = longTeng.getDetailSourceUrl();
		List<LongTeng> existLongTengs = this.longTengMapper.selectByTitleWithWrapper(title, author, detailSourceUrl);
		if (CollectionUtils.isNotEmpty(existLongTengs)) {
			longTeng.setId(existLongTengs.get(0).getId());
			this.longTengMapper.updateById(longTeng);
		} else {
			this.longTengMapper.insert(longTeng);
		}
	}

}
