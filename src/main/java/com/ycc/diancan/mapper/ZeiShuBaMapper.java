/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ycc.diancan.definition.spider.ZeiShuBa;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ZeiShuBaMapper.
 *
 * @author ycc
 * @date 2023-11-28 10:54:28
 */
@Mapper
public interface ZeiShuBaMapper extends BaseMapper<ZeiShuBa> {
	default List<ZeiShuBa> selectByTitleWithWrapper(String title) {
		QueryWrapper<ZeiShuBa> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("title", title);
		return selectList(queryWrapper);
	}

}
