/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.controller;

import com.google.common.collect.Lists;
import com.ycc.diancan.result.Message;
import com.ycc.diancan.result.MessageCode;
import com.ycc.diancan.service.BaShiShuKuService;
import com.ycc.diancan.service.FreeNovelService;
import com.ycc.diancan.service.MaYiYueDuService;
import com.ycc.diancan.service.QianBiService;
import com.ycc.diancan.service.SevenZBookService;
import com.ycc.diancan.service.ShuHuangBuLuoService;
import com.ycc.diancan.service.WanShuWangService;
import com.ycc.diancan.service.ZeiShuBaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * SearchController.
 *
 * @author ycc
 * @date 2023-12-12 09:08:46
 */
@Slf4j
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

	private final ZeiShuBaService zeiShuBaService;
	private final FreeNovelService freeNovelService;
	private final MaYiYueDuService maYiYueDuService;
	private final WanShuWangService wanShuWangService;
	private final BaShiShuKuService baShiShuKuService;
	private final ShuHuangBuLuoService shuHuangBuLuoService;
	private final SevenZBookService sevenZBookService;
	private final QianBiService qianBiService;


	@GetMapping("/title")
	private Message<List<Object>> searchByTitle(String title) {
		if (StringUtils.isBlank(title)) {
			return Message.fail(MessageCode.VALIDATION_ERROR_CODE, "标题不能为空！");
		}
		List<Object> result = Lists.newArrayList();
		result.addAll(this.zeiShuBaService.searchByTitle(title));
		result.addAll(this.freeNovelService.searchByTitle(title));
		result.addAll(this.maYiYueDuService.searchByTitle(title));
		result.addAll(this.wanShuWangService.searchByTitle(title));
		result.addAll(this.baShiShuKuService.searchByTitle(title));
		result.addAll(this.shuHuangBuLuoService.searchByTitle(title));
		result.addAll(this.sevenZBookService.searchByTitle(title));
		result.addAll(this.qianBiService.searchByTitle(title));
		if (CollectionUtils.isEmpty(result)) {
			return Message.fail(MessageCode.NOT_FOUND_CODE, "查询结果为空！");
		}
		return Message.success(result);
	}

	@GetMapping("/author")
	private Message<List<Object>> searchByAuthor(String author) {
		if (StringUtils.isBlank(author)) {
			return Message.fail(MessageCode.VALIDATION_ERROR_CODE, "作者不能为空！");
		}
		List<Object> result = Lists.newArrayList();
		result.addAll(this.zeiShuBaService.searchByAuthor(author));
		result.addAll(this.freeNovelService.searchByAuthor(author));
		result.addAll(this.maYiYueDuService.searchByAuthor(author));
		result.addAll(this.wanShuWangService.searchByAuthor(author));
		result.addAll(this.baShiShuKuService.searchByAuthor(author));
		result.addAll(this.shuHuangBuLuoService.searchByAuthor(author));
		result.addAll(this.sevenZBookService.searchByAuthor(author));
		result.addAll(this.qianBiService.searchByAuthor(author));
		if (CollectionUtils.isEmpty(result)) {
			return Message.fail(MessageCode.NOT_FOUND_CODE, "查询结果为空！");
		}
		return Message.success(result);
	}

}
