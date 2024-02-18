/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.definition;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import lombok.Data;

import java.util.Date;

/**
 * 模型基础.
 *
 * @author ycc
 * @date 2023-11-28 10:41:43
 */
@Data
public class BaseEntity {
	@TableId(type = IdType.ASSIGN_UUID)
	@Column(comment = "id", isNull = false, length = 32)
	private String id;

	@Column(name = "create_time", comment = "创建时间", isNull = false, type = "timestamp")
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;

	@Column(name = "update_time", comment = "修改时间", isNull = false, type = "timestamp")
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;

	@Column(name = "parent_id", comment = "父对象ID", length = 50)
	private String parentId;

	@Column(name = "version", comment = "版本号", type = "int", defaultValue = "0",  length = 10)
	private int version;

	@Column(name = "trash", comment = "删除标记", defaultValue = "false", length = 10)
	private boolean trash;

	@Column(name = "attributes", comment = "属性", length = 200)
	private String attributes;
}
