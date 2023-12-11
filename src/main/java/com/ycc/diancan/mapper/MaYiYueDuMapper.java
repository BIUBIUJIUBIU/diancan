/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ycc.diancan.definition.spider.MaYiYueDu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * MaYiYueDuMapper.
 *
 * @author ycc
 * @date 2023-12-01 11:30:48
 */
@Mapper
public interface MaYiYueDuMapper extends BaseMapper<MaYiYueDu> {

	default List<MaYiYueDu> selectByTitleWithWrapper(String title, String author) {
		QueryWrapper<MaYiYueDu> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("title", title).eq("author", author);
		return selectList(queryWrapper);
	}
}
