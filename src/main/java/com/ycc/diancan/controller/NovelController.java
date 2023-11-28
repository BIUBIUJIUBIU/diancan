package com.ycc.diancan.controller;

import com.ycc.diancan.definition.Novel;
import com.ycc.diancan.result.Message;
import com.ycc.diancan.service.NovelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/novel")
@RequiredArgsConstructor
public class NovelController {


	private final NovelService novelService;

	@GetMapping("/save")
	public Message<Novel> save() {
		Novel novel = new Novel();
		novel.setId(UUID.randomUUID().toString());
		novel.setName("张某某");
		this.novelService.save(novel);

		return Message.success(novel);
	}

	@GetMapping("/list")
	public List<Novel> getAllUsers() {
		return this.novelService.list();
	}
	@GetMapping("/hello")
	public Object hello() {
		return "hello";
	}



}
