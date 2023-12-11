/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ycc.diancan.definition.spider.QianBi;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * BaShiShuKuMapper.
 *
 * @author ycc
 * @date 2023-12-01 11:30:48
 */
@Mapper
public interface QianBiMapper extends BaseMapper<QianBi> {

	default List<QianBi> selectByTitleWithWrapper(String title, String author) {
		QueryWrapper<QianBi> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("title", title).eq("author", author);
		return selectList(queryWrapper);
	}
}
