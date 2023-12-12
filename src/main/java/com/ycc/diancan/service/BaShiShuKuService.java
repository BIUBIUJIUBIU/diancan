/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ycc.diancan.definition.spider.BaShiShuKu;

import java.util.List;

/**
 * FreeNovelService.
 *
 * @author ycc
 * @date 2023-12-01 11:29:31
 */
public interface BaShiShuKuService extends IService<BaShiShuKu>, SpiderService {

	List<BaShiShuKu> searchByTitle(String title);

	List<BaShiShuKu> searchByAuthor(String author);

}
