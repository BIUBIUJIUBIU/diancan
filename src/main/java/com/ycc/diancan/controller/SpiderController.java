/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SpiderController.
 *
 * @author ycc
 * @date 2023-11-29 11:15:06
 */
@RestController
@RequestMapping("/spider")
public class SpiderController {

	@GetMapping("/start/wan")
	private void startWanShu() {

	}

}
