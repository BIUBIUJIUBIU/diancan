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
import lombok.EqualsAndHashCode;

/**
 * QianBiNovel.
 *
 * @author ycc
 * @date 2023-12-11 15:59:08
 */
@Data
@TableName("qian_bi")
@Table(name = "qian_bi")
@EqualsAndHashCode(callSuper = true)
public class QianBi extends BaseSpiderEntity {
	@Column(name = "qian_bi_type", comment = "小说类别", type = "varchar", length = 50)
	private String qianBiType;
	@Column(name = "contents", comment = "小说内容(段落JSON)", type = "mediumtext")
	private String contents;
}


