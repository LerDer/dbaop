package com.ler.two.dbaop.config.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author lww
 * @date 2019-08-14 10:46
 */
@Configuration
@MapperScan(basePackages = "com.ler.two.dbaop.dao.demo1", sqlSessionTemplateRef = "sqlSessionTemplateDemo1")
public class Demo1MybatisConfig {

	private static final Logger log = LoggerFactory.getLogger(Demo1MybatisConfig.class);

	@Bean("dataSourceDemo1")
	public DataSource dataSourceDemo1() {
		try {
			DruidDataSource dataSource = new DruidDataSource();
			dataSource.setDriverClassName("com.mysql.jdbc.Driver");
			dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/demo1?useUnicode=true&useSSL=false&characterEncoding=utf8");
			dataSource.setUsername("root");
			dataSource.setPassword("adminadmin");

			dataSource.setInitialSize(1);
			dataSource.setMaxActive(20);
			dataSource.setMinIdle(1);
			dataSource.setMaxWait(60_000);
			dataSource.setPoolPreparedStatements(true);
			dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
			dataSource.setTimeBetweenEvictionRunsMillis(60_000);
			dataSource.setMinEvictableIdleTimeMillis(300_000);
			dataSource.setValidationQuery("SELECT 1");
			return dataSource;
		} catch (Throwable throwable) {
			log.error("ex caught", throwable);
			throw new RuntimeException();
		}
	}

	@Bean(name = "sqlSessionFactoryDemo1")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSourceDemo1") DataSource dataSource) throws Exception {
		MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource);
		factoryBean.setVfs(SpringBootVFS.class);
		factoryBean.setTypeAliasesPackage("com.ler.two.dbaop.domain");

		Resource[] mapperResources = new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*Dao.xml");
		factoryBean.setMapperLocations(mapperResources);

		MybatisConfiguration configuration = new MybatisConfiguration();
		configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
		configuration.setJdbcTypeForNull(JdbcType.NULL);
		configuration.setMapUnderscoreToCamelCase(true);
		configuration.addInterceptor(new PaginationInterceptor());
		configuration.setUseGeneratedKeys(true);
		factoryBean.setConfiguration(configuration);
		return factoryBean.getObject();
	}

	@Bean(name = "sqlSessionTemplateDemo1")
	public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactoryDemo1") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	@Bean(name = "transactionManagerDemo1")
	public PlatformTransactionManager platformTransactionManager(@Qualifier("dataSourceDemo1") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "transactionTemplateDemo1")
	public TransactionTemplate transactionTemplate(@Qualifier("transactionManagerDemo1") PlatformTransactionManager transactionManager) {
		return new TransactionTemplate(transactionManager);
	}

}
