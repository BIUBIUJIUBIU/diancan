/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ycc.diancan.definition.spider.ZeiShuBa;
import com.ycc.diancan.mapper.ZeiShuBaMapper;
import com.ycc.diancan.service.SpiderService;
import com.ycc.diancan.service.ZeiShuBaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

	@Override
	public void startSpider() {
		log.info("start zei shu ba spider....");
	}
}
