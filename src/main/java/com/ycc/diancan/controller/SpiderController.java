/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.controller;

import com.ycc.diancan.result.Message;
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

	private final WanShuWangService wanShuWangService;
	private final ZeiShuBaService zeiShuBaService;

	@GetMapping("/start/wan")
	private Message<Void> startWanShu() {
		this.wanShuWangService.startSpider();
		log.info("start spider ....");
		return Message.success();
	}

	@GetMapping("/start/zei")
	private Message<Void> startZeiShu() {
		this.zeiShuBaService.startSpider();
		log.info("start spider ....");
		return Message.success();
	}

}
