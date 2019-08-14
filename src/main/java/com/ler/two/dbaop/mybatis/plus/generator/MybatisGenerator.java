package com.ler.two.dbaop.mybatis.plus.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * 该文件要放在单模块的包内，多模块可以生成后自己移动
 *
 * @author lww
 * @date 2019-04-12 6:53 PM
 */
public class MybatisGenerator {

	/**
	 * 需要生成的表
	 */
	private static String[] tableNames = {"user_order"};

	/**
	 * 生成配置，哪一个不需要生成就设置为false
	 */
	@Data
	private static class Cfg {
		private boolean needToGenDomain = true;
		private boolean needToGenDao = true;
		private boolean needToGenMapperXml = true;
		private boolean needToGenService = true;
		private boolean needToGenController = true;
	}


	/**
	 * 作者
	 */
	private static final String AUTHOR = "lww";
	/**
	 * 顶级包名
	 */
	private static final String ROOT_PACKAGE_NAME = "com.ler.two.dbaop";


	/**
	 * 数据库相关配置
	 */
	//private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/demo1?useUnicode=true&useSSL=false&characterEncoding=utf8";
	private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/demo2?useUnicode=true&useSSL=false&characterEncoding=utf8";
	private static final String USER_NAME = "root";
	private static final String PASSWORD = "adminadmin";


	public static void main(String[] args) {
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();

		String substring = ROOT_PACKAGE_NAME.substring(ROOT_PACKAGE_NAME.lastIndexOf(".") + 1);

		String[] split = path.split(substring);
		if (split.length <= 0) {
			System.err.println("split = " + split);
			System.err.println("顶级包名配置错误，顶级包名的最后一个文件夹应该是项目名称！");
		}

		String projectRoot = split[0] + substring;
		// 代码生成器
		ExtendedAutoGenerator mpg = new ExtendedAutoGenerator();
		// 全局配置
		final GlobalConfig gc = new GlobalConfig();
		gc.setOutputDir(projectRoot + "/src/main/java");
		gc.setFileOverride(true);
		// 不需要ActiveRecord特性的请改为false AR特性
		gc.setActiveRecord(false);
		// 开启 swagger2 模式
		gc.setSwagger2(true);
		// XML 二级缓存
		gc.setEnableCache(false);
		// XML ResultMap
		gc.setBaseResultMap(false);
		// XML columList
		gc.setBaseColumnList(false);
		gc.setAuthor(AUTHOR);
		gc.setOpen(false);
		// 设置日期类型为Date，否则为LocalDate
		gc.setDateType(DateType.ONLY_DATE);
		// 自定义文件命名，注意 %s 会自动填充表实体属性！
		gc.setMapperName("%sDao");
		gc.setXmlName("%sDao");
		gc.setServiceName("%sService");
		gc.setServiceImplName("%sServiceImpl");
		gc.setControllerName("%sController");

		mpg.setGlobalConfig(gc);

		// 数据源配置
		DataSourceConfig dsc = new DataSourceConfig();
		dsc.setDbType(DbType.MYSQL);
		// dsc.setSchemaName("public");
		dsc.setUrl(DB_URL);
		dsc.setUsername(USER_NAME);
		dsc.setPassword(PASSWORD);
		dsc.setDriverName("com.mysql.jdbc.Driver");

		dsc.setTypeConvert(new MySqlTypeConvert() {
			// 自定义数据库表字段类型转换【可选】
			@Override
			public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
				System.out.println("转换类型：" + fieldType);
				// 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
				if ((fieldType.toLowerCase()).contains("tinyint(1)")) {
					return DbColumnType.INTEGER;
				}
				return super.processTypeConvert(gc, fieldType);
			}
		});
		mpg.setDataSource(dsc);

		// 包配置
		PackageConfig pc = new PackageConfig();
		pc.setModuleName("");
		pc.setParent(ROOT_PACKAGE_NAME);
		pc.setMapper("dao");
		pc.setEntity("domain");
		pc.setService("service");
		pc.setServiceImpl("service.impl");
		pc.setXml("mapper");
		pc.setController("controller");
		mpg.setPackageInfo(pc);

		// 自定义配置
		InjectionConfig injectionConfig = new InjectionConfig() {
			@Override
			public void initMap() {
				// to do nothing
			}
		};
		List<FileOutConfig> focList = new ArrayList<>();
		focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				// 自定义输入文件名称
				return projectRoot + "/src/main/resources/mapper/" + tableInfo.getEntityName() + "Dao" + StringPool.DOT_XML;
			}
		});
		injectionConfig.setFileOutConfigList(focList);
		mpg.setCfg(injectionConfig);
		TemplateConfig templateConfig = new TemplateConfig().setXml(null);
		mpg.setTemplate(templateConfig);

		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		strategy.setNaming(NamingStrategy.underline_to_camel);
		strategy.setColumnNaming(NamingStrategy.underline_to_camel);
		// 自定义 mapper 父类
		strategy.setSuperMapperClass("com.baomidou.mybatisplus.core.mapper.BaseMapper");
		//是否为lombok模型
		strategy.setEntityLombokModel(true);
		strategy.setRestControllerStyle(true);
		strategy.setInclude(tableNames);
		//是否生成实体时，生成字段注解
		strategy.entityTableFieldAnnotationEnable(true);
		mpg.setStrategy(strategy);

		AbstractTemplateEngine templateEngine = new FreemarkerTemplateEngine();
		mpg.setTemplateEngine(templateEngine);
		// 忽略service跟controller生成，把mpg.execute()方法拆解开自行定制
		//mpg.execute();
		System.out.println("==========================准备生成文件...==========================");
		ConfigBuilder configBuilder = new ConfigBuilder(pc, dsc, strategy, templateConfig, gc);
		configBuilder.setInjectionConfig(injectionConfig);
		List<TableInfo> tableInfoList = configBuilder.getTableInfoList();

		Cfg cfg = new Cfg();
		for (TableInfo tableInfo : tableInfoList) {
			if (!cfg.isNeedToGenDomain()) {
				tableInfo.setEntityName(null);
			}
			if (!cfg.isNeedToGenMapperXml()) {
				tableInfo.setXmlName(null);
			}
			if (!cfg.isNeedToGenDao()) {
				tableInfo.setMapperName(null);
			}
			if (!cfg.isNeedToGenService()) {
				tableInfo.setServiceName(null);
				tableInfo.setServiceImplName(null);
			}
			if (!cfg.isNeedToGenController()) {
				tableInfo.setControllerName(null);
			}
		}
		// 模板引擎初始化执行文件输出
		templateEngine.init(mpg.pretreatmentConfigBuilder(configBuilder)).mkdirs().batchOutput().open();
		System.out.println("==========================文件生成完成！！！==========================");
	}

	private static class ExtendedAutoGenerator extends AutoGenerator {
		@Override
		public ConfigBuilder pretreatmentConfigBuilder(ConfigBuilder config) {
			return super.pretreatmentConfigBuilder(config);
		}
	}
}
