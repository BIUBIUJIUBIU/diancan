/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ycc.diancan.definition.Novel;
import com.ycc.diancan.mapper.NovelMapper;
import com.ycc.diancan.service.NovelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * UserServiceImpl.
 *
 * @author ycc
 * @date 2023-11-28 10:52:27
 */
@Service
@RequiredArgsConstructor
public class NovelServiceImpl extends ServiceImpl<NovelMapper, Novel> implements NovelService {

}