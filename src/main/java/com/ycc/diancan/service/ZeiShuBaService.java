/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ycc.diancan.definition.spider.ZeiShuBa;

import java.util.List;

/**
 * ZeiShuBaService.
 *
 * @author ycc
 * @date 2023-11-28 10:52:16
 */
public interface ZeiShuBaService extends IService<ZeiShuBa>, SpiderService {

	List<ZeiShuBa> searchByTitle(String title);

	List<ZeiShuBa> searchByAuthor(String author);


}
