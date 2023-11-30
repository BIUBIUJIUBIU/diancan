/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ycc.diancan.constant.SpiderConstants;
import com.ycc.diancan.definition.spider.ZeiShuBa;
import com.ycc.diancan.enums.NovelChannel;
import com.ycc.diancan.enums.SourceType;
import com.ycc.diancan.enums.ZeiSHuNovelType;
import com.ycc.diancan.mapper.ZeiShuBaMapper;
import com.ycc.diancan.service.SpiderService;
import com.ycc.diancan.service.ZeiShuBaService;
import com.ycc.diancan.util.ConvertHelper;
import com.ycc.diancan.util.HtmlUtils;
import com.ycc.diancan.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

	private final ZeiShuBaMapper zeiShuBaMapper;

	@Override
	public void startSpider() {
		log.info("##### start zei shu ba spider....");
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
			List<Map<String, String>> modelDetailList = modelListKeyMap.getValue();
			if (CollectionUtils.isEmpty(modelDetailList)) {
				continue;
			}
			for (Map<String, String> modelDetailMap : modelDetailList) {
				String modelNameKey = modelDetailMap.get("modelName");
				String novelType = ConvertHelper.convertEnum(ZeiSHuNovelType.class, modelNameKey, "小说类别");
				String modelHref = modelDetailMap.get("modelHref");
				String modelListContent = HtmlUtils.getHtmlContentByUrl(modelHref);
				List<ZeiShuBa> zeiShuBas = parseNovelTypeListHtmlContent(modelListContent, modelHref, novelChannel, novelType);
				if (CollectionUtils.isEmpty(zeiShuBas)) {
					continue;
				}
				for (ZeiShuBa zeiShuBa : zeiShuBas) {
					analysisZeiShuBaDownloadUrl(zeiShuBa);
					String attributes = zeiShuBa.getAttributes();
					Map<String, Object> attributeList = JsonUtils.convertJSON2Object(attributes, Map.class);
					if (Boolean.TRUE.equals(attributeList.get("isCreate"))) {
						zeiShuBa.setAttributes(null);
						this.zeiShuBaMapper.insert(zeiShuBa);
					} else {
						zeiShuBa.setAttributes(null);
						this.zeiShuBaMapper.updateById(zeiShuBa);
					}
				}
				log.info("success");
			}
		}
	}

	/**
	 * 解析男频小说或女频女主页面的 HTML 内容，提取小说链接信息.
	 *
	 * @param htmlContent HTML内容
	 * @return 包含小说链接信息的结果映射
	 */
	private Map<String, List<Map<String, String>>> parseZeiShuHtml(String htmlContent) {
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

	/**
	 * 解析小说类型列表的HTML内容，返回Zeishuba对象的列表.
	 *
	 * @param modelListContent 模型列表的HTML内容
	 * @param modelHref        模型列表的URL链接
	 * @param novelChannel     小说频道
	 * @param novelType        小说类型
	 * @return ZeiShuBa对象的列表
	 */
	private List<ZeiShuBa> parseNovelTypeListHtmlContent(String modelListContent, String modelHref, String novelChannel, String novelType) {
		List<ZeiShuBa> zeiShuList = Lists.newArrayList();
		Document pageDoc = Jsoup.parse(modelListContent);
		Elements pageinfo = pageDoc.getElementsByClass("pageinfo");
		Elements strong = pageinfo.select("strong");
		String modelListTotalStr = strong.text();
		int modelListTotal = Integer.parseInt(modelListTotalStr);
		int calculatePageCount = calculatePageCount(modelListTotal);
		log.info("##### channel is {}, novel type is {}, total is {}, totalPage is {}",
				novelChannel, novelType, modelListTotal, calculatePageCount);
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
				pageInfoConsole(modelListTotal, zeiShuList.size());
				ZeiShuBa zeiShuBa = new ZeiShuBa();
				zeiShuList.add(zeiShuBa);
				zeiShuBa.setSourceType(SourceType.ZEI_SHU_BA.name());
				zeiShuBa.setNovelChannel(novelChannel);
				zeiShuBa.setZeiShuNovelType(novelType);
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
					zeiShuBa.setTitle(bookName);
					zeiShuBa.setDetailSourceUrl(SpiderConstants.ZEI_SHU_WANG_URL + bookDetailHref);
				}
				// 判断是更新还是创建
				List<ZeiShuBa> zeiShuBas = this.zeiShuBaMapper.selectByTitleWithWrapper(zeiShuBa.getTitle());
				if (CollectionUtils.isEmpty(zeiShuBas)) {
					Map<String, Object> attributes = Maps.newHashMap();
					attributes.put("isCreate", true);
					zeiShuBa.setAttributes(JsonUtils.convertObject2JSON(attributes));
				} else {
					Map<String, Object> attributes = Maps.newHashMap();
					attributes.put("isCreate", false);
					zeiShuBa.setAttributes(JsonUtils.convertObject2JSON(attributes));
					zeiShuBa.setId(zeiShuBas.get(0).getId());
				}
				Elements descriptionElements = liElement.getElementsByClass("intro");
				String description = descriptionElements.text();
				zeiShuBa.setDescription(description);
				Elements novelInfoElements = liElement.getElementsByClass("add");
				Elements novelInfo = novelInfoElements.select("small");
				for (TextNode textNode : novelInfo.textNodes()) {
					String authorText = textNode.text();
					if (!authorText.startsWith("作者：")) {
						continue;
					}
					authorText = authorText.replace("作者：", "");
					zeiShuBa.setAuthor(authorText);
					break;
				}
			}
		}
		return zeiShuList;
	}


	/**
	 * 计算总页数.
	 *
	 * @param total 总数
	 * @return 总页数
	 */
	private int calculatePageCount(int total) {
		return (int) Math.ceil((double) total / 15);
	}

	/**
	 * 分析ZeiShuBa下载链接.
	 *
	 * @param zeiShuBa 要分析的ZeiShuBa对象
	 */
	private void analysisZeiShuBaDownloadUrl(ZeiShuBa zeiShuBa) {
		String detailSourceUrl = zeiShuBa.getDetailSourceUrl();
		if (StringUtils.isBlank(detailSourceUrl)) {
			return;
		}
		String novelDetailHtmlContent = HtmlUtils.getHtmlContentByUrl(detailSourceUrl);
		if (StringUtils.isBlank(novelDetailHtmlContent)) {
			return;
		}
		Document novelDetailDoc = Jsoup.parse(novelDetailHtmlContent);
		Elements downUrlList = novelDetailDoc.getElementsByClass("downurllist");
		Elements downUrlLinks = downUrlList.select("a[href]");
		for (Element downUrlLink : downUrlLinks) {
			String downUrl = downUrlLink.attr("href");
			if (StringUtils.isBlank(downUrl)) {
				continue;
			}
			downUrl = SpiderConstants.ZEI_SHU_WANG_URL + downUrl;
			zeiShuBa.setDownloadSourceUrl(downUrl);
			zeiShuBaContentDownloadUrls(downUrl, zeiShuBa);
		}
	}

	/**
	 * 根据下载页面地址获取下载文件链接.
	 *
	 * @param downloadUrl downloadUrl
	 * @param zeiShuBa    zeiShuBa
	 */
	private void zeiShuBaContentDownloadUrls(String downloadUrl, ZeiShuBa zeiShuBa) {
		String novelDownloadHtmlContent = HtmlUtils.getHtmlContentByUrl(downloadUrl);
		if (StringUtils.isBlank(novelDownloadHtmlContent)) {
			return;
		}
		Document novelDownloadDoc = Jsoup.parse(novelDownloadHtmlContent);
		Elements downFilesList = novelDownloadDoc.getElementsByClass("downfile");
		Elements downFilesLinks = downFilesList.select("a[href]");
		Set<String> downloadFileUrlsSet = Sets.newHashSet();
		for (Element downFileLink : downFilesLinks) {
			String downFileUrl = downFileLink.attr("href");
			if (StringUtils.isBlank(downFileUrl)) {
				continue;
			}
			downloadFileUrlsSet.add(downFileUrl);
		}
		List<String> downloadFileUrls = new ArrayList<>(downloadFileUrlsSet);
		if (CollectionUtils.isEmpty(downloadFileUrls)) {
			return;
		}
		String downloadFileUrlsJson = JsonUtils.convertObject2JSON(downloadFileUrls);
		zeiShuBa.setDownloadUrls(downloadFileUrlsJson);
	}

	/**
	 * 页信息输出.
	 *
	 * @param total       总页数
	 * @param currentPage 当前页数
	 */
	private void pageInfoConsole(int total, int currentPage) {
		int section = currentPage % 100;
		if (section != 0) {
			return;
		}
		String percent = calculatePercent(currentPage, total);
		log.info("#####  current progress: total is {}, current is {}, percent  is {}", total, currentPage, percent);
	}

	/**
	 * 计算给定数字所占总数的百分比.
	 *
	 * @param current 给定数字
	 * @param total   总数
	 * @return 计算得到的百分比字符串
	 */
	private String calculatePercent(double current, double total) {
		double percentage = (current / total);
		// 格式化为保留两位小数的百分比形式
		return NumberUtil.formatPercent(percentage, 2);
	}

}
