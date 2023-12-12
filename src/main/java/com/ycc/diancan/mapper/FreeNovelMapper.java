/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ycc.diancan.definition.spider.FreeNovel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * FreeNovelMapper.
 *
 * @author ycc
 * @date 2023-12-01 11:30:48
 */
@Mapper
public interface FreeNovelMapper extends BaseMapper<FreeNovel> {
	default List<FreeNovel> selectByTitleWithWrapper(String title, String author) {
		QueryWrapper<FreeNovel> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("title", title).eq("author", author);
		return selectList(queryWrapper);
	}

	default List<FreeNovel> selectByTitle(String title) {
		QueryWrapper<FreeNovel> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("title", title);
		return selectList(queryWrapper);
	}

	default List<FreeNovel> selectByAuthor(String author) {
		QueryWrapper<FreeNovel> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("author", author);
		return selectList(queryWrapper);
	}

}
