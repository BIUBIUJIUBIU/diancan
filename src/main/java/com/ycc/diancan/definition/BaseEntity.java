/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.definition;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

/**
 * 模型基础.
 *
 * @author ycc
 * @date 2023-11-28 10:41:43
 */
public class BaseEntity {
	@TableId(type = IdType.ASSIGN_UUID)// mybatis-plus主键注解
	// @IsKey             // actable主键注解
	// @Column(comment = "ID", isNull = false)// 对应数据库字段，不配置name会直接采用属性名作为字段名comment是注解
	// @TableField(fill = FieldFill.INSERT)
	private String id;

	// @Column(name = "create_time", comment = "创建时间", isNull = false)
	// @TableField(fill = FieldFill.INSERT)
	private Date createTime;

	// @Column(name = "update_time", comment = "修改时间", isNull = false)
	// @TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;

	// @Column(name = "parent_id", comment = "父对象ID")
	private String parentId;

	// @Column(name = "version", comment = "版本号", defaultValue = "0")
	private int version;

	// @Column(name = "trash", comment = "删除标记", defaultValue = "false")
	private boolean trash;
}