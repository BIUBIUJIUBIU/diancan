/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.definition;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.IsKey;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import lombok.Data;

/**
 * Novel.
 *
 * @author ycc
 * @date 2023-11-28 14:40:50
 */
@Data
@Table(name = "novel")
@TableName(value = "novel")
public class Novel {
	@TableId(value = "id", type = IdType.ASSIGN_UUID)
	@IsKey
	@Column(name = "id", comment = "主键")
	private String id;

	@TableField(value = "name")
	@Column(name = "name", comment = "名称", isNull = false)
	private String name;

	@TableField(value = "create_time")
	@Column(name = "create_time", comment = "创建时间")
	private String creatTime;

	@Column(name = "update_time", comment = "修改时间")
	private String updateTime;
}
