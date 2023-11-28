package com.ycc.diancan.controller;

import com.ycc.diancan.definition.User;
import com.ycc.diancan.result.Message;
import com.ycc.diancan.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/save")
	public Message<User> save() {
		User user = new User();
		user.setAge(18);
		// user.setAddress("北京王府井大街");
		user.setUsername("张某某");
		// user.setEmail("wwww@163,com");
		this.userService.insertUser(user);

		return Message.success(user);
	}
	@GetMapping("/list")
	public List<User> getAllUsers() {
		return this.userService.list();
	}


}
