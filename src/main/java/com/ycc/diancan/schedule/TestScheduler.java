/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * TestScheduler.
 *
 * @author ycc
 * @date 2023-11-28 16:16:02
 */
@Component
public class TestScheduler {
	@Autowired
	private TaskScheduler taskScheduler;

	// 使用Cron表达式，每15秒执行
	@Scheduled(cron = "0/2 * * * * ?")
	public void performTask() {
		// TODO  需要定时任务处理的逻辑
		// System.out.println("Task performed at " + DateUtil.format(new Date(), DatePattern.NORM_DATETIME_FORMAT));
	}
}
