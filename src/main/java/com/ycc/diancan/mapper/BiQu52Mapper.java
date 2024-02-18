/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ycc.diancan.definition.spider.BiQu52;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * BiQU52Mapper.
 *
 * @author ycc
 * @date 2023-12-01 11:30:48
 */
@Mapper
public interface BiQu52Mapper extends BaseMapper<BiQu52> {

	default List<BiQu52> selectByTitleWithWrapper(String title, String author, String detailSourceUrl) {
		QueryWrapper<BiQu52> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("title", title).eq("author", author).eq("detail_source_url", detailSourceUrl);
		return selectList(queryWrapper);
	}

	default List<BiQu52> selectByTitle(String title) {
		QueryWrapper<BiQu52> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("title", title);
		return selectList(queryWrapper);
	}

	default List<BiQu52> selectByAuthor(String author) {
		QueryWrapper<BiQu52> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("author", author);
		return selectList(queryWrapper);
	}
}
