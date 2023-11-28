package com.ycc.diancan.config.mybatis;


// @Configuration
// @ComponentScan(basePackages = {"com.gitee.sunchenbin.mybatis.actable.manager.*"})
public class DataSourceConfig {

	// @Value("${spring.datasource.driver-class-name}")
	// private String driver;
	//
	// @Value("${spring.datasource.url}")
	// private String url;
	//
	// @Value("${spring.datasource.username}")
	// private String username;
	//
	// @Value("${spring.datasource.password}")
	// private String password;
	//
	// @Bean
	// public PropertiesFactoryBean configProperties() throws IOException {
	// 	PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
	// 	PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
	// 	propertiesFactoryBean.setLocations(resolver.getResources("classpath*:application.yml"));
	// 	return propertiesFactoryBean;
	// }
	//
	// @Bean
	// public DruidDataSource dataSource() {
	// 	DruidDataSource dataSource = new DruidDataSource();
	// 	dataSource.setDriverClassName(this.driver);
	// 	dataSource.setUrl(this.url);
	// 	dataSource.setUsername(this.username);
	// 	dataSource.setPassword(this.password);
	// 	dataSource.setMaxActive(30);
	// 	dataSource.setInitialSize(10);
	// 	dataSource.setValidationQuery("SELECT 1");
	// 	dataSource.setTestOnBorrow(true);
	// 	return dataSource;
	// }
	//
	// @Bean
	// public DataSourceTransactionManager dataSourceTransactionManager() {
	// 	DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
	// 	dataSourceTransactionManager.setDataSource(dataSource());
	// 	return dataSourceTransactionManager;
	// }
	//
	// @Bean
	// public SqlSessionFactoryBean sqlSessionFactory() throws Exception {
	// 	SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
	// 	sqlSessionFactoryBean.setDataSource(dataSource());
	// 	PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
	// 	sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:com/gitee/sunchenbin/mybatis/actable/mapping/*/*.xml"));
	// 	sqlSessionFactoryBean.setTypeAliasesPackage("com.ycc.diancan.definition.*");
	// 	return sqlSessionFactoryBean;
	// }
}
