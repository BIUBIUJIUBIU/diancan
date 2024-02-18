/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ycc.diancan.definition.spider.WanShuWang;

import java.util.List;

/**
 * WanShuWangService.
 *
 * @author ycc
 * @date 2023-11-28 10:52:16
 */
public interface WanShuWangService extends IService<WanShuWang>, SpiderService {

	List<WanShuWang> searchByTitle(String title);

	List<WanShuWang> searchByAuthor(String author);

}
