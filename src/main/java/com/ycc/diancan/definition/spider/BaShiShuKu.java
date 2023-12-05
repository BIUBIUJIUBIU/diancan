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
 * BaShiShuKu(巴士书库).
 *
 * @author ycc
 * @date 2023-12-04 09:38:24
 */
@Data
@TableName("ba_shi_shu_ku")
@Table(name = "ba_shi_shu_ku")
public class BaShiShuKu extends BaseSpiderEntity {
	@Column(name = "ba_shi_shu_ku_novel_type", comment = "小说类别", type = "varchar", length = 50)
	private String baShiShuKuNovelType;
	@Column(name = "download_urls", comment = "下载地址", length = 200)
	private String downloadUrls;
}
