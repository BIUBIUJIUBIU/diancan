package com.ycc.diancan.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ycc.diancan.definition.spider.LongTeng;

import java.util.List;

/**
 * LongTengMapper.
 *
 * @author ycc
 * @date 2023-12-25 2023/12/25
 */
public interface LongTengMapper extends BaseMapper<LongTeng> {
	default List<LongTeng> selectByTitleWithWrapper(String title, String author, String detailSourceUrl) {
		QueryWrapper<LongTeng> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("title", title).eq("author", author).eq("detail_source_url", detailSourceUrl);
		return selectList(queryWrapper);
	}

	default List<LongTeng> selectByTitle(String title) {
		QueryWrapper<LongTeng> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("title", title);
		return selectList(queryWrapper);
	}

	default List<LongTeng> selectByAuthor(String author) {
		QueryWrapper<LongTeng> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("author", author);
		return selectList(queryWrapper);
	}
}
