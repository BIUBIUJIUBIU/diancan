/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.definition.spider;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;

/**
 * FreeNovel.
 *
 * @author ycc
 * @date 2023-12-01 11:29:12
 */
@Data
@TableName("free_novel")
@Table(name = "free_novel")
public class FreeNovel extends BaseSpiderEntity {
	@Column(name = "free_novel_type", comment = "小说类别", type = "varchar", length = 50)
	private String freeNovelType;
	@Column(name = "download_urls", comment = "下载地址", length = 200)
	private String downloadUrls;
}
