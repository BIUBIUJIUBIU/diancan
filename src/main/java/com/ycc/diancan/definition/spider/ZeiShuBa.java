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
 * ZeiShuBa.
 *
 * @author ycc
 * @date 2023-11-29 09:57:06
 */
@Data
@TableName("zei_shu_ba")
@Table(name = "zei_shu_ba")
public class ZeiShuBa extends BaseSpiderEntity {
	@Column(name = "zei_shu_novel_type", comment = "小说类别", length = 50)
	private String zeiShuNovelType;
	@Column(name = "novel_channel", comment = "小说频道", length = 50)
	private String novelChannel;
	@Column(name = "download_urls", comment = "下载地址", length = 200)
	private String downloadUrls;
}
