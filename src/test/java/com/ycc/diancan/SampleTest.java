package com.ycc.diancan;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SampleTest {

	@Test
	void testSelect() {
		FastAutoGenerator.create(
						"jdbc:mysql://1.94.0.129:3306/diancan?serverTimezone=Asia/Shanghai&characterEncoding=utf-8&allowPublicKeyRetrieval=true&useSSL=false",
						"root", "Scl123456")
				.globalConfig(builder ->
						builder.author("popo") // 设置作者
								.enableSwagger() // 开启 swagger 模式
				)
				.packageConfig(builder ->
						builder.parent("com.ycc") // 设置父包名
								.moduleName("diancan") // 设置父包模块名
				)
				.strategyConfig(builder ->
						builder.addInclude("tb_user", "tb_role", "tb_user_role")
				)
				.execute();
	}

}
