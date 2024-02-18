/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ycc.diancan.definition.system.Config;

/**
 * ConfigService.
 *
 * @author ycc
 * @date 2023-12-07 20:23:59
 */
public interface ConfigService extends IService<Config> {

	Object getConfig(String name);

	<T> T getConfig(String key, Class<T> type);

	<T> T getConfig(String key, Class<T> type, T defaultValue);

	Object setConfig(String key, Object value);

	Object setConfig(String key, Object value, String remark);

	void removeConfig(String key);
}
