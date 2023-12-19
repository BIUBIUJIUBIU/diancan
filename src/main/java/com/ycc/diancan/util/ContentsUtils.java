/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.util;

import com.ycc.diancan.vo.BookSection;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ContentsUtils.
 *
 * @author ycc
 * @date 2023-12-11 17:00:18
 */
@Slf4j
public final class ContentsUtils {
	private ContentsUtils() {

	}

	public static void parseSectionIndex(String sectionName, BookSection bookSection) {
		log.info("##### name is {}", sectionName);
		// if (StringUtils.equals(sectionName, "第2718章 泛黄书卷的来历")) {
		// 	System.out.println("S");
		// }
		sectionName = sectionName.replace("~", "")
				.replace("：", "")
				.replace("雪梨", "")
				.replace("厮杀的风暴（大", "")
				.replace("十百", "百")
				.replace("第第", "第")
				.replace("第牛", "第")
				.replace("第要", "第")
				.replace("第谈", "第")
				.replace("第之", "第")
				.replace("第章", "章")
				.replace("儿6章", "章")
				.replace("到414章", "414章")
				.replace("攻5章", "5章")
				.replace("④章", "章")
				.replace("将章", "章")
				.replace("在章", "章")
				.replace("明章", "章")
				.replace("彪章", "章")
				.replace("土章", "章")
				.replace("章封", "章")
				.replace("半卷", "")
				.replace("土179章", "179章")
				.replace("黄5章", "5章")
				.replace("负章", "章")
				.replace("碎74章", "74章")
				.replace("穆07章", "07章")
				.replace("海09章", "09章")
				.replace("章章", "章")
				.replace("妖章", "章")
				.replace(".章", "章")
				.replace("．章", "章")
				.replace("你章", "章")
				.replace("0天319章", "0319章")
				.replace("小86章", "86章")
				.replace("章88章", "88章")
				.replace("学31章", "31章")
				.replace("第1第", "第1")
				.replace("第王卷", "第五卷")
				.replace("莫千", "")
				.replace("-", "")
				.replace("？", "")
				.replace("·", "");
		if (StringUtils.isBlank(sectionName)) {
			return;
		}
		Pattern chapterPattern = Pattern.compile("第(.*?)章 ");
		Pattern rollPattern = Pattern.compile("第(.*?)卷 ");
		Matcher rollMatcher = rollPattern.matcher(sectionName);
		if (rollMatcher.find() && sectionName.indexOf("卷") < 5) {
			String group = rollMatcher.group(1);
			group = transformNumberCapsToLaw(group);
			sectionName = sectionName.replace("第" + group + "卷", "");
			int index = transformNumber(group);
			if (index != -1) {
				bookSection.setRollIndex(index);
				int i = sectionName.indexOf("第");
				if (i != -1) {
					String rollTitle = sectionName.substring(0, i);
					bookSection.setRollTitle(rollTitle);
					sectionName = sectionName.replace(rollTitle, "").replace(" ", "");
				}
			}
		}
		Matcher chapterMatcher = chapterPattern.matcher(sectionName);
		if (chapterMatcher.find()) {
			String group = chapterMatcher.group(1);
			group = transformNumberCapsToLaw(group);
			int index = transformNumber(group);
			if (index != -1) {
				bookSection.setChapterIndex(index);
				sectionName = sectionName.replace("第" + group + "章", "").replace(" ", "");
				bookSection.setChapterTitle(sectionName);
			}
		}
	}

	private static int transformNumber(String formatNumber) {
		if (StringUtils.isBlank(formatNumber)) {
			return -1;
		}
		if (StringUtils.containsAny(formatNumber, "万", "千", "百", "十", "九", "八", "七", "六", "五", "四", "三", "二", "一", "零")) {
			return FormatUtils.convertToLowerCaseNumber(formatNumber.replace("第", "").replace("章", ""));
		}
		return Integer.parseInt(CommonUtils.trimBlankChar(formatNumber));
	}

	private static String transformNumberCapsToLaw(String numberStr) {
		if (StringUtils.isBlank(numberStr)) {
			return null;
		}
		return numberStr.replace("壹", "一")
				.replace("贰", "二")
				.replace("叁", "三")
				.replace("肆", "四")
				.replace("伍", "五")
				.replace("陆", "六")
				.replace("柒", "七")
				.replace("捌", "八")
				.replace("玖", "九")
				.replace("拾", "十")
				.replace("佰", "百")
				.replace("仟", "千");
	}

}
