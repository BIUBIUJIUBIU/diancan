/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.controller;

import com.ycc.diancan.result.Message;
import com.ycc.diancan.service.BaShiShuKuService;
import com.ycc.diancan.service.FreeNovelService;
import com.ycc.diancan.service.MaYiYueDuService;
import com.ycc.diancan.service.ShuHuangBuLuoService;
import com.ycc.diancan.service.WanShuWangService;
import com.ycc.diancan.service.ZeiShuBaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SpiderController.
 *
 * @author ycc
 * @date 2023-11-29 11:15:06
 */
@Slf4j
@RestController
@RequestMapping("/spider")
@RequiredArgsConstructor
public class SpiderController {

	private final ZeiShuBaService zeiShuBaService;
	private final FreeNovelService freeNovelService;
	private final MaYiYueDuService maYiYueDuService;
	private final WanShuWangService wanShuWangService;
	private final BaShiShuKuService baShiShuKuService;
	private final ShuHuangBuLuoService shuHuangBuLuoService;

	@GetMapping("/start")
	private Message<Void> startWanShu() {
		// this.wanShuWangService.startSpider();
		this.zeiShuBaService.startSpider();
		this.shuHuangBuLuoService.startSpider();
		// this.freeNovelService.startSpider();
		this.baShiShuKuService.startSpider();
		// this.maYiYueDuService.startSpider();
		return Message.success();
	}

}
