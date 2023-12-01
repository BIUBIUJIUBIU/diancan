/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ycc.diancan.definition.spider.ShuHuangBuLuo;

import java.util.List;

/**
 * ShuHuangBuLuoMapper.
 *
 * @author ycc
 * @date 2023-11-30 16:35:08
 */
public interface ShuHuangBuLuoMapper extends BaseMapper<ShuHuangBuLuo> {
	default List<ShuHuangBuLuo> selectByTitleWithWrapper(String title) {
		QueryWrapper<ShuHuangBuLuo> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("title", title);
		return selectList(queryWrapper);
	}
}
