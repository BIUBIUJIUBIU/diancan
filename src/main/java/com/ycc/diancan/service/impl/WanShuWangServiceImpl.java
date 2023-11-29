/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ycc.diancan.definition.spider.WanShuWang;
import com.ycc.diancan.mapper.WanShuWangMapper;
import com.ycc.diancan.service.WanShuWangService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * WanShuWangServiceImpl.
 *
 * @author ycc
 * @date 2023-11-28 10:52:27
 */
@Service
@RequiredArgsConstructor
public class WanShuWangServiceImpl extends ServiceImpl<WanShuWangMapper, WanShuWang> implements WanShuWangService {

}
