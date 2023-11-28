package com.ycc.diancan.controller;

import com.ycc.diancan.definition.Novel;
import com.ycc.diancan.result.Message;
import com.ycc.diancan.service.NovelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/test")
public class TestController {
	@GetMapping("/hello")
	public Object hello() {
		return "hello";
	}



}
