package com.ycc.diancan.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ycc.diancan.util.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * MyMetaObjectHandler.
 *
 * @author ycc
 * @date 2023-11-28 10:41:43
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
	@Override
	public void insertFill(MetaObject metaObject) {
		this.setFieldValByName("id", UUIDUtils.generateUUID(), metaObject);
		this.setFieldValByName("createTime", new Date(), metaObject);
		this.setFieldValByName("updateTime", new Date(), metaObject);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		this.setFieldValByName("updateTime", new Date(), metaObject);
	}

}
