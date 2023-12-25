/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ycc.diancan.definition.spider.BiQu52;

import java.util.List;

/**
 * BiQu52Service.
 *
 * @author ycc
 * @date 2023-12-01 11:29:31
 */
public interface BiQu52Service extends IService<BiQu52>, SpiderService {

	List<BiQu52> searchByTitle(String title);

	List<BiQu52> searchByAuthor(String author);

}
