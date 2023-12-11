/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ycc.diancan.definition.system.Config;
import com.ycc.diancan.exception.ValidationException;
import org.apache.ibatis.annotations.Mapper;

/**
 * ConfigMapper.
 *
 * @author ycc
 * @date 2023-12-07 20:34:31
 */
@Mapper
public interface ConfigMapper extends BaseMapper<Config> {
	default Config getByName(String name) {
		QueryWrapper<Config> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("name", name);
		return selectOne(queryWrapper);
	}

	default void deleteByName(String name) {
		QueryWrapper<Config> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("name", name);
		int delete = delete(queryWrapper);
		if (delete == 0) {
			throw new ValidationException("删除失败");
		}
	}
}
