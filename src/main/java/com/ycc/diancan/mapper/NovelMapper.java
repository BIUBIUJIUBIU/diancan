/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ycc.diancan.definition.Novel;
import org.apache.ibatis.annotations.Mapper;

/**
 * UserMapper.
 *
 * @author ycc
 * @date 2023-11-28 10:54:28
 */
@Mapper
public interface NovelMapper extends BaseMapper<Novel> {
}
