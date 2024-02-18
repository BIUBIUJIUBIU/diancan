/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ycc.diancan.definition.system.Config;
import com.ycc.diancan.mapper.ConfigMapper;
import com.ycc.diancan.service.ConfigService;
import com.ycc.diancan.util.UUIDUtils;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Optional;

/**
 * ConfigServiceImpl.
 *
 * @author ycc
 * @date 2023-12-07 20:24:27
 */
@Service
@RequiredArgsConstructor
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {
	/**
	 * CACHE_NAME.
	 */
	public static final String CACHE_NAME = "tk_config";
	private final CacheManager cacheManager;
	private final ConfigMapper configMapper;
	private Cache cache;

	@PostConstruct
	public void init() {
		this.cache = Optional.ofNullable(this.cacheManager.getCache(CACHE_NAME))
				.orElseThrow(() -> new RuntimeException(CACHE_NAME + " cache not found"));
		this.cache.clear();
	}

	@Override
	public Object getConfig(String name) {
		Config config = loadCachedConfig(name);
		return config == null ? null : config.getValue();
	}

	@Override
	public <T> T getConfig(String key, Class<T> type) {
		return type.cast(getConfig(key));
	}

	@Override
	public <T> T getConfig(String key, Class<T> type, T defaultValue) {
		T value = type.cast(getConfig(key));
		return value == null ? defaultValue : value;
	}

	@Override
	public Object setConfig(String key, Object value) {
		Config config = this.configMapper.getByName(key);
		if (config == null) {
			config = new Config(key, value);
			config.setId(UUIDUtils.generateUUID());
		} else {
			config.setValue(value);
		}
		this.cache.evict(key);
		this.configMapper.insert(config);
		return value;
	}

	@Override
	public Object setConfig(String key, Object value, String remark) {
		Config config = this.configMapper.getByName(key);
		if (config == null) {
			config = new Config(key, value);
			config.setId(UUIDUtils.generateUUID());
		} else {
			config.setValue(value);
		}
		config.setRemark(remark);
		this.cache.evict(key);
		this.configMapper.insert(config);
		return value;
	}

	@Override
	@Transactional
	public void removeConfig(String key) {
		this.cache.evict(key);
		this.configMapper.deleteByName(key);
	}


	private Config loadCachedConfig(String name) {
		val valueWrapper = this.cache.get(name);
		if (valueWrapper == null) {
			Config config = this.configMapper.getByName(name);
			this.cache.put(name, config);
			return config;
		}
		return (Config) valueWrapper.get();
	}
}
