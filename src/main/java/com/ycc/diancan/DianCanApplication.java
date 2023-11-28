package com.ycc.diancan;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootApplication
@MapperScan("com.ycc.diancan.mapper")
public class DianCanApplication {

	public static void main(String[] args) {
		Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
		Stopwatch stopwatch = Stopwatch.createStarted();
		Runtime runtime = Runtime.getRuntime();
		final NumberFormat format = NumberFormat.getInstance();
		final long maxMemory = runtime.maxMemory();
		final long allocatedMemory = runtime.totalMemory();
		final long freeMemory = runtime.freeMemory();
		final long mb = 1024 * 1024L;
		final String mega = " MB";
		SpringApplication.run(DianCanApplication.class, args);
		log.info(StringUtils.center(" Memory Info ", 60, "="));
		log.info("Free memory: {} ", format.format(freeMemory / mb) + mega);
		log.info("Allocated memory: {} ", format.format(allocatedMemory / mb) + mega);
		log.info("Max memory: {} ", format.format(maxMemory / mb) + mega);
		log.info("Total free memory: {} ", format.format((freeMemory + (maxMemory - allocatedMemory)) / mb) + mega);
		long dual = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
		log.info(StringUtils.center(String.format(" Server startup in [%s] ",
				DurationFormatUtils.formatDuration(dual, "mm'm 'ss's 'SSS'ms' ", false)), 60, '='));
	}

}
