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
 * WanShuWang.
 *
 * @author ycc
 * @date 2023-11-29 09:57:06
 */
@Data
@TableName("wan_shu_wang")
@Table(name = "wan_shu_wang")
public class WanShuWang extends BaseSpiderEntity {
	@Column(name = "wan_shu_novel_type", comment = "小说类别", type = "varchar", length = 50)
	private String wanShuNovelType;
}
