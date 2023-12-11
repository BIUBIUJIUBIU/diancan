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
 * SevenZBook.
 *
 * @author ycc
 * @date 2023-12-08 15:08:45
 */
@Data
@TableName("seven_z_book")
@Table(name = "seven_z_book")
public class SevenZBook extends BaseSpiderEntity {
	@Column(name = "seven_z_book_type", comment = "小说类别", type = "varchar", length = 50)
	private String sevenZBookType;
	@Column(name = "contents", comment = "小说内容(段落JSON)", type = "mediumtext")
	private String contents;

}
