/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ycc.diancan.definition.User;
import com.ycc.diancan.mapper.UserMapper;
import com.ycc.diancan.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * UserServiceImpl.
 *
 * @author ycc
 * @date 2023-11-28 10:52:27
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

	private final UserMapper userMapper;
	@Override
	public void insertUser(User user) {
		System.out.println("##### 开始执行插入数据操作....");
		int result = this.userMapper.insert(user);
		if (result > 0) {
			System.out.println("用户 " + user.getUsername() + " 成功插入到数据库中。");
		} else {
			System.out.println("插入用户失败。");
		}
	}
}
