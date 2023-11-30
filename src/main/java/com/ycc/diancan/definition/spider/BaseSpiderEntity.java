/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.definition.spider;

import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.ycc.diancan.definition.BaseEntity;
import lombok.Data;

/**
 * BaseSpiderEntity.
 *
 * @author ycc
 * @date 2023-11-29 09:57:42
 */
@Data
public class BaseSpiderEntity extends BaseEntity {
	@Column(name = "title", comment = "标题", length = 200)
	private String title;
	@Column(name = "author", comment = "作者", length = 100)
	private String author;
	@Column(name = "description", comment = "描述", type = "mediumtext")
	private String description;
	@Column(name = "source_type", comment = "来源类型", length = 50)
	private String sourceType;
	@Column(name = "detail_source_url", comment = "小说详情页地址", length = 200)
	private String detailSourceUrl;
	@Column(name = "download_source_url", comment = "小说下载地址", length = 200)
	private String downloadSourceUrl;
}
