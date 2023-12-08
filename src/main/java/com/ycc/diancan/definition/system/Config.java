/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.definition.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.ycc.diancan.definition.BaseEntity;
import com.ycc.diancan.util.JsonUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.util.Objects;

/**
 * Config.
 *
 * @author ycc
 * @date 2023-12-07 20:28:08
 */
@Slf4j
@Data
@TableName("config")
@Table(name = "config")
public class Config extends BaseEntity {
	@Column(name = "name", isNull = false)
	private String name;
	@Column(name = "type", isNull = false)
	private String type;
	@Column(name = "value", type = "text")
	private String value;
	@Column(name = "remark", type = "mediumtext")
	private String remark;

	public Config() {
	}

	public Config(String name, Object value) {
		this.name = name;
		if (value != null) {
			this.type = value.getClass().getName();
			this.value = JsonUtils.convertBasicTypeOrObject2String(value);
		} else {
			this.type = "";
		}
	}

	public void setValue(Object value) {
		if (value != null) {
			this.type = value.getClass().getName();
			this.value = JsonUtils.convertBasicTypeOrObject2String(value);
		} else {
			this.type = "";
			this.value = null;
		}
	}

	@JsonIgnore
	public Object getValue() {
		if (StringUtils.isAnyBlank(this.type, this.value)) {
			return null;
		}
		try {
			Class<?> realClazz = Class.forName(this.type);
			return JsonUtils.convertString2Java(this.value, realClazz);
		} catch (ClassNotFoundException | ParseException | JsonProcessingException e) {
			log.error("config value convert error, type:{}, valueString:{}", this.type, this.value, e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getGenericValue() {
		return (T) getValue();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Config config = (Config) o;
		return Objects.equals(getId(), config.getId()) &&
				Objects.equals(getName(), config.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getName());
	}

}
