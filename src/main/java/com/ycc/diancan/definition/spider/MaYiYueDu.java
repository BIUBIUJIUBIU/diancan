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
 * BaYiZhongWen.
 *
 * @author ycc
 * @date 2023-12-06 14:31:55
 */
@Data
@TableName("ma_yi_yue_du")
@Table(name = "ma_yi_yue_du")
public class MaYiYueDu extends BaseSpiderEntity {
	@Column(name = "ma_yi_yue_du_type", comment = "小说类别", type = "varchar", length = 50)
	private String maYiYueDuType;
}
