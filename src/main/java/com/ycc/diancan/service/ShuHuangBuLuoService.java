/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ycc.diancan.definition.spider.ShuHuangBuLuo;

import java.util.List;

/**
 * ShuHuangBuLuoService.
 *
 * @author ycc
 * @date 2023-11-30 16:35:45
 */
public interface ShuHuangBuLuoService extends IService<ShuHuangBuLuo>, SpiderService {

	List<ShuHuangBuLuo> searchByTitle(String title);

	List<ShuHuangBuLuo> searchByAuthor(String author);

}
