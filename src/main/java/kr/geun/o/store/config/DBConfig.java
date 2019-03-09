package kr.geun.o.store.config;

import kr.geun.o.store.config.annotation.Store;
import org.h2.Driver;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/**
 *
 *
 * @author akageun
 */
@Configuration
public class DBConfig implements InitializingBean {

	@Autowired
	@Qualifier(value = DBConfig.ConstName.STORE_BEAN_NM)
	private DataSource storeDataSource;

	@Override
	public void afterPropertiesSet() throws Exception {
		DatabasePopulatorUtils.execute(getScripts("initQuery/createScheme.sql"), storeDataSource);
	}

	private ResourceDatabasePopulator getScripts(String... script) {
		ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
		for (String st : script) {
			resourceDatabasePopulator.addScript(new ClassPathResource(st));
		}

		return resourceDatabasePopulator;
	}

	public static class ConstName {
		public static final String BATCH = "batch";
		public static final String STORE = "store";

		public static final String BATCH_BEAN_NM = BATCH + "DataSourceBean";
		public static final String BATCH_PROP_NM = BATCH + "-db";

		public static final String BATCH_MANAGER = BATCH + "TransactionManager";

		public static final String STORE_BEAN_NM = STORE + "DataSourceBean";
		public static final String STORE_PROP_NM = STORE + "-db";

		public static final String STORE_SQL_SESSION = STORE + "SQLSession";
		public static final String STORE_MANAGER = STORE + "TransactionManager";
	}

	@Configuration
	public static class BatchDB {

		@Autowired
		private Environment env;

		@Primary
		@Bean(name = ConstName.BATCH_BEAN_NM)
		public DataSource dataSource() {

			SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
			dataSource.setDriver(Driver.load());
			dataSource.setUrl(env.getRequiredProperty(String.format("spring.database.%s.url", ConstName.BATCH_PROP_NM)));
			dataSource.setUsername(env.getRequiredProperty(String.format("spring.database.%s.username", ConstName.BATCH_PROP_NM)));
			dataSource.setPassword(env.getRequiredProperty(String.format("spring.database.%s.password", ConstName.BATCH_PROP_NM)));
			return dataSource;
		}

		/**
		 * 트랜젝션 선언
		 *
		 * @return
		 */
		@Bean(name = ConstName.BATCH_MANAGER)
		public DataSourceTransactionManager transactionManager(@Autowired @Qualifier(value = ConstName.BATCH_BEAN_NM) DataSource batchDataSource) {
			return new DataSourceTransactionManager(batchDataSource);
		}
	}

	@Configuration
	@MapperScan(basePackages = "kr.geun", annotationClass = Store.class, sqlSessionFactoryRef = ConstName.STORE_SQL_SESSION)
	public static class StoreDB {

		@Autowired
		private Environment env;

		@Bean(name = ConstName.STORE_BEAN_NM)
		public DataSource dataSource() {

			SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
			dataSource.setDriver(Driver.load());
			dataSource.setUrl(env.getRequiredProperty(String.format("spring.database.%s.url", ConstName.STORE_PROP_NM)));
			dataSource.setUsername(env.getRequiredProperty(String.format("spring.database.%s.username", ConstName.STORE_PROP_NM)));
			dataSource.setPassword(env.getRequiredProperty(String.format("spring.database.%s.password", ConstName.STORE_PROP_NM)));

			return dataSource;
		}

		/**
		 * sqlSessionFactory 선언
		 *
		 * @return
		 * @throws Exception
		 */
		@Bean(name = ConstName.STORE_SQL_SESSION)
		public SqlSessionFactoryBean sqlSessionFactoryBean() throws Exception {
			SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
			sessionFactory.setDataSource(dataSource());

			// mybatis mapper 위치 설정
			sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/**/*.xml"));
			return sessionFactory;
		}

		/**
		 * 트랜젝션 선언
		 *
		 * @return
		 */
		@Bean(name = ConstName.STORE_MANAGER)
		public DataSourceTransactionManager transactionManager(@Autowired @Qualifier(value = ConstName.STORE_BEAN_NM) DataSource storeDataSource) {
			return new DataSourceTransactionManager(storeDataSource);
		}
	}

}
