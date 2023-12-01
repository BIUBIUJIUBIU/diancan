/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ycc.diancan.definition.spider.FreeNovel;
import com.ycc.diancan.mapper.FreeNovelMapper;
import com.ycc.diancan.service.FreeNovelService;
import com.ycc.diancan.service.SpiderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
	@Override
	public void startSpider() {

	}
}
