/*
 * Copyright 2004-2023 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package com.ycc.diancan.config;

import com.ycc.diancan.util.EnvironmentHelper;
import com.ycc.diancan.util.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.LinkedList;
import java.util.List;

/**
 * DianCanConfiguration.
 *
 * @author ycc
 * @date 2023-11-29 16:52:22
 */
@EnableAsync(proxyTargetClass = true)
@SpringBootConfiguration
@EnableConfigurationProperties({MultipartProperties.class, WebProperties.class})
@AutoConfigureBefore(MessageSourceAutoConfiguration.class)
@RequiredArgsConstructor
public class DianCanConfiguration {

	private final Environment environment;

	@Bean
	MessageSource messageSource() {
		EnvironmentHelper helper = new EnvironmentHelper(this.environment);
		List<String> messages = new LinkedList<>();
		String messagePath = helper.getProperty("message.i18n.diancan");
		if (messagePath != null) {
			messages.add(messagePath);
		}
		String globalMessagePath = helper.getProperty("message.i18n.global", "classpath:i18n/messages");
		messages.add(globalMessagePath);
		String[] messageArray = messages.toArray(new String[0]);
		CollectionUtils.reverseArray(messageArray);
		MessageUtils.initDefaultMessageResources(messageArray);
		return MessageUtils.getDefaultMessageSource();
	}
}
