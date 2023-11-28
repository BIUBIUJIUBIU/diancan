/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TestScheduler.
 *
 * @author ycc
 * @date 2023-11-28 16:16:02
 */
@Component
public class TestScheduler {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	// 使用Cron表达式，每15秒执行
	@Scheduled(cron = "0/15 * * * * ?")
	public void performTask() {
		System.out.println("Task performed at " + dateFormat.format(new Date()));
	}
}
